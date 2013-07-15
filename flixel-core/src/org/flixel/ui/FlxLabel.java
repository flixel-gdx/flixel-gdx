package org.flixel.ui;

import org.flixel.FlxText;

/**
 * <code>FlxLabel</code> differs from <code>FlxText</code> with the boundings.
 * It automatically adjust the dimensions to the boundings of the text.
 * 
 * @author Ka Wing Chin
 */
public class FlxLabel extends FlxText
{
	/**
	 * Internal, to track if the size needs to be auto or not.
	 */
	private int _width;
	/**
	 * Internal, to track if the size needs to be auto or not.
	 */
	private int _height;

	/**
	 * Constructor
	 * @param X			The x-position of the label.
	 * @param Y			The y-position of the label.
	 * @param Text		The text of the label.
	 * @param Width		The width of the label. Default 0 is auto.
	 * @param Height	The height of the label. Default 0 is auto.
	 */
	public FlxLabel(float X, float Y, String Text, int Width, int Height)
	{
		super(X, Y, Width, Text);
		_width = Width;
		_height = Height;
		setSize(16);
	}
	
	/**
	 * Constructor
	 * @param X			The x-position of the label.
	 * @param Y			The y-position of the label.
	 * @param Text		The text of the label.
	 * @param Width		The width of the label. Default 0 is auto.
	 */
	public FlxLabel(float X, float Y, String Text, int Width)
	{
		this(X, Y, Text, Width, 0);
	}
	
	/**
	 * Constructor
	 * @param X			The x-position of the label.
	 * @param Y			The y-position of the label.
	 * @param Text		The text of the label.
	 */
	public FlxLabel(float X, float Y, String Text)
	{
		this(X, Y, Text, 0, 0);
	}
		
	/**
	 * Constructor
	 * @param X			The x-position of the label.
	 * @param Y			The y-position of the label.
	 */
	public FlxLabel(float X, float Y)
	{
		this(X, Y, null, 0, 0);
	}
	
	@Override
	public FlxText setFormat(String Font, float Size, int Color, String Alignment, int ShadowColor, float ShadowX, float ShadowY)
	{
		super.setFormat(Font, Size, Color, Alignment, ShadowColor, ShadowX, ShadowY);
		if(_width == 0)
			width = frameWidth = (int) _textField.getFont().getBounds(_text).width;
		else
			width = _width;
		
		if(_height == 0)
			height = frameHeight = (int) _textField.getBounds().height;
		else
			height = _height;
		return this;
	}
	
	/**
	 * Internal function to update the current animation frame.
	 * Enable to recalc the frame which is protected in FlxText.
	 */
	public void calcFrame()
	{
		super.calcFrame();
	}

}

