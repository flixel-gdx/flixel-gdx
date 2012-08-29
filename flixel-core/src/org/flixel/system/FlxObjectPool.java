package org.flixel.system;

import com.badlogic.gdx.utils.Array;

/**
 * A very basic object pool. Used by <code>FlxQuadTree</code> and
 * <code>FlxList</code> to avoid costly instantiations every frame.
 * 
 * @author Thomas Weston
 *
 * @param <T>	The type of object to store in this pool.
 */
public abstract class FlxObjectPool<T> 
{
	/**
	 * Internal, stores the pooled objects.
	 */
	protected Array<T> _pool;
	
	/**
	 * Constructor.
	 * 
	 * @param StartSize		How many objects to initially create. Optional.
	 */
	public FlxObjectPool(int StartSize)
	{
		_pool = new Array<T>(StartSize);
		
		int i = 0;
		while (i < StartSize)
		{
			_pool.add(create());
		}
	}
	
	/**
	 * Constructor.
	 */
	public FlxObjectPool()
	{
		this(0);
	}
	
	/**
	 * Put an object back in the pool.
	 * 
	 * @param Object	The object to pool.
	 */
	public void dispose(T Object)
	{
		_pool.add(Object);
	}
	
	/**
	 * Gets an object from the pool. If the pool is empty, returns
	 * a new object.
	 * 
	 * @return	A new object.
	 */
	public T getNew()
	{
		T object = null;
		if (_pool.size > 0)
			object = _pool.pop();
		else
			object = create();
		return object;
	}
	
	/**
	 * Instantiates a new object.
	 * 
	 * @return A new object.
	 */
	protected abstract T create();
}