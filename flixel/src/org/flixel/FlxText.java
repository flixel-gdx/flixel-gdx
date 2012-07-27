package org.flixel;

import java.util.Locale;

import com.badlogic.gdx.assets.loaders.FreeTypeFontLoader.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
	protected String _font;
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
		
		width = Width;
		_text = Text;
		allowCollisions = NONE;
		setFormat("org/flixel/data/font/nokiafc22.ttf", 8, 0xFFFFFF, "left", 0);
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
	public FlxText setFormat(String Font, float Size, int Color, String Alignment, int ShadowColor)
	{
		if(Font == null)
			Font = _font;
		
		if (!Font.equals(_font) || Size != _size)
		{
			FreeTypeFontParameter parameters = new FreeTypeFontParameter();
			parameters.flip = true;
						
			_textField = new BitmapFontCache(FlxG.loadAsset(Font + ":" + (int)Size, BitmapFont.class, parameters));
		}
 
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
	public FlxText setFormat(String Font, float Size, int Color, String Alignment)
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
	public FlxText setFormat(String Font, float Size, int Color)
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
	public FlxText setFormat(String Font, float Size)
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
	public FlxText setFormat(String Font)
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
		
		setFormat(_font, Size, _color, getAlignment(), _shadow);
	}
	
	/**
	 * @private
	 */
	@Override
	public void setColor(int Color)
	{
		Color &= 0x00FFFFFFL;
		
		_color = Color;
	
		_textField.setColor((_color>>16)*0.00392f,(_color>>8&0xff)*0.00392f,(_color&0xff)*0.00392f,_alpha);
	}
	
	/**
	 * The font used for this text.
	 */
	public String getFont()
	{
		return _font;
	}
	
	/**
	 * @private
	 */
	public void setFont(String Font)
	{
		if (Font == _font)
			return;
		
		setFormat(Font, _size, _color, getAlignment(), _shadow);
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
		TextBounds bounds = _textField.setWrappedText(_text, 2, 3, width, _alignment);
		// bounds.height is shorter than it should be, and I don't know why.
		// After some trial and error, adding seven seems to be about right in most cases.
		height = (int) FlxU.ceil(bounds.height + 7);		
		setColor(_color);
	}
	
	@Override
	public void draw()
	{			
		if(_flickerTimer != 0)
		{
			_flicker = !_flicker;
			if(_flicker)
				return;
		}
		
		if (_textField.getBounds().width != width)
			calcFrame();
		
		FlxCamera camera = FlxG._activeCamera;
		
		if (cameras != null && !cameras.contains(camera, true))
			return;
		
		if(!onScreen(camera))
			return;
		
		_point.x = x - (camera.scroll.x * scrollFactor.x) - offset.x;
		_point.y = y - (camera.scroll.y * scrollFactor.y) - offset.y;
		_point.x += (_point.x > 0) ? 0.0000001 : -0.0000001;
		_point.y += (_point.y > 0) ? 0.0000001 : -0.0000001;
		
		_textField.setPosition(_point.x, _point.y);
		
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
			float[] rgba = FlxU.getRGBA(_shadow);
			_textField.setColor(rgba[0], rgba[1], rgba[2], rgba[3]);
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
			drawDebug(camera);
	}
}
