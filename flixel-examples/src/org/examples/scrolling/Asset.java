package org.examples.scrolling;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Asset
{
	static public TextureRegion ImgGibs;
	static public TextureRegion ImgBlock;
	
	public static void create()
	{
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("examples/scrolling/pack"));
		ImgGibs = atlas.findRegion("gibs");
		ImgBlock = atlas.findRegion("block");
	}
}
