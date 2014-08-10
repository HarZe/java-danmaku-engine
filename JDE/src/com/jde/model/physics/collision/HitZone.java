package com.jde.model.physics.collision;

import com.jde.model.physics.Movement;
import com.jde.model.physics.Vertex;

public interface HitZone {

	public boolean isInside(Movement self, Vertex collider);
	
	public boolean collides(Movement self, Movement collider, double ms);
	
	public void expand(double size);
}
