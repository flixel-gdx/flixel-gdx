package org.flixel.examples.vibration;

import org.flixel.FlxButton;
import org.flixel.FlxG;
import org.flixel.FlxState;
import org.flixel.event.AFlxButton;

/**
 * A simple demo how vibration works.
 * 
 * @author Ka Wing Chin
 */
public class PlayState extends FlxState
{

	@Override
	public void create()
	{
		FlxG.setBgColor(0xFF000000);
		add(new FlxButton(20, 20, "vibrate 1 sec", vibrate));
		add(new FlxButton(20, 60, "1,2,3 repeat", repeat));
		add(new FlxButton(120, 20, "stop vibrate", stop));
	}

	AFlxButton vibrate = new AFlxButton()
	{
		@Override
		public void onUp()
		{
			FlxG.vibrate();
		}
	};
	
	
	AFlxButton repeat = new AFlxButton()
	{
		@Override
		public void onUp()
		{
			FlxG.vibrate(new long[]{1000,2000,3000}, 1);
		}
	};
	
	
	AFlxButton stop = new AFlxButton()
	{
		@Override
		public void onUp()
		{
			FlxG.stopVibrate();
		}
	}; 
}
