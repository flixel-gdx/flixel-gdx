package org.flixel.examples.thousandparticles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Asset
{
	static public TextureRegion ImgColors; 
	
	static public void create()
	{
		Texture texture = new Texture(Gdx.files.internal("examples/thousandparticles/colors.png"));
		ImgColors = new TextureRegion(texture);
	}
}
