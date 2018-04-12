package com.structures;

import java.util.ArrayList;

/**
 * Title: Block
 * @author Alexander Byrd
 * Date Created: 04/11/2018
 * 
 * Description:
 * A collection of units that make up a block formation of 5x5 or less. Depending
 * on the length and width of units in this "block" the texture of the block will
 * be different.
 */
public class Block 
{
	//Maximum size a block can be
	public static final int MAX_WIDTH = 15;
	
	public ArrayList<Unit> bUnits = new ArrayList<Unit>();
	public int width = 1;
	public int type = 1;
	public double x = 0;
	public double y = 0;
	
	public Block() 
	{
		
	}
	
   /**
    * Determines width based on the square root of the amount of units in the block
    * and also determines the x and y value of the block because it will be the same
    * as the first units x and y position.
    */
	public void setUpBlock()
	{
		//width = (int)Math.sqrt(bUnits.size());
		x = bUnits.get(0).x;
		y = bUnits.get(0).y;
		type = bUnits.get(0).type;
	}

}
