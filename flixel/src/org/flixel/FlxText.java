package org.flixel;

import java.util.Locale;

import org.flixel.data.SystemAsset;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.math.Matrix4;

/**
 * Extends <code>FlxSprite</code> to support rendering text.
 * Can tint, fade, rotate and scale just like a sprite.
 * Doesn't really animate though, as far as I know.
 * Also does nice pixel-perfect centering on pixel fonts
 * as long as they are only one liners.
 * 
 * @author	Ka Wing Chin
 */
public class FlxText extends FlxSprite
{	
	/**
	 * Internal reference to a libgdx <code>BitmapFontCache</code> object.
	 */
	protected BitmapFontCache _textField;
	/**
	 * Internal tracker for the text shadow color, default is clear/transparent.
	 */
	protected int _shadow;
	/**
	 * Internal tracker for the alignment of the text.
	 */
	protected HAlignment _alignment;
	/**
	 * Internal reference to the text to be drawn.
	 */
	protected String _text;
	/**
	 * Internal helper for rotation.
	 */
	protected Matrix4 _matrix;
	/**
	 * Internal reference to the font.
	 */
	protected FileHandle _font;
	/**
	 * Internal tracker for the size of the text.
	 */
	protected int _size;
	
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
		
		if(Text == null)
			Text = "";
		
		_textField = new BitmapFontCache(SystemAsset.system);
		_font = SystemAsset.systemFileHandle;
		width = Width;
		_text = Text;
		setColor(0xFFFFFF);
		_size = 8;
		_alignment = HAlignment.LEFT;
		_shadow = 0;
		allowCollisions = NONE;
		
