package com.jde.model.physics.collision;

import java.util.ArrayList;

import com.jde.model.physics.Movement;
import com.jde.model.physics.Vertex;

public class HitBody implements HitZone {

	protected ArrayList<HitZone> hitZones;
	
	public HitBody(ArrayList<HitZone> hitZones) {
		this.hitZones = hitZones;
	}

	@Override
	public boolean isInside(Movement self, Vertex collider) {
		for (HitZone h : hitZones)
			if (h.isInside(self, collider))
				return true;
		return false;
	}

	@Override
	public boolean collides(Movement self, Movement collider, double ms) {
		for (HitZone h : hitZones)
			if (h.collides(self, collider, ms)) {
				System.out.println("Collision, with : " + self.getPosition());
				return true;
			}
		return false;
	}

}
