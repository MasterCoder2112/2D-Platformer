package com.launcher;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.base.RunGame;


//CLASS DEV STATUS: Copying loads of stuff from 3D Game's FPSLauncher.java


/**
 * @title  FPSLauncher
 * @author Alex Byrd, Jonathan Gramm
 * @modified 5/12/18
 * Description:
 * Starts the program off with the main menu. This holds the
 * Start New Game button, the Controls, the Read Me file, 
 * and the Quit button. 
 *
 */
public class RunLauncher extends BaseUI //, basicUI
{	
	/*The compiler yells about a "serializable class" and "type long".
	 *    We're not using object serialization, so don't worry about it. */
	
	private JButton playGame, readMe, quit;
	protected JButton back;
	
	protected int panelType;
	
	@Override
	public void ActionPerformed(Object o)
	{
		if(o == playGame)
		{
			dispose();
			new RunGame();
		}

		/* if(o == controls)
		{
			dispose();
			new Controls();
		} */
		
		if(o == readMe)
		{
			dispose();
			new ReadMe();
		}
		
		if(o == back)
		{
			dispose();
			new RunLauncher(0);
		}

		if(o == quit)
		{
			System.exit(0);
		}
	}


	public RunLauncher()
	{
		this(0);
	}
	
	public RunLauncher(int panelType)
	{
		super();
		
		this.panelType = panelType;		
		drawLauncherButtons();
	}

	
	protected void drawLauncherButtons()
	{
		playGame = addButton("New Game", new Rectangle(300, 50, 200, 40));
		
		/* controls = new JButton("Controls");
		controls.setBounds(new Rectangle(300, 150, 200, 40));
		controls.addActionListener(aL);
		panel.add(controls); */
		
		readMe = addButton("Read Me", new Rectangle(300, 200, 200, 40));				
		quit = addButton("Quit", new Rectangle(300, 250, 200, 40));
	}
	
   /**
    * Draw all the Textfields and JButtons that will appear and work
    * in the controls menu.
    */
	/* public void drawControlsButtons()
	{
		
	} */
}
