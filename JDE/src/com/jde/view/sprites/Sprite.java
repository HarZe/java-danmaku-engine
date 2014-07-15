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

public class Sprite {

	protected Texture texture;
	protected int displayList;
	protected boolean loaded = false;
	protected String file;

	public Sprite(String file) {
		this.file = file;
	}

	public void preload() {
		try {
			texture = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream(file));
		} catch (IOException e) {
			e.printStackTrace();
		}

		displayList = glGenLists(1);

		glNewList(displayList, GL_COMPILE);
		{
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			
			GL11.glBegin(GL11.GL_QUADS);
			{
				GL11.glTexCoord2d(0, 0);
				GL11.glVertex2d(-texture.getImageWidth() / 2.0,
						-texture.getImageHeight() / 2.0);
				GL11.glTexCoord2d(
						((double) texture.getImageWidth())
								/ texture.getTextureWidth(), 0);
				GL11.glVertex2d(texture.getImageWidth() / 2.0,
						-texture.getImageHeight() / 2.0);
				GL11.glTexCoord2d(
						((double) texture.getImageWidth())
								/ texture.getTextureWidth(),
						((double) texture.getImageHeight())
								/ texture.getTextureHeight());
				GL11.glVertex2d(texture.getImageWidth() / 2.0,
						texture.getImageHeight() / 2.0);
				GL11.glTexCoord2d(0, ((double) texture.getImageHeight())
						/ texture.getTextureHeight());
				GL11.glVertex2d(-texture.getImageWidth() / 2.0,
						texture.getImageHeight() / 2.0);
			}
			GL11.glEnd();
			
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
		glEndList();

		loaded = true;
	}

	public void draw() {
		if (!loaded)
			preload();
		
		texture.bind(); // or GL11.glBind(texture.getTextureID());
		GL11.glCallList(displayList);
	}

	public String toString() {
		return "Sprite. File:" + file + ", loaded:" + loaded;
	}
}
