package com.jde.model.entity.bullet;

import com.jde.audio.SoundEffect;
import com.jde.model.entity.Entity;
import com.jde.model.entity.spawning.Spawnable;
import com.jde.model.physics.Movement;
import com.jde.model.physics.collision.HitBody;
import com.jde.view.sprites.Animation;

/**
 * This Bullet class represents a entity that can spawn and hit the player
 * 
 * @author HarZe (David Serrano)
 */
public class Bullet extends Entity implements Spawnable {

	/** Spawning sound effect */
	protected SoundEffect spawnSound = null;
	
	/** Damage power of the bullet */
	protected double power = 1;

	/** Time stamp of the instant this bullet will spawn */
	protected double spawnTime = 0;
	/** Spawned state of this bullet */
	protected boolean spawned = false;

	/**
	 * Full constructor
	 * 
	 * @param animation
	 *            Animation of the bullet
	 * @param body
	 *            HitBody of the bullet
	 * @param movement
	 *            Movement of the bullet
	 */
	public Bullet(Animation animation, HitBody body, Movement movement) {
		super(animation, body, movement);
	}

	/**
	 * Returns a copy of the bullet
	 */
	public Bullet clone() {
		Bullet b = new Bullet(animation.clone(), body, movement.clone());
		b.setPower(power);
		b.setSpawnTime(spawnTime);
		b.setSpawnSound(spawnSound);
		return b;
	}

	public double getPower() {
		return power;
	}

	public SoundEffect getSpawnSound() {
		return spawnSound;
	}

	public void setPower(double power) {
		this.power = power;
	}

	public void setSpawnSound(SoundEffect spawnSound) {
		this.spawnSound = spawnSound;
	}

	@Override
	public void setSpawnTime(double spawnTime) {
		this.spawnTime = spawnTime;
	}

	@Override
	public void spawn() {
		// Making bullet position independent (no shared reference from now)
		movement.setPosition(movement.getPosition().clone());

		spawned = true;
		if (spawnSound != null)
			spawnSound.play();
	}

	@Override
	public boolean spawned() {
		return spawned;
	}

	@Override
	public double spawnTime() {
		return spawnTime;
	}

	public String toString() {
		return "Bullet " + super.toString();
	}

	@Override
	public void vanish() {
		spawned = false;
	}
}
