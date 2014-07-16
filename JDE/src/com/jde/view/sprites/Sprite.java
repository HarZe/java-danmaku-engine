package com.jde.view.sprites;

public class Sprite {
	
	protected SpriteSheet sheet;
	protected int displayListId;
	protected double x, y, w, h;
	protected double scaling;

	protected boolean loaded;
	
	public Sprite(SpriteSheet sheet, double x, double y, double w, double h, double scaling) {
		this.sheet = sheet;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.scaling = scaling;
	}

	public void draw() {
		if (!loaded)
			displayListId = sheet.getDisplayListId(x, y, w, h, scaling);
		
		sheet.draw(displayListId);
	}
}
