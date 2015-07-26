package org.flixel;

import org.flixel.event.IFlxTimer;
import org.flixel.plugin.TimerManager;

/**
 * A simple timer class, leveraging the new plugins system.
 * Can be used with callbacks or by polling the <code>finished</code> flag.
 * Not intended to be added to a game state or group; the timer manager
 * is responsible for actually calling update(), not the user.
 * 
 * @author Ka Wing Chin
 */
public class FlxTimer
{
	/**
	 * How much time the timer was set for.
	 */
	public float time;
	/**
	 * How many loops the timer was set for.
	 */
	public int loops;
	/**
	 * Pauses or checks the pause state of the timer.
	 */
	public boolean paused;
	/**
	 * Check to see if the timer is finished.
	 */
	public boolean finished;

	/**
	 * Internal tracker for the time's-up callback function.
	 * Callback should be formed "onTimer(Timer:FlxTimer);"
	 */
	protected IFlxTimer _callback;
	/**
	 * Internal tracker for the actual timer counting up.
	 */
	protected float _timeCounter;
	/**
	 * Internal tracker for the loops counting up.
	 */
	protected int _loopsCounter;

	/**
	 * Instantiate the timer.  Does not set or start the timer.
	 */
	public FlxTimer()
	{
		time = 0;
		loops = 0;
		_callback = null;
		_timeCounter = 0;
		_loopsCounter = 0;

		paused = false;
		finished = false;
	}

	/**
	 * Clean up memory.
	 */
	public void destroy()
	{
		stop();
		_callback = null;
	}

	/**
	 * Called by the timer manager plugin to update the timer.
	 * If time runs out, the loop counter is advanced, the timer reset, and the callback called if it exists.
	 * If the timer runs out of loops, then the timer calls <code>stop()</code>.
	 * However, callbacks are called AFTER <code>stop()</code> is called.
	 */
	public void update()
	{
		_timeCounter += FlxG.elapsed;
		while((_timeCounter >= time) && !paused && !finished)
		{
			_timeCounter -= time;

			_loopsCounter++;
			if((loops > 0) && (_loopsCounter >= loops))
				stop();

			if(_callback != null)
				_callback.callback(this);
		}
	}

	/**
	 * Starts or resumes the timer. If this timer was paused,
	 * then all the parameters are ignored, and the timer is resumed.
	 * Adds the timer to the timer manager.
	 * 
	 * @param	Time		How many seconds it takes for the timer to go off.
	 * @param	Loops		How many times the timer should go off.  Default is 1, or "just count down once."
	 * @param	Callback	Optional, triggered whenever the time runs out, once for each loop.  Callback should be formed "onTimer(Timer:FlxTimer);"
	 * 
	 * @return	A reference to itself (handy for chaining or whatever).
	 */
	public FlxTimer start(float Time,int Loops,IFlxTimer Callback)
	{
		TimerManager timerManager = getManager();
		if(timerManager != null)
			timerManager.add(this);

		if(paused)
		{
			paused = false;
			return this;
		}

		paused = false;
		finished = false;
		time = Time;
		loops = Loops;
		_callback = Callback;
		_timeCounter = 0;
		_loopsCounter = 0;
		return this;
	}

	/**
	 * Starts or resumes the timer. If this timer was paused,
	 * then all the parameters are ignored, and the timer is resumed.
	 * Adds the timer to the timer manager.
	 * 
	 * @param	Time		How many seconds it takes for the timer to go off.
	 * @param	Loops		How many times the timer should go off.  Default is 1, or "just count down once."
	 * 
	 * @return	A reference to itself (handy for chaining or whatever).
	 */
	public FlxTimer start(float Time,int Loops)
	{
		return start(Time,Loops,null);
	}

	/**
	 * Starts or resumes the timer. If this timer was paused,
	 * then all the parameters are ignored, and the timer is resumed.
	 * Adds the timer to the timer manager.
	 * 
	 * @param	Time		How many seconds it takes for the timer to go off.
	 * 
	 * @return	A reference to itself (handy for chaining or whatever).
	 */
	public FlxTimer start(float Time)
	{
		return start(Time,1,null);
	}

	/**
	 * Starts or resumes the timer. If this timer was paused,
	 * then all the parameters are ignored, and the timer is resumed.
	 * Adds the timer to the timer manager.
	 *  
	 * @return	A reference to itself (handy for chaining or whatever).
	 */
	public FlxTimer start()
	{
		return start(1,1,null);
	}

	/**
	 * Stops the timer and removes it from the timer manager.
	 */
	public void stop()
	{
		finished = true;
		TimerManager timerManager = getManager();
		if(timerManager != null)
			timerManager.remove(this);
	}

	/**
	 * Read-only: check how much time is left on the timer.
	 */
	public float getTimeLeft()
	{
		return time-_timeCounter;
	}

	/**
	 * Read-only: check how many loops are left on the timer.
	 */
	public int getLoopsLeft()
	{
		return loops-_loopsCounter;
	}

	/**
	 * Read-only: how far along the timer is, on a scale of 0.0 to 1.0.
	 */
	public float getProgress()
	{
		if(time > 0)
			return _timeCounter/time;
		else
			return 0;
	}

	static public TimerManager getManager()
	{
		return (TimerManager) FlxG.getPlugin(TimerManager.class);
	}
}
