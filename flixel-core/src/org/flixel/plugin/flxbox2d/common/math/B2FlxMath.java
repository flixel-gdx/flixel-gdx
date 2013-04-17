package org.flixel.plugin.flxbox2d.common.math;

import org.flixel.FlxG;

import com.badlogic.gdx.math.Vector2;

/**
 * Some bunch of Math stuff. Used for FlxBox2D.
 * 
 * @author Ka Wing Chin
 */
public class B2FlxMath
{
	/**
	 * PI
	 */
	public static final float PI = 3.1415927f;
	/**
	 * Degrees to radians.
	 */
	public static final float DEGREESTORADIANS = PI / 180f;
	/**
	 * Degrees to radians, but shorter variable name.
	 */
	public static final float DEGRAD = DEGREESTORADIANS;
	/**
	 * Radians to degrees.
	 */
	public static final float RADIANSTODEGREES = 180f / PI;
	/**
	 * Radians to degrees, but shorter variable name.
	 */
	public static final float RADDEG = RADIANSTODEGREES;

	
	public static float distance(Vector2 a, Vector2 b)
	{
		float cX = a.x - b.x;
		float cY = a.y - b.y;
		return (float) Math.sqrt(cX * cX + cY * cY);
	}
	
	/**
	 * Generates a random number between the lower and higher number.
	 * 
	 * @param lower
	 *            The lower number.
	 * @param higher
	 *            The higher number.
	 * @return A random number that got generated.
	 */
	public static float randomRange(float lower, float higher)
	{
		return (higher - lower) * FlxG.random() + lower;
	}

	public static Vector2 SubtractVV(Vector2 a, Vector2 b)
	{
		return new Vector2(a.x - b.x, a.y - b.y);
	}

	public final static float max(final float a, final float b)
	{
		return a > b ? a : b;
	}

	public final static int max(final int a, final int b)
	{
		return a > b ? a : b;
	}

}
