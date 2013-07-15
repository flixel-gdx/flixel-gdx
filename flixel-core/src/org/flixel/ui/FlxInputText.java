package org.flixel.ui;

import org.flixel.FlxG;
import org.flixel.FlxText;
import org.flixel.ui.event.IFlxInputText;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import flash.events.Event;
import flash.events.KeyboardEvent;
import flash.events.Listener;

/**
 * Copyright (c) 2009 Martín Sebastián Wain
 * License: Creative Commons Attribution 3.0 United States
 * @link http://creativecommons.org/licenses/by/3.0/us/ 
 * 
 * Input textfield extension for Flixel.
 * Heavely modified to get it work on flixel-gdx.
 * Supports multiline and filters.
 * 
 * @author Ka Wing Chin
 * @author Gama11
 * @author Mr_Walrus
 * @author Martin Sebastian Wain
 * 
 * @link http://forums.flixel.org/index.php/topic,272.0.html
 */
public class FlxInputText extends FlxUITouchable
{
	/**
	 * No filter
	 */
	public static final String NO_FILTER = "";
	/**
	 * Only letters and no space.
	 */
	public static final String ONLY_ALPHA = "^\\d*$";
	/**
	 * Only numbers and no space.
	 */
	public static final String ONLY_NUMERIC = "^\\d*$";
	/**
	 * Only letters and numbers.
	 */
	public static final String ONLY_ALPHA_NUMERIC = "^[A-Za-z0-9_]+$";
	/**
	 * Only letters, numbers and white space.
	 */
	public static final String ONLY_ALPHA_NUMERIC_SPACE = "^[A-Za-z0-9_ ]+$";
	/**
	 * Custom filter.
	 */
	public static final String CUSTOM_FILTER = "";
	/**
	 * All cases.
	 */
	public static final int ALL_CASES = 0;
	/**
	 * Upper case.
	 */
	public static final int UPPER_CASE = 1;
	/**
	 * Lower case.
	 */
	public static final int LOWER_CASE = 2;
	/**
	 * Defines what text to filter. It can be NO_FILTER, ONLY_ALPHA,
	 * ONLY_NUMERIC, ONLY_ALPHA_NUMERIC or CUSTOM_FILTER.
	 */
	private String _filterMode = NO_FILTER;	
	/**
	 * This regular expression will filter out (remove) everything that matches.
	 * This is activated by setting filterMode = FlxInputText.CUSTOM_FILTER.
	 */	
	public String customFilterPattern;
	/**
	 * Text transform, ALL_CASES, LOWER_CASE and UPPER_CASE.
	 */
	private int _forceCase = ALL_CASES;
	/**
	 * The textfield that will be displayed.
	 */
	protected FlxTextCustom textField;
	/**
	 * A function called when the enter key is pressed on this text box.
	 */
	public IFlxInputText callback;	
	/**
	 * The max amount of characters the textfield can contain.
	 */
	private int _maxLength = 0;
	/**
	 * Whether the textfield is in password mode or not.
	 */
	private boolean _passwordMode;
	/**
	 * Tracks the amount of lines.
	 */
	private int _currentLineCounter = 1;
	/**
	 * Buffer text.
	 */
	protected StringBuffer textBuffer;
	/**
	 * Buffer password.
	 */
	private StringBuffer _passwordBuffer;
	/**
	 * Tracks whether the text got changed or not.
	 */
	private boolean _isChanged;
	
	/**
	 * Constructor
	 * @param X			The x-position of the component.
	 * @param Y			The y-position of the component.
	 * @param UISkin	The skin that needs to be applied.
	 * @param Label		The label along side the component.
	 * @param Width		The width of the component. Default 0, unlimited width.
	 * @param Height	The height of the component. Default 0, unlimited height.
	 */
	public FlxInputText(float X, float Y, FlxUISkin skin, String Label, int Width, int Height)
	{
		super(X, Y, skin, Label, Width, Height);
		textField = new FlxTextCustom(X, Y-6, Width, null, true);
		textField.setFormat(null, 16);
		textBuffer = new StringBuffer();
		_passwordBuffer = new StringBuffer();
		FlxG.getStage().addEventListener(KeyboardEvent.KEY_TYPED, handleKeyDown);
	}

	/**
	 * Constructor
	 * @param X			The x-position of the component.
	 * @param Y			The y-position of the component.
	 * @param UISkin	The skin that needs to be applied.
	 * @param Label		The label along side the component
	 * @param Width		The width of the component. Default 0, unlimited width.
	 */
	public FlxInputText(float X, float Y, FlxUISkin skin, String Label, int Width)
	{
		this(X, Y, skin, Label, Width, 0);
	}
	
	/**
	 * Constructor
	 * @param X			The x-position of the component.
	 * @param Y			The y-position of the component.
	 * @param UISkin	The skin that needs to be applied.
	 * @param Label		The label along side the component
	 */
	public FlxInputText(float X, float Y, FlxUISkin skin, String Label)
	{
		this(X, Y, skin, Label, 0, 0);
	}

