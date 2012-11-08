package org.flixel.plugin.flxbox2d.dynamics;

import org.flixel.plugin.flxbox2d.collision.shapes.B2FlxShape;
import org.flixel.plugin.flxbox2d.event.IB2FlxCollision;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Collision event for FlxBox2D collision handling.
 * 
 * @author Ka Wing Chin
 */
public class B2FlxCollision implements ContactListener
{
	private static Array<Integer> _beginIndices;
	private static Array<Integer> _preSolveIndices;
	private static Array<Integer> _postSolveIndices;
	private static Array<Integer> _endIndices;
	private static Array<IB2FlxCollision> _beginCallbacks;
	private static Array<IB2FlxCollision> _preSolveCallbacks;
	private static Array<IB2FlxCollision> _postSolveCallbacks;
	private static Array<IB2FlxCollision> _endCallbacks;

	/**
	 * Constructor
	 */
	public B2FlxCollision()
	{		
		_beginIndices = new Array<Integer>();
		_preSolveIndices = new Array<Integer>();
		_postSolveIndices = new Array<Integer>();
		_endIndices = new Array<Integer>();
		_beginCallbacks = new Array<IB2FlxCollision>();
		_preSolveCallbacks = new Array<IB2FlxCollision>();
		_postSolveCallbacks = new Array<IB2FlxCollision>();
		_endCallbacks = new Array<IB2FlxCollision>();
	}
	
	/**
	 * Add begin contact callback.
	 * @param callback	The callback that will be fired when begin contact event occurs.
	 * @param index		The group index.
	 */
	public static void addBeginContact(IB2FlxCollision callback, int index)
	{
		_beginCallbacks.add(callback);
		_beginIndices.add(index);
	}
	
	/**
	 * Add pre-solve callback.
	 * @param callback	The callback that will be fired when pre-solve event occurs.
	 * @param index		The group index.
	 */
	public static void addPreSolve(IB2FlxCollision callback, int index)
	{
		_preSolveCallbacks.add(callback);
		_preSolveIndices.add(index);
	}
	
	/**
	 * Add post-solve callback.
	 * @param callback	The callback that will be fired when post-solve event occurs.
	 * @param index		The group index.
	 */
	public static void addPostSolve(IB2FlxCollision callback, int index)
	{
		_postSolveCallbacks.add(callback);
		_postSolveIndices.add(index);
	}
	
	/**
	 * Add end contact callback.
	 * @param callback	The callback that will be fired when end contact event occurs.
	 * @param index		The group index.
	 */
	public static void addEndContact(IB2FlxCollision callback, int index)
	{
		_endCallbacks.add(callback);
		_endIndices.add(index);
	}

	/** 
	 * Internal: Called when two fixtures begin to touch.
	 */
	@Override
	public void beginContact(Contact contact)
	{
		collide(contact, _beginCallbacks, _beginIndices);
	}

	/** 
	 * Internal: Called when two fixtures cease to touch. 
	 */
	@Override
	public void endContact(Contact contact)
	{
		collide(contact, _endCallbacks, _endIndices);
	}

	/**
	 * Internal: This is called after a contact is updated. This allows you to inspect a contact before it goes to the solver. If you are
	 * careful, you can modify the contact manifold (e.g. disable contact). A copy of the old manifold is provided so that you can
	 * detect changes. Note: this is called only for awake bodies. Note: this is called even when the number of contact points is
	 * zero. Note: this is not called for sensors. Note: if you set the number of contact points to zero, you will not get an
	 * EndContact callback. However, you may get a BeginContact callback the next step.
	 */
	@Override
	public void preSolve(Contact contact, Manifold oldManifold)
	{
		collide(contact , _preSolveCallbacks, _preSolveIndices);
	}

	/**
	 * Internal: This lets you inspect a contact after the solver is finished. This is useful for inspecting impulses. Note: the contact
	 * manifold does not include time of impact impulses, which can be arbitrarily large if the sub-step is small. Hence the
	 * impulse is provided explicitly in a separate data structure. Note: this is only called for contacts that are touching,
	 * solid, and awake.
	 */
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse)
	{
		collide(contact, _postSolveCallbacks, _postSolveIndices);
	}
	
	/**
	 * A callback will be fired when the fixtures are in the same group.
	 * @param contact	Shapes that contacted each other.
	 * @param callbacks	Callbacks that will be fired.
	 * @param indices	The group index.
	 */
	@SuppressWarnings("unchecked")
	private void collide(Contact contact, Array<IB2FlxCollision> callbacks, Array<Integer> indices)
	{
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		Filter filter1 = fixtureA.getFilterData();
		Filter filter2 = fixtureB.getFilterData();
		
		int index;
		int length = indices.size;		
		B2FlxShape shapeA = (B2FlxShape) ((ObjectMap<String, Object>) fixtureA.getBody().getUserData()).get("shape");
		B2FlxShape shapeB = (B2FlxShape) ((ObjectMap<String, Object>) fixtureB.getBody().getUserData()).get("shape");
		for(int i = 0; i < length; i++)
		{			
			index = indices.get(i);
			if(filter1.groupIndex == index && filter2.groupIndex == index)
			{				
				Vector2[] points = contact.getWorldManifold().getPoints();			
				Vector2[] p = new Vector2[contact.getWorldManifold().getNumberOfContactPoints()];
				for(int j = 0; j < p.length-1; j++)
					p[j] = points[j];
				callbacks.get(i).callback(shapeA, shapeB, p, contact);
			}
		}
				
		if(filter1.groupIndex == filter2.groupIndex && filter1.groupIndex != 0)
		{
			if(filter1.groupIndex > 0)
			{
				// TODO: callback by group index > 0
//				FlxG.log("group index collide > 0");
			}
		}
		else
		{
			if((filter1.maskBits & filter2.categoryBits) != 0 && (filter1.categoryBits & filter2.maskBits) != 0)
			{
				// TODO: callback by category and mask.
//				FlxG.log("category and mask collide");
			}
		}
	}
	
	/**
	 * Clean up memory.
	 */
	public void destroy()
	{
		_beginIndices.clear();
		_beginIndices = null;
		_preSolveIndices.clear();
		_preSolveIndices = null;
		_postSolveIndices.clear();
		_postSolveIndices = null;
		_endIndices.clear();
		_endIndices = null;
		
		_beginCallbacks.clear();
		_beginCallbacks = null;
		_preSolveCallbacks.clear();
		_preSolveCallbacks = null;		
		_postSolveCallbacks.clear();
		_postSolveCallbacks = null;
		_endCallbacks.clear();
		_endCallbacks = null;		
	}
}
