package org.examples.blur;

import org.flixel.FlxGame;

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
