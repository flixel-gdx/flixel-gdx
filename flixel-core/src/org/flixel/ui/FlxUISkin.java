package org.flixel.ui;

import org.flixel.FlxPoint;
import org.flixel.FlxSprite;

import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Ka Wing Chin
 */
public class FlxUISkin
{
	public int NORMAL = 0;
	/**
	 * Used with public variable <code>status</code>, means highlighted (usually from mouse over).
	 */
	public int HIGHLIGHT = 1;
	/**
	 * Used with public variable <code>status</code>, means pressed (usually from mouse click).
	 */
	public int PRESSED = 2;
	public int DISABLED = 3;
	public int HIGHLIGHT_DISABLED = -1;
	public int ACTIVE_NORMAL = -1;
	public int ACTIVE_HIGHTLIGHT = -1;
	public int ACTIVE_PRESSED = -1;
	public int ACTIVE_DISABLED = -1;
	public int ACTIVE_HIGHTLIGHT_DISABLED = -1;
	
	// Image
	public String image;
	public int width;
	public int height;
	
	// Label
	public static final int LABEL_NONE = -1;
	public static final int LABEL_TOP = 0;
	public static final int LABEL_RIGHT = 1;
	public static final int LABEL_BOTTOM = 2;
	public static final int LABEL_LEFT = 3;
	public int labelPosition = -1;
		
	public String labelFont;
	public int labelColor = 0xFFFFFF;
	public float labelSize = 14;
	public String labelAlign = "left";
	public int labelShadowColor = 0;
	public FlxPoint labelShadowPosition = new FlxPoint(1, 1); 

	public FlxPoint labelOffset = new FlxPoint(-1, 6);
	
	public Array<NinePatch> patches;
	
	public void destroy()
	{
		labelOffset = null;
		labelShadowPosition = null;
		if(patches != null)
			patches.clear();
		patches = null;
	}
	
	public void setImage(String Img, int Width, int Height)
	{
		image = Img;
		width = Width;
		height = Height;
	}
	
	/**
	 * Create an array with max size of 9.
	 * @param Patches
	 */
	public void setNinePatch(Array<NinePatch> Patches)
	{
		patches = Patches;
	}
	
