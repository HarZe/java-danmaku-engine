package com.jde.model.physics;

import com.jde.view.Game;

public class Direction {
	
	// Main direction parameters
	protected double angle = 0;			// degrees
	
	protected double rotation = 0;		// degrees / sec
	protected double motion = 0;		// degrees / sec^2
	
	protected double speed = 0;			// vgapx / sec
	protected double acceleration = 0;	// vgapx / sec^2
	
	// Random offsets
	protected double randomAngleOffset = 0;
	protected double randomRotationOffset = 0;
	protected double randomMotionOffset = 0;
	protected double randomSpeedOffset = 0;
	protected double randomAccelerationOffset = 0;
	
	// Duration
	protected double duration = -1;
	
	// Homing position for initial angle calculus
	protected Vertex homingPosition = new Vertex();
	protected boolean homing = false;
	
	public Direction() {
		
	}

	public Direction(double angle) {
		this.angle = angle;
	}
	
	public Direction(Vertex homingPosition) {
		this.homingPosition = homingPosition;
		this.homing = true;
	}

	public boolean isHoming() {
		return homing;
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public Vertex getHomingPosition() {
		return homingPosition;
	}

	public void setHomingPosition(Vertex homingPosition) {
		this.homingPosition = homingPosition;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}
	
	public double getRandomAngleOffset() {
		return randomAngleOffset;
	}

	public void setRandomAngleOffset(double randomAngleOffset) {
		this.randomAngleOffset = randomAngleOffset;
	}

	public double getRandomRotationOffset() {
		return randomRotationOffset;
	}

	public void setRandomRotationOffset(double randomRotationOffset) {
		this.randomRotationOffset = randomRotationOffset;
	}

	public double getRandomSpeedOffset() {
		return randomSpeedOffset;
	}

	public void setRandomSpeedOffset(double randomSpeedOffset) {
		this.randomSpeedOffset = randomSpeedOffset;
	}

	public double getRandomAccelerationOffset() {
		return randomAccelerationOffset;
	}

	public void setRandomAccelerationOffset(double randomAccelerationOffset) {
		this.randomAccelerationOffset = randomAccelerationOffset;
	}

	public double getRandomMotionOffset() {
		return randomMotionOffset;
	}

	public void setRandomMotionOffset(double randomMotionOffset) {
		this.randomMotionOffset = randomMotionOffset;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}
	
	public double getMotion() {
		return motion;
	}

	public void setMotion(double motion) {
		this.motion = motion;
	}

	public void applyRandomitazion() {
		angle += (Game.random.nextDouble()*randomAngleOffset) - (randomAngleOffset / 2.0);
		rotation += (Game.random.nextDouble()*randomRotationOffset) - (randomRotationOffset / 2.0);
		motion += (Game.random.nextDouble()*randomMotionOffset) - (randomMotionOffset / 2.0);
		speed += (Game.random.nextDouble()*randomSpeedOffset) - (randomSpeedOffset / 2.0);
		acceleration += (Game.random.nextDouble()*randomAccelerationOffset) - (randomAccelerationOffset / 2.0);
	}

	public Direction clone() {
		Direction nd;
		if (!homing)
			nd = new Direction(angle);
		else
			nd = new Direction(homingPosition.clone());
		
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
		
		return nd;
	}
	
	public String toString() {
		return "Direction. angle:" + angle + ", speed:"
				+ speed + ", acceleration: " + acceleration;
	}
}
