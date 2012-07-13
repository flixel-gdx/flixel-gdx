package org.flixel.examples.draganddrop;

import org.flixel.FlxG;
import org.flixel.FlxPoint;
import org.flixel.FlxSprite;

public class Crate extends FlxSprite
{
	private String ImgCrate = "examples/draganddrop/pack:crate";
	
	private boolean _pressed;
	
	public Crate(float x, float y)
	{
		super(x, y);
		loadGraphic(ImgCrate);
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
		if(overlapsPoint(new FlxPoint(FlxG.mouse.x, FlxG.mouse.y)) && FlxG.mouse.pressed())
			_pressed = true;
		
		// Release the sprite
		if(FlxG.mouse.justReleased()) // OR if(!Gdx.input.isTouched() && _pressed)
			_pressed = false;
		
		// Drag the sprite
		if(_pressed)
		{
			x = FlxG.mouse.x - width/2;
			y = FlxG.mouse.y - height/2;
		}
		if(isTouching(FLOOR) && !_pressed)
		{
			immovable = true;
			acceleration.y = velocity.y = 0;
		}
		
		super.update();
	}
}
