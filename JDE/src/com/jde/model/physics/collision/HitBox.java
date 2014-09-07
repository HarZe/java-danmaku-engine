package com.jde.model.physics.collision;

import com.jde.model.physics.Movement;
import com.jde.model.physics.Vertex;

/**
 * This HitBody class represents a rectangular zone
 * 
 * @author HarZe (David Serrano)
 */
public class HitBox implements HitZone {

	/** Center of the zone */
	protected Vertex center;
	/** Width and height of the zone */
	protected Vertex dimensions;
	
	/**
	 * Basic constructor
	 * @param center Center of the zone
	 * @param dimensions Width and height
	 */
	public HitBox(Vertex center, Vertex dimensions) {
		this.center = center;
		this.dimensions = dimensions;
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

	@Override
	public boolean isInside(Movement self, Vertex collider) {
		return (center.getX() - (dimensions.getX() / 2.0) <= collider.getX()
				&& center.getX() + (dimensions.getX() / 2.0) >= collider.getX()
				&& center.getY() - (dimensions.getY() / 2.0) <= collider.getY()
				&& center.getY() + (dimensions.getY() / 2.0) >= collider.getY());
	}

}
