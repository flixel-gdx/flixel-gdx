package org.flixel;

/**
 * This is a simple particle class that extends the default behavior
 * of <code>FlxSprite</code> to have slightly more specialized behavior
 * common to many game scenarios.  You can override and extend this class
 * just like you would <code>FlxSprite</code>. While <code>FlxEmitter</code>
 * used to work with just any old sprite, it now requires a
 * <code>FlxParticle</code> based class.
 * 
 * @author Ka Wing Chin
 */
public class FlxParticle extends FlxSprite
{
	/**
	 * How long this particle lives before it disappears.
	 * NOTE: this is a maximum, not a minimum; the object
	 * could get recycled before its lifespan is up.
	 */
	public float lifespan;
	
	/**
	 * Determines how quickly the particles come to rest on the ground.
	 * Only used if the particle has gravity-like acceleration applied.
	 * @default 500
	 */
	public float friction;
	
	/**
	 * Instantiate a new particle.  Like <code>FlxSprite</code>, all meaningful creation
	 * happens during <code>loadGraphic()</code> or <code>makeGraphic()</code> or whatever.
	 */
	public FlxParticle()
	{
		super();
		lifespan = 0;
		friction = 500;
	}
	
	/**
	 * The particle's main update logic.  Basically it checks to see if it should
	 * be dead yet, and then has some special bounce behavior if there is some gravity on it.
	 */
	@Override
	public void update()
	{
		//lifespan behavior
		if(lifespan <= 0)
			return;
		lifespan -= FlxG.elapsed;
		if(lifespan <= 0)
			kill();
		
		//simpler bounce/spin behavior for now
		if(touching > 0)
		{
			if(angularVelocity != 0)
				angularVelocity = -angularVelocity;
		}
		if(acceleration.y > 0) //special behavior for particles with gravity
		{
			if((touching & FLOOR) > 0)
			{
				drag.x = friction;
				
				if(!((wasTouching & FLOOR) > 0))
				{
					if(velocity.y < -elasticity*10)
					{
						if(angularVelocity != 0)
							angularVelocity *= -elasticity;
					}
					else
					{
						velocity.y = 0;
						angularVelocity = 0;
					}
				}
			}
			else
				drag.x = 0;
		}
	}
	
	/**
	 * Triggered whenever this object is launched by a <code>FlxEmitter</code>.
	 * You can override this to add custom behavior like a sound or AI or something.
	 */
	public void onEmit()
	{
	}
}
