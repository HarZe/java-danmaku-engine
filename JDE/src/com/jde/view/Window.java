package com.jde.view;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
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

	/**
	 * Basic constructor, starts up a new game
	 * @param game A currently loaded Game
	 * @param width Width of the window
	 * @param height Height of the window
	 * @throws LWJGLException
	 */
	public Window(Game game, int width, int height) throws LWJGLException {

		this.game = game;
		timer = new Timer();
		this.width = width;
		this.height = height;

		Display.setDisplayMode(new DisplayMode(width, height));
		Display.setTitle(game.toString());
		Display.create(/* new PixelFormat().withSamples(4) */);
		Keyboard.create();

		timer.initialize();

		while (!Display.isCloseRequested()) {

			double time = timer.update();
			game.update(width, height, time);

			Display.update();
			Display.sync(144);
		}

		Keyboard.destroy();
		Display.destroy();
	}
}