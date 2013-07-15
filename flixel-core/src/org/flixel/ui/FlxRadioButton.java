package org.flixel.ui;


/**
 * Skinnable Radiobutton.
 * 
 * @author Ka Wing Chin
 */
public class FlxRadioButton extends FlxUITouchable
{	
	/**
	 * The group where this radio button belongs to.
	 */
	public FlxRadioButtonGroup group;

	/**
	 * Creates a new <code>FlxRadioButton</code> object.
	 * @param X			The x-position of the component.
	 * @param Y			The y-position of the component.
	 * @param ID		The unique ID of the component.
	 * @param Group		The group where the component belongs to.
	 * @param UISkin	The skin that needs to be applied.
	 * @param Label		The label along side the component.
	 */
	public FlxRadioButton(float X, float Y, String ID, FlxRadioButtonGroup Group, FlxUISkin UISkin, String Label)
	{
		super(X, Y, UISkin, Label);
		this.ID = ID;
		group = Group;
		setOn(true);
		group.add(this);
	}
	
	/**
	 * Creates a new <code>FlxRadioButton</code> object.
	 * @param X			The x-position of the component.
	 * @param Y			The y-position of the component.
	 * @param ID		The unique ID of the component.
	 * @param Group		The group where the component belongs to.
	 * @param UISkin	The skin that needs to be applied.
	 */
	public FlxRadioButton(float X, float Y, String ID, FlxRadioButtonGroup Group, FlxUISkin UISkin)
	{
		super(X, Y, UISkin, null);
		this.ID = ID;
		group = Group;
		setOn(true);
		group.add(this);
	}
	
	/**
	 * Creates a new <code>FlxRadioButton</code> object.
	 * @param X			The x-position of the component.
	 * @param Y			The y-position of the component.
	 * @param ID		The unique ID of the component.
	 * @param Group		The group where the component belongs to.
	 */
	public FlxRadioButton(float X, float Y, String ID, FlxRadioButtonGroup Group)
	{
		super(X, Y, null, null);
		this.ID = ID;
		group = Group;
		setOn(true);
		group.add(this);
	}
	
	
	@Override
	public void destroy()
	{
		super.destroy();
		group = null;
	}
		
	/**
	 * Let the group knows to change the behavior of the radio buttons.
	 */
	private void toggleOn()
	{
		group.onChange(this);
	}
	
	/**
	 * Set the radio button active.
	 */
	@Override
	public void onChange()
	{
		toggleOn();
	}
}

