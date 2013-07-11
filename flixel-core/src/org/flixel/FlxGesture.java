
package org.flixel;

import org.flixel.FlxG;
import org.flixel.event.IFlxGesture;
import org.flixel.plugin.GestureManager;

public class FlxGesture {

	public static final int DIRECTION_DOWN = 1;

	public static final int DIRECTION_UP = 2;

	public static final int DIRECTION_LEFT = 3;

	public static final int DIRECTION_RIGHT = 4;

	public static final int TAP = 5;

	public static final int DOUBLE_TAP = 6;

	public static final int LONG_PRESS = 7;

	protected IFlxGesture _callback;

	public FlxGesture () {
		_callback = null;
	}

	public FlxGesture start (IFlxGesture Callback) {
		GestureManager manager = getManager();
		if (manager != null) manager.add(this);

		_callback = Callback;
		return this;
	}

	public FlxGesture start () {
		return start(null);
	}

	public void stop () {
		GestureManager manager = getManager();
		if (manager != null) manager.remove(this);
	}

	public void callback (int Gesture) {
		if (_callback != null) _callback.callback(Gesture);
	}

	public void destroy () {
		_callback = null;
	}

	static public GestureManager getManager () {
		return (GestureManager)FlxG.getPlugin(GestureManager.class);
	}

}
