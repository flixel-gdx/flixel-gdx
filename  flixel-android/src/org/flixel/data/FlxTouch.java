package org.flixel.data;

import android.view.MotionEvent;



public class FlxTouch
{
	/**
	 * Current X position of the touch pointer in the game world.
	 */
	public float x;
	/**
	 * Current Y position of the touch in the game world.
	 */
	public float y;
	/**
	 * Helper variable for tracking whether the screen was just pressed or just released.
	 */
	protected int _last;
	/**
	 * Helper variable for tracking whether the screen was just pressed or just released.
	 */
	protected int _current;
	
	public FlxTouch()
	{
		x = 0;
		y = 0;
		_current = 0;
		_last = 0;
	}
	
	public void update()
	{		
		if((_last == -1) && (_current == -1))
			_current = 0;
		else if((_last == 2) && (_current == 2))
			_current = 1;
		_last = _current;
	}
	
	/**
	 * Resets the just pressed/just released flags and sets touch to not pressed.
	 */
	public void reset()
	{
		_current = 0;
		_last = 0;
	}
	
	public boolean pressed()
	{
		return _current > 0;
	}
	
	public boolean justTouched()
	{
		return _current == 2;
	}
	
	public boolean justRemoved()
	{
		return _current == -1;
	}
	
	public void handleTouchDown(MotionEvent event)
	{
		x = event.getX();
		y = event.getY();
		//Log.i("coordinates", Float.toString(x) + " : " + Float.toString(y));
		//Log.i("coordinates", Float.toString(x/1.5f) + " : " + Float.toString(y/1.5f));
		
		if(_current > 0) 
			_current = 1;
		else 
			_current = 2;
	}
	
	
	public void handleTouchRemove(MotionEvent event)
	{
		x = event.getX();
		y = event.getY();
		if(_current > 0) 
			_current = -1;
		else 
			_current = 0;
	}


	
}
