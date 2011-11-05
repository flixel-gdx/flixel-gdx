package org.flixel;

import org.flixel.data.SystemAsset;

import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;

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
	 * Internal reference to a Flash <code>TextField</code> object.
	 */
	protected BitmapFontCache _textField;
	/**
	 * Whether the actual text field needs to be regenerated and stamped again.
	 * This is NOT the same thing as <code>FlxSprite.dirty</code>.
	 */
	protected boolean _regen;
	/**
	 * Internal tracker for the text shadow color, default is clear/transparent.
	 */
	protected int _shadow;
	
	protected boolean wordWrap;
	
	private String text;
	protected HAlignment alignment;
	
	
	
	//TODO: add Font url
	public FlxText(float X, float Y, int Width, String Text)
	{
		super(X,Y);
		text = Text;
		if(text == null)
			text = "";
		_textField = SystemAsset.system;
		width = Width;		
		wordWrap = true;
		setText(text);
//		_textField.setColor(0xFFFFFFFF);
		_regen = true;
		_shadow = 0;
		allowCollisions = NONE;
	}
	
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
	public FlxText setFormat(float Size, int Color, HAlignment Alignment, int ShadowColor)
	{		
//		_textField.scale(Size);
		_textField.setColor(Color);
		alignment = Alignment;
//		_textField.drawMultiLine(spriteBatch, str, Size, Color, alignmentWidth, Alignment);
		_shadow = ShadowColor;
		_regen = true;
		
		return this;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{		
		if(!this.text.equals(text))
		{
			this.text = text;
			_regen = true;
		}
	}
	
	
	/**
	 * The size of the text being displayed.
	 */
//	public float getSize()
//	{
//		return _textField.getScaleX();
//	}
	
	/**
	 * @private
	 */
//	public void setSize(float Size)
//	{
//		_textField.scale(Size);
//		_regen = true;
//	}
	
	

	public int getColor()
	{
		return _textField.getColor().toIntBits();
	}
	
	
	
	public void setColor(int Color)
	{
		_textField.setColor(Color);
		_regen = true;
	}
	
	
	public void setAlpha(float Alpha)
	{
		_textField.setColor(_textField.getColor().r, _textField.getColor().g, _textField.getColor().b, Alpha);
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
		

		_point.x = x - (FlxG.camera.scroll.x * scrollFactor.x) - offset.x;
		_point.y = y - (FlxG.camera.scroll.y * scrollFactor.y) - offset.y;
		_point.x += (_point.x > 0) ? 0.0000001 : -0.0000001;
		_point.y += (_point.y > 0) ? 0.0000001 : -0.0000001;
		// if(((angle == 0) || (_bakedRotation > 0)) && (scale.x == 1) && (scale.y == 1) && (blend == null))
		{ // Simple render
		// framePixels.setPosition(_point.x, _point.y);
		// framePixels.setRotation(angle);
		// FlxG.batch.setProjectionMatrix(camera.buffer.combined);
		// framePixels.draw(FlxG.batch);
		// _textField.draw(FlxG.batch, text, _point.x, _point.y);
		// _textField.setPosition(_point.x, _point.y);
			_textField.setText(text, _point.x, _point.y);
			_textField.draw(FlxG.batch);
			// _textField.draw(FlxG.batch, text, x, y);
			// framePixels.draw(FlxG.batch);
		}
		// else
		{ // Advanced render
		// framePixels.setPosition(-origin.x, -origin.y);
		// framePixels.setScale(scale.x, scale.y);
		// if((angle != 0) && (_bakedRotation <= 0))
		// framePixels.setRotation(angle * 0.017453293f);
		// framePixels.setPosition(_point.x,_point.y);
		// FlxG.batch.setProjectionMatrix(camera.buffer.combined);
			// _textField.draw(FlxG.batch, text, x, y);
			// framePixels.draw(FlxG.batch);
		}
		_VISIBLECOUNT++;
		
	}
	
}
