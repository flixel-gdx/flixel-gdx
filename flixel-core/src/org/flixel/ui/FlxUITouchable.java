package org.flixel.ui;

import org.flixel.FlxCamera;
import org.flixel.FlxG;
import org.flixel.FlxSound;
import org.flixel.ui.event.IFlxUIListener;

import flash.events.Event;
import flash.events.IEventListener;
import flash.events.MouseEvent;

/**
 * The parent class for touchable UI components.
 * 
 * @author Ka Wing Chin
 */
public class FlxUITouchable extends FlxUIComponent
{
	/**
	 * Used with public variable <code>status</code>, means not highlighted or
	 * pressed.
	 */
	static public final int NORMAL = 0;
	/**
	 * Used with public variable <code>status</code>, means highlighted (usually
	 * from mouse over).
	 */
	static public final int HIGHLIGHT = 1;
	/**
	 * Used with public variable <code>status</code>, means pressed (usually
	 * from mouse click).
	 */
	static public final int PRESSED = 2;
	/**
	 * This function is called when the component is released. We recommend
	 * assigning your main component behavior to this function via the
	 * <code>FlxUITouchable</code> constructor.
	 */
	public IFlxUIListener onUp;
	/**
	 * This function is called when the component is pressed down.
	 */
	public IFlxUIListener onDown;
	/**
	 * This function is called when the mouse goes over the component.
	 */
	public IFlxUIListener onOver;
	/**
	 * This function is called when the mouse leaves the component area.
	 */
	public IFlxUIListener onOut;
	/**
	 * Set this to play a sound when the mouse goes over the component. We
	 * recommend using the helper function setSounds()!
	 */
	public FlxSound soundOver;
	/**
	 * Set this to play a sound when the mouse leaves the component. We
	 * recommend using the helper function setSounds()!
	 */
	public FlxSound soundOut;
	/**
	 * Set this to play a sound when the component is pressed down. We recommend
	 * using the helper function setSounds()!
	 */
	public FlxSound soundDown;
	/**
	 * Set this to play a sound when the component is released. We recommend
	 * using the helper function setSounds()!
	 */
	public FlxSound soundUp;
	/**
	 * Used for checkbox-style behavior.
	 */
	protected boolean _onToggle;
	/**
	 * Tracks whether or not the component is currently pressed.
	 */
	protected boolean _pressed;
	/**
	 * Whether or not the component has initialized itself yet.
	 */
	private boolean _initialized;

	/**
	 * Creates a new <code>FlxUITouchable</code> object.
	 * 
	 * @param X The x-position of the component.
	 * @param Y The y-position of the component.
	 * @param UISkin The skin that needs to be applied.
	 * @param Label The label along side the component.
	 * @param Width The width of the component. Default auto.
	 * @param Height The height of the component. Default auto.
	 */
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

	/**
	 * Creates a new <code>FlxUITouchable</code> object.
	 * 
	 * @param X The x-position of the component.
	 * @param Y The y-position of the component.
	 * @param UISkin The skin that needs to be applied.
	 * @param Label The label along side the component.
	 * @param Width The width of the component. Default auto.
	 */
	public FlxUITouchable(float X, float Y, FlxUISkin Skin, String Label, int Width)
	{
		this(X, Y, Skin, Label, Width, 0);
	}

	/**
	 * Creates a new <code>FlxUITouchable</code> object.
	 * 
	 * @param X The x-position of the component.
	 * @param Y The y-position of the component.
	 * @param UISkin The skin that needs to be applied.
	 * @param Label The label along side the component.
	 */
	public FlxUITouchable(float X, float Y, FlxUISkin Skin, String Label)
	{
		this(X, Y, Skin, Label, 0, 0);
	}

	/**
	 * Creates a new <code>FlxUITouchable</code> object.
	 * 
	 * @param X The x-position of the component.
	 * @param Y The y-position of the component.
	 * @param UISkin The skin that needs to be applied.
	 */
	public FlxUITouchable(float X, float Y, FlxUISkin Skin)
	{
		this(X, Y, Skin, null, 0, 0);
	}

