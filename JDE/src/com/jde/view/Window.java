package com.jde.view;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.jde.model.utils.Timer;

/**
 * This Window class is the container of all the engine while working
 * 
 * @author HarZe (David Serrano)
 */
public class Window {
	
	/** Game currently loaded */
	protected Game game;
	/** Global timer */
	protected Timer timer;
	/** Width of the window */
	protected int width;
	/** Height of the window */
	protected int height;
	/** Fixed FPS */
	protected int fixedFps;

	/**
	 * Basic constructor, starts up a new game
	 * @param game A currently loaded Game
	 * @param width Width of the window
	 * @param height Height of the window
	 * @param fps Fixed target FPS
	 * @throws LWJGLException
	 */
	public Window(Game game, int width, int height, int fps) throws LWJGLException {

		this.game = game;
		timer = new Timer();
		this.width = width;
		this.height = height;
		this.fixedFps = fps;

		Display.setDisplayMode(new DisplayMode(width, height));
		Display.setTitle(game.toString());
		Display.create(/* new PixelFormat().withSamples(4) */);
		Keyboard.create();

		timer.initialize();

		while (!Display.isCloseRequested()) {

			double realTime = timer.update();
			double time = realTime;
			if (fixedFps != 0)
				time = 1000.0 / ((double) fixedFps);
			
			game.update(width, height, time, realTime);

			// VSync / Fixed FPS
			if (fixedFps != 0)
				Display.sync(fixedFps);
			Display.update();
		}

		Keyboard.destroy();
		AL.destroy();
		Display.destroy();
		System.exit(0);
	}
}