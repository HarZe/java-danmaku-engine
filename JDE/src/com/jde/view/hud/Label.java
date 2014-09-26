package com.jde.view.hud;

import com.jde.model.physics.Vertex;

/**
 * This Label class is an abstract class for HUD's labels
 * 
 * @author HarZe (David Serrano)
 */
public abstract class Label {

	/** Position of the label, in VGApx */
	protected Vertex position;
	/** Name of the label */
	protected String name;

	/**
	 * Basic constructor
	 * 
	 * @param name
	 *            Name of the label
	 * @param pos
	 *            Center position (in VGApx, absolute to the canvas)
	 */
	public Label(String name, Vertex pos) {
		this.position = pos;
		this.name = name;
	}

	/**
	 * Draws the label
	 */
	public abstract void draw();

	public String getName() {
		return name;
	}

	public Vertex getPosition() {
		return position;
	}

}
