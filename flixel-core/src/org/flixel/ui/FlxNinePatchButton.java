package org.flixel.ui;

import org.flixel.ui.event.IFlxUIListener;

/**
 * Scalable button.
 * 
 * @author Ka Wing Chin
 */
public class FlxNinePatchButton extends FlxUITouchable
{
	private static final String ImgTopLeft = "org/flixel/data/pack:ninepatch_button_topleft";
	private static final String ImgTopCenter = "org/flixel/data/pack:ninepatch_button_topcenter";
	private static final String ImgTopRight = "org/flixel/data/pack:ninepatch_button_topright";
	private static final String ImgBottomLeft = "org/flixel/data/pack:ninepatch_button_bottomleft";
	private static final String ImgBottomCenter = "org/flixel/data/pack:ninepatch_button_bottomcenter";
	private static final String ImgBottomRight = "org/flixel/data/pack:ninepatch_button_bottomright";
	private static final String ImgMiddleLeft = "org/flixel/data/pack:ninepatch_button_middleleft";
	private static final String ImgMiddleCenter = "org/flixel/data/pack:ninepatch_button_middlecenter";
	private static final String ImgMiddleRight = "org/flixel/data/pack:ninepatch_button_middleright";

	/**
	 * Create a <code>FlxNinePatchButton</code> object.
	 * 
	 * @param X The x-position of the component.
	 * @param Y The y-position of the component.
	 * @param UISkin The skin that needs to be applied.
	 * @param Label The label along side the component.
	 * @param Width The width of the component. Default auto.
	 * @param Height The height of the component. Default auto.
	 * @param onClick The function to call whenever the button is clicked.
	 */
	public FlxNinePatchButton(float X, float Y, FlxUISkin Skin, String Label, int Width, int Height, IFlxUIListener onClick)
	{
		super(X, Y, Skin, Label, Width, Height);
		onUp = onClick;

		if(Width > 0)
			label.width = Width;
	}

	/**
	 * Create a <code>FlxNinePatchButton</code> object.
	 * 
	 * @param X The x-position of the component.
	 * @param Y The y-position of the component.
	 * @param UISkin The skin that needs to be applied.
	 * @param Label The label along side the component.
	 * @param Width The width of the component. Default auto.
	 * @param Height The height of the component. Default auto.
	 */
	public FlxNinePatchButton(float X, float Y, FlxUISkin skin, String Label, int Width, int Height)
	{
		this(X, Y, skin, Label, Width, 0, null);
	}

	/**
	 * Create a <code>FlxNinePatchButton</code> object.
	 * 
	 * @param X The x-position of the component.
	 * @param Y The y-position of the component.
	 * @param UISkin The skin that needs to be applied.
	 * @param Label The label along side the component.
	 * @param Width The width of the component. Default auto.
	 */
	public FlxNinePatchButton(float X, float Y, FlxUISkin skin, String Label, int Width)
	{
		this(X, Y, skin, Label, Width, 0, null);
	}

	/**
	 * Create a <code>FlxNinePatchButton</code> object.
	 * 
	 * @param X The x-position of the component.
	 * @param Y The y-position of the component.
	 * @param UISkin The skin that needs to be applied.
	 * @param Label The label along side the component.
	 */
	public FlxNinePatchButton(float X, float Y, FlxUISkin skin, String Label)
	{
		this(X, Y, skin, Label, 0, 0, null);
	}

	/**
	 * Create a <code>FlxNinePatchButton</code> object.
	 * 
	 * @param X The x-position of the component.
	 * @param Y The y-position of the component.
	 * @param UISkin The skin that needs to be applied.
	 */
	public FlxNinePatchButton(float X, float Y, FlxUISkin skin)
	{
		this(X, Y, skin, null, 0, 0, null);
	}

	/**
	 * Create a <code>FlxNinePatchButton</code> object.
	 * 
	 * @param X The x-position of the component.
	 * @param Y The y-position of the component.
	 */
	public FlxNinePatchButton(float X, float Y)
	{
		this(X, Y, null, null, 0, 0, null);
	}

	/**
	 * Create a <code>FlxNinePatchButton</code> object.
	 * 
	 * @param X The x-position of the component.
	 */
	public FlxNinePatchButton(float X)
	{
		this(X, 0, null, null, 0, 0, null);
	}

	/**
	 * Create a <code>FlxNinePatchButton</code> object.
	 */
	public FlxNinePatchButton()
	{
		this(0, 0, null, null, 0, 0, null);
	}

	@Override
	public void setDefaultSkin()
	{
		skin = new FlxUISkin();
		skin.HIGHLIGHT_DISABLED = 3;
		skin.DISABLED = 4;
		skin.setFormat(null, 16);
		skin.labelVerticalAlign = "middle";
		skin.labelAlign = "center";
		skin.setNinePatch(FlxNinePatch.TOP_LEFT, ImgTopLeft, 8, 8);
		skin.setNinePatch(FlxNinePatch.TOP_CENTER, ImgTopCenter, 1, 8);
		skin.setNinePatch(FlxNinePatch.TOP_RIGHT, ImgTopRight, 8, 8);
		skin.setNinePatch(FlxNinePatch.MIDDLE_LEFT, ImgMiddleLeft, 8, 1);
		skin.setNinePatch(FlxNinePatch.MIDDLE_CENTER, ImgMiddleCenter, 1, 1);
		skin.setNinePatch(FlxNinePatch.MIDDLE_RIGHT, ImgMiddleRight, 8, 1);
		skin.setNinePatch(FlxNinePatch.BOTTOM_LEFT, ImgBottomLeft, 8, 8);
		skin.setNinePatch(FlxNinePatch.BOTTOM_CENTER, ImgBottomCenter, 1, 8);
		skin.setNinePatch(FlxNinePatch.BOTTOM_RIGHT, ImgBottomRight, 8, 8);
		skin.labelOffset.y = 0;
	}

	@Override
	public void update()
	{
		super.update();
		// Default button appearance is to simply update
		// the label appearance based on animation frame.
		if(label == null)
			return;

		if(status == skin.HIGHLIGHT_DISABLED || status == skin.DISABLED)
			label.setAlpha(0.5f);
	}
}
