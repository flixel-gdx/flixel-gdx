package org.flixel;

import org.flixel.system.gdx.text.GdxTextField;

import flash.text.TextField;
import flash.text.TextFormat;

/**
 * Extends <code>FlxSprite</code> to support rendering text.
 * Can tint, fade, rotate and scale just like a sprite.
 * Doesn't really animate though, as far as I know.
 * Also does nice pixel-perfect centering on pixel fonts
 * as long as they are only one liners.
 * 
 * @author Ka Wing Chin
 * @author Thomas Weston
 */
public class FlxText extends FlxSprite
{
	/**
	 * Internal reference to a Flash <code>TextField</code> object.
	 */
	protected TextField _textField;
	/**
	 * Whether the actual text field needs to be regenerated and stamped again.
	 * This is NOT the same thing as <code>FlxSprite.dirty</code>.
	 */
	protected boolean _regen;
	/**
	 * Internal tracker for the text shadow color, default is clear/transparent.
	 */
	protected int _shadow;

	/**
	 * Creates a new <code>FlxText</code> object at the specified position.
	 * 
	 * @param	X				The X position of the text.
	 * @param	Y				The Y position of the text.
	 * @param	Width			The width of the text object (height is determined automatically).
	 * @param	Text			The actual text you would like to display initially.
	 * @param	EmbeddedFont	Whether this text field uses embedded fonts or not.
	 */
	public FlxText(float X, float Y, int Width, String Text, boolean EmbeddedFont)
	{
		super(X,Y);
		width = frameWidth = Width;

		if(Text == null)
			Text = "";
		_textField = new GdxTextField();
		_textField.width = Width;
		_textField.setText(Text);
		TextFormat format = new TextFormat("org/flixel/data/font/nokiafc22.ttf",8,0xffffff);
		_textField.defaultTextFormat = format;
		_textField.setTextFormat(format);
		
		_regen = true;
		_shadow = 0;
		allowCollisions = NONE;
		calcFrame();
	}

	/**
	 * Creates a new <code>FlxText</code> object at the specified position.
	 * 
	 * @param	X		The X position of the text.
	 * @param	Y		The Y position of the text.
	 * @param	Width	The width of the text object (height is determined automatically).
	 * @param	Text	The actual text you would like to display initially.
	 */
	public FlxText(float X, float Y, int Width, String Text)
	{
		this(X, Y, Width, Text, true);
	}

	/**
	 * Creates a new <code>FlxText</code> object at the specified position.
	 * 
	 * @param	X		The X position of the text.
	 * @param	Y		The Y position of the text.
	 * @param	Width	The width of the text object (height is determined automatically).
	 */
	public FlxText(float X, float Y, int Width)
	{
		this(X, Y, Width, null, true);
	}

	/**
	 * Clean up memory.
	 */
	@Override
	public void destroy()
	{
		_textField = null;
		super.destroy();
	}

	/**
	 * You can use this if you have a lot of text parameters 
	 * to set instead of the individual properties.
	 * 
	 * @param	Font		The name of the font face for the text display.
	 * @param	Size		The size of the font (in pixels essentially).
	 * @param	Color		The color of the text in 0xRRGGBB format.
	 * @param	Alignment	A string representing the desired alignment ("left,"right" or "center").
	 * @param	ShadowColor	A uint representing the desired text shadow color 0xAARRGGBB format.
	 * 
	 * @return	This FlxText instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxText setFormat(String Font,float Size,int Color,String Alignment,int ShadowColor)
	{
		if(Font == null)
			Font = "";
		TextFormat format = _textField.defaultTextFormat;
		format.font = Font;
		format.size = Size;
		format.color = Color;
		format.align = Alignment;
		_textField.defaultTextFormat = format;
		_textField.setTextFormat(format);
		_shadow = ShadowColor;
		_regen = true;
		calcFrame();
		return this;
	}

	/**
	 * You can use this if you have a lot of text parameters
	 * to set instead of the individual properties.
	 * 
	 * @param	Font		The name of the font face for the text display.
	 * @param	Size		The size of the font (in pixels essentially).
	 * @param	Color		The color of the text in 0xRRGGBB format.
	 * @param	Alignment	A string representing the desired alignment ("left,"right" or "center").
	 * 
	 * @return	This FlxText instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxText setFormat(String Font,float Size,int Color,String Alignment)
	{
		return setFormat(Font,Size,Color,Alignment,0);
	}

	/**
	 * You can use this if you have a lot of text parameters
	 * to set instead of the individual properties.
	 * 
	 * @param	Font		The name of the font face for the text display.
	 * @param	Size		The size of the font (in pixels essentially).
	 * @param	Color		The color of the text in 0xRRGGBB format.
	 * 
	 * @return	This FlxText instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxText setFormat(String Font,float Size,int Color)
	{
		return setFormat(Font,Size,Color,null,0);
	}

	/**
	 * You can use this if you have a lot of text parameters
	 * to set instead of the individual properties.
	 * 
	 * @param	Font	The name of the font face for the text display.
	 * @param	Size	The size of the font (in pixels essentially).
	 * 
	 * @return	This FlxText instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxText setFormat(String Font,float Size)
	{
		return setFormat(Font,Size,0xffffffff,null,0);
	}

	/**
	 * You can use this if you have a lot of text parameters
	 * to set instead of the individual properties.
	 * 
	 * @param	Font	The name of the font face for the text display.
	 * 
	 * @return	This FlxText instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxText setFormat(String Font)
	{
		return setFormat(Font,8,0xffffffff,null,0);
	}

	/**
	 * You can use this if you have a lot of text parameters
	 * to set instead of the individual properties.
	 * 
	 * @return	This FlxText instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxText setFormat()
	{
		return setFormat(null,8,0xffffffff,null,0);
	}

	/**
	 * The text being displayed.
	 */
	public String getText()
	{
		return _textField.getText();
	}

