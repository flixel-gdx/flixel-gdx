package org.examples.draganddrop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class Asset
{

	public static AtlasRegion ImgCrate;
	public static AtlasRegion ImgLogo;
	
	public static void create()
	{
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("examples/draganddrop/pack"));
		ImgCrate = atlas.findRegion("crate");
		ImgLogo = atlas.findRegion("flixellogo");
	}

}
