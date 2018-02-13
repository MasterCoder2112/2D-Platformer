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
		
		//Key events
		if(key[KeyEvent.VK_ESCAPE])
		{
			RunGame.frame.dispose();
			return false;
		}
		
		if(key[KeyEvent.VK_UP])
		{
			player.y -= player.speed;
		}
		
		if(key[KeyEvent.VK_DOWN])
		{
			player.y += player.speed;
		}
		
		if(key[KeyEvent.VK_RIGHT])
		{
			player.x += player.speed;
		}
		
		if(key[KeyEvent.VK_LEFT])
		{
			player.x -= player.speed;
		}
		
		return true;
	}
}
