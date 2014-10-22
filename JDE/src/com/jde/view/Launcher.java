package com.jde.view;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.lwjgl.LWJGLException;

import com.jde.model.utils.Parser;

public class Launcher {

	/**
	 * Launch the application.
	 */
	public static void launch() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Launcher window = new Launcher();
					window.frmJavaDanmakuEngine.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private JFrame frmJavaDanmakuEngine;
	private JTextField textFieldWidthRes;
	private JTextField textFieldHeightRes;

	private final ButtonGroup buttonGroup = new ButtonGroup();

	/**
	 * Create the application.
	 */
	public Launcher() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		// Detect XML game files
		File folder = new File("res/games/");
		ArrayList<String> listOfFiles = new ArrayList<String>();

		for (File f : folder.listFiles())
		    if (f.isFile() && f.getName().endsWith(".xml"))
		    	listOfFiles.add(f.getName());
		
		String[] arrayOfFiles = new String[listOfFiles.size()];
		int i = 0;
		for (String s : listOfFiles)
			arrayOfFiles[i++] = s;
		
			
		// Swing
		frmJavaDanmakuEngine = new JFrame();
		frmJavaDanmakuEngine.setResizable(false);
		frmJavaDanmakuEngine.setTitle("Java Danmaku Engine" + Game.VERSION + " - Launcher");
		frmJavaDanmakuEngine.setBounds(100, 100, 480, 240);
		frmJavaDanmakuEngine.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmJavaDanmakuEngine.getContentPane().setLayout(null);
		
		final JComboBox<String> comboBoxResolution = new JComboBox<String>();
		comboBoxResolution.setToolTipText("Those are the most used 4:3 default resolutions");
		comboBoxResolution.setModel(new DefaultComboBoxModel<String>(new String[] {"640x480", "960x720", "1280x960"}));
		comboBoxResolution.setSelectedIndex(1);
		comboBoxResolution.setBounds(191, 7, 271, 24);
		frmJavaDanmakuEngine.getContentPane().add(comboBoxResolution);
		
		final JRadioButton rdbtnDefaultRes = new JRadioButton("Default resolution:");
		buttonGroup.add(rdbtnDefaultRes);
		rdbtnDefaultRes.setSelected(true);
		rdbtnDefaultRes.setBounds(8, 8, 175, 23);
		frmJavaDanmakuEngine.getContentPane().add(rdbtnDefaultRes);
		
		JRadioButton rdbtnCustomRes = new JRadioButton("Custom resolution:");
		buttonGroup.add(rdbtnCustomRes);
		rdbtnCustomRes.setBounds(8, 35, 175, 23);
		frmJavaDanmakuEngine.getContentPane().add(rdbtnCustomRes);
		
		textFieldWidthRes = new JTextField();
		textFieldWidthRes.setBounds(191, 37, 114, 19);
		frmJavaDanmakuEngine.getContentPane().add(textFieldWidthRes);
		textFieldWidthRes.setColumns(10);
		
		textFieldHeightRes = new JTextField();
		textFieldHeightRes.setBounds(348, 37, 114, 19);
		frmJavaDanmakuEngine.getContentPane().add(textFieldHeightRes);
		textFieldHeightRes.setColumns(10);
		
		JLabel lblX = new JLabel("x");
		lblX.setBounds(323, 37, 27, 19);
		frmJavaDanmakuEngine.getContentPane().add(lblX);
		
		JLabel lblFixedFps = new JLabel("Fixed FPS:");
		lblFixedFps.setBounds(8, 66, 90, 15);
		frmJavaDanmakuEngine.getContentPane().add(lblFixedFps);
		
		final JComboBox<String> comboBoxFixedFps = new JComboBox<String>();
		comboBoxFixedFps.setToolTipText("<html>This option forces the computer to play at specific frame rate (60 for example),<br>and the physics engine has 3 times more frames (180 in the example) to improve accuracy.<br>Also, if your computer cannot keep up the target FPS, the game will slow down to avoid skipping frames.<br><br>When \"Unlimited\" is selected, the computer will process as much frames as it can.<br>The game will not slow down if need. This mode is not recommended,<br>because fps drops can be very unestable for the engine.<br>But it is useful to check how much fixed FPS your computer is able to handle.</html>");
		comboBoxFixedFps.setModel(new DefaultComboBoxModel<String>(new String[] {"30 FPS [90 engine FPS] (not recommended)", "60 FPS [180 engine FPS] (recommended for average PCs)", "120 FPS [360 engine FPS] (recommended for high end PCs)", "Unlimited (not recommended, unstable)"}));
		comboBoxFixedFps.setSelectedIndex(1);
		comboBoxFixedFps.setBounds(8, 88, 454, 24);
		frmJavaDanmakuEngine.getContentPane().add(comboBoxFixedFps);
		
		JLabel lblAvailableGames = new JLabel("Available games:");
		lblAvailableGames.setBounds(12, 124, 450, 15);
		frmJavaDanmakuEngine.getContentPane().add(lblAvailableGames);
		
		final JComboBox<String> comboBoxAvailableGames = new JComboBox<String>();
		comboBoxAvailableGames.setBounds(8, 146, 454, 24);
		comboBoxAvailableGames.setModel(new DefaultComboBoxModel<String>(arrayOfFiles));
		frmJavaDanmakuEngine.getContentPane().add(comboBoxAvailableGames);
		
		JButton btnPlay = new JButton("Play");
		btnPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				// Check resolution
				int w = 640, h = 480;
				
				if (rdbtnDefaultRes.isSelected()) {
					switch (comboBoxResolution.getSelectedIndex()) {
						
					case 1:
						w = 960;
						h = 720;
						break;
						
					case 2:
						w = 1280;
						h = 960;
						break;
					}
				}
				else {
					w = Integer.parseInt(textFieldWidthRes.getText());
					h = Integer.parseInt(textFieldHeightRes.getText());
				}
				
				// Check fixed FPS
				int fps = 60;
				
				switch (comboBoxFixedFps.getSelectedIndex()) {
				case 0:
					fps = 30;
					break;
				case 2:
					fps = 120;
					break;
				case 3:
					fps = 0;
					break;
				}
				
				// Start playing
				Game newGame;
				
				try {
					Parser parser = new Parser();
					newGame = parser.parseXML("res/games/" + comboBoxAvailableGames.getSelectedItem());
					new Window(newGame, w, h, fps);
					
				} catch (LWJGLException ex) {
					ex.printStackTrace();
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				
			}
		});
		btnPlay.setBounds(8, 182, 454, 25);
		frmJavaDanmakuEngine.getContentPane().add(btnPlay);
	}
}
