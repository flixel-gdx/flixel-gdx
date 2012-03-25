package org.flixel;

import org.flixel.data.SystemAsset;

import com.badlogic.gdx.math.Circle;

/**
 * 
 * 
 * @author Ka Wing Chin
 */
public class FlxAnalog extends FlxGroup
{
	/**
	 * How fast the speed of this object is changing.
	 */
	private static final float ACCELERATION = 10;
	
	public float x;
	public float y;
	private float _centerX;
	private float _centerY;

	private Circle _pad;
	private FlxSprite _base;
	private FlxSprite _stick;
	
	public FlxPoint accel;
	public boolean pressed;
	
		
	/**
	 * Constructor
	 * @param X		The x-position of the analog stick
	 * @param Y		The y-position of the analog stick.
	 */
	public FlxAnalog(float X, float Y)
	{
		x = X;
		y = Y;
		accel  = new FlxPoint();
		
		_pad = new Circle(64+X, 64+Y, 64);
		_centerX = X+_pad.radius-32;
		_centerY = Y+_pad.radius-32;
		
		_base = new FlxSprite(X,Y, SystemAsset.ImgControlBase);
		_base.setAlpha(0.75f);
		_base.setSolid(false);
		_base.ignoreDrawDebug = true;
		add(_base);
		
		_stick = new FlxSprite(_centerX, _centerY).loadGraphic(SystemAsset.ImgControlKnob, false, false, 64, 64);
		_stick.setSolid(false);
		_stick.ignoreDrawDebug = true;
		add(_stick);
	}
	
	
	@Override
	public void update()
	{
		if(FlxG.mouse.pressed())
		{
			if(_pad.contains(FlxG.mouse.x, FlxG.mouse.y))
			{
				pressed = true;
				_stick.x = FlxG.mouse.x - _stick.width*.5f;
				_stick.y = FlxG.mouse.y - _stick.height*.5f;
				accel.x = ((64 - (128 - (FlxG.mouse.x)))-x) / ACCELERATION;
				accel.y = ((64 - (128 - (FlxG.mouse.y)))-y) / ACCELERATION;
			}
		}
		else
		{
			pressed = false;			
			_stick.x = _centerX;
			_stick.y = _centerY;
		}
		super.update();
	}
}
