package org.flixel.plugin.flxbox2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * This is a global helper class for Box2D.
 * @author Ka Wing Chin
 */
public class B2FlxB
{
	/** 
	 * The ratio from meters to pixels. 
	 */
	public static final float RATIO = 32f;
	/**
	 * The world where the object lives.
	 */
	public static World world;
	/**
	 * Vertices for polygon rendering.
	 */
	public static Vector2[] vertices;
	
	/**
	 * Called by <code>B2FlxState</code> to setup the vertices.
	 */
	public static void init()
	{
		if(vertices == null)
		{
			vertices = new Vector2[1000];
			for(int i = 0; i < vertices.length; i++)
				vertices[i] = new Vector2();
		}
	}
	
	/**
	 * Clean up memory.
	 */
	public static void destroy()
	{
		world = null;
		vertices = null;
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
