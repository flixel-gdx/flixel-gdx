package org.flixel;

import org.flixel.event.AFlxButton;
import org.flixel.data.SystemAsset;



/**
 * A simple button class that calls a function when clicked by the mouse.
 * 
 * @author	Ka Wing Chin
 */
public class FlxButton extends FlxSprite
{
	/**
	 * Used with public variable <code>status</code>, means not highlighted or pressed.
	 */
	static public final int NORMAL = 0;
	/**
	 * Used with public variable <code>status</code>, means highlighted (usually from mouse over).
	 */
	static public final int HIGHLIGHT = 1;
	/**
	 * Used with public variable <code>status</code>, means pressed (usually from mouse click).
	 */
	static public final int PRESSED = 2;
	
	/**
	 * The text that appears on the button.
	 */
	public FlxText label;
	/**
	 * Controls the offset (from top left) of the text from the button.
	 */
	public FlxPoint labelOffset;
	/**
	 * This event is called when the button is released or pressed.
	 * We recommend assigning your main button behavior to this function
	 * via the <code>FlxButton</code> constructor.
	 */
	public AFlxButton buttonEvent;
	/**
	 * Shows the current state of the button.
	 */
	public int status;
	/**
	 * Set this to play a sound when the mouse goes over the button.
	 * We recommend using the helper function setSounds()!
	 */
	public FlxSound soundOver;
	/**
	 * Set this to play a sound when the mouse leaves the button.
	 * We recommend using the helper function setSounds()!
	 */
	public FlxSound soundOut;
	/**
	 * Set this to play a sound when the button is pressed down.
	 * We recommend using the helper function setSounds()!
	 */
	public FlxSound soundDown;
	/**
	 * Set this to play a sound when the button is released.
	 * We recommend using the helper function setSounds()!
	 */
	public FlxSound soundUp;

	/**
	 * Used for checkbox-style behavior.
	 */
	protected boolean _onToggle;
	
	/**
	 * Tracks whether or not the button is currently pressed.
	 */
	protected boolean _pressed;
	/**
	 * Whether or not the button has initialized itself yet.
	 */
	protected boolean _initialized;
	
	
	/**
	 * Creates a new <code>FlxButton</code> object with a gray background
	 * and a callback function on the UI thread.
	 * 
	 * @param	X			The X position of the button.
	 * @param	Y			The Y position of the button.
	 * @param	Label		The text that you want to appear on the button.
	 * @param	OnClick		The function to call whenever the button is clicked.
	 */
	public FlxButton(float X, float Y, String Label, AFlxButton ButtonEvent)
	{
		super(X,Y);
		if(Label != null)
		{
			label = new FlxText(20,0,80,Label);
			//label.setFormat(null,0,0x333333,"center");
			label.setAlignment("center");
			labelOffset = new FlxPoint(-1,3);
		}
		loadGraphic(SystemAsset.ImgButton,false,80,20);
		buttonEvent = ButtonEvent;
		
		soundOver = null;
		soundOut = null;
		soundDown = null;
		soundUp = null;

		status = NORMAL;
		_onToggle = false;
		_pressed = false;
		_initialized = false;
	}
	
	/**
	 * Creates a new <code>FlxButton</code> object with a gray background
	 * and a callback function on the UI thread.
	 * 
	 * @param	X			The X position of the button.
	 * @param	Y			The Y position of the button.
	 * @param	Label		The text that you want to appear on the button.
	 */
	public FlxButton(float X, float Y, String Label)
	{
		this(X, Y, Label, null);
	}
	
	/**
	 * Creates a new <code>FlxButton</code> object with a gray background
	 * and a callback function on the UI thread.
	 * 
	 * @param	X			The X position of the button.
	 * @param	Y			The Y position of the button.
	 */
	public FlxButton(float X, float Y)
	{
		this(X, Y, null, null);
	}
	
	/**
	 * Creates a new <code>FlxButton</code> object with a gray background
	 * and a callback function on the UI thread.
	 * 
	 * @param	X			The X position of the button.
	 */
	public FlxButton(float X)
	{
		this(X, 0, null, null);
	}
	
	/**
	 * Creates a new <code>FlxButton</code> object with a gray background
	 * and a callback function on the UI thread.
	 */
	public FlxButton()
	{
		this(0, 0, null, null);
	}
	
	@Override
	public void destroy()
	{		
		if(label != null)
		{
			label.destroy();
			label = null;
		}
		buttonEvent = null;

//		if(soundOver != null)
//			soundOver.destroy();
//		if(soundOut != null)
//			soundOut.destroy();
//		if(soundDown != null)
//			soundDown.destroy();
//		if(soundUp != null)
//			soundUp.destroy();
		super.destroy();
	}
	
	
	@Override
	public void preUpdate()
	{
		if(!_initialized)
		{			
			_initialized = true;			
		}
		super.preUpdate();
	}
	
	@Override
	public void update()
	{
		updateButton(); //Basic button logic

		//Default button appearance is to simply update
		// the label appearance based on animation frame.
		if(label == null)
			return;
		switch(getFrame())
		{
			case HIGHLIGHT:	//Extra behavior to accomodate checkbox logic.
				label.setAlpha(1.0f);
				break;
			case PRESSED:
				label.setAlpha(0.5f);
				label.y++;
				break;
			case NORMAL:
			default:
				label.setAlpha(0.8f);
				break;
		}
	}
	
	/**
	 * Basic button update logic
	 */
	protected void updateButton()
	{
		if(cameras == null)
			cameras = FlxG.cameras;
		FlxCamera camera;
		int i = 0;
		int l = cameras.size;
		int pointerId = 0;
		//TODO: Use FlxG.mouse.activePointers instead?
		final int totalPointers = 8;
		boolean offAll = true;
		while(i < l)
		{
			camera = cameras.get(i++);
			while(pointerId < totalPointers)
			{
				FlxG.mouse.getWorldPosition(pointerId, camera, _point);
				if(overlapsPoint(_point, true, camera))
				{
					offAll = false;
					if(FlxG.mouse.justPressed(pointerId))
					{
						status = PRESSED;
						if(buttonEvent != null)
							buttonEvent.onDown();
					}
					else if(FlxG.mouse.pressed(pointerId))
					{					
						status = PRESSED;
						if(buttonEvent != null)
							buttonEvent.onPressed();
					}
					else if(FlxG.mouse.justReleased(pointerId))
					{
						if(buttonEvent != null)
							buttonEvent.onUp();
						status = NORMAL;
					}
					if(status == NORMAL)
					{
						status = HIGHLIGHT;
						if(buttonEvent != null)
							buttonEvent.onOver();
						//if(soundOver != null)
							//soundOver.play(true);
					}
				}
				++pointerId;
			}			
		}
		if(offAll)
		{
			if(status != NORMAL)
			{
				if(buttonEvent != null)
					buttonEvent.onOut();
			}
			status = NORMAL;
		}
		
		//Then if the label and/or the label offset exist,
		// position them to match the button.
		if(label != null)
		{
			label.x = x;
			label.y = y;
		}
		if(labelOffset != null)
		{
			label.x += labelOffset.x;
			label.y += labelOffset.y;
		}

		//Then pick the appropriate frame of animation
		if((status == HIGHLIGHT) && _onToggle)
			setFrame(NORMAL);
		else
			setFrame(status);
	}
	
	
	@Override
	public void draw()
	{
		super.draw();
		if(label != null)
		{
			label.scrollFactor =  scrollFactor;
			label.cameras = cameras;
			label.draw();
		}
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
}
