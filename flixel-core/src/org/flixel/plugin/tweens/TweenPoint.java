package org.flixel.plugin.tweens;

import org.flixel.FlxPoint;

import aurelienribon.tweenengine.TweenAccessor;

/**
 * The tween accessor for FlxPoint. Don't call any methods from this class. If
 * you got other variables that needs to be tweened, you can override this
 * class. You'll need to register your TweenAccessor with the your own sprite
 * class and tween accessor.
 * 
 * @author Claudio Ficara
 */
public class TweenPoint implements TweenAccessor<FlxPoint>
{
	/**
	 * The x-position from FlxObject.
	 */
	public static final int X = 1;
	/**
	 * The y-position from FlxObject.
	 */
	public static final int Y = 2;
	/**
	 * The x- and y-position from FlxObject.
	 */
	public static final int XY = 3;

	@Override
	public int getValues(FlxPoint target, int tweenType, float[] returnValues)
	{
		switch(tweenType)
		{
			case X:
				returnValues[0] = target.x;
				return 1;
			case Y:
				returnValues[0] = target.y;
				return 1;
			case XY:
				returnValues[0] = target.x;
				returnValues[1] = target.y;
				return 2;
			default:
				assert false;
				return -1;
		}
	}

	@Override
	public void setValues(FlxPoint target, int tweenType, float[] newValues)
	{
		switch(tweenType)
		{
			case X:
				target.x = newValues[0];
				break;
			case Y:
				target.y = newValues[0];
				break;
			case XY:
				target.x = newValues[0];
				target.y = newValues[1];
				break;
			default:
				assert false;
				break;
		}
	}
}
