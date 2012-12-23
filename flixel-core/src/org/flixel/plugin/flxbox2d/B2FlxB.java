package org.flixel.plugin.flxbox2d;

import org.flixel.FlxBasic;
import org.flixel.plugin.flxbox2d.collision.shapes.B2FlxShape;

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
	 * A list for shapes and joints that didn't get killed due the world got locked.
	 */
	public static Array<FlxBasic> scheduledForRemoval;
	/**
	 * A list for shapes that didn't get inactive due the world got locked.
	 */
	public static Array<B2FlxShape> scheduledForInActive;
	/**
	 * A list for shapes that didn't get active due the world got locked.
	 */
	public static Array<B2FlxShape> scheduledForActive;
	/**
	 * A list for shapes that didn't get moved due the world got locked.
	 */
	public static Array<B2FlxShape> scheduledForMove;
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
		scheduledForInActive = new Array<B2FlxShape>();
		scheduledForActive = new Array<B2FlxShape>();
		scheduledForMove = new Array<B2FlxShape>();
		
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
		scheduledForRemoval.clear();
		scheduledForRemoval = null;
		scheduledForActive.clear();
		scheduledForActive = null;
		scheduledForInActive.clear();
		scheduledForInActive = null;
		scheduledForMove.clear();
		scheduledForMove = null;
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
	
	public static void addSafelyRemove(FlxBasic value)
	{
		int length = scheduledForRemoval.size;
		for(int i = 0; i < length; i++)
		{
			if(scheduledForRemoval.get(i) == value)
				return;
		}
		scheduledForRemoval.add(value);
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
	
	public static void addInActive(B2FlxShape value)
	{
		int length = scheduledForInActive.size;
		for(int i = 0; i < length; i++)
		{
			if(scheduledForInActive.get(i) == value)
				return;
		}
		scheduledForInActive.add(value);
	}
	
	static void safelyDeactivateBodies()
	{
		int length = scheduledForInActive.size;
		for(int i = 0; i < length; i++)
		{
			scheduledForInActive.get(i).setActive(false);
		}
		scheduledForInActive.clear();
	}

	public static void addActive(B2FlxShape value)
	{
		int length = scheduledForActive.size;
		for(int i = 0; i < length; i++)
		{
			if(scheduledForActive.get(i) == value)
				return;
		}
		scheduledForActive.add(value);
	}

	static void safelyActivateBodies()
	{
		int length = scheduledForActive.size;
		for(int i = 0; i < length; i++)
		{
			scheduledForActive.get(i).setActive(true);
		}
		scheduledForActive.clear();
	}
	
	public static void addMove(B2FlxShape value)
	{
		int length = scheduledForMove.size;
		for(int i = 0; i < length; i++)
		{
			if(scheduledForMove.get(i) == value)
				return;
		}
		scheduledForMove.add(value);
	}
	
	static void safelyMoveBodies()
	{
		int length = scheduledForMove.size;
		for(int i = 0; i < length; i++)
		{
			scheduledForMove.get(i).body.setTransform(scheduledForMove.get(i).position, scheduledForMove.get(i).angle);
		}
		scheduledForMove.clear();
	}
}
