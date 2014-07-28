package com.jde.model.entity.bullet;

import com.jde.model.entity.Entity;
import com.jde.model.entity.spawning.Spawnable;
import com.jde.model.physics.Movement;
import com.jde.model.physics.collision.HitZone;
import com.jde.view.sprites.Sprite;

public class Bullet extends Entity implements Spawnable {

	protected double spawnTime;
	protected boolean spawned;

	public Bullet(Sprite sprite, HitZone hitbox, Movement movement) {
		super(sprite, hitbox, movement);
		this.spawnTime = 0;
		this.spawned = false;
	}

	@Override
	public void spawn(double timeStamp) {
		if (spawnTime >= timeStamp)
			return;

		spawned = true;
		movement.setPosition(movement.getPosition().clone());
		//movement.applyRandomization();
		forward(timeStamp - spawnTime);
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
	public void setSpawnTime(double spawnTime) {
		this.spawnTime = spawnTime;
	}

	@Override
	public void vanish() {
		spawned = false;
	}

	/**
	 * Warning: this is not a pure cloning, it provides a Bullet template
	 */
	public Bullet clone() {
		Bullet b = new Bullet(sprite, hitbox, movement.clone());
		b.setSpawnTime(spawnTime);
		return b;
	}

	public String toString() {
		return "Bullet " + super.toString();
	}
}
