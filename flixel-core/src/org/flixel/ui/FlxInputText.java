package org.flixel.ui;

import org.flixel.FlxG;
import org.flixel.FlxText;
import org.flixel.FlxU;
import org.flixel.ui.event.IFlxInputText;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;

import flash.events.Event;
import flash.events.KeyboardEvent;
import flash.events.Listener;

/**
 * FlxInputText v1.10, Input text field extension for Flixel.
 * Heavely modified to get it working for flixel-gdx.
 * 
 * @author Gama11
 * @author Mr_Walrus
 * @author Nitram_cero
 * @author Martin Sebastian Wain
 * @author Ka Wing Chin
 */
public class FlxInputText extends FlxUITouchable
{	
	protected FlxTextCustom textField;
	static public final String NO_FILTER = "";
	static public final String ONLY_ALPHA = "^\\d*$";
	static public final String ONLY_NUMERIC = "^\\d*$";
	static public final String ONLY_ALPHANUMERIC = "^[A-Za-z0-9_]+$";
	/**
	 * Alpha numeric with white space.
	 */
	static public final String ONLY_ALPHANUMERICSPACE = "^[A-Za-z0-9_ ]+$";
	static public final String CUSTOM_FILTER = "";

	static public final int ALL_CASES = 0;
	static public final int UPPER_CASE = 1;
	static public final int LOWER_CASE = 2;

	/**
	 * Defines what text to filter. It can be NO_FILTER, ONLY_ALPHA,
	 * ONLY_NUMERIC, ONLY_ALPHA_NUMERIC or CUSTOM_FILTER (Remember to append
	 * "FlxInputText." as a prefix to those constants)
	 */
	private String _filterMode = NO_FILTER;

	/**
	 * This regular expression will filter out (remove) everything that matches.
	 * This is activated by setting filterMode = FlxInputText.CUSTOM_FILTER.
	 */
	public String customFilterPattern;

	/**
	 * A function called when the enter key is pressed on this text box.
	 * Function should be formatted "onEnterPressed(text:String)".
	 */
	public IFlxInputText callback;
	/**
	 * If this is set to true, text typed is forced to be uppercase.
	 */
	private int _forceCase = ALL_CASES;

	/**
	 * The max amount of characters the textfield can contain.
	 */
	private int _maxLength = 0;	
	private int _currentLineCounter = 1;
	/**
	 * Buffer text.
	 */
	protected StringBuffer buffer;
	private boolean _isChanged;
	private boolean _passwordMode;
	private StringBuffer _textPassword;
	

	/**
	 * Creates a new editable text box.
	 * 
	 * @param X					The X position of the text.
	 * @param Y					The Y position of the text.
	 * @param Width				The width of the text box.
	 * @param Text				The text to display initially.
	 * @param TextColor			The color of the text.
	 * @param BackgroundColor	The color of the box background. Set to 0 to disable background.
	 * @param EmbeddedFont		Whether this text field uses embedded fonts.
	 */
	public FlxInputText(float X, float Y, FlxUISkin skin, String Text, int Width, int Height)
	{
		super(X, Y, skin, Text, Width, Height);
		textField = new FlxTextCustom(X, Y-6, Width, null, true);
		textField.setFormat(null, 16);
		buffer = new StringBuffer();
		_textPassword = new StringBuffer();
		FlxG.getStage().addEventListener(KeyboardEvent.KEY_TYPED, handleKeyDown);
	}

	public FlxInputText(float X, float Y, FlxUISkin skin, String Text)
	{
		this(X, Y, skin, Text, 300, 32);
	}

	public FlxInputText(float X, float Y, FlxUISkin skin)
	{
		this(X, Y, skin, null, 300, 32);
	}
	
	public FlxInputText(float X, float Y)
	{
		this(X, Y, null, null, 300, 32);
	}

	/**
	 * Draw the caret in addition to the text.
	 */
	@Override
	public void draw()
	{
		super.draw();
		textField.draw();
	}

	/**
	 * Check for mouse input every tick.
	 */
	@Override
	public void update()
	{		
		checkFocus();
		super.update();
	}
	
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
	
