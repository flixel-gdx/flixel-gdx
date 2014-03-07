package org.flixel.ui;

/**
 * A checkbox style button with only ON and OFF labeled.
 * 
 * @author Ka Wing Chin
 */
public class FlxSwitch extends FlxUITouchable
{
	private final String ImgSwitch = "org/flixel/data/pack:switch";

	/**
	 * Creates a new <code>FlxSwitch</code> object.
	 * 
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
	 * 
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
	 * 
	 * @param X
	 * @param Y
	 * @param ID
	 */
	public FlxSwitch(float X, float Y, String ID)
	{
		this(X, Y, ID, null, null);
	}

	@Override
	public void setDefaultSkin()
	{
		skin = new FlxUISkin();
		skin.NORMAL = 0;
		skin.PRESSED = -1;
		skin.HIGHLIGHT = -1;
		skin.DISABLED = -1;
		skin.ACTIVE_NORMAL = 1;
		skin.labelPosition = FlxUISkin.LABEL_LEFT;
		skin.setFormat(null, 16);
		skin.setImage(ImgSwitch, 107, 34);
	}
}
