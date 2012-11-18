package org.flixel;

import org.flixel.event.IFlxAnalog;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/**
 * An analog stick or thumbstick with callbacks. It can easily be customized by 
 * overriding the parent methods.
 * 
 * @author Ka Wing Chin
 */
public class FlxAnalog extends FlxGroup
{
	/**
	 * Base image
	 */
	private final String ImgBase = "org/flixel/data/pack:base";
	/**
	 * Stick image
	 */
	private final String ImgThumb = "org/flixel/data/pack:stick";
	
	/**
	 * From radians to degrees.
	 */
	private final float DEGREES = (float) (180f / Math.PI);

	/**
	 * Used with public variable <code>status</code>, means not highlighted or pressed.
	 */
	private final int NORMAL = 0;
	/**
	 * Used with public variable <code>status</code>, means highlighted (usually from mouse over).
	 */
	private final int HIGHLIGHT = 1;
	/**
	 * Used with public variable <code>status</code>, means pressed (usually from mouse click).
	 */
	private final int PRESSED = 2;		
	/**
	 * Shows the current state of the button.
	 */
	public int status;
	
	/**
	 * X position of the upper left corner of this object in world space.
	 */
	public float x;
	/**
	 * Y position of the upper left corner of this object in world space.
	 */
	public float y;
	
	/**
	 * An list of analogs that are currently active.
	 */
	private static Array<FlxAnalog> _analogs;
	/**
	 * The current pointer that's active on the analog.
	 */
	private int _currentPointer;
	/**
	 * This is just a pre-allocated x-y point container to be used however you like
	 */
	protected FlxPoint _point;
	/**
	 * This function is called when the button is released.
	 */
	public IFlxAnalog onUp;
	/**
	 * This function is called when the button is pressed down.
	 */
	public IFlxAnalog onDown;
	/**
	 * This function is called when the mouse goes over the button.
	 */
	public IFlxAnalog onOver;
	/**
	 * This function is called when the button is hold down.
	 */
	public IFlxAnalog onPressed;
	
	/**
	 * The area which the joystick will react.
	 */
	private Circle _zone;
	/**
	 * The background of the joystick, also known as the base.
	 */
	public FlxSprite bg;
	/**
	 * The thumb 
	 */
	public FlxSprite thumb;
	
	/**
	 * The radius where the thumb can move.
	 */
	private float _radius;
	private float _direction;
	private float _amount;		
	
	/**
	 * How fast the speed of this object is changing.
	 */
	public FlxPoint acceleration;
	/**
	 * The speed of easing when the thumb is released.
	 */
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
		
		if(_analogs == null)
			_analogs = new Array<FlxAnalog>();
		_analogs.add(this);
		
		status = NORMAL;
		_direction = 0;
		_amount = 0;
		_currentPointer = -1;
		acceleration = new FlxPoint();
		_point = new FlxPoint();
		
		createBase();
		createThumb();
		createZone();
	}
	
	/**
	 * Constructor
	 * @param	X		The X-coordinate of the point in space. The position doesn't start at top-left, but the center of the thumb.
 	 * @param	Y		The Y-coordinate of the point in space. The position doesn't start at top-left, but the center of the thumb.
 	 * @param	radius	The radius where the thumb can move. If 0, the background will be use as radius.
	 */
	public FlxAnalog(float x, float y, float radius)
	{
		this(x, y, radius, .25f);
	}
	
	/**
	 * Constructor
	 * @param	X		The X-coordinate of the point in space. The position doesn't start at top-left, but the center of the thumb.
 	 * @param	Y		The Y-coordinate of the point in space. The position doesn't start at top-left, but the center of the thumb.
	 */
	public FlxAnalog(float x, float y)
	{
		this(x, y, 0, .25f);
	}
	
	/**
	 * Constructor
	 * @param	X		The X-coordinate of the point in space. The position doesn't start at top-left, but the center of the thumb.
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
		if(_analogs != null)
			_analogs.clear();
		_analogs = null;
		onUp = onDown = onOver = onPressed = null;
		acceleration = null;
		_point = null;
		thumb = null;
		_zone = null;
		bg = null;
		thumb = null;
	}
	
	
	/**
	 * Update the behavior. 
	 */
	@Override
	public void update()
	{
		boolean foundFreePointer = false;
		int pointerId = 0;		
		int	totalPointers = FlxG.mouse.activePointers + 1;		
		
		if(_analogs.size > 1)  // For multiple analogs.
		{
			// There is no reason to get into the loop if their is already a pointer on the analog
			if(_currentPointer > 0)
				pointerId = _currentPointer;
			else
			{
				while(pointerId < totalPointers)
				{			
					for(int i = 0; i < _analogs.size; i++)
					{
						// check whether the pointer is already taken by another analog.
						if(_analogs.get(i) != this && _analogs.get(i)._currentPointer != pointerId) 
						{
							foundFreePointer = true;
							break;
						}
					}
					if(foundFreePointer)
						break;
					++pointerId;
				}
			}
			updateAnalog(pointerId);
		}
		else // For single analog.
		{
			while(pointerId < totalPointers)
			{	
				updateAnalog(pointerId);
				++pointerId;
			}
		}
		super.update();
	}
	
	protected void updateAnalog(int pointerId)
	{		
		boolean offAll = true;
		FlxG.mouse.getWorldPosition(pointerId, null, _point);
		if(_zone.contains(_point.x, _point.y) || (status == PRESSED))
		{
			offAll = false;	
			if(FlxG.mouse.pressed(pointerId))
			{
				_currentPointer = pointerId;
				status = PRESSED;			
				if(FlxG.mouse.justPressed(pointerId))
				{
					if(onDown != null)
						onDown.callback();
				}						
				
				if(status == PRESSED)
				{
					if(onPressed != null)
						onPressed.callback();						
					
					float dx = _point.x-x;
					float dy = _point.y-y;
					
					double dist = Math.sqrt(dx * dx + dy * dy);
					if(dist < 1) 
						dist = 0;
					_direction = (float) MathUtils.atan2(dy, dx);
					_amount = FlxU.min(_radius, (float) dist) / _radius;
					
					acceleration.x = (float) (MathUtils.cos(_direction) * _amount * _radius);
					acceleration.y = (float) (MathUtils.sin(_direction) * _amount * _radius);			
				}					
			}
			else if(FlxG.mouse.justReleased(pointerId) && status == PRESSED)
			{				
				_currentPointer = -1;
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
			if(Math.abs(_amount) < 0.1) 
				_amount = 0;
		}
		thumb.x = (float) (x + MathUtils.cos(_direction) * _amount * _radius - (thumb.width * .5f));
		thumb.y = (float) (y + MathUtils.sin(_direction) * _amount * _radius - (thumb.height * .5f));
		if(offAll)
		{
			status = NORMAL;
			_currentPointer = -1;
		}
	}
	
	/**
	 * Returns the angle in degrees.
	 * @return	The angle.
	 */
	public float getAngle()
	{
		return (MathUtils.atan2(acceleration.y,acceleration.x) * DEGREES);
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
	 * Set <code>alpha</code> to a number between 0 and 1 to change the opacity of the analog.
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