	@Override
	public void onChange()
	{
		setActive(true);
		if(FlxG.mobile)
			Gdx.input.setOnscreenKeyboardVisible(true);
	}

	/**
	 * Handles keypresses generated on the stage.
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
					if(buffer.length() > 0)
					{
						buffer.delete(buffer.length()-1, buffer.length());
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
						buffer.append("\n");
						_isChanged = true;
					}
				}
				// Add some text
				else
				{
					if(buffer.length() < _maxLength || _maxLength == 0)
					{
						buffer.append(filter(String.valueOf(((KeyboardEvent)e).charCode)));
						if(_passwordMode)
							_textPassword.append("*");
						_isChanged = true;
					}
				}
				if(_isChanged)
				{
					// Precalculate if it fits the boundings.
					if(1 < textField.getMaxLines())
					{
						textField.setText(buffer);
						if(textField.getMaxLines() < textField.getTotalLines())
							buffer.deleteCharAt(buffer.length()-1);
					}
					textField.setText(_passwordMode ? _textPassword : buffer);
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
		buffer = null;
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
	
	public FlxText getTextField()
	{
		return textField;
	}
	
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
	
	public void setSize(float Size)
	{
		textField.setSize(Size);
	}
		
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
	
	public void setMaxLines(int Lines)
	{
		textField.setMaxLines(Lines);
	}
	
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
	
	public void setFilterMode(String filterMode)
	{
		_filterMode = filterMode;
		textField.setText(filter(textField.getText()));
	}
	
	/**
	 * Defines what text to filter. It can be NO_FILTER, ONLY_ALPHA, ONLY_NUMERIC, ONLY_ALPHA_NUMERIC or CUSTOM_FILTER
	 * (Remember to append "FlxInputText." as a prefix to those constants)
	 * @param	newFilter		The filtering mode
	 */
	public String getFilterMode()
	{
		return _filterMode;
	}
	
	public void setText(CharSequence Text)
	{
		textField.setText(Text);
	}
	
	public void getText()
	{
		textField.getText();
	}
}

/**
 * 
 * 
 * @author Ka Wing Chin
 */
class FlxTextCustom extends FlxText
{
	/**
	 * The amount of lines allowed in the textfield.
	 */
	private int _maxLines = 1;
	private float _firstLineHeight;
	/**
	 * The bounding height for each line.
	 */
	private float _lineHeight;
	private CharSequence _saveText;

	public FlxTextCustom(float X, float Y, int Width, String Text, boolean EmbeddedFont)
	{
		super(X, Y, Width, Text, EmbeddedFont);		
	}
	
	@Override
	protected void calcFrame()
	{
		TextBounds bounds;
//		if(_maxLines > 1)
			bounds = _textField.setWrappedText(_text, 2, 3, width, _alignment);
//		else
//			bounds = _textField.setText(_text, 2, 3);
		
		// bounds.height is shorter than it should be.
		// After some trial and error, adding seven seems to make it about right in most cases.
		height = frameHeight = (int) FlxU.ceil(bounds.height + 7);
	}
	
	public void saveOldText()
	{
		_saveText = _text;
	}
	
	public CharSequence getOldText()
	{
		return _saveText;
	}
	
	@Override
	public FlxText setFormat(String Font, float Size, int Color, String Alignment, int ShadowColor, float ShadowX, float ShadowY)
	{
		super.setFormat(Font, Size, Color, Alignment, ShadowColor, ShadowX, ShadowY);
		// Save text.
		String text = getText();
		
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
	
	public void setMaxLines(int Lines)
	{
		if(_maxLines == 0)
			return;
		_maxLines = Lines;
	}
	
	public int getMaxLines()
	{
		return _maxLines;
	}
	
	public float getHeight()
	{
		return _textField.getBounds().height;
	}
	
	public int getTotalLines()
	{
		if(_maxLines == 1 || getHeight() == _firstLineHeight)
			return 1;
		else
			return (int) ((getHeight() - _firstLineHeight) / _lineHeight) + 1;
	}
}
