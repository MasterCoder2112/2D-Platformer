package com.structures;

import java.util.ArrayList;

import com.entities.Entity;

/**
 * @title Map
 * @author Alexander Byrd
 * Date Created: March 2, 2018
 * 
 * Description:
 * Holds all the values for a given "map" so that they will stay the same
 * when reloading it.
 */
public class Map 
{
	public ArrayList<Platform> platforms = new ArrayList<Platform>();
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	
	public Map() 
	{
		
	}
}
