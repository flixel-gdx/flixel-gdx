package org.flixel.examples.tile;

import org.flixel.FlxG;
import org.flixel.FlxState;
import org.flixel.FlxTileblock;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;

public class PlayState extends FlxState
{

	@Override
	public void create()
	{
		FlxTileblock tileblock = new FlxTileblock(0, FlxG.height-32, FlxG.width, 32);
		tileblock.loadTiles(Gdx.files.getFileHandle("examples/tile/blocks.png", Files.FileType.Internal), 16, 16, 128, 16);
		add(tileblock);
	}

}
