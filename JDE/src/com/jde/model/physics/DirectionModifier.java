package com.jde.model.physics;

/**
 * This DirectionModifier class contains a range (start and end) for all the
 * parameters in a Direction, in order to iterate and modify it
 * 
 * @author HarZe (David Serrano)
 */

public class DirectionModifier {

	// Direction basic parameters ranges

	/** Initial angle (direction) in degrees */
	protected double angleStart = 0;
	/** Final angle (direction) in degrees */
	protected double angleEnd = 0;

	/** Initial rotation in degrees per second */
	protected double rotationStart = 0;
	/** Final rotation in degrees per second */
	protected double rotationEnd = 0;

	/** Initial rotation acceleration (motion) in degrees per second² */
	protected double motionStart = 0;
	/** Final rotation acceleration (motion) in degrees per second² */
	protected double motionEnd = 0;

	/** Initial speed in vgapx per second */
	protected double speedStart = 0;
	/** Final speed in vgapx per second */
	protected double speedEnd = 0;

	/** Initial acceleration in vgapx per second² */
	protected double accelerationStart = 0;
	/** Final acceleration in vgapx per second² */
	protected double accelerationEnd = 0;

	// Random offset ranges

	/** Initial maximum random offset angle (direction) in degrees */
	protected double randomAngleOffsetStart = 0;
	/** Final maximum random offset angle (direction) in degrees */
	protected double randomAngleOffsetEnd = 0;

	/** Initial maximum random offset rotation in degrees per second */
	protected double randomRotationOffsetStart = 0;
	/** Final maximum random offset rotation in degrees per second */
	protected double randomRotationOffsetEnd = 0;

	/** Initial maximum random offset motion in degrees per second² */
	protected double randomMotionOffsetStart = 0;
	/** Final maximum random offset motion in degrees per second² */
	protected double randomMotionOffsetEnd = 0;

	/** Initial maximum random offset speed in vgapx per second */
	protected double randomSpeedOffsetStart = 0;
	/** Final maximum random offset speed in vgapx per second */
	protected double randomSpeedOffsetEnd = 0;

	/** Initial maximum random offset acceleration in vgapx per second² */
	protected double randomAccelerationOffsetStart = 0;
	/** Final maximum random offset acceleration in vgapx per second² */
	protected double randomAccelerationOffsetEnd = 0;

	// Exponent modifiers

	/** Exponent modifier of the angle */
	protected double angleExponent = 1;
	/** Exponent modifier of the rotation */
	protected double rotationExponent = 1;
	/** Exponent modifier of the motion */
	protected double motionExponent = 1;
	/** Exponent modifier of the speed */
	protected double speedExponent = 1;
	/** Exponent modifier of the acceleration */
	protected double accelerationExponent = 1;

	// Homing modifiers

	/** Initial offset of the homing direction */
	protected double homingOffsetStart = 0;
	/** Final offset of the homing direction */
	protected double homingOffsetEnd = 0;

	/**
	 * Default constructor
	 */
	public DirectionModifier() {

	}

	/**
	 * Returns a copy of the direction modifier
	 */
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

		d.setHomingOffsetStart(homingOffsetStart);
		d.setHomingOffsetEnd(homingOffsetEnd);

