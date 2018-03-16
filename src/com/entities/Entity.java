package com.entities;

import java.util.ArrayList;

import com.base.Game;
import com.base.RunGame;
import com.structures.Platform;

/**
 * @title Entity
 * @author Alexander Byrd
 * Date Created: February 17, 2018
 * 
 * Description:
 * A creature/enemy/object player can interact with within the game
 * that can move, track the place, collide with other objects and
 * platforms, has a certain amount of health and armor, can die,
 * anything basically the player can do, this can do, but this can
 * be friendly towards the player or not.
 *
 */
public class Entity 
{
	public int health;
	public int armor;
	public int ID;
	public int shield;
	public double x;
	public double y;
	public double topOfEntity;
	public double damage;
	
   /*
    * Angle from current entity to target gotten through eyesight
    */
	public double upAngle;
	
	//Where the current floor is for the entity
	public double floor;
	
	public int weight;
	public double height;
	public double startHeight;
	public int girth;
	
	/* MOVEMENT VARIABLES ******************************/
	
	public double speed;
	public double startSpeed;
	public double upSpeed;
	public double fallingSpeed;
	public double horizontalMovement;
	public double extraMovementX;
	public double extraMovementY;
	public double maxAirHSpeed; //Max speed entity can be in the air
	public int direction = 1; //1 is right, -1 is left
	
	//If entity is targetting another entity instead of the player
	public Entity targetEntity;
	
	/* Possible effects the Entity has on it ******/
	public int poisoned;
	public int invincible;
	public int invisible;
	public int transparent;
	public int onFire;
	public int frozen;
	public int slowed;
	public int spedUp;
	public int lowGravity;
	public int shootTimer;
	public int shotDelay; //How long a shot/melee should be delayed until the next
	
	/* Entity flags ************************************/
	public boolean isAlive = true;
	public boolean inAir;
	public boolean jumping;
	public boolean swimming;
	public boolean crouching;
	public boolean running;
	public boolean isStuck;
	public boolean isFriendly;
	public boolean canFly;
	public boolean notSolid; //IF it can move through things
	public boolean targetOnTop; //If target on top of entity
	public boolean canMove;
	public boolean canShoot;
	public boolean canMelee;
	public boolean multiDirectShooting;
	public boolean canChangeDirect;
	public boolean isMeleeing;
	public boolean isShooting;
	
	public Platform platformOn; //Platform entity is on
	public double projTargetY;
	public double projTargetX;
	public double distanceFromTarget;
	
	//Constructs the entity
	public Entity(int ID, double x, double y, boolean isFriendly) 
	{
		this.ID = ID;
		this.x = x;
		this.y = y;
		this.isFriendly = isFriendly;
		
		if(ID == 0)
		{
			health = 50;
			armor = 100;
			shield = 50;
			weight = 1;
			height = 75;
			girth = 45;
			damage = 10;
			speed = 0.005;
			shotDelay = 10000;
		}
		
		//x = 700;
		//y = RunGame.HEIGHT - 30;
		upSpeed = 0;
		horizontalMovement = 0;
		
		topOfEntity = (int)y - height;
		floor = RunGame.HEIGHT - 30;
		//public Inventory inventory = null;
		
		/* Possible effects the entity has on him/her ******/
		poisoned = 0;
		invincible = 0;
		invisible = 0;
		transparent = 0;
		onFire = 0;
		frozen = 0;
		slowed = 0;
		spedUp = 0;
		lowGravity = 0;
		shootTimer = 0;
		
		/* Entity flags ************************************/
		isAlive = true;
		jumping = false;
		swimming = false;
		crouching = false;
		running = false;
		canFly = false;
		notSolid = false;
		canMove = true;
		canShoot = true;
		canMelee = true;
		multiDirectShooting = true;
		canChangeDirect = true;
		
		startHeight = height;
		startSpeed = speed;
		
		//If entity is friendly, it don't hurt you
		if(isFriendly)
		{
			damage = 0;
			canShoot = false;
			canMelee = false;
		}
	}
	
