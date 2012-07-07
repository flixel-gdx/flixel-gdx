package org.flixel;

import org.flixel.data.SystemAsset;
import org.flixel.event.AFlxButton;
import org.flixel.event.IMouseObserver;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;



/**
 * A simple button class that calls a function when clicked by the mouse.
 * 
 * @author	Ka Wing Chin
 */
public class FlxButton extends FlxSprite implements IMouseObserver
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
	public AFlxButton callback;
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
	private boolean _initialized;	
	
	/**
	 * Creates a new <code>FlxButton</code> object with a gray background
	 * and a callback function on the UI thread.
	 * 
	 * @param	X			The X position of the button.
	 * @param	Y			The Y position of the button.
	 * @param	Label		The text that you want to appear on the button.
	 * @param	OnClick		The function to call whenever the button is clicked.
	 */
	public FlxButton(float X, float Y, String Label, AFlxButton Callback)
	{
		super(X,Y);
		if(Label != null)
		{
			label = new FlxText(0,0,80,Label);
			label.setFormat(null,8,0x333333,"center");
			labelOffset = new FlxPoint(-1,3);
			label.x = x + labelOffset.x; // add this.
			label.y = y + labelOffset.y; // add this.
		}
		loadGraphic(SystemAsset.ImgButton,true,false,80,20);
		callback = Callback;
		
		soundOver = null;
		soundOut = null;
		soundDown = null;
		soundUp = null;

		status = NORMAL;
		_onToggle = false;
		_pressed = false;
		_initialized = false;
		updateButton();
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
		if(FlxG._game != null)
			FlxG._game.removeObserver(this);
		if(label != null)
		{
			label.destroy();
			label = null;
		}
		callback = null;

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
	public void preUpdate()
	{		
		super.preUpdate();		
		if(!_initialized)
		{
			if(FlxG._game != null)
			{
				FlxG._game.addObserver(this);
				_initialized = true;
			}
		}
	}
	
	@Override
	public void update()
	{
		//updateButton(); //Basic button logic

		//Default button appearance is to simply update
		// the label appearance based on animation frame.
		if(label == null)
			return;
		switch(getFrame())
		{
			case HIGHLIGHT:	//Extra behavior to accommodate checkbox logic.
				label.setAlpha(1.0f);
				break;
			case PRESSED:
				label.setAlpha(0.5f);
				label.y = y+labelOffset.y+1;
				break;
			case NORMAL:
				label.x = x + labelOffset.x;
				label.y = y + labelOffset.y;
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
		//Figure out if the button is highlighted or pressed or what
		// (ignore checkbox behavior for now).
		if(cameras == null)
			cameras = FlxG.cameras;
		FlxCamera camera;
		int i = 0;
		int l = cameras.size;
		int pointerId = 0;		
		int	totalPointers = FlxG.mouse.activePointers;
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
						if(callback != null)
							callback.onDown();
						if(soundDown != null)
							soundDown.play(true);
					}
					if(FlxG.mouse.pressed(pointerId))
					{
						status = PRESSED;
						if(callback != null)
							callback.onPressed();
					}
					if(FlxG.mouse.justReleased(pointerId) && visible && status == PRESSED)
					{
						status = HIGHLIGHT;
						if(Gdx.app.getType() == ApplicationType.Android)
							status = NORMAL;
						if(callback != null)		
							callback.onUp();
						if(soundUp != null)
							soundUp.play(true);
					}
					else if(status == NORMAL)
					{
						status = HIGHLIGHT;
						if(callback != null)
							callback.onOver();
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
				if(callback != null)
					callback.onOut();
				if(soundOut != null)
					soundOut.play(true);
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
	
	/**
	 * Just draws the button graphic and text label to the screen.
	 */
	@Override
	public void draw(FlxCamera Camera)
	{
		super.draw(Camera);
		if(label != null)
		{
			label.scrollFactor =  scrollFactor;
			label.cameras = cameras;
			label.draw(Camera);
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
	 * @param SoundOver			What embedded sound effect to play when the mouse goes over the button. Default is null, or no sound.
	 * @param SoundOverVolume	How load the that sound should be.
	 * @param SoundOut			What embedded sound effect to play when the mouse leaves the button area. Default is null, or no sound.
	 * @param SoundOutVolume	How load the that sound should be.
	 * @param SoundDown			What embedded sound effect to play when the mouse presses the button down. Default is null, or no sound.
	 * @param SoundDownVolume	How load the that sound should be.
	 * @param SoundUp			What embedded sound effect to play when the mouse releases the button. Default is null, or no sound.
	 * @param SoundUpVolume		How load the that sound should be.
	 */
	public void setSounds(Sound SoundOver, float SoundOverVolume, Sound SoundOut, float SoundOutVolume, Sound SoundDown, float SoundDownVolume, Sound SoundUp, float SoundUpVolume)
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
	 * @param SoundOver			What embedded sound effect to play when the mouse goes over the button. Default is null, or no sound.
	 * @param SoundOverVolume	How load the that sound should be.
	 * @param SoundOut			What embedded sound effect to play when the mouse leaves the button area. Default is null, or no sound.
	 * @param SoundOutVolume	How load the that sound should be.
	 * @param SoundDown			What embedded sound effect to play when the mouse presses the button down. Default is null, or no sound.
	 * @param SoundDownVolume	How load the that sound should be.
	 * @param SoundUp			What embedded sound effect to play when the mouse releases the button. Default is null, or no sound.	 
	 */
	public void setSounds(Sound SoundOver, float SoundOverVolume, Sound SoundOut, float SoundOutVolume, Sound SoundDown, float SoundDownVolume, Sound SoundUp)
	{
		setSounds(SoundOver, SoundOverVolume, SoundOut, SoundOutVolume, SoundDown, SoundDownVolume, SoundUp, 1.0f);
	}
	
	/**
	 * Set sounds to play during mouse-button interactions.
	 * These operations can be done manually as well, and the public
	 * sound variables can be used after this for more fine-tuning,
	 * such as positional audio, etc.
	 * 
	 * @param SoundOver			What embedded sound effect to play when the mouse goes over the button. Default is null, or no sound.
	 * @param SoundOverVolume	How load the that sound should be.
	 * @param SoundOut			What embedded sound effect to play when the mouse leaves the button area. Default is null, or no sound.
	 * @param SoundOutVolume	How load the that sound should be.
	 * @param SoundDown			What embedded sound effect to play when the mouse presses the button down. Default is null, or no sound.
	 * @param SoundDownVolume	How load the that sound should be.	 
	 */
	public void setSounds(Sound SoundOver, float SoundOverVolume, Sound SoundOut, float SoundOutVolume, Sound SoundDown, float SoundDownVolume)
	{
		setSounds(SoundOver, SoundOverVolume, SoundOut, SoundOutVolume, SoundDown, SoundDownVolume, null, 1.0f);
	}
	
	/**
	 * Set sounds to play during mouse-button interactions.
	 * These operations can be done manually as well, and the public
	 * sound variables can be used after this for more fine-tuning,
	 * such as positional audio, etc.
	 * 
	 * @param SoundOver			What embedded sound effect to play when the mouse goes over the button. Default is null, or no sound.
	 * @param SoundOverVolume	How load the that sound should be.
	 * @param SoundOut			What embedded sound effect to play when the mouse leaves the button area. Default is null, or no sound.
	 * @param SoundOutVolume	How load the that sound should be.
	 * @param SoundDown			What embedded sound effect to play when the mouse presses the button down. Default is null, or no sound.	 
	 */
	public void setSounds(Sound SoundOver, float SoundOverVolume, Sound SoundOut, float SoundOutVolume, Sound SoundDown)
	{
		setSounds(SoundOver, SoundOverVolume, SoundOut, SoundOutVolume, SoundDown, 1.0f, null, 1.0f);
	}

	/**
	 * Set sounds to play during mouse-button interactions.
	 * These operations can be done manually as well, and the public
	 * sound variables can be used after this for more fine-tuning,
	 * such as positional audio, etc.
	 * 
	 * @param SoundOver			What embedded sound effect to play when the mouse goes over the button. Default is null, or no sound.
	 * @param SoundOverVolume	How load the that sound should be.
	 * @param SoundOut			What embedded sound effect to play when the mouse leaves the button area. Default is null, or no sound.
	 * @param SoundOutVolume	How load the that sound should be.	 
	 */
	public void setSounds(Sound SoundOver, float SoundOverVolume, Sound SoundOut, float SoundOutVolume)
	{
		setSounds(SoundOver, SoundOverVolume, SoundOut, SoundOutVolume, null, 1.0f, null, 1.0f);
	}
		
	/**
	 * Set sounds to play during mouse-button interactions.
	 * These operations can be done manually as well, and the public
	 * sound variables can be used after this for more fine-tuning,
	 * such as positional audio, etc.
	 * 
	 * @param SoundOver			What embedded sound effect to play when the mouse goes over the button. Default is null, or no sound.
	 * @param SoundOverVolume	How load the that sound should be.
	 * @param SoundOut			What embedded sound effect to play when the mouse leaves the button area. Default is null, or no sound.
	 */
	public void setSounds(Sound SoundOver, float SoundOverVolume, Sound SoundOut)
	{
		setSounds(SoundOver, SoundOverVolume, SoundOut, 1.0f, null, 1.0f, null, 1.0f);
	}
	
	/**
	 * Set sounds to play during mouse-button interactions.
	 * These operations can be done manually as well, and the public
	 * sound variables can be used after this for more fine-tuning,
	 * such as positional audio, etc.
	 * 
	 * @param SoundOver			What embedded sound effect to play when the mouse goes over the button. Default is null, or no sound.
	 * @param SoundOverVolume	How load the that sound should be.
	 */
	public void setSounds(Sound SoundOver, float SoundOverVolume)
	{
		setSounds(SoundOver, SoundOverVolume, null, 1.0f, null, 1.0f, null, 1.0f);
	}
	
	/**
	 * Set sounds to play during mouse-button interactions.
	 * These operations can be done manually as well, and the public
	 * sound variables can be used after this for more fine-tuning,
	 * such as positional audio, etc.
	 * 
	 * @param SoundOver			What embedded sound effect to play when the mouse goes over the button. Default is null, or no sound.	 
	 */
	public void setSounds(Sound SoundOver)
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
	 * @private
	 */
	public void setOn(boolean On)
	{
		_onToggle = On;
	}

	@Override
	public void updateListener()
	{
		updateButton();
	}	
}
