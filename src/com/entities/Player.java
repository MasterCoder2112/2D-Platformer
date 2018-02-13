package com.entities;

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
	//Things that the player may or may not have in the future
	public int health = 100;
	public int armor = 100;
	public int ammo = 0;
	public int shield = 0;
	public double x = 100;
	public double y = 100;
	//public Weapon[] weapons = null;
	public int weight = 0;
	public int height = 100;
	public int girth = 40;
	public double speed = 0.05;
	//public Inventory inventory = null;
	
	/* Possible effects the player has on him/her ******/
	public int poisoned = 0;
	public int invincible = 0;
	public int invisible = 0;
	public int onFire = 0;
	public int frozen = 0;
	public int slowed = 0;
	public int spedUp = 0;
	public int lowGravity = 0;
	
	/* Player flags ************************************/
	public boolean isAlive = true;
	public boolean firing = false;
	public boolean jumping = false;
	public boolean swimming = false;
	public boolean crouching = false;
	public boolean running = false;
	
	//Initialize values
	public Player() 
	{
		//Things that the player may or may not have in the future
		health = 100;
		armor = 100;
		ammo = 0;
		shield = 0;
		x = 100;
		y = 100;
		//public Weapon[] weapons = null;
		weight = 0;
		height = 100;
		girth = 40;
		speed = 0.01;
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

}
