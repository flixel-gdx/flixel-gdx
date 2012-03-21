package org.flixel.system.input;

import org.flixel.FlxG;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectIntMap;

/**
 * Keeps track of what keys are pressed and how with handy booleans or strings.
 * 
 * @author Ka Wing Chin
 */
public class Input
{
	private ObjectIntMap<String> _lookup;
	
	private final int _total = 256;
	
	protected Array<KeyState> _map;
	/**
	 * Helper variable for tracking whether a key was just pressed or just
	 * released.
	 */
	protected int _last;

	/**
	 * Constructor
	 */
	public Input()
	{
		_lookup = new ObjectIntMap<String>(_total);
		_map = new Array<KeyState>(_total);
		for (int i = 0; i < _total; ++i)
			_map.add(new KeyState("", 0, 0));
	}
	
	/**
	 * Updates the key states (for tracking just pressed, just released, etc).
	 */
	public void update()
	{
		for(KeyState o : _map)
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
		for(KeyState o : _map)
		{
			if (o.name.isEmpty())
				continue;
			
			try {
				Keyboard.class.getField(o.name).setBoolean(this, false);
			} catch (Exception e) {
				FlxG.log("Keyboard", e.getMessage());
			}
			o.current = 0;
			o.last = 0;
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
		if(Key > 0)
			return _map.get(Key).current == 1;
		return false;
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
		return pressed(_lookup.get(Key, 0));
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
		if(Key > 0)
			return _map.get(Key).current == 2;
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
		return justPressed(_lookup.get(Key, 0));
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
		if(Key > 0)
			return _map.get(Key).current == -1;
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
		return justReleased(_lookup.get(Key, 0));
	}

	/**
	 * If any keys are not "released" (0),
	 * this function will return an array indicating
	 * which keys are pressed and what state they are in.
	 * 
	 * @return	An array of key state data.  Null if there is no data.
	 */
	public Array<KeyData> record()
	{
		Array<KeyData> data = new Array<KeyData>();
		int i = 0;
		while(i < _total)
		{
			KeyState o = _map.get(i++);
			if((o == null) || (o.current == 0))
				continue;
			data.add(new KeyData(i-1, o.current));
		}
		return data.size > 0 ? data: null;
	}
	
	/**
	 * Part of the keystroke recording system.
	 * Takes data about key presses and sets it into array.
	 * 
	 * @param	Record	Array of data about key states.
	 */
	public void playback(Array<KeyData> Record)
	{
		int i = 0;
		int l = Record.size;
		KeyData o;
		KeyState o2;
		while(i < l)
		{
			o = Record.get(i++);
			o2 = _map.get(o.code);
			o2.current = o.value;
			if(o.value > 0)
				try {
					Keyboard.class.getField(o2.name).setBoolean(this, true);
				} catch (Exception e) {
					FlxG.log("Input", e.getMessage());
				}
		}
	}
	
	/**
	 * Look up the key code for any given string name of the key or button.
	 * 
	 * @param	KeyName		The <code>String</code> name of the key.
	 * 
	 * @return	The key code for that key.
	 */
	public int getKeyCode(String KeyName)
	{
		return _lookup.get(KeyName, 0);
	}
	
	/**
	 * Check to see if any keys are pressed right now.
	 * 
	 * @return	Whether any keys are currently pressed.
	 */
	public boolean any()
	{
		for (KeyState o : _map)
		{
			if(o.current > 0)
				return true;
		}
		return false;
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
		_map.set(KeyCode, new KeyState(KeyName, 0, 0));
	}
	
	/**
	 * Clean up memory.
	 */
	public void destroy()
	{
		_lookup = null;
		_map = null;
	}
	
	public class KeyState
	{
		public KeyState(String Name, int Current, int Last){name = Name; current = Current; last = Last;}
		public String name;
		public int current;
		public int last;
	}
	
	static public class KeyData
	{
		public KeyData(int Code, int Value){code = Code; value = Value;}
		public int code;
		public int value;
	}
}

