package org.flixel.plugin.flxbox2d.plugin;

import java.util.Iterator;

import org.flixel.FlxBasic;
import org.flixel.FlxGroup;
import org.flixel.plugin.flxbox2d.B2FlxB;
import org.flixel.plugin.flxbox2d.collision.shapes.B2FlxShape;
import org.flixel.plugin.flxbox2d.common.math.B2FlxMath;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Cause an explosion force at a point.
 * This plugin is based on Reynaldo Columna's PhysInjector.
 * 
 * @author Ka Wing Chin
 */
public class ExplosionPlugin
{
	/**
	 * Cache, the position of the explosion.
	 */
	private final Vector2 _expOrigen = new Vector2();
	/**
	 * Cache, the impulse that will be applied on the body.
	 */
	private final Vector2 _impulse = new Vector2();
	
	/**
	 * Create an explosion at a given point.
	 * @param x				The x-position of the explosion.
	 * @param y				The y-position of the explosion.
	 * @param coverage		The coverage of the explosion.
	 * @param impulseForce	The force of the impulse.
	 * @param value			The shape or group which the force will applied to.
	 */
	public void create(float x, float y, float coverage, float impulseForce, FlxBasic value)
	{		
		if(value != null)
		{
			FlxBasic basic;
			if(value instanceof FlxGroup)
			{
				int length = ((FlxGroup)value).length;
				int i = 0;
				while(i < length)
				{
					basic = ((FlxGroup)value).members.get(i);
					create(x, y, coverage, impulseForce, basic);
					i++;
				}
			}
			else if(value instanceof B2FlxShape)
			{			
				applyExplosion(((B2FlxShape)value).body, x, y, coverage, impulseForce);
			}			
		}
		else
		{
			Iterator<Body> bodies = B2FlxB.world.getBodies();		
			while(bodies.hasNext())
			{
				applyExplosion(bodies.next(), x, y, coverage, impulseForce);
			}			
		}		
	}
	
	/**
	 * Create an explosion at a given point. All shapes within range will be affected.
	 * @param x				The x-position of the explosion.
	 * @param y				The y-position of the explosion.
	 * @param coverage		The coverage of the explosion.
	 * @param impulseForce	The force of the impulse.
	 */ 
	public void create(float x, float y, float coverage, float impulseForce)
	{
		create(x, y, coverage, impulseForce, null);
	}
	
	/**
	 * Apply the impulse to the body.
	 */
	private void applyExplosion(Body body, float x, float y, float coverage, float impulseForce)
	{
		_expOrigen.set(x / B2FlxB.RATIO, y / B2FlxB.RATIO);		
		Vector2 bodyPosition = body.getPosition();
		
		float maxDistance = coverage / B2FlxB.RATIO;		
		float distance = B2FlxMath.distance(bodyPosition, _expOrigen);
		if(distance > maxDistance)
			distance = maxDistance - 0.01f;

		float strength = (maxDistance - distance) / maxDistance;
		float force = strength * impulseForce;
		float angle = (float) Math.atan2(bodyPosition.y - _expOrigen.y, bodyPosition.x - _expOrigen.x);
		
		_impulse.set(MathUtils.cos(angle) * force, MathUtils.sin(angle) * force);
		body.applyLinearImpulse(_impulse, bodyPosition, true);
	}
}

