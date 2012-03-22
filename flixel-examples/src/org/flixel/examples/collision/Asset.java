package org.flixel.examples.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Asset
{
	static public TextureRegion ElevatorPNG;
	static public TextureRegion CratePNG;
	static public TextureRegion FlixelRiderPNG;
	
	static public void create()
	{
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("examples/collision/pack"));
		ElevatorPNG = atlas.findRegion("elevator");
		CratePNG = atlas.findRegion("crate");
		FlixelRiderPNG = atlas.findRegion("flixelLogo");
	}
}
