package org.flixel.ui;

import org.flixel.FlxSprite;

/**
 * This is an abstract UI component. It supports single image and ninepatch.
 * Needs skin to get it working.
 * 
 * @author Ka Wing Chin
 */
public abstract class FlxUIComponent extends FlxSprite
{
	/**
	 * The ID of the component. It may be required by some UI components like
	 * <code>FlxRadioButton</code>
	 */
	public String ID;
	/**
	 * Tracks whether the component is enabled or not.
	 */
	protected boolean enabled;
	/**
	 * Tracks whether the component is focused/highlighted or not.
	 */
	protected boolean focused;
	/**
	 * Tracks whether the component is activated.
	 */
	protected boolean activated;
	/**
	 * The current state of the button.
	 */
	public int status;
	/**
	 * The <code>FlxLabel</code> for the component.
	 */
	public FlxTextExt label;
	/**
	 * The <code>FlxSkin</code> of the component
	 */
	public FlxUISkin skin;
	/**
	 * The status of the <code>FlxSkin</code>
	 */
	public int skinStatus;
	/**
	 * Y-position to vertical align the label in the middle.
	 */
	private float _verticalCenter;
	/**
	 * Whether the component uses <code>NinePatch</code> or not.
	 */
	protected boolean _isNinePatch;
	/**
	 * Top left of the <code>Ninepatch</code>.
	 */
	protected FlxNinePatch _topLeft;
	/**
	 * Top center of the <code>Ninepatch</code>.
	 */
	protected FlxNinePatch _topCenter;
	/**
	 * Top right of the <code>Ninepatch</code>.
	 */
	protected FlxNinePatch _topRight;
	/**
	 * Middle left of the <code>Ninepatch</code>.
	 */
	protected FlxNinePatch _middleLeft;
	/**
	 * Middle center of the <code>Ninepatch</code>. If this is not null, it will
	 * be used as base graphic for this component.
	 */
	protected FlxNinePatch _middleCenter;
	/**
	 * Middle right of the <code>Ninepatch</code>.
	 */
	protected FlxNinePatch _middleRight;
	/**
	 * Bottom left of the <code>Ninepatch</code>.
	 */
	protected FlxNinePatch _bottomLeft;
	/**
	 * Bottom center of the <code>Ninepatch</code>.
	 */
	protected FlxNinePatch _bottomCenter;
	/**
	 * Bottom right of the <code>Ninepatch</code>.
	 */
	protected FlxNinePatch _bottomRight;

	/**
	 * Internal, the width that is given in the constructor.
	 */
	protected float _width;
	/**
	 * Internal the height that is given in the constructor.
	 */
	protected float _height;
	/**
	 * Whether or not the component has initialized itself yet.
	 */
	private boolean _initialized;
	/**
	 * Just cached variables for positioning.
	 */
	private float _endX;
	private float _endY;
	private float _startX;
	private float _startY;
	private float _patchWidth;
	private float _patchHeight;

