package com.jde.view.sprites;

import java.util.ArrayList;

/**
 * This Animation class is a list of sprites and durations, in order to draw an
 * animated sprite
 * 
 * @author HarZe (David Serrano)
 */
public class Animation {

	/** List of sprites or "frames" */
	protected ArrayList<Sprite> sprites;
	/** List of durations of each frame */
	protected ArrayList<Double> durations;
	/** Elapsed time since the animation started */
	protected double elapsed = 0;
	/** Current frame number */
	protected int current = 0;
	/** Number of the frame to come back when looping */
	protected int loopFrom = 0;

	/**
	 * Basic constructor
	 * @param sprites List of sprites
	 * @param durations List of durations
	 */
	public Animation(ArrayList<Sprite> sprites, ArrayList<Double> durations) {
		this.sprites = sprites;
		this.durations = durations;
		
		// Default looping is keeping on the last frame
		this.loopFrom = sprites.size() - 1;
	}

	/**
	 * Returns a copy of the animation
	 */
	public Animation clone() {
		Animation a = new Animation(sprites, durations);
		a.setLoopFrom(loopFrom);
		return a;
	}

	/**
	 * Draws the current frame of the animation
	 */
	public void draw() {
		sprites.get(current).draw();
	}

	/**
	 * Forwards the animation
	 * @param ms Milliseconds to forward
	 */
	public void forward(double ms) {
		elapsed += ms;

		// Skip to next frame while needed
		while (current < sprites.size() && durations.get(current) < elapsed)
			current++;

		// Check for looping
		if (current == sprites.size()) {
			elapsed -= durations.get(current - 1) - durations.get(loopFrom);
			current = loopFrom;
		}
	}

	public int getLoopFrom() {
		return loopFrom;
	}

	/**
	 * Reset the animation to the first frame
	 */
	public void reset() {
		current = 0;
		elapsed = 0;
	}

	public void setLoopFrom(int loopFrom) {
		if (loopFrom < sprites.size() - 1)
			this.loopFrom = loopFrom;
	}

}
