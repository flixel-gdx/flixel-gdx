package org.flixel.ui;

import org.flixel.FlxG;
import org.flixel.ui.event.IFlxDialogBox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;

/**
 * Skinnable DialogBox. When clicked a dialog will be opened. For desktop it
 * will be Swing dialog. For Android it will be standard Android dialog.
 * 
 * @author Ka Wing Chin
 */
public class FlxDialogBox extends FlxInputText implements TextInputListener
{
	/**
	 * The title of the dialog.
	 */
	private String _title;
	/**
	 * Callback will be fired on input or on cancel.
	 */
	public IFlxDialogBox callback;

	/**
	 * Creates new <code>FlxDialogBox</code> object.
	 * 
	 * @param X The x-position of the component.
	 * @param Y The y-position of the component.
	 * @param UISkin The skin that needs to be applied.
	 * @param Label The label along side the component.
	 * @param Width The width of the component. Default 0, unlimited width.
	 * @param Height The height of the component. Default 0, unlimited height.
	 * @param Title The title of the dialog box.
	 */
	public FlxDialogBox(float X, float Y, FlxUISkin skin, String Label, int Width, int Height, String Title)
	{
		super(X, Y, skin, Label, Width, Height);
		_title = Title;
		// FlxG.getStage().removeEventListener(KeyboardEvent.KEY_TYPED,
		// handleKeyDown);
	}

	/**
	 * Creates new <code>FlxDialogBox</code> object.
	 * 
	 * @param X The x-position of the component.
	 * @param Y The y-position of the component.
	 * @param UISkin The skin that needs to be applied.
	 * @param Label The label along side the component.
	 * @param Width The width of the component. Default 0, unlimited width.
	 * @param Height The height of the component. Default 0, unlimited height.
	 */
	public FlxDialogBox(float X, float Y, FlxUISkin skin, String Label, int Width, int Height)
	{
		this(X, Y, skin, Label, Width, Height, null);
	}

	/**
	 * Creates new <code>FlxDialogBox</code> object.
	 * 
	 * @param X The x-position of the component.
	 * @param Y The y-position of the component.
	 * @param UISkin The skin that needs to be applied.
	 * @param Label The label along side the component.
	 * @param Width The width of the component. Default 0, unlimited width.
	 */
	public FlxDialogBox(float X, float Y, FlxUISkin skin, String Label, int Width)
	{
		this(X, Y, skin, Label, Width, 0, null);
	}

	/**
	 * Creates new <code>FlxDialogBox</code> object.
	 * 
	 * @param X The x-position of the component.
	 * @param Y The y-position of the component.
	 * @param UISkin The skin that needs to be applied.
	 * @param Label The label along side the component.
	 */
	public FlxDialogBox(float X, float Y, FlxUISkin skin, String Label)
	{
		this(X, Y, skin, Label, 0, 0, null);
	}

	/**
	 * Creates new <code>FlxDialogBox</code> object.
	 * 
	 * @param X The x-position of the component.
	 * @param Y The y-position of the component.
	 * @param UISkin The skin that needs to be applied.
	 */
	public FlxDialogBox(float X, float Y, FlxUISkin skin)
	{
		this(X, Y, skin, null, 0, 0, null);
	}

	/**
	 * Creates new <code>FlxDialogBox</code> object.
	 * 
	 * @param X The x-position of the component.
	 * @param Y The y-position of the component.
	 */
	public FlxDialogBox(float X, float Y)
	{
		this(X, Y, null, null, 0, 0, null);
	}

	@Override
	public void destroy()
	{
		super.destroy();
		callback = null;
	}

	/**
	 * Will be called when text got applied from the dialog box into the
	 * textfield.
	 */
	@Override
	public void input(String text)
	{
		if(text.length() > getMaxLength() && getMaxLength() != 0)
			text = text.substring(0, getMaxLength());
		textfield.setText(filter(text));
		if(callback != null)
			callback.onInput();
		setActive(false);
	}

	/**
	 * Will be called when the dialog box got canceled.
	 */
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
			Gdx.input.getTextInput(this, _title, textfield.getText(), "Place holder");
		}
	}

	@Override
	protected void checkFocus()
	{
		// Keep this empty
	}
}
