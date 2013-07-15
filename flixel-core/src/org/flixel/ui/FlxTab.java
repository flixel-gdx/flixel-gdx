package org.flixel.ui;

/**
 * A skinnable tab. It cannot function without <code>FlxTabGroup</code>;
 * Add <code>FlxTab</code> to a group via <code>FlxTabGroup.addTab()</code>.
 * 
 * @author  Ka Wing Chin
 */
public class FlxTab extends FlxUITouchable
{
	/**
	 * The group where the tab belongs to.
	 */
	FlxTabGroup _group;

	public FlxTab(FlxUISkin UISkin, String Label)
	{
		super(0, 0, UISkin, Label);
		setOn(true);
		origin.x = origin.y = 0;
	}
	
	@Override
	public void destroy()
	{
		super.destroy();
		_group = null;
	}
	
	@Override
	protected void onChange()
	{
		_group.onChange(this);
	}
}
