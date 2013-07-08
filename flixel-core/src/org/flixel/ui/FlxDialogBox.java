package org.flixel.ui;

import org.flixel.FlxG;
import org.flixel.FlxText;
import org.flixel.ui.event.IFlxDialogBox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;

import flash.events.KeyboardEvent;

/**
 *
 * @author Ka Wing Chin
 */
public class FlxDialogBox extends FlxInputText implements TextInputListener
{
	
	
	private String _title;	
	public FlxText textField;
	public IFlxDialogBox callback;

	public FlxDialogBox(float X, float Y, FlxUISkin skin, int Width, String Text, String Title)
	{
		super(X, Y, skin, Text, Width);
		textField = new FlxText(X+4, Y-6, Width);
		textField.setFormat(null, 16);
		_title = Title;
		FlxG.getStage().removeEventListener(KeyboardEvent.KEY_TYPED, handleKeyDown);
	}
	
	public FlxDialogBox(float X, float Y, FlxUISkin skin, int Width, String Text)
	{
		this(X, Y, skin, Width, Text, null);
	}
	
	public FlxDialogBox(float X, float Y, FlxUISkin skin, int Width)
	{
		this(X, Y, skin, Width, null, null);
	}
	
	public FlxDialogBox(float X, float Y, FlxUISkin skin)
	{
		this(X, Y, skin, 328, null, null);
	}

	public FlxDialogBox(float X, float Y)
	{
		this(X, Y, null, 328, null, null);
	}
	
	@Override
	public void destroy()
	{
		super.destroy();
		textField.destroy();
		textField = null;
		callback = null;
	}

	@Override
	public void draw()
	{
		super.draw();
		textField.draw();
	}

	@Override
	public void input(String text)
	{
		setActive(false);
		if(text.length() > getMaxLength())
			text = text.substring(0, getMaxLength());
		textField.setText(filter(text));
		if(callback != null)
			callback.onInput();
	}

	@Override
	public void canceled()
	{
		setActive(false);
		if(callback != null)
			callback.onCancel();
	}
	
	@Override
	protected void checkFocus()
	{
		if(FlxG.mouse.justPressed())
		{
			if(overlapsPoint(_point.make(FlxG.mouse.x, FlxG.mouse.y)))
			{
				setActive(true);
				Gdx.input.getTextInput(this, _title, textField.getText());				
			}
		}
	}
	
	@Override
	public void onChange()
	{
		// Keep this empty
	}
}

