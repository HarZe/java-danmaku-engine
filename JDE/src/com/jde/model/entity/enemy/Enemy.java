package com.jde.model.entity.enemy;

import java.util.ArrayList;

import com.jde.model.entity.Entity;
import com.jde.model.entity.bullet.Bullet;
import com.jde.model.entity.bullet.Wave;
import com.jde.model.entity.spawning.Spawnable;
import com.jde.model.physics.Movement;
import com.jde.model.physics.collision.HitBody;
import com.jde.view.sprites.Animation;

/**
 * This Enemy class represents a entity that can spawn and attack the player
 * with bullets
 * 
 * @author HarZe (David Serrano)
 */
public class Enemy extends Entity implements Spawnable {

	/** Time stamp of the instant this enemy will spawn */
	protected double spawnTime = 0;
	/** Spawned state of this enemy */
	protected boolean spawned = false;
	/** Enemy health points */
	protected double health;

	/** Wave of bullets used to attack the player */
	protected Wave wave = null;

	/**
	 * Full constructor of the enemy
	 * 
	 * @param animation
	 *            Animation of the enemy
	 * @param body
	 *            HitBody of the enemy
	 * @param movement
	 *            Movement of the enemy
	 * @param wave
	 *            Wave of bullets of the enemy
	 * @param health
	 *            Base health points of the enemy
	 */
	public Enemy(Animation animation, HitBody body, Movement movement,
			Wave wave, double health) {
		super(animation, body, movement);
		this.health = health;

		if (wave != null)
			this.wave = wave.clone();
	}

	/**
	 * Returns a copy of the enemy
	 */
	public Enemy clone() {
		Enemy e;

		if (wave != null)
			e = new Enemy(animation.clone(), body, movement.clone(),
					wave.clone(), health);
		else
			e = new Enemy(animation.clone(), body, movement.clone(), null,
					health);

		e.setSpawnTime(spawnTime);
		return e;
	}

	/**
	 * This method reduce the health points of the enemy, if they become less or
	 * equal to 0, enemy will vanish
	 * 
	 * @param dmgPoints
	 *            Damage points
	 */
	public void damage(double dmgPoints) {
		health -= dmgPoints;

		if (health <= 0)
			vanish();
	}

	/**
	 * This method forwards the enemy and generate new bullets to attack (if
	 * any)
	 * 
	 * @param ms
	 *            Milliseconds to forward
	 * @return A list of bullets spawned by the enemy
	 */
	public ArrayList<Bullet> forwardAndBullets(double ms) {
		forward(ms);
		if (wave != null)
			return wave.forward(ms);
		else
			return new ArrayList<Bullet>();
	}

	@Override
	public void setSpawnTime(double spawnTime) {
		this.spawnTime = spawnTime;
		if (wave != null)
			wave.setSpawnTime(spawnTime);
	}

	@Override
	public void spawn(double timeStamp) {
		if (spawnTime >= timeStamp)
			return;

		spawned = true;
	}

	/**
	 * This method spawns the enemy and the bullets (if any). WARNING: this
	 * method SHOULD be used instead of spawn()
	 * 
	 * @param timeStamp
	 *            Time stamp of the moment the spawning is required
	 * @return A list of bullets spawned by the enemy
	 */
	public ArrayList<Bullet> spawnAndBullets(double timeStamp) {
		spawn(timeStamp);
		if (!spawned)
			return null;

		forward(timeStamp - spawnTime);
		if (wave != null)
			return wave.start(timeStamp, movement.getPosition());
		else
			return new ArrayList<Bullet>();
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
	public void vanish() {
		spawned = false;
	}
}
