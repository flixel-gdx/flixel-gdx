package org.flixel;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.widget.TextView;
import flash.display.BitmapData;

/**
 * Extends <code>FlxSprite</code> to support rendering text. Can tint, fade,
 * rotate and scale just like a sprite. Doesn't really animate though, as far as
 * I know. Also does nice pixel-perfect centering on pixel fonts as long as they
 * are only one liners.
 */
public class FlxText extends FlxSprite
{

	protected TextView _tf;
	protected boolean _regen;
	protected int _shadow;
	
	protected String _text;	
	protected int _color;
	protected float _textSize;
	protected Align _align;
	protected Typeface _font;

	/**
	 * Creates a new <code>FlxText</code> object at the specified position.
	 * 
	 * @param	X				The X position of the text.
	 * @param	Y				The Y position of the text.
	 * @param	Width			The width of the text object (height is determined automatically).
	 * @param	Text			The actual text you would like to display initially.
	 * @param	EmbeddedFont	Whether this text field uses embedded fonts or nto
	 */
	public FlxText(int X, int Y, int Width, String Text, boolean EmbeddedFont)
	{
		super(X, Y);
		constructor(X, Y, Width, Text, EmbeddedFont);
	}

	public FlxText(int X, int Y, int Width, String Text)
	{
		super(X, Y);
		constructor(X, Y, Width, Text, true);
	}
	
	public FlxText(int X, int Y, int Width)
	{
		super(X, Y);
		constructor(X, Y, Width, null, true);
	}
	
	private void constructor(int X, int Y, int Width, String Text, boolean EmbeddedFont)
	{
		if(Text == null)
			Text = "";
			
		_text = Text;
		width = Width;
		_color = 0xFFFFFFFF;
		_textSize = 8;
		_align = Align.LEFT;
		_font = Typeface.createFromAsset(FlxResource.context.getAssets(), "font/nokiafc22.ttf");
		//_font = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
		
		_regen = true;
		_shadow = 0;
		setSolid(false);
		calcFrame();
	}
	
	
	/**
	 * You can use this if you have a lot of text parameters
	 * to set instead of the individual properties.
	 * 
	 * @param	Font		The name of the font face for the text display.
	 * @param	Size		The size of the font (in pixels essentially).
	 * @param	Color		The color of the text in traditional flash 0xRRGGBB format.
	 * @param	Alignment	A int representing the desired alignment (Gravity.right e.g.);
	 * @param	ShadowColor	A uint representing the desired text shadow color in flash 0xRRGGBB format.
	 * 
	 * @return	This FlxText instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxText setFormat(String Font, float Size, int Color, Align Alignment, int ShadowColor)
	{		
		if(Font != null)			
			_font = Typeface.createFromAsset(FlxResource.context.getAssets(), "font/"+Font);		
		_textSize = Size;
		_color = Color;
		if(Alignment != null)
			_align = Alignment;
		_shadow = ShadowColor;
		_regen = true;
		calcFrame();
		return this;
	}
	
	public FlxText setFormat(String Font, float Size, int Color, Align Alignment)
	{
		return setFormat(Font, Size, Color, Alignment, 0xFFFFFF);
	}
	
	public FlxText setFormat(String Font, float Size, int Color)
	{
		return setFormat(Font, Size, Color, Align.LEFT, 0xFFFFFF);
	}
	
	public FlxText setFormat(String Font, float Size)
	{
		return setFormat(Font, Size, 0xFFFFFF, Align.LEFT, 0xFFFFFF);
	}
	
	public FlxText setFormat(String Font)
	{
		return setFormat(Font, 8, 0xFFFFFF, Align.LEFT, 0xFFFFFF);
	}
	
	public FlxText setFormat()
	{
		return setFormat(null, 8, 0xFFFFFF, Align.LEFT, 0xFFFFFF);
	}
	
	/**
	 * The text being displayed.
	 */
	public String getText()
	{
		return _text;
	}
	
