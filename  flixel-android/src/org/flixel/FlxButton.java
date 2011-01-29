package org.flixel;


/**
 * A simple button class that calls a function when clicked by the mouse.
 * Supports labels, highlight states, and parallax scrolling.
 */
public class FlxButton extends FlxGroup
{
	/**
	 * Set this to true if you want this button to function even while the game
	 * is paused.
	 */
	public boolean pauseProof;
	/**
	 * Used for checkbox-style behavior.
	 */
	protected boolean _onToggle;
	/**
	 * Stores the 'off' or normal button state graphic.
	 */
	protected FlxSprite _off;
	/**
	 * Stores the 'on' or highlighted button state graphic.
	 */
	protected FlxSprite _on;
	/**
	 * Stores the 'off' or normal button state label.
	 */
	protected FlxText _offT;
	/**
	 * Stores the 'on' or highlighted button state label.
	 */
	protected FlxText _onT;
	/**
	 * This function is called when the button is clicked.
	 */
	protected FlxButtonListener _callback;
	/**
	 * Tracks whether or not the button is currently pressed.
	 */
	protected boolean _pressed;
	/**
	 * Helper variable for correcting its members' <code>scrollFactor</code>
	 * objects.
	 */
	protected FlxPoint _sf;
	/**
	 * Tracks whether the touch area is within in the button.
	 */
	protected boolean _inArea;
	/**
	 * Enable hover when swipe over the button.
	 */
	private boolean _swipeTouch;
	/**
	 * Tracks the current state if swipeTouch is on.
	 */
	private int _current;


	/**
	 * Creates a new <code>FlxButton</code> object with a gray background and a
	 * callback function on the UI thread.
	 * 
	 * @param X The X position of the button.
	 * @param Y The Y position of the button.
	 * @param Callback The function to call whenever the button is clicked.
	 */
	public FlxButton(float X, float Y, FlxButtonListener Callback, int Width, int Height)
	{
		super();
		constructor(X, Y, Callback, Width, Height);
	}
	
	public FlxButton(int X, int Y, FlxButtonListener Callback, int Width)
	{
		super();
		constructor(X, Y, Callback, Width, 20);
	}
	
	public FlxButton(float X, float Y, FlxButtonListener Callback)
	{
		super();
		constructor(X, Y, Callback, 100, 20);
	}
	
	public FlxButton(float X, float Y)
	{
		super();
		constructor(X, Y, null, 100, 20);
	}
	
	private void constructor(float X, float Y, FlxButtonListener Callback, int Width, int Height)
	{
		x = X;
		y = Y;
		width = Width;
		height = Height;
		_on = new FlxSprite().createGraphic(width, height, 0xffffffff);
		_on.setSolid(false);
		add(_on, true);
		_off = new FlxSprite().createGraphic(width, height, 0xff7f7f7f);
		_off.setSolid(false);
		add(_off, true);
		_offT = null;
		_onT = null;
		_callback = Callback;
		_onToggle = false;
		_pressed = false;
		_current = 0;
		_sf = null;
		pauseProof = false;
		_swipeTouch = true;
	}
	

	/**
	 * Set your own image as the button background.
	 * 
	 * @param Image A FlxSprite object to use for the button background.
	 * @param ImageHighlight A FlxSprite object to use for the button background
	 *        when highlighted (optional).
	 * 
	 * @return This FlxButton instance (nice for chaining stuff together, if
	 *         you're into that).
	 */
	public FlxButton loadGraphic(FlxSprite Image, FlxSprite ImageHighlight)
	{
		_off = (FlxSprite) replace(_off, Image);
		if(ImageHighlight == null)
		{
			if(_on != _off)
				remove(_on);
			_on = _off;
		}
		else
			_on = (FlxSprite) replace(_on, ImageHighlight);
		_on.setSolid(false);
		_off.setSolid(false);
		_off.scrollFactor = scrollFactor;
		_on.scrollFactor = scrollFactor;
		width = _off.width;
		height = _off.height;
		refreshHulls();
		return this;
	}
	
