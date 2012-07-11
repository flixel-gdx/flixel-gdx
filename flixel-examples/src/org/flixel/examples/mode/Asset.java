package org.flixel.examples.mode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Asset
{
	public static TextureRegion ImgEnemy;
	public static TextureRegion ImgGibs;
	public static TextureRegion ImgSpawnerGibs;
	public static TextureRegion ImgCursor;
	public static TextureRegion ImgBullet;
	public static TextureRegion ImgBot;
	public static TextureRegion ImgJet;
	public static TextureRegion ImgSpaceman;
	public static TextureRegion ImgTech;
	public static TextureRegion ImgDirt;
	public static TextureRegion ImgDirtTop;
	public static TextureRegion ImgMiniFrame;
	public static TextureRegion ImgSpawner;
	public static TextureRegion ImgBotBullet;
	
	public static String Attract1;
	public static String Attract2;

	public static void create()
	{
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("examples/mode/pack"));
		ImgGibs = atlas.findRegion("gibs");
		ImgSpawnerGibs = atlas.findRegion("spawner_gibs");
		ImgCursor = atlas.findRegion("cursor");
		ImgBullet = atlas.findRegion("bullet");
		ImgBot = atlas.findRegion("bot");
		ImgJet = atlas.findRegion("jet");
		ImgSpaceman = atlas.findRegion("spaceman");
		ImgTech = atlas.findRegion("tech_tiles");
		ImgDirt = atlas.findRegion("dirt");
		ImgDirtTop = atlas.findRegion("dirt_top");
		ImgMiniFrame = atlas.findRegion("miniframe");
		ImgSpawner = atlas.findRegion("spawner");
		ImgBotBullet = atlas.findRegion("bot_bullet");
		
		Attract1 = Gdx.files.internal("examples/mode/attract1.fgr").readString();
		Attract2 = Gdx.files.internal("examples/mode/attract2.fgr").readString();
	}

}
