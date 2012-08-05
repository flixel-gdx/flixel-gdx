package org.flixel.examples.bloom;

import org.flixel.FlxGame;

/**
 * A simple "light bloom" special effect test.
 * 
 * @author Adam Atomic
 * @author Ka Wing Chin
 */
public class FlxBloom extends FlxGame
{
	public FlxBloom()
	{
		super(640,480,PlayState.class,1,60,60);
		forceDebugger = true;
	}
}
