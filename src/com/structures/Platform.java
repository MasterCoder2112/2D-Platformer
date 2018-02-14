package com.structures;

import com.base.Game;

/**
 * @title Platform
 * @author Alexander Byrd
 * Date Created: February 13, 2018
 * 
 * Description:
 * A platform (can be horizontal or vertical) which can move, by doesn't
 * have to. Can be solid or not (Secret areas may not be, or if liquids
 * are added at a later date). Also can have variable speed.
 */
public class Platform 
{
	//Movement variables
	public int startX;
	public int endX;
	public int startY;
	public int endY;
	public double xSpeed;
	public double ySpeed;
	public int xDirection;
	public int yDirection;
	
	//Position variables
	public double x;
	public double y;
	public int width;
	public int height;
	
	//FLAGS
	public boolean isSolid;
	public boolean isLiquid;
	public boolean isMoving;
	
	//Possible future variables
	public int structureType; //Shape or form of the platform. Could just be an extension of this class
	public int gravity; //Platform could pull you in
	public int slickness; //Could be used for ice or other type platforms
	public int stickiness; //Block could slow you down or make it harder for certain movements
	
	//Initialize a platform given all the variables
	public Platform(int x, int y, int width, int height, int xMoveDist, int yMoveDist, double xSpeed, double ySpeed,
			boolean isSolid, boolean isLiquid) 
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.startX = x;
		this.endX = startX + xMoveDist;
		this.startY = y;
		this.endY = y + yMoveDist;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.isSolid = isSolid;
		this.isLiquid = isLiquid;
		
		//If the platform is supposed to move
		if(startX != endX || startY != endY)
		{
			isMoving = true;
		}
		
		//Add to the game
		Game.platforms.add(this);
	}
	
	//Minimal variables needed for a non-moving default platform
	public Platform(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		isSolid = true;
		isLiquid = false;
		this.startX = x;
		this.endX = x;
		this.startY = y;
		this.endY = y;
		this.xSpeed = 0;
		this.ySpeed = 0;
		
		//Add to the game
		Game.platforms.add(this);
	}
	
   /**
    * Updates movement variables of a platform, probably more 
    * effects in the future as well
    */
	public void updatePlatform()
	{
		//For right now, the only variables to update are movement variables.
		if(isMoving)
		{
		   /*
		    * If the platform can keep moving in a given x direction, keep moving
		    * it that direction. Once it reaches the last movement, it sets the 
		    * position to the destination x and change the direction.
		    */
			if(xDirection == 0)
			{
				if(x + xSpeed < endX)
				{
					x += xSpeed;
				}
				else
				{
					x = endX;
					xDirection = 1;
				}
			}
			else
			{
				if(x - xSpeed > startX)
				{
					x -= xSpeed;
				}
				else
				{
					x = startX;
					xDirection = 0;
				}
			}
			
			//Same thing but in the y direction
			if(yDirection == 0)
			{
				if(y + ySpeed < endY)
				{
					y += ySpeed;
				}
				else
				{
					y = endY;
					yDirection = 1;
				}
			}
			else
			{
				if(y - ySpeed > startY)
				{
					y -= ySpeed;
				}
				else
				{
					y = startY;
					yDirection = 0;
				}
			}
		}
	}
}
