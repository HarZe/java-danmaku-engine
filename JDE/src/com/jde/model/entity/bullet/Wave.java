package com.jde.model.entity.bullet;

import java.util.ArrayList;

import com.jde.audio.Music;
import com.jde.audio.SoundEffect;
import com.jde.model.entity.spawning.Spawnable;
import com.jde.model.entity.spawning.Spawner;
import com.jde.model.physics.Direction;
import com.jde.model.physics.DirectionModifier;
import com.jde.model.physics.Movement;
import com.jde.model.physics.Vertex;

/**
 * This Wave class represents a set of bullets and sub-waves to spawn. It is a
 * two-in-one class, it can act like a bullet spawner or it can manage multiple
 * sub-waves which also spawn bullets (superwave)
 * 
 * @author HarZe (David Serrano)
 */
public class Wave implements Spawnable {

	// Misc

	/**
	 * This method modifies the direction of all the sub-waves and bullets of a
	 * given wave, a given modifier, and a iteration state
	 * 
	 * @param w
	 *            The wave to modify
	 * @param mod
	 *            The direction modifier to apply to the wave
	 * @param step
	 *            Step number of the iteration
	 * @param total
	 *            Total number of iterations
	 */
	public static void modifyWave(Wave w, DirectionModifier mod, int step,
			int total) {
		if (w.isSuperWave()) {
			for (Wave sw : w.getSubWaves()) {
				modifyWave(sw, mod, step, total);
			}
		} else {
			mod.modify(step, total, w.getBullet().getMovement().getDirections()
					.get(0));
		}
	}
	/** Spawner of bullets */
	protected Spawner<Bullet> spawnerBullets;

	/** Spawner of sub-waves */
	protected Spawner<Wave> spawnerWaves;
	/** List of currently working sub-waves */
	protected ArrayList<Wave> spawnedWaves;
	/** Origin vertex point to spawn the bullets */
	protected Vertex spawnOrigin;
	/** Elapsed time since the wave started */
	protected double elapsed = 0;

	// Configuration

	/** Spawned state of the wave */
	protected boolean spawned = false;
	/** Tiem stamp of the moment the wave started */
	protected double timeStarted;
	/** Template bullet of the wave */
	protected Bullet bullet;

	/** Spawn position relative to the origin */
	protected Vertex spawnPoint = new Vertex();
	
	/** List of sub-waves */
	protected ArrayList<Wave> subWaves = new ArrayList<Wave>();
	/** Initial sound effect */
	protected SoundEffect spawnSound = null;

	/** Wave music effect */
	protected Music music = null;
	/** Wave direction modifiers */
	protected ArrayList<DirectionModifier> modifiers = new ArrayList<DirectionModifier>();
	/** Time stamp of the instant the wave spawns */
	protected double spawnTime = 0;
	/** Elapsed time between the spawn and the first bullet spawn */
	protected double timeStart = 0;

	/** Elapsed time between the spawn and the last bullet spawn */
	protected double timeEnd = 0;
	/** Exponent modifier of the time */
	protected double timeExponent = 1;

	/** Number of bullets in the wave */
	protected int bullets = 1;
	/** Number of sub-waves in the wave */
	protected int waves = 1;

	/** Number of times the wave will repeat */
	protected int repeat = 0;

	/** Time between repetitions (they can overlap) */
	protected double interval = 0;

	/** If true, the origin spawn point will be the center of the game board */
	protected boolean absolute = false;

	/**
	 * Composed wave (superwave) constructor
	 * 
	 * @param subWaves
	 *            List of sub-waves
	 * @param modifier
	 *            Direction modifier for the sub-waves
	 */
	public Wave(ArrayList<Wave> subWaves, DirectionModifier modifier) {
		this.modifiers.add(modifier.clone());
		for (Wave w : subWaves)
			this.subWaves.add(w.clone());
		this.bullet = null;
	}

	/**
	 * Simple wave constructor
	 * 
	 * @param bullet
	 *            Template bullet of the wave
	 */
	public Wave(Bullet bullet) {
		this.spawnerBullets = new Spawner<Bullet>(0);
		this.bullet = bullet.clone();
	}

	/**
	 * Set all the positions of the bullets with the spawn position vertex
	 * 
	 * @param bullets
	 *            List of bullets
	 * @return The same list with the bullets already modified
	 */
	public ArrayList<Bullet> applySpawnPosition(ArrayList<Bullet> bullets) {
		// [223, 240] is the center of the game board
		Vertex spawnPos = absolute ? new Vertex(223, 240) : spawnOrigin;

		for (Bullet b : bullets)
			b.getMovement().setPosition(
					b.getMovement().getPosition().add(spawnPos));

		return bullets;
	}

