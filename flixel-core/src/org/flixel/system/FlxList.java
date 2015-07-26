package org.flixel.system;

import org.flixel.FlxObject;

import com.badlogic.gdx.utils.Pool;

/**
 * A miniature linked list class.
 * Useful for optimizing time-critical or highly repetitive tasks!
 * See <code>FlxQuadTree</code> for how to use it, IF YOU DARE.
 * 
 * @author Ka Wing Chin
 */
public class FlxList
{
	/**
	 * Stores a reference to a <code>FlxObject</code>.
	 */
	public FlxObject object;
	/**
	 * Stores a reference to the next link in the list.
	 */
	public FlxList next;

	/**
	 * Internal, a pool of <code>FlxQuadTree</code>s to prevent constant <code>new</code> calls.
	 */
	static private Pool<FlxList> _pool = new Pool<FlxList>()
	{
		@Override
		protected FlxList newObject()
		{
			return new FlxList();
		}
	};

	/**
	 * Gets a new <code>FlxList</code> from the pool.
	 * 
	 * @return	A new <code>FlxList</code>.
	 */
	static public FlxList getNew()
	{
		return _pool.obtain();
	}

	/**
	 * Creates a new link, and sets <code>object</code> and <code>next</code> to <code>null</code>.
	 */
	public FlxList()
	{
		object = null;
		next = null;
	}

	/**
	 * Clean up memory.
	 */
	public void destroy()
	{
		object = null;
		if(next != null)
			next.destroy();
		next = null;

		_pool.free(this);
	}
}
