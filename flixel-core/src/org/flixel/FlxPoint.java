package org.flixel;

import com.badlogic.gdx.math.Vector2;

/**
 * Stores a 2D floating point coordinate.
 * 
 * @author Ka Wing Chin
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
	 * @param	X	The X-coordinate of the point in space.
	 * @param	Y	The Y-coordinate of the point in space.
	 */
	public FlxPoint(float X, float Y)
	{
		x = X;
		y = Y;
	}

	/**
	 * Instantiate a new point object.
	 * 
	 * @param	X	The X-coordinate of the point in space.
	 */
	public FlxPoint(float X)
	{
		this(X, 0);
	}

	/**
	 * Instantiate a new point object.
	 */
	public FlxPoint()
	{
		this(0, 0);
	}

	/**
	 * Instantiate a new point object.
	 * 
	 * @param	X	The X-coordinate of the point in space.
	 * @param	Y	The Y-coordinate of the point in space.
	 */
	public FlxPoint make(float X, float Y)
	{
		x = X;
		y = Y;
		return this;
	}

	/**
	 * Instantiate a new point object.
	 * 
	 * @param	X	The X-coordinate of the point in space.
	 */
	public FlxPoint make(float X)
	{
		return make(X, 0);
	}

	/**
	 * Instantiate a new point object.
	 */
	public FlxPoint make()
	{
		return make(0, 0);
	}

	/**
	 * Helper function, just copies the values from the specified point.
	 * 
	 * @param	Point	Any <code>FlxPoint</code>.
	 * 
	 * @return	A reference to itself.
	 */
	public FlxPoint copyFrom(FlxPoint Point)
	{
		x = Point.x;
		y = Point.y;
		return this;
	}

	/**
	 * Helper function, just copies the values from this point to the specified point.
	 * 
	 * @param	Point	Any <code>FlxPoint</code>.
	 * 
	 * @return	A reference to the altered point parameter.
	 */
	public FlxPoint copyTo(FlxPoint Point)
	{
		Point.x = x;
		Point.y = y;
		return Point;
	}

	/**
	 * Helper function, just copies the values from the specified libgdx vector.
	 * 
	 * @param	Point	Any <code>Point</code>.
	 * 
	 * @return	A reference to itself.
	 */
	public FlxPoint copyFromFlash(Vector2 Point)
	{
		x = Point.x;
		y = Point.y;
		return this;
	}

	/**
	 * Helper function, just copies the values from this point to the specified libgdx vector.
	 * 
	 * @param	Point	Any <code>Point</code>.
	 * 
	 * @return	A reference to the altered point parameter.
	 */
	public Vector2 copyToFlash(Vector2 Point)
	{
		Point.x = x;
		Point.y = y;
		return Point;
	}
}
