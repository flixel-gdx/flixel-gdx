package org.flixel.ui;


/**
 * 
 * @author Ka Wing Chin
 */
public class FlxSwitch extends FlxUITouchable
{	
	public FlxSwitch(float X, float Y, FlxUISkin skin, String Label, int Width)
	{
		super(X, Y, skin, Label, Width);		
		setOn(true);
	}
	
	public FlxSwitch(float X, float Y, FlxUISkin skin, String Label)
	{
		this(X, Y, skin, Label, 0);
	}
}
