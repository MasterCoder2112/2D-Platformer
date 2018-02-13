package com.base;

import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import com.entities.Player;

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
	private int tickCount = 0;
	public static final double GRAVITY = 0.000005;
	public Player player;
	
	//Set up initial variables
	public Game() 
	{
		player = new Player();
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
		
		//If running
		if(key[KeyEvent.VK_SHIFT])
		{
			player.running = true;
		}
		else
		{
			player.running = false;
		}
		
		//If jumping and player is on the ground
		if(key[KeyEvent.VK_UP] && player.y == player.floor)
		{
			player.jumping = true;
			player.upSpeed = Player.DEFAULT_JUMP_SPEED;
		}
		
		//If crouching
		if(key[KeyEvent.VK_DOWN])
		{
			player.crouching = true;
		}
		else
		{
			player.crouching = false;
		}
		
		player.updateValues();
		
		double xa = 0;
		
		//If moving right
		if(key[KeyEvent.VK_RIGHT])
		{		
			xa = player.speed;
		}
		
		//If moving left
		if(key[KeyEvent.VK_LEFT])
		{
			xa = -player.speed;
		}
		
	   /*
	    * If player is in the air, any movement from side to side
	    * is going to be much slower as it is hard to change direction
	    * while in air
	    */
		if(player.inAir)
		{
			xa /= 3;
		}

		player.move(xa);
		
		return true;
	}
}
