package org.flixel.ui;

import org.flixel.FlxPoint;

import com.badlogic.gdx.utils.Array;

/**
 * 
 * @author Ka Wing Chin
 */
public class FlxUISkin
{
	/**
	 * Used with public variable <code>FlxUIComponent.skinStatus</code>, means
	 * not highlighted or pressed.
	 */
	public int NORMAL = 0;
	/**
	 * Used with public variable <code>FlxUIComponent.skinStatus</code>, means
	 * highlighted (usually from mouse over).
	 */
	public int HIGHLIGHT = 1;
	/**
	 * Used with public variable <code>FlxUIComponent.skinStatus</code>, means
	 * pressed (usually from mouse click).
	 */
	public int PRESSED = 2;
	/**
	 * Used with public variable <code>FlxUIComponent.skinStatus</code>, means
	 * disabled.
	 */
	public int DISABLED = 3;
	/**
	 * Used with public variable <code>FlxUIComponent.skinStatus</code>, means
	 * highlighted and disabled (usually from mouse click).
	 */
	public int HIGHLIGHT_DISABLED = -1;
	/**
	 * Used with public variable <code>FlxUIComponent.skinStatus</code>, means
	 * activated not highlighted or pressed (usually from mouse click).
	 */
	public int ACTIVE_NORMAL = -1;
	/**
	 * Used with public variable <code>FlxUIComponent.skinStatus</code>, means
	 * activated, not highlighted or pressed (usually from mouse click).
	 */
	public int ACTIVE_HIGHTLIGHT = -1;
	/**
	 * Used with public variable <code>FlxUIComponent.skinStatus</code>, means
	 * activated and pressed (usually from mouse click).
	 */
	public int ACTIVE_PRESSED = -1;
	/**
	 * Used with public variable <code>FlxUIComponent.skinStatus</code>, means
	 * activated and disabled.
	 */
	public int ACTIVE_DISABLED = -1;
	/**
	 * Used with public variable <code>FlxUIComponent.skinStatus</code>, means
	 * activated and hightlight and disabled (usually from mouse click).
	 */
	public int ACTIVE_HIGHTLIGHT_DISABLED = -1;

	/**
	 * Single image of the skin.
	 */
	public String image;
	/**
	 * The width of the image.
	 */
	public int width;
	/**
	 * The height of the image.
	 */
	public int height;

	/**
	 * Label position is equal to x and y of the FlxUIComponent.
	 */
	public static final int LABEL_NONE = -1;
	/**
	 * Label is above the FlxUIComponent, component.y - label.height.
	 */
	public static final int LABEL_TOP = 0;
	/**
	 * Label is at the right of the FlxUIComponent, component.x +
	 * component.width.
	 */
	public static final int LABEL_RIGHT = 1;
	/**
	 * Label is below the FlxUIComponent, component.y + component.height.
	 */
	public static final int LABEL_BOTTOM = 2;
	/**
	 * Label is at the left of the FlxUIComponent, component.x - label.width.
	 */
	public static final int LABEL_LEFT = 3;
	/**
	 * The label position. Default LABEL_NONE.
	 */
	public int labelPosition = -1;
	/**
	 * Label width. Default 0, which means auto.
	 */
	public int labelWidth = 0;
	/**
	 * Label height. Deault 0, which means auto.
	 */
	public int labelHeight = 0;
	/**
	 * Label font. Default system font.
	 */
	public String labelFont;
	/**
	 * Label color. Default 0xFFFFFF.
	 */
	public int labelColor = 0xFFFFFF;
	/**
	 * Label size. Default 16.
	 */
	public float labelSize = 16;
	/**
	 * Label horizontal alignment. Default "left". Others are "center" and
	 * "right.
	 */
	public String labelAlign = "left";
	/**
	 * Label vertical alignment. Default "top". Others are "middle" and
	 * "bottom". It only works when labelPosition is LABEL_NONE.
	 */
	public String labelVerticalAlign = "top";

	/**
	 * Label shadow color. Default 0 (no shadow).
	 */
	public int labelShadowColor = 0;
	/**
	 * Label shadow position. Default (1, 1).
	 */
	public FlxPoint labelShadowPosition = new FlxPoint(1, 1);
	/**
	 * Label offset. Default (-1, 6).
	 */
	public FlxPoint labelOffset = new FlxPoint(-1, 6);
	/**
	 * An array which holds <code>Ninepatch</coded>es.
	 */
	public Array<FlxNinePatch> patches;

	/**
	 * Clean the memory.
	 */
	public void destroy()
	{
		labelOffset = null;
		labelShadowPosition = null;
		if(patches != null)
			patches.clear();
		patches = null;
	}

	/**
	 * Skin the component with single image.
	 * 
	 * @param Img The image.
	 * @param Width The width of the image.
	 * @param Height The height of the image.
	 */
	public void setImage(String Img, int Width, int Height)
	{
		image = Img;
		width = Width;
		height = Height;
	}

	/**
	 * Set an array of NinePatches.
	 * 
	 * @param Patches
	 */
	public void setNinePatch(Array<FlxNinePatch> Patches)
	{
		patches = Patches;
	}

	/**
	 * Set a ninepatch.
	 * 
	 * @param Position The position of the ninepatch (e.g. NinePatch.TOP_LEFT).
	 * @param Img The image of the ninepatch.
	 * @param Width The width of the image.
	 * @param Height The height of the image.
	 * @param Repeat Repeats the pattern of a ninepatch. Default is false.
	 */
	public void setNinePatch(int Position, String Img, int Width, int Height, boolean Repeat)
	{
		if(patches == null)
		{
			patches = new Array<FlxNinePatch>(9);
			for(int i = 0; i < 9; i++)
				patches.add(null);
		}
		patches.set(Position, new FlxNinePatch(Position, Img, Width, Height, Repeat));
	}

