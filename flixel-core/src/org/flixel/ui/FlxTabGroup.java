package org.flixel.ui;

import org.flixel.FlxG;
import org.flixel.FlxGroup;
import org.flixel.FlxObject;
import org.flixel.FlxSprite;
import org.flixel.ui.event.IFlxTab;

/**
 * A group that holds <FlxTab>s. It automatically resize the tabs to fit in the
 * given width. The tabs can also be set vertically.
 * 
 * @author Ka Wing Chin
 */
public class FlxTabGroup extends FlxUIGroup
{
	private final String ImgDivider = "org/flixel/data/pack:tab_divider";
	private final String ImgDividerVertical = "org/flixel/data/pack:tab_divider_vertical";

	/**
	 * The divider skin.
	 */
	private FlxUISkin _dividerSkin;
	/**
	 * A group that holds the tabs.
	 */
	private FlxGroup _tabs;
	/**
	 * A group that holds the dividers.
	 */
	private FlxGroup _dividers;
	/**
	 * A group that holds the contents.
	 */
	private FlxGroup _content;
	/**
	 * Tracks the current tab.
	 */
	private int _current = 0;
	/**
	 * The width of the tab group.
	 */
	public int width;
	/**
	 * The height of the tab group.
	 */
	public int height;
	/**
	 * The tab width when placed vertically.
	 */
	public int tabWidth;
	/**
	 * Callback when the tab got changed.
	 */
	public IFlxTab onChange;

	/**
	 * Create a new <code>FlxTabGroup</code> object.
	 * 
	 * @param X The x-position.
	 * @param Y The y-position.
	 * @param Width The width of the tab group when placed horizontally. Default
	 *        0, the width of the screen in game pixels will be used.
	 * @param Height The height of the tab group when placed vertically. Default
	 *        0, the height of the screen in game pixels will be used.
	 * @param TabWidth The width of the tab when placed vertically. Default 0,
	 *        is 70 px.
	 */
	public FlxTabGroup(float X, float Y, int Width, int Height, int TabWidth)
	{
		super(X, Y, "");
		x = X;
		y = Y;
		width = (Width == 0) ? FlxG.width : Width;
		height = (Height == 0) ? FlxG.height : Height;
		tabWidth = (TabWidth == 0) ? 70 : TabWidth;
		label.setSize(12);
		add(_content = new FlxGroup());
		add(_tabs = new FlxGroup());
		add(_dividers = new FlxGroup());

		align = ALIGN_HORIZONTAL;
		setDefault(0);
	}

	/**
	 * Create a new <code>FlxTabGroup</code> object.
	 * 
	 * @param X The x-position.
	 * @param Y The y-position.
	 * @param Width The width of the tab group when placed horizontally. Default
	 *        0, the width of the screen in game pixels will be used.
	 * @param Height The height of the tab group when placed vertically. Default
	 *        0, the height of the screen in game pixels will be used.
	 */
	public FlxTabGroup(float X, float Y, int Width, int Height)
	{
		this(X, Y, Width, Height, 0);
	}

	/**
	 * Create a new <code>FlxTabGroup</code> object.
	 * 
	 * @param X The x-position.
	 * @param Y The y-position.
	 * @param Width The width of the tab group when placed horizontally. Default
	 *        0, the width of the screen in game pixels will be used.
	 */
	public FlxTabGroup(float X, float Y, int Width)
	{
		this(X, Y, 0, 0, 0);
	}

	/**
	 * Create a new <code>FlxTabGroup</code> object.
	 * 
	 * @param X The x-position.
	 * @param Y The y-position.
	 */
	public FlxTabGroup(float X, float Y)
	{
		this(X, Y, 0, 0, 0);
	}

	/**
	 * Create a new <code>FlxTabGroup</code> object.
	 * 
	 * @param X The x-position.
	 */
	public FlxTabGroup(float X)
	{
		this(X, 0, 0, 0, 0);
	}

	/**
	 * Create a new <code>FlxTabGroup</code> object.
	 */
	public FlxTabGroup()
	{
		this(0, 0, 0, 0, 0);
	}

	@Override
	public void destroy()
	{
		super.destroy();
		if(_dividerSkin != null)
			_dividerSkin.destroy();
		_dividerSkin = null;
		_tabs = null;
		_dividers = null;
		_content = null;
		onChange = null;
	}

	/**
	 * Add tab to the group.
	 * 
	 * @param tab
	 */
	public void addTab(FlxTab tab)
	{
		_tabs.add(tab);
		tab._group = this;
		FlxGroup group = new FlxGroup();
		_content.add(group);
		add(group);

		if(_dividerSkin != null && _tabs.length > 1)
		{
			_dividers.add(new FlxSprite(0, 0).loadGraphic(_dividerSkin.image, false, false, _dividerSkin.width, _dividerSkin.height));
		}

		resetTabs();
		if(_current == 0)
			setDefault(_current);
	}

