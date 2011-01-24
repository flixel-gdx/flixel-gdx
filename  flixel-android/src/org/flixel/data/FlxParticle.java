package org.flixel.data;

import org.flixel.FlxObject;
import org.flixel.FlxSprite;

public class FlxParticle extends FlxSprite
{
	protected float _bounce;

	public FlxParticle(float Bounce)
	{
		super();
		_bounce = Bounce;
	}
	
	@Override
	public void hitSide(FlxObject Contact, float Velocity)
	{			
		velocity.x = -velocity.x * _bounce;
		if(angularVelocity != 0)
			angularVelocity = -angularVelocity * _bounce;
	}

	

	@Override
	public void hitBottom(FlxObject Contact, float Velocity)
	{
		onFloor = true;
		if(((velocity.y > 0) ? velocity.y : -velocity.y) > _bounce * 100)
		{
			velocity.y = -velocity.y * _bounce;
			if(angularVelocity != 0)
				angularVelocity *= -_bounce;
		}
		else
		{
			angularVelocity = 0;
			super.hitBottom(Contact, Velocity);
		}
		velocity.x *= _bounce;
	}
}
