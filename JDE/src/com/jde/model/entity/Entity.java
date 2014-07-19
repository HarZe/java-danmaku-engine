package com.jde.model.entity;

import org.lwjgl.opengl.GL11;

import com.jde.model.physics.Movement;
import com.jde.model.physics.collision.HitZone;
import com.jde.view.sprites.Sprite;

public class Entity {

	protected Sprite sprite;
	protected HitZone hitbox;
	protected Movement movement;
	
	protected boolean lookAtMovingDirection = true;
	protected boolean drawingRotateCorretion = true;
	
	public Entity(Sprite sprite, HitZone hitbox, Movement movement) {
		this.sprite = sprite;
		this.movement = movement;
		this.hitbox = hitbox;
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
		if (drawingRotateCorretion)
			GL11.glRotated(90, 0, 0, 1);
		if (lookAtMovingDirection)
			GL11.glRotated(movement.getDirection().getAngle(), 0, 0, 1);
		sprite.draw();
		
		GL11.glPopMatrix();
	}
	
	public void forward(double ms) {
		movement.forward(ms);
	}

	public Entity clone() {
		return new Entity(sprite, hitbox, movement.clone());
	}
	
	public String toString() {
		return "{" + "\n" + "\t" + sprite + "\n" + "\t" + movement
				+ "\n" + "}";
	}
}
