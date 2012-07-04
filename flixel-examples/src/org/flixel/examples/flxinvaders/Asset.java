package org.flixel.examples.flxinvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Asset
{

	public static TextureRegion ImgShip;
	public static TextureRegion ImgAlien;
	
	public static void create()
	{
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("examples/flxinvaders/pack"));
		ImgShip = atlas.findRegion("ship");
		ImgAlien = atlas.findRegion("alien");
	}
}
