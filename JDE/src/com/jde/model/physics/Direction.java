package com.jde.model.physics;

import java.util.Random;

/**
 * This Direction class contains all the parameters that defines and modifies
 * movement through time
 * 
 * @author HarZe (David Serrano)
 */

public class Direction {

	// Main direction parameters

	/** Angle (direction) in degrees */
	protected double angle = 0;

	/** Rotation in degrees per second */
	protected double rotation = 0;
	/** Rotation acceleration (aka. "motion") in degrees per second² */
	protected double motion = 0;

	/** Speed in VGA pixels per second */
	protected double speed = 0;
	/** Speed acceleration in VGA pixels per second² */
	protected double acceleration = 0;

	// Random offsets

	/** Maximum offset of the random angle deviation, in degrees */
	protected double randomAngleOffset = 0;
	/** Maximum offset of the random rotation deviation, in degrees per second */
	protected double randomRotationOffset = 0;
	/** Maximum offset of the random motion deviation, in degrees per second² */
	protected double randomMotionOffset = 0;
	/** Maximum offset of the random speed deviation, in vgapx per second */
	protected double randomSpeedOffset = 0;
	/**
	 * Maximum offset of the random acceleration deviation, in vgapx per second²
	 */
	protected double randomAccelerationOffset = 0;

	// Duration

	/**
	 * Duration of the direction (negative value means eternal), in milliseconds
	 */
	protected double duration = -1;

	// Homing position for initial angle calculus

	/** Vertex position point "home" */
	protected Vertex homingPosition = new Vertex();
	/** Angle offset of the homing */
	protected double homingOffset = 0;
	/** Determines if homing is enabled */
	protected boolean homing = false;
	/** Determines if homing to player is enabled */
	protected boolean homingToPlayer = true;

	// Inheritance

	/** Determines if this direction inherits from a previous one */
	protected boolean inheritance = true;

	// Random seed

	/** Global random number generator */
	protected static Random randomGen = new Random();

	/**
	 * Default constructor with homing disabled
	 */
	public Direction() {

	}

	/**
	 * Basic constructor with homing disabled
	 * 
	 * @param angle
	 *            Initial angle in degrees
	 */
	public Direction(double angle) {
		this.angle = angle;
	}

	/**
	 * Basic constructor with homing enabled
	 * 
	 * @param homingPosition
	 *            Reference to the vertex used as "home"
	 */
	public Direction(Vertex homingPosition) {
		this.homingPosition = homingPosition;
		this.homing = true;
		this.homingToPlayer = false;
	}

	/**
	 * Modifies all the basic parameters with a random offset (based on the
	 * maximum random offsets of each parameter)
	 */
	public void applyRandomitazion() {
		angle += (randomGen.nextDouble() * randomAngleOffset)
				- (randomAngleOffset / 2.0);
		rotation += (randomGen.nextDouble() * randomRotationOffset)
				- (randomRotationOffset / 2.0);
		motion += (randomGen.nextDouble() * randomMotionOffset)
				- (randomMotionOffset / 2.0);
		speed += (randomGen.nextDouble() * randomSpeedOffset)
				- (randomSpeedOffset / 2.0);
		acceleration += (randomGen.nextDouble() * randomAccelerationOffset)
				- (randomAccelerationOffset / 2.0);
	}

	/**
	 * Returns a copy of the direction
	 */
	public Direction clone() {
		Direction nd;
		if (!homing)
			nd = new Direction(angle);
		else if (!homingToPlayer)
			nd = new Direction(homingPosition.clone());
		else
			nd = new Direction();

		nd.setRotation(rotation);
		nd.setMotion(motion);
		nd.setSpeed(speed);
		nd.setAcceleration(acceleration);

		nd.setRandomAccelerationOffset(randomAccelerationOffset);
		nd.setRandomAngleOffset(randomAngleOffset);
		nd.setRandomRotationOffset(randomRotationOffset);
		nd.setRandomMotionOffset(randomMotionOffset);
		nd.setRandomSpeedOffset(randomSpeedOffset);

		nd.setDuration(duration);
		nd.setInheritance(inheritance);
		nd.setHomingOffset(homingOffset);

		nd.setHoming(homing);
		nd.setHomingToPlayer(homingToPlayer);

		return nd;
	}

	public double getAcceleration() {
		return acceleration;
	}

	public double getAngle() {
		return angle;
	}

	public double getDuration() {
		return duration;
	}

	public double getHomingOffset() {
		return homingOffset;
	}

	public Vertex getHomingPosition() {
		return homingPosition;
	}

	public double getMotion() {
		return motion;
	}

	public double getRandomAccelerationOffset() {
		return randomAccelerationOffset;
	}

	public double getRandomAngleOffset() {
		return randomAngleOffset;
	}

	public double getRandomMotionOffset() {
		return randomMotionOffset;
	}

	public double getRandomRotationOffset() {
		return randomRotationOffset;
	}

	public double getRandomSpeedOffset() {
		return randomSpeedOffset;
	}

	public double getRotation() {
		return rotation;
	}

	public double getSpeed() {
		return speed;
	}

	public boolean isHoming() {
		return homing;
	}

	public boolean isHomingToPlayer() {
		return homingToPlayer;
	}

	public boolean isInheritance() {
		return inheritance;
	}

	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public void setHoming(boolean homing) {
		this.homing = homing;
	}

	public void setHomingOffset(double homingOffset) {
		this.homingOffset = homingOffset;
	}

	public void setHomingPosition(Vertex homingPosition) {
		this.homingPosition = homingPosition;
	}

	public void setHomingToPlayer(boolean homingToPlayer) {
		this.homingToPlayer = homingToPlayer;
	}

	public void setInheritance(boolean inheritance) {
		this.inheritance = inheritance;
	}

	public void setMotion(double motion) {
		this.motion = motion;
	}

	public void setRandomAccelerationOffset(double randomAccelerationOffset) {
		this.randomAccelerationOffset = randomAccelerationOffset;
	}

	public void setRandomAngleOffset(double randomAngleOffset) {
		this.randomAngleOffset = randomAngleOffset;
	}

	public void setRandomMotionOffset(double randomMotionOffset) {
		this.randomMotionOffset = randomMotionOffset;
	}

	public void setRandomRotationOffset(double randomRotationOffset) {
		this.randomRotationOffset = randomRotationOffset;
	}

	public void setRandomSpeedOffset(double randomSpeedOffset) {
		this.randomSpeedOffset = randomSpeedOffset;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}
}
