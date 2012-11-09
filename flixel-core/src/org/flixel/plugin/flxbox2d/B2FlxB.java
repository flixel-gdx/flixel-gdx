package org.flixel.plugin.flxbox2d;

import org.flixel.FlxBasic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

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
	 * A list for shapes that didn't got killed due the world got locked.
	 */
	public static Array<FlxBasic> scheduledForRemoval;
	/**
	 * Vertices for polygon rendering.
	 */
	public static Vector2[] vertices;
	
	
	/**
	 * Called by <code>B2FlxState</code> to setup the vertices.
	 */
	public static void init()
	{
		scheduledForRemoval = new Array<FlxBasic>();
		
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
		destroyBodies();
		scheduledForRemoval.clear();
		scheduledForRemoval = null;
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

	/**
	 * Internal: Safely remove bodies which couldn't deleted by calling kill().
	 * This will be called after World::step().
	 */
	static void safelyRemoveBodies()
	{
		int length = scheduledForRemoval.size;
		for(int i = 0; i < length; i++)
		{
			scheduledForRemoval.get(i).kill();
		}
		scheduledForRemoval.clear();
	}
	
	private static void destroyBodies()
	{
		int length = scheduledForRemoval.size;
		for(int i = 0; i < length; i++)
		{
			scheduledForRemoval.get(i).destroy();
		}
		scheduledForRemoval.clear();
	}
}
