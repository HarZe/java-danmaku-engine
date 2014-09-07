package com.jde.main;

import org.lwjgl.LWJGLException;

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
		//System.setProperty("org.lwjgl.librarypath",System.getProperty("user.dir") + "/native"); 

		try {
			Parser parser = new Parser();
			new Window(parser.parseXML(args[0]), 640, 480);

		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

}
