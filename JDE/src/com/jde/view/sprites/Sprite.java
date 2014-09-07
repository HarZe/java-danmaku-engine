package com.jde.view.sprites;

/**
 * This Sprite class contains there location of a sprite in a certain
 * spritesheet
 * 
 * @author HarZe (David Serrano)
 */
public class Sprite {

	/** Spritesheet that contains this sprite */
	protected SpriteSheet sheet;
	/** OpenGL display list id of the sprite */
	protected int displayListId;

	/** Top left X-axis coordinate of the sprite in the sheet */
	protected double x;
	/** Top left Y-axis coordinate of the sprite in the sheet */
	protected double y;
	/** Sprite width in the sheet */
	protected double w;
	/** Sprite height in the sheet */
	protected double h;
	/** Scale factor, applied when drawing */
	protected double scaling;

	/** It is true when a displayListId has been generated */
	protected boolean loaded;

	/**
	 * Basic constructor
	 * 
	 * @param sheet
	 *            Spritesheet that contains this sprite
	 * @param x
	 *            Top left X-axis coordinate of the sprite in the sheet
	 * @param y
	 *            Top left Y-axis coordinate of the sprite in the sheet
	 * @param w
	 *            Sprite width in the sheet
	 * @param h
	 *            Sprite height in the sheet
	 * @param scaling
	 *            Scale factor
	 */
	public Sprite(SpriteSheet sheet, double x, double y, double w, double h,
			double scaling) {
		this.sheet = sheet;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.scaling = scaling;
	}

	/**
	 * Draws the sprite (and loads it when called the first time)
	 */
	public void draw() {
		if (!loaded) {
			displayListId = sheet.getDisplayListId(x, y, w, h, scaling);
			loaded = true;
		}

		sheet.draw(displayListId);
	}
}
