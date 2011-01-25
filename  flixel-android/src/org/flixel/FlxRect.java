package org.flixel;

/**
 * Stores a rectangle.
 */
public class FlxRect extends FlxPoint
{
	/**
	 * @default 0
	 */
	public int width;
	
	/**
	 * @default 0
	 */
	public int height;
	
	/**
	 * Instantiate a new rectangle.
	 * 
	 * @param	X		The X-coordinate of the point in space.
	 * @param	Y		The Y-coordinate of the point in space.
	 * @param	Width	Desired width of the rectangle.
	 * @param	Height	Desired height of the rectangle.
	 */
	public FlxRect(float X, float Y, int Width, int Height)
	{
		super(X, Y);
		constructor(X, Y, Width, Height);
	}
	
	/**
	 * Instantiate a new rectangle.
	 * 
	 * @param	X		The X-coordinate of the point in space.
	 * @param	Y		The Y-coordinate of the point in space.
	 * @param	Width	Desired width of the rectangle.
	 */
	public FlxRect(float X, float Y, int Width)
	{
		super(X, Y);
		constructor(X, Y, Width, 0);
	}
	
	/**
	 * Instantiate a new rectangle.
	 * 
	 * @param	X		The X-coordinate of the point in space.
	 * @param	Y		The Y-coordinate of the point in space.
	 * @param	Width	Desired width of the rectangle.
	 * @param	Height	Desired height of the rectangle.
	 */
	public FlxRect(float X, float Y)
	{
		super(X, Y);
		constructor(X, Y, 0, 0);
	}
	
	/**
	 * Instantiate a new rectangle.
	 */
	public FlxRect()
	{
		super();
		constructor(0, 0, 0, 0);
	}
	
	private void constructor(float X, float Y, int Width, int Height)
	{
		width = Width;
		height = Height;
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
	public float geTtop()
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
	
	
}
