package com.jde.model.entity.bullet;

import java.util.ArrayList;

import com.jde.model.entity.spawning.Spawner;
import com.jde.model.physics.Direction;
import com.jde.model.physics.DirectionModifier;
import com.jde.model.physics.Movement;
import com.jde.model.physics.Vertex;

public class Wave {

	// Misc
	protected Spawner<Bullet> spawner;
	protected Vertex spawnOrigin;
	protected double elapsed = 0;
	protected boolean started = false;
	protected double timeStarted;

	// Config
	protected Bullet bullet;
	protected Vertex spawnPoint = new Vertex();
	protected ArrayList<Wave> subWaves = new ArrayList<Wave>();

	protected ArrayList<DirectionModifier> modifiers = new ArrayList<DirectionModifier>();

	protected double timeStart = 0;
	protected double timeEnd = 0;

	protected int bullets = 1;

	protected boolean repeat = false;
	protected double interval = 0;

	public Wave(Bullet bullet) {
		this.spawner = new Spawner<Bullet>(0);

		this.bullet = bullet.clone();
	}

	public Wave(ArrayList<Wave> subWaves, DirectionModifier modifier) {
		this.modifiers.add(modifier.clone());
		
		int total = subWaves.size();
		int step = 0;
		for (Wave w : subWaves) {
			Wave clonWave = w.clone();
			modifyWave(clonWave, modifier, step, total);
			this.subWaves.add(clonWave);
			step++;
		}
		
		this.bullet = null;
	}
	
	protected Wave(ArrayList<Wave> subWaves, DirectionModifier modifier, boolean clone) {
		this.modifiers.add(modifier.clone());
		
		for (Wave w : subWaves) {
			Wave clonWave = w.clone();
			this.subWaves.add(clonWave);
		}
		
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

	public int getBullets() {
		return bullets;
	}

	public void setBullets(int bullets) {
		this.bullets = bullets;
	}

	public boolean isRepeat() {
		return repeat;
	}

	public void setRepeat(boolean repeat) {
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
	
	public ArrayList<Bullet> start(double timeStart, double timeStamp,
			Vertex position) {
		if (started)
			return null;

		started = true;
		timeStarted = timeStart;
		spawnOrigin = position;
		
		if (bullet != null) {
			spawner = new Spawner<Bullet>(timeStart);
			spawner.addSpawnables(getBulletWave(timeStart));
			return applySpawnPosition(spawner.forward(timeStamp - timeStart));
		} 
		else {
			ArrayList<Bullet> spawnedBullets = new ArrayList<Bullet>();

			for (Wave w : subWaves) {
				for (Bullet b : applySpawnPosition(w.start(timeStart,
						timeStamp, new Vertex())))
					spawnedBullets.add(b);
			}

			return spawnedBullets;
		}

	}

	public ArrayList<Bullet> forward(double ms) {
		if (!started)
			return null;

		if (bullet != null) {

			elapsed += ms;
			if (repeat && elapsed > interval) {
				timeStarted += interval;
				spawner.addSpawnables(getBulletWave(timeStarted));
				elapsed -= interval;
			}

			return applySpawnPosition(spawner.forward(ms));
		} 
		else {
			ArrayList<Bullet> spawnedBullets = new ArrayList<Bullet>();

			for (Wave w : subWaves)
				for (Bullet b : applySpawnPosition(w.forward(ms)))
					spawnedBullets.add(b);

			return spawnedBullets;
		}
	}

	protected ArrayList<Bullet> getBulletWave(double timeStamp) {

		ArrayList<Bullet> bulletList = new ArrayList<Bullet>();

		double time = timeStamp;

		for (int i = 0; i < bullets; i++) {
			Bullet current = bullet.clone();
			Movement currentMovement = current.getMovement();

			double timeStep = (timeEnd - timeStart) / bullets;

			currentMovement.setPosition(spawnPoint.clone());
			current.setSpawnTime(time);
			time += timeStep;

			ArrayList<Direction> bulletDirs = current.getMovement()
					.getDirections();
			for (int j = 0; j < Math.min(modifiers.size(), bulletDirs.size()); j++)
				modifiers.get(j).modify(i, bullets, bulletDirs.get(j));

			bulletList.add(current);
		}

		return bulletList;
	}

	public ArrayList<Bullet> applySpawnPosition(ArrayList<Bullet> bullets) {
		for (Bullet b : bullets)
			b.getMovement().setPosition(
					b.getMovement().getPosition().add(spawnOrigin));
		return bullets;
	}
	
	public static void modifyWave(Wave w, DirectionModifier mod, int step, int total) {
		if (w.isSuperWave()) {
			for (Wave sw : w.getSubWaves()) {
				// TODO
			}
		}
		else {
			mod.modify(step, total, w.getBullet().getMovement().getDirections().get(0));
		}
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
			w = new Wave(subWaves, modifiers.get(0), true);
		
		ArrayList<DirectionModifier> clonMods = new ArrayList<DirectionModifier>();
		for (DirectionModifier d : modifiers)
			clonMods.add(d.clone());
		w.setModifiers(clonMods);

		w.setTimeEnd(timeEnd);
		w.setTimeStart(timeStart);

		w.setBullets(bullets);
		w.setInterval(interval);
		w.setRepeat(repeat);
		w.setSpawnPoint(spawnPoint.clone());

		return w;
	}

}
