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
	public boolean toBeDeleted;
	
	public Projectile(double x, double y, double speed, double damage, int direction, int ID) 
	{
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.damage = damage;
		this.direction = direction;
		this.ID = ID;
		
		//Add to game
		Game.projectiles.add(this);
	}
	
   /**
    * Moves the projectile in the intended direction and speed
    */
	public void move()
	{
		double xa = speed * direction;
		
		//If the bullet can move, move it, if not then delete it
		if(checkCollision(xa, 0))
		{
			x += xa;
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
		
		//TODO Collision
		//Check all platforms to see if the player is inside of them
		for(Platform pf: Game.platforms)
		{
			if(((newX <= pf.x + pf.width && newX + width > pf.x + 1)) 
					&& ((newY > pf.y && newY - height <= pf.y + pf.height)))
			{							
				return false;
			}
		}
		
		return true;
	}

}
