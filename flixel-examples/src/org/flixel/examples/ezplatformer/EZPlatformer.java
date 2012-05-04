package org.flixel.examples.ezplatformer;

import org.flixel.FlxGame;

public class EZPlatformer extends FlxGame
{

	public EZPlatformer()
	{
		super(320, 240, 640, 480, PlayState.class, 2);
	}
	
	@Override
	public void create()
	{
		Asset.create();
		super.create();
	}

}
