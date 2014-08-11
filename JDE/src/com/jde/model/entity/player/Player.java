package com.jde.model.entity.player;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.jde.model.physics.Direction;
import com.jde.model.physics.Movement;
import com.jde.model.physics.Vertex;
import com.jde.model.physics.collision.HitBox;
import com.jde.model.physics.collision.HitZone;
import com.jde.view.sprites.Animation;

public class Player {
	
	protected HitZone gameZone = new HitBox(new Vertex(223,240), new Vertex(384 - 20, 448 - 20));
	
	protected Animation animation;
	protected Animation focusAnimation;
	protected Animation movingLeftAnimation;
	protected Animation movingRightAnimation;
	protected Movement movement;
	
	protected double baseSpeed = 300;
	protected double focusFactor = 0.5;
	
	protected double hitboxRadius = 4;
	
	protected boolean lshift, up, down, left, right;
	
	public Player(Animation animation, Animation focusAnimation, Vertex spawnPoint) {
		this.animation = animation;
		this.focusAnimation = focusAnimation;
		
		ArrayList<Direction> dirs = new ArrayList<Direction>();
		dirs.add(new Direction());
		movement = new Movement(spawnPoint.clone() , dirs);
		movement.setLookAtMovingDirection(false);
		movement.setSpin(0);
	}

	public double getBaseSpeed() {
		return baseSpeed;
	}

	public void setBaseSpeed(double baseSpeed) {
		this.baseSpeed = baseSpeed;
	}

	public double getFocusFactor() {
		return focusFactor;
	}

	public void setFocusFactor(double focusFactor) {
		this.focusFactor = focusFactor;
	}
	
	public double getHitboxRadius() {
		return hitboxRadius;
	}

	public void setHitboxRadius(double hitboxRadius) {
		this.hitboxRadius = hitboxRadius;
	}

	public Movement getMovement() {
		return movement;
	}

	public void setMovingLeftAnimation(Animation movingLeftAnimation) {
		this.movingLeftAnimation = movingLeftAnimation;
	}

	public void setMovingRightAnimation(Animation movingRightAnimation) {
		this.movingRightAnimation = movingRightAnimation;
	}

	public void draw() {
		GL11.glPushMatrix();
		
		GL11.glTranslated(movement.getPosition().getX(), movement.getPosition().getY(), 0);
		GL11.glRotated(movement.getDrawingAngle(), 0, 0, 1);
		
		if (movingLeftAnimation != null && left && !right)
			movingLeftAnimation.draw();
		else if (movingRightAnimation != null && !left && right)
			movingRightAnimation.draw();
		else
			animation.draw();
		
		GL11.glPopMatrix();
	}

	public void drawFocus() {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			GL11.glPushMatrix();
			
			GL11.glTranslated(movement.getPosition().getX(), movement.getPosition().getY(), -1);
			GL11.glRotated(movement.getDrawingAngle(), 0, 0, 1);
			
			focusAnimation.draw();
			
			GL11.glPopMatrix();
		}
	}
	
	public void update(double ms) {
		up = Keyboard.isKeyDown(Keyboard.KEY_UP);
		down = Keyboard.isKeyDown(Keyboard.KEY_DOWN);
		left = Keyboard.isKeyDown(Keyboard.KEY_LEFT);
		right = Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
		lshift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
		
		Vertex dir = new Vertex();
		if (up)
			dir.add(new Vertex(0, -1));
		if (down)
			dir.add(new Vertex(0, 1));
		if (left)
			dir.add(new Vertex(-1, 0));
		if (right)
			dir.add(new Vertex(1, 0));
		
		double speed = baseSpeed;
		if (dir.getX() != 0 || dir.getY() != 0)
			movement.getDirection().setAngle(dir.angle());
		else
			speed = 0;
		
		if (lshift)
			speed *= focusFactor;
		movement.getDirection().setSpeed(speed);
			
		animation.forward(ms);
		focusAnimation.forward(ms);
		movement.forward(ms);
		
		if (movingLeftAnimation != null && left && !right)
			movingLeftAnimation.forward(ms);
		else
			movingLeftAnimation.reset();
		
		if (movingRightAnimation != null && !left && right)
			movingRightAnimation.forward(ms);
		else
			movingRightAnimation.reset();
		
		// Correct if out of bound
		if (!gameZone.isInside(null, movement.getPosition()))
			movement.forward(-ms);
			
	}
}
