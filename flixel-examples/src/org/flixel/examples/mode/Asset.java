package org.flixel.examples.mode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Asset
{
	public static TextureRegion ImgEnemy;
	public static TextureRegion ImgGibs;
	public static TextureRegion ImgCursor;
	public static Sound SndHit;
	public static Sound SndHit2;
	
	public static String Attract1;
	public static String Attract2;

	public static void create()
	{
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("examples/mode/pack"));
		ImgEnemy = atlas.findRegion("bot");
		ImgGibs = atlas.findRegion("spawner_gibs");
		ImgCursor = atlas.findRegion("cursor");
		SndHit = Gdx.audio.newSound(Gdx.files.internal("examples/mode/menu_hit.mp3"));
		SndHit2 = Gdx.audio.newSound(Gdx.files.internal("examples/mode/menu_hit_2.mp3"));
		
		Attract1 = Gdx.files.internal("examples/mode/attract1.fgr").readString();
		Attract2 = Gdx.files.internal("examples/mode/attract2.fgr").readString();
	}

}
