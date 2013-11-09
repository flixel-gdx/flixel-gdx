package org.flixel.system.gdx.utils;

import com.badlogic.gdx.utils.Array;

/**
 * A pool that cycles through its contained items.
 * 
 * @author Thomas Weston
 */
public abstract class RotationPool <T>
{
	private Array<T> _items;
	private int _currentIndex;
	private int _maxIndex;
	
	public RotationPool(int size)
	{
		_items = new Array<T>(size);
		_maxIndex = size - 1;
		_currentIndex = 0;
	}
	
	public T obtain()
	{
		if(_currentIndex > _maxIndex)
			_currentIndex = 0;
		
		if(_currentIndex >= _items.size)
			_items.add(newObject());
		
		return _items.get(_currentIndex++);
	}
	
	protected abstract T newObject();
}
