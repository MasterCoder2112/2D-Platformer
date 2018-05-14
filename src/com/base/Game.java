package com.base;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

import com.entities.Entity;
import com.entities.Player;
import com.entities.Projectile;
import com.input.InputHandler;
import com.structures.Block;
import com.structures.Map;
import com.structures.Platform;
import com.structures.Unit;

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
	
	public static ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	
	public static Map unmodifiedMap = new Map();
	public static Map currentMap = new Map();
	public static int playerMovement = 0;
	public static int shootTimer = 0; //Allows for shooting not to be as fast
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
		if(key[KeyEvent.VK_R] && eventTimer == 0)
		{
			Display.player = new Player();
			projectiles = new ArrayList<Projectile>();
			currentMap.entities = new ArrayList<Entity>();
			currentMap.platforms = new ArrayList<Platform>();
			restart();
			eventTimer++;
		}
		
		//If t is pressed, load the test level
		if(key[KeyEvent.VK_T] && eventTimer == 0)
		{
			Display.player = new Player();
			projectiles = new ArrayList<Projectile>();
			currentMap.entities = new ArrayList<Entity>();
			currentMap.platforms = new ArrayList<Platform>();
			testLevel();
			eventTimer++;
		}
		
		//If z is pressed, load a random level
		if(key[KeyEvent.VK_Z] && eventTimer == 0)
		{
			Display.player = new Player();
			projectiles = new ArrayList<Projectile>();
			currentMap.entities = new ArrayList<Entity>();
			currentMap.platforms = new ArrayList<Platform>();
			randomLevel();
			eventTimer++;
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
		for(Platform pf: currentMap.platforms)
		{
			pf.updatePlatform();
		}
		
		//Update all the units
		for(Unit u: currentMap.solidUnits)
		{
			u.updateUnit();
		}
		
		//Update all the blocks
		for(Block b: currentMap.blocks)
		{
			b.setUpBlock();
		}
		
		//Update all the entities
		for(int i = 0; i < currentMap.entities.size(); i++)
		{
			Entity e = currentMap.entities.get(i);
			e.updateEntity();
		}
		
		ArrayList<Projectile> delete = new ArrayList<Projectile>();
		
		//Update all the projectiles
		try
		{
			for(Projectile p: projectiles)
			{
				p.move();
				
				//If it is to be deleted
				if(p.toBeDeleted)
				{
					delete.add(p);
				}
			}
		}
		catch(Exception e)
		{
			
		}
		
		//Remove those projectiles that are to be deleted from the game
		for(Projectile p: delete)
		{
			projectiles.remove(p);
		}
		
		//Players x movement each tick
		double xa = 0;
		
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
			if((key[KeyEvent.VK_UP] || key[KeyEvent.VK_SPACE] || key[KeyEvent.VK_W]) && Player.y == Player.floor)
			{
				Display.player.jumping = true;
				Display.player.upSpeed = Player.DEFAULT_JUMP_SPEED;
			}
			
			//If crouching
			if(key[KeyEvent.VK_DOWN] || key[KeyEvent.VK_C] || key[KeyEvent.VK_S])
			{
				Display.player.crouching = true;
			}
			else
			{
				Display.player.crouching = false;
			}
			
			//If moving right
			if(key[KeyEvent.VK_RIGHT] || key[KeyEvent.VK_D])
			{		
				xa = Display.player.speed;
				Display.player.direction = 1;
			}
			
			//If moving left
			if(key[KeyEvent.VK_LEFT] || key[KeyEvent.VK_A])
			{
				xa = -Display.player.speed;
				Display.player.direction = -1;
			}
			
			//If shooting a projectile
			if((key[KeyEvent.VK_V]) && shootTimer == 0)
			{
				SoundController.laser.playAudioFile(0);
				//Depending on direction, have projectile come out of left or
				//right side of Display.player.
				if(Display.player.direction == 1)
				{
					new Projectile(Player.x + Player.girth, Player.y - (Player.height * 3 / 4),
						0.02, 25, Display.player.direction, 0, null, Math.PI / 2, 0);
				}
				else
				{
					new Projectile(Player.x, Player.y - (Player.height * 3 / 4),
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
		}
		
		//Resets player movement each time so that the player moving
		//image continues to update. Is set to 0 if the player stopped
		//moving
		if(xa != 0)
		{
			playerMovement++;
			
			if(playerMovement >= 16000)
			{
				playerMovement = 1;
			}
		}
		else
		{
			playerMovement = 0;
		}
		
		Display.player.move(xa);
		
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
    * Resets the current map. This does not create a new one. Basically just
    * creates a copy of all the things in the unmodified map and places it 
    * into the new currentMap.
    */
	public void restart()
	{
		currentMap = new Map();
		
		for(Platform p: unmodifiedMap.platforms)
		{
			p = new Platform((int)p.x, (int)p.y, p.width, p.height, p.xMoveDist, p.yMoveDist, p.xSpeed, p.ySpeed,
					p.isSolid, p.isLiquid, p.type);
			currentMap.platforms.add(p);
		}
		
		for(Entity e: unmodifiedMap.entities)
		{
			e = new Entity(0, e.x, e.y, e.isFriendly);
			currentMap.entities.add(e);
		}
		
		for(Unit u: unmodifiedMap.solidUnits)
		{
			u = new Unit(u.type, u.startX, u.startY, u.xSpeed, u.ySpeed, u.xDist, u.yDist);
			currentMap.solidUnits.add(u);
		}
	}
	
   /**
    * Generates the a random level for InfDev testing
    */
	public void randomLevel()
	{
		unmodifiedMap = new Map();
		currentMap = new Map();
		
		//Chooses random values for everything
		Random rand =  new Random();
		
		for(int i = 0; i < 5; i++)
		{
			int xEnd = rand.nextInt(1);
			int yEnd = rand.nextInt(1);
			
			int rX = rand.nextInt(1);
			int rY = rand.nextInt(1);
			
			//Randomize which direction platform moves
			if(xEnd == 0)
			{
				xEnd = rand.nextInt(RunGame.WIDTH);
			}
			else
			{
				xEnd = -rand.nextInt(RunGame.WIDTH);
			}
			
			if(yEnd == 0)
			{
				yEnd = rand.nextInt(RunGame.WIDTH);
			}
			else
			{
				yEnd = -rand.nextInt(RunGame.WIDTH);
			}
			
			//Randomize Whether platform starts in negative or positive x
			//Also get rid of any remainder if not unit of 15
			if(rX == 0)
			{
				rX = rand.nextInt(RunGame.WIDTH);
				
				rX -= rX % 15;
			}
			else
			{
				rX = -rand.nextInt(RunGame.WIDTH);
				
				rX -= rX % 15;
			}
			
			//Same thing but for yPos.
			if(rY == 0)
			{
				rY = rand.nextInt(RunGame.WIDTH);
				
				rY -= rY % 15;
			}
			else
			{
				rY = -rand.nextInt(RunGame.WIDTH);
				
				rY -= rY % 15;
			}
			
			int w = rand.nextInt(RunGame.WIDTH);
			int h = rand.nextInt(RunGame.HEIGHT - 30);
			
			Platform temp = new Platform(rX, rY,
					w - (w % 15),
					h - (h % 15),
					xEnd, yEnd,
					rand.nextInt(10) / 100.0, rand.nextInt(10) / 100.0, true, false, rand.nextInt(4) + 1);
			
			Platform temp2 = new Platform(rX, rY,
					w - (w % 15),
					h - (h % 15),
					xEnd, yEnd,
					rand.nextInt(10) / 100.0, rand.nextInt(10) / 100.0, true, false, rand.nextInt(4) + 1);
			
			currentMap.platforms.add(temp);
			unmodifiedMap.platforms.add(temp2);
		}
		
		boolean isFriend = false;
		
		if(rand.nextInt(10) == 0)
		{
			isFriend = true;
		}
		
		Entity temp = new Entity(0, rand.nextInt(RunGame.WIDTH),
				rand.nextInt(RunGame.HEIGHT - 30), isFriend);
		
		//The copy of it
		Entity temp2 = new Entity(0, rand.nextInt(RunGame.WIDTH),
				rand.nextInt(RunGame.HEIGHT - 30), isFriend);
		
		//Add entity to the game
		currentMap.entities.add(temp);
		
		unmodifiedMap.entities.add(temp2);
	}
	
   /**
    * Generates the default test level for InfDev testing
    */
	public void testLevel()
	{
		unmodifiedMap = new Map();
		currentMap = new Map();
		
		Platform p1 = new Platform(105, 330, 180, 15, 0, 250, 0, -0.01, true, false, 4);
		Platform p2 = new Platform(405, 225, 120, 120, 0, 0, 0, 0, true, false, 2);
		Platform p3 = new Platform(300, 405, 300, 30, 100, 0, 0.01, 0, true, false, 3);
		Platform p4 = new Platform(105, 300, 105, 30, 0, 350, 0, 0, true, false, 1);
		Platform p5 = new Platform(300, 300, 30, 105, 100, 0, 0.01, 0, true, false, 2);
		
		//Copies of those platforms
		Platform x1 = new Platform((int)p1.x, (int)p1.y, p1.width, p1.height, p1.xMoveDist,
				p1.yMoveDist, p1.xSpeed, p1.ySpeed, p1.isSolid, p1.isLiquid, p1.type);
		Platform x2 = new Platform((int)p2.x, (int)p2.y, p2.width, p2.height, p2.xMoveDist,
				p2.yMoveDist, p2.xSpeed, p2.ySpeed, p2.isSolid, p2.isLiquid , p2.type);
		Platform x3 = new Platform((int)p3.x, (int)p3.y, p3.width, p3.height, p3.xMoveDist,
				p3.yMoveDist, p3.xSpeed, p3.ySpeed, p3.isSolid, p3.isLiquid, p3.type);
		Platform x4 = new Platform((int)p4.x, (int)p4.y, p4.width, p4.height, p4.xMoveDist,
				p4.yMoveDist, p4.xSpeed, p4.ySpeed, p4.isSolid, p4.isLiquid, p4.type);
		Platform x5 = new Platform((int)p5.x, (int)p5.y, p5.width, p5.height, p5.xMoveDist,
				p5.yMoveDist, p5.xSpeed, p5.ySpeed, p5.isSolid, p5.isLiquid, p5.type);
		
		//Add to the game
		currentMap.platforms.add(p1);
		currentMap.platforms.add(p2);
		currentMap.platforms.add(p3);
		currentMap.platforms.add(p4);
		currentMap.platforms.add(p5);
		unmodifiedMap.platforms.add(x1);
		unmodifiedMap.platforms.add(x2);
		unmodifiedMap.platforms.add(x3);
		unmodifiedMap.platforms.add(x4);
		unmodifiedMap.platforms.add(x5);
		
		Entity temp = new Entity(0, 600, RunGame.HEIGHT - 30, false);
		Entity temp2 = new Entity(0, 600, RunGame.HEIGHT - 30, false);
		
		//Add entity to the game
		currentMap.entities.add(temp);
		unmodifiedMap.entities.add(temp2);
	}
}
