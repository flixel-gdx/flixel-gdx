package org.flixel.examples.flxtilemap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Asset
{	
	public static TextureRegion Auto_Tiles;
	public static TextureRegion Alt_Tiles;
	public static TextureRegion Empty_Tiles;
	public static String Default_Auto;
	public static String Default_Alt;
	public static String Default_Empty;
	public static TextureRegion ImgSpaceman;
	
	public static void create()
	{			
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("examples/flxtilemap/pack"));
		Auto_Tiles = atlas.findRegion("auto_tiles");
		Alt_Tiles = atlas.findRegion("alt_tiles");
		Empty_Tiles = atlas.findRegion("empty_tiles");
		Default_Auto = Gdx.files.internal("examples/flxtilemap/default_auto.txt").readString();
		Default_Alt = Gdx.files.internal("examples/flxtilemap/default_alt.txt").readString();
		Default_Empty = Gdx.files.internal("examples/flxtilemap/default_empty.txt").readString();
		ImgSpaceman = atlas.findRegion("spaceman");
	}
}
