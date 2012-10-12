package org.flixel.plugin.flxbox2d.common.math;

import org.flixel.FlxG;

/**
 * Some bunch of Math stuff. Used for FlxBox2D.
 * 
 * @author Ka Wing Chin
 */
public class B2FlxMath
{
	public static final float PI = 3.1415927f;
	public static final float DEGREESTORADIANS = PI / 180f;
	public static final float DEGRAD = DEGREESTORADIANS;
	public static final float RADIANSTODEGREES = 180f / PI;
	public static final float RADDEG = RADIANSTODEGREES;
	
	public static float randomRange(float lower, float higher)
	{
		return (higher - lower) * FlxG.random() + lower;
	}
}
