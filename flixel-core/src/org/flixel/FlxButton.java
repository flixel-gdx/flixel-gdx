package org.flixel;

import org.flixel.event.IFlxButton;

import flash.events.Event;
import flash.events.IEventListener;
import flash.events.MouseEvent;

/**
 * A simple button class that calls a function when clicked by the mouse.
 * 
 * @author Ka Wing Chin
 * @author Thomas Weston
 */
public class FlxButton extends FlxSprite
{
	static protected String ImgDefaultButton = "org/flixel/data/pack:button";

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
	 * This function is called when the button is released.
	 * We recommend assigning your main button behavior to this function
	 * via the <code>FlxButton</code> constructor.
	 */
	public IFlxButton onUp;
	/**
	 * This function is called when the button is pressed down.
	 */
	public IFlxButton onDown;
	/**
	 * This function is called when the mouse goes over the button.
	 */
	public IFlxButton onOver;
	/**
	 * This function is called when the mouse leaves the button area.
	 */
	public IFlxButton onOut;
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
	 * Internal event listener.
	 */
	private final IEventListener mouseUpListener = new IEventListener()
	{
		@Override
		public void onEvent(Event e)
		{
			onMouseUp((MouseEvent) e);
		}
	};

	/**
	 * Creates a new <code>FlxButton</code> object with a gray background
	 * and a callback function on the UI thread.
	 * 
	 * @param	X			The X position of the button.
	 * @param	Y			The Y position of the button.
	 * @param	Label		The text that you want to appear on the button.
	 * @param	OnClick		The function to call whenever the button is clicked.
	 */
	public FlxButton(float X,float Y,String Label,IFlxButton OnClick)
	{
		super(X,Y);
		if(Label != null)
		{
			label = new FlxText(0,0,80,Label);
			label.setFormat(null,8,0x333333,"center");
			labelOffset = new FlxPoint(-1,3);
		}
		loadGraphic(ImgDefaultButton,true,false,80,20);

		onUp = OnClick;
		onDown = null;
		onOut = null;
		onOver = null;

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
	public FlxButton(float X,float Y,String Label)
	{
		this(X,Y,Label,null);
	}

	/**
	 * Creates a new <code>FlxButton</code> object with a gray background
	 * and a callback function on the UI thread.
	 * 
	 * @param	X			The X position of the button.
	 * @param	Y			The Y position of the button.
	 */
	public FlxButton(float X,float Y)
	{
		this(X,Y,null,null);
	}

	/**
	 * Creates a new <code>FlxButton</code> object with a gray background
	 * and a callback function on the UI thread.
	 * 
	 * @param	X			The X position of the button.
	 */
	public FlxButton(float X)
	{
		this(X,0,null,null);
	}

	/**
	 * Creates a new <code>FlxButton</code> object with a gray background
	 * and a callback function on the UI thread.
	 */
	public FlxButton()
	{
		this(0,0,null,null);
	}

	/**
	 * Called by the game state when state is changed (if this object belongs to the state)
	 */
	@Override
	public void destroy()
	{
		if(FlxG.getStage() != null)
			FlxG.getStage().removeEventListener(MouseEvent.MOUSE_UP, mouseUpListener);
		if(label != null)
		{
			label.destroy();
			label = null;
		}
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

	/**
	 * Since button uses its own mouse handler for thread reasons,
	 * we run a little pre-check here to make sure that we only add
	 * the mouse handler when it is actually safe to do so.
	 */
	@Override
	public void preUpdate()
	{
		super.preUpdate();
		
		if(!_initialized)
		{
			if(FlxG.getStage() != null)
			{
				FlxG.getStage().addEventListener(MouseEvent.MOUSE_UP, mouseUpListener);
				_initialized = true;
			}
		}
	}

	/**
	 * Called by the game loop automatically, handles mouseover and click detection.
	 */
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
			case HIGHLIGHT: //Extra behavior to accommodate checkbox logic.
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
		if(status == PRESSED)
			status = NORMAL;

		//Figure out if the button is highlighted or pressed or what
		// (ignore checkbox behavior for now).
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
					FlxG.mouse.getWorldPosition(pointerId,camera,_point);
					if(overlapsPoint(_point,true,camera))
					{
						offAll = false;
						if(FlxG.mouse.pressed(pointerId))
						{
							status = PRESSED;
							if(FlxG.mouse.justPressed(pointerId))
							{
								if(onDown != null)
								{
									onDown.callback();
								}
								if(soundDown != null)
									soundDown.play(true);
							}
						}
						if(status == NORMAL)
						{
							status = HIGHLIGHT;
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
			}
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
		if((status == HIGHLIGHT) && (_onToggle || FlxG.mobile))
			setFrame(NORMAL);
		else
			setFrame(status);
	}

	/**
	 * Just draws the button graphic and text label to the screen.
	 */
	@Override
	public void draw()
	{
		super.draw();
		if(label != null)
		{
			label.scrollFactor = scrollFactor;
			label.cameras = cameras;
			label.draw();
		}
	}

	/**
	 * Updates the size of the text field to match the button.
	 */
	@Override
	protected void resetHelpers()
	{
		super.resetHelpers();
		if(label != null)
			label.width = width;
	}

	/**
	 * Set sounds to play during mouse-button interactions.
	 * These operations can be done manually as well, and the public
	 * sound variables can be used after this for more fine-tuning,
	 * such as positional audio, etc.
	 * 
	 * @param	SoundOver			What embedded sound effect to play when the mouse goes over the button. Default is null, or no sound.
	 * @param	SoundOverVolume		How load the that sound should be.
	 * @param	SoundOut			What embedded sound effect to play when the mouse leaves the button area. Default is null, or no sound.
	 * @param	SoundOutVolume		How load the that sound should be.
	 * @param	SoundDown			What embedded sound effect to play when the mouse presses the button down. Default is null, or no sound.
	 * @param	SoundDownVolume		How load the that sound should be.
	 * @param	SoundUp				What embedded sound effect to play when the mouse releases the button. Default is null, or no sound.
	 * @param	SoundUpVolume		How load the that sound should be.
	 */
	public void setSounds(String SoundOver, float SoundOverVolume, String SoundOut, float SoundOutVolume, String SoundDown, float SoundDownVolume, String SoundUp, float SoundUpVolume)
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
	 * Set sounds to play during mouse-button interactions.
	 * These operations can be done manually as well, and the public
	 * sound variables can be used after this for more fine-tuning,
	 * such as positional audio, etc.
	 * 
	 * @param	SoundOver			What embedded sound effect to play when the mouse goes over the button. Default is null, or no sound.
	 * @param	SoundOverVolume		How load the that sound should be.
	 * @param	SoundOut			What embedded sound effect to play when the mouse leaves the button area. Default is null, or no sound.
	 * @param	SoundOutVolume		How load the that sound should be.
	 * @param	SoundDown			What embedded sound effect to play when the mouse presses the button down. Default is null, or no sound.
	 * @param	SoundDownVolume		How load the that sound should be.
	 * @param	SoundUp				What embedded sound effect to play when the mouse releases the button. Default is null, or no sound.
	 */
	public void setSounds(String SoundOver, float SoundOverVolume, String SoundOut, float SoundOutVolume, String SoundDown, float SoundDownVolume, String SoundUp)
	{
		setSounds(SoundOver, SoundOverVolume, SoundOut, SoundOutVolume, SoundDown, SoundDownVolume, SoundUp, 1.0f);
	}

	/**
	 * Set sounds to play during mouse-button interactions.
	 * These operations can be done manually as well, and the public
	 * sound variables can be used after this for more fine-tuning,
	 * such as positional audio, etc.
	 * 
	 * @param	SoundOver			What embedded sound effect to play when the mouse goes over the button. Default is null, or no sound.
	 * @param	SoundOverVolume		How load the that sound should be.
	 * @param	SoundOut			What embedded sound effect to play when the mouse leaves the button area. Default is null, or no sound.
	 * @param	SoundOutVolume		How load the that sound should be.
	 * @param	SoundDown			What embedded sound effect to play when the mouse presses the button down. Default is null, or no sound.
	 * @param	SoundDownVolume		How load the that sound should be.
	 */
	public void setSounds(String SoundOver, float SoundOverVolume, String SoundOut, float SoundOutVolume, String SoundDown, float SoundDownVolume)
	{
		setSounds(SoundOver, SoundOverVolume, SoundOut, SoundOutVolume, SoundDown, SoundDownVolume, null, 1.0f);
	}

	/**
	 * Set sounds to play during mouse-button interactions.
	 * These operations can be done manually as well, and the public
	 * sound variables can be used after this for more fine-tuning,
	 * such as positional audio, etc.
	 * 
	 * @param	SoundOver			What embedded sound effect to play when the mouse goes over the button. Default is null, or no sound.
	 * @param	SoundOverVolume		How load the that sound should be.
	 * @param	SoundOut			What embedded sound effect to play when the mouse leaves the button area. Default is null, or no sound.
	 * @param	SoundOutVolume		How load the that sound should be.
	 * @param	SoundDown			What embedded sound effect to play when the mouse presses the button down. Default is null, or no sound.
	 */
	public void setSounds(String SoundOver, float SoundOverVolume, String SoundOut, float SoundOutVolume, String SoundDown)
	{
		setSounds(SoundOver, SoundOverVolume, SoundOut, SoundOutVolume, SoundDown, 1.0f, null, 1.0f);
	}

	/**
	 * Set sounds to play during mouse-button interactions.
	 * These operations can be done manually as well, and the public
	 * sound variables can be used after this for more fine-tuning,
	 * such as positional audio, etc.
	 * 
	 * @param	SoundOver			What embedded sound effect to play when the mouse goes over the button. Default is null, or no sound.
	 * @param	SoundOverVolume		How load the that sound should be.
	 * @param	SoundOut			What embedded sound effect to play when the mouse leaves the button area. Default is null, or no sound.
	 * @param	SoundOutVolume		How load the that sound should be.
	 */
	public void setSounds(String SoundOver, float SoundOverVolume, String SoundOut, float SoundOutVolume)
	{
		setSounds(SoundOver, SoundOverVolume, SoundOut, SoundOutVolume, null, 1.0f, null, 1.0f);
	}

	/**
	 * Set sounds to play during mouse-button interactions.
	 * These operations can be done manually as well, and the public
	 * sound variables can be used after this for more fine-tuning,
	 * such as positional audio, etc.
	 * 
	 * @param	SoundOver			What embedded sound effect to play when the mouse goes over the button. Default is null, or no sound.
	 * @param	SoundOverVolume		How load the that sound should be.
	 * @param	SoundOut			What embedded sound effect to play when the mouse leaves the button area. Default is null, or no sound.
	 */
	public void setSounds(String SoundOver, float SoundOverVolume, String SoundOut)
	{
		setSounds(SoundOver, SoundOverVolume, SoundOut, 1.0f, null, 1.0f, null, 1.0f);
	}

	/**
	 * Set sounds to play during mouse-button interactions.
	 * These operations can be done manually as well, and the public
	 * sound variables can be used after this for more fine-tuning,
	 * such as positional audio, etc.
	 * 
	 * @param	SoundOver			What embedded sound effect to play when the mouse goes over the button. Default is null, or no sound.
	 * @param	SoundOverVolume		How load the that sound should be.
	 */
	public void setSounds(String SoundOver, float SoundOverVolume)
	{
		setSounds(SoundOver, SoundOverVolume, null, 1.0f, null, 1.0f, null, 1.0f);
	}

	/**
	 * Set sounds to play during mouse-button interactions.
	 * These operations can be done manually as well, and the public
	 * sound variables can be used after this for more fine-tuning,
	 * such as positional audio, etc.
	 * 
	 * @param	SoundOver			What embedded sound effect to play when the mouse goes over the button. Default is null, or no sound.
	 */
	public void setSounds(String SoundOver)
	{
		setSounds(SoundOver, 1.0f, null, 1.0f, null, 1.0f, null, 1.0f);
	}

	/**
	 * Set sounds to play during mouse-button interactions.
	 * These operations can be done manually as well, and the public
	 * sound variables can be used after this for more fine-tuning,
	 * such as positional audio, etc.
	 */
	public void setSounds()
	{
		setSounds(null, 1.0f, null, 1.0f, null, 1.0f, null, 1.0f);
	}

	/**
	 * Use this to toggle checkbox-style behavior.
	 */
	public boolean getOn()
	{
		return _onToggle;
	}

	/**
	 * Use this to toggle checkbox-style behavior.
	 */
	public void setOn(boolean On)
	{
		_onToggle = On;
	}

	/**
	 * Internal function for handling the actual callback call (for UI thread dependent calls like <code>FlxU.openURL()</code>).
	 */
	protected void onMouseUp(MouseEvent event)
	{
		if(!exists || !visible || !active || (status != PRESSED))
			return;
		if(onUp != null)
			onUp.callback();
		if(soundUp != null)
			soundUp.play(true);
	}
}
