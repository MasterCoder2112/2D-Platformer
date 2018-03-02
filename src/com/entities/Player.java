package com.entities;

import com.base.Game;
import com.entities.Entity;
import com.base.RunGame;
import com.structures.Platform;

/**
 * @title Player
 * @author Alexander Byrd
 * Date Created: February 12, 2018
 * 
 * Description:
 * Tracks all of the stats of the player and updates them as needed.
 *
 */
public class Player 
{
	public static final double DEFAULT_SPEED = 0.015;
	public static final double DEFAULT_JUMP_SPEED = 0.04;
	public static final int DEFAULT_HEIGHT = 75;
	
	public Platform platformOn;
	
	//Things that the player may or may not have in the future
	public static int health;
	public static int armor;
	public static int ammo;
	public static int shield;
	public static double x;
	public static double y;
	
	//Where the current floor for the player is
	public static double floor;
	
	//public Weapon[] weapons = null;
	public static int weight;
	public static double height;
	public static int girth;
	
	/* MOVEMENT VARIABLES ******************************/
	
	public double speed;
	public double upSpeed;
	public double fallingSpeed;
	public double horizontalMovement;
	public double extraMovementX;
	public double extraMovementY;
	public double maxAirHSpeed; //Max speed you can be in the air
	public int direction = 1; //1 is right, -1 is left
	
	//public Inventory inventory = null;
	
	/* Possible effects the player has on him/her ******/
	public int poisoned;
	public int invincible;
	public int invisible;
	public int onFire;
	public int frozen;
	public int slowed;
	public int spedUp;
	public int lowGravity;
	
	/* Player flags ************************************/
	public static boolean isAlive = true;
	public boolean inAir = false;
	public boolean firing = false;
	public boolean jumping = false;
	public boolean swimming = false;
	public boolean crouching = false;
	public boolean running = false;
	public boolean isStuck = false;
	public static boolean godMode = false;
	
	/* GRAPHICAL CRAP *********************************/
	
   /*
    * Because the graphics object considers the top left of the block to
    * be "the y value" this corrects that so that the y value in the program
    * references the bottom of the block instead. This variable is only used
    * to make sure the block looks like it is crouching correctly.
    */
	public double topOfPlayer;
	
	//Initialize values
	public Player() 
	{
		//Things that the player may or may not have in the future
		health = 100;
		armor = 100;
		ammo = 0;
		shield = 0;
		x = 500;
		y = RunGame.HEIGHT - 30;
		//public Weapon[] weapons = null;
		weight = 0;
		height = DEFAULT_HEIGHT;
		girth = 40;
		speed = DEFAULT_SPEED;
		upSpeed = 0;
		horizontalMovement = 0;
		
		topOfPlayer = (int)y - height;
		floor = RunGame.HEIGHT - 30;
		//public Inventory inventory = null;
		
		/* Possible effects the player has on him/her ******/
		poisoned = 0;
		invincible = 0;
		invisible = 0;
		onFire = 0;
		frozen = 0;
		slowed = 0;
		spedUp = 0;
		lowGravity = 0;
		
		/* Player flags ************************************/
		isAlive = true;
		firing = false;
		jumping = false;
		swimming = false;
		crouching = false;
		running = false;
	}
	
   /**
    * Updates players movements in the x direction
    */
	public void move(double xa)
	{
	   /*
	    * If in the air, update the x position with xa seperately
	    * as any prior horizontal movement will already be in 
	    * motion and is harder to stop in the air. Otherwise just
	    * set the horizontal movement equal to the change in x.
	    */
		if(inAir)
		{
			//If player can move in this direction
			if(checkCollision(xa, 0))
			{		
				x += xa;
			}
		}
		else
		{
			horizontalMovement = xa;
		}
		
		//If can move in this direction and player is not jumping
		if(checkCollision(extraMovementX, 0) && !jumping)
		{		
			x += extraMovementX;
		}
		
		//If can move in this direction
		if(checkCollision(0, extraMovementY))
		{		
			y += extraMovementY;
		}
		
		//If can move in this direction
		if(checkCollision(horizontalMovement, 0))
		{		
			x += horizontalMovement;
		}
		
		//If player reaches left edge of screen, loop him/her around
		if(x + girth < 0)
		{
			x = RunGame.WIDTH - 10;
		}
		
		//Loop player around if he/she reaches the edge of the screen
		if(x > RunGame.WIDTH)
		{
			x = -girth + 10;
		}
		
		updateValues();
	}
	
