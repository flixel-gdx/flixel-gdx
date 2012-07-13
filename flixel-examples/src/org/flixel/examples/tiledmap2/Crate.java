package org.flixel.examples.tiledmap2;

import org.flixel.FlxSprite;

public class Crate extends FlxSprite
{
	private static String ImgCrate = "examples/tiledmap2/pack:crate";
	
	public Crate(float X, float Y)
	{
		super(X, Y);
		loadGraphic(ImgCrate,false);
		height = height-1;		//draw the crate 1 pixel into the floor
		acceleration.y = 400;
		drag.x = 200;
	}
}
