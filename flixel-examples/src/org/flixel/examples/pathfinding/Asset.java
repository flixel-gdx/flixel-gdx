package org.flixel.examples.pathfinding;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Asset
{
	static public TextureRegion ImgTiles;
	static public String DataMap;
	
	public static void create()
	{
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("examples/pathfinding/pack"));
		ImgTiles = atlas.findRegion("tiles");
		
		DataMap = Gdx.files.internal("examples/pathfinding/pathfinding_map.txt").readString();
	}
}
