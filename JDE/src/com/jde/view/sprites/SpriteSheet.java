package com.jde.view.sprites;

import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glNewList;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 * This SpriteSheet contains a texture used to extract and draw sprites
 * 
 * @author HarZe (David Serrano)
 */
public class SpriteSheet {

	/** OpenGL texture of the sheet */
	protected Texture texture;
	/** Becomes true when sheet is successfully loaded */
	protected boolean loaded = false;
	/** File name of the PNG source */
	protected String file;

	/**
	 * Basic constructor
	 * 
	 * @param file
	 *            Name/path of the PNG file
	 */
	public SpriteSheet(String file) {
		this.file = file;
	}

	/**
	 * This method draws a sprite from this sheet, and loads the sheet the first
	 * time is called
	 * 
	 * @param displayListId
	 *            Display list ID number of the sprite
	 */
	public void draw(int displayListId) {
		if (!loaded)
			preload();

		texture.bind();
		GL11.glCallList(displayListId);
	}

	/**
	 * This method compiles and return the display list ID for a certain sprite
	 * inside the sheet, given its coordinates
	 * 
	 * @param x
	 *            Top left X-axis coordinate of the sprite in the sheet
	 * @param y
	 *            Top left Y-axis coordinate of the sprite in the sheet
	 * @param w
	 *            Sprite width in the sheet
	 * @param h
	 *            Sprite height in the sheet
	 * @param scaling
	 *            Scale factor
	 * @param rotation
	 *            Rotation angle (degrees)
	 * @return The new display list ID of a sprite
	 */
	public int getDisplayListId(double x, double y, double w, double h,
			double scaling, double rotation) {
		if (!loaded)
			preload();

		int displayListId = glGenLists(1);

		glNewList(displayListId, GL_COMPILE);
		{
			GL11.glEnable(GL11.GL_TEXTURE_2D);

			double imgW = texture.getImageWidth();
			double imgH = texture.getImageHeight();
			double ratioW = ((double) texture.getImageWidth())
					/ texture.getTextureWidth();
			double ratioH = ((double) texture.getImageHeight())
					/ texture.getTextureHeight();

			// Texture relative coordinates
			double tx = ratioW * (x / imgW);
			double txw = ratioW * ((x + w) / imgW);
			double ty = ratioH * (y / imgH);
			double tyh = ratioH * ((y + h) / imgH);

			// Vertex real coordinates
			double vx = w / 2.0;
			double vy = h / 2.0;

			GL11.glScaled(scaling, scaling, scaling);
			GL11.glRotated(rotation, 0, 0, 1);
			GL11.glBegin(GL11.GL_QUADS);
			{
				GL11.glTexCoord2d(tx, ty);
				GL11.glVertex2d(-vx, -vy);

				GL11.glTexCoord2d(txw, ty);
				GL11.glVertex2d(vx, -vy);

				GL11.glTexCoord2d(txw, tyh);
				GL11.glVertex2d(vx, vy);

				GL11.glTexCoord2d(tx, tyh);
				GL11.glVertex2d(-vx, vy);
			}
			GL11.glEnd();

			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
		glEndList();

		return displayListId;
	}

	/**
	 * This method loads the spritesheet into OpenGL as a texture from the PNG
	 * file
	 */
	public void preload() {
		try {
			texture = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream(file));
		} catch (IOException e) {
			e.printStackTrace();
		}

		loaded = true;
	}
}
