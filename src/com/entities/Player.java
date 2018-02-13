package com.entities;

import com.base.Game;
import com.base.RunGame;

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
	public static final double DEFAULT_SPEED = 0.01;
	public static final double DEFAULT_JUMP_SPEED = 0.04;
	public static final int DEFAULT_HEIGHT = 70;
	
	//Things that the player may or may not have in the future
	public int health;
	public int armor;
	public int ammo;
	public int shield;
	public double x;
	public double y;
	
	//Where the current floor for the player is
	public double floor;
	
	//public Weapon[] weapons = null;
	public int weight;
	public double height;
	public int girth;
	
	/* MOVEMENT VARIABLES ******************************/
	
	public double speed;
	public double upSpeed;
	public double fallingSpeed;
	public double horizontalMovement;
	public double maxAirHSpeed; //Max speed you can be in the air
	
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
	public boolean isAlive = true;
	public boolean inAir = false;
	public boolean firing = false;
	public boolean jumping = false;
	public boolean swimming = false;
	public boolean crouching = false;
	public boolean running = false;
	
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
		x = 100;
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
			x += xa;
		}
		else
		{
			horizontalMovement = xa;
		}
		
		x += horizontalMovement;
		
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
	}
	
   /**
    * Updates any effects or stats the player has if possible,
    * this includes jumping and crouching too as those are
    * special movement values.
    */
	public void updateValues()
	{
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
			y -= upSpeed;
			upSpeed -= Game.GRAVITY;
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
			//Slowly rise up to default height if not crouching
			if(height < DEFAULT_HEIGHT)
			{
				height += crouchAmount;
				topOfPlayer -= crouchAmount;
			}
			else
			{
				height = DEFAULT_HEIGHT;
				topOfPlayer = (int)y - DEFAULT_HEIGHT;
			}
		}
		
		//TODO change floor stuff in future
		floor = RunGame.HEIGHT - 30;

		//If in the air
		if(y < floor)
		{
			inAir = true;
			
			//If not jumping (therefore falling)
			if(!jumping)
			{
				y -= fallingSpeed;
				fallingSpeed -= fallingSpeed - Game.GRAVITY;
			}
		}
		
		//If player is below the floor then quickly reset his/her position
		if(!checkLower())
		{
			y = floor;
		}
		
		//If player is above ceiling quickly reset his/her position and get
		//them out of jumping if they are
		if(!checkUpper())
		{
			y = height;
			jumping = false;
			upSpeed = 0;
		}
		
		//If on the ground, there is no longer a falling speed for the player
		//because the player is no longer falling
		if(y == floor)
		{
			fallingSpeed = 0;
			inAir = false;
		}
		
		//If on the ground, then update horizontal movement to be 0
		if(!inAir)
		{
			horizontalMovement = 0;
		}
	}
	
   /**
    * Makes sure player doesn't go above the screen
    * @param ya
    * @return
    */
	public boolean checkUpper()
	{
		//As long as not going out of the top of the screen
		if(y - height <= 0)
		{
			return false;
		}
		
		return true;
	}
	
   /**
    * Makes sure Player is not moving out of the bottom of the screen
    * @return
    */
	public boolean checkLower()
	{
		//If not moving out of bottom of the screen
		if(y + 30 >= RunGame.HEIGHT)
		{
			return false;
		}
		
		return true;
	}

}
