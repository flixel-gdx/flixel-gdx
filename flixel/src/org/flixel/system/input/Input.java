package org.flixel.system.input;

import gnu.trove.map.hash.TIntObjectHashMap;

import com.badlogic.gdx.InputAdapter;

/**
 * Keeps track of what keys are pressed and how with handy booleans or strings.
 * 
 * @author Ka Wing Chin
 */
public class Input extends InputAdapter
{
	/**
	 * @private
	 */
	int[] _lookup;
	/**
	 * @private
	 */
	TIntObjectHashMap<KeyboardData> _map;
	/**
	 * Helper variable for tracking whether a key was just pressed or just
	 * released.
	 */
	protected int _last;
	/**
	 * @private
	 */
	final int _total = 256;

	/**
	 * Constructor
	 */
	public Input()
	{
		_lookup = new int[_total];
		_map = new TIntObjectHashMap<KeyboardData>(_total);	
	}
	
	
	/**
	 * Updates the key states (for tracking just pressed, just released, etc).
	 */
	public void update()
	{
		for(KeyboardData o : _map.valueCollection())
		{
			if((o.last == -1) && (o.current == -1))
				o.current = 0;
			else if((o.last == 2) && (o.current == 2))
				o.current = 1;
			o.last = o.current;
		}
	}
	
	
	/**
	 * Resets all the keys.
	 */
	public void reset()
	{
		for(KeyboardData data : _map.valueCollection())
		{
			data.current = 0;
			data.last = 0;
		}
	}
	
	
	/**
	 * Check to see if this key is pressed.
	 * 
	 * @param Key One of the key constants listed above (e.g. "LEFT" or "A").
	 * 
	 * @return Whether the key is pressed
	 */
	public boolean pressed(int Key)
	{
		if(_lookup[Key] > 0)
			return _map.get(_lookup[Key]).current == 1;
		return false;
	}
	
	
	/**
	 * Check to see if this key was just pressed.
	 * 
	 * @param Key One of the key constants listed above (e.g. "LEFT" or "A").
	 * 
	 * @return Whether the key was just pressed
	 */
	public boolean justPressed(int Key)
	{		
		if(_lookup[Key] > 0)
			return _map.get(_lookup[Key]).current == 2;
		return false;
	}

	
	/**
	 * Check to see if this key is just released.
	 * 
	 * @param Key One of the key constants listed above (e.g. "LEFT" or "A").
	 * 
	 * @return Whether the key is just released.
	 */
	public boolean justReleased(int Key)
	{
		if(_lookup[Key] > 0)
			return _map.get(_lookup[Key]).current == -1;
		return false;
	}

	
	/**
	 * An internal helper function used to build the key array.
	 * 
	 * @param KeyName String name of the key (e.g. "LEFT" or "A")
	 * @param KeyCode The numeric Flash code for this key.
	 */
	protected void addKey(int KeyCode)
	{
		_lookup[KeyCode] = KeyCode;
		_map.put(KeyCode, new KeyboardData());
		KeyboardData o = _map.get(KeyCode);
		o.current = 0;
		o.last = 0;
	}
	
	public class KeyboardData
	{
		public int current = 0;
		public int last = 0;
	}
}

