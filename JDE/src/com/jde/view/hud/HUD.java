package com.jde.view.hud;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;

/**
 * This HUD class is a list of labels (sprites and text) to show ingame
 * 
 * @author HarZe (David Serrano)
 */
public class HUD {

	/** List of labels */
	protected HashMap<String, Label> labels;

	/**
	 * Basic constructor
	 */
	public HUD() {
		labels = new HashMap<String, Label>();
	}

	/**
	 * Adds a label to the HUD
	 * 
	 * @param name
	 *            Name of the label (must be unique)
	 * @param label
	 *            Label to add
	 */
	public void addLabel(String name, Label label) {
		labels.put(name, label);
	}

	/**
	 * Draws the HUD
	 */
	public void draw() {
		GL11.glPushMatrix();

		for (Label l : labels.values())
			l.draw();

		GL11.glPopMatrix();
	}

	/**
	 * This method returns the requested label
	 * 
	 * @param name
	 *            Name of the label
	 * @return The requested label
	 */
	public Label getLabel(String name) {
		return labels.get(name);
	}

	/**
	 * Removes a label from the HUD
	 * 
	 * @param name
	 *            Name of the label
	 */
	public void removeLabel(String name) {
		labels.remove(name);
	}

}