	/**
	 * Creates a new <code>FlxUIComponent</code> object.
	 * 
	 * @param X The x-position of the component.
	 * @param Y The y-position of the component.
	 * @param UISkin The skin that needs to be applied.
	 * @param Label The label along side the component.
	 * @param Width The width of the component. Default auto.
	 * @param Height The height of the component. Default auto.
	 */
	public FlxUIComponent(float X, float Y, FlxUISkin UISkin, String Label, int Width, int Height)
	{
		super(X, Y);
		skin = UISkin;
		if(skin == null)
			setDefaultSkin();

		if(Label != null)
		{
			label = new FlxTextExt(0, 0, skin.labelWidth, skin.labelHeight, Label, true);
			label.setFormat(skin.labelFont, skin.labelSize, skin.labelColor, skin.labelAlign, skin.labelShadowColor);
			label.calcFrame();
		}
		// Single image
		if(skin.image != null)
			loadGraphic(skin.image, false, false, skin.width, skin.height);
		// Ninepatch
		if(skin.patches != null)
		{
			origin.x = origin.y = 0;
			_isNinePatch = true;
			_height = Height;
			_width = Width;
			if(skin.patches.get(FlxNinePatch.TOP_LEFT) != null)
				setPatchDimension(_topLeft = skin.patches.get(FlxNinePatch.TOP_LEFT));
			if(skin.patches.get(FlxNinePatch.TOP_CENTER) != null)
				_topCenter = skin.patches.get(FlxNinePatch.TOP_CENTER);
			if(skin.patches.get(FlxNinePatch.TOP_RIGHT) != null)
				setPatchDimension(_topRight = skin.patches.get(FlxNinePatch.TOP_RIGHT));

			if(skin.patches.get(FlxNinePatch.BOTTOM_LEFT) != null)
				setPatchDimension(_bottomLeft = skin.patches.get(FlxNinePatch.BOTTOM_LEFT));
			if(skin.patches.get(FlxNinePatch.BOTTOM_CENTER) != null)
				_bottomCenter = skin.patches.get(FlxNinePatch.BOTTOM_CENTER);
			if(skin.patches.get(FlxNinePatch.BOTTOM_RIGHT) != null)
				setPatchDimension(_bottomRight = skin.patches.get(FlxNinePatch.BOTTOM_RIGHT));

			if(skin.patches.get(FlxNinePatch.MIDDLE_LEFT) != null)
				setPatchDimension(_middleLeft = skin.patches.get(FlxNinePatch.MIDDLE_LEFT));
			if(skin.patches.get(FlxNinePatch.MIDDLE_CENTER) != null)
				_middleCenter = skin.patches.get(FlxNinePatch.MIDDLE_CENTER);
			if(skin.patches.get(FlxNinePatch.MIDDLE_RIGHT) != null)
				setPatchDimension(_middleRight = skin.patches.get(FlxNinePatch.MIDDLE_RIGHT));

			adjustDimension();
		}

		if(skin.labelVerticalAlign.equals("top"))
			_verticalCenter = y;
		else if(skin.labelVerticalAlign.equals("middle"))
			_verticalCenter = y + (height / 2f) - (label.height / 2f);
		else if(skin.labelVerticalAlign.equals("bottom"))
			_verticalCenter = y + (height - label.height);

		scrollFactor.x = scrollFactor.y = 0;
		moves = false;
		immovable = true;
		enabled = true;
		focused = false;
		activated = false;
	}

	/**
	 * Creates a new <code>FlxUIComponent</code> object.
	 * 
	 * @param X The x-position of the component.
	 * @param Y The y-position of the component.
	 * @param UISkin The skin that needs to be applied.
	 * @param Label The label along side the component.
	 * @param Width The width of the component. Default auto.
	 */
	public FlxUIComponent(float X, float Y, FlxUISkin Skin, String Label, int Width)
	{
		this(X, Y, Skin, Label, Width, 0);
	}

	/**
	 * Creates a new <code>FlxUIComponent</code> object.
	 * 
	 * @param X The x-position of the component.
	 * @param Y The y-position of the component.
	 * @param UISkin The skin that needs to be applied.
	 * @param Label The label along side the component.
	 */
	public FlxUIComponent(float X, float Y, FlxUISkin Skin, String Label)
	{
		this(X, Y, Skin, Label, 0, 0);
	}

	/**
	 * Creates a new <code>FlxUIComponent</code> object.
	 * 
	 * @param X The x-position of the component.
	 * @param Y The y-position of the component.
	 * @param UISkin The skin that needs to be applied.
	 */
	public FlxUIComponent(float X, float Y, FlxUISkin Skin)
	{
		this(X, Y, Skin, null, 0, 0);
	}

	/**
	 * Cache the width and height of a single patch. Just for performance sake.
	 * 
	 * @param patch A single nine patch.
	 */
	private void setPatchDimension(FlxNinePatch patch)
	{
		if(patch == null)
			return;
		if(_patchWidth == 0)
			_patchWidth = patch.width;
		if(_patchHeight == 0)
			_patchHeight = patch.height;
	}

