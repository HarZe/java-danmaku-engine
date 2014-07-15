package com.jde.model.entity.bullet;

import java.util.ArrayList;

import com.jde.model.entity.spawning.Spawner;
import com.jde.model.physics.Movement;
import com.jde.model.physics.Vertex;

public class Horde {
	
	// Misc
	protected Spawner<Bullet> spawner;
	protected double elapsed = 0;
	protected boolean started = false;
	protected double timeStarted;
	
	// Config
	protected Bullet bullet;
	protected Vertex spawnPoint = new Vertex();
	protected Vertex spawnOffset = new Vertex();
	
	protected double angleStart = 0;
	protected double angleEnd = 0;
	
	protected double timeStart = 0;
	protected double timeEnd = 0;
	
	protected double bullets = 1;
	
	protected boolean repeat = false;
	protected double interval = 0;
	

	public Horde(Bullet bullet) {
		this.spawner = new Spawner<Bullet>(0);

		this.bullet = bullet;
	}

	public Vertex getSpawnPoint() {
		return spawnPoint;
	}

	public void setSpawnPoint(Vertex spawnPoint) {
		this.spawnPoint = spawnPoint;
	}

	public Vertex getSpawnOffset() {
		return spawnOffset;
	}

	public void setSpawnOffset(Vertex spawnOffset) {
		this.spawnOffset = spawnOffset;
	}

	public double getAngleStart() {
		return angleStart;
	}

	public void setAngleStart(double angleStart) {
		this.angleStart = angleStart;
	}

	public double getAngleEnd() {
		return angleEnd;
	}

	public void setAngleEnd(double angleEnd) {
		this.angleEnd = angleEnd;
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

	public double getBullets() {
		return bullets;
	}

	public void setBullets(double bullets) {
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
	
	public ArrayList<Bullet> start(double timeStart, double timeStamp) {
		if (started)
			return null;
		
		started = true;
		timeStarted = timeStart;
		spawner = new Spawner<Bullet>(timeStart);
		spawner.addSpawnables(getBulletHorde(timeStart));
		return spawner.forward(timeStamp - timeStart);
	}
	
	public ArrayList<Bullet> forward(double ms) {
		if (!started)
			return null;
		
		elapsed += ms;
		if (repeat && elapsed > interval) {
			timeStarted += interval;
			spawner.addSpawnables(getBulletHorde(timeStarted));
			elapsed -= interval;
		}
		
		return spawner.forward(ms);
	}

	protected ArrayList<Bullet> getBulletHorde(double timeStamp) {
		
		ArrayList<Bullet> bulletList = new ArrayList<Bullet>();
		
		double angleStep = (angleEnd - angleStart) / bullets;
		double timeStep = (timeEnd - timeStart) / bullets;
		
		double angle = angleStart;
		double time = timeStamp;
		
		for (int i = 0; i < bullets; i++) {
			Bullet current = bullet.clone();
			Movement currentMovement = current.getMovement();
			
			currentMovement.setPosition(spawnPoint);	// TODO: add offset for position (probably should be done at spawn time)
			
			current.setSpawnTime(time);
			currentMovement.setAngle(angle);
			
			time += timeStep;
			angle += angleStep;
			bulletList.add(current);
		}
		
		return bulletList;
	}
	
	/**
	 * Warning: this is not a pure cloning, it provides a Horde template
	 */
	@Override
	public Horde clone() {
		Horde h = new Horde(bullet.clone());
		h.setAngleEnd(angleEnd);
		h.setAngleStart(angleStart);
		h.setBullets(bullets);
		h.setInterval(interval);
		h.setRepeat(repeat);
		h.setSpawnPoint(spawnPoint.clone());
		h.setTimeEnd(timeEnd);
		h.setTimeStart(timeStart);
		return h;
	}

}
