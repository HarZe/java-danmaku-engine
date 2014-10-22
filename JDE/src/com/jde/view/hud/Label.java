package com.jde.view.hud;

import java.util.Comparator;

import com.jde.model.physics.Vertex;

/**
 * This Label class is an abstract class for HUD's labels
 * 
 * @author HarZe (David Serrano)
 */
public abstract class Label {

	/**
	 * This LabelComparator class is a comparator in which higher values of
	 * layer goes first
	 * 
	 * @author HarZe (David Serrano)
	 */
	public static class LabelComparator implements Comparator<Label> {
		@Override
		public int compare(Label l1, Label l2) {
			return l1.getLayer() - l2.getLayer();
		}
	}

	/** Position of the label, in VGApx */
	protected Vertex position;
	/** Name of the label */
	protected String name;
	/** Layer (higher values will overlap) number */
	protected int layer = 0;

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

	public int getLayer() {
		return layer;
	}

	public String getName() {
		return name;
	}

	public Vertex getPosition() {
		return position;
	}

	public Label setLayer(int layer) {
		this.layer = layer;
		return this;
	}

}
