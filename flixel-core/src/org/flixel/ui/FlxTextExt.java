package org.flixel.ui;

import org.flixel.FlxText;

/**
 * Just a customized FlxText. It calculates the bounding and how many lines
 * there are.
 * 
 * @author Ka Wing Chin
 */
public class FlxTextExt extends FlxText
{
	/**
	 * The line height of the first line.
	 */
	private float _firstLineHeight;
	/**
	 * The bounding height for each line.
	 */
	private float _lineHeight;
	/**
	 * Internal, to track if the size needs to be auto or not.
	 */
	private int _width;
	/**
	 * Internal, to track if the size needs to be auto or not.
	 */
	private int _height;

	/**
	 * Creates a new <code>FlxTextExt</code> object.
	 * 
	 * @param X The X position of the text.
	 * @param Y The Y position of the text.
	 * @param Width The width of the text object (height is determined
	 *        automatically).
	 * @param Text The actual text you would like to display initially.
	 * @param EmbeddedFont Whether this text field uses embedded fonts or not.
	 */
	public FlxTextExt(float X, float Y, int Width, int Height, String Text, boolean EmbeddedFont)
	{
		super(X, Y, Width, Text, EmbeddedFont);
		_width = Width;
		_height = Height;
		calcFrame();
	}

	/**
	 * Creates a new <code>FlxTextExt</code> object.
	 * 
	 * @param X The X position of the text.
	 * @param Y The Y position of the text.
	 * @param Width The width of the text object (height is determined
	 *        automatically).
	 * @param Text The actual text you would like to display initially.
	 */
	public FlxTextExt(float X, float Y, int Width, int Height, String Text)
	{
		this(X, Y, Width, Height, Text, true);
	}

	/**
	 * Creates a new <code>FlxTextExt</code> object.
	 * 
	 * @param X The X position of the text.
	 * @param Y The Y position of the text.
	 * @param Width The width of the text object (height is determined
	 *        automatically).
	 */
	public FlxTextExt(float X, float Y, int Width, int Height)
	{
		this(X, Y, Width, Height, null, true);
	}

	/**
	 * Creates a new <code>FlxTextExt</code> object.
	 * 
	 * @param X The X position of the text.
	 * @param Y The Y position of the text.
	 * @param Width The width of the text object (height is determined
	 *        automatically).
	 */
	public FlxTextExt(float X, float Y, int Width)
	{
		this(X, Y, Width, 0, null, true);
	}

	/**
	 * Creates a new <code>FlxTextExt</code> object.
	 * 
	 * @param X The X position of the text.
	 * @param Y The Y position of the text.
	 */
	public FlxTextExt(float X, float Y)
	{
		this(X, Y, 0, 0, null, true);
	}

	@Override
	public FlxText setFormat(String Font, float Size, int Color, String Alignment, int ShadowColor)
	{
		super.setFormat(Font, Size, Color, Alignment, ShadowColor);
		// Save text.
		String text = getText();

		// Calculate lineheight.
		_textField.setText("ABC");
		_firstLineHeight = _textField.height;
		_textField.setText("ABC\nABC");
		float doubleLine = _textField.height;
		_lineHeight = doubleLine - _firstLineHeight;

		if(_width == 0)
			width = frameWidth = (int) _textField.width;
		else
			width = _width;

		if(_height == 0)
			height = frameHeight = (int) _textField.height;
		else
			height = _height;

		// Set text back.
		setText(text);
		calcFrame();
		return this;
	}

	/**
	 * Get the current bounding height of the textfield.
	 * 
	 * @return
	 */
	public float getHeight()
	{
		return _textField.height;
	}

	/**
	 * Get the total lines of the textfield.
	 * 
	 * @return
	 */
	public int getTotalLines()
	{
		return (int) ((getHeight() - _firstLineHeight) / _lineHeight) + 1;
	}

	/**
	 * Internal function to update the current animation frame. Gives access to
	 * calcFrame which is normally protected.
	 */
	public void calcFrame()
	{
		super.calcFrame();
	}
}
