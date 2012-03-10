package org.examples.tiledmap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Asset
{	
	public static TextureRegion tiles;
	
	public static void create()
	{			
		Texture t = new Texture(Gdx.files.internal("examples/tiledmap/autotiles.png"));
		tiles = new TextureRegion(t, 128, 8);		
	}

}
