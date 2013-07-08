package org.flixel.ui;

import org.flixel.FlxSprite;
import org.flixel.ui.FlxUISkin.NinePatch;

/**
 *
 * @author Ka Wing Chin
 */
public class FlxUIComponent extends FlxSprite
{	
	public String ID;
	protected boolean enabled;
	protected boolean focused;
	protected boolean activated;
	
	/**
	 * Shows the current state of the button.
	 */
	public int status;
	public FlxLabel label;
	public FlxUISkin skin;
	public int skinStatus;
	
	/**
	 * Ninepatch stuff
	 */
	protected boolean _isNinePatch;
	protected FlxSprite _topLeft;
	protected FlxSprite _topCenter;
	protected FlxSprite _topRight;
	protected FlxSprite _bottomLeft;
	protected FlxSprite _bottomCenter;
	protected FlxSprite _bottomRight;
	protected FlxSprite _middleLeft;
	protected FlxSprite _middleCenter; 
	protected FlxSprite _middleRight;
	
	private float _width;
	private float _height;
	
	public FlxUIComponent(float X, float Y, FlxUISkin UISkin, String Label, int Width, int Height)
	{
		super(X, Y);
		skin = UISkin;		
		if(skin.image != null)
			loadGraphic(skin.image, false, false, skin.width, skin.height);
		if(skin.patches != null)
		{
			_isNinePatch = true;
			_width = Width;
			_height = Height;
			_topLeft = skin.patches.get(NinePatch.TOP_LEFT).loadGraphic();
			_topCenter = skin.patches.get(NinePatch.TOP_CENTER).loadGraphic();
			_topRight = skin.patches.get(NinePatch.TOP_RIGHT).loadGraphic();
			_middleLeft = skin.patches.get(NinePatch.MIDDLE_LEFT).loadGraphic();
			_middleCenter = skin.patches.get(NinePatch.MIDDLE_CENTER).loadGraphic();
			_middleRight = skin.patches.get(NinePatch.MIDDLE_RIGHT).loadGraphic();
			_bottomLeft = skin.patches.get(NinePatch.BOTTOM_LEFT).loadGraphic();
			_bottomCenter = skin.patches.get(NinePatch.BOTTOM_CENTER).loadGraphic();
			_bottomRight = skin.patches.get(NinePatch.BOTTOM_RIGHT).loadGraphic();			
		}
		
		if(Label != null)
		{
			label = new FlxLabel(0, 0, Label, Width, Height);
			label.setFormat(skin.labelFont,
							skin.labelSize,
							skin.labelColor,
							skin.labelAlign,
							skin.labelShadowColor,
							skin.labelShadowPosition.x,
							skin.labelShadowPosition.y);
		}
		
		scrollFactor.x = scrollFactor.y = 0;
		moves = false;
		immovable = true;
		enabled = true;
		focused = false;
		activated = false;
	}
	
	public FlxUIComponent(float X, float Y, FlxUISkin Skin, String Label)
	{
		this(X, Y, Skin, Label, 0, 0);
	}
	
	public FlxUIComponent(float X, float Y, FlxUISkin Skin)
	{
		this(X, Y, Skin, null, 0, 0);
	}
	
	public FlxUIComponent(float X, float Y)
	{
		this(X, Y, null, null, 0, 0);
	}
		
	@Override
	public void destroy()
	{
		super.destroy();
		if(label != null)
		{
			label.destroy();
			label = null;
		}
		skin.destroy();
		skin = null;
		if(_isNinePatch)
		{
			_topLeft.destroy();
			_topLeft = null;
			_topCenter.destroy();
			_topCenter = null;
			_topRight.destroy();
			_topRight = null;
			_bottomLeft.destroy();
			_bottomLeft = null;
			_bottomCenter.destroy();
			_bottomCenter = null;
			_bottomRight.destroy();
			_bottomRight = null;
			_middleLeft.destroy();
			_middleLeft = null;
			_middleRight.destroy();
			_middleRight = null;			
		}
	}
	
