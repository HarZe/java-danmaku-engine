package com.jde.view.hud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import org.lwjgl.opengl.GL11;

import com.jde.model.physics.Vertex;
import com.jde.view.sprites.Sprite;
import com.jde.view.sprites.SpriteSheet;

/**
 * This HUD class is a list of labels (sprites and text) to show ingame
 * 
 * @author HarZe (David Serrano)
 */
public class HUD {

	/** List of labels */
	protected static HashMap<String, Label> labels = new HashMap<String, Label>();

	// Misc
	/** List of labels ordered by layer */
	protected static ArrayList<Label> sortedLabels = new ArrayList<Label>();

	/** Ready to draw */
	protected static boolean sorted = false;

	/**
	 * Adds a label to the HUD
	 * 
	 * @param name
	 *            Name of the label (must be unique)
	 * @param label
	 *            Label to add
	 */
	public static void addLabel(String name, Label label) {
		labels.put(name, label);
		sorted = false;
	}

	/**
	 * Draws the HUD
	 */
	public static void draw() {

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
	 * This method converts a decimal number in a fixed length integer number
	 * 
	 * @param number
	 *            Input number
	 * @param length
	 *            Fixed output length
	 * @return The fixed integer number
	 */
	public static String formatIntegerNumber(double number, int length) {
		String formated = "";
		String stringNumber = ("" + number).split("\\.")[0];
		int numberLength = stringNumber.length();

		for (int i = length - 1; i >= 0; i--) {
			if (i < numberLength)
				formated += stringNumber.charAt(numberLength - 1 - i);
			else
				formated += "0";

			if (i % 3 == 0 && i > 0)
				formated += ",";
		}

		return formated;
	}

	/**
	 * This method fixes the lengths of the parts of a decimal number
	 * @param number Decimal number
	 * @param intLength Fixed length of the integer part
	 * @param decLength Fixed length of the decimal part
	 * @return The fixed decimal number
	 */
	public static String formatDecimalNumber(double number, int intLength, int decLength) {
		String parts[] = ("" + number).split("\\.");
		parts[1] = parts[1].substring(0, decLength - 1);
		String ret = "";
		
		if (parts[0].length() < intLength)
			for (int i = 0; i < intLength - parts[0].length(); i++)
				ret += "0";
		
		ret += parts[0] + "." + parts[1];
		
		if (parts[1].length() < decLength)
			for (int i = 0; i < decLength - parts[1].length(); i++)
				ret += "0";
		
		return ret;
	}

	/**
	 * This method returns the requested label
	 * 
	 * @param name
	 *            Name of the label
	 * @return The requested label
	 */
	public static Label getLabel(String name) {
		return labels.get(name);
	}

	/**
	 * Removes a label from the HUD
	 * 
	 * @param name
	 *            Name of the label
	 */
	public static void removeLabel(String name) {
		labels.remove(name);
	}

	/**
	 * Removes all labels
	 */
	public static void resetHUD() {
		labels.clear();
		sortedLabels.clear();
		sorted = false;
	}

	/**
	 * Sets the default JDE HUD
	 */
	public static void setDefaultHUD() {
		// Default JDE spritesheets & sprites
		SpriteSheet sheet = new SpriteSheet("res/sprites/jde-hud.png");

		Sprite leftBar = new Sprite(sheet, 0, 0, 64, 960).setScaling(0.5);
		Sprite topBar = new Sprite(sheet, 64, 0, 768, 32).setScaling(0.5);
		Sprite bottomBar = new Sprite(sheet, 64, 928, 768, 32).setScaling(0.5);
		Sprite rightBar = new Sprite(sheet, 832, 0, 448, 960).setScaling(0.5);

		Sprite hiScore = new Sprite(sheet, 66, 76, 136, 35).setScaling(0.5);
		Sprite score = new Sprite(sheet, 66, 116, 136, 35).setScaling(0.5);
		Sprite player = new Sprite(sheet, 66, 183, 136, 35).setScaling(0.5);
		Sprite power = new Sprite(sheet, 66, 227, 136, 35).setScaling(0.5);
		Sprite fps = new Sprite(sheet, 66, 897, 82, 30).setScaling(0.5);

		// Score-font
		PseudoFont scoreFont = new PseudoFont();
		for (int i = 0; i < 10; i++)
			scoreFont.addChar("" + i, new Sprite(sheet, 204 + (21.4 * i), 117,
					21.4, 35).setScaling(0.5));
		scoreFont.addChar(".",
				new Sprite(sheet, 422, 117, 14, 35).setScaling(0.5));
		scoreFont.addChar(",",
				new Sprite(sheet, 434, 117, 14, 35).setScaling(0.5));

		// Labels
		labels.put("left-bar", new SpriteLabel("left-bar", leftBar.center(),
				leftBar));
		labels.put("right-bar", new SpriteLabel("right-bar", rightBar.center(),
				rightBar));
		labels.put("bottom-bar",
				new SpriteLabel("bottom-bar", bottomBar.center(), bottomBar));
		labels.put("top-bar", new SpriteLabel("top-bar", topBar.center(),
				topBar));
		labels.put("hi-score-sprite", new SpriteLabel("hi-score-sprite",
				hiScore.centerFrom(new Vertex(850, 50)), hiScore).setLayer(1));
		labels.put(
				"score-sprite",
				new SpriteLabel("score-sprite", score.centerFrom(new Vertex(
						850, 100)), score).setLayer(1));
		labels.put(
				"player-sprite",
				new SpriteLabel("player-sprite", player.centerFrom(new Vertex(
						850, 200)), player).setLayer(1));
		labels.put(
				"power-sprite",
				new SpriteLabel("power-sprite", power.centerFrom(new Vertex(
						850, 250)), power).setLayer(1));
		labels.put("fps-sprite", new SpriteLabel("fps-sprite",
				fps.centerFrom(new Vertex(1020, 922)), fps).setLayer(1));

		labels.put("score", new TextLabel("score", new Vertex(503, 59), "0",
				scoreFont).setLayer(1));
		labels.put("fps", new TextLabel("fps", new Vertex(560, 470), "0",
				scoreFont).setLayer(1));
	}

}
