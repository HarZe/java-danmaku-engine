package com.jde.model.physics.collision;

import com.jde.model.physics.Movement;
import com.jde.model.physics.Vertex;

/**
 * This HitZone interface represents a colliding object
 * 
 * @author HarZe (David Serrano)
 */

public interface HitZone {

	/**
	 * This method checks if an entity collides with a moving point
	 * 
	 * @param self
	 *            The movement of the entity which contains the hit zone, MUST BE a clone
	 * @param other
	 *            The movement of the point that is supposed to collide, MUST BE a clone
	 * @param ms
	 *            The milliseconds to forward
	 * @return True if the entities collide
	 */
	public boolean collides(Movement self, Movement other, double ms);

	/**
	 * Expands the size of the hit zone
	 * 
	 * @param size
	 *            Size to increase, in vgapx
	 */
	public void expand(double size);

	/**
	 * This method checks if a point is inside the hit zone
	 * 
	 * @param self
	 *            The movement of the entity which contains the hit zone
	 * @param other
	 *            The position to check
	 * @return True if the other entity is inside the hit zone
	 */
	public boolean isInside(Movement self, Vertex other);

}
