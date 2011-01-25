package org.flixel.data;

import org.flixel.FlxG;
import org.flixel.FlxSprite;


/**
 * This is a special effects utility class to help FlxGame do the 'fade' effect.
 */
public class FlxFade extends FlxSprite
{

	/**
	 * How long the effect should last.
	 */
	protected float _delay;
	/**
	 * Callback for when the effect is finished.
	 */
	protected FlxFadeListener _complete;
	public float delay = 0;
	
	
	/**
	 * Constructor initializes the fade object
	 */
	public FlxFade()
	{
		super();
		createGraphic(FlxG.width,FlxG.height,0,true);
		scrollFactor.x = 0;
		scrollFactor.y = 0;
		exists = false;
		setSolid(false);
		setFixed(true);
	}
	
	/**
	 * Reset and trigger this special effect
	 * 
	 * @param	Color			The color you want to use
	 * @param	Duration		How long it should take to fade the screen out
	 * @param	FadeComplete	A function you want to run when the fade finishes
	 * @param	Force			Force the effect to reset
	 */
	public void start(int Color, float Duration, FlxFadeListener FadeComplete, boolean Force)
	{
		if(!Force && exists)
			return;
		fill(Color);
		_delay = Duration;
		_complete = FadeComplete;
		setAlpha(0);
		exists = true;
	}
	
	/**
	 * Reset and trigger this special effect
	 * 
	 * @param	Color			The color you want to use
	 * @param	Duration		How long it should take to fade the screen out
	 * @param	FadeComplete	A function you want to run when the fade finishes
	 */
	public void start(int Color, float Duration, FlxFadeListener FadeComplete)
	{
		start(Color, Duration, FadeComplete, false);
	}
	
	/**
	 * Reset and trigger this special effect
	 * 
	 * @param	Color			The color you want to use
	 * @param	Duration		How long it should take to fade the screen out
	 */
	public void start(int Color, float Duration)
	{
		start(Color, Duration, null, false);
	}
	
	/**
	 * Reset and trigger this special effect
	 * 
	 * @param	Color			The color you want to use
	 */
	public void start(int Color)
	{
		start(Color, 1, null, false);
	}
	
	/**
	 * Reset and trigger this special effect
	 */
	public void start()
	{
		start(0xFF000000, 1, null, false);
	}
	
	/**
	 * Stops and hides this screen effect.
	 */
	public void stop()
	{
		exists = false;
	}

	/**
	 * Updates and/or animates this special effect
	 */
	@Override
	public void update()
	{
		if(delay > 0)
		{
			delay -= FlxG.elapsed;
			return;
		}
		
		setAlpha(getAlpha() + FlxG.elapsed/_delay);
		if(getAlpha() >= 1)
		{
			setAlpha(1);
			if(_complete != null)
				_complete.fadeComplete();
		}
	}

}
