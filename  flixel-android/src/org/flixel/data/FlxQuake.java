package org.flixel.data;

import org.flixel.FlxG;


/**
 * This is a special effects utility class to help FlxGame do the 'quake' or screenshake effect.
 */
public class FlxQuake
{
	/**
	 * The game's level of zoom.
	 */
	protected int _zoom;
	/**
	 * The intensity of the quake effect: a percentage of the screen's size.
	 */
	protected float _intensity;
	/**
	 * Set to countdown the quake time.
	 */
	protected float _timer;
	
	/**
	 * The amount of X distortion to apply to the screen.
	 */
	public int x;
	/**
	 * The amount of Y distortion to apply to the screen.
	 */
	public int y;

	/**
	 * Constructor.
	 */
	public FlxQuake(int Zoom)
	{
		_zoom = Zoom;
		start(0);
	}

	/**
	 * Reset and trigger this special effect.
	 * 
	 * @param	Intensity	Percentage of screen size representing the maximum distance that the screen can move during the 'quake'.
	 * @param	Duration	The length in seconds that the "quake" should last.
	 */
	public void start(float Intensity, float Duration)
	{
		stop();
		_intensity = Intensity;
		_timer = Duration;
	}
	
	public void start(float Intensity)
	{
		start(Intensity, 0.5f);
	}
	
	public void start()
	{
		start(0.05f, 0.5f);
	}
	
	
	
	/**
	 * Stops this screen effect.
	 */
	public void stop()
	{
		x = 0;
		y = 0;
		_intensity = 0;
		_timer = 0;
	}

	
	/**
	 * Updates and/or animates this special effect.
	 */
	public void update()
	{
		if(_timer > 0)
		{
			_timer -= FlxG.elapsed;
			if(_timer <= 0)
			{
				_timer = 0;
				x = 0;
				y = 0;
			}
			else
			{
				x = (int) ((Math.random()*_intensity*FlxG.width*2-_intensity*FlxG.width)*1);
				y = (int) ((Math.random()*_intensity*FlxG.height*2-_intensity*FlxG.height)*1);
			}
		}		
	}

}
