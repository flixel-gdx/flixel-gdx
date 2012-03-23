package org.flixel.examples.ezplatformer;

import org.flixel.FlxGame;

public class EZPlatformer extends FlxGame
{

	public EZPlatformer()
	{
		super(320, 240, PlayState.class);
	}
	
	@Override
	public void create()
	{
		Asset.create();
		super.create();
	}

}
