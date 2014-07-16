package com.jde.model.physics;

public class Movement {

	protected Vertex position;
	protected double angle;
	protected double speed;
	protected double acceleration;

	public Movement() {
		position = new Vertex();
		angle = speed = acceleration = 0;
	}

	public Movement(Vertex position, double angle, double speed,
			double acceleration) {
		this.position = position;
		this.angle = angle;
		this.speed = speed;
		this.acceleration = acceleration;
	}

	public Vertex getPosition() {
		return position;
	}

	public void setPosition(Vertex position) {
		this.position = position;
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

	public void forward(double ms) {
		speed += acceleration * ms * 0.001;
		Vertex step = Vertex.angle(Math.toRadians(angle + 90));
		step.scale(speed * ms * 0.001);
		position.add(step);
	}

	public Movement clone() {
		return new Movement(position.clone(), angle, speed, acceleration);
	}

	public String toString() {
		return "Movement. Pos:" + position + ", angle:" + angle + ", speed:"
				+ speed + ", acceleration: " + acceleration;
	}
}
