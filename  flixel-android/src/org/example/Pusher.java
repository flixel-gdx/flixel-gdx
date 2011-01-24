package org.example;

import org.flixel.FlxSprite;
import org.flixel.data.R;

public class Pusher extends FlxSprite
{
	protected int _x;
	protected int _width;
	
	public Pusher(int X, float Y, int Width)
	{
		super(X, Y, R.drawable.pusher);
		_x = X;				//The starting height
		_width = Width;		//How far over to travel
		setFixed(true);		//We want the pusher to be "solid" and not shift during collisions
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
