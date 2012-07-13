package org.flixel.examples.tiledmap2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLoader;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;

public class Asset
{
	public static TiledMap map;
	
	public static void create()
	{
		final String path = "examples/tiledmap2/";
		final String mapname = "map01";
				
		FileHandle mapHandle = Gdx.files.internal(path + mapname + ".tmx");		
		map = TiledLoader.createMap(mapHandle);
	}

}
