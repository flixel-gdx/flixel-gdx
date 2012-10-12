package org.flixel.plugin.flxbox2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class B2FlxB
{
	// The ratio from meters to pixels.
	public static final float RATIO = 32f;
	// The world where the object lives.
	public static World world;
	
	/**
	 * Clean up memory.
	 */
	public static void destroy()
	{
		world = null;
	}

	/**
	 * Creates a static body.
	 * @param position	The position of the body.
	 * @return	The static body.
	 */
	public static Body getGroundBody(Vector2 position)
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(position);
		Body body = world.createBody(bodyDef);
		return body;
	}
	
	/**
	 * Creates a static body.
	 * @return	The static body.
	 */
	public static Body getGroundBody()
	{
		return getGroundBody(new Vector2());
	}
}
