package org.example;

import org.flixel.FlxSprite;
import org.flixel.data.R;

public class SpaceMan extends FlxSprite
{
	
	public SpaceMan(int X, int Y)
	{
		super(X, Y);
		loadGraphic(R.drawable.spaceman, true, true, 8);
		setSolid(true);
		addAnimation("walk", new int[]{1,2,3,0}, 12);
		play("walk");
	}
	
	@Override
	public void update()
	{
		//Log.i("SpaceMan", Integer.toString(width));
		//Log.i("SpaceMan", Integer.toString(_curFrame));
		
		
		super.update();
	}
}
