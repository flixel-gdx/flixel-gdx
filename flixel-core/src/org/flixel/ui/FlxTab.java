package org.flixel.ui;

/**
 * @author  Ka Wing Chin
 */
public class FlxTab extends FlxUITouchable
{
	FlxTabGroup _group;

	public FlxTab(FlxUISkin UISkin, String Label)
	{
		super(0, 0, UISkin, Label);
		setOn(true);
		origin.x = origin.y = 0;
	}
	
	@Override
	protected void onChange()
	{
		_group.onChange(this);
	}
}
