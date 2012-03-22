package org.flixel.examples.tile;

import org.flixel.FlxGame;

public class TileDemo extends FlxGame
{

	public TileDemo()
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
