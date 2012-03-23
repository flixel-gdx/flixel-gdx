package org.flixel.examples.ezplatformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Asset
{	
	public static TextureRegion ImgAuto;
	
	public static void create()
	{			
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("examples/ezplatformer/pack"));
		ImgAuto = atlas.findRegion("autotiles");		
	}

}
