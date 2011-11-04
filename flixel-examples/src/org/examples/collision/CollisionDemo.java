package org.examples.collision;

import org.flixel.FlxGame;

public class CollisionDemo extends FlxGame
{

	public CollisionDemo()
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
