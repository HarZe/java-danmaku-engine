package com.jde.model.physics;

public class Direction {
	
	protected double angle = 0;
	protected double speed = 0;
	protected double acceleration = 0;
	protected double duration = -1;
	
	public Direction() {
		
	}

	public Direction(double angle, double speed, double acceleration) {
		this.angle = angle;
		this.speed = speed;
		this.acceleration = acceleration;
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
	
	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public Direction clone() {
		Direction nd = new Direction(angle, speed, acceleration);
		nd.setDuration(duration);
		return nd;
	}
	
	public String toString() {
		return "Direction. angle:" + angle + ", speed:"
				+ speed + ", acceleration: " + acceleration;
	}
}
