package org.flixel.ui;

import org.flixel.FlxG;
import org.flixel.ui.event.IFlxInputText;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import flash.events.Event;
import flash.events.IEventListener;
import flash.events.KeyboardEvent;

/*@formatter:off*/
/**
 * Copyright (c) 2009 Martín Sebastián Wain
 * License: Creative Commons Attribution 3.0 United States
 * @link http://creativecommons.org/licenses/by/3.0/us/ 
 * 
 * Input textfield extension for Flixel.
 * Heavily modified to get it working on flixel-gdx.
 * Supports multiline and filters.
 * @link http://forums.flixel.org/index.php/topic,272.0.html
 * 
 * @author Ka Wing Chin
 * @author Gama11
 * @author Mr_Walrus
 * @author nitram_cero (Martin Sebastián Wain)
 *//*@formatter:on*/
public class FlxInputText extends FlxUITouchable
{
	private final String ImgTextAreaTopLeft = "org/flixel/data/pack:ninepatch_textarea_topleft";
	private final String ImgTextAreaTopCenter = "org/flixel/data/pack:ninepatch_textarea_topcenter";
	private final String ImgTextAreaTopRight = "org/flixel/data/pack:ninepatch_textarea_topright";
	private final String ImgTextAreaMiddleLeft = "org/flixel/data/pack:ninepatch_textarea_middleleft";
	private final String ImgTextAreaMiddleRight = "org/flixel/data/pack:ninepatch_textarea_middleright";
	private final String ImgTextAreaBottomLeft = "org/flixel/data/pack:ninepatch_textarea_bottomleft";
	private final String ImgTextAreaBottomCenter = "org/flixel/data/pack:ninepatch_textarea_bottomcenter";
	private final String ImgTextAreaBottomRight = "org/flixel/data/pack:ninepatch_textarea_bottomright";

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
	public FlxTextExt textfield;
	/**
	 * A function called when the enter key is pressed on this text box.
	 */
	public IFlxInputText callback;
	/**
	 * The max amount of characters the textfield can contain.
	 */
	private int _maxLength = 0;
	/**
	 * Internal, how many lines are allowed.
	 */
	private int _maxLines = 1;
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
	protected StringBuilder textBuffer;
	/**
	 * Buffer password.
	 */
	private StringBuilder _passwordBuffer;
	/**
	 * Tracks whether the text got changed or not.
	 */
	private boolean _isChanged;

	/**
	 * Create a new <code>FlxInputText</code> object.
	 * 
	 * @param X The x-position of the component.
	 * @param Y The y-position of the component.
	 * @param UISkin The skin that needs to be applied.
	 * @param Label The label along side the component.
	 * @param Width The width of the component. Default 0, unlimited width.
	 * @param Height The height of the component. Default 0, unlimited height.
	 */
	public FlxInputText(float X, float Y, FlxUISkin skin, String Label, int Width, int Height)
	{
		super(X, Y, skin, Label, Width, Height);
		textfield = new FlxTextExt(X, Y - 6, Width, Height, null, true);
		textfield.offset.y = (skin == null) ? -12 : 0;
		textfield.setFormat(null, 16);
		textBuffer = new StringBuilder();
		_passwordBuffer = new StringBuilder();
		FlxG.getStage().addEventListener(KeyboardEvent.KEY_TYPED, handleKeyDown);
	}

	/**
	 * Create a new <code>FlxInputText</code> object.
	 * 
	 * @param X The x-position of the component.
	 * @param Y The y-position of the component.
	 * @param UISkin The skin that needs to be applied.
	 * @param Label The label along side the component
	 * @param Width The width of the component. Default 0, unlimited width.
	 */
	public FlxInputText(float X, float Y, FlxUISkin skin, String Label, int Width)
	{
		this(X, Y, skin, Label, Width, 0);
	}

	/**
	 * Create a new <code>FlxInputText</code> object.
	 * 
	 * @param X The x-position of the component.
	 * @param Y The y-position of the component.
	 * @param UISkin The skin that needs to be applied.
	 * @param Label The label along side the component
	 */
	public FlxInputText(float X, float Y, FlxUISkin skin, String Label)
	{
		this(X, Y, skin, Label, 0, 0);
	}