	/**
	 * Returns a copy (not spawned) of the wave
	 */
	@Override
	public Wave clone() {
		Wave w;

		if (bullet != null)
			w = new Wave(bullet.clone());

		else
			w = new Wave(subWaves, modifiers.get(0).clone());

		ArrayList<DirectionModifier> clonMods = new ArrayList<DirectionModifier>();
		for (DirectionModifier d : modifiers)
			clonMods.add(d.clone());
		w.setModifiers(clonMods);

		w.setTimeEnd(timeEnd);
		w.setTimeStart(timeStart);
		w.setTimeExponent(timeExponent);

		w.setBullets(bullets);
		w.setWaves(waves);
		w.setInterval(interval);
		w.setRepeat(repeat);
		w.setAbsolute(absolute);
		w.setSpawnPoint(spawnPoint.clone());

		w.setSpawnTime(spawnTime);
		w.setMusic(music);
		w.setSpawnSound(spawnSound);

		return w;
	}

	/**
	 * This method calculates the modified time based on the time exponent, to
	 * accelerate spawning of bullets
	 * 
	 * @param step
	 *            Step number of the iteration
	 * @param total
	 *            Total number of iterations
	 * @return The modified-by-exponent time stamp
	 */
	protected double curvedTime(int step, int total) {
		double dStep = step;
		double dTotal = total;
		double relativeStep = Math.pow(dStep / dTotal, timeExponent);
		return timeStart + (timeEnd - timeStart) * relativeStep;
	}

	/**
	 * Forwards the wave and get the spawned bullets
	 * 
	 * @param ms
	 *            Milliseconds to forward
	 * @return A list of bullets to spawn
	 */
	public ArrayList<Bullet> forward(double ms) {
		if (!spawned)
			return new ArrayList<Bullet>();

		elapsed += ms;

		// If this wave is simple
		if (!isSuperWave()) {

			// Check repetition in order to add a new set of bullets
			if (repeat != 0 && elapsed > interval) {
				timeStarted += interval;
				elapsed -= interval;
				spawnerBullets.addSpawnables(getBulletWave(timeStarted));

				if (repeat > 0)
					repeat--;
				
				if (spawnSound != null)
					spawnSound.play();
			}

			return applySpawnPosition(spawnerBullets.forward(ms));
		}

		// If the wave is composed
		else {
			ArrayList<Bullet> spawnedBullets = new ArrayList<Bullet>();

			// Check repetition in order to add a new set of sub-waves
			if (repeat != 0 && elapsed > interval) {
				timeStarted += interval;
				elapsed -= interval;
				spawnerWaves.addSpawnables(getWaves(timeStarted));

				if (repeat > 0)
					repeat--;
				
				if (spawnSound != null)
					spawnSound.play();
			}

			// Taking the bullets spawned from the currently active sub-waves
			for (Wave w : spawnedWaves)
				for (Bullet b : w.forward(ms))
					spawnedBullets.add(b);

			// Starting the new sub-waves
			for (Wave sw : spawnerWaves.forward(ms)) {
				spawnedWaves.add(sw);
				for (Bullet b : sw.start(timeStarted + elapsed,
						spawnPoint.clone()))
					spawnedBullets.add(b);
			}

			return applySpawnPosition(spawnedBullets);
		}
	}

	/**
	 * @return The wave's bullet template
	 */
	public Bullet getBullet() {
		return bullet;
	}

	public int getBullets() {
		return bullets;
	}

	/**
	 * This method generates all the bullets of the wave using their modifiers
	 * 
	 * @param timeStamp
	 *            Time stamp of the instant the wave is called
	 * @return A list of bullets to spawn
	 */
	protected ArrayList<Bullet> getBulletWave(double timeStamp) {

		ArrayList<Bullet> bulletList = new ArrayList<Bullet>();
		double time = timeStamp;

		for (int i = 0; i < bullets; i++) {
			Bullet current = bullet.clone();
			Movement currentMovement = current.getMovement();

			currentMovement.setPosition(spawnPoint.clone()); // TODO: check if
																// necessary
			time = timeStamp + curvedTime(i, bullets - 1);
			current.setSpawnTime(time);

			ArrayList<Direction> bulletDirs = current.getMovement()
					.getDirections();
			for (int j = 0; j < Math.min(modifiers.size(), bulletDirs.size()); j++)
				modifiers.get(j).modify(i, bullets, bulletDirs.get(j));

			currentMovement.applyRandomization();
			bulletList.add(current);
		}

		return bulletList;
	}

	public double getInterval() {
		return interval;
	}

