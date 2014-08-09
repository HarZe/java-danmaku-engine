package com.jde.model.physics.collision;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import com.jde.model.physics.Direction;
import com.jde.model.physics.Movement;
import com.jde.model.physics.Vertex;

public class HitPolygon implements HitZone {

	protected LinkedList<Vertex> verteces;
	protected LinkedList<Vertex> normals;
	protected double radius = 0;

	public HitPolygon(LinkedList<Vertex> vtcs) {
		verteces = vtcs;
		normals = new LinkedList<Vertex>();

		if (verteces.size() >= 2) {
			Iterator<Vertex> it = verteces.iterator();
			Vertex previousVertex = it.next();
			radius = previousVertex.lenght();

			while (it.hasNext()) {
				Vertex v = it.next();
				normals.add(new Vertex(previousVertex, v).normal().opposite().unit());
				radius = Math.max(radius, v.lenght());
				previousVertex = v;
			}

			normals.add(new Vertex(verteces.getLast(), verteces.getFirst())
					.normal().opposite().unit());
		}
	}
	
	@Override
	public boolean isInside(Movement self, Vertex collider) {
		ArrayList<Vertex> fixedVerteces = new ArrayList<Vertex>();
		for (Vertex v : verteces)
			fixedVerteces.add(self.getPosition().clone().add(v.clone().rotate(Math.toRadians(self.getPhysicAngle()))));
		
		int last_side = 0, current_side = 0;
		for (int i = 0; i < fixedVerteces.size(); i++) {
			Vertex a = fixedVerteces.get(i);
			Vertex b = fixedVerteces.get(i % fixedVerteces.size());

			Vertex edge = b.sub(a);
			Vertex point = collider.clone().sub(a);
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
	
	protected boolean pierces(Movement self, Movement collider, double ms) {
		return cyrusBeck(self, collider, ms);
	}
	
	protected boolean cyrusBeck(Movement self, Movement collider, double ms) {
		
		double tIn = 0;
		double tOut = 1;
		double tHit;
		boolean found = false;
		
		LinkedList<Vertex> fixedVerteces = new LinkedList<Vertex>();
		for (Vertex v : verteces)
			fixedVerteces.add(self.getPosition().clone().add(v.clone().rotate(Math.toRadians(self.getPhysicAngle()))));
		
		Vertex pos = collider.getPosition();
		Direction cDir = collider.getDirection();
		Vertex dir = Vertex.angleToVertex(cDir.getAngle()).scale(cDir.getSpeed() * 0.001 * ms);

		Iterator<Vertex> itV = verteces.iterator();
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

		return tIn <= tOut;
	}

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
					
		if (isInside(self, collider.getPosition()) || pierces(self, collider, stepMs))
			return true;
					
			if (step < steps - 1) {
				self.forward(stepMs);
				collider.forward(stepMs);
			}
		}
					
		return false;
	}
	
	public HitPolygon clone() {
		return new HitPolygon(verteces);
	}
	
	/*
	// Tester
	public static void main(String args[]) {
		LinkedList<Vertex> list = new LinkedList<Vertex>();
		list.add(new Vertex(10,0));
		list.add(new Vertex(0,10));
		list.add(new Vertex(-10,0));
		list.add(new Vertex(0,-10));
		HitPolygon hit = new HitPolygon(list);
		
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
