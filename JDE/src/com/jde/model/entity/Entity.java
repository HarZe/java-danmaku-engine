package com.jde.model.entity;

import org.lwjgl.opengl.GL11;

import com.jde.model.physics.Movement;
import com.jde.model.physics.collision.HitBody;
import com.jde.view.sprites.Animation;

public class Entity {

	protected Animation animation;
	protected HitBody body;
	protected Movement movement;
	
	public Entity(Animation animation, HitBody body, Movement movement) {
		this.animation = animation;
		this.movement = movement;
		this.body = body;
	}

	public Movement getMovement() {
		return movement;
	}

	public void setMovement(Movement movement) {
		this.movement = movement;
	}

	public void draw() {
		GL11.glPushMatrix();
		
		GL11.glTranslated(movement.getPosition().getX(), movement.getPosition().getY(), 0);
		GL11.glRotated(movement.getDrawingAngle(), 0, 0, 1);
		
		animation.draw();
		
		GL11.glPopMatrix();
	}
	
	public void forward(double ms) {
		movement.forward(ms);
		animation.forward(ms);
	}
	
	public boolean collides(Movement collider, double ms) {
		return body.collides(movement.clone(), collider.clone(), ms);
	}

	public Entity clone() {
		return new Entity(animation.clone(), body, movement.clone());
	}
}
