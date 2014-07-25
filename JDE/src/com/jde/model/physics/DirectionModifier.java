package com.jde.model.physics;


public class DirectionModifier {

	protected double angleStart = 0;
	protected double angleEnd = 0;

	protected double rotationStart = 0;
	protected double rotationEnd = 0;

	protected double speedStart = 0;
	protected double speedEnd = 0;

	protected double accelerationStart = 0;
	protected double accelerationEnd = 0;

	public DirectionModifier() {

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

	public double getRotationStart() {
		return rotationStart;
	}

	public void setRotationStart(double rotationStart) {
		this.rotationStart = rotationStart;
	}

	public double getRotationEnd() {
		return rotationEnd;
	}

	public void setRotationEnd(double rotationEnd) {
		this.rotationEnd = rotationEnd;
	}

	public double getSpeedStart() {
		return speedStart;
	}

	public void setSpeedStart(double speedStart) {
		this.speedStart = speedStart;
	}

	public double getSpeedEnd() {
		return speedEnd;
	}

	public void setSpeedEnd(double speedEnd) {
		this.speedEnd = speedEnd;
	}

	public double getAccelerationStart() {
		return accelerationStart;
	}

	public void setAccelerationStart(double accelerationStart) {
		this.accelerationStart = accelerationStart;
	}

	public double getAccelerationEnd() {
		return accelerationEnd;
	}

	public void setAccelerationEnd(double accelerationEnd) {
		this.accelerationEnd = accelerationEnd;
	}

	public void modify(int step, int total, Direction dir) {
		
		if (total == 1)
			step = 0;
		else
			total--;

		double angleStep = (angleEnd - angleStart) * step / total;
		double rotationStep = (rotationEnd - rotationStart) * step / total;
		double speedStep = (speedEnd - speedStart) * step / total;
		double accelerationStep = (accelerationEnd - accelerationStart) * step
				/ total;

		double angle = angleStart + angleStep;
		double rotation = rotationStart + rotationStep;
		double speed = speedStart + speedStep;
		double acceleration = accelerationStart + accelerationStep;

		dir.setAngle(dir.getAngle() + angle);
		dir.setRotation(dir.getRotation() + rotation);
		dir.setSpeed(dir.getSpeed() + speed);
		dir.setAcceleration(dir.getAcceleration() + acceleration);
	}
	
	public void combine(DirectionModifier mod) {
		angleStart += mod.getAccelerationStart();
		angleEnd += mod.getAccelerationEnd();
		rotationStart += mod.getRotationStart();
		rotationEnd += mod.getRotationEnd();
		speedStart += mod.getSpeedStart();
		speedEnd += mod.getSpeedEnd();
		accelerationStart += mod.getAccelerationStart();
		accelerationEnd += mod.getAccelerationEnd();
	}

	public DirectionModifier clone() {
		DirectionModifier d = new DirectionModifier();

		d.setAngleStart(angleStart);
		d.setAngleEnd(angleEnd);
		d.setRotationStart(rotationStart);
		d.setRotationEnd(rotationEnd);
		d.setSpeedStart(speedStart);
		d.setSpeedEnd(speedEnd);
		d.setAccelerationStart(accelerationStart);
		d.setAccelerationEnd(accelerationEnd);

		return d;
	}

}