	/**
	 * Remove a tab by index.
	 * 
	 * @param Index
	 */
	public void removeTab(int Index)
	{
		if(Index > _tabs.length - 1)
			return;

		_tabs.remove(_tabs.members.get(Index), true);
		_content.members.removeIndex(Index);
		if(Index == _current)
			setDefault(0);
		resetTabs();
	}

	/**
	 * Resets the layout of the tabs.
	 */
	private void resetTabs()
	{
		FlxSprite divider;
		FlxUIComponent tab;
		int length = _tabs.length;
		int tWidth = (align == ALIGN_HORIZONTAL) ? this.width / length : tabWidth;
		for(int i = 0; i < length; i++)
		{
			tab = (FlxUIComponent) _tabs.members.get(i);
			tab.width = tWidth;
			tab.scale.x = tWidth;
			tab.x = x;
			tab.y = y;
			if(align == ALIGN_HORIZONTAL)
				tab.x += i * tWidth;
			else if(align == ALIGN_VERTICAL)
				tab.y += i * tab.height;
			tab.label.width = tWidth;
			tab.label.setAlignment(tab.skin.labelAlign);

			if(_dividerSkin != null && i < _tabs.length - 1)
			{
				if(align == ALIGN_HORIZONTAL)
				{
					divider = (FlxSprite) _dividers.members.get(i);
					divider.x = tab.x + tab.width;
					divider.y = tab.y;
				}
				else
				{
					divider = (FlxSprite) _dividers.members.get(i);
					divider.x = tab.x;
					divider.y = tab.y + tab.height;
				}
			}
		}
	}

	/**
	 * Load a skin for the divider.
	 * 
	 * @param Skin The skin for the divider.
	 * @param Width The width of the skin.
	 * @param Height The height of the skin.
	 */
	public void loadDividerSkin(String Skin, int Width, int Height)
	{
		if(_dividerSkin == null)
			_dividerSkin = new FlxUISkin();
		_dividerSkin.setImage(Skin, Width, Height);
		if(_dividers.length < _tabs.length)
		{
			for(int i = 0; i < _tabs.length - 1; i++)
			{
				_dividers.add(new FlxSprite(0, 0).loadGraphic(Skin, false, false, Width, Height));
			}
			resetTabs();
		}
	}

	/**
	 * Load a skin for the divider.
	 * 
	 * @param Skin The skin for the divider.
	 */
	public void loadDividerSkin(FlxUISkin Skin)
	{
		loadDividerSkin(Skin.image, Skin.width, Skin.height);
	}

	/**
	 * Load the default skin for the divider.
	 */
	public void loadDividerSkin()
	{
		if(align == ALIGN_HORIZONTAL)
			loadDividerSkin(ImgDivider, 1, 48);
		else
			loadDividerSkin(ImgDividerVertical, 70, 1);
	}

	/**
	 * Add content at given index.
	 * 
	 * @param Index The index where the object needs to be stored.
	 * @param Object The object that needs to be added.
	 */
	public void addContent(int Index, FlxObject Object)
	{
		if(Index > _tabs.length - 1)
			return;
		if(_current != Index)
			Object.visible = false;
		((FlxGroup) _content.members.get(Index)).add(Object);
	}

	/**
	 * This method will be called when tab got changed.
	 * 
	 * @param tab The tab that will be set active.
	 */
	protected void onChange(FlxTab tab)
	{
		// Break if it's already selected.
		if(_current == _tabs.members.indexOf(tab, true))
		{
			tab.setActive(true);
			return;
		}

		FlxTab object;
		FlxGroup group;
		for(int i = 0; i < _tabs.members.size; i++)
		{
			object = (FlxTab) _tabs.members.get(i);
			object.setActive(false);

			group = (FlxGroup) _content.members.get(i);
			for(int ii = 0; ii < group.length; ii++)
				group.members.get(ii).visible = false;
		}
		tab.setActive(true);
		_current = _tabs.members.indexOf(tab, true);

		group = (FlxGroup) _content.members.get(_current);
		for(int i = 0; i < group.length; i++)
			group.members.get(i).visible = true;

		if(onChange != null)
			onChange.callback();
	}

	/**
	 * Set the default tab at given index.
	 * 
	 * @param Index The index of the tab that needs to be set default.
	 */
	public void setDefault(int Index)
	{
		if(Index > _tabs.length - 1)
			return;

		int i = 0;
		while(i < _tabs.length)
		{
			((FlxUIComponent) _tabs.members.get(i)).setActive(false);
			((FlxGroup) _content.members.get(i)).setAll("visible", false);
			++i;
		}
		((FlxUIComponent) _tabs.members.get(Index)).setActive(true);
		((FlxGroup) _content.members.get(Index)).setAll("visible", true);
		_current = Index;
	}
}