	/**
	 * Create a new <code>FlxInputText</code> object.
	 * 
	 * @param X The x-position of the component.
	 * @param Y The y-position of the component.
	 * @param UISkin The skin that needs to be applied.
	 */
	public FlxInputText(float X, float Y, FlxUISkin skin)
	{
		this(X, Y, skin, null, 0, 0);
	}

	/**
	 * Create a new <code>FlxInputText</code> object.
	 * 
	 * @param X The x-position of the component.
	 * @param Y The y-position of the component.
	 */
	public FlxInputText(float X, float Y)
	{
		this(X, Y, null, null, 0, 0);
	}

	/**
	 * Create a new <code>FlxInputText</code> object.
	 * 
	 * @param X The x-position of the component.
	 */
	public FlxInputText(float X)
	{
		this(X, 0, null, null, 0, 0);
	}

	/**
	 * Create a new <code>FlxInputText</code> object.
	 */
	public FlxInputText()
	{
		this(0, 0, null, null, 0, 0);
	}

	@Override
	public void setDefaultSkin()
	{
		skin = new FlxUISkin();
		skin.DISABLED = 1;
		skin.HIGHLIGHT = 2;
		skin.PRESSED = 2;
		skin.ACTIVE_NORMAL = 2;
		skin.ACTIVE_HIGHTLIGHT = 2;
		skin.ACTIVE_PRESSED = 2;
		skin.labelPosition = FlxUISkin.LABEL_TOP;
		skin.labelOffset.y = -3;
		skin.labelWidth = 200;
		skin.setFormat(null, 8, 0x0099CC);

		skin.setNinePatch(FlxNinePatch.TOP_LEFT, ImgTextAreaTopLeft, 4, 2);
		skin.setNinePatch(FlxNinePatch.TOP_CENTER, ImgTextAreaTopCenter, 1, 2);
		skin.setNinePatch(FlxNinePatch.TOP_RIGHT, ImgTextAreaTopRight, 4, 2);
		skin.setNinePatch(FlxNinePatch.MIDDLE_LEFT, ImgTextAreaMiddleLeft, 4, 1);
		skin.setNinePatch(FlxNinePatch.MIDDLE_RIGHT, ImgTextAreaMiddleRight, 4, 1);
		skin.setNinePatch(FlxNinePatch.BOTTOM_LEFT, ImgTextAreaBottomLeft, 4, 2);
		skin.setNinePatch(FlxNinePatch.BOTTOM_CENTER, ImgTextAreaBottomCenter, 1, 2);
		skin.setNinePatch(FlxNinePatch.BOTTOM_RIGHT, ImgTextAreaBottomRight, 4, 2);
	}

	@Override
	public void draw()
	{
		super.draw();
		textfield.draw();
	}

	@Override
	public void update()
	{
		checkFocus();
		super.update();
	}