	public void setNinePatch(int Position, String Img, int Width, int Height)
	{
		if(patches == null)
		{
			patches = new Array<NinePatch>(9);
			for(int i = 0; i < 9; i++)
				patches.add(null);
		}
		patches.set(Position, new NinePatch(Position, Img, Width, Height));
		if(Position == NinePatch.MIDDLE_CENTER)
			setImage(Img, Width, Height);
	}
	
	
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
	 * You can use this if you have a lot of text parameters
	 * to set instead of the individual properties.
	 * 
	 * @param	Font		The name of the font face for the text display.
	 * @param	Size		The size of the font (in pixels essentially).
	 * @param	Color		The color of the text in traditional flash 0xRRGGBB format.
	 * @param	Alignment	A string representing the desired alignment ("left,"right" or "center").
	 * @param	ShadowColor	An int representing the desired text shadow color in flash 0xAARRGGBB format.
	 * @param	ShadowX		The x-position of the shadow, default is 1.
	 * 
	 * @return	This FlxText instance (nice for chaining stuff together, if you're into that).
	 */
	public void setFormat(String Font, float Size, int Color, String Alignment, int ShadowColor, float ShadowX)
	{
		setFormat(Font, Size, Color, Alignment, ShadowColor, ShadowX, 1f);
	}
	
	/**
	 * You can use this if you have a lot of text parameters
	 * to set instead of the individual properties.
	 * 
	 * @param	Font		The name of the font face for the text display.
	 * @param	Size		The size of the font (in pixels essentially).
	 * @param	Color		The color of the text in traditional flash 0xRRGGBB format.
	 * @param	Alignment	A string representing the desired alignment ("left,"right" or "center").
	 * @param	ShadowColor	An int representing the desired text shadow color in flash 0xAARRGGBB format.
	 * 
	 * @return	This FlxText instance (nice for chaining stuff together, if you're into that).
	 */
	public void setFormat(String Font, float Size, int Color, String Alignment, int ShadowColor)
	{
		setFormat(Font, Size, Color, Alignment, ShadowColor, 1f, 1f);
	}	
		
	/**
	 * You can use this if you have a lot of text parameters
	 * to set instead of the individual properties.
	 * 
	 * @param	Font		The name of the font face for the text display.
	 * @param	Size		The size of the font (in pixels essentially).
	 * @param	Color		The color of the text in traditional flash 0xRRGGBB format.
	 * @param	Alignment	A string representing the desired alignment ("left,"right" or "center").
	 * 
	 * @return	This FlxText instance (nice for chaining stuff together, if you're into that).
	 */
	public void setFormat(String Font, float Size, int Color, String Alignment)
	{
		setFormat(Font, Size, Color, Alignment, 0, 1f, 1f);
	}

	/**
	 * You can use this if you have a lot of text parameters
	 * to set instead of the individual properties.
	 * 
	 * @param	Font		The name of the font face for the text display.
	 * @param	Size		The size of the font (in pixels essentially).
	 * @param	Color		The color of the text in traditional flash 0xRRGGBB format.
	 * 
	 * @return	This FlxText instance (nice for chaining stuff together, if you're into that).
	 */
	public void setFormat(String Font, float Size, int Color)
	{
		setFormat(Font, Size, Color, "left", 0, 1f, 1f);
	}
	
	/**
	 * You can use this if you have a lot of text parameters
	 * to set instead of the individual properties.
	 * 
	 * @param	Font		The name of the font face for the text display.
	 * @param	Size		The size of the font (in pixels essentially).
	 * 
	 * @return	This FlxText instance (nice for chaining stuff together, if you're into that).
	 */
	public void setFormat(String Font, float Size)
	{
		setFormat(Font, Size, 0xFFFFFFFF, "left", 0, 1f, 1f);
	}
	
	/**
	 * You can use this if you have a lot of text parameters
	 * to set instead of the individual properties.
	 * 
	 * @param	Font		The name of the font face for the text display.
	 * 
	 * @return	This FlxText instance (nice for chaining stuff together, if you're into that).
	 */
	public void setFormat(String Font)
	{
		setFormat(Font, 8, 0xFFFFFFFF, "left", 0, 1f, 1f);
	}
	
	/**
	 * You can use this if you have a lot of text parameters
	 * to set instead of the individual properties.
	 * 
	 * @return	This FlxText instance (nice for chaining stuff together, if you're into that).
	 */
	public void setFormat()
	{
		setFormat(null, 8, 0xFFFFFFFF, "left", 0, 1f, 1f);
	}
	
	public class NinePatch
	{
		public static final int TOP_LEFT = 0;
		public static final int TOP_CENTER = 1;
		public static final int TOP_RIGHT = 2;
		public static final int MIDDLE_LEFT = 3;
		public static final int MIDDLE_CENTER = 4;
		public static final int MIDDLE_RIGHT = 5;
		public static final int BOTTOM_LEFT = 6;
		public static final int BOTTOM_CENTER = 7;
		public static final int BOTTOM_RIGHT = 8;
		
		public int position;
		public String image;
		public int width;
		public int height;
		
		public NinePatch(int Position, String Img, int Width, int Height)
		{
			position = Position;
			image = Img;
			width = Width;
			height = Height;
		}
		
		public FlxSprite loadGraphic()
		{
			FlxSprite sprite = new FlxSprite().loadGraphic(image, false, false, width, height);
			sprite.immovable = true;
			sprite.scrollFactor.x = sprite.scrollFactor.y = 0;
			sprite.setOriginToCorner();
			return sprite;
		}
	}
}

