package com.jde.model.physics.collision;

import java.util.ArrayList;

import com.jde.model.physics.Movement;
import com.jde.model.physics.Vertex;

/**
 * This HitBody class represents a colliding object composed of multiple HitZone
 * 
 * @author HarZe (David Serrano)
 */
public class HitBody implements HitZone {

	/** List of HitZone */
	protected ArrayList<HitZone> hitZones;

	/**
	 * Basic constructor
	 * 
	 * @param hitZones
	 *            List of hit zones
	 */
	public HitBody(ArrayList<HitZone> hitZones) {
		this.hitZones = hitZones;
	}

	@Override
	public boolean collides(Movement self, Movement other, double ms) {
		for (HitZone h : hitZones)
			if (h.collides(self, other, ms)) {
				System.out.println("Collision, with : " + self.getPosition()); // TODO:
																				// delete
																				// this,
																				// debug
				return true;
			}
		return false;
	}

	@Override
	public void expand(double size) {
		for (HitZone h : hitZones)
			h.expand(size);
	}

	@Override
	public boolean isInside(Movement self, Vertex other) {
		for (HitZone h : hitZones)
			if (h.isInside(self, other))
				return true;
		return false;
	}
}
