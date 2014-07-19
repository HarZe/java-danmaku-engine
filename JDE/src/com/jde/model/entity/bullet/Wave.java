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

	protected ArrayList<DirectionModifier> modifiers;

	protected double timeStart = 0;
	protected double timeEnd = 0;

	protected int bullets = 1;

	protected boolean repeat = false;
	protected double interval = 0;

	public Wave(Bullet bullet) {
		this.spawner = new Spawner<Bullet>(0);

		this.bullet = bullet;
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

	public ArrayList<Bullet> start(double timeStart, double timeStamp,
			Vertex position) {
		if (started)
			return null;

		started = true;
		timeStarted = timeStart;
		spawnOrigin = position;
		spawner = new Spawner<Bullet>(timeStart);
		spawner.addSpawnables(getBulletWave(timeStart));
		return applySpawnPosition(spawner.forward(timeStamp - timeStart));
	}

	public ArrayList<Bullet> forward(double ms) {
		if (!started)
			return null;

		elapsed += ms;
		if (repeat && elapsed > interval) {
			timeStarted += interval;
			spawner.addSpawnables(getBulletWave(timeStarted));
			elapsed -= interval;
		}

		return applySpawnPosition(spawner.forward(ms));
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
			
			ArrayList<Direction> bulletDirs = current.getMovement().getDirections();
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

	/**
	 * Warning: this is not a pure cloning, it provides a Horde template
	 */
	@Override
	public Wave clone() {
		Wave w = new Wave(bullet.clone());
		
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
