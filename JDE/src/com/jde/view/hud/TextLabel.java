package com.jde.view.hud;

import org.lwjgl.opengl.GL11;

import com.jde.model.physics.Vertex;

/**
 * This TextLabel class is a text used as label in the HUD
 * 
 * @author HarZe (David Serrano)
 */
public class TextLabel extends Label {

	/** Text of the label */
	protected String text;
	/** Font used to draw */
	protected PseudoFont font;

	/**
	 * Basic constructor
	 * 
	 * @param name
	 *            Name of the label
	 * @param pos
	 *            Center position (in VGApx, absolute to the canvas)
	 * @param text
	 *            Text of the label
	 * @param font
	 *            Font used to draw
	 */
	public TextLabel(String name, Vertex pos, String text, PseudoFont font) {
		super(name, pos);
		this.text = text;
		this.font = font;
	}

	@Override
	public void draw() {
		GL11.glPushMatrix();
		GL11.glTranslated(position.getX(), position.getY(), 0);
		font.draw(text);
		GL11.glPopMatrix();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
