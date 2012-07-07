package org.flixel.examples.blur;

import org.flixel.FlxGame;

/**
 * A demo that shows colored squares. The squares leave a trail.
 * 
 * @author Adam Atomic
 * @author Ka Wing Chin
 */
public class BlurDemo extends FlxGame
{
	public BlurDemo()
	{
		super(480, 320, PlayState.class, 1, 30, 30);
	}
	
	
	@Override
	public void create()
	{
		Asset.create();
		super.create();
	}
}