	@Override
	public void destroy()
	{
		super.destroy();
		if(label != null)
			label.destroy();
		label = null;
		if(skin != null)
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
			if(_middleLeft != null)
				_middleLeft.destroy();
			_middleLeft = null;
			if(_middleRight != null)
				_middleRight.destroy();
			_middleRight = null;
			if(_bottomLeft != null)
				_bottomLeft.destroy();
			_bottomLeft = null;
			if(_bottomCenter != null)
				_bottomCenter.destroy();
			_bottomCenter = null;
			if(_bottomRight != null)
				_bottomRight.destroy();
			_bottomRight = null;
		}
	}

	/**
	 * Load the default skin.
	 */
	public abstract void setDefaultSkin();

	@Override
	public void preUpdate()
	{
		super.preUpdate();
		if(!_initialized)
		{
			_initialized = true;
			label.calcFrame();
		}
	}

	@Override
	public void update()
	{
		// If it's a ninepatch, place the parts correctly.
		if(_isNinePatch)
		{
			_startX = x - _patchWidth;
			_startY = y - _patchHeight;
			_endX = x + width;
			_endY = y + height;

			if(_topLeft != null)
			{
				_topLeft.x = _startX;
				_topLeft.y = _startY;
			}
			if(_topCenter != null)
			{
				_topCenter.x = x;
				_topCenter.y = _startY;
			}
			if(_topRight != null)
			{
				_topRight.x = _endX;
				_topRight.y = _startY;
			}
			if(_bottomLeft != null)
			{
				_bottomLeft.x = _startX;
				_bottomLeft.y = _endY;
			}
			if(_bottomCenter != null)
			{
				_bottomCenter.x = x;
				_bottomCenter.y = _endY;
			}
			if(_bottomRight != null)
			{
				_bottomRight.x = _endX;
				_bottomRight.y = _endY;
			}
			if(_middleLeft != null)
			{
				_middleLeft.x = _startX;
				_middleLeft.y = y;
			}
			if(_middleCenter != null)
			{
				_middleCenter.x = x;
				_middleCenter.y = y;
			}
			if(_middleRight != null)
			{
				_middleRight.x = _endX;
				_middleRight.y = y;
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
					label.y = _isNinePatch ? _verticalCenter : y + _verticalCenter;
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
					label.x = x - label.width;
					label.x -= _middleLeft != null ? _middleLeft.width : 0;
					label.y = y;
					break;
				default:
					break;
			}

			if(skin.labelOffset != null)
			{
				label.x += skin.labelOffset.x;
				label.y += skin.labelOffset.y;
			}
		}
	}

	@Override
	public void draw()
	{
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
			if(_middleCenter != null)
				_middleCenter.draw();
			if(_middleRight != null)
				_middleRight.draw();
			if(_bottomLeft != null)
				_bottomLeft.draw();
			if(_bottomCenter != null)
				_bottomCenter.draw();
			if(_bottomRight != null)
				_bottomRight.draw();
		}
		else
			super.draw();

		if(label != null)
		{
			label.scrollFactor = scrollFactor;
			label.cameras = cameras;
			label.draw();
		}
	}

	/**
	 * Adjust the dimension of the component
	 */
	private void adjustDimension()
	{
		label.calcFrame();
		width = (_width == 0) ? label.width : _width;
		height = (_height == 0) ? label.height : _height;
		if(_middleCenter != null)
			_middleCenter.width = width;
		if(_topCenter != null)
			_topCenter.width = width;
		if(_bottomCenter != null)
			_bottomCenter.width = width;

		if(_middleCenter != null)
			_middleCenter.height = height;
		if(_middleLeft != null)
			_middleLeft.height = height;
		if(_middleRight != null)
			_middleRight.height = height;
	}

	@Override
	public void setFrame(int Frame)
	{
		if(Frame == -1)
			return;
		skinStatus = Frame;
		if(_isNinePatch)
			setNinePatchStatus(Frame);
		else
			super.setFrame(Frame);
	}

	/**
	 * Set the status of ninepatch.
	 * 
	 * @param Frame
	 */
	protected void setNinePatchStatus(int Frame)
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

	/**
	 * Whether to enable the component or not.
	 * 
	 * @param enable boolean
	 */
	public void setEnable(boolean enable)
	{
		enabled = enable;
		setFrame(enabled ? skin.NORMAL : skin.DISABLED);
	}

	/**
	 * Whether the component is enabled or not.
	 * 
	 * @return
	 */
	public boolean getEnable()
	{
		return enabled;
	}

	/**
	 * Whether to focus/highlight the component or not.
	 * 
	 * @param focus boolean
	 */
	public void setFocus(boolean focus)
	{
		focused = focus;
		if(enabled)
			setFrame(focused ? skin.ACTIVE_HIGHTLIGHT : skin.NORMAL);
		else
			setFrame(focused ? skin.ACTIVE_HIGHTLIGHT_DISABLED : skin.DISABLED);
	}

	/**
	 * Whether the component is focused/highlighted or not.
	 * 
	 * @return
	 */
	public boolean getFocus()
	{
		return focused;
	}

	/**
	 * Whether the component is activated or not.
	 * 
	 * @param active boolean
	 */
	public void setActive(boolean active)
	{
		activated = active;
		if(activated)
			setFrame(enabled ? skin.ACTIVE_NORMAL : skin.ACTIVE_DISABLED);
		else
			setFrame(enabled ? skin.NORMAL : skin.DISABLED);
	}

	/**
	 * Whether the component is activated or not.
	 * 
	 * @return
	 */
	public boolean getActivated()
	{
		return activated;
	}
}
