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
	public int getValues(FlxSprite target, int tweenType, float[] returnValues)
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
			case ANGLE:
				returnValues[0] = target.angle;
				return 1;
			case ALPHA:
				returnValues[0] = target.getAlpha();
				return 1;
			case COLOR:
				returnValues[0] = target.getColor();
				return 1;
			case SCALE_X:
				returnValues[0] = target.scale.x;
				return 1;
			case SCALE_Y:
				returnValues[0] = target.scale.y;
				return 1;
			case SCALE_XY:
				returnValues[0] = target.scale.x;
				returnValues[1] = target.scale.y;
				return 2;
			default:
				assert false;
				return -1;
		}
	}

	@Override
	public void setValues(FlxSprite target, int tweenType, float[] newValues)
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
			case ANGLE:
				target.angle = newValues[0];
				break;
			case ALPHA:
				target.setAlpha(newValues[0]);
				break;
			case COLOR:
				target.setColor((int) newValues[0]);
				break;
			case SCALE_X:
				target.scale.x = newValues[0];
				break;
			case SCALE_Y:
				target.scale.y = newValues[0];
				break;
			case SCALE_XY:
				target.scale.x = newValues[0];
				target.scale.y = newValues[1];
				break;
			default:
				assert false;
				break;
		}
	}
}
