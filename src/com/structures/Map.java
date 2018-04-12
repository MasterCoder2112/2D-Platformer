package com.structures;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import com.base.RunGame;
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
	public ArrayList<Unit> solidUnits = new ArrayList<Unit>();
	public ArrayList<Block> blocks = new ArrayList<Block>();
	
	//Map of all units, even the air units
	public Unit[][] map;
	
	
	public Map() 
	{
		//Default map is just the size of the single screen
		map = new Unit[(RunGame.HEIGHT / 15) * 4][(RunGame.WIDTH / 15) * 4];
		
		//Set up the map defaultely with air units
		for(int r = 0; r < map.length; r++)
		{
			for(int c = 0; c < map[0].length; c++)
			{
				map[r][c] = new Unit(0, r * 15, c * 15, 0, 0, 0, 0);
			}
		}
	}
	
   /**
    * Will load a given map using the file name sent in. After
    * execution, map will be initialized as well as the platforms
    * , entities, etc...
    * @param file
    */
	public void loadMap(String file)
	{
		//A new scanner object that is defaultly set to null
		Scanner sc = null;
		
	   /*
	    * Try to read the settings file and if not,
	    * state the error
	    */
		try 
		{
			int row = 0;
			int col = 0;
			
			//Creates a Scanner that can read the file
			sc = new Scanner(new BufferedReader
					(new FileReader(file)));
			
			//Sets up the map. Well at least the size of it that is
			int mapWidth = sc.nextInt();
			int mapHeight = sc.nextInt();
			map = new Unit[mapHeight][mapWidth];
			
			
			//Read in the file to set the units of the map up
			while(sc.hasNextLine())
			{
				//Split line into each unit
				String line = sc.nextLine();
				String[] unitsInRow = line.split(",");
				
				//TODO have each unit also contain a x and y speed, and movement variables
				
				//For each unit, get the type, x, and y value and add it to the map
				for(String u: unitsInRow)
				{
					String[] attributes = u.split(":");
					
					int type = Integer.parseInt(attributes[0]);
					double xSpeed = Double.parseDouble(attributes[1]);
					double ySpeed = Double.parseDouble(attributes[2]);
					int xDist = Integer.parseInt(attributes[3]);
					int yDist = Integer.parseInt(attributes[4]);
					
					Unit next = new Unit(type, col * Unit.UNIT_LENGTH, row * Unit.UNIT_LENGTH, xSpeed,
							ySpeed, xDist, yDist);
					
					solidUnits.add(next);
					map[row][col] = next;
					col++;
				}
				
				row++;
			}
		}
		catch(Exception ex)
		{
			//For some reason sc.hasNextLine() throws error on autogradr
			System.out.println("Error reading file: "+file);
		}
		finally
		{
			sc.close(); //Close file scanner
		}
		
		//Parses units together into platforms
		findPlatforms();
	}
	
   /**
    * Takes the units in the map, finds which ones are next to each other,
    * and puts them together to form one "platform" type. In order to be
    * considered together though, they must be not only right next to each
    * other, but must also have the same x and y speed, and same movement
    * values. Otherwise, it is just a seperate platform that is next to
    * another platform
    */
	public void findPlatforms()
	{
		//TODO Figure this crap out eventually
	}
	
   /**
    * Uses the units in the map to create blocks. Blocks are sections of
    * units where the length and width of the units in an area create a
    * block, which will in turn have a bigger texture.
    */
	public void createBlocks()
	{
		boolean[][] searched = new boolean[map.length][map[0].length];
		
		for(int i = 0; i < solidUnits.size(); i++)
		{
			Unit u = solidUnits.get(i);
			
			int ux = (int)u.x / 15;
			int uy = (int)u.y / 15;
			
			//If it hasn't been searched yet
			if(((!searched[uy][ux])
					&& u.type != 0))
			{
				Block b = new Block();
				u.parentBlock = b;
				b.bUnits.add(u);
				searched[uy][ux] = true;
				
				boolean limitFound = false;
				int limit = 0;
				
				for(int j = 1; j <= Block.MAX_WIDTH; j++)
				{
					for(int k = 0; k <= j; k++)
					{
						for(int l = 0; l <= j; l++)
						{
							Unit temp = map[uy + l][ux + k];
							
						   /*
						    * If this unit has either been searched, or
						    * is already in a block, or is of type air,
						    * then end these loops. It cannot be in a
						    * block of the above range.
						    */
							if(((searched[uy + l][ux + k]
									&& temp.parentBlock != null && 
									!temp.parentBlock.equals(b))
									|| temp.type == 0 || temp.type != u.type) 
									&& !temp.equals(u))
							{
								limitFound = true;
								break;
							}
						}
						
						//Break out if a limit is found
						if(limitFound)
						{
							break;
						}
					}
					
					//If limit is found then break, otherwise update limit and go again
					if(limitFound)
					{
						limit = j - 1;
						break;
					}
					
					//If reached max limit
					if(j == Block.MAX_WIDTH)
					{
						limit = Block.MAX_WIDTH;
					}
				}
				
				//Go up to limit in both x and y and add those given units to the
				//block
				for(int k = 0; k <= limit; k++)
				{
					for(int l = 0; l <= limit; l++)
					{
						//Go back through and add all the units in square to both
						//the bUnits array list and searched double array.
						if(!searched[uy + l][ux + k])
						{
							Unit temp = map[uy + l][ux + k];
							
							temp.parentBlock = b;
							searched[uy + l][ux + k] = true;
							b.bUnits.add(temp);
						}
					}
				}
				
				//Set width of the block
				b.width = limit + 1;
				
				//Update the block
				b.setUpBlock();
				
				//Add block to the map
				blocks.add(b);
			}
		}
	}
}
