package com.jde.model.entity.spawning;

/**
 * This Spawnable interface represents a entity that can spawn
 * 
 * @author HarZe (David Serrano)
 */
public interface Spawnable {

	/**
	 * Sets the time stamp of the instant it will spawn
	 * 
	 * @param spawnTime
	 *            Time stamp of the spawn instant
	 */
	public void setSpawnTime(double spawnTime);

	/**
	 * Spawns the entity (make it visible) and forwards it from spawnTime to
	 * timeStamp
	 * 
	 * @param timeStamp
	 *            Time stamp of the spawn instant, in milliseconds
	 */
	public void spawn(double timeStamp);

	/**
	 * @return True if the entity has spawned
	 */
	public boolean spawned();

	/**
	 * @return The time stamp of the instant it spawned or will spawn
	 */
	public double spawnTime();

	/**
	 * Despawn the entity
	 */
	public void vanish();

}