	/**
	 * @private
	 */
	public void setText(String Text)
	{
		String ot = _text;
		_text = Text;
		if(!_text.toString().equals(ot))
		{
			_regen = true;
			calcFrame();
		}
	}
	
	
	/**
	 * The size of the text being displayed.
	 */
	 public float getSize()
	{
		return _textSize;
	}

	/**
	 * @private
	 */
	public void setSize(float Size)
	{
		_textSize = Size;
		_regen = true;
		calcFrame();
	}
	
	
	/**
	 * The color of the text being displayed.
	 */
	@Override
	public int getColor()
	{
		return _color;
	}
	
	/**
	 * @private
	 */
	@Override
	public void setColor(int Color)
	{
		_color = Color;
		_regen = true;
		calcFrame();
	}
	
	/**
	 * The font used for this text.
	 */
	public String getFont()
	{
		return _font.toString();
	}
	 
	
	/**
	 * @private
	 */
	public void setFont(String Font)
	{
		_font = Typeface.createFromAsset(FlxResource.context.getAssets(), "font/" + Font);
		_regen = true;
		calcFrame();
	}
	
	/**
	 * The alignment of the font ("left", "right", or "center").
	 */
	public Align getAlignment()
	{
		return _align;
	}
	
	/**
	 * @private
	 */
	public void setAlignment(Align Alignment)
	{
		_align = Alignment;
		calcFrame();
	}
	
	/**
	 * The alignment of the font ("left", "right", or "center").
	 */
	public int getShadow()
	{
		return _shadow;
	}
	
	/**
	 * @private
	 */
	public void setShadow(int Color)
	{
		_shadow = Color;
		calcFrame();
	}
	
	
	/**
	 * Internal function to update the current animation frame.
	 */
	@Override
	protected void calcFrame()
	{
		Paint paint = new Paint();
		paint.setTextAlign(_align);
		paint.setColor(_color);
		paint.setTextSize(_textSize);
		paint.setTypeface(_font);
		paint.setTextAlign(_align);
		if(_shadow != 0)
			paint.setShadowLayer(1, 1, 1, _shadow);
		FontMetricsInt fm = paint.getFontMetricsInt();
		_framePixels.clearBitmap(); // Clear the traces.
		_framePixels.fillRect(_flashRect, 0);
		
		//Just leave if there's no text to render
		if(_text.length() <= 0)				
			return;
		
		if(_regen)
		{
			//Need to generate a new buffer to store the text graphic
			int nl = _text.split("\n").length;
			height = 0;
			for(int i = 0; i < nl; i++)
				height += fm.bottom - fm.top;
			height += 6; // account for 2px gutter on top and bottom
			_pixels = new BitmapData(width, height, true, 0);
			
			_bbb = new BitmapData(width, height, true, 0);
			frameHeight = height;

			_flashRect.setX(0);
			_flashRect.setY(0);
			_flashRect.setWidth(width);
			_flashRect.setHeight(height);
			_regen = false;
		}
		else // Else just clear the old buffer before redrawing the text	
			_pixels.fillRect(_flashRect, 0);
		
		
		
		// Finally, update the visible pixels
		if((_framePixels == null) || (_framePixels.width != _pixels.width) || (_framePixels.height != _pixels.height))
			_framePixels = new BitmapData(_pixels.width, _pixels.height, true, 0);
		
		_framePixels.fillRect(_flashRect, 0);
		if(_align == Align.CENTER)
			_framePixels.getCanvas().drawText(_text, (width - 4) / 2, height - 6, paint);
		else if(_align == Align.RIGHT)
			_framePixels.getCanvas().drawText(_text, width - 2, height - 6, paint);
		else
			_framePixels.getCanvas().drawText(_text, x, height - 6, paint);		
		_framePixels.copyPixels(_pixels, _flashRect, _flashPointZero);
		
		if(FlxG.showBounds)
			drawBounds();
		if(getSolid())
			refreshHulls();
	}

}
