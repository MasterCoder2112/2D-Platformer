package com.entities;

/**
 * @title Point
 * @author Alexander Byrd
 * Date Created: 2/26/2018
 * 
 * Description:
 * Creates a position (x and y values) called a point, but instead of ints
 * it is composed of doubles so that it is more precise. It simply makes
 * it easier to draw vectors int the code.
 *
 */
public class Point 
{
	public double x;
	public double y;
	
	public Point(double x, double y) 
	{
		this.x = x;
		this.y = y;
	}

}
