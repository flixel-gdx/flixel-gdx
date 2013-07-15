package org.flixel.ui;


/**
 * Skinnable Checkbox
 * 
 * @author Ka Wing Chin
 */
public class FlxCheckBox extends FlxUITouchable
{	
	/**
	 * Creates a new <code>FlxCheckbox</code> object.
	 * @param X			The x-position of the component.
	 * @param Y			The y-position of the component.
	 * @param ID		The unique ID of the component.
	 * @param UISkin	The skin that needs to be applied.
	 * @param Label		The label along side the component.
	 */
	public FlxCheckBox(float X, float Y, String ID, FlxUISkin UISkin, String Label)
	{
		super(X, Y, UISkin, Label, 0);
		this.ID = ID;
		setOn(true);
	}
	
	/**
	 * Creates a new <code>FlxCheckbox</code> object.
	 * @param X			The x-position of the component.
	 * @param Y			The y-position of the component.
	 * @param ID		The unique ID of the component.
	 * @param UISkin	The skin that needs to be applied.
	 */
	public FlxCheckBox(float X, float Y, String ID, FlxUISkin UISkin)
	{
		this(X, Y, ID, UISkin, null);
	}
	
	/**
	 * Creates a new <code>FlxCheckbox</code> object.
	 * @param X			The x-position of the component.
	 * @param Y			The y-position of the component.
	 * @param ID		The unique ID of the component.
	 */
	public FlxCheckBox(float X, float Y, String ID)
	{
		this(X, Y, ID, null, null);
	}
}

