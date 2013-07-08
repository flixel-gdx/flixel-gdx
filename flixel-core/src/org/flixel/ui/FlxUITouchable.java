package org.flixel.ui;

import org.flixel.FlxCamera;
import org.flixel.FlxG;
import org.flixel.FlxSound;
import org.flixel.ui.event.IFlxUIListener;

import flash.events.Event;
import flash.events.Listener;
import flash.events.MouseEvent;

/**
 *
 * @author Ka Wing Chin
 */
public class FlxUITouchable extends FlxUIComponent
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
	 * This function is called when the button is released.
	 * We recommend assigning your main button behavior to this function
	 * via the <code>FlxButton</code> constructor.
	 */
	public IFlxUIListener onUp;
	/**
	 * This function is called when the button is pressed down.
	 */
	public IFlxUIListener onDown;
	/**
	 * This function is called when the mouse goes over the button.
	 */
	public IFlxUIListener onOver;
	/**
	 * This function is called when the mouse leaves the button area.
	 */
	public IFlxUIListener onOut;
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
	private boolean _initialized;

	public FlxUITouchable(float X, float Y, FlxUISkin Skin, String Label, int Width, int Height)
	{
		super(X, Y, Skin, Label, Width, Height);
		onUp = null;
		onDown = null;
		onOut = null;
		onOver = null;
		
		soundOver = null;
		soundOut = null;
		soundDown = null;
		soundUp = null;

		status = skin.NORMAL;
		_onToggle = false;
		_pressed = false;
		_initialized = false;
	}
	
	public FlxUITouchable(float X, float Y, FlxUISkin Skin, String Label, int Width)
	{
		this(X, Y, Skin, Label, Width, 0);
	}
	
	public FlxUITouchable(float X, float Y, FlxUISkin Skin, String Label)
	{
		this(X, Y, Skin, Label, 0, 0);
	}
	
	public FlxUITouchable(float X, float Y, FlxUISkin Skin)
	{
		this(X, Y, Skin, null, 0, 0);
	}
	
	public FlxUITouchable(float X, float Y)
	{
		this(X, Y, null, null, 0, 0);
	}
	
	@Override
	public void destroy()
	{		
		if(FlxG.getStage() != null)
			FlxG.getStage().removeEventListener(MouseEvent.MOUSE_UP, onMouseUp);
		onUp = null;
		onDown = null;
		onOut = null;
		onOver = null;
		if(soundOver != null)
			soundOver.destroy();
		if(soundOut != null)
			soundOut.destroy();
		if(soundDown != null)
			soundDown.destroy();
		if(soundUp != null)
			soundUp.destroy();
		super.destroy();
	}

	protected void init()
	{
		FlxG.getStage().addEventListener(MouseEvent.MOUSE_UP, onMouseUp);
		_initialized = true;
	}
	
	@Override
	public void preUpdate()
	{		
		super.preUpdate();		
		if(!_initialized)
		{
			if(FlxG.getStage() != null)
				init();
		}
	}
	
	/**
	 * Basic button update logic
	 */
	@Override
	public void update()
	{
		super.update();
		if(enabled)
			updateComponent();
	}
	
	protected void updateComponent()
	{
		if(status == PRESSED)
			status = NORMAL;
				
		if(FlxG.mouse.getVisible())
		{
			if(cameras == null)
				cameras = FlxG.cameras;
			FlxCamera camera;
			int i = 0;
			int l = cameras.size;
			int pointerId = 0;		
			int	totalPointers = FlxG.mouse.activePointers + 1;
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
						if(FlxG.mouse.pressed(pointerId))
						{
							status = PRESSED;
							skinStatus = activated ? skin.ACTIVE_PRESSED : skin.PRESSED;
							if(FlxG.mouse.justPressed(pointerId))
							{
								if(onDown != null)
									onDown.callback();
								if(soundDown != null)
									soundDown.play(true);
							}
						}
					
						if(status == NORMAL || (activated && skinStatus == skin.ACTIVE_NORMAL))
						{
							status = HIGHLIGHT;
							skinStatus = activated ? skin.ACTIVE_HIGHTLIGHT : skin.HIGHLIGHT;
							if(onOver != null)
								onOver.callback();
							if(soundOver != null)
								soundOver.play(true);
						}
					}
					++pointerId;
				}			
			}
			if(offAll)
			{
				if(status != NORMAL)
				{
					if(onOut != null)
						onOut.callback();
					if(soundOut != null)
						soundOut.play(true);
				}
				status = NORMAL;
				skinStatus = activated ? skin.ACTIVE_NORMAL : skin.NORMAL;
			}
		}
		//Then pick the appropriate frame of animation
		if((status == HIGHLIGHT) && FlxG.mobile)
		{
			if(activated)
				skinStatus = enabled ? skin.ACTIVE_NORMAL : skin.ACTIVE_DISABLED;
			else
				skinStatus = enabled ? skin.NORMAL : skin.DISABLED;
			setFrame(skinStatus);
		}	
		else
			setFrame(skinStatus);
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
	 * Internal function for handling the actual callback call (for UI thread dependent calls like <code>FlxU.openURL()</code>).
	 */
	Listener onMouseUp = new Listener()
	{
		@Override
		public void onEvent(Event e)
		{
			if(!exists || !visible || !active || (status != PRESSED))
				return;
			if(_onToggle)
			{
				activated = !activated;
				skinStatus = activated ? skin.ACTIVE_NORMAL : skin.NORMAL;
				setFrame(skinStatus);
			}			
			onChange();
			if(onUp != null)
				onUp.callback();
			if(soundUp != null)
				soundUp.play(true);
		}
	};

	protected void onChange()
	{
		// Override this in subclass.		
	}
}

