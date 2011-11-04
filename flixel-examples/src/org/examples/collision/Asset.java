package org.examples.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Asset
{
	static public TextureRegion gibs;
	static public TextureRegion platform;
	
	static public void create()
	{
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("examples/collision/pack"));
		gibs = atlas.findRegion("gibs");
		platform = atlas.findRegion("platform");
	}
}
