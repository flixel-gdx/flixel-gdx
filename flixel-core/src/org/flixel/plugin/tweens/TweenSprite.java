package org.flixel.plugin.tweens;

import org.flixel.FlxSprite;

import aurelienribon.tweenengine.TweenAccessor;

/**
 * The tween accessor for FlxSprite. Don't call any methods from this class. If
 * you got other variables that needs to be tweened, you can override this
 * class. You'll need to register your TweenAccessor with the your own sprite
 * class and tween accessor.
 * 
 * @author Ka Wing Chin
 */
public class TweenSprite implements TweenAccessor<FlxSprite>
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
	/**
	 * The angle from FlxObject.
	 */
	public static final int ANGLE = 4;
	/**
	 * The alpha from FlxSprite.
	 */
	public static final int ALPHA = 5;
	/**
	 * The color from FlxSprite.
	 */
	public static final int COLOR = 6;
	/**
	 * The x-scale from FlxSprite.
	 */
	public static final int SCALE_X = 7;
	/**
	 * The y-scale from FlxSprite.
	 */
	public static final int SCALE_Y = 8;
	/**
	 * The x- and y-scale from FlxSprite.
	 */
	public static final int SCALE_XY = 9;

	@Override
	public int getValues(FlxSprite Target, int TweenType, float[] ReturnValues)
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
			case ANGLE:
				ReturnValues[0] = Target.angle;
				return 1;
			case ALPHA:
				ReturnValues[0] = Target.getAlpha();
				return 1;
			case COLOR:
				ReturnValues[0] = Target.getColor();
				return 1;
			case SCALE_X:
				ReturnValues[0] = Target.scale.x;
				return 1;
			case SCALE_Y:
				ReturnValues[0] = Target.scale.y;
				return 1;
			case SCALE_XY:
				ReturnValues[0] = Target.scale.x;
				ReturnValues[1] = Target.scale.y;
				return 2;
			default:
				assert false;
				return -1;
		}
	}

	@Override
	public void setValues(FlxSprite Target, int TweenType, float[] NewValues)
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
			case ANGLE:
				Target.angle = NewValues[0];
				break;
			case ALPHA:
				Target.setAlpha(NewValues[0]);
				break;
			case COLOR:
				Target.setColor((int) NewValues[0]);
				break;
			case SCALE_X:
				Target.scale.x = NewValues[0];
				break;
			case SCALE_Y:
				Target.scale.y = NewValues[0];
				break;
			case SCALE_XY:
				Target.scale.x = NewValues[0];
				Target.scale.y = NewValues[1];
				break;
			default:
				assert false;
				break;
		}
	}
}
