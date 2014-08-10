package com.jde.view;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.jde.model.stage.Stage;
import com.jde.view.sprites.Sprite;
import com.jde.view.sprites.SpriteSheet;

public class Game {
	
	protected String VERSION = "pre-alpha 0.1.2";
	
	protected HUD hud;
	
	protected ArrayList<Stage> stages;
	protected int currentStage;
	
	public static Random random = new Random();

	public Game(ArrayList<Stage> stages) {
		SpriteSheet hudsheet = new SpriteSheet("res/sprites/hud.png");
		hud = new HUD(new Sprite(hudsheet, 0, 0, 640, 480, 1));
		this.stages = stages;
		currentStage = 0;
	}
	
	public void draw(int w, int h) {
        GL11.glViewport(0, 0, w, h);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, w, h, 0, 1, -1);
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        
        GL11.glPushMatrix();
        
		// draw game scene
		GL11.glColor4d(0, 0, 0.1, 1);
		GL11.glRectf(32, 16, 32+384, 16+448);
		
		// draw stage & hud overlay
		GL11.glColor4f(1, 1, 1, 1);
		stages.get(currentStage).draw();
		hud.draw(w,h);
		
		GL11.glPopMatrix();
	}
	
	public void forward(double ms) {
		//System.out.println(((int) (1000 / ms)) + " fps");
		
		stages.get(currentStage).forward(ms);
		
		if (stages.get(currentStage).isFinished() && currentStage + 1 < stages.size())
			currentStage++;
	}

	@Override
	public String toString() {
		return "JDE " + VERSION;
	}
}