	/**
	 * Check whether a click has been occurred outside the textfield. Hides the
	 * keyboard on mobile.
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
	 * This will automatically called when textfield got clicked. Allows to type
	 * in the textfield. Shows the keyboard on mobile.
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
	 * @param e The triggering keyboard event.
	 */
	IEventListener handleKeyDown = new IEventListener()
	{
		@Override
		public void onEvent(Event e)
		{
			if(activated)
			{
				int key = ((KeyboardEvent) e).keyCode;
				if(key == Keys.SHIFT_LEFT || key == Keys.SHIFT_RIGHT || key == Keys.CONTROL_LEFT || key == Keys.CONTROL_RIGHT || key == Keys.ALT_LEFT || key == Keys.ALT_RIGHT)
				{
					return;
				}
				// Backspace
				if(key == Keys.BACKSPACE)
				{
					if(textBuffer.length() > 0)
					{
						textBuffer.delete(textBuffer.length() - 1, textBuffer.length());
						if(_passwordMode)
							_passwordBuffer.delete(_passwordBuffer.length() - 1, _passwordBuffer.length());
						_isChanged = true;
					}
				}
				// Enter
				else if(key == Keys.ENTER)
				{
					if(callback != null)
						callback.onEnter(textfield.getText());
					if(1 < _maxLines && _currentLineCounter < _maxLines)
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
						textBuffer.append(filter(String.valueOf(((KeyboardEvent) e).charCode)));
						if(_passwordMode)
							_passwordBuffer.append("*");
						_isChanged = true;
					}
				}
				if(_isChanged)
				{
					// Calculate whether it fits the bounding or not.
					textfield.setText(textBuffer.toString());
					if(_maxLines < textfield.getTotalLines())
						textBuffer.deleteCharAt(textBuffer.length() - 1);
					textfield.setText(_passwordMode ? _passwordBuffer.toString() : textBuffer.toString());
					_currentLineCounter = textfield.getTotalLines() == -1 ? 1 : textfield.getTotalLines();
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
		handleKeyDown = null;
		textfield.destroy();
		textfield = null;
		callback = null;
		textBuffer = null;
		_passwordBuffer = null;
	}

	/**
	 * Checks an input string against the current filter and returns a filtered
	 * string.
	 * 
	 * @param text Unfiltered text
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
	 * Set the maximum lines the textfield is allowed.
	 * 
	 * @param Lines
	 */
	public void setMaxLines(int Lines)
	{
		_maxLines = Lines;
	}

	/**
	 * Get the current case that got forced.
	 * 
	 * @return
	 */
	public int getForceCase()
	{
		return _forceCase;
	}

	/**
	 * Enforce upper-case or lower-case
	 * 
	 * @param Case The Case that's being enforced. Either ALL_CASES, UPPER_CASE
	 *        or LOWER_CASE.
	 */
	public void setForceCase(int Case)
	{
		_forceCase = Case;
		textfield.setText(filter(textfield.getText()));
	}

	/**
	 * Set the font size.
	 * 
	 * @param Size
	 */
	public void setSize(float Size)
	{
		textfield.setSize(Size);
	}

	/**
	 * The maximum chars the textfield is allowed.
	 * 
	 * @param Length
	 */
	public void setMaxLength(int Length)
	{
		_maxLength = Length;
		if(textfield.getText().length() > _maxLength)
			textfield.setText(textfield.getText().substring(0, _maxLength));
	}

	/**
	 * Set the maximum length for the field (e.g. "3" for Arcade type hi-score
	 * initials)
	 * 
	 * @param Length The maximum length. 0 means unlimited.
	 */
	public int getMaxLength()
	{
		return _maxLength;
	}

	/**
	 * Make the textfield go in to password mode and show * for every char.
	 * 
	 * @param enable
	 */
	public void setPasswordMode(boolean enable)
	{
		_passwordMode = enable;
	}

	/**
	 * Whether or not the textfield is a password textfield
	 * 
	 * @param enable Whether to en- or disable password mode
	 */
	public boolean getPasswordMode()
	{
		return _passwordMode;
	}

	/**
	 * Set the filter mode that the text needs to be filtered. NO_FILTER,
	 * ONLY_ALPHA, ONLY_NUMERIC, ONLY_ALPHA_NUMERIC, ONLY_ALPHA_NUMERIC_SPACE or
	 * CUSTOM_FILTER
	 * 
	 * @param filterMode
	 */
	public void setFilterMode(String filterMode)
	{
		_filterMode = filterMode;
		textfield.setText(filter(textfield.getText()));
	}

	/**
	 * Defines what text to filter. It can be NO_FILTER, ONLY_ALPHA,
	 * ONLY_NUMERIC, ONLY_ALPHA_NUMERIC, ONLY_ALPHA_NUMERIC_SPACE or
	 * CUSTOM_FILTER (Remember to append "FlxInputText." as a prefix to those
	 * constants)
	 */
	public String getFilterMode()
	{
		return _filterMode;
	}

	/**
	 * Set text in the textfield.
	 * 
	 * @param Text
	 */
	public void setText(CharSequence Text)
	{
		textfield.setText(Text.toString());
	}

	/**
	 * Get the text from the textfield.
	 */
	public CharSequence getText()
	{
		return textfield.getText();
	}
}
