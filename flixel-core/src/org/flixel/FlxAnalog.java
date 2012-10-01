package org.flixel;

import org.flixel.event.AFlxAnalog;

import com.badlogic.gdx.math.Circle;

/**
 * An analog stick or thumbstick with callbacks. It can easily be customized by 
 * overriding the parent methods.
 * 
 * @author Ka Wing Chin
 */
public class FlxAnalog extends FlxGroup
{
	// Base image
	private final String ImgBase = "org/flixel/data/pack:base";
	// Stick image
	private final String ImgThumb = "org/flixel/data/pack:stick";
	
	// From radians to degrees.
	private final double DEGREES = (180 / Math.PI);

	// Used with public variable <code>status</code>, means not highlighted or pressed.
	private final int NORMAL = 0;
	// Used with public variable <code>status</code>, means highlighted (usually from mouse over).
	private final int HIGHLIGHT = 1;
	// Used with public variable <code>status</code>, means pressed (usually from mouse click).
	private final int PRESSED = 2;		
	// Shows the current state of the button.
	public int status;
	
	// X position of the upper left corner of this object in world space.
	public float x;
	// Y position of the upper left corner of this object in world space.
	public float y;
	
	// This function is called when the button is released.
	public AFlxAnalog onUp;
	// This function is called when the button is pressed down.
	public AFlxAnalog onDown;
	// This function is called when the mouse goes over the button.
	public AFlxAnalog onOver;
	// This function is called when the button is hold down.
	public AFlxAnalog onPressed;
	
	// The area which the joystick will react.
	private Circle _zone;
	// The background of the joystick, also known as the base.
	public FlxSprite bg;
	// The thumb 
	public FlxSprite thumb;
	
	// The radius where the thumb can move.
	private float _radius;
	private double _direction;
	private float _amount;		
	
	// How fast the speed of this object is changing.
	public FlxPoint acceleration;
	// The speed of easing when the thumb is released.
	private float _ease;
		
	/**
	 * Constructor
	 * @param	X		The X-coordinate of the point in space.
 	 * @param	Y		The Y-coordinate of the point in space.
 	 * @param	radius	The radius where the thumb can move. If 0, the background will be use as radius.
 	 * @param	ease	The duration of the easing. The value must be between 0 and 1.
	 */
	public FlxAnalog(float x, float y, float radius, float ease)
	{
		this.x = x;
		this.y = y;			
		_radius = radius;
		_ease = ease;
		
		status = NORMAL;
		_direction = 0;
		_amount = 0;
		acceleration = new FlxPoint();
		
		createBase();
		createThumb();
		createZone();
	}
	
	/**
	 * Constructor
	 * @param	X		The X-coordinate of the point in space.
 	 * @param	Y		The Y-coordinate of the point in space.
 	 * @param	radius	The radius where the thumb can move. If 0, the background will be use as radius.
	 */
	public FlxAnalog(float x, float y, float radius)
	{
		this(x, y, radius, .25f);
	}
	
	/**
	 * Constructor
	 * @param	X		The X-coordinate of the point in space.
 	 * @param	Y		The Y-coordinate of the point in space.
	 */
	public FlxAnalog(float x, float y)
	{
		this(x, y, 0, .25f);
	}
	
	/**
	 * Constructor
	 * @param	X		The X-coordinate of the point in space.
	 */
	public FlxAnalog(float x)
	{
		this(x, 0, 0, .25f);
	}
	
	/**
	 * Constructor
	 */
	public FlxAnalog()
	{
		this(0, 0, 0, .25f);
	}
	
	
	/**
	 * Creates the background of the analog stick.
	 * Override this to customize the background.
	 */
	protected void createBase()
	{
		bg = new FlxSprite(x, y).loadGraphic(ImgBase);
		bg.x += -bg.width * .5;
		bg.y += -bg.height * .5;
		bg.scrollFactor.x = bg.scrollFactor.y = 0;
		bg.setSolid(false);
		bg.ignoreDrawDebug = true;
		add(bg);			
	}
	
