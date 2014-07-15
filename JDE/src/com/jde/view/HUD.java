package com.jde.view;

import org.lwjgl.opengl.GL11;

import com.jde.view.sprites.Sprite;

public class HUD {
	
	protected Sprite background;

	public HUD(Sprite background) {
		this.background = background;
	}
	
	public void draw(int w, int h) {
		GL11.glPushMatrix();
		GL11.glTranslated(w/2.0, h/2.0, 0);
		background.draw();
		GL11.glPopMatrix();
	}

}
