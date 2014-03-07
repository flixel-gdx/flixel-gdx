package org.flixel.ui;

/**
 * A skinnable tab. It cannot function without <code>FlxTabGroup</code>; Add
 * <code>FlxTab</code> to a group via <code>FlxTabGroup.addTab()</code>.
 * 
 * @author Ka Wing Chin
 */
public class FlxTab extends FlxUITouchable
{
	private final String ImgTab = "org/flixel/data/pack:tab";

	/**
	 * The group where the tab belongs to.
	 */
	FlxTabGroup _group;

	public FlxTab(FlxUISkin UISkin, String Label)
	{
		super(0, 0, UISkin, Label, 0, 48);
		setOn(true);
		origin.x = origin.y = 0;
	}

	@Override
	public void setDefaultSkin()
	{
		skin = new FlxUISkin();
		skin.HIGHLIGHT = 1;
		skin.PRESSED = 2;
		skin.ACTIVE_NORMAL = 1;
		skin.setFormat(null, 8, 0xFFFFFF, "center");
		skin.labelVerticalAlign = "middle";
		skin.setImage(ImgTab, 1, 48);
		skin.labelOffset.y = 0;
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
