package org.flixel.examples.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Asset
{
	static public TextureRegion ImgFlixelLogo;
	static public TextureRegion ImgZombie; // by Txai Viegas
	public static TextureRegion ImgDroid;

	public static void create()
	{
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("examples/animation/pack"));
		ImgFlixelLogo = atlas.findRegion("flixellogo");
		ImgZombie = atlas.findRegion("zombietxai");
		ImgDroid = atlas.findRegion("droid");
	}

}
