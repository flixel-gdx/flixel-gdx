package org.flixel.examples.tile;

import org.flixel.FlxG;
import org.flixel.FlxState;
import org.flixel.FlxTileblock;

public class PlayState extends FlxState
{
	private String ImgTechTiles = "examples/tile/pack:tech";
	@Override
	public void create()
	{
		FlxG.setBgColor(0xFF000000);
		FlxTileblock tileblock = new FlxTileblock(0, FlxG.height-32, FlxG.width, 32);
		tileblock.loadTiles(ImgTechTiles, 16, 16);
		add(tileblock);
	}

}
