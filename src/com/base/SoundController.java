package com.base;

import java.util.ArrayList;
import com.base.Sound;

/**
 * @title SoundController
 * @author Alexander Byrd
 * @date 3/24/2017
 * 
 * Description:
 * The only purpose of this is to hold all the various sounds that the
 * game has, and makes them public and static so they can be accessed
 * from anywhere to be played. Instantiates all the sounds with a 
 * defaultSize of clips to be played, and the file location of the
 * audio file it needs to load.
 */
public class SoundController 
{
	// Sound files
	public static Sound jump;
	public static Sound laser;
	public static Sound crushed;
	public static Sound death;
	public static Sound meleeHit;
	public static Sound meleeSwing;
	public static Sound defaultHurt;
	public static Sound wallHit;
	public static Sound music;
	
	//Stores all the sounds so that all the sounds can be
	//set to certain settings at once
	public static ArrayList<Sound> allSounds;
	
	private int defaultSize = 4;
	
   /**
    * Contructs all sounds held in the sound controller
    */
	public SoundController() 
	{
		//reset the arraylist
		allSounds = null;
		allSounds = new ArrayList<Sound>();
		
		jump = addSound(jump, "jump", false, defaultSize);
		laser = addSound(laser, "laser", false, defaultSize);
		crushed = addSound(crushed, "crushed", false, defaultSize);
		death = addSound(death, "death", false, defaultSize);
		meleeHit = addSound(meleeHit, "meleeHit", false, defaultSize);
		meleeSwing = addSound(meleeSwing, "meleeSwing", false, defaultSize);
		defaultHurt = addSound(defaultHurt, "defaultHurt", false, defaultSize);
		wallHit = addSound(wallHit, "wallHit", false, defaultSize);
		music = addSound(music, "level0", true, 1);
	}
	
   /**
    * Used to reset all of the sound volumes. Usually used when the game
    * is reloaded.
    * @param newVolume
    */
	public void resetAllVolumes(float newVolume)
	{
		for(Sound temp: allSounds)
		{
			temp.resetVolume(newVolume);
		}
	}
	
   /**
    * Basically nullifies all sounds to clear the heap
    */
	public void resetSounds()
	{
		for(Sound temp: allSounds)
		{
			temp.nullify();
			temp = null;
		}
		
		allSounds = null;
	}
	
   /**
    * Instantiates a single sound at a time, checking to see if first it
    * is in the current resource pack, and then if not there it will use
    * the default sound found in the default resource pack. If not in
    * either the sound will just be null, but it will not cause the
    * program to crash.
    * @param temp
    * @param fileName
    */
	private Sound addSound(Sound temp, String fileName, boolean loop, int size)
	{
		try
		{
			//Try to find sound in current resource pack
			temp =  new Sound(size, "resources/audio/"+fileName+".wav", loop);
			allSounds.add(temp);
			temp.audioName = fileName;
		}
		catch(Exception e)
		{
			try
			{
				//If sound is not found in the resource pack
				temp =  new Sound(size, "resources/audio/"+fileName+".wav", loop);
				allSounds.add(temp);
				temp.audioName = fileName;
			}
			catch(Exception ex)
			{
				//Sound is just null if not found in either
				//Print exception though for debugging
				//Sound will not be added to allSounds if this is the case
			}
		}
		
		return temp;
	}
}
