package org.flixel.ui;


/**
 *
 * @author Ka Wing Chin
 */
public class FlxRadioButton extends FlxUITouchable
{	
	public boolean isChecked;
	public FlxRadioButtonGroup group;

	public FlxRadioButton(float X, float Y, String ID, FlxUISkin UISkin, FlxRadioButtonGroup Group, String Label)
	{
		super(X, Y, UISkin, Label);
		this.ID = ID;
		group = Group;
		setOn(true);
		group.add(this);
		skin.labelPosition = FlxUISkin.LABEL_RIGHT;
		skin.DISABLED = 3;
		skin.HIGHLIGHT_DISABLED = 4;
		skin.ACTIVE_NORMAL = 5;
		skin.ACTIVE_HIGHTLIGHT = 6;
		skin.ACTIVE_PRESSED = 7;
		skin.ACTIVE_DISABLED = 8;
		skin.ACTIVE_HIGHTLIGHT_DISABLED = 9;
	}
	
	@Override
	public void destroy()
	{
		super.destroy();
		group = null;
	}
		
	private void toggleOn()
	{
		group.onChange(this);
	}
	
	@Override
	public void onChange()
	{
		toggleOn();
	}
}

