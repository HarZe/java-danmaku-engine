package com.jde.model.physics;

import java.util.ArrayList;

/**
 * This Movement class contains all the data about the position and direction of
 * an entity
 * 
 * @author HarZe (David Serrano)
 */
public class Movement {

	/** Position of the entity */
	protected Vertex position;
	/** Current direction of the entity */
	protected Direction direction;
	/** Self-rotation angle (in degrees) */
	protected double spin = 90;
	/**
	 * If enabled, the entity will rotate itself to face the direction is
	 * heading
	 */
	protected boolean lookAtMovingDirection = true;

	/** A list with all the directions that the entity will follow */
	protected ArrayList<Direction> directions;
	/** Index of the current direction of the list */
	protected int currentDir = 0;
	/** Elapsed time since entity started moving using the current direction */
	protected double elapsed = 0;

	/**
	 * Basic constructor, which must provide a position and a list of directions
	 * 
	 * @param position
	 *            Initial position point
	 * @param directions
	 *            List of directions
	 */
	public Movement(Vertex position, ArrayList<Direction> directions) {
		this.position = position;
		this.directions = new ArrayList<Direction>();

		for (Direction d : directions)
			this.directions.add(d.clone());

		direction = this.directions.get(currentDir);
	}

	/**
	 * Applies the randomization to all the directions in the list
	 */
	public void applyRandomization() {
		for (Direction d : directions)
			d.applyRandomitazion();
	}

	/**
	 * Returns a copy of the movement
	 */
	public Movement clone() {
		Movement m = new Movement(position.clone(), directions);
		m.currentDir = currentDir;
		m.direction = m.directions.get(currentDir);
		m.elapsed = elapsed;
		m.spin = spin;
		m.lookAtMovingDirection = lookAtMovingDirection;
		return m;
	}

	/**
	 * It makes the position change based on the current direction and time
	 * passed
	 * 
	 * @param ms
	 *            The amount of time to go forward, in milliseconds
	 */
	public void forward(double ms) {
		elapsed += ms; // Adds time to the elapsed time counter

		// Check if current direction has ended and there is a next one ready
		if (currentDir + 1 < directions.size() && direction.getDuration() >= 0
				&& direction.getDuration() < elapsed) {

			// Reset the elapsed time counter
			elapsed -= direction.getDuration();
			// Forward the remaining time of the finishing direction
			forwardAux(ms - elapsed);

			// Getting the new direction
			currentDir++;
			Direction newDir = directions.get(currentDir);

			// Add the previous direction values if inheritance is enabled
			if (direction.isInheritance()) {
				newDir.setAngle(newDir.getAngle() + direction.getAngle());
				newDir.setRotation(newDir.getRotation()
						+ direction.getRotation());
				newDir.setMotion(newDir.getMotion() + direction.getMotion());
				newDir.setSpeed(newDir.getSpeed() + direction.getSpeed());
				newDir.setAcceleration(newDir.getAcceleration()
						+ direction.getAcceleration());
			}

			// Set the new direction and remaining time for the new direction
			direction = newDir;
			ms = elapsed;
		}

		// Finally, forward the current direction
		forwardAux(ms);
	}

	private void forwardAux(double ms) {
		// Modifying the speeds based on their accelerations
		direction.setSpeed(direction.getSpeed() + direction.getAcceleration()
				* ms * 0.001);
		direction.setRotation(direction.getRotation() + direction.getMotion()
				* ms * 0.001);

		// If homing is enabled, homing position is used
		if (direction.isHoming())
			direction.setAngle(direction.getHomingPosition().clone()
					.sub(position)
					.rotate(Math.toRadians(direction.getHomingOffset()))
					.angle());
		// If homing is disabled, angle and rotation are used
		else
			direction.setAngle(direction.getAngle()
					+ (direction.getRotation() * ms * 0.001));

		// Getting the vertex of the new angle
		Vertex step = Vertex
				.angleToVertex(Math.toRadians(direction.getAngle()));
		// Resize it to match the speed
		step.scale(direction.getSpeed() * ms * 0.001);
		
		// Finally, forward the position
		position.add(step);
	}

	public Direction getCurrentDirection() {
		return direction;
	}

	public ArrayList<Direction> getDirections() {
		return directions;
	}

	/**
	 * @return The angle (degrees) used to draw the sprite
	 */
	public double getDrawingAngle() {
		return spin + (lookAtMovingDirection ? direction.getAngle() : 0);
	}

	public Vertex getPosition() {
		return position;
	}

	public double getSpin() {
		return spin;
	}

	public boolean isLookAtMovingDirection() {
		return lookAtMovingDirection;
	}

	public void setLookAtMovingDirection(boolean lookAtMovingDirection) {
		this.lookAtMovingDirection = lookAtMovingDirection;
	}

	public void setPosition(Vertex position) {
		this.position = position;
	}

	public void setSpin(double spin) {
		this.spin = spin;
	}
}
