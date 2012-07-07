package org.flixel.examples.replay;

import org.flixel.FlxGame;

/**
 * Replays are a powerful new feature in Flixel. Replays are 
 * essentially a list of what keyboard keys were pressed, and what 
 * mouse inputs were given, during a specific time frame. Because 
 * Flixel is largely deterministic, we can use that information 
 * to recreate a gameplay session that someone else recorded, as 
 * long as we have the same SWF. Replays can be used for 
 * debugging, arcade-style "attract modes" or in-game demos, and 
 * even for cutscenes. Replays can be manipulated using the "VCR" 
 * panel on the debugger overlay, or directly through functions like 
 * FlxG.loadReplay(), FlxG.recordReplay(), and 
 * FlxG.reloadReplay(). Adventurous game makers can also 
 * check out the more complex Mode source code to see an 
 * example of loading a replay from a file to create an "attract 
 * mode". 
 * 
 * @author deadkidsong
 * @author Thomas Weston
 */
public class ReplayDemo extends FlxGame
{
	public ReplayDemo()
	{
		super(400, 300, PlayState.class, 1, 20, 20);
	}
	
	@Override
	public void create()
	{
		Asset.create();
		super.create();
	}
}
