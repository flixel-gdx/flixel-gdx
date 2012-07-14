package org.flixel;

import com.badlogic.gdx.math.Rectangle;

/**
 * 
 * 
 * @author Ka Wing Chin
 */
public class FlxAnalog extends FlxGroup
{
	// Base image
	private final String ImgBase = "org/flixel/data/pack:base";
	// Stick image
	private final String ImgStick = "org/flixel/data/pack:stick";
	
	// How fast the speed of this object is changing.	
	private static final float ACCELERATION = 10;
	
	public float x;
	public float y;
	private float _centerX;
	private float _centerY;

	private Rectangle _pad;
	private FlxSprite _base;
	public FlxSprite _stick;
	
	public FlxPoint accel;
	public boolean pressed;
	private float yMin;
	private float yMax;
	private float xMin;
	private float xMax;
		
	/**
	 * Constructor
	 * @param X		The x-position of the analog stick
	 * @param Y		The y-position of the analog stick.
	 */
	public FlxAnalog(float X, float Y)
	{
		accel  = new FlxPoint();
		x = X;
		y = Y;		
		
		yMin = 0-24+y;
		yMax = 100-24+y;
		xMin = 0-24+x;
		xMax = 100-24+x;
		
		_pad = new Rectangle(x,y,100,100);
		_centerX = X+_pad.width*.25f;
		_centerY = Y+_pad.width*.25f;
		
		_base = new FlxSprite(X,Y, ImgBase);
		//_base.setAlpha(0.75f);
		_base.setSolid(false);
		_base.ignoreDrawDebug = true;
		add(_base);
		
		_stick = new FlxSprite(_centerX, _centerY, ImgStick);
		_stick.width = _stick.height = 48;
		_stick.offset.x = 20; 
		_stick.offset.y = 3;
		_stick.setSolid(false);
		_stick.ignoreDrawDebug = true;
		add(_stick);
	}
	
	
	@Override
	public void update()
	{
		if(FlxG.mouse.pressed())
		{
			if(_pad.contains(FlxG.mouse.screenX, FlxG.mouse.screenY) || pressed)
			{
				pressed = true;
				_stick.x = FlxG.mouse.x - _stick.width*.5f;
				_stick.y = FlxG.mouse.y - _stick.height*.5f;
				
				if(_stick.y <= yMin)
					_stick.y = yMin;
				if(_stick.y >= yMax)
					_stick.y = yMax;
				if(_stick.x <= xMin)
					_stick.x = xMin;
				if(_stick.x >= xMax)
					_stick.x = xMax;
				
				accel.x = ((74 - (100 - (_stick.x)))-x) / ACCELERATION;
				accel.y = ((74 - (100 - (_stick.y)))-y) / ACCELERATION;
			}
		}
		else
		{
			pressed = false;			
			_stick.x = _centerX -((_centerX - _stick.x) / 1.5f);
			_stick.y = _centerY - ((_centerY - _stick.y) / 1.5f);
			// TODO: add motion to accel when released.
			accel.x = 0;//FlxU.round(((74 - (100 - (_stick.x)))-x) / ACCELERATION);
			accel.y = 0;//FlxU.round(((74 - (100 - (_stick.y)))-y) / ACCELERATION);			
		}
		super.update();
	}
}
