package org.flixel.examples.collision;

import org.flixel.FlxGame;

public class CollisionDemo extends FlxGame
{

	public CollisionDemo()
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