	/**
	 * Creates the thumb of the analog stick.
	 * Override this to customize the thumb.
	 */
	protected void createThumb()
	{
		thumb = new FlxSprite(x, y).loadGraphic(ImgThumb);
		thumb.scrollFactor.x = thumb.scrollFactor.y = 0;
		thumb.setSolid(false);
		thumb.ignoreDrawDebug = true;
		add(thumb);
	}
	
	/**
	 * Creates the touch zone. It's based on the size of the background. 
	 * The thumb will react when the mouse is in the zone.
	 * Override this to customize the zone.
	 * 
	 * @param	contract	Contract the size.
	 */
	protected void createZone()
	{
		if(_radius == 0)			
			_radius = bg.width / 2;
		_zone = new Circle(x, y, _radius);
	}
	
	/**
	 * Clean up memory.
	 */
	@Override
	public void destroy()
	{
		super.destroy();
		onUp = onDown = onOver = onPressed = null;
		acceleration = null;
		thumb = null;
		_zone = null;
		bg = null;
		thumb = null;
	}
	
	
	/**
	 * Update the behaviour. 
	 */
	@Override
	public void update()
	{
		boolean offAll = true;
		
		if(_zone.contains(FlxG.mouse.screenX, FlxG.mouse.screenY) || status == PRESSED)
		{
			offAll = false;				
			
			if(FlxG.mouse.pressed())
			{
				status = PRESSED;	
				
				if(FlxG.mouse.justPressed())
				{
					if(onDown != null)
						onDown.callback();
				}
							
				if(status == PRESSED)
				{
					if(onPressed != null)
						onPressed.callback();						
					
					float dx = FlxG.mouse.screenX-x;
					float dy = FlxG.mouse.screenY-y;
	
					double dist = Math.sqrt(dx * dx + dy * dy); // TODO: replace Math.sqrt, but need to benchmark this in Flash version 11.4.
					if(dist < 1) 
						dist = 0;
					_direction = Math.atan2(dy, dx); // TODO: replace Math.atan2, but need to benchmark this in Flash version 11.4.
					_amount = FlxU.min(_radius, (float) dist) / _radius;
							
					acceleration.x = (float) (FlxU.cos(_direction) * _amount * _radius);
					acceleration.y = (float) (FlxU.sin(_direction) * _amount * _radius);			
				}					
			}
			else if(FlxG.mouse.justReleased() && status == PRESSED)
			{				
				status = HIGHLIGHT;
				if(onUp != null)
					onUp.callback();					
				acceleration.x = 0;
				acceleration.y = 0;
			}			
			
			if(status == NORMAL)
			{
				status = HIGHLIGHT;
				if(onOver != null)
					onOver.callback();
			}
		}
		if((status == HIGHLIGHT || status == NORMAL) && _amount != 0)
		{				
			_amount *= _ease;
			if(FlxU.abs(_amount) < 0.1) 
				_amount = 0;				
		}					
		
		thumb.x = (float) (x + FlxU.cos(_direction) * _amount * _radius - (thumb.width * .5f));
		thumb.y = (float) (y + FlxU.sin(_direction) * _amount * _radius - (thumb.height * .5f));
		
		if(offAll)
		{
			status = NORMAL;
		}
		super.update();
	}
	
	/**
	 * returns the angle in degrees.
	 * @return	The angle.
	 */
	public float getAngle()
	{
		return (float) (Math.atan2(acceleration.y,acceleration.x) * DEGREES);
	}
	
	/**
	 * Whether the thumb is pressed or not.
	 */
	public boolean pressed()
	{
		return status == PRESSED;
	}
	
	/**
	 * Whether the thumb is just pressed or not.
	 */
	public boolean justPressed()
	{
		return FlxG.mouse.justPressed() && status == PRESSED;
	}
	
	/**
	 * Whether the thumb is just released or not.
	 */
	public boolean justReleased()
	{
		return FlxG.mouse.justReleased() && status == HIGHLIGHT;
	}

	/**
	 * Set <code>alpha</code> to a number between 0 and 1 to change the opacity of the gamepad.
	 * @param Alpha
	 */
	public void setAlpha(float Alpha)
	{
		for(int i = 0; i < members.size; i++)
		{
			((FlxSprite) members.get(i)).setAlpha(Alpha);
		}
	}
}
