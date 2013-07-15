package org.flixel.ui;

import org.flixel.ui.event.IFlxUIListener;

/**
 * Scalabe button.
 * @author Ka Wing Chin
 */
public class FlxNinePatchButton extends FlxUITouchable
{
	/**
	 * Y-position to vertical align the label in the middle.
	 */
	private float _verticalCenter;
	
	/**
	 * Constructor
	 * @param X			The x-position of the component.
	 * @param Y			The y-position of the component.
	 * @param UISkin	The skin that needs to be applied.
	 * @param Label		The label along side the component.
	 * @param Width		The width of the component. Default auto.
	 * @param Height	The height of the component. Default auto.
	 * @param onClick	The function to call whenever the button is clicked.
	 */
	public FlxNinePatchButton(float X, float Y, FlxUISkin skin, String Label, int Width, int Height, IFlxUIListener onClick)
	{
		super(X, Y, skin, Label, Width, Height);
		onUp = onClick;
		this.skin.labelPosition = FlxUISkin.LABEL_NONE;
		if(Width > 0)
			label.width = Width;
		label.setAlignment("center");
	}
	
	/**
	 * Constructor
	 * @param X			The x-position of the component.
	 * @param Y			The y-position of the component.
	 * @param UISkin	The skin that needs to be applied.
	 * @param Label		The label along side the component.
	 * @param Width		The width of the component. Default auto.
	 * @param Height	The height of the component. Default auto.
	 */
	public FlxNinePatchButton(float X, float Y, FlxUISkin skin,  String Label,  int Width, int Height)
	{
		this(X, Y, skin, Label, Width, 0, null);
	}
	
	/**
	 * Constructor
	 * @param X			The x-position of the component.
	 * @param Y			The y-position of the component.
	 * @param UISkin	The skin that needs to be applied.
	 * @param Label		The label along side the component.
	 * @param Width		The width of the component. Default auto.
	 */
	public FlxNinePatchButton(float X, float Y, FlxUISkin skin, String Label, int Width)
	{
		this(X, Y, skin, Label, Width, 0, null);
	}
	
	/**
	 * Constructor
	 * @param X			The x-position of the component.
	 * @param Y			The y-position of the component.
	 * @param UISkin	The skin that needs to be applied.
	 * @param Label		The label along side the component.
	 */
	public FlxNinePatchButton(float X, float Y, FlxUISkin skin, String Label)
	{
		this(X, Y, skin, Label, 0, 0, null);
	}
	
	/**
	 * Constructor
	 * @param X			The x-position of the component.
	 * @param Y			The y-position of the component.
	 * @param UISkin	The skin that needs to be applied.
	 */
	public FlxNinePatchButton(float X, float Y, FlxUISkin skin)
	{
		this(X, Y, skin, null, 0, 0, null);
	}
	
	/**
	 * Constructor
	 * @param X			The x-position of the component.
	 * @param Y			The y-position of the component.
	 */
	public FlxNinePatchButton(float X, float Y)
	{
		this(X, Y, null, null, 0, 0, null);
	}
	
	@Override
	public void update()
	{		
		super.update();
		// Default button appearance is to simply update
		// the label appearance based on animation frame.
		if(label == null)
			return;
		
		if(_height > 0)
			label.y = _verticalCenter;
		
		if(status == skin.HIGHLIGHT_DISABLED || status == skin.DISABLED)
			label.setAlpha(0.5f);
	}
	
	@Override
	public void stretch()
	{
		super.stretch();
		if(_height > 0)
			_verticalCenter = y + (height / 2f) - (label.height / 2f);
	}
}