		calcFrame();
	}
	
	/**
	 * Creates a new <code>FlxText</code> object at the specified position.
	 * 
	 * @param	X				The X position of the text.
	 * @param	Y				The Y position of the text.
	 * @param	Width			The width of the text object (height is determined automatically).
	 * @param	Text			The actual text you would like to display initially.
	 */
	public FlxText(float X, float Y, int Width, String Text)
	{
		this(X, Y, Width, Text, true);
	}
	
	/**
	 * Creates a new <code>FlxText</code> object at the specified position.
	 * 
	 * @param	X				The X position of the text.
	 * @param	Y				The Y position of the text.
	 * @param	Width			The width of the text object (height is determined automatically).
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
	 * @param	Color		The color of the text in traditional flash 0xRRGGBB format.
	 * @param	Alignment	A string representing the desired alignment ("left,"right" or "center").
	 * @param	ShadowColor	A uint representing the desired text shadow color in flash 0xRRGGBB format.
	 * 
	 * @return	This FlxText instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxText setFormat(FileHandle Font, float Size, int Color, String Alignment, int ShadowColor)
	{
		if(Font == null)
			Font = SystemAsset.systemFileHandle;
	
		if (!Font.equals(_font) || Size != _size)
			_textField = new BitmapFontCache(FlxG.addFont(Font, Size));
		
		_font = Font;
		_size = (int) Size;
		
		setColor(Color);
		_alignment = HAlignment.valueOf(Alignment.toUpperCase(Locale.ENGLISH));		
		_shadow = ShadowColor;
		
		calcFrame();
	
		return this;
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
	public FlxText setFormat(FileHandle Font, float Size, int Color, String Alignment)
	{
		return setFormat(Font, Size, Color, Alignment, 0);
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
	public FlxText setFormat(FileHandle Font, float Size, int Color)
	{
		return setFormat(Font, Size, Color, "left", 0);
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
	public FlxText setFormat(FileHandle Font, float Size)
	{
		return setFormat(Font, Size, 0xFFFFFFFF, "left", 0);
	}
	
	/**
	 * You can use this if you have a lot of text parameters
	 * to set instead of the individual properties.
	 * 
	 * @param	Font		The name of the font face for the text display.
	 * 
	 * @return	This FlxText instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxText setFormat(FileHandle Font)
	{
		return setFormat(Font, 8, 0xFFFFFFFF, "left", 0);
	}
	
	/**
	 * You can use this if you have a lot of text parameters
	 * to set instead of the individual properties.
	 * 
	 * @return	This FlxText instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxText setFormat()
	{
		return setFormat(null, 8, 0xFFFFFFFF, "left", 0);
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
		_text = Text;
		calcFrame();
	}
	
	/**
	 * The size of the text being displayed.
	 */
	public float getSize()
	{
		return _size;
	}
	
	/**
	 * @private
	 */
	public void setSize(float Size)
	{
		if (Size == _size)
			return;
		
		_size = (int) Size;
		_textField = new BitmapFontCache(FlxG.addFont(_font, Size));
		calcFrame();
	}
	
	/**
	 * @private
	 */
	@Override
	public void setColor(long Color)
	{
		Color &= 0x00FFFFFFL;
		
		_color = Color;
	
		_textField.setColor((_color>>16)*0.00392f,(_color>>8&0xff)*0.00392f,(_color&0xff)*0.00392f,_alpha);
	}
	
	/**
	 * The font used for this text.
	 */
	public FileHandle getFont()
	{
		return _font;
	}
	
	/**
	 * @private
	 */
	public void setFont(FileHandle Font)
	{
		if (_font.equals(Font))
			return;
		
		_textField = new BitmapFontCache(FlxG.addFont(Font, _size));
		calcFrame();
	}
	
	/**
	 * The alignment of the font ("left", "right", or "center").
	 */
	public String getAlignment()
	{
		return _alignment.toString().toLowerCase(Locale.ENGLISH);
	}
	
	/**
	 * @private
	 */
	public void setAlignment(String Alignment)
	{
		_alignment = HAlignment.valueOf(Alignment.toUpperCase(Locale.ENGLISH));
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
	 * @private
	 */
	public void setShadow(int Color)
	{
		_shadow = Color;
	}
	
	@Override
	public void setAlpha(float Alpha)
	{
		if(Alpha > 1)
			Alpha = 1;
		if(Alpha < 0)
			Alpha = 0;
		if(Alpha == _alpha)
			return;

		_textField.setColor(_textField.getColor().r, _textField.getColor().g, _textField.getColor().b, Alpha);
		_alpha = Alpha;
	}	
	
	@Override
	protected void calcFrame()
	{
		TextBounds bounds = _textField.setWrappedText(_text, 0, 0, width, _alignment);
		height = (int) FlxU.ceil(bounds.height);		
		setColor(_color);
	}
	
	@Override
	public void draw(FlxCamera Camera)
	{			
		if(_flickerTimer != 0)
		{
			_flicker = !_flicker;
			if(_flicker)
				return;
		}
		
		if (_textField.getBounds().width != width)
			calcFrame();
		
		if(!onScreen(Camera))
			return;
		
		_point.x = x - (Camera.scroll.x * scrollFactor.x) - offset.x;
		_point.y = y - (Camera.scroll.y * scrollFactor.y) - offset.y;
		_point.x += (_point.x > 0) ? 0.0000001 : -0.0000001;
		_point.y += (_point.y > 0) ? 0.0000001 : -0.0000001;
		
		_textField.setPosition(_point.x, _point.y + 2);
		
		if (angle != 0)
		{
			_matrix = FlxG.batch.getTransformMatrix().cpy();
		
			Matrix4 rotationMatrix = FlxG.batch.getTransformMatrix();
			rotationMatrix.translate(_textField.getX() + (width / 2), _textField.getY() + (height / 2), 0);
			rotationMatrix.rotate(0, 0, 1, angle);
			rotationMatrix.translate(-(_textField.getX() + (width / 2)), -(_textField.getY() + (height / 2)), 0);
		
			FlxG.batch.setTransformMatrix(rotationMatrix);
		}
		
		//Render a single pixel shadow beneath the text
		if (_shadow != 0)
		{
			_textField.setColor(FlxU.colorFromHex(_shadow));
			_textField.translate(1f, 1f);
			_textField.draw(FlxG.batch);
			_textField.translate(-1f, -1f);
			setColor(_color);
		}
		
		_textField.draw(FlxG.batch);
		
		if (angle != 0)
			FlxG.batch.setTransformMatrix(_matrix);
		
		_VISIBLECOUNT++;
		
		if(FlxG.visualDebug && !ignoreDrawDebug)
			drawDebug(Camera);
	}
}
