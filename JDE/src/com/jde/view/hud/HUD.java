package com.jde.view.hud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import org.lwjgl.opengl.GL11;

/**
 * This HUD class is a list of labels (sprites and text) to show ingame
 * 
 * @author HarZe (David Serrano)
 */
public class HUD {

	/**
	 * This method converts a decimal number in a fixed length integer number
	 * 
	 * @param number Input number
	 * @param length Fixed output length
	 * @return The fixed integer number
	 */
	public static String formatNumber(double number, int length) {
		String formated = "";
		int zerosToAdd = length - ("" + number).split("\\.")[0].length();
		for (int i = 0; i < zerosToAdd; i++)
			formated += "0";
		return formated + ("" + number).split("\\.")[0];
	}

	/** List of labels */
	protected HashMap<String, Label> labels;
	
	// Misc
	/** List of labels ordered by layer */
	protected ArrayList<Label> sortedLabels;
	/** Ready to draw */
	protected boolean sorted = false;

	/**
	 * Basic constructor
	 */
	public HUD() {
		labels = new HashMap<String, Label>();
		sortedLabels = new ArrayList<Label>();
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
		sorted = false;
	}

	/**
	 * Draws the HUD
	 */
	public void draw() {

		// Sort using layer values
		if (!sorted) {
			sortedLabels.clear();
			PriorityQueue<Label> queue = new PriorityQueue<Label>(20,
					new Label.LabelComparator());
			for (Label l : labels.values())
				queue.add(l);
			while (queue.size() > 0)
				sortedLabels.add(queue.poll());
		}

		GL11.glPushMatrix();

		for (Label l : sortedLabels)
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
