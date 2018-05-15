package com.base;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * Title: RunGame
 * @author Alex Byrd
 * Date Created: 2/12/2018
 *
 * Sets up the frame of the game, the cursor, and instantiates the
 * new display object. As well as shows version number
 */
public class RunGame 
{
	public static JFrame frame;
	public static Display display;
	private String dev = "Infdev";
	private double versionNum = 0.35;
	public static final int WIDTH = 810;
	public static final int HEIGHT = 600;
	
	public RunGame()
	{
		/*
		    * Sets up image of cursor, its width and height, and it's type of 
		    * BufferedImage.
		    */
			BufferedImage cursor = new BufferedImage
					(16, 16, BufferedImage.TYPE_INT_ARGB);
			
		   /*
		    * Sets up a new cursor of custom type to be used. This cursor is 
		    * blank, meaning it is completely see through and won't be seen
		    * in the way of the graphics when playing the game.
		    */
			Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor
					(cursor, new Point(0, 0), "blank");
			
		   /*
		    * Self explanitory stuff
		    */
			display = new Display();
			frame = new JFrame("Excelsior Version "+dev+" "+versionNum);
			frame.add(display);
			frame.setSize(WIDTH, HEIGHT);
			frame.getContentPane().setCursor(blank);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setResizable(false);
			frame.setLocationRelativeTo(null);
			
			//ImageIcon titleIcon = new ImageIcon("resources"+FPSLauncher.themeName+"/textures/hud/titleIcon.png");
			//frame.setIconImage(titleIcon.getImage());
			frame.setVisible(true);
			frame.setAlwaysOnTop(true);
			frame.setFocusableWindowState(true);
			frame.setAutoRequestFocus(true);
			frame.setFocusable(true);

			//Start the game.
			display.start();
	}
}

