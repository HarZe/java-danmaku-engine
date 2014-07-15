package com.jde.main;

import org.lwjgl.LWJGLException;

import com.jde.model.utils.Parser;
import com.jde.view.Window;

public class Main {

	public Main() {

	}

	public static void main(String[] args) {
		try {
			Parser parser = new Parser();
			Window w = new Window(parser.parseXML("res/test.xml"), 640, 480);

		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
