package org.examples.tiledmap2;

import org.flixel.FlxSprite;

public class Crate extends FlxSprite
{
	public Crate(float X, float Y)
	{
		super(X, Y);
		loadGraphic(Asset.ImgCrate,false,10,10);
		acceleration.y = 400;
		drag.x = 200;
	}
}
