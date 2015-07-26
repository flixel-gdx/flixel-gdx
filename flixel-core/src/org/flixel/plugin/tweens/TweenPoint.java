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
	public int getValues(FlxPoint Target, int TweenType, float[] ReturnValues)
	{
		switch(TweenType)
		{
			case X:
				ReturnValues[0] = Target.x;
				return 1;
			case Y:
				ReturnValues[0] = Target.y;
				return 1;
			case XY:
				ReturnValues[0] = Target.x;
				ReturnValues[1] = Target.y;
				return 2;
			default:
				assert false;
				return -1;
		}
	}

	@Override
	public void setValues(FlxPoint Target, int TweenType, float[] NewValues)
	{
		switch(TweenType)
		{
			case X:
				Target.x = NewValues[0];
				break;
			case Y:
				Target.y = NewValues[0];
				break;
			case XY:
				Target.x = NewValues[0];
				Target.y = NewValues[1];
				break;
			default:
				assert false;
				break;
		}
	}
}
