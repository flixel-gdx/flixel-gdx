package org.example;

import org.flixel.FlxSprite;
import org.flixel.data.R;

public class Crate extends FlxSprite
{
	public Crate(float X, float Y)
	{
		super(X, Y, R.drawable.crate);
		height = height - 1;	//draw the crate 1 pixel into the floor
		acceleration.y = 400;
		drag.x = 200;
	}
}
