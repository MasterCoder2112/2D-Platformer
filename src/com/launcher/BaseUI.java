package com.launcher;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import com.base.RunGame;


//CLASS DEV STATUS: Copying loads of stuff from 3D Game's FPSLauncher.java


/**
 * @title  UI base, abstract
 * @author Alex Byrd, Jonathan Gramm
 * @modified 5/12/18
 * Description:
 * Contains basic UI elements and functions for child UIs to
 * build off of.
 *
 */
public abstract class BaseUI extends JFrame //, basicUI
{
	/*The compiler yells about a "serializable class" and "type long".
	 *    We're not using object serialization, so don't worry about it. */
	
	protected JPanel panel = new JPanel();

	protected JButton back;

	public static final int WIDTH  = 800;
	public static final int HEIGHT = 400;
	
   /*
    * Instantiates a new ActionListener that listens to whether Buttons 
    * are clicked or not.
    */
	ActionListener aL = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			ActionPerformed(e.getSource());
		}
	};
	

	protected void ActionPerformed(Object o)
	{
		//Eclipse warns it's unhandled - ignore it, it'll still compile; it's intentional.
		//throw new Exception("ActionPerformed method must be overridden by child classes.");
	}
	
	public BaseUI()
	{
		//setTitle("Excelsior Launcher ("+RunGame.dev+" "+RunGame.versionNum+")");
		setSize(new Dimension(WIDTH, HEIGHT));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setContentPane(panel);
		setLocationRelativeTo(null);
		
		panel.setBackground(new Color(0, 127, 225)); //Teal
		
		setResizable(false);
		setVisible(true);
		
		panel.setLayout(null);		
	}

	/**
	 * Creates a new button with the provided text & bounds, gives it the
	 * ActionListener [aL], adds it to the [panel], and returns it as this
	 * function's output.
	 */
	protected JButton addButton(String text, Rectangle bounds)
	{
		JButton btn  = new JButton(text);
		btn.setBounds(bounds);
		btn.addActionListener(aL);
		panel.add(btn);
		return btn;
	}
	
	/**
	 * Creates a new label with the provided text & bounds, adds it to the
	 * [panel], and returns it as this function's output.
	 */
	protected JLabel addLabel(String text, Rectangle bounds)
	{
		JLabel lbl = new JLabel(text);
		lbl.setBounds(bounds);
		panel.add(lbl);
		return lbl;
	}
	
	/**
	 * Creates a new text field with the provided text & bounds, sets it as
	 * either editable or read-only, adds it to the [panel], and returns it as
	 * this function's output.
	 */
	protected JTextField addTextField(String text, Rectangle bounds, boolean readOnly)
	{
		JTextField txt = new JTextField();
		txt.setBounds(bounds);
		txt.setText(text);
		txt.setEditable(!readOnly);
		panel.add(txt);
		return txt;
	}
	protected JTextArea addTextArea(String text, Rectangle bounds, boolean readOnly)
	{
		JTextArea txt = new JTextArea();
		txt.setBounds(bounds);
		txt.setText(text);
		txt.setEditable(!readOnly);
		txt.setLineWrap(true);
		panel.add(txt);
		return txt;
	}

	protected String InsertLineBreak(String text, int index)
	{
		return text.substring(0, index) + "\n" + text.substring(index + 1, text.length() - 1);
	}
	protected String InsertLineBreaks(String text, int lineLimit, boolean startWithIndent)
	{
		String temp = "" + text, rtn = "";
		boolean isFirst = true;
		String breakables = ",,?!\'\" ";
		while (temp.length() > lineLimit)
		{
			if (!isFirst) rtn += "\n";
				else if (startWithIndent) temp = "        " + temp;
			
			int i;
			for (i = lineLimit; i > 0; i--)
				if (breakables.contains("" + temp.toCharArray()[i])) //if (the current character is line-breakable)
				{
					rtn += temp.substring(0, i);
					temp = temp.substring(i, temp.length() - 1);
					break;
				}

			if (i == 0) //if (the for-loop failed to find a line-breakable character), break the while-loop.
			{
				if (!isFirst) rtn += "\n";
				rtn += temp;
				break;
			}
			//implicit else
			isFirst = false;
		}
		if (!isFirst) rtn += "\n" + temp;
		return rtn;
	}
}
