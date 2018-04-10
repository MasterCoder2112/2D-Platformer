package com.structures;

import com.base.Game;

/**
 * Title: Unit
 * @author Alexander Byrd
 * @date 04/02/2018
 * 
 * Description:
 * A game unit (a block of pixels) spanning 15 pixels by 15 pixels. Used
 * to construct platforms, and other entities. A Unit is also able to
 * be destroyed which is why the health is there. Some units cannot be
 * destroyed that are essential to gameplay and level progression, but
 * for secret areas and or treachorous traps, some units can be destroyed
 * for more complex gameplay mechanics.
 * 
 * Type determines texture, health, and destructability of a unit
 */
public class Unit 
{
	//Unchangeable unit length/width
	public static final int UNIT_LENGTH = 15;
	
	public int health = 100;
	public int type = 0;
	public double x = 0;
	public double y = 0;
	public int startX = 0;
	public int startY = 0;
	public double xSpeed = 0;
	public double ySpeed = 0;
	public int xDist = 0;
	public int yDist = 0;
	
	public boolean canDestroy = false; //Can't destroy by default
 	public boolean isSolid = false; //Air or not?
	
	public Unit(int type, int x, int y, double xSpeed, double ySpeed,
			int xDist, int yDist) 
	{
		this.type = type;
		this.x = x;
		this.y = y;
		this.startX = x;
		this.startY = y;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.xDist = xDist;
		this.yDist = yDist;
		
		//Depending on the type, set value for the object
		switch(type)
		{
			case 0:
				canDestroy = false;
				isSolid = false;
				health = 1;
				break;
			default:
				canDestroy = true;
				isSolid = true;
				health = 100;
				break;
		}
	}
	
   /**
    * Updates movement of unit.
    */
	public void updateUnit()
	{
		//Unit oldPos = Game.currentMap.map[(int)(x / 15)][(int)(y / 15)];
	   /*
	    * If the platform can keep moving in a given x direction, keep moving
	    * it that direction. Once it reaches the last movement, it sets the 
	    * position to the destination x and change the direction.
	    */
		if(xSpeed > 0)
		{
			if(x + xSpeed < xDist)
			{
				x += xSpeed;
			}
			else
			{
				x = xDist;
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
			if(y + ySpeed < yDist)
			{
				y += ySpeed;
			}
			else
			{
				y = yDist;
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
		
		//Unit newPos = Game.currentMap.map[(int)(x / 15)][(int)(y / 15)];
	}
}
