package com.jde.model.entity.spawning;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Spawner<S extends Spawnable> {

	protected PriorityQueue<S> spawns;
	protected double elapsedTime;

	public Spawner(double initTime) {
		spawns = new PriorityQueue<S>(100, new SpawnablesComparator());
		elapsedTime = initTime;
	}
	
	public int pendingSpawns() {
		return spawns.size();
	}

	public void addSpawnable(S spawnable) {
		spawns.add(spawnable);
	}
	
	public void addSpawnables(List<S> spawnables) {
		for (S s : spawnables)
			spawns.add(s);
	}

	public ArrayList<S> forward(double ms) {
		ArrayList<S> frameSpawns = new ArrayList<S>();

		while (spawns.size() > 0
				&& spawns.peek().spawnTime() < elapsedTime + ms) {
			if (spawns.peek().spawnTime() >= elapsedTime)
				frameSpawns.add(spawns.poll());
			else
				System.err.println("Spawner error: out of frame spawnable. "
						+ spawns.poll().spawnTime() + " out of [" + elapsedTime
						+ ", " + (elapsedTime + ms) + "]");
		}

		elapsedTime += ms;
		return frameSpawns;
	}

	protected class SpawnablesComparator implements Comparator<S> {

		@Override
		public int compare(S s1, S s2) {
			return Integer.valueOf((int) (s1.spawnTime() * 1000)).compareTo(
					Integer.valueOf((int) (s2.spawnTime() * 1000)));
		}

	}
}