	@Override
	public void destroy()
	{
		if(FlxG.getStage() != null)
			FlxG.getStage().removeEventListener(MouseEvent.MOUSE_UP, onMouseUp);
		onMouseUp = null;
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

	@Override
	public void setDefaultSkin()
	{
		FlxG.log("You don't have a skin provided in your subclass");
	}

	/**
	 * Initialize the mouse listener.
	 */
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

	@Override
	public void update()
	{
		super.update();
		if(enabled)
			updateComponent();
	}

	/**
	 * Basic component update logic
	 */
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
			int totalPointers = FlxG.mouse.activePointers + 1;
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
	 * 
	 * @param boolean
	 */
	public void setOn(boolean On)
	{
		_onToggle = On;
	}

	/**
	 * Whether the component has a checkbox-style behavior.
	 * 
	 * @return
	 */
	public boolean getOn()
	{
		return _onToggle;
	}

	/**
	 * Set sounds to play during mouse-button interactions. These operations can
	 * be done manually as well, and the public sound variables can be used
	 * after this for more fine-tuning, such as positional audio, etc.
	 * 
	 * @param SoundOver What embedded sound effect to play when the mouse goes
	 *        over the button. Default is null, or no sound.
	 * @param SoundOverVolume How load the that sound should be.
	 * @param SoundOut What embedded sound effect to play when the mouse leaves
	 *        the button area. Default is null, or no sound.
	 * @param SoundOutVolume How load the that sound should be.
	 * @param SoundDown What embedded sound effect to play when the mouse
	 *        presses the button down. Default is null, or no sound.
	 * @param SoundDownVolume How load the that sound should be.
	 * @param SoundUp What embedded sound effect to play when the mouse releases
	 *        the button. Default is null, or no sound.
	 * @param SoundUpVolume How load the that sound should be.
	 */
	public void setSounds(String SoundOver, float SoundOverVolume, String SoundOut, float SoundOutVolume, String SoundDown, float SoundDownVolume, String SoundUp,
			float SoundUpVolume)
	{
		if(SoundOver != null)
			soundOver = FlxG.loadSound(SoundOver, SoundOverVolume);
		if(SoundOut != null)
			soundOut = FlxG.loadSound(SoundOut, SoundOutVolume);
		if(SoundDown != null)
			soundDown = FlxG.loadSound(SoundDown, SoundDownVolume);
		if(SoundUp != null)
			soundUp = FlxG.loadSound(SoundUp, SoundUpVolume);
	}

	/**
	 * Set sounds to play during mouse-button interactions. These operations can
	 * be done manually as well, and the public sound variables can be used
	 * after this for more fine-tuning, such as positional audio, etc.
	 * 
	 * @param SoundOver What embedded sound effect to play when the mouse goes
	 *        over the button. Default is null, or no sound.
	 * @param SoundOverVolume How load the that sound should be.
	 * @param SoundOut What embedded sound effect to play when the mouse leaves
	 *        the button area. Default is null, or no sound.
	 * @param SoundOutVolume How load the that sound should be.
	 * @param SoundDown What embedded sound effect to play when the mouse
	 *        presses the button down. Default is null, or no sound.
	 * @param SoundDownVolume How load the that sound should be.
	 * @param SoundUp What embedded sound effect to play when the mouse releases
	 *        the button. Default is null, or no sound.
	 */
	public void setSounds(String SoundOver, float SoundOverVolume, String SoundOut, float SoundOutVolume, String SoundDown, float SoundDownVolume, String SoundUp)
	{
		setSounds(SoundOver, SoundOverVolume, SoundOut, SoundOutVolume, SoundDown, SoundDownVolume, SoundUp, 1.0f);
	}

	/**
	 * Set sounds to play during mouse-button interactions. These operations can
	 * be done manually as well, and the public sound variables can be used
	 * after this for more fine-tuning, such as positional audio, etc.
	 * 
	 * @param SoundOver What embedded sound effect to play when the mouse goes
	 *        over the button. Default is null, or no sound.
	 * @param SoundOverVolume How load the that sound should be.
	 * @param SoundOut What embedded sound effect to play when the mouse leaves
	 *        the button area. Default is null, or no sound.
	 * @param SoundOutVolume How load the that sound should be.
	 * @param SoundDown What embedded sound effect to play when the mouse
	 *        presses the button down. Default is null, or no sound.
	 * @param SoundDownVolume How load the that sound should be.
	 */
	public void setSounds(String SoundOver, float SoundOverVolume, String SoundOut, float SoundOutVolume, String SoundDown, float SoundDownVolume)
	{
		setSounds(SoundOver, SoundOverVolume, SoundOut, SoundOutVolume, SoundDown, SoundDownVolume, null, 1.0f);
	}

	/**
	 * Set sounds to play during mouse-button interactions. These operations can
	 * be done manually as well, and the public sound variables can be used
	 * after this for more fine-tuning, such as positional audio, etc.
	 * 
	 * @param SoundOver What embedded sound effect to play when the mouse goes
	 *        over the button. Default is null, or no sound.
	 * @param SoundOverVolume How load the that sound should be.
	 * @param SoundOut What embedded sound effect to play when the mouse leaves
	 *        the button area. Default is null, or no sound.
	 * @param SoundOutVolume How load the that sound should be.
	 * @param SoundDown What embedded sound effect to play when the mouse
	 *        presses the button down. Default is null, or no sound.
	 */
	public void setSounds(String SoundOver, float SoundOverVolume, String SoundOut, float SoundOutVolume, String SoundDown)
	{
		setSounds(SoundOver, SoundOverVolume, SoundOut, SoundOutVolume, SoundDown, 1.0f, null, 1.0f);
	}

	/**
	 * Set sounds to play during mouse-button interactions. These operations can
	 * be done manually as well, and the public sound variables can be used
	 * after this for more fine-tuning, such as positional audio, etc.
	 * 
	 * @param SoundOver What embedded sound effect to play when the mouse goes
	 *        over the button. Default is null, or no sound.
	 * @param SoundOverVolume How load the that sound should be.
	 * @param SoundOut What embedded sound effect to play when the mouse leaves
	 *        the button area. Default is null, or no sound.
	 * @param SoundOutVolume How load the that sound should be.
	 */
	public void setSounds(String SoundOver, float SoundOverVolume, String SoundOut, float SoundOutVolume)
	{
		setSounds(SoundOver, SoundOverVolume, SoundOut, SoundOutVolume, null, 1.0f, null, 1.0f);
	}

	/**
	 * Set sounds to play during mouse-button interactions. These operations can
	 * be done manually as well, and the public sound variables can be used
	 * after this for more fine-tuning, such as positional audio, etc.
	 * 
	 * @param SoundOver What embedded sound effect to play when the mouse goes
	 *        over the button. Default is null, or no sound.
	 * @param SoundOverVolume How load the that sound should be.
	 * @param SoundOut What embedded sound effect to play when the mouse leaves
	 *        the button area. Default is null, or no sound.
	 */
	public void setSounds(String SoundOver, float SoundOverVolume, String SoundOut)
	{
		setSounds(SoundOver, SoundOverVolume, SoundOut, 1.0f, null, 1.0f, null, 1.0f);
	}

	/**
	 * Set sounds to play during mouse-button interactions. These operations can
	 * be done manually as well, and the public sound variables can be used
	 * after this for more fine-tuning, such as positional audio, etc.
	 * 
	 * @param SoundOver What embedded sound effect to play when the mouse goes
	 *        over the button. Default is null, or no sound.
	 * @param SoundOverVolume How load the that sound should be.
	 */
	public void setSounds(String SoundOver, float SoundOverVolume)
	{
		setSounds(SoundOver, SoundOverVolume, null, 1.0f, null, 1.0f, null, 1.0f);
	}

	/**
	 * Set sounds to play during mouse-button interactions. These operations can
	 * be done manually as well, and the public sound variables can be used
	 * after this for more fine-tuning, such as positional audio, etc.
	 * 
	 * @param SoundOver What embedded sound effect to play when the mouse goes
	 *        over the button. Default is null, or no sound.
	 */
	public void setSounds(String SoundOver)
	{
		setSounds(SoundOver, 1.0f, null, 1.0f, null, 1.0f, null, 1.0f);
	}

	/**
	 * Set sounds to play during mouse-button interactions. These operations can
	 * be done manually as well, and the public sound variables can be used
	 * after this for more fine-tuning, such as positional audio, etc.
	 */
	public void setSounds()
	{
		setSounds(null, 1.0f, null, 1.0f, null, 1.0f, null, 1.0f);
	}

	/**
	 * Internal function for handling the actual callback call (for UI thread
	 * dependent calls like <code>FlxU.openURL()</code>).
	 */
	IEventListener onMouseUp = new IEventListener()
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

	/**
	 * This method will be called on mouse up. It's used for internal behavior.
	 * Override this in subclass.
	 */
	protected void onChange()
	{
		// Override this in subclass.
	}
}