	/**
	 * Constructor
	 * @param X			The x-position of the component.
	 * @param Y			The y-position of the component.
	 * @param UISkin	The skin that needs to be applied.
	 */
	public FlxInputText(float X, float Y, FlxUISkin skin)
	{
		this(X, Y, skin, null, 0, 0);
	}
	
	/**
	 * Constructor
	 * @param X			The x-position of the component.
	 * @param Y			The y-position of the component.
	 */
	public FlxInputText(float X, float Y)
	{
		this(X, Y, null, null, 0, 0);
	}

	@Override
	public void draw()
	{
		super.draw();
		textField.draw();
	}

	@Override
	public void update()
	{
		checkFocus();
		super.update();
	}
	
	/**
	 * Check whether a click has been occurred outside the textfield. Hides the keyboard on mobile.
	 */
	protected void checkFocus()
	{
		// Focus lost.
		if(activated && FlxG.mouse.justPressed() && !overlapsPoint(_point.make(FlxG.mouse.x, FlxG.mouse.y)))
		{
			setActive(false);
			// Remove the keyboard.
			if(FlxG.mobile)
				Gdx.input.setOnscreenKeyboardVisible(false);
		}
	}
	
	/**
	 * This will automatically called when textfield got clicked. Allows to type in the textfield.
	 * Shows the keyboard on mobile.
	 */
	@Override
	public void onChange()
	{
		setActive(true);
		if(FlxG.mobile)
			Gdx.input.setOnscreenKeyboardVisible(true);
	}

	/**
	 * Handles key presses generated on the stage.
	 * 
	 * @param e		The triggering keyboard event.
	 */
	Listener handleKeyDown = new Listener()
	{
		@Override
		public void onEvent(Event e) 
		{			
			if(activated)
			{
				int key = ((KeyboardEvent)e).keyCode;
				if(key == Keys.SHIFT_LEFT || key == Keys.SHIFT_RIGHT
				|| key == Keys.CONTROL_LEFT || key == Keys.CONTROL_RIGHT
				|| key == Keys.ALT_LEFT || key == Keys.ALT_RIGHT)
				{
					return;
				}
				// Backspace
				if(key == Keys.BACKSPACE)
				{
					if(textBuffer.length() > 0)
					{
						textBuffer.delete(textBuffer.length()-1, textBuffer.length());
						if(_passwordMode)
							_passwordBuffer.delete(_passwordBuffer.length()-1, _passwordBuffer.length());
						_isChanged = true;
					}
				}				
				// Enter
				else if(key == Keys.ENTER)
				{
					if(callback != null)
						callback.onEnter(textField.getText());
					if(1 < textField.getMaxLines() && _currentLineCounter < textField.getMaxLines())
					{
						textBuffer.append("\n");
						_isChanged = true;
					}
				}
				// Add some text
				else
				{
					if(textBuffer.length() < _maxLength || _maxLength == 0)
					{
						textBuffer.append(filter(String.valueOf(((KeyboardEvent)e).charCode)));
						if(_passwordMode)
							_passwordBuffer.append("*");
						_isChanged = true;
					}
				}
				if(_isChanged)
				{
					// Precalculate whether it fits the boundings or not.
					textField.setText(textBuffer);
					if(textField.getMaxLines() < textField.getTotalLines())
						textBuffer.deleteCharAt(textBuffer.length()-1);
					textField.setText(_passwordMode ? _passwordBuffer : textBuffer);
					_currentLineCounter = textField.getTotalLines() == -1 ? 1 : textField.getTotalLines();
				}
				_isChanged = false;
			}
		}
	};

	@Override
	public void destroy()
	{
		super.destroy();
		FlxG.getStage().removeEventListener(KeyboardEvent.KEY_TYPED, handleKeyDown);
		textField.destroy();
		textField = null;
		callback = null;
		textBuffer = null;
		_passwordBuffer = null;
	}

	/**
	 * Checks an input string against the current filter and returns a filtered string.
	 * 
	 * @param text	Unfiltered text
	 * @return Text filtered by the the filter mode of the box
	 */
	protected String filter(String text)
	{
		if(_forceCase == UPPER_CASE)
			text = text.toUpperCase();
		else if(_forceCase == LOWER_CASE)
			text = text.toLowerCase();
			
		if(_filterMode != NO_FILTER)
		{
			if(!text.matches(_filterMode))
				text = "";
		}	
		return text;
	}
	
	/**
	 * Get the textfield.
	 * @return	The textfield.
	 */
	public FlxText getTextField()
	{
		return textField;
	}
	
	/**
	 * Get the current case that got forced.
	 * @return
	 */
	public int getForceCase()
	{ 
		return _forceCase;
	}
	
	/**
	 * Enforce upper-case or lower-case
	 * @param	Case		The Case that's being enforced. Either ALL_CASES, UPPER_CASE or LOWER_CASE.
	 */
	public void setForceCase(int Case)
	{ 
		_forceCase = Case;
		textField.setText(filter(textField.getText()));
	}
	
