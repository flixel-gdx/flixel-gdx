package org.flixel.ui;


/**
 * A checkbox style button with only ON and OFF labeled.
 * 
 * @author Ka Wing Chin
 */
public class FlxSwitch extends FlxUITouchable
{	
	/**
	 * Creates a new <code>FlxSwitch</code> object.
	 * @param X
	 * @param Y
	 * @param ID
	 * @param skin
	 * @param Label
	 * @param Width
	 */
	public FlxSwitch(float X, float Y, String ID, FlxUISkin skin, String Label)
	{
		super(X, Y, skin, Label, 0);
		this.ID = ID;
		setOn(true);
	}
	
	/**
	 * Creates a new <code>FlxSwitch</code> object.
	 * @param X
	 * @param Y
	 * @param ID
	 * @param skin
	 * @param Label
	 * @param Width
	 */
	public FlxSwitch(float X, float Y, String ID, FlxUISkin skin)
	{
		this(X, Y, ID, skin, null);
	}
	
	/**
	 * Creates a new <code>FlxSwitch</code> object.
	 * @param X
	 * @param Y
	 * @param ID
	 */
	public FlxSwitch(float X, float Y, String ID)
	{
		this(X, Y, ID, null, null);
	}
}
