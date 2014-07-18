package com.jde.model.entity.bullet;

import java.util.ArrayList;

import com.jde.model.physics.Vertex;

public class Horde {
	
	protected ArrayList<Wave> waves;

	public Horde(ArrayList<Wave> waves) {
		this.waves = waves;
	}

	public ArrayList<Bullet> start(double timeStart, double timeStamp) {
		ArrayList<Bullet> bullets = new ArrayList<Bullet>();
		
		for (Wave w : waves)
			for (Bullet b : w.start(timeStart, timeStamp))
				bullets.add(b);
		
		return bullets;
	}
	
	public ArrayList<Bullet> forward(double ms) {
		ArrayList<Bullet> bullets = new ArrayList<Bullet>();
		
		for (Wave w : waves)
			for (Bullet b : w.forward(ms))
				bullets.add(b);
		
		return bullets;
	}
	
	public void setSpawnPoint(Vertex v) {
		for (Wave w : waves)
			w.setSpawnPoint(v);
	}
	
	public Horde clone() {
		ArrayList<Wave> newWaves = new ArrayList<Wave>();
		
		for (Wave w : waves)
			newWaves.add(w.clone());
		
		return new Horde(newWaves);
	}
}
