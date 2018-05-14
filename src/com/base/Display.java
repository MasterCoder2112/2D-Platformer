package com.base;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.entities.Entity;
import com.entities.Player;
import com.entities.Projectile;
import com.input.InputHandler;
import com.structures.Block;
import com.structures.Platform;
import com.structures.Unit;
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
	public static Player player;
	
	//Controls all sounds and music
	private SoundController sc;
	
	//BufferedImages for graphics
	private BufferedImage player1;
	private BufferedImage player2;
	private BufferedImage player3;
	private BufferedImage player4;
	private BufferedImage playerStopped;
	private BufferedImage playerDead;
	private BufferedImage background;
	private BufferedImage texture1;
	private BufferedImage texture2;
	private BufferedImage texture3;
	private BufferedImage texture4;
	private BufferedImage entity1;
	private BufferedImage entity2;
	private BufferedImage entity3;
	private BufferedImage entity4;
	private BufferedImage entityDead;
	
	//Sets up variables
	public Display() 
	{
		//Handles key events 
		input = new InputHandler();
		addKeyListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
		player = new Player();
		
		//Starts new Game object for handling events
		game = new Game();
		
		//Loads all graphics into the game
		loadGraphics();
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
			graph.setColor(new Color(0x00007b));
			
			//Sets font of any text drawn on the screen
			graph.setFont(new Font("Nasalization", 1, 15));
			
			//Draws Background
			graph.drawImage(background, 0, 0, RunGame.WIDTH,
					RunGame.HEIGHT, null);
			
			
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
				graph.drawString("God Mode: On", 350, 25);
			}
			
			BufferedImage playerImage = player2;
			
			//Determine player image depending on how long player was
			//moving for.
			if(Game.playerMovement != 0)
			{
				if(Game.playerMovement < 4000)
				{
					playerImage = player1;
				}
				else if(Game.playerMovement < 8000)
				{
					playerImage = player2;
				}
				else if(Game.playerMovement < 12000)
				{
					playerImage = player3;
				}
				else if(Game.playerMovement < 16000)
				{
					playerImage = player4;
				}
			}
			else
			{
				playerImage = playerStopped;
			}
			
			if(!Player.isAlive)
			{
				playerImage = playerDead;
			}
				
			//Draws player depending on direction the player is facing
			if(player.direction == 1)
			{
				graph.drawImage(playerImage, (int)player.x,
						(int)player.topOfPlayer, player.girth,
					((int)player.y - (int)player.topOfPlayer), null);
			}
			else
			{
				graph.drawImage(playerImage, (int)player.x + player.girth,
						(int)player.topOfPlayer, -player.girth,
					((int)player.y - (int)player.topOfPlayer), null);
			}
			
			graph.drawRect((int)player.x,
					(int)player.topOfPlayer, player.girth,
				((int)player.y - (int)player.topOfPlayer));
			
			//Draw all the platforms
			/*
			for(int i = 0; i < Game.currentMap.platforms.size(); i++)
			{
				Platform p = Game.currentMap.platforms.get(i);
				
				BufferedImage unit = texture1;
				
				//Depending on type of platform, change unit texture
				switch(p.type)
				{
					case 1:
						unit = texture1;
						break;
					
					case 2:
						unit = texture2;
						break;
						
					case 3: 
						unit = texture3;
						break;
						
					default:
						unit = texture4;
						break;
				}
				
				for(int j = 0; j < p.pUnits.size(); j++)
				{
					Unit u = p.pUnits.get(j);
					graph.drawImage(unit,(int)u.x, (int)u.y, Unit.UNIT_LENGTH, Unit.UNIT_LENGTH, null);
					//graph.fillRect((int)u.x, (int)u.y, Unit.UNIT_LENGTH, Unit.UNIT_LENGTH);
					//graph.fillOval((int)pf.x, (int)pf.y, pf.width, pf.height);
				}
			}*/
			
			//Draw all blocks (TESTING RIGHT NOW)
			for(int i = 0; i < Game.currentMap.blocks.size(); i++)
			{
				Block b = Game.currentMap.blocks.get(i);
				
				BufferedImage block = texture1;
				
				//Depending on type of platform, change unit texture
				switch(b.type)
				{
					case 1:
						block = texture1;
						break;
					
					case 2:
						block = texture2;
						break;
						
					case 3: 
						block = texture3;
						break;
						
					default:
						block = texture4;
						break;
				}

				graph.drawImage(block,(int)b.x, (int)b.y, b.width * Unit.UNIT_LENGTH, b.width * Unit.UNIT_LENGTH, null);
				//graph.fillRect((int)u.x, (int)u.y, Unit.UNIT_LENGTH, Unit.UNIT_LENGTH);
				//graph.fillOval((int)pf.x, (int)pf.y, pf.width, pf.height);
			}
			
			//Draw all the platforms
			for(int i = 0; i < Game.currentMap.entities.size(); i++)
			{
				Entity e = Game.currentMap.entities.get(i);
				
				BufferedImage entityImage = entity1;
				
				//Determine player image depending on how long player was
				//moving for.
				if(e.entityMovement != 0)
				{
					if(e.entityMovement < 4000)
					{
						entityImage = entity1;
					}
					else if(e.entityMovement < 8000)
					{
						entityImage = entity2;
					}
					else if(e.entityMovement < 12000)
					{
						entityImage = entity3;
					}
					else if(e.entityMovement < 16000)
					{
						entityImage = entity4;
					}
				}
				else
				{
					entityImage = entity2;
				}
				
				if(e.isMeleeing)
				{
					graph.fillRect((int)e.x - (e.girth / 2), (int)e.topOfEntity + (int)(e.height / 2),
							(int)e.y - (int)e.topOfEntity, e.girth);
				}
				else
				{	
					//Draws player depending on direction the player is facing
					if(e.direction == 1)
					{
						graph.drawImage(entityImage, (int)e.x, (int)e.topOfEntity, e.girth,
								(int)e.y - (int)e.topOfEntity, null);
					}
					else
					{
						graph.drawImage(entityImage, (int)e.x + e.girth, (int)e.topOfEntity, -e.girth,
								(int)e.y - (int)e.topOfEntity, null);
					}
				}
			}
			
			//Draw all the projectiles
			for(int i = 0; i < Game.projectiles.size(); i++)
			{
				Projectile p = Game.projectiles.get(i);
				//graph.fillRect((int)p.x, (int)p.y, p.width, p.height);
				graph.fillOval((int)p.x, (int)p.y, p.width, p.height);
			}
			
			//Draws makeshift crosshair just so I can see where the cursor is, it's pretty off center
			graph.fillRect((int)InputHandler.mouseX, (int)InputHandler.mouseY, 9, 3);
			graph.fillRect((int)InputHandler.mouseX + 3, (int)InputHandler.mouseY - 3, 3, 9);
		
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
	
   /**
    * Load all graphics images in and scan for transparency pixels
    */
	public void loadGraphics()
	{	
		//Try to load graphics file
		try
		{
			playerStopped = ImageIO.read
					(new File("resources/playerStopped.png"));
			makeTransparent(playerStopped);
		}
		catch(Exception e)
		{
			//Leave as default black box if no textures found
		}
		
		//Try to load graphics file
		try
		{
			player1 = ImageIO.read
					(new File("resources/player1.png"));
			makeTransparent(player1);
		}
		catch(Exception e)
		{
			//Leave as default black box if no textures found
		}
		
		//Try to load graphics file
		try
		{
			player2 = ImageIO.read
					(new File("resources/player2.png"));
			makeTransparent(player2);
		}
		catch(Exception e)
		{
			//Leave as default black box if no textures found
		}
		
		//Try to load graphics file
		try
		{
			player3 = ImageIO.read
					(new File("resources/player3.png"));
			makeTransparent(player3);
		}
		catch(Exception e)
		{
			//Leave as default black box if no textures found
		}
		
		//Try to load graphics file
		try
		{
			player4 = ImageIO.read
					(new File("resources/player4.png"));
			makeTransparent(player4);
		}
		catch(Exception e)
		{
			//Leave as default black box if no textures found
		}
		
		//Try to load graphics file
		try
		{
			playerDead = ImageIO.read
					(new File("resources/playerDead.png"));
			makeTransparent(playerDead);
		}
		catch(Exception e)
		{
			//Leave as default black box if no textures found
		}
		
		//Try to load graphics file
		try
		{
			entity1 = ImageIO.read
					(new File("resources/entity1.png"));
			makeTransparent(entity1);
		}
		catch(Exception e)
		{
			//Leave as default black box if no textures found
		}
		
		//Try to load graphics file
		try
		{
			entity2 = ImageIO.read
					(new File("resources/entity2.png"));
			makeTransparent(entity2);
		}
		catch(Exception e)
		{
			//Leave as default black box if no textures found
		}
		
		//Try to load graphics file
		try
		{
			entity3 = ImageIO.read
					(new File("resources/entity3.png"));
			makeTransparent(entity3);
		}
		catch(Exception e)
		{
			//Leave as default black box if no textures found
		}
		
		//Try to load graphics file
		try
		{
			entity4 = ImageIO.read
					(new File("resources/entity4.png"));
			makeTransparent(entity4);
		}
		catch(Exception e)
		{
			//Leave as default black box if no textures found
		}
		
		//Try to load graphics file
		try
		{
			background = ImageIO.read
					(new File("resources/background.png"));
		}
		catch(Exception e)
		{
			//Leave as default black box if no textures found
		}
		
		//Try to load graphics file
		try
		{
			texture1 = ImageIO.read
					(new File("resources/texture1.png"));
			makeTransparent(texture1);
		}
		catch(Exception e)
		{
			//Leave as default black box if no textures found
		}
		
		//Try to load graphics file
		try
		{
			texture2 = ImageIO.read
					(new File("resources/texture2.png"));
			makeTransparent(texture2);
		}
		catch(Exception e)
		{
			//Leave as default black box if no textures found
		}
		
		//Try to load graphics file
		try
		{
			texture3 = ImageIO.read
					(new File("resources/texture3.png"));
			makeTransparent(texture3);
		}
		catch(Exception e)
		{
			//Leave as default black box if no textures found
		}
		
		//Try to load graphics file
		try
		{
			texture4 = ImageIO.read
					(new File("resources/texture4.png"));
			makeTransparent(texture4);
		}
		catch(Exception e)
		{
			//Leave as default black box if no textures found
		}
	}
	
   /**
    * Makes a given image sent in transparent
    * @param image
    */
	public void makeTransparent(BufferedImage image)
	{
		//Effectively makes all white pixels in image transparent
		for (int x = 0; x < image.getWidth(); ++x)
		{
			for (int y = 0; y < image.getHeight(); ++y)
			{
				//Masks RGB value at a given x, y pixel in the image, and if
				//it is white (0xFFFFFF) then set that pixel to being transparent
				//(aka. a value of 0)
				if ((image.getRGB(x, y) & 0x00FFFFFF) == 0x00FFFFFF) 
				{
					//System.out.println(image.getRGB(x, y));
				    image.setRGB(x, y, 0);
				}
			}
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
		sc = new SoundController();
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
			SoundController.music.playAudioFile(0);
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
		sc.resetSounds();
		System.exit(0);
		
		try
		{
			mainThread.join();
		}
		catch(Exception e)
		{
			System.out.println("Unable to end thread");
			System.exit(0);
		}
	}

}
