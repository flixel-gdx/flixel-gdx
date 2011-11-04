package org.examples.draganddrop;

import org.flixel.FlxG;
import org.flixel.FlxPoint;
import org.flixel.FlxSprite;

public class Crate extends FlxSprite
{
	private boolean _pressed;
	
	public Crate(float x, float y)
	{
		super(x, y);
		loadGraphic(Asset.ImgCrate);
		_pressed = false;
		setSolid(true);
		acceleration.y = 400;
		maxVelocity.y = 400;
	}
	
	@Override
	public void update()
	{
		if(_pressed)
		{
			immovable = false;
			acceleration.y = velocity.y = 0;
		}
		else
		{
			immovable = false;
			acceleration.y = 400;
		}
		
		// Picks up the sprite
		if(overlapsPoint(new FlxPoint(FlxG.touch.x, FlxG.touch.y)) && FlxG.touch.pressed())
			_pressed = true;
		
		// Release the sprite
		if(FlxG.touch.justReleased()) // OR if(!Gdx.input.isTouched() && _pressed)
			_pressed = false;
		
		// Drag the sprite
		if(_pressed)
		{
			x = FlxG.touch.x - width/2;
			y = FlxG.touch.y - height/2;
		}
		if(isTouching(FLOOR) && !_pressed)
		{
			immovable = true;
			acceleration.y = velocity.y = 0;
		}
		
		super.update();
	}
}
