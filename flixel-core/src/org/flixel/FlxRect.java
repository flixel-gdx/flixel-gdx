package org.flixel;

import com.badlogic.gdx.math.Rectangle;

/**
 * Stores a rectangle.
 * 
 * @author Ka Wing Chin
 */
public class FlxRect
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
	 * @default 0
	 */
	public float width;
	/**
	 * @default 0
	 */
	public float height;

	/**
	 * Instantiate a new rectangle.
	 * 
	 * @param	X		The X-coordinate of the point in space.
	 * @param	Y		The Y-coordinate of the point in space.
	 * @param	Width	Desired width of the rectangle.
	 * @param	Height	Desired height of the rectangle.
	 */
	public FlxRect(float X, float Y, float Width, float Height)
	{
		x = X;
		y = Y;
		width = Width;
		height = Height;
	}

	/**
	 * Instantiate a new rectangle.
	 * 
	 * @param	X		The X-coordinate of the point in space.
	 * @param	Y		The Y-coordinate of the point in space.
	 * @param	Width	Desired width of the rectangle.
	 */
	public FlxRect(float X, float Y, float Width)
	{
		this(X, Y, Width, 0);
	}

	/**
	 * Instantiate a new rectangle.
	 * 
	 * @param	X		The X-coordinate of the point in space.
	 * @param	Y		The Y-coordinate of the point in space.
	 */
	public FlxRect(float X, float Y)
	{
		this(X, Y, 0, 0);
	}

	/**
	 * Instantiate a new rectangle.
	 * 
	 * @param	X		The X-coordinate of the point in space.
	 */
	public FlxRect(float X)
	{
		this(X, 0, 0, 0);
	}

	/**
	 * Instantiate a new rectangle.
	 */
	public FlxRect()
	{
		this(0, 0, 0, 0);
	}

	/**
	 * The X coordinate of the left side of the rectangle.  Read-only.
	 */
	public float getLeft()
	{
		return x;
	}

	/**
	 * The X coordinate of the right side of the rectangle.  Read-only.
	 */
	public float getRight()
	{
		return x + width;
	}

	/**
	 * The Y coordinate of the top of the rectangle.  Read-only.
	 */
	public float getTop()
	{
		return y;
	}

	/**
	 * The Y coordinate of the bottom of the rectangle.  Read-only.
	 */
	public float getBottom()
	{
		return y + height;
	}

	/**
	 * Instantiate a new rectangle.
	 * 
	 * @param	X		The X-coordinate of the point in space.
	 * @param	Y		The Y-coordinate of the point in space.
	 * @param	Width	Desired width of the rectangle.
	 * @param	Height	Desired height of the rectangle.
	 * 
	 * @return	A reference to itself.
	 */
	public FlxRect make(float X, float Y, float Width, float Height)
	{
		x = X;
		y = Y;
		width = Width;
		height = Height;
		return this;
	}

	/**
	 * Instantiate a new rectangle.
	 * 
	 * @param	X		The X-coordinate of the point in space.
	 * @param	Y		The Y-coordinate of the point in space.
	 * @param	Width	Desired width of the rectangle.
	 * 
	 * @return	A reference to itself.
	 */
	public FlxRect make(float X, float Y, float Width)
	{
		return make(X, Y, Width, 0);
	}

	/**
	 * Instantiate a new rectangle.
	 * 
	 * @param	X		The X-coordinate of the point in space.
	 * @param	Y		The Y-coordinate of the point in space.
	 * 
	 * @return	A reference to itself.
	 */
	public FlxRect make(float X, float Y)
	{
		return make(X, Y, 0, 0);
	}

	/**
	 * Instantiate a new rectangle.
	 * 
	 * @param	X		The X-coordinate of the point in space.
	 * 
	 * @return	A reference to itself.
	 */
	public FlxRect make(float X)
	{
		return make(X, 0, 0, 0);
	}

	/**
	 * Instantiate a new rectangle.
	 * 
	 * @return	A reference to itself.
	 */
	public FlxRect make()
	{
		return make(0, 0, 0, 0);
	}

	/**
	 * Helper function, just copies the values from the specified rectangle.
	 * 
	 * @param	Rect	Any <code>FlxRect</code>.
	 * 
	 * @return	A reference to itself.
	 */
	public FlxRect copyFrom(FlxRect Rect)
	{
		x = Rect.x;
		y = Rect.y;
		width = Rect.width;
		height = Rect.height;
		return this;
	}

	/**
	 * Helper function, just copies the values from this rectangle to the specified rectangle.
	 * 
	 * @param	Rect	Any <code>FlxRect</code>.
	 * 
	 * @return	A reference to the altered rectangle parameter.
	 */
	public FlxRect copyTo(FlxRect Rect)
	{
		Rect.x = x;
		Rect.y = y;
		Rect.width = width;
		Rect.height = height;
		return Rect;
	}

	/**
	 * Helper function, just copies the values from the specified libgdx rectangle.
	 * 
	 * @param	Rect	Any <code>Rectangle</code>.
	 * 
	 * @return	A reference to itself.
	 */
	public FlxRect copyFromFlash(Rectangle Rect)
	{
		x = Rect.x;
		y = Rect.y;
		width = Rect.width;
		height = Rect.height;
		return this;
	}

	/**
	 * Helper function, just copies the values from this rectangle to the specified libgdx rectangle.
	 * 
	 * @param	Rect	Any <code>Rectangle</code>.
	 * 
	 * @return	A reference to the altered rectangle parameter.
	 */
	public Rectangle copyToFlash(Rectangle Rect)
	{
		Rect.x = x;
		Rect.y = y;
		Rect.width = width;
		Rect.height = height;
		return Rect;
	}

	/**
	 * Checks to see if some <code>FlxRect</code> object overlaps this <code>FlxRect</code> object.
	 * 
	 * @param	Rect	The rectangle being tested.
	 * 
	 * @return	Whether or not the two rectangles overlap.
	 */
	public boolean overlaps(FlxRect Rect)
	{
		return (Rect.x + Rect.width > x) && (Rect.x < x+width) && (Rect.y + Rect.height > y) && (Rect.y < y+height);
	}
}