	/**
	 * Set a ninepatch.
	 * 
	 * @param Position The position of the ninepatch (e.g. NinePatch.TOP_LEFT).
	 * @param Img The image of the ninepatch.
	 * @param Width The width of the image.
	 * @param Height The height of the image.
	 */
	public void setNinePatch(int Position, String Img, int Width, int Height)
	{
		setNinePatch(Position, Img, Width, Height, false);
	}

	/**
	 * You can use this if you have a lot of text parameters to set instead of
	 * the individual properties.
	 * 
	 * @param Font The name of the font face for the text display.
	 * @param Size The size of the font (in pixels essentially).
	 * @param Color The color of the text in traditional flash 0xRRGGBB format.
	 * @param Alignment A string representing the desired alignment
	 *        ("left,"right" or "center").
	 * @param ShadowColor An int representing the desired text shadow color in
	 *        flash 0xAARRGGBB format.
	 * @param ShadowX The x-position of the shadow, default is 1.
	 * @param ShadowY The y-position of the shadow, default is 1.
	 * 
	 * @return This FlxText instance (nice for chaining stuff together, if
	 *         you're into that).
	 */
	public void setFormat(String Font, float Size, int Color, String Alignment, int ShadowColor, float ShadowX, float ShadowY)
	{
		labelFont = Font;
		labelSize = Size;
		labelColor = Color;
		labelAlign = Alignment;
		labelShadowColor = ShadowColor;
		labelShadowPosition.make(ShadowX, ShadowY);
	}

	/**
	 * You can use this if you have a lot of text parameters to set instead of
	 * the individual properties.
	 * 
	 * @param Font The name of the font face for the text display.
	 * @param Size The size of the font (in pixels essentially).
	 * @param Color The color of the text in traditional flash 0xRRGGBB format.
	 * @param Alignment A string representing the desired alignment
	 *        ("left,"right" or "center").
	 * @param ShadowColor An int representing the desired text shadow color in
	 *        flash 0xAARRGGBB format.
	 * @param ShadowX The x-position of the shadow, default is 1.
	 * 
	 * @return This FlxText instance (nice for chaining stuff together, if
	 *         you're into that).
	 */
	public void setFormat(String Font, float Size, int Color, String Alignment, int ShadowColor, float ShadowX)
	{
		setFormat(Font, Size, Color, Alignment, ShadowColor, ShadowX, 1f);
	}

	/**
	 * You can use this if you have a lot of text parameters to set instead of
	 * the individual properties.
	 * 
	 * @param Font The name of the font face for the text display.
	 * @param Size The size of the font (in pixels essentially).
	 * @param Color The color of the text in traditional flash 0xRRGGBB format.
	 * @param Alignment A string representing the desired alignment
	 *        ("left,"right" or "center").
	 * @param ShadowColor An int representing the desired text shadow color in
	 *        flash 0xAARRGGBB format.
	 * 
	 * @return This FlxText instance (nice for chaining stuff together, if
	 *         you're into that).
	 */
	public void setFormat(String Font, float Size, int Color, String Alignment, int ShadowColor)
	{
		setFormat(Font, Size, Color, Alignment, ShadowColor, 1f, 1f);
	}

	/**
	 * You can use this if you have a lot of text parameters to set instead of
	 * the individual properties.
	 * 
	 * @param Font The name of the font face for the text display.
	 * @param Size The size of the font (in pixels essentially).
	 * @param Color The color of the text in traditional flash 0xRRGGBB format.
	 * @param Alignment A string representing the desired alignment
	 *        ("left,"right" or "center").
	 * 
	 * @return This FlxText instance (nice for chaining stuff together, if
	 *         you're into that).
	 */
	public void setFormat(String Font, float Size, int Color, String Alignment)
	{
		setFormat(Font, Size, Color, Alignment, 0, 1f, 1f);
	}

	/**
	 * You can use this if you have a lot of text parameters to set instead of
	 * the individual properties.
	 * 
	 * @param Font The name of the font face for the text display.
	 * @param Size The size of the font (in pixels essentially).
	 * @param Color The color of the text in traditional flash 0xRRGGBB format.
	 * 
	 * @return This FlxText instance (nice for chaining stuff together, if
	 *         you're into that).
	 */
	public void setFormat(String Font, float Size, int Color)
	{
		setFormat(Font, Size, Color, "left", 0, 1f, 1f);
	}

	/**
	 * You can use this if you have a lot of text parameters to set instead of
	 * the individual properties.
	 * 
	 * @param Font The name of the font face for the text display.
	 * @param Size The size of the font (in pixels essentially).
	 * 
	 * @return This FlxText instance (nice for chaining stuff together, if
	 *         you're into that).
	 */
	public void setFormat(String Font, float Size)
	{
		setFormat(Font, Size, 0xFFFFFFFF, "left", 0, 1f, 1f);
	}

	/**
	 * You can use this if you have a lot of text parameters to set instead of
	 * the individual properties.
	 * 
	 * @param Font The name of the font face for the text display.
	 * 
	 * @return This FlxText instance (nice for chaining stuff together, if
	 *         you're into that).
	 */
	public void setFormat(String Font)
	{
		setFormat(Font, 8, 0xFFFFFFFF, "left", 0, 1f, 1f);
	}

	/**
	 * You can use this if you have a lot of text parameters to set instead of
	 * the individual properties.
	 * 
	 * @return This FlxText instance (nice for chaining stuff together, if
	 *         you're into that).
	 */
	public void setFormat()
	{
		setFormat(null, 8, 0xFFFFFFFF, "left", 0, 1f, 1f);
	}
}
