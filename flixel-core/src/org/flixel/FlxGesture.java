package org.flixel;

import org.flixel.event.IFlxGesture;
import org.flixel.plugin.GestureManager;
import org.flixel.plugin.GestureManager.GestureData;

/**
 * A gesture class. By performing a gesture a callback will be fired.
 * Not intended to be added to a game state or group;
 * the <code>GestureManager</code> is responsible for actually calling update(), not the user.
 * 
 * @author cyphertext
 * @author Ka Wing Chin
 */
public class FlxGesture
{
	/**
	 * Drag a finger over the screen.
	 */
	static public final int PAN = 0;
	/**
	 * Drag a finger downwards and lift it.
	 */
	static public final int DIRECTION_DOWN = 1;
	/**
	 * Drag a finger upwards and lift it.
	 */
	static public final int DIRECTION_UP = 2;
	/**
	 * Drag a finger leftwards and lift it.
	 */
	static public final int DIRECTION_LEFT = 3;
	/**
	 * Drag a finger rightwards and lift it.
	 */
	static public final int DIRECTION_RIGHT = 4;
	/**
	 * Tap on the screen and lift it without moving outside of the tap square.
	 */
	static public final int TAP = 5;
	/**
	 * Tap twice on the screen and lift it without moving outside of the tap square.
	 */
	static public final int DOUBLE_TAP = 6;
	/**
	 * Tap on screen, hold it for a couple of seconds and lift it.
	 */
	static public final int LONG_PRESS = 7;
	/**
	 * Perform a pinch open gesture where the fingers moves away.
	 */
	static public final int ZOOM = 8;
	/**
	 * Perform a pinch close gesture where the fingers moves closer.
	 */
	static public final int PINCH = 9;
	/**
	 * Finger went down on the screen or a mouse button was pressed.
	 */
	static public final int TOUCH_DOWN = 10;
	/**
	 * The callback will be called when a gesture is performed.
	 */
	protected IFlxGesture _callback;

	/**
	 * Instantiate the <code>FlxGesture</code>. Does not activate the gesture.
	 * To activate, call FlxGesture::start().
	 */
	public FlxGesture()
	{

	}

	/**
	 * Make this <code>FlxGesture</code> object active in the manager.
	 * 
	 * @param	Callback	The callback that will be fired when a gesture is performed.
	 * 
	 * @return	The <code>FlxGesture</code> object that got activated.
	 */
	public FlxGesture start(IFlxGesture Callback)
	{
		GestureManager manager = getManager();
		if(manager != null)
			manager.add(this);
		else
		{
			FlxG.log("WARNING: Did you forget to plug-in GestureManager?");
			return null;
		}

		_callback = Callback;
		return this;
	}

	/**
	 * Make this <code>FlxGesture</code> object active in the manager.
	 * 
	 * @return	The <code>FlxGesture</code> object that got activated.
	 */
	public FlxGesture start()
	{
		return start(null);
	}

	/**
	 * Removes <code>FlxGesture</code> object from the manager.
	 */
	public void stop()
	{
		GestureManager manager = getManager();
		if(manager != null)
			manager.remove(this);
	}

	/**
	 * Fires callback by gesture type, e.g. <code>FlxGesture.PAN, FlxGesture.DIRECTION_DOWN, etc.</code>.
	 * 
	 * @param	Gesture	The gesture type that needs to be updated.
	 */
	public void callback(int Gesture, GestureData data)
	{
		if(_callback != null)
			_callback.callback(Gesture, data);
	}

	/**
	 * Clean up memory.
	 */
	public void destroy()
	{
		_callback = null;
	}

	/**
	 * Get the <code>GestureManager</code> object that is used by <code>FlxGesture</code>.
	 * 
	 * @return	The <code>GestureManager</code> object.
	 */
	static public GestureManager getManager()
	{
		return (GestureManager) FlxG.getPlugin(GestureManager.class);
	}
}
