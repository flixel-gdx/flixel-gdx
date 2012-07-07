package org.flixel.examples.tiledmap2;

import org.flixel.FlxGame;

/**
 * It's the playstate3 from the collision demo. This demo loads the tiles from
 * a TMX file created by Lithander.
 * 
 * @author Adam Atomic
 * @author Ka Wing Chin
 */
public class TiledMap2Demo extends FlxGame
{

	public TiledMap2Demo()
	{
		super(320, 240, 640, 480, PlayState.class, 2, 40, 40);
		forceDebugger = true;
	}
	
	@Override
	public void create()
	{
		Asset.create();
		super.create();
	}

}
