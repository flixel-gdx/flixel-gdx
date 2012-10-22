package org.flixel.plugin.flxbox2d.common.math;

import org.flixel.FlxG;

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
	
	/**
	 * Generates a random number between the lower and higher number. 
	 * @param lower		The lower number.
	 * @param higher	The higher number.
	 * @return	A random number that got generated.
	 */
	public static float randomRange(float lower, float higher)
	{
		return (higher - lower) * FlxG.random() + lower;
	}
}
