package com.jde.model.entity;

import org.lwjgl.opengl.GL11;

import com.jde.model.physics.Movement;
import com.jde.model.physics.collision.HitBody;
import com.jde.view.sprites.Animation;

/**
 * This Entity class represents a moving, animated and able to collide object
 * 
 * @author HarZe (David Serrano)
 */

public class Entity {

	/** Animation (sprites) of the entity */
	protected Animation animation;
	/** Body (hit zones) of the entity */
	protected HitBody body;
	/** Movement (position and directions) of the entity */
	protected Movement movement;
	
	/**
	 * Full constructor
	 * @param animation Animation of the entity
	 * @param body HitBody of the entity
	 * @param movement Movement of the entity
	 */
	public Entity(Animation animation, HitBody body, Movement movement) {
		this.animation = animation;
		this.movement = movement;
		this.body = body;
	}

	/**
	 * Returns a copy of the entity
	 */
	public Entity clone() {
		return new Entity(animation.clone(), body, movement.clone());
	}

	/**
	 * Check if other entity is colliding with this one
	 * @param entity Movement of other entity
	 * @param ms Milliseconds to forward
	 * @return True if the entities collide
	 */
	public boolean collides(Movement entity, double ms) {
		return body.collides(movement.clone(), entity.clone(), ms);
	}

	/**
	 * Draws the entity animation
	 */
	public void draw() {
		GL11.glPushMatrix();
		
		GL11.glTranslated(movement.getPosition().getX(), movement.getPosition().getY(), 0);
		GL11.glRotated(movement.getDrawingAngle(), 0, 0, 1);
		
		animation.draw();
		
		GL11.glPopMatrix();
	}
	
	/**
	 * Forwards the entity, both animation and position moves forward
	 * @param ms Milliseconds to forward
	 */
	public void forward(double ms) {
		movement.forward(ms);
		animation.forward(ms);
	}
	
	public Movement getMovement() {
		return movement;
	}

	public void setMovement(Movement movement) {
		this.movement = movement;
	}
}
