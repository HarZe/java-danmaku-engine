package com.jde.model.entity.spawning;

public interface Spawnable {

	/**
	 * Spawns the entity (make it visible) and forwards it from spawnTime to timeStamp, no spawn if spawnTime > timeStamp
	 * @param timeStamp
	 */
	public void spawn(double timeStamp);
	
	public boolean spawned();
	
	public double spawnTime();
	
	public void setSpawnTime(double spawnTime);
	
	public void vanish();
	
}
