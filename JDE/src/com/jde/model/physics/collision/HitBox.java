package com.jde.model.physics.collision;

import com.jde.model.physics.Movement;
import com.jde.model.physics.Vertex;

public class HitBox implements HitZone {

	protected Vertex center;
	protected Vertex dimensions;
	
	public HitBox(Vertex center, Vertex dimensions) {
		this.center = center;
		this.dimensions = dimensions;
	}

	@Override
	public boolean isInside(Movement self, Vertex collider) {
		return (center.getX() - (dimensions.getX() / 2.0) <= collider.getX()
				&& center.getX() + (dimensions.getX() / 2.0) >= collider.getX()
				&& center.getY() - (dimensions.getY() / 2.0) <= collider.getY()
				&& center.getY() + (dimensions.getY() / 2.0) >= collider.getY());
	}

	@Override
	public boolean collides(Movement self, Movement collider, double ms) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void expand(double size) {
		// TODO Auto-generated method stub
		
	}

}
