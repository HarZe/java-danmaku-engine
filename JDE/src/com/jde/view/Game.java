package com.jde.view;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.jde.model.physics.Vertex;
import com.jde.model.stage.Stage;
import com.jde.view.sprites.Sprite;
import com.jde.view.sprites.SpriteSheet;

/**
 * This Game class is the main OpenGL manager of the engine while playing the
 * stages
 * 
 * @author HarZe (David Serrano)
 */
public class Game {

	/** VGA base resolution */
	public static Vertex BASE_RES = new Vertex(640, 480);
	/** VGA based top-left game board position */
	public static Vertex GAME_BOARD_POS = new Vertex(32, 16);
	/** VGA based size of the game board */
	public static Vertex GAME_BOARD_SIZE = new Vertex(384, 448);
	/** VGA based center of the game board */
	public static Vertex GAME_BOARD_CENTER = new Vertex(223, 240);

	/** Version number */
	protected String VERSION = "pre-alpha 0.1.9";

	/** Game state */
	protected boolean loaded = false;

	/** Player's HUD */
	protected HUD hud;
	/** List of stages of the game */
	protected ArrayList<Stage> stages;

	/** Index of the current stage */
	protected int currentStage;

	/**
	 * Basic constructor
	 * 
	 * @param stages
	 *            List of stages in the game
	 */
	public Game(ArrayList<Stage> stages) {
		SpriteSheet hudsheet = new SpriteSheet("res/sprites/hud.png");
		hud = new HUD(new Sprite(hudsheet, 0, 0, BASE_RES.getX(),
				BASE_RES.getY(), 1));
		this.stages = stages;
		currentStage = 0;
	}

	/**
	 * Check if the player has a collision in this frame
	 * 
	 * @param ms
	 *            Milliseconds to forward
	 * @return True if the player collided
	 */
	protected boolean colliding(double ms) {
		return stages.get(currentStage).colliding(ms);
	}

	/**
	 * Draws the full game, stage and HUD
	 * 
	 * @param w
	 *            Width of the viewport
	 * @param h
	 *            Hieght of the viewport
	 */
	protected void draw(int w, int h) {
		if (!loaded) {
			initGL(w, h);
			loaded = true;
		}

		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		GL11.glPushMatrix();

		// game scaling if needed
		GL11.glScaled(w / BASE_RES.getX(), h / BASE_RES.getY(), 1);

		// draw game scene
		GL11.glColor4d(0, 0, 0.1, 1);
		GL11.glRectd(GAME_BOARD_POS.getX(), GAME_BOARD_POS.getY(),
				GAME_BOARD_POS.getX() + GAME_BOARD_SIZE.getX(),
				GAME_BOARD_POS.getY() + GAME_BOARD_SIZE.getY());

		// draw stage & hud overlay
		GL11.glColor4f(1, 1, 1, 1);
		stages.get(currentStage).draw();
		hud.draw((int) BASE_RES.getX(), (int) BASE_RES.getY());

		GL11.glPopMatrix();
	}

	/**
	 * Forwards the stage, and checks if it has finished in order to forward to
	 * the next stage (if any)
	 * 
	 * @param ms
	 *            Milliseconds to forward
	 */
	protected void forward(double ms) {
		stages.get(currentStage).forward(ms);

		if (stages.get(currentStage).isFinished()
				&& currentStage + 1 < stages.size())
			currentStage++;
	}

	/**
	 * Sets up the orthogonal projection in OpenGL
	 * 
	 * @param w
	 *            Width of the viewport
	 * @param h
	 *            Height of the viewport
	 */
	protected void initGL(int w, int h) {
		GL11.glViewport(0, 0, w, h);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, w, h, 0, 1, -1);

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public String toString() {
		return "JDE " + VERSION;
	}

	/**
	 * This method updates to the next frame, drawing and multi-thread collision
	 * detection are simultaneous; when finished, all entities go forward
	 * 
	 * @param w
	 *            Width of the viewport
	 * @param h
	 *            Height of the viewport
	 * @param ms
	 *            Milliseconds to forward
	 */
	public void update(int w, int h, double ms) {

		Collider collider = new Collider(ms);
		collider.start();

		draw(w, h);

		try {
			collider.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		forward(ms);

		// System.out.println(((int) (1000 / ms)) + " fps");
	}

	/**
	 * This method converts a Vertex which points are represented with (0,0) as
	 * the center of the game board into "real" coordinates to draw
	 * 
	 * @param v Vertex to convert
	 * @return A reference to the same Vertex already modified
	 */
	public static Vertex unvirtualizeCoordinates(Vertex v) {
		return v.add(GAME_BOARD_CENTER);
	}

	/**
	 * This Collider class is a thread for the colliding() method
	 * 
	 * @author HarZe (David Serrano)
	 */
	protected class Collider extends Thread {

		protected double ms;
		protected boolean collision;

		public Collider(double ms) {
			this.ms = ms;
		}

		public boolean collision() {
			return collision;
		}

		public void run() {
			collision = colliding(ms);
		}

	}
}
