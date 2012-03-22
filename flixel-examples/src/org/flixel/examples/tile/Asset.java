package org.flixel.examples.tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Asset
{
	static public TextureRegion ImgTechTiles;
	
	public static void create()
	{
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("examples/tile/pack"));
		ImgTechTiles = atlas.findRegion("tech");
	}
}
