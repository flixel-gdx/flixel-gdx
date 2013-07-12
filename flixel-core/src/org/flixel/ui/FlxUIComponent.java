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
		
		if(skin.image != null)
			loadGraphic(skin.image, false, false, skin.width, skin.height);
		if(skin.patches != null)
		{
			origin.x = origin.y = 0;
			_isNinePatch = true;
			_height = Height; 
			_width = Width;
			if(skin.patches.get(NinePatch.TOP_LEFT) != null)
				_topLeft = skin.patches.get(NinePatch.TOP_LEFT).loadGraphic();
			if(skin.patches.get(NinePatch.TOP_CENTER) != null)
				_topCenter = skin.patches.get(NinePatch.TOP_CENTER).loadGraphic();
			if(skin.patches.get(NinePatch.TOP_RIGHT) != null)
				_topRight = skin.patches.get(NinePatch.TOP_RIGHT).loadGraphic();
			if(skin.patches.get(NinePatch.MIDDLE_LEFT) != null)
				_middleLeft = skin.patches.get(NinePatch.MIDDLE_LEFT).loadGraphic();
			if(skin.patches.get(NinePatch.MIDDLE_CENTER) != null)
				_middleCenter = skin.patches.get(NinePatch.MIDDLE_CENTER).loadGraphic();
			if(skin.patches.get(NinePatch.MIDDLE_RIGHT) != null)
				_middleRight = skin.patches.get(NinePatch.MIDDLE_RIGHT).loadGraphic();
			if(skin.patches.get(NinePatch.BOTTOM_LEFT) != null)
				_bottomLeft = skin.patches.get(NinePatch.BOTTOM_LEFT).loadGraphic();
			if(skin.patches.get(NinePatch.BOTTOM_CENTER) != null)
				_bottomCenter = skin.patches.get(NinePatch.BOTTOM_CENTER).loadGraphic();
			if(skin.patches.get(NinePatch.BOTTOM_RIGHT) != null)
				_bottomRight = skin.patches.get(NinePatch.BOTTOM_RIGHT).loadGraphic();	
			
			stretch();
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
			if(_topLeft != null)
				_topLeft.destroy();
			_topLeft = null;
			if(_topCenter != null)
				_topCenter.destroy();
			_topCenter = null;
			if(_topRight != null)
				_topRight.destroy();
			_topRight = null;
			if(_bottomLeft != null)
				_bottomLeft.destroy();
			_bottomLeft = null;
			if(_bottomCenter != null)
				_bottomCenter.destroy();
			_bottomCenter = null;
			if(_bottomRight != null)
				_bottomRight.destroy();
			_bottomRight = null;
			if(_middleLeft != null)
				_middleLeft.destroy();
			_middleLeft = null;
			if(_middleRight != null)
				_middleRight.destroy();
			_middleRight = null;			
		}
	}
	
	@Override
	public void update()
	{
		if(_isNinePatch)
		{
			if(_topLeft != null)
			{
				_topLeft.x = x - _topLeft.width;
				_topLeft.y = y - _topLeft.height;				
			}
			if(_topCenter != null)
			{
				_topCenter.x = x;
				_topCenter.y = y - _topCenter.height;				
			}
			if(_topRight != null)
			{
				_topRight.x = x + width;
				_topRight.y = y - _topRight.height;				
			}
			if(_bottomLeft != null)
			{
				_bottomLeft.x = x - _bottomLeft.width;
				_bottomLeft.y = y + height;				
			}
			if(_bottomCenter != null)
			{
				_bottomCenter.x = x;
				_bottomCenter.y = y + height;				
			}
			if(_bottomRight != null)
			{
				_bottomRight.x = x + width;
				_bottomRight.y = y + height;				
			}
			if(_middleLeft != null)
			{
				_middleLeft.x = x - _middleLeft.width;
				_middleLeft.y = y;				
			}
			if(_middleRight != null)
			{
				_middleRight.x = x + width;
				_middleRight. y = y;				
			}
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
					label.y = y - label.height;
					label.y -= _topCenter != null ? _topCenter.height : 0; 
					break;
				case FlxUISkin.LABEL_RIGHT:
					label.x = width + x;
					label.x += _middleRight != null ? _middleRight.width : 0;
					label.y = y;
					break;
				case FlxUISkin.LABEL_BOTTOM:
					label.x = x;
					label.y = y + height;					
					label.y += _bottomCenter != null ? _bottomCenter.height : 0; 
					break;
				case FlxUISkin.LABEL_LEFT:
					label.x = x - width;
					label.x -= _middleLeft != null ? _middleLeft.width : 0;
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
			if(_topLeft != null)
				_topLeft.draw();
			if(_topCenter != null)
				_topCenter.draw();
			if(_topRight != null)
				_topRight.draw();
			if(_middleLeft != null)
				_middleLeft.draw();
//			if(_middleCenter != null)
//				_middleCenter.draw();
			if(_middleRight != null)
				_middleRight.draw();
			if(_bottomLeft != null)
				_bottomLeft.draw();
			if(_bottomCenter != null)
				_bottomCenter.draw();
			if(_bottomRight != null)
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
		label.calcFrame();
		width = (_width == 0) ? label.width : _width; 
		height = (_height == 0) ? label.height : _height;
		
		scale.x = width / _middleCenter.width;
		if(_topCenter != null)
			_topCenter.scale.x = width / _topCenter.width;
		if(_bottomCenter != null)
			_bottomCenter.scale.x = width / _bottomCenter.width;
		
		scale.y = height / _middleCenter.height;
		if(_middleLeft != null)
			_middleLeft.scale.y = height / _middleLeft.height;
		if(_middleRight != null)
			_middleRight.scale.y = height / _middleRight.height;
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
		if(_topLeft != null)
			_topLeft.setFrame(Frame);
		if(_topCenter != null)
			_topCenter.setFrame(Frame);
		if(_topRight != null)
			_topRight.setFrame(Frame);
		if(_middleLeft != null)
			_middleLeft.setFrame(Frame);
		if(_middleCenter != null)
			_middleCenter.setFrame(Frame);
		if(_middleRight != null)
			_middleRight.setFrame(Frame);
		if(_bottomLeft != null)
			_bottomLeft.setFrame(Frame);
		if(_bottomCenter != null)
			_bottomCenter.setFrame(Frame);
		if(_bottomRight != null)
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