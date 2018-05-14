package com.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.base.Display;
import com.base.SoundController;
import com.entities.Player;
import com.entities.Projectile;
import com.base.Game;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener
{
	//All the possible combinations on the keyboard
	public boolean[] key = new boolean[68836];
	
	public static double mouseX;
	public static double mouseY;
	private static double upAngle;
	private static double distanceX;
	private static double distanceY;
	
	@Override
   /**
    * If a key is pressed, this takes in that input event, gets the keycode
    * from it, and then sets the element in the array for address to true
    * to show that the key has been pressed.
    */
	public void keyPressed(KeyEvent e) 
	{
		int keyCode = e.getKeyCode();
		
		if(keyCode > 0 && keyCode < key.length)
		{
			key[keyCode] = true;
		}
	}

	@Override
	/**
	 * When a key is released it sets the keys addresses to being false
	 * again since they are not being pressed.
	 */
	public void keyReleased(KeyEvent e) 
	{
		int keyCode = e.getKeyCode();
		
		if(keyCode > 0 && keyCode < key.length)
		{
			key[keyCode] = false;
		}
	}
	
	public void mousePressed(MouseEvent e)
	{
		SoundController.laser.playAudioFile(0);
		
		distanceX = mouseX - (Player.x + Player.girth);
        distanceY = (Player.y - (Player.height * 3/4) - mouseY);
        
        upAngle = Math.atan((distanceX)/(distanceY));
        
        if(mouseX >= Display.player.x && mouseX <= Display.player.x + Display.player.girth)
        {
        	upAngle = 0;
        }
        
		
		//Depending on direction, have projectile come out of left or
		//right side of Display.player.
		if(Display.player.direction == 1 && Game.shootTimer == 0)
		{
			new Projectile(Player.x + Player.girth, Player.y - (Player.height * 3 / 4),
				0.02, 25, Display.player.direction, 0, null, -upAngle, 0);
		}
		else if(Game.shootTimer == 0)
		{
			new Projectile(Player.x, Player.y - (Player.height * 3 / 4),
					0.02, 25, Display.player.direction, 0, null, upAngle, 0);
		}
		
		Game.shootTimer++;
	}
		
	public void mouseReleased(MouseEvent e) 
	{
		
	}
	
	public void mouseExited(MouseEvent e)
	{
		
	}
	
	public void mouseEntered(MouseEvent e)
	{
		
	}
	
	public void mouseClicked(MouseEvent e) 
	{
		
	}
	
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseX = e.getX();
        mouseY = e.getY();
        
        if(mouseX < Player.x)
        {
        	Display.player.direction = -1;
        }
        else
        {
        	Display.player.direction = 1;
        }
        
	}

}