	/**
	 * Set the font size.
	 * @param Size
	 */
	public void setSize(float Size)
	{
		textField.setSize(Size);
	}
	
	/**
	 * The maximum chars the textfield is allowed.
	 * @param Length
	 */
	public void setMaxLength(int Length)
	{
		_maxLength = Length;
		if (textField.getText().length() > _maxLength) 
			textField.setText(textField.getText().substring(0, _maxLength));
	}
	
	/**
	 * Set the maximum length for the field (e.g. "3" for Arcade type hi-score initials)
	 * @param	Length		The maximum length. 0 means unlimited.
	 */
	public int getMaxLength()
	{
		return _maxLength;
	}
	
	/**
	 * Set the maximum lines the textfield is allowed.
	 * @param Lines
	 */
	public void setMaxLines(int Lines)
	{
		textField.setMaxLines(Lines);
	}
	
	/**
	 * Make the textfield go in to password mode and show * for every char.
	 * @param enable
	 */
	public void setPasswordMode(boolean enable)
	{
		_passwordMode = enable;
	}
	
	/**
	 * Whether or not the textfield is a password textfield
	 * @param	enable		Whether to en- or disable password mode
	 */
	public boolean getPasswordMode()
	{
		return _passwordMode;
	}
	
	/**
	 * Set the filter mode that the text needs to be filtered.
	 * NO_FILTER, ONLY_ALPHA, ONLY_NUMERIC, ONLY_ALPHA_NUMERIC, ONLY_ALPHA_NUMERIC_SPACE or CUSTOM_FILTER
	 * @param filterMode
	 */
	public void setFilterMode(String filterMode)
	{
		_filterMode = filterMode;
		textField.setText(filter(textField.getText()));
	}
	
	/**
	 * Defines what text to filter. It can be NO_FILTER, ONLY_ALPHA, ONLY_NUMERIC, ONLY_ALPHA_NUMERIC, ONLY_ALPHA_NUMERIC_SPACE or CUSTOM_FILTER
	 * (Remember to append "FlxInputText." as a prefix to those constants)
	 * @param	newFilter		The filtering mode
	 */
	public String getFilterMode()
	{
		return _filterMode;
	}
	
	/**
	 * Set text in the textfield.
	 * @param Text
	 */
	public void setText(CharSequence Text)
	{
		textField.setText(Text);
	}
	
	/**
	 * Get the text from the textfield.
	 */
	public CharSequence getText()
	{
		return textField.getText();
	}
}

/**
 * Just a custmized FlxText. It calculates the boundings and how many lines there are.
 * 
 * @author Ka Wing Chin
 */
class FlxTextCustom extends FlxText
{
	/**
	 * The amount of lines allowed in the textfield.
	 */
	private int _maxLines = 1;
	/**
	 * The line height of the first line.
	 */
	private float _firstLineHeight;
	/**
	 * The bounding height for each line.
	 */
	private float _lineHeight;

	/**
	 * Constructor
	 * 
	 * @param	X				The X position of the text.
	 * @param	Y				The Y position of the text.
	 * @param	Width			The width of the text object (height is determined automatically).
	 * @param	Text			The actual text you would like to display initially.
	 * @param	EmbeddedFont	Whether this text field uses embedded fonts or not.
	 */
	public FlxTextCustom(float X, float Y, int Width, String Text, boolean EmbeddedFont)
	{
		super(X, Y, Width, Text, EmbeddedFont);		
	}
	
	@Override
	public FlxText setFormat(String Font, float Size, int Color, String Alignment, int ShadowColor, float ShadowX, float ShadowY)
	{
		super.setFormat(Font, Size, Color, Alignment, ShadowColor, ShadowX, ShadowY);
		// Save text.
		CharSequence text = getText();
		
		// Calculate lineheight.
		_textField.setWrappedText("ABC", 2, 3, FlxG.width, _alignment);
		_firstLineHeight = _textField.getBounds().height;
		_textField.setWrappedText("ABC\nABC", 2, 3, FlxG.width, _alignment);
		float doubleLine = _textField.getBounds().height;
		_lineHeight = doubleLine - _firstLineHeight;
		
		// Set text back.
		_text = text;
		calcFrame();
		return this;
	}
	
	/**
	 * Set the maximum lines.
	 * @param Lines
	 */
	public void setMaxLines(int Lines)
	{
		if(_maxLines == 0)
			return;
		_maxLines = Lines;
	}
	
	/**
	 * Get the maximum lines.
	 * @return
	 */
	public int getMaxLines()
	{
		return _maxLines;
	}
	
	/**
	 * Get the current bounding height of the textfield.
	 * @return
	 */
	public float getHeight()
	{
		return _textField.getBounds().height;
	}
	
	/**
	 * Get the total lines of the textfield.
	 * @return
	 */
	public int getTotalLines()
	{
		return (int) ((getHeight() - _firstLineHeight) / _lineHeight) + 1;
	}
}
