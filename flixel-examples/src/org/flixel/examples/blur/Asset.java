package org.flixel.examples.blur;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Asset
{
	static public TextureRegion Img0090e9;
	static public TextureRegion Img00f2ed;
	static public TextureRegion ImgFFF237;
	static public TextureRegion ImgFF0012;
	static public TextureRegion ImgCC00FF;	
	static public TextureRegion Img00F225;
	static public TextureRegion ImgFF6600;
	static public TextureRegion Img801E98;
	static public TextureRegion ImgColors;
	
	public static void create()
	{
		Texture colors = new Texture(Gdx.files.internal("examples/blur/colors.png"));
		ImgColors = new TextureRegion(colors);
		Img0090e9 = new TextureRegion(colors, 0, 0, 32, 32);
		Img00f2ed = new TextureRegion(colors, 32, 0, 32, 32);
		ImgFFF237 = new TextureRegion(colors, 64, 0, 32, 32);
		ImgFF0012 = new TextureRegion(colors, 96, 0, 32, 32);
		Img00F225 = new TextureRegion(colors, 128, 0, 32, 32);
		ImgCC00FF = new TextureRegion(colors, 160, 0, 32, 32);
		ImgFF6600 = new TextureRegion(colors, 192, 0, 32, 32);
		Img801E98 = new TextureRegion(colors, 224, 0, 32, 32);
	}

}
