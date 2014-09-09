package com.jde.main;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.ImageIOImageData;

import com.jde.model.utils.Parser;
import com.jde.view.Window;

/**
 * This Main class is the launcher of the XML parser and later the game itself
 * 
 * @author HarZe (David Serrano)
 */
public class Main {

	public Main() {

	}

	public static void main(String[] args) {

		/* UNCOMMENT TO GENERATE JAR: */
		// System.setProperty("org.lwjgl.librarypath",System.getProperty("user.dir") + "/native");

		// Loading JDE icon
		try {
			String OS = System.getProperty("os.name").toUpperCase();

			if (OS.contains("WIN")) {
				Display.setIcon(new ByteBuffer[] {
						new ImageIOImageData().imageToByteBuffer(ImageIO
								.read(new File("res/logo/jde-icon16.png")),
								false, false, null),
						new ImageIOImageData().imageToByteBuffer(ImageIO
								.read(new File("res/logo/jde-icon32.png")),
								false, false, null) });
			}

			else if (OS.contains("MAC")) {
				Display.setIcon(new ByteBuffer[] { new ImageIOImageData()
						.imageToByteBuffer(ImageIO.read(new File(
								"res/logo/jde-icon128.png")), false, false,
								null) });
			}

			else {
				Display.setIcon(new ByteBuffer[] { new ImageIOImageData().imageToByteBuffer(
						ImageIO.read(new File("res/logo/jde-icon32.png")),
						false, false, null) });
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Launching JDE
		try {
			Parser parser = new Parser();
			new Window(parser.parseXML(args[0]), Integer.parseInt(args[1]),
					Integer.parseInt(args[2]));

		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

}
