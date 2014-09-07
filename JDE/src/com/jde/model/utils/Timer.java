package com.jde.model.utils;

/**
 * This Timer class is simple timer
 * 
 * @author HarZe (David Serrano)
 */
public class Timer {

	protected long lastTime;
	protected double elapsedTime;
	protected boolean firstRun = true;

	/**
	 * Constructor
	 */
	public Timer() {

	}

	/**
	 * Initializes the timer
	 */
	public void initialize() {
		lastTime = System.nanoTime();
		firstRun = false;
	}

	/**
	 * @return elapsed time since the next to last update call
	 */
	public double getElapsedTime() {
		return elapsedTime;
	}

	/**
	 * Updates the timer, must be called each iteration
	 * 
	 * @return the elapsed time in milliseconds
	 */
	public double update() {
		if (firstRun) {
			firstRun = false;
			lastTime = System.nanoTime();
			return 0;
		} else {
			long elapsedTime = System.nanoTime() - lastTime;
			lastTime = System.nanoTime();
			this.elapsedTime = elapsedTime / (double) 1000000;
			return this.elapsedTime;
		}
	}

}
