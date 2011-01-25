package org.flixel.data;

import android.view.MotionEvent;


/**
 * This class helps contain and track the touch pointer in your game.
 */
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
	
	/**
	 * Check to see if the screen is pressed.
	 * 
	 * @return	Whether the screen is pressed.
	 */
	public boolean pressed()
	{
		return _current > 0;
	}
	
	/**
	 * Check to see if the screen was just pressed.
	 * 
	 * @return Whether the screen was just pressed.
	 */
	public boolean justTouched()
	{
		return _current == 2;
	}
	
	/**
	 * Check to see if the screen was just released.
	 * 
	 * @return	Whether the screen was just released.
	 */
	public boolean justRemoved()
	{
		return _current == -1;
	}
	
	/**
	 * Event handler so FlxGame can toggle the touch.
	 * @param event A <code>MotionEvent</code> object.
	 */
	public void handleTouchDown(MotionEvent event)
	{
		x = event.getX();
		y = event.getY();
		
		if(_current > 0) 
			_current = 1;
		else 
			_current = 2;
	}
	
	/**
	 * Event handler so FlxGame can toggle the touch.
	 * @param event A <code>MotionEvent</code> object.
	 */
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
