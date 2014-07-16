package com.jde.model.physics.collision;

import com.jde.model.physics.Movement;
import com.jde.model.physics.Vertex;

public class HitBox implements HitZone {

	protected Vertex center;
	protected Vertex dimensions;

	public HitBox() {
		
	}
	
	public HitBox(Vertex center, Vertex dimensions) {
		this.center = center;
		this.dimensions = dimensions;
	}

	@Override
	public boolean isInside(Vertex v) {
		return (center.getX() - (dimensions.getX() / 2.0) <= v.getX()
				&& center.getX() + (dimensions.getX() / 2.0) >= v.getX()
				&& center.getY() - (dimensions.getY() / 2.0) <= v.getY()
				&& center.getY() + (dimensions.getY() / 2.0) >= v.getY());
	}

	@Override
	public boolean collides(Movement m, double ms) {
		// TODO Auto-generated method stub
		return false;
	}

}
