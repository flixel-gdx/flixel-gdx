package org.flixel.examples.savedemo;

import org.flixel.FlxGame;

/**
 * Flash includes a simple way to save data locally,
 * and the FlxSave object allows you to interface with it.
 * This system is not ideal for all things; for example,
 * if you are doing online high scores, this won't really work.
 * If you want players to be able to move and trade save game files,
 * this isn't a good fit either. However, for fast and simple
 * saving of local data, especially things like unlocked progress
 * or user preferences, FlxSave is an easy and built-in option.
 * 
 * @author Zachary Tarvit
 * @author Thomas Weston
 */
public class SaveDemo extends FlxGame
{
	public SaveDemo()
	{
		super(400, 300, PlayState.class, 1, 20, 20);
	}
}
