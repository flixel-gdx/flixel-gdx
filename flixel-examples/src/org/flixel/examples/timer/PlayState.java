package org.flixel.examples.timer;

import org.flixel.FlxState;
import org.flixel.FlxText;
import org.flixel.FlxTimer;
import org.flixel.event.AFlxTimer;


/**
 * A short demo how to use the methods for the plugins.
 * In this demo the TimerManager is used.
 * 
 * @author Ka Wing Chin
 */
public class PlayState extends FlxState
{

	private FlxTimer _timer;
	private FlxText _text;
	private boolean _complete;

	@Override
	public void create()
	{
		// The timer class.
		_timer = new FlxTimer();
		_timer.start(3, 1, stop);
		
		// Removes an instance of a plugin from the global plugin array.		
		//FlxG.removePlugin(FlxTimer.getManager());
		
		// Removes an instance of a plugin from the global plugin array.
		//FlxG.removePluginType(TimerManager.class);
		
		// Removes an instance of a plugin from the global plugin array.
		//FlxG.getPlugin(TimerManager.class);
		
		_text = new FlxText(10, 10, 200, "Time: ");
		add(_text);
	}
	
	
	@Override
	public void update()
	{
		if(!_complete)
			_text.setText("Time: " + _timer.getTimeLeft());
		super.update();
	}
	
	
	AFlxTimer stop = new AFlxTimer()
	{
		@Override
		public void onComplete(FlxTimer flxTimer)
		{
			_text.setText("Time: complete");
			_complete = true;
		}
	};
}