		return d;
	}

	public double getAccelerationEnd() {
		return accelerationEnd;
	}

	public double getAccelerationExponent() {
		return accelerationExponent;
	}

	public double getAccelerationStart() {
		return accelerationStart;
	}

	public double getAngleEnd() {
		return angleEnd;
	}

	public double getAngleExponent() {
		return angleExponent;
	}

	public double getAngleStart() {
		return angleStart;
	}

	public double getHomingOffsetEnd() {
		return homingOffsetEnd;
	}

	public double getHomingOffsetStart() {
		return homingOffsetStart;
	}

	public double getMotionEnd() {
		return motionEnd;
	}

	public double getMotionExponent() {
		return motionExponent;
	}

	public double getMotionStart() {
		return motionStart;
	}

	public double getRandomAccelerationOffsetEnd() {
		return randomAccelerationOffsetEnd;
	}

	public double getRandomAccelerationOffsetStart() {
		return randomAccelerationOffsetStart;
	}

	public double getRandomAngleOffsetEnd() {
		return randomAngleOffsetEnd;
	}

	public double getRandomAngleOffsetStart() {
		return randomAngleOffsetStart;
	}

	public double getRandomMotionOffsetEnd() {
		return randomMotionOffsetEnd;
	}

	public double getRandomMotionOffsetStart() {
		return randomMotionOffsetStart;
	}

	public double getRandomRotationOffsetEnd() {
		return randomRotationOffsetEnd;
	}

	public double getRandomRotationOffsetStart() {
		return randomRotationOffsetStart;
	}

	public double getRandomSpeedOffsetEnd() {
		return randomSpeedOffsetEnd;
	}

	public double getRandomSpeedOffsetStart() {
		return randomSpeedOffsetStart;
	}

	public double getRotationEnd() {
		return rotationEnd;
	}

	public double getRotationExponent() {
		return rotationExponent;
	}

	public double getRotationStart() {
		return rotationStart;
	}

	public double getSpeedEnd() {
		return speedEnd;
	}

	public double getSpeedExponent() {
		return speedExponent;
	}

	public double getSpeedStart() {
		return speedStart;
	}

	/**
	 * This method modifies the given Direction using the parameters of the
	 * calling Direction modifier, specifying the point between "start" and
	 * "end" giving the step number from a total
	 * 
	 * @param step
	 *            Number of the step, must be: 0 <= step < total
	 * @param total
	 *            Total number of steps, must be greater than 0
	 * @param dir
	 *            The direction to modify
	 */
	public void modify(int step, int total, Direction dir) {

		if (total == 1)
			step = 0;
		else
			total--;

		double fixedStep = ((double) step) / total;

		// Exponential parameter calculus
		double angle = angleStart + (angleEnd - angleStart)
				* Math.pow(fixedStep, angleExponent);
		double rotation = rotationStart + (rotationEnd - rotationStart)
				* Math.pow(fixedStep, rotationExponent);
		double motion = motionStart + (motionEnd - motionStart)
				* Math.pow(fixedStep, motionExponent);
		double speed = speedStart + (speedEnd - speedStart)
				* Math.pow(fixedStep, speedExponent);
		double acceleration = accelerationStart
				+ (accelerationEnd - accelerationStart)
				* Math.pow(fixedStep, accelerationExponent);

		// Random offsets linear calculus
		double randomAngleOffsetStep = (randomAngleOffsetEnd - randomAngleOffsetStart)
				* fixedStep;
		double randomRotationOffsetStep = (randomRotationOffsetEnd - randomRotationOffsetStart)
				* fixedStep;
		double randomMotionOffsetStep = (randomMotionOffsetEnd - randomMotionOffsetStart)
				* fixedStep;
		double randomSpeedOffsetStep = (randomSpeedOffsetEnd - randomSpeedOffsetStart)
				* fixedStep;
		double randomAccelerationOffsetStep = (randomAccelerationOffsetEnd - randomAccelerationOffsetStart)
				* fixedStep;

		double randomAngleOffset = randomAngleOffsetStart
				+ randomAngleOffsetStep;
		double randomRotationOffset = randomRotationOffsetStart
				+ randomRotationOffsetStep;
		double randomMotionOffset = randomMotionOffsetStart
				+ randomMotionOffsetStep;
		double randomSpeedOffset = randomSpeedOffsetStart
				+ randomSpeedOffsetStep;
		double randomAccelerationOffset = randomAccelerationOffsetStart
				+ randomAccelerationOffsetStep;

		// Homing offset calculus
		double homingOffset = (homingOffsetEnd - homingOffsetStart) * fixedStep;

		// Final sets
		dir.setAngle(dir.getAngle() + angle);
		dir.setRotation(dir.getRotation() + rotation);
		dir.setMotion(dir.getMotion() + motion);
		dir.setSpeed(dir.getSpeed() + speed);
		dir.setAcceleration(dir.getAcceleration() + acceleration);

		dir.setRandomAngleOffset(dir.getRandomAngleOffset() + randomAngleOffset);
		dir.setRandomRotationOffset(dir.getRandomRotationOffset()
				+ randomRotationOffset);
		dir.setRandomMotionOffset(dir.getRandomMotionOffset()
				+ randomMotionOffset);
		dir.setRandomSpeedOffset(dir.getRandomSpeedOffset() + randomSpeedOffset);
		dir.setRandomAccelerationOffset(dir.getRandomAccelerationOffset()
				+ randomAccelerationOffset);

		dir.setHomingOffset(homingOffset);
	}

	public void setAccelerationEnd(double accelerationEnd) {
		this.accelerationEnd = accelerationEnd;
	}

	public void setAccelerationExponent(double accelerationExponent) {
		this.accelerationExponent = accelerationExponent;
	}

	public void setAccelerationStart(double accelerationStart) {
		this.accelerationStart = accelerationStart;
	}

	public void setAngleEnd(double angleEnd) {
		this.angleEnd = angleEnd;
	}

	public void setAngleExponent(double angleExponent) {
		this.angleExponent = angleExponent;
	}

	public void setAngleStart(double angleStart) {
		this.angleStart = angleStart;
	}

	public void setHomingOffsetEnd(double homingOffsetEnd) {
		this.homingOffsetEnd = homingOffsetEnd;
	}

	public void setHomingOffsetStart(double homingOffsetStart) {
		this.homingOffsetStart = homingOffsetStart;
	}

	public void setMotionEnd(double motionEnd) {
		this.motionEnd = motionEnd;
	}

	public void setMotionExponent(double motionExponent) {
		this.motionExponent = motionExponent;
	}

	public void setMotionStart(double motionStart) {
		this.motionStart = motionStart;
	}

	public void setRandomAccelerationOffsetEnd(
			double randomAccelerationOffsetEnd) {
		this.randomAccelerationOffsetEnd = randomAccelerationOffsetEnd;
	}

	public void setRandomAccelerationOffsetStart(
			double randomAccelerationOffsetStart) {
		this.randomAccelerationOffsetStart = randomAccelerationOffsetStart;
	}

	public void setRandomAngleOffsetEnd(double randomAngleOffsetEnd) {
		this.randomAngleOffsetEnd = randomAngleOffsetEnd;
	}

	public void setRandomAngleOffsetStart(double randomAngleOffsetStart) {
		this.randomAngleOffsetStart = randomAngleOffsetStart;
	}

	public void setRandomMotionOffsetEnd(double randomMotionOffsetEnd) {
		this.randomMotionOffsetEnd = randomMotionOffsetEnd;
	}

	public void setRandomMotionOffsetStart(double randomMotionOffsetStart) {
		this.randomMotionOffsetStart = randomMotionOffsetStart;
	}

	public void setRandomRotationOffsetEnd(double randomRotationOffsetEnd) {
		this.randomRotationOffsetEnd = randomRotationOffsetEnd;
	}

	public void setRandomRotationOffsetStart(double randomRotationOffsetStart) {
		this.randomRotationOffsetStart = randomRotationOffsetStart;
	}

	public void setRandomSpeedOffsetEnd(double randomSpeedOffsetEnd) {
		this.randomSpeedOffsetEnd = randomSpeedOffsetEnd;
	}

	public void setRandomSpeedOffsetStart(double randomSpeedOffsetStart) {
		this.randomSpeedOffsetStart = randomSpeedOffsetStart;
	}

	public void setRotationEnd(double rotationEnd) {
		this.rotationEnd = rotationEnd;
	}

	public void setRotationExponent(double rotationExponent) {
		this.rotationExponent = rotationExponent;
	}

	public void setRotationStart(double rotationStart) {
		this.rotationStart = rotationStart;
	}

	public void setSpeedEnd(double speedEnd) {
		this.speedEnd = speedEnd;
	}

	public void setSpeedExponent(double speedExponent) {
		this.speedExponent = speedExponent;
	}

	public void setSpeedStart(double speedStart) {
		this.speedStart = speedStart;
	}

}
