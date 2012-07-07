package org.flixel.examples.splitscreen;

import org.flixel.FlxGame;

/**
 * One of the new features in Flixel is the introduction of a flexible 
 * and powerful camera class called (unsurprisingly) FlxCamera. 
 * By default, a new Flixel game project starts with one camera that 
 * is the same size as the Flash Player window, which can be 
 * referenced at FlxG.camera. You can replace that camera or 
 * add additional cameras to create effects like "split screen"
 * views, or "picture in picture" style displays, or even mini-maps.
 * Each camera is an independent display object, with its own zoom,
 * color tint, rotation, and scaling values. Finally, each game object
 * maintains its own camera list, so you can easily instruct 
 * certain objects to only display on certain cameras. Adventurous 
 * game makers can also check out the more complex Mode 
 * source code for more ways to use cameras in-game.
 *  
 * @author Philippe Mongeau
 * @author Thomas Weston
 */
public class SplitScreen extends FlxGame
{
	public SplitScreen() 
	{
		super(400, 300, PlayState.class, 1, 20, 20);
	}
}