	public ArrayList<DirectionModifier> getModifiers() {
		return modifiers;
	}

	public Music getMusic() {
		return music;
	}

	public int getRepeat() {
		return repeat;
	}

	public Vertex getSpawnPoint() {
		return spawnPoint;
	}

	public SoundEffect getSpawnSound() {
		return spawnSound;
	}

	public double getSpawnTime() {
		return spawnTime;
	}

	public ArrayList<Wave> getSubWaves() {
		return subWaves;
	}

	public double getTimeEnd() {
		return timeEnd;
	}

	public double getTimeExponent() {
		return timeExponent;
	}

	public double getTimeStart() {
		return timeStart;
	}

	public int getWaves() {
		return waves;
	}

	/**
	 * This method generates all the sub-waves of the wave using their modifiers
	 * 
	 * @param timeStamp
	 *            Time stamp of the instant the wave is called
	 * @return A list of new sub-waves to spawn
	 */
	protected ArrayList<Wave> getWaves(double timeStamp) {

		ArrayList<Wave> newWaves = new ArrayList<Wave>();
		double time = timeStamp;
		int total = subWaves.size() * waves;
		int step = 0;

		for (Wave w : subWaves)
			for (int i = 0; i < waves; i++) {
				Wave gw = w.clone();
				time = timeStamp + curvedTime(i, total - 1);
				gw.setSpawnTime(time);

				modifyWave(gw, modifiers.get(0), step, total);
				newWaves.add(gw);
				step++;
			}

		return newWaves;
	}

	public boolean isAbsolute() {
		return absolute;
	}

	/**
	 * @return True if it is a composed wave (superwave)
	 */
	public boolean isSuperWave() {
		return bullet == null;
	}

	public void setAbsolute(boolean absolute) {
		this.absolute = absolute;
	}

	public void setBullets(int bullets) {
		this.bullets = bullets;
	}

	public void setInterval(double interval) {
		this.interval = interval;
	}

	public void setModifiers(ArrayList<DirectionModifier> modifiers) {
		this.modifiers = modifiers;
	}

	public void setMusic(Music music) {
		this.music = music;
	}

	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}

	public void setSpawnPoint(Vertex spawnPoint) {
		this.spawnPoint = spawnPoint;
	}

	public void setSpawnSound(SoundEffect spawnSound) {
		this.spawnSound = spawnSound;
	}

	public void setSpawnTime(double spawnTime) {
		this.spawnTime = spawnTime;
	}

	public void setTimeEnd(double timeEnd) {
		this.timeEnd = timeEnd;
	}

	public void setTimeExponent(double timeExponent) {
		this.timeExponent = timeExponent;
	}

	public void setTimeStart(double timeStart) {
		this.timeStart = timeStart;
	}

	public void setWaves(int waves) {
		this.waves = waves;
	}

	@Override
	public void spawn() {
		spawned = true;
	}

	@Override
	public boolean spawned() {
		return spawned;
	}

	@Override
	public double spawnTime() {
		return spawnTime;
	}

	/**
	 * This method must be called instead of spawn(), it starts the wave and
	 * their spawners
	 * 
	 * @param timeStamp Time stamp of the instant the spawn is called
	 * @param position Position of the wave
	 * @return A list of bullets spawned
	 */
	public ArrayList<Bullet> start(double timeStamp, Vertex position) {
		if (spawned)
			return null;

		spawn();
		timeStarted = spawnTime;
		spawnOrigin = position;

		if (music != null)
			music.play();
		if (spawnSound != null)
			spawnSound.play();
		
		// If it is a simple wave, the bullet spawner starts
		if (!isSuperWave()) {
			spawnerBullets = new Spawner<Bullet>(timeStarted);
			spawnerBullets.addSpawnables(getBulletWave(timeStarted));
			return applySpawnPosition(spawnerBullets.forward(timeStamp
					- timeStarted));
		}
		
		// If it is a composed wave, all sub-waves are started
		else {
			spawnerWaves = new Spawner<Wave>(timeStarted);
			spawnedWaves = new ArrayList<Wave>();
			ArrayList<Bullet> spawnedBullets = new ArrayList<Bullet>();
			ArrayList<Wave> newWaves = getWaves(timeStamp);
			spawnerWaves.addSpawnables(newWaves);

			for (Wave sw : spawnerWaves.forward(timeStamp - timeStarted)) {
				spawnedWaves.add(sw);
				for (Bullet b : sw.start(timeStamp, spawnPoint.clone()))
					spawnedBullets.add(b);
			}

			return applySpawnPosition(spawnedBullets);
		}
	}

	@Override
	public void vanish() {
		spawned = false;
	}

}
