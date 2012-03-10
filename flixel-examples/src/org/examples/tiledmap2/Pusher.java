package org.examples.tiledmap2;

import org.flixel.FlxSprite;

public class Pusher extends FlxSprite
{
	private float _x;
	private float _width;

	public Pusher(float X, float Y, float Width)
	{
		super(X, Y, Asset.ImgPusher);
		_x = X;				//The starting height								
		_width = Width;		//How far over to travel		
		immovable = true;	//We want the pusher to be "solid" and not shift during collisions		
		velocity.x = 40;	//Basic pusher speed
		
	}
	
	
	@Override
	public void update()
	{
		//Update the elevator's motion
		super.update();
		
		//Turn around if necessary
		if(x > _x + _width)
		{
			x = _x + _width;
			velocity.x = -velocity.x;
		}
		else if(x < _x)
		{
			x = _x;
			velocity.x = -velocity.x;
		}
	}
}
