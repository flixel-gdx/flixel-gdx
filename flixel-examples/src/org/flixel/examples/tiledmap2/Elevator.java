package org.flixel.examples.tiledmap2;

import org.flixel.FlxSprite;

public class Elevator extends FlxSprite
{
	private float _y;
	private float _height;

	public Elevator(float X, float Y, float Height)
	{
		super(X, Y, Asset.ImgElevator);
		width = 48;			//Minor bounding box adjustment
		_y = Y;				//The starting height
		_height = Height;	//How far down to travel
		immovable = true;		//We want the elevator to be "solid" and not shift during collisions
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