   /**
    * Updates entity values such as movement, attack phase, graphics, etc..
    */
	public void updateEntity()
	{
		//Always update the top of entity value so the graphical value doesn't glitch out.
		topOfEntity = y - height;
		
		//By default the entity is not crouching
		crouching = false;
		
		//Also by default entity is not running
		running = false;
		
		//Distance checking variables
		double entityX = x;
		double playerX = Player.x;
		double entityY = y;
		double playerY = Player.y;
		
		double leastX = Math.abs(Player.x - x);
		double leastY = Math.abs(Player.y - y);
		
		//Switch distance checking variables depending on what side of
		//the entity the player is on
		if(Math.abs(Player.x - (x + girth)) < leastX)
		{
			leastX = Math.abs(Player.x - (x + girth));
			entityX = x + girth;
			playerX = Player.x;
		}
		
		if(Math.abs((Player.x + Player.girth) - (x + girth)) < leastX)
		{
			leastX = Math.abs((Player.x + girth) - (x + girth));
			entityX = x + girth;
			playerX = Player.x + Player.girth;
		}
		
		if(Math.abs((Player.x + Player.girth) - (x)) < leastX)
		{
			leastX = Math.abs((Player.x + girth) - (x));
			entityX = x;
			playerX = Player.x + Player.girth;
		}
		
		
		
		//Switch distance checking variables depending on whether
		//player is above or below entity
		if(Math.abs(Player.y - (y - height)) < leastY)
		{
			leastY = Math.abs(Player.y - (y - height));
			entityY = y - height;
			playerY = Player.y;
		}
		
		if(Math.abs((Player.y - Player.height) - (y - height)) < leastY)
		{
			leastY = Math.abs((Player.y - Player.height) - (y - height));
			entityY = y - height;
			playerY = Player.y - Player.height;
		}
		
		if(Math.abs((Player.y - Player.height) - (y)) < leastY)
		{
			leastY = Math.abs((Player.y - Player.height) - (y));
			entityY = y;
			playerY = Player.y - Player.height;
		}
		
		//Distance from the target (currently only the player)
		distanceFromTarget = Math.sqrt(((Math.abs(entityX - playerX))*(Math.abs(entityX - playerX))) 
				+ ((Math.abs(entityY - playerY))*(Math.abs(entityY - playerY))));
		
	   /*
	    * At least for now, have entity face the direction the player is
	    * in. Only if the entity can change direction too (Turrets and
	    * such cannot change direction. They are static)
	    */
		if(canChangeDirect)
		{
			if(x < Player.x)
			{
				direction = 1;
			}
			else
			{
				direction = -1;
			}
		}
		
	   /*
	    * Player and the entity are alive
	    */
		if(Player.isAlive && isAlive)
		{
			//If player is above entity, and the entity is not already
			//in the air and player is not standing on top of the entity
			if(Player.y < topOfEntity && y == floor && !targetOnTop)
			{
				jumping = true;
				upSpeed = 0.04;
			}
			
			//If the entity can shoot/hit target
			if(shootTimer == 0 && checkEyeSight())
			{
				shootTimer++;
				
				//If the target can shoot, and the target is either too far away
				//to hit, or the entity cannot melee
				if((distanceFromTarget > 20 || !canMelee) && canShoot)
				{
					double sourceX = x;
					
					//Depending on direction, shoot off certain side of entity
					if(direction == 1)
					{
						sourceX = x + girth;
					}
					
					//TODO yeah

					//If target is negligibly above the entity,
					//then just shoot directly up.
					if((x <= Player.x + Player.girth && x + girth > Player.x))
					{
						upAngle = 0;
						
						//Figure out where the player is within the entities x
						//bounds and shoot up at that location to hit the player
						if(Player.x > x)
						{
							sourceX = Player.x + 2;
						}
						else if(Player.x + Player.girth < x + girth)
						{
							sourceX = Player.x + Player.girth;
						}
						else
						{
							sourceX = Player.x + (Player.girth / 2);
						}
					}
					
				   /*
				    * If target can shoot in multiple directions, keep the upAngle
				    * the same, but if not, set it to be completely horizontal, 
				    * which in this case is Math.PI / 2 
				    */
					if(multiDirectShooting)
					{
						new Projectile(sourceX, y - ((height * 7) / 8), 0.02, 10, direction, 0,
								this, upAngle, projTargetY);
					}
					else
					{
						new Projectile(sourceX, y - ((height * 7) / 8), 0.02, 10, direction, 0,
								this, Math.PI / 2, projTargetY);
					}
					
					isShooting = true;
				}
				else
				{
					//If can do melee damage
					if(canMelee && distanceFromTarget <= 20)
					{
						isMeleeing = true;
						//Do melee damage
						Player.hurt(damage);
					}
				}
			}
			else
			{
			   /*
			    * Only crouch of the entity cannot see the player and 
			    * the player is below the entity and the entity is supposed
			    * to check its eyesight now.
			    */
				if(shootTimer == 0 && ((!checkEyeSight()) && Player.y >= topOfEntity) 
						&& !targetOnTop)
				{
					crouching = true;
				}
			}
		}
		
		//Reset shootTimer if needed, otherwise just keep counting the delay
		if(shootTimer >= shotDelay)
		{
			shootTimer = 0;
		}
		else
		{
		   /*
		    * If halfway through the delay, then set isMeleeing and isShooting to 
		    * false so that the shooting texture won't always display, but turn
		    * on and off for each shot/melee hit
		    */
			if(shootTimer == (int)(shotDelay / 2))
			{
				isMeleeing = false;
				isShooting = false;
			}
			
			shootTimer++;
		}
		
		//Checks the entities position and updates the floor values and such
		//based on it.
		updateCollision(0,0);
		
		//If target is on top of entity, move the entity in the forward
		//direction (really could be any though) to edge the target off
		//to continue shooting the player.
		if(targetOnTop)
		{
			direction = 1;
			
			//Make it run to catch the player off gaurd too
			running = true;
		}
		
		//Updates speed values if entity is running
		if(running)
		{
			speed = 2 * startSpeed; 
		}
		else
		{
			speed = startSpeed;
		}

		//Change in x depends on direction and speed of entity
		double xa = direction * speed;
		
		//If player is dead (at least for now) stop moving, or if the
		//entity cannot move
		if(!Player.isAlive || !canMove || !isAlive)
		{
			xa = 0;
		}
		else
		{
		   /*
		    * If in the air, update the x position with xa seperately
		    * as any prior horizontal movement will already be in 
		    * motion and is harder to stop in the air. Otherwise just
		    * set the horizontal movement equal to the change in x.
		    */
			if(inAir)
			{
				xa /= 3;
				
				//If player can move in this direction
				if(checkCollision(xa, 0, null))
				{		
					x += xa;
				}
			}
			else
			{
				horizontalMovement = xa;
			}
			
			//If can move in this direction and entity is not jumping
			if(checkCollision(extraMovementX, 0, null) && !jumping)
			{		
				x += extraMovementX;
			}
			
			//If can move in this direction
			if(checkCollision(0, extraMovementY, null))
			{		
				y += extraMovementY;
			}
			
			//TODO fix horizontal crap with jumping
			
			//If can move in this direction
			if(checkCollision(horizontalMovement, 0, null))
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
			
			//If alive, it can jump and crouch
			if(isAlive)
			{
				//If jumping
				if(jumping)
				{	
					//Move entity up but decrease speed each time by gravity
					//But the entity has to be able to move upward to do this
					if(checkCollision(0, -upSpeed, null)
							&& !targetOnTop)
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
					int newHeight = (int)((5 * startHeight) / 8);
					
					//Slowly lower yourself if crouching until you reach the 
					//desired height. Also correct graphics for such
					if(height > newHeight)
					{
						height -= crouchAmount;
						topOfEntity += crouchAmount;
					}
					else
					{
						height = newHeight;
						topOfEntity = (int)y - height;
					}
					
					//If in the air, crouching speeds the entity up, otherwise
					//it slows the entity down
					if(inAir)
					{
					   /*
					    * Only do once per jump, but if in the air and you
					    * crouch, your speed will be increased once and
					    * only once so it doesn't keep compounding on itself.
					    */
						if(maxAirHSpeed == 0)
						{
							//TODO FIXX
							maxAirHSpeed = startSpeed * direction * 1.25;
							horizontalMovement = maxAirHSpeed;
						}
						
						speed = speed * 1.25;
					}
					else
					{
						speed = startSpeed / 2;
					}
				}
				else
				{
					maxAirHSpeed = 0;
					
					//Only rise up if not stuck under a platform
					if(!isStuck)
					{
						//Slowly rise up to default height if not crouching
						if(height < startHeight)
						{
							height += crouchAmount;
							topOfEntity -= crouchAmount;
							
							//If moving up got the entity stuck in a platform, move
							//the entity back down
							if(!checkCollision(0, 0, null))
							{
								height -= crouchAmount;
								topOfEntity += crouchAmount;
							}
						}
						else
						{
							height = startHeight;
							topOfEntity = (int)y - startHeight;
						}
					}
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
		
		//If on the ground, there is no longer a falling speed for the entity
		//because the entity is no longer falling
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
	public boolean updateCollision(double xa, double ya)
	{
		//By default you can move
		boolean canMove = true;
		
		//The new X value
		double newX = (x + xa);
		
		//The new Y value
		double newY = (y + ya);
		
		//Set floor back to the bottom of the level each time by default
		floor = RunGame.HEIGHT - 30;
		
		//Reset whether player is on a platform or not
		platformOn = null;
		
		//If player hits the ceiling here
		if(newY - height <= 0)
		{
			jumping = false;
			canMove = false;
		}
		
		//If player hits bottom of the screen
		if(newY + 30 >= RunGame.HEIGHT)
		{
			fallingSpeed = 0;
			inAir = false;
			floor = RunGame.HEIGHT - 30;
			extraMovementX = 0;
			extraMovementY = 0;
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
				extraMovementY += e.upSpeed;
				
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
				
				//TODO add entity crushing player if weight is above certain limit
				
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
						extraMovementY += e.upSpeed;
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
			   /*
			    * Add to extraMovement each time, because if there is a negative
			    * movement and a positive movement(assuming player isn't being 
			    * crushed either) then the forces would cancel out.
			    */
				extraMovementX += pf.xSpeed;
				extraMovementY += pf.ySpeed;
				
				//If block moves up and down, make sure to keep
				//setting the players position to the top of the
				//block if the player is on it
				if(y - height < pf.y && pf.ySpeed != 0
						&& y < pf.y + pf.height)
				{
					y = pf.y;
				}
				
				//Update where the floor is based on the players position each tick as well.
				if(y <= pf.y && floor >= pf.y + pf.height)
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
					//Then say the player is not on a platform and reset the floor value.
					if(floor >= RunGame.HEIGHT - 30)
					{
						floor = RunGame.HEIGHT - 30;
						platformOn = null;
					}
				}
				
				//If block is crushing player vertically
				if(!checkCollision(0, pf.ySpeed, pf))
				{
					isStuck = true;
					
					//If platform is moving down to crush the player
					if(pf.ySpeed > 0)
					{
						height = (startHeight - Math.abs((pf.y + pf.height)
								- (y - startHeight))) + 0.5;
						topOfEntity = (y - height);
					}
					//If platform is moving up and crushing the player into something
					else if(pf.ySpeed < 0)
					{
						//Decrease height steadily instead of all at once
						height -= ((y - Math.abs((pf.y + pf.ySpeed))));
						y = topOfEntity + height;
						topOfEntity = y - height;
					}
					
					newY = y + ya;
					
					//If entity is crushed (below crouch height)
					if(height < (int)((5 * startHeight) / 8) + 0.5)
					{
						health = 0;
						isAlive = false;
					}
				}
				
			   /*
			    * Checks if entity can move forward (while ignoring the block
			    * currently touching the entity and moving it) and also checks
			    * to make sure the entity can't move backwards as well so that the entity
			    * is for sure being crushed. Also the xSpeed of the platform that is pushing
			    * the entity in this check is not 0, because then it shouldn't be crushing
			    * the entity.
			    */
				if(!checkCollision(pf.xSpeed, 0, pf)
						&& !checkCollision(-pf.xSpeed, 0, null)
						&& pf.xSpeed != 0.0)
				{
					isStuck = true;
					
					//Player can crouch to avoid being crushed vertically,
					//but there ain't nothing you can do horizontally
					health = 0;
					isAlive = false;
				}
				
				canMove = false;
			}
			else
			{
				//Update where the floor is based on the players position each tick as well.
				if(y <= pf.y && floor >= pf.y && 
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
						extraMovementX += pf.xSpeed;
						extraMovementY += pf.ySpeed;
					}
				}
			}
		}
		
		//If Entity is above the player, set the floor to being the players
		//head
		if((newX <= Player.x + Player.girth && newX + girth > Player.x)
				&& newY <= Player.y - Player.height)
		{
			floor = Player.y - Player.height;
		}
		
		//Don't allow entity to move into the player. Unless the player is above
		//the entity
		if(((newX <= Player.x + Player.girth && newX + girth > Player.x)) 
					&& ((newY > Player.y - Player.height && newY - height <= Player.y)))
		{
			if(Player.floor == y - height)
			{
				targetOnTop = true;
			}
			else
			{
				targetOnTop = false;
			}
			
			//TODO change in the future for all targets
			
			//If player is not on top of the entity, the entity can not move into
			//the player
			if(!targetOnTop)
			{
				canMove = false;
			}
		}
		else
		{
			//If target isn't touching player anyway, then the target is
			//not on top of it, so reset it.
			targetOnTop = false;
		}
		
		//Defaultly, if no other floor height is set, set it to the level floor
		if(floor >= RunGame.HEIGHT - 30)
		{
			floor = RunGame.HEIGHT - 30;
			
			//If it gets here, the player is not on a platform
			platformOn = null;
		}
		
		return canMove;
	}
	
   /**
    * Check to make sure the player can move in any direction and not.
    * It does not do what updateCollision does and update movement 
    * variables and such. It only updates whether the player can move
    * in a given direction based on the movement variables already calculated
    * beforehand.
    * 
    * @param xa, ya, exclude
    * @return
    */
	public boolean checkCollision(double xa, double ya, Platform exclude)
	{
		//The new X value
		double newX = (x + xa);
		
		//The new Y value
		double newY = (y + ya);
		
		//If player hits the ceiling here
		if(newY - height < 0)
		{
			return false;
		}
		
		//If player hits bottom of the screen
		if(newY + 30 > RunGame.HEIGHT)
		{
			return false;
		}
		
	   /*
	    * Make sure player cannot go through an entity in the game either
	    */
		for(Entity e: Game.entities)
		{
			if(!e.equals(this) && ((newX <= e.x + e.girth && newX + girth > e.x + 1)) 
					&& ((newY > e.y - e.height && newY - height <= e.y)))
			{			
				return false;
			}
		}

		//Check all platforms to see if the player is inside of them
		for(Platform pf: Game.platforms)
		{
			if(((newX <= pf.x + pf.width && newX + girth > pf.x + 1)) 
					&& ((newY > pf.y && newY - height <= pf.y + pf.height)))
			{	
				if(exclude == null || (exclude != null && !pf.equals(exclude)))
				{
					return false;
				}
			}
		}
		
		//Do not allow the entity to move into the player either
		if(((newX <= Player.x + Player.girth && newX + girth > Player.x)) 
				&& ((newY > Player.y - Player.height && newY - height <= Player.y))
				&& !targetOnTop)
		{
			return false;
		}
		
		return true;
	}
	
   /**
    * Hurt the entity with a variable amount of damage. If the
    * entities health goes below 0, then the enemy dies, and 
    * the game should get rid of it.
    * @param damage
    */
	public void hurt(double damage)
	{
		health -= damage;
		
		//Remove the entity if dead
		if(health <= 0)
		{
			isAlive = false;
			Game.entities.remove(this);
		}
	}
	
   /**
    * Checks the horizontal plane to see if the target is in the sightline
    * of the entity or not. If not,then have the entity crouch to try and get
    * sight of the player.
    * @return
    */
	public boolean checkEyeSight()
	{
		ArrayList<Point> pointsToCheck = new ArrayList<Point>();
		
		double targetX = Player.x;
		double targetY = Player.y;
		double targetWidth = Player.girth;
		double targetHeight = Player.height;
		
		//If there is a target besides the player
		if(targetEntity != null)
		{
			targetX = targetEntity.x;
			targetY = targetEntity.y;
			targetWidth = targetEntity.girth;
			targetHeight = targetEntity.height;
		}
		
		//Target Values
		Point bottomRight = new Point(targetX + targetWidth, targetY);
		Point bottomLeft = new Point(targetX, targetY);
		Point topLeft = new Point(targetX, targetY - targetHeight);
		Point topRight = new Point(targetX + targetWidth, targetY - targetHeight);
		
		double yCheck = topLeft.y;
		double xCheck = topLeft.x;
		
		//Depending on the entities position in relation to the target, only
		//add the points that the entity even has a chance of seeing
		if(targetX >= x)
		{
			while(yCheck <= bottomLeft.y)
			{
				pointsToCheck.add(new Point(xCheck, yCheck));
				yCheck += 15;
			}
		}
		else
		{
			yCheck = topRight.y;
			xCheck = topRight.x;
			
			//Points down right side of player added
			while(yCheck <= bottomRight.y)
			{
				pointsToCheck.add(new Point(xCheck, yCheck));
				yCheck += 15;
			}
		}
		
		//Check up and down directions too
		if(targetY < y)
		{
			yCheck = bottomLeft.y;
			xCheck = bottomLeft.x;
			
			//Points all on bottom of player added
			while(xCheck <= bottomRight.x)
			{
				pointsToCheck.add(new Point(xCheck, yCheck));
				xCheck += 15;
			}
		}
		else
		{
			yCheck = topLeft.y;
			xCheck = topLeft.x;
			
			//Points all on top of player added
			while(xCheck <= topRight.x)
			{
				pointsToCheck.add(new Point(xCheck, yCheck));
				xCheck += 15;
			}
		}
		
		//Check all major points on player for eyesight comformation
		for(Point checkPoint: pointsToCheck)
		{
			//If a point on the target can be seen
			if(checks(checkPoint, targetWidth, targetHeight))
			{
				//Tell projectiles where the target is too
				projTargetY = checkPoint.y;
				projTargetX = checkPoint.x;
				return true;
			}
		}
		
		return false;
	}
	
   /**
    * Checks each individual target point to see if the eyesight hits it 
    * or not.
    * @param pointOnTarget
    * @param targetWidth
    * @param targetHeight
    * @return
    */
	private boolean checks(Point pointOnTarget, double targetWidth, double targetHeight)
	{
		//Where the eyesight vector is currently at in the horizontal plane
		double eyeX = x;
		double eyeY = y - ((height * 7) / 8);
		double eyeSpeed = 3;
		
		//If facing to the right, eyeX starts at a different location
		if(direction == 1)
		{
			eyeX = (int)(x + girth);
		}
		
		//Distance from target in the X direction
		double distanceX = Math.abs(pointOnTarget.x - eyeX);
		
		//Distance from target in the Y direction
		double distanceY = Math.abs(pointOnTarget.y - eyeY);
		
		//How fast in what direction the eyesight should move
		double xMove = eyeSpeed * direction;
		
		//If target is closer than 1 pixel away
		if(distanceX < eyeSpeed)
		{
			xMove = distanceX * direction;
		}
		
		//How fast the projectile should move up given the current x velocity
		double yMove = 0;
		
		//If the target is not directly above or below the entity
		if(distanceX != 0)
		{
			upAngle = Math.atan((distanceX)/(distanceY));
			
			//How fast the projectile should move up given the current x velocity
			yMove = Math.abs(xMove) / Math.tan(upAngle);
			
		   /*
		    * Fixes speed issues when Math.tan(upAngle) is less than 1 making
		    * ya greater than the speed that the projectile should be.
		    */
			if(Math.abs(yMove) > eyeSpeed)
			{
				//Figures out how many times the speed goes into the calculated
				//speed in the y direction. Then divides the change in x by that
				//much so that ya can just be set to speed, and the angle still be
				//the same.
				xMove /= yMove;

				yMove = eyeSpeed;
			}
			
			//Move the eyesight upward on screen if the player is above the entity
			if(pointOnTarget.y < eyeY)
			{
				yMove = -yMove;
			}
		}
		else
		{
			//Default y movement is downward on the screen
			//if there is no x movement
			yMove = eyeSpeed;
			
			upAngle = 0;
			
			//If the player is less than one pixel below the
			//entity, then set yMovement to just that distance
			if(distanceY < eyeSpeed)
			{
				yMove = distanceY;
			}
			
			//If target is above the entity, then switch the yMove
			//direction
			if(pointOnTarget.y < eyeY)
			{
				yMove = -yMove;
			}
		}
		
		//Check until it returns false
		while(true)
		{
			//Check all platforms to see if the eyesight hits one of them
			//meaning it reaches the inside of the platforms border
			for(Platform pf: Game.platforms)
			{
				if(((eyeX <= pf.x + pf.width && eyeX > pf.x + 1))
						&& ((eyeY >= pf.y && eyeY <= pf.y + pf.height)))
				{
					return false;
				}
			}
			
			//If eyesight has hit the target
			if(((eyeX <= pointOnTarget.x + targetWidth && eyeX + girth > pointOnTarget.x)) 
					&& ((eyeY <= pointOnTarget.y && eyeY >= pointOnTarget.y - targetHeight)))
			{
				return true;
			}
			
		   /*
		    * If the eyesight has passed the side of the target it was
		    * heading towards, then it obviously has missed the target,
		    * and therefore return false in terms of seeing the target.
		    */
			if(direction == 1)
			{
				if(eyeX > pointOnTarget.x + targetWidth)
				{
					return false;
				}
			}
			else
			{
				if(eyeX < pointOnTarget.x)
				{
					return false;
				}
			}
			
			eyeX += xMove;
			eyeY += yMove;
		}
	}
}
