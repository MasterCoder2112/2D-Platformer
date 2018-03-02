package com.base;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import com.entities.Entity;
import com.entities.Player;
import com.entities.Projectile;
import com.input.InputHandler;
import com.structures.Platform;

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
	public static Player player;
	
	//Sets up variables
	public Display() 
	{
		//Handles key events 
		input = new InputHandler();
		addKeyListener(input);
		player = new Player();
		
		//Starts new Game object for handling events
		game = new Game();
	}
	
	//Repaints the screen
	public void paint(Graphics g)
	{
		try
		{
			//Redraws screen
		    screen = createImage(getWidth(),getHeight());
		    graph = screen.getGraphics();
			
		    //Sets players color
			graph.setColor(new Color(0, 0, 100));
			
			//Sets font of any text drawn on the screen
			graph.setFont(new Font("Nasalization", 1, 15));
			
			//If player is dead
			if(!Player.isAlive)
			{
				graph.setColor(new Color(255, 0, 0));
				
				graph.drawString("To Restart, press R", 125, 25);
			}
			
			//Show players health
			graph.drawString("Health: "+Player.health, 25, 25);
			
			//If player is in god mode
			if(Player.godMode)
			{
				graph.drawString("God Mode: On", 150, 25);
			}
			
			//Draws player
			graph.fillRect((int)player.x, (int)player.topOfPlayer, player.girth,
					(int)player.y - (int)player.topOfPlayer);
			
			//Draw all the platforms
			for(int i = 0; i < Game.platforms.size(); i++)
			{
				Platform pf = Game.platforms.get(i);
				graph.fillRect((int)pf.x, (int)pf.y, pf.width, pf.height);
				//graph.fillOval((int)pf.x, (int)pf.y, pf.width, pf.height);
			}
			
			//Draw all the platforms
			for(int i = 0; i < Game.entities.size(); i++)
			{
				Entity e = Game.entities.get(i);
				
				if(e.isMeleeing)
				{
					graph.fillRect((int)e.x - (e.girth / 2), (int)e.topOfEntity + (int)(e.height / 2),
							(int)e.y - (int)e.topOfEntity, e.girth);
				}
				else
				{
					graph.fillRect((int)e.x, (int)e.topOfEntity, e.girth, (int)e.y - (int)e.topOfEntity);
				}
			}
			
			//Draw all the projectiles
			for(int i = 0; i < Game.projectiles.size(); i++)
			{
				Projectile p = Game.projectiles.get(i);
				//graph.fillRect((int)p.x, (int)p.y, p.width, p.height);
				graph.fillOval((int)p.x, (int)p.y, p.width, p.height);
			}
		
		    // At the end of the method, draw the backBuffer to the 
		    // screen.
		    g.drawImage(screen, 0, 0, this);
	
			g.dispose();
		}
		catch(Exception e)
		{
			//Do nothing, we don't want these errors showing
		}
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
