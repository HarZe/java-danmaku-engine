package com.jde.model.entity.spawning;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * This Spawner class contains a list of Spawnable objects and make them spawn
 * at the proper time
 * 
 * @author HarZe (David Serrano)
 */
public class Spawner<S extends Spawnable> {

	/**
	 * This SpawnablesComparator compares Spawnable objects
	 * 
	 * @author HarZe (David Serrano)
	 */
	protected class SpawnablesComparator implements Comparator<S> {
		@Override
		public int compare(S s1, S s2) {
			return Integer.valueOf((int) (s1.spawnTime() * 1000)).compareTo(
					Integer.valueOf((int) (s2.spawnTime() * 1000)));
		}
	}

	/** List of Spawnable objects to spawn */
	protected PriorityQueue<S> spawns;
	/** Elapsed time since the spawner started */
	protected double elapsedTime;

	/**
	 * Basic constructor
	 * 
	 * @param initTime
	 *            Initial time stamp for the spawner
	 */
	public Spawner(double initTime) {
		spawns = new PriorityQueue<S>(20, new SpawnablesComparator());
		elapsedTime = initTime;
	}

	/**
	 * Adds a Spawnable object to the spawner
	 * 
	 * @param spawnable
	 *            Spawnable object to add
	 */
	public void addSpawnable(S spawnable) {
		spawns.add(spawnable);
	}

	/**
	 * Adds a list of Spawnable objects to the spawner
	 * 
	 * @param spawnables
	 *            List of Spawnable objects to add
	 */
	public void addSpawnables(List<S> spawnables) {
		for (S s : spawnables)
			spawns.add(s);
	}

	/**
	 * Forwards the spawner and spawn some Spawnable objects (if any)
	 * 
	 * @param ms
	 *            Milliseconds to forward
	 * @return A list of Spawnable objects meant to spawn
	 */
	public ArrayList<S> forward(double ms) {
		ArrayList<S> frameSpawns = new ArrayList<S>();

		while (spawns.size() > 0
				&& spawns.peek().spawnTime() < elapsedTime + ms)
			frameSpawns.add(spawns.poll());

		elapsedTime += ms;
		return frameSpawns;
	}

	/**
	 * @return The number of Spawnable object that have not spawned yet
	 */
	public int pendingSpawns() {
		return spawns.size();
	}
}
