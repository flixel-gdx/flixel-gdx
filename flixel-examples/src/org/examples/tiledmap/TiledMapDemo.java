package org.examples.tiledmap;

import org.flixel.FlxGame;

public class TiledMapDemo extends FlxGame
{

	public TiledMapDemo()
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
