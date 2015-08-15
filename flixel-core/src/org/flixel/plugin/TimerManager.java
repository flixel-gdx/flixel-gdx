package org.flixel.plugin;

import org.flixel.FlxBasic;
import org.flixel.FlxTimer;

import com.badlogic.gdx.utils.Array;

/**
 * A simple manager for tracking and updating game timer objects.
 * 
 * @author Ka Wing Chin
 */
public class TimerManager extends FlxBasic
{
	protected Array<FlxTimer> _timers;

	/**
	 * Instantiates a new timer manager.
	 */
	public TimerManager()
	{
		_timers = new Array<FlxTimer>();
		visible = false; //don't call draw on this plugin
	}

	/**
	 * Clean up memory.
	 */
	@Override
	public void destroy()
	{
		clear();
		_timers = null;
	}

	/**
	 * Called by <code>FlxG.updatePlugins()</code> before the game state has been updated.
	 * Cycles through timers and calls <code>update()</code> on each one.
	 */
	@Override
	public void update()
	{
		int i = _timers.size-1;
		FlxTimer timer;
		while(i >= 0)
		{
			timer = _timers.get(i--);
			if((timer != null) && !timer.paused && !timer.finished && (timer.time > 0))
				timer.update();
		}
	}

	/**
	 * Add a new timer to the timer manager.
	 * Usually called automatically by <code>FlxTimer</code>'s constructor.
	 * 
	 * @param	Timer	The <code>FlxTimer</code> you want to add to the manager.
	 */
	public void add(FlxTimer Timer)
	{
		_timers.add(Timer);
	}

	/**
	 * Remove a timer from the timer manager.
	 * Usually called automatically by <code>FlxTimer</code>'s <code>stop()</code> function.
	 * 
	 * @param	Timer	The <code>FlxTimer</code> you want to remove from the manager.
	 */
	public void remove(FlxTimer Timer)
	{
		int index = _timers.indexOf(Timer, true);
		if(index >= 0)
			_timers.removeIndex(index);
	}

	/**
	 * Removes all the timers from the timer manager.
	 */
	public void clear()
	{
		int i = _timers.size - 1;
		FlxTimer timer;
		while(i >= 0)
		{
			timer = _timers.get(i--);
			if(timer != null)
				timer.destroy();
		}
		_timers.clear();
	}
}
