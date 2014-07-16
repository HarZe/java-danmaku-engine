package com.jde.model.entity.player;

import com.jde.model.entity.Entity;
import com.jde.model.physics.Movement;
import com.jde.model.physics.collision.HitZone;
import com.jde.view.sprites.Sprite;

public class Player extends Entity {

	public Player(Sprite sprite, HitZone hitbox, Movement movement) {
		super(sprite, hitbox, movement);
	}

}
