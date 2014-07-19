package com.jde.model.physics;

public class Direction {
	
	protected double angle = 0;
	protected double angleStart = 0;
	protected double angleEnd = 0;
	protected double speed = 0;
	protected double acceleration = 0;
	protected double duration = -1;
	
	protected Vertex homingPosition = new Vertex();
	protected boolean homing = false;
	
	public Direction() {
		
	}

	public Direction(double angleStart, double angleEnd) {
		this.angle = angleStart;
		this.angleStart = angleStart;
		this.angleEnd = angleEnd;
	}
	
	public Direction(Vertex homingPosition) {
		this.homingPosition = homingPosition;
		this.homing = true;
	}

	public boolean isHoming() {
		return homing;
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

	public double getAngleStart() {
		return angleStart;
	}

	public void setAngleStart(double angleStart) {
		this.angleStart = angleStart;
	}

	public double getAngleEnd() {
		return angleEnd;
	}

	public void setAngleEnd(double angleEnd) {
		this.angleEnd = angleEnd;
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
		Direction nd;
		if (!homing)
			nd = new Direction(angleStart, angleEnd);
		else
			nd = new Direction(homingPosition.clone());
		nd.setSpeed(speed);
		nd.setAcceleration(acceleration);
		nd.setDuration(duration);
		return nd;
	}
	
	public String toString() {
		return "Direction. angle:" + angle + ", speed:"
				+ speed + ", acceleration: " + acceleration;
	}
}
