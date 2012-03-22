package org.flixel.examples.tilemap;

import org.flixel.FlxGame;

public class Tilemap extends FlxGame
{

	public Tilemap()
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
