package org.examples.replay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Asset
{
	static public TextureRegion ImgTiles;
	static public String MapData;

	public static void create()
	{
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("examples/replay/pack"));
		ImgTiles = atlas.findRegion("tiles");
		MapData = Gdx.files.internal("examples/replay/simpleMap.csv").readString();
	}

}