	public FlxButton loadGraphic(FlxSprite Image)
	{
		return loadGraphic(Image, null);
	}

	/**
	 * Add a text label to the button.
	 * 
	 * @param Text A FlxText object to use to display text on this button
	 *        (optional).
	 * @param TextHighlight A FlxText object that is used when the button is
	 *        highlighted (optional).
	 * 
	 * @return This FlxButton instance (nice for chaining stuff together, if
	 *         you're into that).
	 */
	public FlxButton loadText(FlxText Text, FlxText TextHighlight)
	{
		if(Text != null)
		{
			if(_offT == null)
			{
				_offT = Text;
				add(_offT);
			}
			else
				_offT = (FlxText) replace(_offT, Text);
		}
		if(TextHighlight == null)
			_onT = _offT;
		else
		{
			if(_onT == null)
			{
				_onT = TextHighlight;
				add(_onT);
			}
			else
				_onT = (FlxText) replace(_onT, TextHighlight);
		}
		_offT.scrollFactor = scrollFactor;
		_onT.scrollFactor = scrollFactor;
		return this;
	}
	
	public FlxButton loadText(FlxText Text)
	{
		return loadText(Text, null);
	}

	/**
	 * Called by the game loop automatically, handles mouseover and click
	 * detection.
	 */
	@Override
	public void update()
	{		
		visibility(true);
		if(overlapsPoint(FlxG.touch.x/FlxG.ratio, FlxG.touch.y/FlxG.ratio))
		{	
			if(FlxG.getPause() && !pauseProof)
				return;
			_inArea = true;
			
			//check if button was just justSwipped
			if(FlxG.touch.pressed() && _swipeTouch)
			{
				if(_current > 0)
					_current = 1;
				else
					_current = 2;
			}
			if(FlxG.touch.justRemoved())
				_current = 0;
			if(FlxG.touch.justTouched())
				_current = 2;
			
			if(FlxG.touch.justRemoved() && _pressed && _callback != null)
			{
				_pressed = false;
				_callback.onTouchUp();	
			}
			if(!FlxG.touch.pressed())
			{
				_pressed = false;
			}
			else if(!_pressed && _swipeTouch)
			{
				_pressed = true;
			}
			else if(FlxG.touch.justTouched() && !_pressed)
			{
					_pressed = true;
			}
			visibility(!_pressed);
		}
		else
		{
			_pressed = false;
			_current = 0;
			_inArea = false;
		}
		if(_onToggle)
			visibility(_off.visible);
		super.update();
	}

	/**
	 * Use this to toggle checkbox-style behavior.
	 */
	public boolean getOn()
	{
		return _onToggle;
	}

	/**
	 * @private
	 */
	public void setOn(boolean On)
	{
		_onToggle = On;
	}

	/**
	 * Called by the game state when state is changed (if this object belongs to
	 * the state).
	 * This method is empty.
	 */
	@Override
	public void destroy()
	{	
//		if(FlxG.getStage() != null)
//			FlxG.getStage().setOnTouchListener(null);
	}

	/**
	 * Internal function for handling the visibility of the off and on graphics.
	 * 
	 * @param On Whether the button should be on or off.
	 */
	protected void visibility(boolean On)
	{
		if(On)
		{		
			_on.visible = false;
			if(_onT != null)
				_onT.visible = false;
			_off.visible = true;
			if(_offT != null)
				_offT.visible = true;
		}
		else
		{
			_off.visible = false;
			if(_offT != null)
				_offT.visible = false;
			_on.visible = true;
			if(_onT != null)
				_onT.visible = true;
		}
	}

	/**
	 * 
	 * @return
	 */
	public boolean getPressed()
	{
		return _pressed;
	}
		
	public boolean getRemoved()
	{
		return !_pressed && _inArea && FlxG.touch.justRemoved();
	}
	
	public boolean getTouched()
	{
		return _pressed && _current == 2;
	}
	
	
	/**
	 * Enables the button to react while it get swiped.
	 * @param value	Set this to true if you want this button to function when swiped.
	 */
	public void setTouchMove(boolean value)
	{
		_swipeTouch = value;
	}
}
