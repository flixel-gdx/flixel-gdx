package org.examples.tiledmap2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLoader;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;

public class Asset
{
	public static TiledMap map;
	public static TextureRegion ImgPlayer;
	public static TextureRegion ImgPusher;
	public static TextureRegion ImgElevator;
	public static TextureRegion ImgCrate;
	public static TextureRegion ImgBG;
	public static TextureRegion ImgTiles;
	public static TextureRegion ImgGibs;
	
	public static void create()
	{
		final String path = "examples/tiledmap2/";
		final String mapname = "map01";
				
		FileHandle mapHandle = Gdx.files.internal(path + mapname + ".tmx");		
		map = TiledLoader.createMap(mapHandle);
		
		Texture t = new Texture(Gdx.files.internal("examples/tiledmap2/tiles.png"));
		ImgTiles = new TextureRegion(t, 128, 8);
		
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(path + "pack"));
		ImgPlayer = atlas.findRegion("player");
		ImgPusher = atlas.findRegion("pusher");
		ImgElevator = atlas.findRegion("elevator");
		ImgCrate = atlas.findRegion("crate");
		ImgBG = atlas.findRegion("bg");
		ImgGibs = atlas.findRegion("gibs");
	}

}
