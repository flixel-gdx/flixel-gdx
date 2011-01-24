package org.example;

import org.flixel.FlxSprite;
import org.flixel.data.R;

public class Elevator extends FlxSprite
{
	protected float _y;
	protected int _height;
	
	public Elevator(float X, float Y, int Height)
	{
		super(X, Y, R.drawable.elevator);
		width = 48;			//Minor bounding box adjustment
		_y = Y;				//The starting height
		_height = Height;	//How far down to travel
		setFixed(true);		//We want the elevator to be "solid" and not shift during collisions
		velocity.y = 40;	//Basic elevator speed
	}
	
	@Override
	public void update()
	{
		//Update the elevator's motion
		super.update();
		
		//Turn around if necessary
		if(y > _y + _height)
		{
			y = _y + _height;
			velocity.y = -velocity.y;
		}
		else if(y < _y)
		{
			y = _y; 
			velocity.y = -velocity.y;
		}
	}
}
