package org.flixel.plugin.flxbox2d;

import java.util.Iterator;

import org.flixel.FlxBasic;
import org.flixel.FlxG;
import org.flixel.plugin.flxbox2d.collision.shapes.B2FlxShape;
import org.flixel.plugin.flxbox2d.dynamics.joints.B2FlxJoint;
import org.flixel.plugin.flxbox2d.managers.B2FlxContactManager;
import org.flixel.plugin.flxbox2d.system.debug.B2FlxDebug;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Joint;
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
	 * The contact manager.
	 */
	public static B2FlxContactManager contact;
	/**
	 * A list for shapes and joints that didn't get killed due the world got locked.
	 */
	public static Array<FlxBasic> scheduledForRemoval;
	/**
	 * A list of shapes and joints that didn't get revived due the world got locked.
	 */
	public static Array<FlxBasic> scheduledForRevival;
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
	 * An array of joints.
	 */
	public static Array<B2FlxJoint> joints;
	/**
	 * Internal, track wether to use the debugger or not.
	 */
	private static boolean _drawDebug;
		
	/**
	 * Called by <code>B2FlxState</code> to setup the vertices.
	 */
	public static void init()
	{	
		scheduledForRemoval = new Array<FlxBasic>();
		scheduledForRevival = new Array<FlxBasic>();
		scheduledForInActive = new Array<B2FlxShape>();
		scheduledForActive = new Array<B2FlxShape>();
		scheduledForMove = new Array<B2FlxShape>();
		
		if(vertices == null)
		{
			vertices = new Vector2[1000];
			for(int i = 0; i < vertices.length; i++)
				vertices[i] = new Vector2();
		}
		
		joints = new Array<B2FlxJoint>();
	}
	
	/**
	 * Clean up memory.
	 */
	public static void destroy()
	{
		for(int i = 0; i < joints.size; i++)
		{
			joints.get(i).destroy();
		}
		joints.clear();
		joints = null;
		
		Iterator<Body> bodies = world.getBodies();
		while(bodies.hasNext())
		{
			Body body = bodies.next();
			if(body != null)
				world.destroyBody(body);
		}
		
		Iterator<Joint> joints = world.getJoints();
		while(joints.hasNext())
		{
			Joint joint = joints.next();
			if(joint != null)
				world.destroyJoint(joint);
		}
		world.dispose();
		world = null;
		contact.destroy();
		contact = null;
		vertices = null;
		if(_drawDebug)
		{
			FlxG.getPlugin(B2FlxDebug.class).destroy();
			FlxG.removePluginType(B2FlxDebug.class);			
			_drawDebug = false;
		}
		scheduledForRemoval.clear();
		scheduledForRemoval = null;
		scheduledForActive.clear();
		scheduledForActive = null;
		scheduledForInActive.clear();
		scheduledForInActive = null;
		scheduledForMove.clear();
		scheduledForMove = null;
	}
	
	public static void initDebugger()
	{
		if(_drawDebug)
			return;
		_drawDebug = true;
		FlxG.addPlugin(new B2FlxDebug());
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
	 * Shapes or joints that didn't got removed will be scheduled.
	 * @param value		The shape or joint that needs to be removed.
	 */
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
	 * Internal: Safely remove bodies which couldn't be deleted by calling kill().
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
	
	/**
	 * Shapes or joints that didn't got revived will be scheduled.
	 * @param value		The shape or joint that needs to be revived.
	 */
	public static void addSafelyRevive(FlxBasic value)
	{
		int length = scheduledForRevival.size;
		for(int i = 0; i < length; i++)
		{
			if(scheduledForRevival.get(i) == value)
				return;
		}
		scheduledForRevival.add(value);
	}

	/**
	 * Internal: Safely revive bodies which couldn't be revived by calling revive().
	 * This will be called after World::step().
	 */
	static void safelyReviveBodies()
	{
		int length = scheduledForRevival.size;
		for(int i = 0; i < length; i++)
		{
			scheduledForRevival.get(i).revive();
		}
		scheduledForRevival.clear();
	}
	
	/**
	 * Shapes that didn't got deactivated will be scheduled.
	 * @param value		The shape that needs to be deactivated.
	 */
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
	
	/**
	 * Internal: Safely deactivate bodies which couldn't be deactivated by calling setActive(false).
	 * This will be called after World::step().
	 */
	static void safelyDeactivateBodies()
	{
		int length = scheduledForInActive.size;
		for(int i = 0; i < length; i++)
		{
			scheduledForInActive.get(i).setActive(false);
		}
		scheduledForInActive.clear();
	}

	/**
	 * Shapes that didn't got activated will be scheduled.
	 * @param value		The shape that needs to be activated.
	 */
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

	/**
	 * Internal: Safely activate bodies which couldn't be activated by calling setActive(true).
	 * This will be called after World::step().
	 */
	static void safelyActivateBodies()
	{
		int length = scheduledForActive.size;
		for(int i = 0; i < length; i++)
		{
			scheduledForActive.get(i).setActive(true);
		}
		scheduledForActive.clear();
	}
	
	/**
	 * Shapes that didn't got moved will be scheduled.
	 * @param value		The shape that needs to be moved.
	 */
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
	
	/**
	 * Internal: Safely move bodies which couldn't be moved during the revive().
	 * This will be called after World::step().
	 */
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