	@Override
	public void update()
	{
		if(_isNinePatch)
		{
			_topLeft.x = x - _topLeft.width;
			_topLeft.y = y - _topLeft.height;
			_topCenter.x = x;
			_topCenter.y = y - _topCenter.height;
			_topRight.x = x + width;
			_topRight.y = y - _topRight.height;
			_bottomLeft.x = x - _bottomLeft.width;
			_bottomLeft.y = y + height;
			_bottomCenter.x = x;
			_bottomCenter.y = y + height;
			_bottomRight.x = x + width;
			_bottomRight.y = y + height;
			_middleLeft.x = x - _middleLeft.width;
			_middleLeft.y = y;
			_middleRight.x = x + width;
			_middleRight. y = y;
		}
		
		super.update();
		
		if(_isNinePatch)
			setNinePatchStatus(skinStatus);
		
		// Then if the label and/or the label offset exist,
		// position them to match the button.
		if(label != null)
		{
			switch(skin.labelPosition)
			{
				case FlxUISkin.LABEL_NONE:
					label.x = x;
					label.y = y;
					break;
				case FlxUISkin.LABEL_TOP:
					label.x = x;
					label.y = y - height;
					break;
				case FlxUISkin.LABEL_RIGHT:
					label.x = width + x;
					label.y = y;
					break;
				case FlxUISkin.LABEL_BOTTOM:
					label.x = x;
					label.y = y + height;					
					break;
				case FlxUISkin.LABEL_LEFT:
					label.x = x - width;
					label.y = y;
					break;
				default:
					break;
			}
		}
		if(skin.labelOffset != null)
		{
			label.x += skin.labelOffset.x;
			label.y += skin.labelOffset.y;
		}
	}
	
	@Override
	public void draw()
	{
		super.draw();
		if(_isNinePatch)
		{
			_topLeft.draw();
			_topCenter.draw();
			_topRight.draw();
			_middleLeft.draw();
//			_middleCenter.draw();
			_middleRight.draw();
			_bottomLeft.draw();
			_bottomCenter.draw();
			_bottomRight.draw();
		}
		if(label != null)
		{
			label.scrollFactor = scrollFactor;
			label.cameras = cameras;
			label.draw();
		}
	}
	
	public void stretch()
	{
		width = (_width == 0) ? label.width : _width; 
		height = (_height == 0) ? label.height : _height;		
		scale.x = _topCenter.scale.x = _bottomCenter.scale.x = width;
		scale.y = _middleLeft.scale.y = _middleRight.scale.y = height;
		label.calcFrame();
	}
	
	@Override
	public void setFrame(int Frame)
	{
		if(Frame == -1)
			return;
		skinStatus = Frame;
		super.setFrame(Frame);
		if(_isNinePatch)
			setNinePatchStatus(Frame);
	}

	public void setNinePatchStatus(int Frame)
	{		
		_topLeft.setFrame(Frame);
		_topCenter.setFrame(Frame);
		_topRight.setFrame(Frame);
		_middleLeft.setFrame(Frame);
		_middleCenter.setFrame(Frame);
		_middleRight.setFrame(Frame);
		_bottomLeft.setFrame(Frame);
		_bottomCenter.setFrame(Frame);
		_bottomRight.setFrame(Frame);
	}
	
	public void setEnable(boolean enable)
	{
		enabled = enable;
		setFrame(enabled ? skin.NORMAL : skin.DISABLED);
	}
	
	public boolean getEnable()
	{
		return enabled;
	}
	
	public void setFocus(boolean focus)
	{
		focused = focus;
		if(enabled)
			setFrame(focused ? skin.ACTIVE_HIGHTLIGHT : skin.NORMAL);
		else
			setFrame(focused ? skin.ACTIVE_HIGHTLIGHT_DISABLED : skin.DISABLED);
	}
	
	public boolean getFocus()
	{
		return focused;
	}
	
	public void setActive(boolean active)
	{
		activated = active;
		if(activated)
			setFrame(enabled ? skin.ACTIVE_NORMAL : skin.ACTIVE_DISABLED);
		else
			setFrame(enabled ? skin.NORMAL : skin.DISABLED);		
	}
	
	public boolean getActivated()
	{
		return activated;
	}
}