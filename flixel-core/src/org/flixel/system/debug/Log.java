package org.flixel.system.debug;

import org.flixel.system.FlxWindow;
import org.flixel.system.gdx.text.GdxTextField;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import flash.text.TextField;
import flash.text.TextFormat;

/**
 * A simple trace output window for use in the debugger overlay.
 * 
 * @author Thomas Weston
 */
public class Log extends FlxWindow
{
	static protected final int MAX_LOG_LINES = 200;

	protected TextField _text;
	protected Array<String> _lines;

	/**
	 * Creates a new window object.  This Flash-based class is mainly (only?) used by <code>FlxDebugger</code>.
	 * 
	 * @param	Title		The name of the window, displayed in the header bar.
	 * @param	Width		The initial width of the window.
	 * @param	Height		The initial height of the window.
	 * @param	Resizable	Whether you can change the size of the window with a drag handle.
	 * @param	Bounds		A rectangle indicating the valid screen area for the window.
	 * @param	BGColor		What color the window background should be, default is gray and transparent.
	 * @param	TopColor	What color the window header bar should be, default is black and transparent.
	 */
	public Log(String Title, float Width, float Height, boolean Resizable, Rectangle Bounds, int BGColor, int TopColor)
	{
		super(Title, Width, Height, Resizable, Bounds, BGColor, TopColor);

		_text = new GdxTextField();
		_text.x = 2;
		_text.y = 15;
		//_text.multiline = true;
		//_text.wordWrap = true;
		//_text.selectable = true;
		_text.defaultTextFormat = new TextFormat("Courier",12,0xffffff);
		//addChild(_text);

		_lines = new Array<String>(true, 16);
	}

	/**
	 * Creates a new window object.  This Flash-based class is mainly (only?) used by <code>FlxDebugger</code>.
	 * 
	 * @param	Title		The name of the window, displayed in the header bar.
	 * @param	Width		The initial width of the window.
	 * @param	Height		The initial height of the window.
	 * @param	Resizable	Whether you can change the size of the window with a drag handle.
	 * @param	Bounds		A rectangle indicating the valid screen area for the window.
	 * @param	BGColor		What color the window background should be, default is gray and transparent.
	 */
	public Log(String Title, float Width, float Height, boolean Resizable, Rectangle Bounds, int BGColor)
	{
		this(Title, Width, Height, Resizable, Bounds, BGColor, 0x7f000000);
	}

	/**
	 * Creates a new window object.  This Flash-based class is mainly (only?) used by <code>FlxDebugger</code>.
	 * 
	 * @param	Title		The name of the window, displayed in the header bar.
	 * @param	Width		The initial width of the window.
	 * @param	Height		The initial height of the window.
	 * @param	Resizable	Whether you can change the size of the window with a drag handle.
	 * @param	Bounds		A rectangle indicating the valid screen area for the window.
	 */
	public Log(String Title, float Width, float Height, boolean Resizable, Rectangle Bounds)
	{
		this(Title, Width, Height, Resizable, Bounds, 0x7f7f7f7f, 0x7f000000);
	}

	/**
	 * Creates a new window object.  This Flash-based class is mainly (only?) used by <code>FlxDebugger</code>.
	 * 
	 * @param	Title		The name of the window, displayed in the header bar.
	 * @param	Width		The initial width of the window.
	 * @param	Height		The initial height of the window.
	 * @param	Resizable	Whether you can change the size of the window with a drag handle.
	 */
	public Log(String Title, float Width, float Height, boolean Resizable)
	{
		this(Title, Width, Height, Resizable, null, 0x7f7f7f7f, 0x7f000000);
	}

	/**
	 * Creates a new window object.  This Flash-based class is mainly (only?) used by <code>FlxDebugger</code>.
	 * 
	 * @param	Title		The name of the window, displayed in the header bar.
	 * @param	Width		The initial width of the window.
	 * @param	Height		The initial height of the window.
	 */
	public Log(String Title, float Width, float Height)
	{
		this(Title, Width, Height, true, null, 0x7f7f7f7f, 0x7f000000);
	}

	/**
	 * Clean up memory.
	 */
	@Override
	public void destroy()
	{
		//removeChild(_text);
		_text = null;
		_lines = null;
		super.destroy();
	}

	/**
	 * Adds a new line to the log window.
	 * 
	 * @param	Text		The line you want to add to the log window.
	 */
	public void add(String Text)
	{
		if(_lines.size <= 0)
			_text.setText("");
		_lines.add(Text);
		if(_lines.size > MAX_LOG_LINES)
		{
			_lines.removeIndex(0);
			String newText = "";
			for(int i = 0; i < _lines.size; i++)
				newText += _lines.get(i)+"\n";
			_text.setText(newText);
		}
		//else
		//	_text.appendText(Text+"\n");
		//	_text.scrollV = _text.height;
	}

	/**
	 * Adjusts the width and height of the text field accordingly.
	 */
	@Override
	protected void updateSize()
	{
		super.updateSize();

		_text.width = _width-10;
		_text.height = _height-15;
	}
}
