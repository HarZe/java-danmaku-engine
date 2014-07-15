package com.jde.model.entity;

import org.lwjgl.opengl.GL11;

import com.jde.model.physics.Hitbox;
import com.jde.model.physics.Movement;
import com.jde.view.sprites.Sprite;

public class Entity {

	protected Sprite sprite;
	protected Hitbox hitbox;
	protected Movement movement;
	
	public Entity(Sprite sprite, Hitbox hitbox, Movement movement) {
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
		GL11.glRotated(movement.getAngle(), 0, 0, 1);
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