	/**
	 * The text being displayed.
	 */
	public void setText(String Text)
	{
		String ot = _textField.getText();
		_textField.setText(Text);
		if(!_textField.getText().equals(ot))
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
		return _textField.defaultTextFormat.size;
	}

	/**
	 * The size of the text being displayed.
	 */
	public void setSize(float Size)
	{
		TextFormat format = _textField.defaultTextFormat;
		format.size = Size;
		_textField.defaultTextFormat = format;
		_textField.setTextFormat(format);
		_regen = true;
		calcFrame();
	}

	/**
	 * The color of the text being displayed.
	 */
	@Override
	public int getColor() 
	{
		return _textField.defaultTextFormat.color;
	}
	
	/**
	 * The color of the text being displayed.
	 */
	@Override
	public void setColor(int Color)
	{
		TextFormat format = _textField.defaultTextFormat;
		format.color = Color;
		_textField.defaultTextFormat = format;
		_textField.setTextFormat(format);
		_regen = true;
		calcFrame();
	}
	
	/**
	 * The font used for this text.
	 */
	public String getFont()
	{
		return _textField.defaultTextFormat.font;
	}

	/**
	 * The font used for this text.
	 */
	public void setFont(String Font)
	{
		TextFormat format = _textField.defaultTextFormat;
		format.font = Font;
		_textField.defaultTextFormat = format;
		_textField.setTextFormat(format);
		_regen = true;
		calcFrame();
	}

	/**
	 * The alignment of the font ("left", "right", or "center").
	 */
	public String getAlignment()
	{
		return _textField.defaultTextFormat.align;
	}

	/**
	 * The alignment of the font ("left", "right", or "center").
	 */
	public void setAlignment(String Alignment)
	{
		TextFormat format = _textField.defaultTextFormat;
		format.align = Alignment;
		_textField.defaultTextFormat = format;
		_textField.setTextFormat(format);
		calcFrame();
	}

	/**
	 * The color of the text shadow in 0xAARRGGBB hex format.
	 */
	public int getShadow()
	{
		return _shadow;
	}

	/**
	 * The color of the text shadow in 0xAARRGGBB hex format.
	 */
	public void setShadow(int Color)
	{
		_shadow = Color;
	}

	@Override
	protected void calcFrame()
	{
		_textField.width = width;
		_textField.setText(_textField.getText()); //force text field to redraw
		height = frameHeight = FlxU.ceil(_textField.height * 1.2f);
		_regen = false;
		dirty = false;
	}
	
	@Override
	public void draw()
	{
		if(_flicker)
			return;
		
		if(dirty)
			calcFrame();
		
		FlxCamera camera = FlxG._activeCamera;

		if(cameras == null)
			cameras = FlxG.cameras;

		if(!cameras.contains(camera, true))
			return;

		if(!onScreen(camera))
			return;

		_point.x = x - (camera.scroll.x*scrollFactor.x) - offset.x;
		_point.y = y - (camera.scroll.y*scrollFactor.y) - offset.y;
		_point.x += (_point.x > 0)?0.0000001f:-0.0000001f;
		_point.y += (_point.y > 0)?0.0000001f:-0.0000001f;
		
		_textField.x = _point.x;
		_textField.y = _point.y;
		
		_textField.rotation = angle;
		
		_textField.scaleX = scale.x;
		_textField.scaleY = scale.y;
		
		_textField.alpha = getAlpha();
		
		//Render a single pixel shadow beneath the text
		if(_shadow != 0)
		{
			_textField.x += 1;
			_textField.y += 1;
			
			TextFormat format = _textField.defaultTextFormat;
			int color = format.color;
			format.color = FlxU.multiplyColors(_shadow, camera.getColor());
			_textField.setTextFormat(format);
			
			((GdxTextField) _textField).render();
			
			format.color = color;
			_textField.setTextFormat(format);
			
			_textField.x -= 1;
			_textField.y -= 1;
		}
		
		TextFormat format = _textField.defaultTextFormat;
		int color = format.color;
		format.color = FlxU.multiplyColors(color, camera.getColor());
		_textField.setTextFormat(format);
		
		((GdxTextField) _textField).render();

		format.color = color;
		_textField.setTextFormat(format);
		
		_VISIBLECOUNT++;

		if(FlxG.visualDebug && !ignoreDrawDebug)
			drawDebug(camera);
	}
}
														