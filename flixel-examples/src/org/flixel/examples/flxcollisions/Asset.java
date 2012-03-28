package org.flixel.examples.flxcollisions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Asset
{
	public static FileHandle ImgMap;
	public static TextureRegion ImgPlayer;
	public static TextureRegion ImgPusher;
	public static TextureRegion ImgElevator;
	public static TextureRegion ImgCrate;
	public static TextureRegion ImgBG;
	public static TextureRegion ImgTiles;
	public static TextureRegion ImgGibs;
	
	public static void create()
	{							
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("examples/flxcollisions/pack"));
		ImgMap = Gdx.files.internal("examples/flxcollisions/map.png");
		ImgPlayer = atlas.findRegion("player");
		ImgPusher = atlas.findRegion("pusher");
		ImgElevator = atlas.findRegion("elevator");
		ImgCrate = atlas.findRegion("crate");
		ImgBG = atlas.findRegion("bg");
		ImgTiles = atlas.findRegion("tiles");
		ImgGibs = atlas.findRegion("gibs");
	}

}
