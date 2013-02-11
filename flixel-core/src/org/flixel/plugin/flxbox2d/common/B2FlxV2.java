package org.flixel.plugin.flxbox2d.common;

import org.flixel.plugin.flxbox2d.B2FlxB;

import com.badlogic.gdx.math.Vector2;

/**
 * It's just an subclass of Vector2. It only does the pixels to meters ratio.
 * 
 * @author Ka Wing Chin
 */
public class B2FlxV2 extends Vector2
{
	private static final long serialVersionUID = -8590716581405868354L;

	/**
	 * Constructor
	 * 
	 * @param x
	 *            The x-component
	 * @param y
	 *            The y-component
	 */
	public B2FlxV2(float x, float y)
	{
		super(x / B2FlxB.RATIO, y / B2FlxB.RATIO);
	}

	/**
	 * Constructor
	 * 
	 * @param x
	 *            The x-component
	 */
	public B2FlxV2(float x)
	{
		this(x / B2FlxB.RATIO, 0);
	}

	/**
	 * Constructor
	 */
	public B2FlxV2()
	{
		this(0, 0);
	}

	public final static float cross(Vector2 a, Vector2 b)
	{
		return a.x * b.y - a.y * b.x;
	}

	public final static Vector2 cross(Vector2 a, float s)
	{
		return new Vector2(s * a.y, -s * a.x);
	}

	public final static Vector2 cross(float s, Vector2 a)
	{
		return new Vector2(-s * a.y, s * a.x);
	}
}