   /**
    * Updates any effects or stats the player has if possible,
    * this includes jumping and crouching too as those are
    * special movement values.
    */
	public void updateValues()
	{
		//Checks the players position and updates the floor values and such
		//based on it.
		checkCollision(0,0);
		
		//Updates speed values if player is running
		if(running)
		{
			speed = 2 * DEFAULT_SPEED; 
		}
		else
		{
			speed = DEFAULT_SPEED;
		}
		
		//If jumping
		if(jumping)
		{		
			//Move player up but decrease speed each time by gravity
			//But the player has to be able to move upward
			if(checkCollision(0, -upSpeed))
			{	
				y -= upSpeed;
				upSpeed -= Game.GRAVITY;
			}
			else
			{
				jumping = false;
				upSpeed = 0;
				fallingSpeed = 0;
			}
		}
		
		double crouchAmount = 0.01;
		
		//If crouching
		if(crouching)
		{
			//Target height if crouching
			int newHeight = (int)((5 *DEFAULT_HEIGHT) / 8);
			
			//Slowly lower yourself if crouching until you reach the 
			//desired height. Also correct graphics for such
			if(height > newHeight)
			{
				height -= crouchAmount;
				topOfPlayer += crouchAmount;
			}
			else
			{
				height = newHeight;
				topOfPlayer = (int)y - height;
			}
			
			//If in the air, crouching speeds you up, otherwise
			//it slows you down
			if(inAir)
			{
			   /*
			    * Only do once per jump, but if in the air and you
			    * crouch, your speed will be increased once and
			    * only once so it doesn't keep compounding on itself.
			    */
				if(maxAirHSpeed == 0)
				{
					maxAirHSpeed = horizontalMovement * 1.25;
					horizontalMovement = maxAirHSpeed;
				}
				
				speed = speed * 1.25;
			}
			else
			{
				speed = DEFAULT_SPEED / 2;
			}
		}
		else
		{
			maxAirHSpeed = 0;
			
			//TODO HERE2
			//Only rise up if not stuck under a platform
			if(!isStuck)
			{
				//System.out.println("HERE");
				//Slowly rise up to default height if not crouching
				if(height < DEFAULT_HEIGHT)
				{
					height += crouchAmount;
					topOfPlayer -= crouchAmount;
					
					//If moving up got the player stuck in a platform, move
					//the player back down
					if(!checkCollision(0,0))
					{
						height -= crouchAmount;
						topOfPlayer += crouchAmount;
					}
				}
				else
				{
					height = DEFAULT_HEIGHT;
					topOfPlayer = (int)y - DEFAULT_HEIGHT;
				}
			}
		}

		//If in the air
		if(y < floor)
		{
			inAir = true;
			
			//If not jumping (therefore falling)
			if(!jumping)
			{
				//Check to see if y can keep falling anyway
				if(y - fallingSpeed < floor)
				{
					y -= fallingSpeed;
					fallingSpeed = fallingSpeed - Game.GRAVITY;
				}
				else
				{
					y = floor;
				}
			}
		}
		
		//If on the ground, there is no longer a falling speed for the player
		//because the player is no longer falling
		if(y >= floor)
		{
			jumping = false;
			fallingSpeed = 0;
			inAir = false;
			y = floor;
			extraMovementY = 0;
			extraMovementX = 0;
		}
		
		//If on the ground, then update horizontal movement to be 0
		if(!inAir)
		{
			horizontalMovement = 0;
		}
	}
	
