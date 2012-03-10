package org.examples.tiledmap2;

import org.flixel.FlxGame;

public class TiledMap2Demo extends FlxGame
{

	public TiledMap2Demo()
	{
		super(480, 320, PlayState.class);
	}
	
	@Override
	public void create()
	{
		Asset.create();
		super.create();
	}

}
