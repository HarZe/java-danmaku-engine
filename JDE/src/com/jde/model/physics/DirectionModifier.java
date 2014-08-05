package com.jde.model.physics;


public class DirectionModifier {

	// Direction basic parameters ranges
	protected double angleStart = 0;
	protected double angleEnd = 0;

	protected double rotationStart = 0;
	protected double rotationEnd = 0;

	protected double motionStart = 0;
	protected double motionEnd = 0;
	
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

	// Exponent modifiers
	protected double angleExponent = 1;
	protected double rotationExponent = 1;
	protected double motionExponent = 1;
	protected double speedExponent = 1;
	protected double accelerationExponent = 1;
	
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

	public double getMotionStart() {
		return motionStart;
	}

	public void setMotionStart(double motionStart) {
		this.motionStart = motionStart;
	}

	public double getMotionEnd() {
		return motionEnd;
	}

	public void setMotionEnd(double motionEnd) {
		this.motionEnd = motionEnd;
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

	public double getAngleExponent() {
		return angleExponent;
	}

	public void setAngleExponent(double angleExponent) {
		this.angleExponent = angleExponent;
	}

	public double getRotationExponent() {
		return rotationExponent;
	}

	public void setRotationExponent(double rotationExponent) {
		this.rotationExponent = rotationExponent;
	}

	public double getMotionExponent() {
		return motionExponent;
	}

	public void setMotionExponent(double motionExponent) {
		this.motionExponent = motionExponent;
	}

	public double getSpeedExponent() {
		return speedExponent;
	}

	public void setSpeedExponent(double speedExponent) {
		this.speedExponent = speedExponent;
	}

	public double getAccelerationExponent() {
		return accelerationExponent;
	}

	public void setAccelerationExponent(double accelerationExponent) {
		this.accelerationExponent = accelerationExponent;
	}

	public void modify(int step, int total, Direction dir) {
		
		if (total == 1)
			step = 0;
		else
			total--;
		
		double fixedStep = ((double) step) / total;

		// Exponential parameter calculus
		double angle = angleStart + (angleEnd - angleStart)*Math.pow(fixedStep, angleExponent);
		double rotation = rotationStart + (rotationEnd - rotationStart)*Math.pow(fixedStep, rotationExponent);
		double motion = motionStart + (motionEnd - motionStart)*Math.pow(fixedStep, motionExponent);
		double speed = speedStart + (speedEnd - speedStart)*Math.pow(fixedStep, speedExponent);
		double acceleration = accelerationStart + (accelerationEnd - accelerationStart)*Math.pow(fixedStep, accelerationExponent);
		
		// Random offsets linear calculus
		double randomAngleOffsetStep = (randomAngleOffsetEnd - randomAngleOffsetStart) * fixedStep;
		double randomRotationOffsetStep = (randomRotationOffsetEnd - randomRotationOffsetStart) * fixedStep;
		double randomMotionOffsetStep = (randomMotionOffsetEnd - randomMotionOffsetStart) * fixedStep;
		double randomSpeedOffsetStep = (randomSpeedOffsetEnd - randomSpeedOffsetStart) * fixedStep;
		double randomAccelerationOffsetStep = (randomAccelerationOffsetEnd - randomAccelerationOffsetStart) * fixedStep;
		
		double randomAngleOffset = randomAngleOffsetStart + randomAngleOffsetStep;
		double randomRotationOffset = randomRotationOffsetStart + randomRotationOffsetStep;
		double randomMotionOffset = randomMotionOffsetStart + randomMotionOffsetStep;
		double randomSpeedOffset = randomSpeedOffsetStart + randomSpeedOffsetStep;
		double randomAccelerationOffset = randomAccelerationOffsetStart + randomAccelerationOffsetStep;

		// Final sets
		dir.setAngle(dir.getAngle() + angle);
		dir.setRotation(dir.getRotation() + rotation);
		dir.setMotion(dir.getMotion() + motion);
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
		d.setMotionStart(motionStart);
		d.setMotionEnd(motionEnd);
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
		
		d.setAngleExponent(angleExponent);
		d.setRotationExponent(rotationExponent);
		d.setMotionExponent(motionExponent);
		d.setSpeedExponent(speedExponent);
		d.setAccelerationExponent(accelerationExponent);

		return d;
	}

}
