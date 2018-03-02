package com.entities;

import com.base.Game;
import com.base.RunGame;
import com.structures.Platform;

/**
 * @title Projectile
 * @author Alexander Byrd
 * Date Created: February 15, 2018
 * 
 * Description:
 * A Projectile object, that will move with a given speed in a given
 * direction (based on the direction the entity that shot it was facing)
 * and will deal a given amount of damage upon hitting another entity on the screen.
 *
 */
public class Projectile 
{
	public int direction;
	public double damage;
	public double speed;
	public int ID;
	public int width = 15;
	public int height = 15;
	public double y;
	public double x;
	public double ya;
	public double xa;
	public boolean toBeDeleted;
	
	//Entity who shot it, null if from a player
	public Entity source;
	
	public Projectile(double x, double y, double speed, double damage, int direction,
			int ID, Entity src, double upAngle, double targetY) 
	{
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.damage = damage;
		this.direction = direction;
		this.ID = ID;
		source = src;
		
		//Set the y movement variable based on the upAngle sent in
		//and the current x movement of the projectile
		this.ya = speed / Math.tan(upAngle);
		
		//Set the x movement variable for the projectile
		this.xa = direction * (Math.tan(upAngle) * this.ya);
		
	   /*
	    * Fixes speed issues when Math.tan(upAngle) is less than 1 making
	    * ya greater than the speed that the projectile should be.
	    */
		if(Math.abs(this.ya) > speed)
		{
			//Figures out how many times the speed goes into the calculated
			//speed in the y direction. Then divides the change in x by that
			//much so that ya can just be set to speed, and the angle still be
			//the same.
			double temp = this.ya / speed;
			this.xa /= temp;

			this.ya = speed;
		}

		//If player is directly above, then shoot straight up at a
		//constant speed
		if(Math.tan(upAngle) == 0)
		{
			this.ya = speed;
			this.xa = 0;
		}

	   /*
	    * Change the direction if the target is below this or
	    * to the right of it, because the upAngle makes this act
	    * weird otherwise and go the opposite direction that it
	    * should... Only do if entity shoots this projectile.
	    */
		if((targetY < this.y) && src != null)
		{
			ya = -ya;
		}
		
		//Add to game
		Game.projectiles.add(this);
	}
	
   /**
    * Moves the projectile in the intended direction and speed
    */
	public void move()
	{	
		//If the bullet can move, move it, if not then delete it
		if(checkCollision(xa, ya))
		{
			x += xa;
			y += ya;
		}
		else
		{
			toBeDeleted = true;
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
		//The new X value
		double newX = (x + xa);
		
		//The new Y value
		double newY = (y + height + ya);
		
		//If player hits the ceiling here
		if(newY - height < 0 || newY + 30 > RunGame.HEIGHT)
		{
			return false;
		}
		
		//If it goes out of the screen horizontally
		if(newX > RunGame.WIDTH || newX < 0)
		{
			return false;
		}
		
	   /*
	    * Scan for all the entities, see if this is within range of them
	    * and then hit the enemy if within range. Also cannot hit the
	    * source enemy of the shot
	    */
		for(Entity e: Game.entities)
		{
			if(((newX <= e.x + e.girth && newX + width > e.x + 1)) 
					&& ((newY > e.y - e.height && newY - height <= e.y))
					&& !e.equals(source))
			{		
				e.hurt(damage);
				return false;
			}
		}
		
		//Check all platforms to see if the player is inside of them
		for(Platform pf: Game.platforms)
		{
			if(((newX <= pf.x + pf.width && newX + width > pf.x + 1)) 
					&& ((newY > pf.y && newY - height <= pf.y + pf.height)))
			{		
				return false;
			}
		}
		
		//If the player didn't shoot the projectile and the projectile
		//hits the player
		if(((newX <= Player.x + Player.girth && newX + width > Player.x + 1)) 
					&& ((newY > Player.y - Player.height && newY - height <= Player.y))
					&& source != null)
		{
			Player.hurt(damage);
			return false;
		}
		
		return true;
	}

}
