package com.jde.model.physics.collision;

import com.jde.model.physics.Movement;
import com.jde.model.physics.Vertex;

public interface HitZone {

	public boolean isInside(Vertex v);
	
	public boolean collides(Movement m, double ms);
}
