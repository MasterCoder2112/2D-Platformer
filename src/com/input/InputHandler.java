package com.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener 
{
	//All the possible combinations on the keyboard
	public boolean[] key = new boolean[68836];
	
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

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
