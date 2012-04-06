package org.flixel.examples.mode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
	
	public static Sound SndHit;
	public static Sound SndHit2;
	public static Sound SndShoot;
	public static Sound SndJet;
	public static Sound SndExplode;
	public static Sound SndLand;
	public static Sound SndJump;
	public static Sound SndHurt;
	public static Sound SndJam;
	public static Sound SndCount;
	public static Sound SndBotShoot;
	public static Sound SndBotHit;
	
	public static Music SndMode;
	
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
		
		SndHit = Gdx.audio.newSound(Gdx.files.internal("examples/mode/menu_hit.mp3"));
		SndHit2 = Gdx.audio.newSound(Gdx.files.internal("examples/mode/menu_hit_2.mp3"));
		SndShoot = Gdx.audio.newSound(Gdx.files.internal("examples/mode/shoot.mp3"));
		SndJet = Gdx.audio.newSound(Gdx.files.internal("examples/mode/jet.mp3"));
		SndExplode = Gdx.audio.newSound(Gdx.files.internal("examples/mode/asplode.mp3"));
		SndLand = Gdx.audio.newSound(Gdx.files.internal("examples/mode/land.mp3"));
		SndJump = Gdx.audio.newSound(Gdx.files.internal("examples/mode/jump.mp3"));
		SndHurt = Gdx.audio.newSound(Gdx.files.internal("examples/mode/hurt.mp3"));
		SndJam = Gdx.audio.newSound(Gdx.files.internal("examples/mode/jam.mp3"));
		SndCount = Gdx.audio.newSound(Gdx.files.internal("examples/mode/countdown.mp3"));
		SndBotShoot = Gdx.audio.newSound(Gdx.files.internal("examples/mode/enemy.mp3"));
		SndBotHit = Gdx.audio.newSound(Gdx.files.internal("examples/mode/hit.mp3"));
		
		SndMode = Gdx.audio.newMusic(Gdx.files.internal("examples/audio/mode.ogg"));
		
		Attract1 = Gdx.files.internal("examples/mode/attract1.fgr").readString();
		Attract2 = Gdx.files.internal("examples/mode/attract2.fgr").readString();
	}

}
