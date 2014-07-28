package com.jde.model.physics;


public class DirectionModifier {

	// Direction basic parameters ranges
	protected double angleStart = 0;
	protected double angleEnd = 0;

	protected double rotationStart = 0;
	protected double rotationEnd = 0;

	protected double speedStart = 0;
	protected double speedEnd = 0;

	protected double accelerationStart = 0;
	protected double accelerationEnd = 0;
	
	// Random offset ranges
	protected double randomAngleOffsetStart = 0;
	protected double randomAngleOffsetEnd = 0;
	
	protected double randomRotationOffsetStart = 0;
	protected double randomRotationOffsetEnd = 0;
	
	protected double randomMotionOffsetStart = 0;
	protected double randomMotionOffsetEnd = 0;
	
	protected double randomSpeedOffsetStart = 0;
	protected double randomSpeedOffsetEnd = 0;
	
	protected double randomAccelerationOffsetStart = 0;
	protected double randomAccelerationOffsetEnd = 0;

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

	public double getRandomAngleOffsetStart() {
		return randomAngleOffsetStart;
	}

	public void setRandomAngleOffsetStart(double randomAngleOffsetStart) {
		this.randomAngleOffsetStart = randomAngleOffsetStart;
	}

	public double getRandomAngleOffsetEnd() {
		return randomAngleOffsetEnd;
	}

	public void setRandomAngleOffsetEnd(double randomAngleOffsetEnd) {
		this.randomAngleOffsetEnd = randomAngleOffsetEnd;
	}

	public double getRandomRotationOffsetStart() {
		return randomRotationOffsetStart;
	}

	public void setRandomRotationOffsetStart(double randomRotationOffsetStart) {
		this.randomRotationOffsetStart = randomRotationOffsetStart;
	}

	public double getRandomRotationOffsetEnd() {
		return randomRotationOffsetEnd;
	}

	public void setRandomRotationOffsetEnd(double randomRotationOffsetEnd) {
		this.randomRotationOffsetEnd = randomRotationOffsetEnd;
	}

	public double getRandomSpeedOffsetStart() {
		return randomSpeedOffsetStart;
	}

	public void setRandomSpeedOffsetStart(double randomSpeedOffsetStart) {
		this.randomSpeedOffsetStart = randomSpeedOffsetStart;
	}

	public double getRandomSpeedOffsetEnd() {
		return randomSpeedOffsetEnd;
	}

	public void setRandomSpeedOffsetEnd(double randomSpeedOffsetEnd) {
		this.randomSpeedOffsetEnd = randomSpeedOffsetEnd;
	}

	public double getRandomAccelerationOffsetStart() {
		return randomAccelerationOffsetStart;
	}

	public void setRandomAccelerationOffsetStart(
			double randomAccelerationOffsetStart) {
		this.randomAccelerationOffsetStart = randomAccelerationOffsetStart;
	}

	public double getRandomAccelerationOffsetEnd() {
		return randomAccelerationOffsetEnd;
	}

	public void setRandomAccelerationOffsetEnd(double randomAccelerationOffsetEnd) {
		this.randomAccelerationOffsetEnd = randomAccelerationOffsetEnd;
	}

	public double getRandomMotionOffsetStart() {
		return randomMotionOffsetStart;
	}

	public void setRandomMotionOffsetStart(double randomMotionOffsetStart) {
		this.randomMotionOffsetStart = randomMotionOffsetStart;
	}

	public double getRandomMotionOffsetEnd() {
		return randomMotionOffsetEnd;
	}

	public void setRandomMotionOffsetEnd(double randomMotionOffsetEnd) {
		this.randomMotionOffsetEnd = randomMotionOffsetEnd;
	}

	public void modify(int step, int total, Direction dir) {
		
		if (total == 1)
			step = 0;
		else
			total--;

		double angleStep = (angleEnd - angleStart) * step / total;
		double rotationStep = (rotationEnd - rotationStart) * step / total;
		double speedStep = (speedEnd - speedStart) * step / total;
		double accelerationStep = (accelerationEnd - accelerationStart) * step / total;
		double randomAngleOffsetStep = (randomAngleOffsetEnd - randomAngleOffsetStart) * step / total;
		double randomRotationOffsetStep = (randomRotationOffsetEnd - randomRotationOffsetStart) * step / total;
		double randomMotionOffsetStep = (randomMotionOffsetEnd - randomMotionOffsetStart) * step / total;
		double randomSpeedOffsetStep = (randomSpeedOffsetEnd - randomSpeedOffsetStart) * step / total;
		double randomAccelerationOffsetStep = (randomAccelerationOffsetEnd - randomAccelerationOffsetStart) * step / total;

		double angle = angleStart + angleStep;
		double rotation = rotationStart + rotationStep;
		double speed = speedStart + speedStep;
		double acceleration = accelerationStart + accelerationStep;
		double randomAngleOffset = randomAngleOffsetStart + randomAngleOffsetStep;
		double randomRotationOffset = randomRotationOffsetStart + randomRotationOffsetStep;
		double randomMotionOffset = randomMotionOffsetStart + randomMotionOffsetStep;
		double randomSpeedOffset = randomSpeedOffsetStart + randomSpeedOffsetStep;
		double randomAccelerationOffset = randomAccelerationOffsetStart + randomAccelerationOffsetStep;

		dir.setAngle(dir.getAngle() + angle);
		dir.setRotation(dir.getRotation() + rotation);
		dir.setSpeed(dir.getSpeed() + speed);
		dir.setAcceleration(dir.getAcceleration() + acceleration);
		dir.setRandomAngleOffset(dir.getRandomAngleOffset() + randomAngleOffset);
		dir.setRandomRotationOffset(dir.getRandomRotationOffset() + randomRotationOffset);
		dir.setRandomMotionOffset(dir.getRandomMotionOffset() + randomMotionOffset);
		dir.setRandomSpeedOffset(dir.getRandomSpeedOffset() + randomSpeedOffset);
		dir.setRandomAccelerationOffset(dir.getRandomAccelerationOffset() + randomAccelerationOffset);
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
		d.setRandomAngleOffsetStart(randomAngleOffsetStart);
		d.setRandomAngleOffsetEnd(randomAngleOffsetEnd);
		d.setRandomRotationOffsetStart(randomRotationOffsetStart);
		d.setRandomRotationOffsetEnd(randomRotationOffsetEnd);
		d.setRandomMotionOffsetStart(randomMotionOffsetStart);
		d.setRandomMotionOffsetEnd(randomMotionOffsetEnd);
		d.setRandomSpeedOffsetStart(randomSpeedOffsetStart);
		d.setRandomRotationOffsetEnd(randomRotationOffsetEnd);
		d.setRandomAccelerationOffsetStart(randomAccelerationOffsetStart);
		d.setRandomAccelerationOffsetEnd(randomAccelerationOffsetEnd);

		return d;
	}

}
