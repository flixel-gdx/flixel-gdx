package org.flixel.ui;


/**
 *
 * @author Ka Wing Chin
 */
public class FlxCheckBox extends FlxUITouchable
{	
	public FlxCheckBox(float X, float Y, String ID, FlxUISkin UISkin, String Label, int Width)
	{
		super(X, Y, UISkin, Label, Width);
		this.ID = ID;
		setOn(true);
	}
	
	public FlxCheckBox(float X, float Y, String ID, FlxUISkin UISkin, String Label)
	{
		this(X, Y, ID, UISkin, Label, 0);
	}
}

