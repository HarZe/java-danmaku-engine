package com.jde.view.sprites;

import org.lwjgl.opengl.GL11;

import com.jde.model.physics.Vertex;

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
	protected double scaling = 1;
	/** Rotation angle (degrees) applied when drawing */
	protected double rotation = 0;

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
	 */
	public Sprite(SpriteSheet sheet, double x, double y, double w, double h) {
		this.sheet = sheet;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	/**
	 * @return Vertex which contains the center (in the spritesheet) of this
	 *         Sprite
	 */
	public Vertex center() {
		return new Vertex(x + (w / 2.0), y + (h / 2.0)).scale(scaling);
	}

	/**
	 * This method return the center of this Sprite given an the NW corner
	 * 
	 * @param v
	 *            Upper corner of the Sprite
	 * @return The center relative to NW corner
	 */
	public Vertex centerFrom(Vertex v) {
		return new Vertex(v.getX() + (w / 2.0), v.getY() + (h / 2.0)).scale(scaling);
	}

	/**
	 * Draws the sprite (and loads it when called the first time)
	 */
	public void draw() {
		if (!loaded) {
			displayListId = sheet.getDisplayListId(x, y, w, h, scaling,
					rotation);
			loaded = true;
		}

		GL11.glPushMatrix();
		sheet.draw(displayListId);
		GL11.glPopMatrix();
	}

	public double getH() {
		return h;
	}

	public double getRotation() {
		return rotation;
	}

	public double getScaling() {
		return scaling;
	}

	public double getW() {
		return w;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public Sprite setRotation(double rotation) {
		this.rotation = rotation;
		return this;
	}

	public Sprite setScaling(double scaling) {
		this.scaling = scaling;
		return this;
	}

}
