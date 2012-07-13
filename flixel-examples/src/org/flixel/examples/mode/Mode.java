package org.flixel.examples.mode;

import org.flixel.FlxGame;

/**
 * A fully featured game, with enemies and procedural level generation.
 * 
 * @author Adam Atomic
 * @author Thomas Weston
 */
public class Mode extends FlxGame
{
	public Mode()
	{
		super(320, 240, 640, 480, MenuState.class, 2, 50, 50);		
	}
}
