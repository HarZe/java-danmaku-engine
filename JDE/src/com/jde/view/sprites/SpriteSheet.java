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

public class SpriteSheet {

	protected Texture texture;
	protected boolean loaded = false;
	protected String file;

	public SpriteSheet(String file) {
		this.file = file;
	}

	public void preload() {
		try {
			texture = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream(file));
		} catch (IOException e) {
			e.printStackTrace();
		}

		loaded = true;
	}

	public int getDisplayListId(double x, double y, double w, double h,
			double scaling) {
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
			double tx = ratioW*(x / imgW);
			double txw = ratioW*((x+w) / imgW);
			double ty = ratioH*(y / imgH);
			double tyh = ratioH*((y+h) / imgH);
			
			// Vertex real coordinates
			double vx = w / 2.0;
			double vy = h / 2.0;

			GL11.glScaled(scaling, scaling, scaling);
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

	public void draw(int displayListId) {
		if (!loaded)
			preload();

		texture.bind(); // or GL11.glBind(texture.getTextureID());
		GL11.glCallList(displayListId);
	}

	public String toString() {
		return "Sprite. File:" + file + ", loaded:" + loaded;
	}
}
