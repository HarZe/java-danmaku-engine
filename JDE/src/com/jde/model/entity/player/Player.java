package com.jde.model.entity.player;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.jde.model.entity.Entity;
import com.jde.model.physics.Movement;
import com.jde.model.physics.Vertex;
import com.jde.model.physics.collision.HitBody;
import com.jde.model.physics.collision.HitBox;
import com.jde.model.physics.collision.HitZone;
import com.jde.view.sprites.Animation;

public class Player extends Entity {
	
	protected HitZone gameZone = new HitBox(new Vertex(223,240), new Vertex(384 - 20, 448 - 20));
	
	protected Animation focusAnimation;
	
	protected double baseSpeed = 300;
	protected double focusFactor = 0.5;

	public Player(Animation animation, HitBody body, Movement movement, Animation focusAnimation) {
		super(animation, body, movement);
		this.focusAnimation = focusAnimation;
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
	
	public void drawFocus() {
		if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			return;
		
		GL11.glPushMatrix();
		
		GL11.glTranslated(movement.getPosition().getX(), movement.getPosition().getY(), 1);
		GL11.glRotated(movement.getDrawingAngle(), 0, 0, 1);
		
		focusAnimation.draw();
		
		GL11.glPopMatrix();
	}

	public void update(double ms) {
		boolean up = Keyboard.isKeyDown(Keyboard.KEY_UP);
		boolean down = Keyboard.isKeyDown(Keyboard.KEY_DOWN);
		boolean left = Keyboard.isKeyDown(Keyboard.KEY_LEFT);
		boolean right = Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
		boolean lshift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
		
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
			
		forward(ms);
		focusAnimation.forward(ms);
		
		// Correct if out of bound
		if (!gameZone.isInside(null, movement.getPosition()))
			movement.forward(-ms);
			
	}
}
