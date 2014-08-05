package com.jde.model.entity.bullet;

import java.util.ArrayList;

import com.jde.model.entity.spawning.Spawnable;
import com.jde.model.entity.spawning.Spawner;
import com.jde.model.physics.Direction;
import com.jde.model.physics.DirectionModifier;
import com.jde.model.physics.Movement;
import com.jde.model.physics.Vertex;

public class Wave implements Spawnable {

	// Misc
	protected Spawner<Bullet> spawnerBullets;
	protected Spawner<Wave> spawnerWaves;
	protected ArrayList<Wave> spawnedWaves;

	protected Vertex spawnOrigin;
	protected double elapsed = 0;
	protected boolean spawned = false;
	protected double timeStarted;

	// Config
	protected Bullet bullet;
	protected Vertex spawnPoint = new Vertex();
	protected ArrayList<Wave> subWaves = new ArrayList<Wave>();

	protected ArrayList<DirectionModifier> modifiers = new ArrayList<DirectionModifier>();

	protected double spawnTime = 0;
	protected double timeStart = 0;
	protected double timeEnd = 0;
	protected double timeExponent = 1;

	protected int bullets = 1;
	protected int waves = 1;

	protected int repeat = 0;
	protected double interval = 0;
	
	protected boolean absolute = false;

	public Wave(Bullet bullet) {
		this.spawnerBullets = new Spawner<Bullet>(0);
		this.bullet = bullet.clone();
	}

	public Wave(ArrayList<Wave> subWaves, DirectionModifier modifier) {
		this.modifiers.add(modifier.clone());
		for (Wave w : subWaves)
			this.subWaves.add(w.clone());
		this.bullet = null;
	}

	public boolean isSuperWave() {
		return bullet == null;
	}

	public Bullet getBullet() {
		return bullet;
	}

	public Vertex getSpawnPoint() {
		return spawnPoint;
	}

	public void setSpawnPoint(Vertex spawnPoint) {
		this.spawnPoint = spawnPoint;
	}

	public double getSpawnTime() {
		return spawnTime;
	}

	public void setSpawnTime(double spawnTime) {
		this.spawnTime = spawnTime;
	}

	public double getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(double timeStart) {
		this.timeStart = timeStart;
	}

	public double getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(double timeEnd) {
		this.timeEnd = timeEnd;
	}

	public double getTimeExponent() {
		return timeExponent;
	}

	public void setTimeExponent(double timeExponent) {
		this.timeExponent = timeExponent;
	}

	public int getBullets() {
		return bullets;
	}

	public void setBullets(int bullets) {
		this.bullets = bullets;
	}

	public int getWaves() {
		return waves;
	}

	public void setWaves(int waves) {
		this.waves = waves;
	}

	public int getRepeat() {
		return repeat;
	}

	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}

	public double getInterval() {
		return interval;
	}

	public void setInterval(double interval) {
		this.interval = interval;
	}

	public ArrayList<DirectionModifier> getModifiers() {
		return modifiers;
	}

	public void setModifiers(ArrayList<DirectionModifier> modifiers) {
		this.modifiers = modifiers;
	}

	public ArrayList<Wave> getSubWaves() {
		return subWaves;
	}

	public boolean isAbsolute() {
		return absolute;
	}

	public void setAbsolute(boolean absolute) {
		this.absolute = absolute;
	}

	@Override
	public void spawn(double timeStamp) {
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

	@Override
	public void vanish() {
		spawned = false;
	}

	public ArrayList<Bullet> start(double timeStamp, Vertex position) {
		if (spawned)
			return null;

		spawn(timeStamp);
		timeStarted = spawnTime;
		spawnOrigin = position;

		if (bullet != null) {
			spawnerBullets = new Spawner<Bullet>(timeStarted);
			spawnerBullets.addSpawnables(getBulletWave(timeStarted));
			return applySpawnPosition(spawnerBullets.forward(timeStamp
					- timeStarted));
		} 
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

	public ArrayList<Bullet> forward(double ms) {
		if (!spawned)
			return new ArrayList<Bullet>();

		elapsed += ms;

		if (bullet != null) {

			if (repeat != 0 && elapsed > interval) {
				timeStarted += interval;
				elapsed -= interval;
				spawnerBullets.addSpawnables(getBulletWave(timeStarted));
				
				if (repeat > 0)
					repeat--;
			}

			return applySpawnPosition(spawnerBullets.forward(ms));
		} 
		else {
			ArrayList<Bullet> spawnedBullets = new ArrayList<Bullet>();

			if (repeat != 0 && elapsed > interval) {
				timeStarted += interval;
				elapsed -= interval;
				spawnerWaves.addSpawnables(getWaves(timeStarted));
				
				if (repeat > 0)
					repeat--;
			}
			
			for (Wave w : spawnedWaves)
				for (Bullet b : w.forward(ms))
					spawnedBullets.add(b);
			
			for (Wave sw : spawnerWaves.forward(ms)) {
				spawnedWaves.add(sw);
				for (Bullet b : sw.start(timeStarted + elapsed, spawnPoint.clone()))
					spawnedBullets.add(b);
			}

			return applySpawnPosition(spawnedBullets);
		}
	}

	protected ArrayList<Bullet> getBulletWave(double timeStamp) {

		ArrayList<Bullet> bulletList = new ArrayList<Bullet>();
		double time = timeStamp;

		for (int i = 0; i < bullets; i++) {
			Bullet current = bullet.clone();
			Movement currentMovement = current.getMovement();

			currentMovement.setPosition(spawnPoint.clone());	// TODO: check if necessary
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

	public ArrayList<Bullet> applySpawnPosition(ArrayList<Bullet> bullets) {
		Vertex spawnPos = absolute ? new Vertex(223, 240) : spawnOrigin; // Center of the game zone
		
		for (Bullet b : bullets)
			b.getMovement().setPosition(
					b.getMovement().getPosition().add(spawnPos));
		
		return bullets;
	}

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
	
	protected double curvedTime(int step, int total) {
		double dStep = step;
		double dTotal = total;
		double relativeStep = Math.pow(dStep / dTotal, timeExponent);
		return timeStart + (timeEnd - timeStart)*relativeStep;
	}

	/**
	 * Warning: this is not a pure cloning, it provides a Horde template
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

		return w;
	}

}
