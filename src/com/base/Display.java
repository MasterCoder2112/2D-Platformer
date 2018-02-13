package com.base;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import com.entities.Player;
import com.input.InputHandler;

/**
 * @title Display
 * @author Alexander Byrd
 * Date Created: February 12, 2018
 * 
 * Description:
 * Updates graphical objects and sprites on the screen every tick.
 * May do more in the future but we will get there later.
 *
 */
public class Display extends Canvas implements Runnable
{
	private static final long serialVersionUID = 1L;
	
	
	private InputHandler input;
	public static Thread mainThread;
	private boolean isRunning = true;
	private Game game;
	private Image screen;
	private Graphics graph;
	
	//Sets up variables
	public Display() 
	{
		//Handles key events 
		input = new InputHandler();
		addKeyListener(input);
		
		//Starts new Game object for handling events
		game = new Game();
	}
	
	//Repaints the screen
	public void paint(Graphics g)
	{
		//Redraws screen
	    screen = createImage(getWidth(),getHeight());
	    graph = screen.getGraphics();
		
	    //Sets players color
		graph.setColor(new Color(25, 25, 0));
		
		//Draws player
		graph.fillRect((int)game.player.x, (int)game.player.topOfPlayer, game.player.girth,
				(int)game.player.y - (int)game.player.topOfPlayer);
	
	    // At the end of the method, draw the backBuffer to the 
	    // screen.
	    g.drawImage(screen, 0, 0, this);

		g.dispose();
	}
	
	@Override
   /**
    * Overides the canvas' default update (refresh) method to prevent flickering.
    */
	public void update(Graphics g)
	{
		paint(g);
	}
	
   /**
    * Start the main game thread
    */
	public void start()
	{
		mainThread = new Thread(this);
		mainThread.start();
	}

	@Override
	/**
	 * Method called when the game thread is running
	 */
	public void run() 
	{
		//While game is running tick events
		while(isRunning)
		{
			this.requestFocus();
			isRunning = game.tick(input.key);
			
			//Calls paint method
			this.repaint();
		}
		
		exit();
	}
	
   /**
    * Exits the game by ending the thread and exiting the app
    */
	public void exit()
	{
		System.exit(0);
		
		try
		{
			mainThread.join();
		}
		catch(Exception e)
		{
			System.out.println("Unable to end thread");
		}
	}

}
