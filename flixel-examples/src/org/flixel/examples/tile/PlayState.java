package org.flixel.examples.tile;

import org.flixel.FlxG;
import org.flixel.FlxState;
import org.flixel.FlxTileblock;

public class PlayState extends FlxState
{

	@Override
	public void create()
	{
		FlxTileblock tileblock = new FlxTileblock(0, FlxG.height-32, FlxG.width, 32);
		tileblock.loadTiles(Asset.ImgNumberTiles, 16, 16);
		add(tileblock);
	}

}
