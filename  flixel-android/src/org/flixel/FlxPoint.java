package org.flixel;

/**
 * Stores a 2D floating point coordinate.
 */
public class FlxPoint
{
	/**
	 * @default 0
	 */
	public float x;
	/**
	 * @default 0
	 */
	public float y;
	
	
	/**
	 * Instantiate a new point object.
	 * 
	 * @param	X		The X-coordinate of the point in space.
	 * @param	Y		The Y-coordinate of the point in space.
	 */
	public FlxPoint(float X, float Y)
	{
		constructor(X, Y);
	}
	
	/**
	 * Instantiate a new point object.
	 * 
	 * @param	X		The X-coordinate of the point in space.
	 */
	public FlxPoint(float X)
	{
		constructor(X, 0);
	}
	
	/**
	 * Instantiate a new point object.
	 */
	public FlxPoint()
	{
		constructor(0, 0);
	}
	
	private void constructor(float X, float Y)
	{
		x = X;
		y = Y;
	}
	
	/**
	 * Convert object to readable string name.  Useful for debugging, save games, etc.
	 */
	@Override
	public String toString()
	{
		return getClass().getName();		
	}
}
