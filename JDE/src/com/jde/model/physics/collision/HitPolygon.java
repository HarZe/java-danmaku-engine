package com.jde.model.physics.collision;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import com.jde.model.physics.Direction;
import com.jde.model.physics.Movement;
import com.jde.model.physics.Vertex;

/**
 * This HitPolygon class represents a colliding object with a convex polygon
 * shape
 * 
 * @author HarZe (David Serrano)
 */

public class HitPolygon implements HitZone {

	/** List of vertexes of the polygon */
	protected LinkedList<Vertex> vertexes;
	/** List of the normal vertexes (perpendicular to each edge) */
	protected LinkedList<Vertex> normals;
	/** Radius of the circumcircle */
	protected double radius = 0;

	/**
	 * Basic constructor
	 * 
	 * @param vtcs
	 *            List of vertexes sorted clockwise
	 */
	public HitPolygon(LinkedList<Vertex> vtcs) {
		vertexes = vtcs;
		normals = new LinkedList<Vertex>();

		if (vertexes.size() >= 2) {
			Iterator<Vertex> it = vertexes.iterator();
			Vertex previousVertex = it.next();
			radius = previousVertex.length();

			// Calculating the normals
			while (it.hasNext()) {
				Vertex v = it.next();
				normals.add(new Vertex(previousVertex, v).normal().opposite()
						.unit());
				radius = Math.max(radius, v.length());
				previousVertex = v;
			}

			normals.add(new Vertex(vertexes.getLast(), vertexes.getFirst())
					.normal().opposite().unit());
		}
	}

	/**
	 * Returns a copy of the HitPolygon
	 */
	public HitPolygon clone() {
		return new HitPolygon(vertexes);
	}

	// self and collider MUST be clons
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
		int steps = (int) (maxDistanceToCollide / 6.0);
		if (steps < 6)
			steps = 6;

		// Step collision (the forward segment is divided and processed to get
		// accurate results)
		double stepMs = ms / (steps - 1);
		for (int step = 0; step < steps; step++) {

			if (isInside(self, other.getPosition())
					|| pierces(self, other, stepMs))
				return true;

			if (step < steps - 1) {
				self.forward(stepMs);
				other.forward(stepMs);
			}
		}

		return false;
	}

	/**
	 * This method uses the Cyrus-Beck algorithm to check piercing
	 * 
	 * @param self
	 *            Movement (position) of this polygon
	 * @param other
	 *            Movement of the piercing object
	 * @param ms
	 *            Time to forward
	 * @return True if the object pierces the polygon
	 */
	protected boolean cyrusBeck(Movement self, Movement other, double ms) {

		double tIn = 0;
		double tOut = 1;
		double tHit;
		boolean found = false;

		LinkedList<Vertex> fixedVerteces = new LinkedList<Vertex>();
		for (Vertex v : vertexes)
			fixedVerteces.add(self
					.getPosition()
					.clone()
					.add(v.clone().rotate(
							Math.toRadians(self.getDrawingAngle()))));

		Vertex pos = other.getPosition();
		Direction cDir = other.getCurrentDirection();
		Vertex dir = Vertex.angleToVertex(Math.toRadians(cDir.getAngle()))
				.scale(cDir.getSpeed() * 0.001 * ms);

		Iterator<Vertex> itV = vertexes.iterator();
		Iterator<Vertex> itN = normals.iterator();

		while (!found && itN.hasNext()) {
			Vertex pi = itV.next();
			Vertex ni = itN.next();

			Vertex pi_Pos = new Vertex(pi, pos);
			double num = pi_Pos.opposite().scalar(ni);
			double den = dir.scalar(ni);
			double pi_Pos_scalar_ni = pi_Pos.scalar(ni);

			if (den == 0 && pi_Pos_scalar_ni >= 0)
				found = true;
			else if (!(den == 0 && pi_Pos_scalar_ni < 0)) {
				tHit = num / den;

				if (den < 0)
					if (tHit > tIn)
						tIn = tHit;
					else if (den > 0)
						tOut = Math.min(tOut, tHit);

				found = tIn > tOut;
			}
		}

		return tIn > 0 && tIn <= tOut;
	}

	@Override
	public void expand(double size) {
		for (Vertex v : vertexes)
			v.resize(v.length() + size);
	}

	@Override
	public boolean isInside(Movement self, Vertex other) {
		ArrayList<Vertex> fixedVerteces = new ArrayList<Vertex>();
		for (Vertex v : vertexes)
			fixedVerteces.add(self
					.getPosition()
					.clone()
					.add(v.clone().rotate(
							Math.toRadians(self.getDrawingAngle()))));

		// Checks if the object is always in the same side (which means is
		// inside)
		int last_side = 0, current_side = 0;
		for (int i = 0; i < fixedVerteces.size(); i++) {
			Vertex a = fixedVerteces.get(i);
			Vertex b = fixedVerteces.get((i + 1) % fixedVerteces.size());

			Vertex edge = b.clone().sub(a);
			Vertex point = other.clone().sub(a);
			double cp = edge.crossProduct(point);
			current_side = cp > 0 ? 1 : -1;

			if (cp == 0)
				return false;
			else if (last_side == 0)
				last_side = current_side;
			else if (last_side != current_side)
				return false;
		}

		return true;
	}

	/**
	 * This method detects if a moving point is piercing the polygon
	 * 
	 * @param self
	 *            Movement (position) of this polygon
	 * @param other
	 *            Movement of the piercing object
	 * @param ms
	 *            Milliseconds to forward
	 * @return True if the object pierces the polygon
	 */
	protected boolean pierces(Movement self, Movement other, double ms) {
		return cyrusBeck(self, other, ms);
	}

}
