package com.launcher;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import javax.swing.JTextArea;

import java.awt.Rectangle;

/**
 * @title  FPSLauncher
 * @author Alex Byrd, Jonathan Gramm
 * @modified 5/12/18
 * Description:
 * The README section of the launcher; reads & displays
 * info from a file and has a button back to the main menu. 
 *
 */
public class ReadMe extends RunLauncher
{	
	/*The compiler yells about a "serializable class" and "type long".
	 *    We're not using object serialization, so don't worry about it. */
	
	private JTextArea readme;
	
   /**
    * ReadMe menu constructor
    */
	public ReadMe() 
	{
		super(1);
		setTitle("Read Me:");
		drawReadMeText();
	}

	@Override
	protected void drawLauncherButtons()
	{
		back = addButton("Back to menu", new Rectangle(300, 150, 200, 40));
	}
	
	public void drawReadMeText()
	{
		String textString = "";
		try
		{
			Scanner sc = new Scanner(new BufferedReader
					(new FileReader("README.txt")));
		
			while(sc.hasNext())
			{
				textString += " "+sc.next();
			}
			
			sc.close();
		}
		catch(FileNotFoundException e)
		{
			
		}
	
		readme = addTextArea(textString, new Rectangle(WIDTH / 8, 50, WIDTH * 6 / 8, 80), true); //TODO: fix		
	}
}
