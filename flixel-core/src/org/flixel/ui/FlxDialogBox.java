package org.flixel.ui;

import org.flixel.FlxG;
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
	public IFlxDialogBox callback;

	public FlxDialogBox(float X, float Y, FlxUISkin skin, int Width, int Height, String Text, String Title)
	{
		super(X, Y, skin, Text, Width, Height);
		_title = Title;
		FlxG.getStage().removeEventListener(KeyboardEvent.KEY_TYPED, handleKeyDown);
	}

	public FlxDialogBox(float X, float Y, FlxUISkin skin, int Width, int Height, String Text)
	{
		this(X, Y, skin, Width, 32, Text, null);
	}

	public FlxDialogBox(float X, float Y, FlxUISkin skin, int Width, int Height)
	{
		this(X, Y, skin, Width, 32, null, null);
	}

	public FlxDialogBox(float X, float Y, FlxUISkin skin)
	{
		this(X, Y, skin, 328, 32, null, null);
	}

	public FlxDialogBox(float X, float Y)
	{
		this(X, Y, null, 328, 32, null, null);
	}

	@Override
	public void destroy()
	{
		super.destroy();
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
		if(text.length() > getMaxLength() && getMaxLength() != 0)
			text = text.substring(0, getMaxLength());
		textField.setText(filter(text));
		if(callback != null)
			callback.onInput();
		setActive(false);
	}

	@Override
	public void canceled()
	{
		if(callback != null)
			callback.onCancel();
		setActive(false);
	}

	@Override
	public void onChange()
	{
		if(overlapsPoint(_point.make(FlxG.mouse.x, FlxG.mouse.y)))
		{
			setActive(true);
			Gdx.input.getTextInput(this, _title, textField.getText());
		}
	}	

	@Override
	protected void checkFocus()
	{
		// Keep this empty
	}
}
