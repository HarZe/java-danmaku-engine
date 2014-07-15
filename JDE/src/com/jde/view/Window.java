package com.jde.view;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.jde.model.utils.Timer;

public class Window {
	protected Game game;
	protected Timer timer;
	protected int width;
	protected int height;
	
	public Window(Game game, int width, int height) throws LWJGLException {
		
		this.game = game;
		timer = new Timer();
		this.width = width;
		this.height = height;
		
		Display.setDisplayMode(new DisplayMode(width, height));
		Display.setTitle(game.toString());
		Display.create(/*new PixelFormat().withSamples(4)*/);
		Keyboard.create();
		
		timer.initialize();
		
		while (!Display.isCloseRequested()) {
			
			double time = timer.update();
			game.draw(width, height);
			game.forward(time);
			
			Display.update();
			//Display.sync(60);
		}

		Keyboard.destroy();
		Display.destroy();
	}
}