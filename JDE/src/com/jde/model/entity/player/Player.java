package com.jde.model.entity.player;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.jde.model.entity.bullet.Bullet;
import com.jde.model.entity.bullet.Wave;
import com.jde.model.physics.Direction;
import com.jde.model.physics.Movement;
import com.jde.model.physics.Vertex;
import com.jde.view.Game;
import com.jde.view.sprites.Animation;

/**
 * This Player class contains the animations, attacks and stats of the playable
 * character
 * 
 * @author HarZe (David Serrano)
 */
public class Player {

	/** Game board limit border for the player */
	protected static double BORDER = 20;

	/** Player main animation */
	protected Animation animation;
	/** Player focus (real body) animation */
	protected Animation focusAnimation;
	/** Player animation when moving to left */
	protected Animation movingLeftAnimation;
	/** Player animation when moving to right */
	protected Animation movingRightAnimation;
	/** Player movement */
	protected Movement movement;

	/** Player base speed (vgapx/sec) */
	protected double baseSpeed = 300;
	/** Player focus speed slowing factor */
	protected double focusFactor = 0.5;

	/** Player hit body radius (vgapx) */
	protected double hitboxRadius = 4;

	/** Player basic wave attack */
	protected Wave baseAttack;
	/** Cooldown time between attacks */
	protected double baseCooldown = 200;

	// Misc
	/** Current cooldown time remaining */
	protected double cooldown = 0;

	/** Shooting key state */
	protected boolean shoot;
	/** Bombing key state */
	protected boolean bomb;
	/** Focus key state */
	protected boolean focus;
	/** Moving up key state */
	protected boolean up;
	/** Moving down key state */
	protected boolean down;
	/** Moving left key state */
	protected boolean left;
	/** Moving right key state */
	protected boolean right;

	/**
	 * Basic constructor
	 * 
	 * @param animation
	 *            Main player animation
	 * @param focusAnimation
	 *            Focused player overlay animation
	 * @param spawnPoint
	 *            Respawn point of the player
	 * @param baseAttack
	 *            Player's wave attack
	 */
	public Player(Animation animation, Animation focusAnimation,
			Vertex spawnPoint, Wave baseAttack) {
		this.animation = animation;
		this.focusAnimation = focusAnimation;
		this.baseAttack = baseAttack;

		ArrayList<Direction> dirs = new ArrayList<Direction>();
		dirs.add(new Direction());
		movement = new Movement(spawnPoint.clone(), dirs);
		movement.setLookAtMovingDirection(false);
		movement.setSpin(0);
	}

	/**
	 * Draws the player (main animation)
	 */
	public void draw() {
		GL11.glPushMatrix();

		GL11.glTranslated(movement.getPosition().getX(), movement.getPosition()
				.getY(), 0);
		GL11.glRotated(movement.getDrawingAngle(), 0, 0, 1);

		if (movingLeftAnimation != null && left && !right)
			movingLeftAnimation.draw();
		else if (movingRightAnimation != null && !left && right)
			movingRightAnimation.draw();
		else
			animation.draw();

		GL11.glPopMatrix();
	}