   /**
    * Check to make sure the player can move in any direction and not
    * 
    * @param xa
    * @return
    */
	public boolean checkCollision(double xa, double ya)
	{
		//By default you can move
		boolean canMove = true;
		
		//Are you still under a block
		boolean underBlock = false;
		
		//The new X value
		double newX = (x + xa);
		
		//The new Y value
		double newY = (y + ya);
		
		//Set floor back to the bottom of the level each time by default
		floor = RunGame.HEIGHT - 30;
		
		//Reset whether player is on a platform or not
		platformOn = null;
		
		//If player hits the ceiling here
		if(newY - height < 0)
		{
			canMove = false;
		}
		
		//If player hits bottom of the screen
		if(newY + 30 > RunGame.HEIGHT)
		{
			canMove = false;
		}
		
	   /*
	    * Make sure player cannot go through an entity in the game either
	    */
		for(Entity e: Game.entities)
		{
			if(((newX <= e.x + e.girth && newX + girth > e.x + 1)) 
					&& ((newY > e.y - e.height && newY - height <= e.y)))
			{
				extraMovementY = e.upSpeed;
				
				//If entity moves up and down, make sure to keep
				//setting the players position to the top of the
				//entity if the player is on it
				if(y - height < e.y - e.height && e.upSpeed != 0
						&& y < e.y)
				{
					y = e.y - e.height;
				}
				
				//Update where the floor is based on the players position each tick as well.
				if(y <= e.y - e.height && floor > e.y - e.height)
				{					
					floor = e.y - e.height;
				}
				else
				{
					//If the floor is below or the same as the levels floor value.
					if(floor >= RunGame.HEIGHT - 30)
					{
						floor = RunGame.HEIGHT - 30;
					}
				}
				
				//If entity is crushing player
				if(y > e.y && topOfPlayer < e.y
						&& topOfPlayer > e.y - e.height && e.upSpeed > 0 && upSpeed == 0 
						&& fallingSpeed == 0 && e.weight > 10)
				{
					underBlock = true;
					isStuck = true;
					height = (DEFAULT_HEIGHT - Math.abs((e.y) - (y - DEFAULT_HEIGHT))) + 0.5;
					topOfPlayer = (y - height);
					
					//If player is crushed (below crouch height)
					if(height < (int)((5 * DEFAULT_HEIGHT) / 8) + 0.5)
					{
						health = 0;
						isAlive = false;
					}
				}
				
				canMove = false;
			}
			else
			{
				//Update where the floor is based on the players position each tick as well.
				if(y <= e.y - e.height && floor > e.y - e.height && 
						(newX <= e.x + e.girth && newX + girth > e.x + 1))
				{	
					floor = e.y - e.height;
					
					//Only if on the top of the entity
					if(Math.abs(floor - y) < 0.05)
					{
						extraMovementY = e.upSpeed;
					}
				}
			}
		}

		//Check all platforms to see if the player is inside of them
		for(Platform pf: Game.platforms)
		{
			if(((newX <= pf.x + pf.width && newX + girth > pf.x + 1)) 
					&& ((newY > pf.y && newY - height <= pf.y + pf.height)))
			{
				//The extra movement added to the player when he is on 
				//a moving platform both horizontally and vertically
				extraMovementX = pf.xSpeed;
				extraMovementY = pf.ySpeed;
				
				//If block moves up and down, make sure to keep
				//setting the players position to the top of the
				//block if the player is on it
				if(y - height < pf.y && pf.ySpeed != 0
						&& y < pf.y + pf.height)
				{
					y = pf.y;
				}
				
				//Update where the floor is based on the players position each tick as well.
				if(y <= pf.y && floor > pf.y + pf.height)
				{
				   /*
				    * If the player is directly over or on this platform, set this platform
				    * as being the one that the player is "on" so that the player can move
				    * with it if it is moving. 
				    */
					platformOn = pf;
					
					floor = pf.y;
				}
				else
				{
					horizontalMovement = extraMovementX;
					
					//If the floor is below or the same as the levels floor value.
					if(floor >= RunGame.HEIGHT - 30)
					{
						floor = RunGame.HEIGHT - 30;
						platformOn = null;
						extraMovementX = 0;
						extraMovementY = 0;
					}
				}
				
				//If block is crushing player
				if(y > pf.y + pf.height && topOfPlayer < pf.y + pf.height
						&& topOfPlayer > pf.y && pf.ySpeed != 0 && upSpeed == 0 && fallingSpeed == 0)
				{
					underBlock = true;
					isStuck = true;
					height = (DEFAULT_HEIGHT - Math.abs((pf.y + pf.height) - (y - DEFAULT_HEIGHT))) + 0.5;
					topOfPlayer = (y - height);
					
					//If player is crushed (below crouch height) and not in godMode
					if(height < (int)((5 * DEFAULT_HEIGHT) / 8) + 0.5
							&& !Player.godMode)
					{
						health = 0;
						isAlive = false;
					}
				}
				
				canMove = false;
			}
			else
			{
				//Update where the floor is based on the players position each tick as well.
				if(y <= pf.y && floor > pf.y + pf.height && 
						(newX <= pf.x + pf.width && newX + girth > pf.x + 1))
				{
				   /*
				    * If the player is directly over or on this platform, set this platform
				    * as being the one that the player is "on" so that the player can move
				    * with it if it is moving. 
				    */
					platformOn = pf;
					
					floor = pf.y;
					
					//Only if on the top of the block
					if(Math.abs(floor - y) < 0.05)
					{
						//The extra movement added to the player when he is on 
						//a moving platform both horizontally and vertically
						extraMovementX = pf.xSpeed;
						extraMovementY = pf.ySpeed;
					}
				}
			}
		}
		
		//If no longer under a block
		if(!underBlock)
		{
			isStuck = false;
		}
		
		//Defaultly, if no other floor height is set, set it to the level floor
		if(floor >= RunGame.HEIGHT - 30)
		{
			floor = RunGame.HEIGHT - 30;
			
			//If it gets here, the player is not on a platform
			platformOn = null;
			
			extraMovementX = 0;
		}
		
		return canMove;
	}
	
   /**
    * Hurt the player with a variable amount of damage, and if the players health goes
    * below 0, then the player is dead.
    * @param damage
    */
	public static void hurt(double damage)
	{
		if(!godMode)
		{
			health -= damage;
			
			if(health <= 0)
			{
				isAlive = false;
			}
		}
	}
}
