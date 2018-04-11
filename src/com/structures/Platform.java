package com.structures;

import java.util.ArrayList;

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
	public int xMoveDist;
	public int yMoveDist;
	public int type;
	
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
	
	//Holds all the units this platform contains
	public ArrayList<Unit> pUnits = new ArrayList<Unit>();
	
	//Initialize a platform given all the variables
	public Platform(int x, int y, int width, int height, int xMoveDist, int yMoveDist, double xSpeed, double ySpeed,
			boolean isSolid, boolean isLiquid, int type) 
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
		this.xMoveDist = xMoveDist;
		this.yMoveDist = yMoveDist;
		this.type = type;
		
		//If the platform is supposed to move
		if(startX != endX || startY != endY)
		{
			isMoving = true;
		}
		
		breakIntoUnits();
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
		Game.currentMap.platforms.add(this);
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
			if(xSpeed > 0)
			{
				if(x + xSpeed < endX)
				{
					x += xSpeed;
				}
				else
				{
					x = endX;
					xSpeed = -xSpeed;
				}
			}
			else
			{
				if(x + xSpeed > startX)
				{
					x += xSpeed;
				}
				else
				{
					x = startX;
					xSpeed = -xSpeed;
				}
			}
			
			//Same thing but in the y direction
			if(ySpeed > 0)
			{
				if(y + ySpeed < endY)
				{
					y += ySpeed;
				}
				else
				{
					y = endY;
					ySpeed = -ySpeed;
				}
			}
			else
			{
				if(y - ySpeed > startY)
				{
					y += ySpeed;
				}
				else
				{
					y = startY;
					ySpeed = -ySpeed;
				}
			}
		}
	}
	
   /**
    * Takes platform sent in (only used if platforms are hardcoded)
    * and breaks it into units that are then added into the map
    */
	public void breakIntoUnits()
	{	
		//Runs through the platform and breaks it into its unit parts and
		//adds them into the game. Type is one for now
		for(int r = startY; r < startY + height; r += 15)
		{
			for(int c = startX; c < startX + width; c += 15)
			{
				//System.out.println(type);
				Unit u = new Unit(type, c, r, xSpeed, ySpeed, (endX - startX) + c, (endY - startY) + r);
				
				pUnits.add(u);
				Game.currentMap.map[r / 15][c / 15] = u;
				Game.currentMap.solidUnits.add(u);
			}
		}
		
		Game.currentMap.createBlocks();
	}
}
