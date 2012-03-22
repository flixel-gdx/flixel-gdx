package org.flixel.examples.replay;

import org.flixel.FlxGame;

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