	/**
	 * Draws the focused player animation
	 */
	public void drawFocus() {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			GL11.glPushMatrix();

			GL11.glTranslated(movement.getPosition().getX(), movement
					.getPosition().getY(), -1);
			GL11.glRotated(movement.getDrawingAngle(), 0, 0, 1);

			focusAnimation.draw();

			GL11.glPopMatrix();
		}
	}

	public Wave getBaseAttack() {
		return baseAttack;
	}

	public double getBaseCooldown() {
		return baseCooldown;
	}

	public double getBaseSpeed() {
		return baseSpeed;
	}

	public double getFocusFactor() {
		return focusFactor;
	}

	public double getHitboxRadius() {
		return hitboxRadius;
	}

	public Movement getMovement() {
		return movement;
	}

	public void setBaseAttack(Wave baseAttack) {
		this.baseAttack = baseAttack;
	}

	public void setBaseCooldown(double baseCooldown) {
		this.baseCooldown = baseCooldown;
	}

	public void setBaseSpeed(double baseSpeed) {
		this.baseSpeed = baseSpeed;
	}

	public void setFocusFactor(double focusFactor) {
		this.focusFactor = focusFactor;
	}

	public void setHitboxRadius(double hitboxRadius) {
		this.hitboxRadius = hitboxRadius;
	}

	public void setMovingLeftAnimation(Animation movingLeftAnimation) {
		this.movingLeftAnimation = movingLeftAnimation;
	}

	public void setMovingRightAnimation(Animation movingRightAnimation) {
		this.movingRightAnimation = movingRightAnimation;
	}

	/**
	 * This method updates the player state, spawn new bullets (if any) and
	 * moves the player if needed
	 * 
	 * @param ms
	 *            Milliseconds to forward
	 * @param timeStamp
	 *            Current time stamp
	 * @return PLayer's bullets to spawn
	 */
	public ArrayList<Bullet> update(double ms, double timeStamp) {

		ArrayList<Bullet> bullets = new ArrayList<Bullet>();

		// Getting keyboard state
		up = Keyboard.isKeyDown(Keyboard.KEY_UP);
		down = Keyboard.isKeyDown(Keyboard.KEY_DOWN);
		left = Keyboard.isKeyDown(Keyboard.KEY_LEFT);
		right = Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
		focus = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
		shoot = Keyboard.isKeyDown(Keyboard.KEY_Z);
		bomb = Keyboard.isKeyDown(Keyboard.KEY_X);

		// Calculating direction vector
		Vertex dir = new Vertex();
		if (up)
			dir.add(new Vertex(0, -1));
		if (down)
			dir.add(new Vertex(0, 1));
		if (left)
			dir.add(new Vertex(-1, 0));
		if (right)
			dir.add(new Vertex(1, 0));

		// Getting player's speed
		double speed = baseSpeed;
		if (dir.getX() != 0 || dir.getY() != 0)
			movement.getCurrentDirection().setAngle(dir.angle());
		else
			speed = 0;

		if (focus)
			speed *= focusFactor;
		movement.getCurrentDirection().setSpeed(speed);

		// Forwarding animations and player's position
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

		// Border limit corrections (if crossing the border):

		// Vertical
		if (movement.getPosition().getY() < Game.GAME_BOARD_POS.getY() + BORDER)
			movement.getPosition().setY(Game.GAME_BOARD_POS.getY() + BORDER);
		else if (movement.getPosition().getY() > Game.GAME_BOARD_POS.getY()
				+ Game.GAME_BOARD_SIZE.getY() - BORDER)
			movement.getPosition().setY(
					Game.GAME_BOARD_POS.getY() + Game.GAME_BOARD_SIZE.getY()
							- BORDER);

		// Horizontal
		if (movement.getPosition().getX() < Game.GAME_BOARD_POS.getX() + BORDER)
			movement.getPosition().setX(Game.GAME_BOARD_POS.getX() + BORDER);
		else if (movement.getPosition().getX() > Game.GAME_BOARD_POS.getX()
				+ Game.GAME_BOARD_SIZE.getX() - BORDER)
			movement.getPosition().setX(
					Game.GAME_BOARD_POS.getX() + Game.GAME_BOARD_SIZE.getX()
							- BORDER);

		// Bullet generation
		cooldown -= ms;
		if (shoot) {
			if (cooldown <= 0) {
				cooldown = baseCooldown;
				baseAttack = baseAttack.clone();
				return baseAttack.start(timeStamp, movement.getPosition());
			} else if (baseAttack.spawned())
				return baseAttack.forward(ms);
		} else
			baseAttack = baseAttack.clone();

		return bullets;
	}
}
