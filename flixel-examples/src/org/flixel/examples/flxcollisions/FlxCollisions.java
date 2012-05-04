package org.flixel.examples.flxcollisions;

import org.flixel.FlxGame;

/**
 * A test bed for Flixel's collision systems.
 * 
 * @author Thomas Weston
 */
public class FlxCollisions extends FlxGame
{
	public FlxCollisions()
	{
		super(320, 240, 640, 480, PlayState.class, 2, 40, 40);
	}
	
	@Override
	public void create()
	{
		Asset.create();
		super.create();
	}
}
