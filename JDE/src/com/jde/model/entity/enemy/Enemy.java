package com.jde.model.entity.enemy;

import java.util.ArrayList;

import com.jde.model.entity.Entity;
import com.jde.model.entity.bullet.Bullet;
import com.jde.model.entity.bullet.Horde;
import com.jde.model.entity.spawning.Spawnable;
import com.jde.model.physics.Movement;
import com.jde.model.physics.collision.HitZone;
import com.jde.view.sprites.Sprite;

public class Enemy extends Entity implements Spawnable {
	
	protected double spawnTime;
	protected boolean spawned;
	protected double health;
	
	protected Horde horde;

	public Enemy(Sprite sprite, HitZone hitbox, Movement movement, Horde horde, double health) {
		super(sprite, hitbox, movement);
		this.health = health;
		this.spawnTime = 0;
		this.spawned = false;
		this.horde = null;
		lookAtMovingDirection = false;
		
		if (horde != null) {
			this.horde = horde.clone();
			this.horde.setSpawnPoint(movement.getPosition());	// Matches the horde origin position with the enemy position
		}
	}
	
	public void damage(double dmgPoints) {
		health -= dmgPoints;
		
		if (health <= 0)
			vanish();
	}
	
	public ArrayList<Bullet> spawnAndBullets(double timeStamp) {
		spawn(timeStamp);
		if (!spawned)
			return null;
		
		forward(timeStamp - spawnTime);
		if (horde != null)
			return horde.start(spawnTime, timeStamp);
		else
			return new ArrayList<Bullet>();
	}
	
	public ArrayList<Bullet> forwardAndBullets(double ms) {
		forward(ms);
		if (horde != null)
			return horde.forward(ms);
		else
			return new ArrayList<Bullet>();
	}
	
	/**
	 * Use spawnAndBullets(double) instead
	 */
	@Override
	public void spawn(double timeStamp) {
		if (spawnTime >= timeStamp)
			return;
		
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
	public void setSpawnTime(double spawnTime) {
		this.spawnTime = spawnTime;
	}

	@Override
	public void vanish() {
		spawned = false;
	}
	
	/**
	 * Warning: this is not a pure cloning, it provides a Enemy template
	 */
	public Enemy clone() {
		Enemy e = new Enemy(sprite, hitbox, movement.clone(), horde.clone(), health);
		e.setSpawnTime(spawnTime);
		return e;
	}
	
	public String toString() {
		return "Enemy " + super.toString();
	}
}
