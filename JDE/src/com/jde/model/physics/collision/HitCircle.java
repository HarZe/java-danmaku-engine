package com.jde.model.physics.collision;

import com.jde.model.physics.Direction;
import com.jde.model.physics.Movement;
import com.jde.model.physics.Vertex;

/**
 * This HitCircle class represents a colliding object with a circle shape
 * 
 * @author HarZe (David Serrano)
 */

public class HitCircle implements HitZone {

	/** Center position of the circle */
	protected Vertex center;
	/** Radius of the circle, in vgapx */
	protected double radius;

	/**
	 * Basic constructor
	 * 
	 * @param center
	 *            Center of the circle
	 * @param radius
	 *            Radius of the circle
	 */
	public HitCircle(Vertex center, double radius) {
		this.center = center;
		this.radius = radius;
	}

	/**
	 * Returns a copy of the HitCircle
	 */
	public HitCircle clone() {
		return new HitCircle(center.clone(), radius);
	}

	// self and other MUST be clones
	@Override
	public boolean collides(Movement self, Movement other, double ms) {

		// Aprox. distance check (counting acceleration)
		double SECURE_MARGIN = 1.2;
		double absSelfSpeed = Math.abs(self.getCurrentDirection().getSpeed());
		double absCollSpeed = Math.abs(other.getCurrentDirection().getSpeed());
		double maxSelfSpeed = Math.max(absSelfSpeed, absSelfSpeed
				+ self.getCurrentDirection().getAcceleration() * 0.001 * ms);
		double maxColliderSpeed = Math.max(absCollSpeed, absCollSpeed
				+ other.getCurrentDirection().getAcceleration() * 0.001 * ms);
		double maxDistanceToCollide = radius
				+ (maxSelfSpeed + maxColliderSpeed) * 0.001 * ms;
		if (SECURE_MARGIN * maxDistanceToCollide < self.getPosition()
				.distanceTo(other.getPosition()))
			return false;

		// Necessary steps for accurate collision
		int steps = (int) (maxDistanceToCollide / 2.0);
		if (steps < 2)
			steps = 2;

		// Step collision (the forward segment is divided and processed to get
		// accurate results)
		double stepMs = ms / (steps - 1);
		for (int step = 0; step < steps; step++) {

			if (isInside(self, other.getPosition())
					|| pierces(self.getPosition(), other, stepMs))
				return true;

			if (step < steps - 1) {
				self.forward(stepMs);
				other.forward(stepMs);
			}
		}

		return false;
	}

	@Override
	public void expand(double size) {
		radius += size;
	}

	public Vertex getCenter() {
		return center;
	}

	public double getRadius() {
		return radius;
	}

	@Override
	public boolean isInside(Movement self, Vertex collider) {
		return center.clone().add(self.getPosition()).distanceTo(collider) <= radius;
	}

	/**
	 * This method detects if a moving point is piercing the circle
	 * 
	 * @param selfPos
	 *            Position of this circle
	 * @param other
	 *            Movement of the piercing object
	 * @param ms
	 *            Milliseconds to forward
	 * @return True if the object pierces the circle
	 */
	protected boolean pierces(Vertex selfPos, Movement other, double ms) {
		selfPos = selfPos.clone().add(center);
		Direction cDir = other.getCurrentDirection();
		Vertex v = Vertex.angleToVertex(Math.toRadians(cDir.getAngle())).scale(
				cDir.getSpeed() * 0.001 * ms);
		Vertex cp = new Vertex(selfPos, other.getPosition());

		double r2 = radius * radius;
		double a = v.scalar(v);
		double b = 2 * (cp.scalar(v));
		double c = cp.scalar(cp) - r2;

		double deltha = b * b - 4 * a * c;

		if (deltha >= 0) {
			double t = (-b - Math.sqrt(deltha)) / (2 * a);
			if ((t <= 1) && (t > 0))
				return true;
		}

		return false;
	}

	public void setCenter(Vertex center) {
		this.center = center;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}
}
