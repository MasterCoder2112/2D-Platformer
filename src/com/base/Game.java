package com.base;

import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import com.entities.Entity;
import com.entities.Player;
import com.entities.Projectile;
import com.structures.Platform;

/**
 * @title Game
 * @author Alexander Byrd
 * Date Created: February 12, 2018
 * 
 * Description:
 * Initializes game threads and game events. Basically handles all the
 * actions that will occur within gameplay.
 *
 */
public class Game
{
	public static int tickCount = 0;
	public static final double GRAVITY = 0.000005;
	
	public static ArrayList<Platform> platforms = new ArrayList<Platform>();
	public static ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	public static ArrayList<Entity> entities = new ArrayList<Entity>();
	
	private int shootTimer = 0; //Allows for shooting not to be as fast
	private int eventTimer = 0; //Allows events not to be activated as fast
	
	//Set up initial variables
	public Game() 
	{
		//Constructs InfDev level for testing
		testLevel();
	}
	
   /**
    * Handles game events and keyboard inputs
    * @param key
    */
	public boolean tick(boolean key[])
	{
		tickCount++;

		/* KEY EVENTS ****/
		
		//If quitting
		if(key[KeyEvent.VK_ESCAPE])
		{
			RunGame.frame.dispose();
			return false;
		}
		
		//If restarting, reset values and reset level
		if(key[KeyEvent.VK_R])
		{
			Display.player = new Player();
			projectiles = new ArrayList<Projectile>();
			entities = new ArrayList<Entity>();
			platforms = new ArrayList<Platform>();
			testLevel();
		}
		
		//If restarting, reset values and reset level
		if(key[KeyEvent.VK_G] && eventTimer == 0)
		{
			if(Player.godMode)
			{
				Player.godMode = false;
			}
			else
			{
				Player.godMode = true;
			}
			
			eventTimer++;
		}
		
		//Update all the platforms
		for(Platform pf: platforms)
		{
			pf.updatePlatform();
		}
		
		//Update all the entities
		for(Entity e: entities)
		{
			e.updateEntity();
		}
		
		ArrayList<Projectile> delete = new ArrayList<Projectile>();
		
		//Update all the projectiles
		for(Projectile p: projectiles)
		{
			p.move();
			
			//If it is to be deleted
			if(p.toBeDeleted)
			{
				delete.add(p);
			}
		}
		
		//Remove those projectiles that are to be deleted from the game
		for(Projectile p: delete)
		{
			projectiles.remove(p);
		}
		
		//Only allow player to move and update if alive
		if(Player.isAlive)
		{
			//If running
			if(key[KeyEvent.VK_SHIFT])
			{
				Display.player.running = true;
			}
			else
			{
				Display.player.running = false;
			}
			
			//If jumping and player is on the ground
			if(key[KeyEvent.VK_UP] && Player.y == Player.floor)
			{
				Display.player.jumping = true;
				Display.player.upSpeed = Player.DEFAULT_JUMP_SPEED;
			}
			
			//If crouching
			if(key[KeyEvent.VK_DOWN])
			{
				Display.player.crouching = true;
			}
			else
			{
				Display.player.crouching = false;
			}
			
			double xa = 0;
			
			//If moving right
			if(key[KeyEvent.VK_RIGHT])
			{		
				xa = Display.player.speed;
				Display.player.direction = 1;
			}
			
			//If moving left
			if(key[KeyEvent.VK_LEFT])
			{
				xa = -Display.player.speed;
				Display.player.direction = -1;
			}
			
			//If shooting a projectile
			if(key[KeyEvent.VK_V] && shootTimer == 0)
			{
				//Depending on direction, have projectile come out of left or
				//right side of Display.player.
				if(Display.player.direction == 1)
				{
					new Projectile(Player.x + Player.girth, Player.y - Player.height,
						0.02, 25, Display.player.direction, 0, null, Math.PI / 2, 0);
				}
				else
				{
					new Projectile(Player.x, Player.y - Player.height,
							0.02, 25, Display.player.direction, 0, null, Math.PI / 2, 0);
				}
				
				shootTimer++;
			}
			
		   /*
		    * If player is in the air, any movement from side to side
		    * is going to be much slower as it is hard to change direction
		    * while in air
		    */
			if(Display.player.inAir)
			{
				xa /= 3;
			}
	
			Display.player.move(xa);
		}
		
		//Keep updating time after the player shot last here
		if(shootTimer > 0)
		{
			shootTimer++;
		}
		
		//If above 5001, then reset to 0 so the player can shoot again
		if(shootTimer > 5001)
		{
			shootTimer = 0;
		}
		
		//Same as shootTimer but for events
		if(eventTimer > 0)
		{
			eventTimer++;
		}
		
		//Diddo
		if(eventTimer > 2501)
		{
			eventTimer = 0;
		}
		
		//If player is dead. Reset his/her look
		if(!Player.isAlive)
		{
			Player.height = 15;
			Display.player.topOfPlayer = Player.y - Player.height;
			Player.girth = Player.DEFAULT_HEIGHT;
		}
		
		return true;
	}
	
   /**
    * Generates the test level for InfDev testing
    */
	public void testLevel()
	{
		new Platform(100, RunGame.HEIGHT - 105, 200, 15);
		new Platform(400, 400, 200, 15);
		//new Platform(300, 400, 300, 25, 100, 0, 0.01, 0, true, false);
		//new Platform(100, 125, 100, 25, 0, 350, 0, 0.01, true, false);
		//new Platform(300, 200, 100, 25, 200, 100, 0.01, 0.01, true, false);
		new Entity(0, 600, RunGame.HEIGHT - 30, false);
	}
}
