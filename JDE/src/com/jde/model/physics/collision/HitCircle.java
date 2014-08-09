package com.jde.model.physics.collision;

import com.jde.model.physics.Direction;
import com.jde.model.physics.Movement;
import com.jde.model.physics.Vertex;

public class HitCircle implements HitZone {

	protected Vertex center;
	protected double radius;
	
	public HitCircle(Vertex center, double radius) {
		this.center = center;
		this.radius = radius;
	}

	public Vertex getCenter() {
		return center;
	}

	public void setCenter(Vertex center) {
		this.center = center;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	@Override
	public boolean isInside(Movement self, Vertex collider) {
		return center.clone().add(self.getPosition()).distanceTo(collider) <= radius;
	}
	
	protected boolean pierces(Vertex selfPos, Movement collider, double ms) {
		selfPos = selfPos.clone().add(center);
		Direction cDir = collider.getDirection();
		Vertex v = Vertex.angleToVertex(cDir.getAngle()).scale(cDir.getSpeed() * 0.001 * ms);
		Vertex cp = new Vertex(selfPos, collider.getPosition());
		
		double r2 = radius * radius;
		double a = v.scalar(v);
		double b = 2 * (cp.scalar(v));
		double c = cp.scalar(cp) - r2;

		double deltha = b*b - 4*a*c;

		if (deltha >= 0) {
			double t = (-b - Math.sqrt(deltha)) / (2 * a);
			if ((t <= 1) && (t > 0))
				return true;
		}
		
		return false;
	}

	// self and collider MUST be clons
	@Override
	public boolean collides(Movement self, Movement collider, double ms) {
		
		// Aprox. distance check (counting acceleration)
		final double SECURE_MARGIN = 1.2;
		double maxSelfSpeed = Math.max(self.getDirection().getSpeed(), self.getDirection().getSpeed() + self.getDirection().getAcceleration() * 0.001 * ms);
		double maxCooliderSpeed = Math.max(collider.getDirection().getSpeed(), collider.getDirection().getSpeed() + collider.getDirection().getAcceleration() * 0.001 * ms);
		double maxDistanceToCollide = (maxSelfSpeed + maxCooliderSpeed) * 0.001 * ms;
		if (SECURE_MARGIN*maxDistanceToCollide < self.getPosition().distanceTo(collider.getPosition()))
			return false;
		
		// Necessary steps for accurate collision
		double maxDistance = maxSelfSpeed * 0.001 * ms;
		int steps = (int) Math.floor(3 * maxDistance / radius);
		if (steps < 3)
			steps = 3;
		
		// Step collision
		double stepMs = ms / (steps - 1);
		for (int step = 0; step < steps; step++) {
			
			if (isInside(self, collider.getPosition()) || pierces(self.getPosition(), collider, stepMs))
				return true;
			
			if (step < steps - 1) {
				self.forward(stepMs);
				collider.forward(stepMs);
			}
		}
			
		return false;
	}

	public HitCircle clone() {
		return new HitCircle(center.clone(), radius);
	}
	
	/*
	// Tester
	public static void main(String args[]) {
		HitCircle hit = new HitCircle(new Vertex(0, 10), 11);
		
		ArrayList<Direction> selfDirs = new ArrayList<Direction>();
		Direction selfDir1 = new Direction();
		selfDir1.setAngle(-135);
		selfDir1.setSpeed(100);
		selfDirs.add(selfDir1);
		Movement self = new Movement(new Vertex(200,200), selfDirs);
		
		
		ArrayList<Direction> colDirs = new ArrayList<Direction>();
		Direction colDir1 = new Direction();
		colDir1.setAngle(45);
		colDir1.setSpeed(10);
		colDirs.add(colDir1);
		Movement collider = new Movement(new Vertex(), colDirs);
		
		while (!hit.collides(self, collider, 16)) {
			self.forward(16);
			collider.forward(16);
		}
		
		System.out.println(collider.getPosition());
	}
	*/
}
