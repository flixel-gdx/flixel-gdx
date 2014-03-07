package org.flixel.ui;

/**
 * Skinnable Checkbox
 * 
 * @author Ka Wing Chin
 */
public class FlxCheckBox extends FlxUITouchable
{
	private final String ImgCheckBox = "org/flixel/data/pack:checkbox";

	/**
	 * Creates a new <code>FlxCheckbox</code> object.
	 * 
	 * @param X The x-position of the component.
	 * @param Y The y-position of the component.
	 * @param ID The unique ID of the component.
	 * @param UISkin The skin that needs to be applied.
	 * @param Label The label along side the component.
	 */
	public FlxCheckBox(float X, float Y, String ID, FlxUISkin UISkin, String Label)
	{
		super(X, Y, UISkin, Label, 0);
		this.ID = ID;
		setOn(true);
	}

	/**
	 * Creates a new <code>FlxCheckbox</code> object.
	 * 
	 * @param X The x-position of the component.
	 * @param Y The y-position of the component.
	 * @param ID The unique ID of the component.
	 * @param UISkin The skin that needs to be applied.
	 */
	public FlxCheckBox(float X, float Y, String ID, FlxUISkin UISkin)
	{
		this(X, Y, ID, UISkin, null);
	}

	/**
	 * Creates a new <code>FlxCheckbox</code> object.
	 * 
	 * @param X The x-position of the component.
	 * @param Y The y-position of the component.
	 * @param ID The unique ID of the component.
	 */
	public FlxCheckBox(float X, float Y, String ID)
	{
		this(X, Y, ID, null, null);
	}

	@Override
	public void setDefaultSkin()
	{
		skin = new FlxUISkin();
		skin.DISABLED = 3;
		skin.HIGHLIGHT_DISABLED = 4;
		skin.ACTIVE_NORMAL = 5;
		skin.ACTIVE_HIGHTLIGHT = 6;
		skin.ACTIVE_PRESSED = 7;
		skin.ACTIVE_DISABLED = 8;
		skin.ACTIVE_HIGHTLIGHT_DISABLED = 9;
		skin.labelPosition = FlxUISkin.LABEL_RIGHT;
		skin.setImage(ImgCheckBox, 32, 32);
	}
}
