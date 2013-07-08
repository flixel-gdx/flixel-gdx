package org.flixel.ui;

import org.flixel.FlxG;
import org.flixel.FlxGroup;
import org.flixel.FlxObject;
import org.flixel.FlxSprite;
import org.flixel.ui.event.IFlxTab;

/**
 *
 * @author Ka Wing Chin
 */
public class FlxTabGroup extends FlxUIGroup
{
	private FlxUISkin _dividerSkin;
	private FlxGroup _tabs;
	private FlxGroup _dividers;
	private FlxGroup _content;
	private int _current = 0;
	public IFlxTab onChange;
	public int width;

	public FlxTabGroup(float X, float Y, int Width)
	{
		super(X, Y, "");
		x = X;
		y = Y;
		width = Width;
		label.setSize(12);		
		add(_content = new FlxGroup());
		add(_tabs = new FlxGroup());
		add(_dividers = new FlxGroup());
		
		align = ALIGN_HORIZONTAL;
		setDefault(0);
	}
	
	public FlxTabGroup(float X, float Y)
	{
		this(X, 0, FlxG.width);
	}
	
	public FlxTabGroup(float X)
	{
		this(X, 0, FlxG.width);
	}
	
	public FlxTabGroup()
	{
		this(0, 0, FlxG.width);
	}
	
	public void addTab(FlxTab tab)
	{
		_tabs.add(tab);
		tab._group = this;
		FlxGroup group = new FlxGroup();
		_content.add(group);
		add(group);
		
		if(_dividerSkin != null && _tabs.length > 1)
		{
			_dividers.add(new FlxSprite(0, 0)
			.loadGraphic(_dividerSkin.image, false, false, _dividerSkin.width, _dividerSkin.height));			
		}
		
		resetTabs();
		if(_current == 0)
			setDefault(_current);
	}
	
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
	
	private void resetTabs()
	{
		FlxSprite divider;
		FlxUIComponent tab;
		int length = _tabs.length;
		int tabWidth = this.width / length;
		for(int i = 0; i < length; i++)
		{
			tab = (FlxUIComponent) _tabs.members.get(i);
			tab.width = tabWidth;
			tab.scale.x = tabWidth;
			if(align == ALIGN_HORIZONTAL)
				tab.x = i * tabWidth;
			else if(align == ALIGN_VERTICAL)
				tab.y = i * tab.height;
			tab.label.width = tabWidth;
			tab.label.setAlignment(tab.skin.labelAlign);
			
			if(_dividerSkin != null && i < _tabs.length-1)
			{
				divider = (FlxSprite) _dividers.members.get(i);
				divider.x = tab.x + tab.width;		
			}
		}
	}
	
	public void loadDividerSkin(String Skin, int Width, int Height)
	{
		if(_dividerSkin == null)
			_dividerSkin = new FlxUISkin();
		_dividerSkin.setImage(Skin, Width, Height);
		if(_dividers.length < _tabs.length)
		{
			for(int i = 0; i < _tabs.length-1; i++)
			{
				_dividers.add(new FlxSprite(0, 0)
				.loadGraphic(Skin, false, false, Width, Height));
			}
			resetTabs();
		}
	}
	
	public void addContent(FlxObject Object, int Index)
	{
		if(Index > _tabs.length - 1)
			return;
		/*if(align == ALIGN_HORIZONTAL)
			Object.y += y + ((FlxTab)_tabs.members.get(0)).height; 
		else if(align == x + ALIGN_VERTICAL)
			Object.x += ((FlxTab)_tabs.members.get(0)).width;*/
		if(_current != Index)
			Object.visible = false;
		((FlxGroup)_content.members.get(Index)).add(Object);
	}
	
	public void loadSkin(String Skin, int Width, int Height, FlxUISkin params)
	{
		FlxUIComponent tab;
		for(int i = 0; i < _tabs.length; i++)
		{
			tab = (FlxUIComponent) _tabs.members.get(i);
			tab.loadGraphic(Skin, false, false, Width, Height);
			if(params != null)
				tab.skin = params;
		}
	}

	public void loadSkin(String Skin, int Width, int Height)
	{
		loadSkin(Skin, Width, Height, null);
	}
	
	protected void onChange(FlxTab tab)
	{
		// Break if it's already selected.
		if(_current == _tabs.members.indexOf(tab, true))
		{
			tab.setActive(true);
			return;
		}
		
		FlxTab object;
		for(int i = 0; i < _tabs.members.size; i++)
		{
			object = (FlxTab) _tabs.members.get(i);
			object.setActive(false);
			((FlxGroup)_content.members.get(i)).setAll("visible", false);
		}
		tab.setActive(true);
		_current = _tabs.members.indexOf(tab, true);
		((FlxGroup)_content.members.get(_current)).setAll("visible", true);
		if(onChange != null)
			onChange.callback();
	}
	
	public void setDefault(int Index)
	{
		if(Index > _tabs.length-1)
			return;
		
		int i = 0;
		while(i < _tabs.length)
		{
			((FlxUIComponent)_tabs.members.get(i)).setActive(false);
			((FlxGroup)_content.members.get(i)).setAll("visible", false);
			++i;
		}
		((FlxUIComponent)_tabs.members.get(Index)).setActive(true);
		((FlxGroup)_content.members.get(Index)).setAll("visible", true);
		_current = Index;
	}
}

