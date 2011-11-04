package org.examples.animation;

import org.flixel.FlxGame;

public class AnimationDemo extends FlxGame
{
	public AnimationDemo()
	{
		super(480, 320, PlayState.class, 1, 60, 30);
	}
	
	@Override
	public void create()
	{
		Asset.create();
		super.create();
	}
}
