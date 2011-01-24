package org.flixel.data;

import java.util.Hashtable;

public class FlxInput
{
	/**
	 * @private
	 */
	private Hashtable<String, Integer> _lookup;
	/**
	 * @private
	 */
	private Hashtable<Integer, FlxKeyboardData> _map;
	/**
	 * @private
	 */
	//private final int _t = 256;

	/**
	 * Helper variable for tracking whether a key was just pressed or just
	 * released.
	 */
	protected int _last;
	/**
	 * Helper variable for tracking whether a key was just pressed or just
	 * released.
	 */
	protected int _current;

	/**
	 * Constructor
	 */
	public FlxInput()
	{
		_lookup = new Hashtable<String, Integer>();
		_map = new Hashtable<Integer, FlxKeyboardData>();
	}

	/**
	 * Updates the key states (for tracking just pressed, just released, etc).
	 */
	public void update()
	{
		for(FlxKeyboardData o : _map.values())
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
		for(FlxKeyboardData data : _map.values())
		{
			data.name = "";
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
	public boolean pressed(String Key)
	{
		if(_lookup.containsKey(Key))
			return _map.get(_lookup.get(Key)).current == 1;
		return false;
	}

	/**
	 * Check to see if this key was just pressed.
	 * 
	 * @param Key One of the key constants listed above (e.g. "LEFT" or "A").
	 * 
	 * @return Whether the key was just pressed
	 */
	public boolean justPressed(String Key)
	{		
		if(_lookup.containsKey(Key))
			return _map.get(_lookup.get(Key)).current == 2;
		return false;
	}

	/**
	 * Check to see if this key is just released.
	 * 
	 * @param Key One of the key constants listed above (e.g. "LEFT" or "A").
	 * 
	 * @return Whether the key is just released.
	 */
	public boolean justReleased(String Key)
	{
		if(_lookup.containsKey(Key))
			return _map.get(_lookup.get(Key)).current == -1;
		return false;
	}

	/**
	 * Event handler so FlxGame can toggle keys.
	 * 
	 * @param event A <code>KeyboardEvent</code> object.
	 */
	public void handleKeyDown(int KeyCode)
	{
		FlxKeyboardData o = _map.get(KeyCode);
		if(o == null)
			return;
		
		if(o.current > 0)
			o.current = 1;
		else
			o.current = 2;
		// o.name = true;

	}

	/**
	 * Event handler so FlxGame can toggle keys.
	 * 
	 * @param event A <code>KeyboardEvent</code> object.
	 */
	public void handleKeyUp(int KeyCode)
	{
		FlxKeyboardData o = _map.get(KeyCode);
		if(o == null)
			return;
		if(o.current > 0)
			o.current = -1;
		else
			o.current = 0;
		// o.name = false;
	}

	/**
	 * An internal helper function used to build the key array.
	 * 
	 * @param KeyName String name of the key (e.g. "LEFT" or "A")
	 * @param KeyCode The numeric Flash code for this key.
	 */
	protected void addKey(String KeyName, int KeyCode)
	{
		_lookup.put(KeyName, KeyCode);
		_map.put(KeyCode, new FlxKeyboardData());
		FlxKeyboardData o = _map.get(KeyCode);
		o.name = KeyName;
		o.current = 0;
		o.last = 0;
	}

}
