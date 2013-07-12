package org.flixel.ui;

import org.flixel.ui.event.IFlxUIListener;

/**
 *
 * @author Ka Wing Chin
 */
public class FlxNinePatchButton extends FlxUITouchable
{
	public FlxNinePatchButton(float X, float Y, FlxUISkin skin, String Label, IFlxUIListener onClick, int Width, int Height)
	{
		super(X, Y, skin, Label, Width, Height);
		onUp = onClick;
		this.skin.labelPosition = FlxUISkin.LABEL_NONE;
		label.setAlignment("center");
	}
	
	public FlxNinePatchButton(float X, float Y, FlxUISkin skin,  String Label, IFlxUIListener onClick, int Width)
	{
		this(X, Y, skin, Label, onClick, Width, 0);
	}
	
	public FlxNinePatchButton(float X, float Y, FlxUISkin skin, String Label, IFlxUIListener onClick)
	{
		this(X, Y, skin, Label, onClick, 0, 0);
	}
	
	public FlxNinePatchButton(float X, float Y, FlxUISkin skin, String Label)
	{
		this(X, Y, skin, Label, null, 0, 0);
	}
	
	public FlxNinePatchButton(float X, float Y, FlxUISkin skin)
	{
		this(X, Y, skin, null, null, 0, 0);
	}
	
	public FlxNinePatchButton(float X, float Y)
	{
		this(X, Y, null, null, null, 0, 0);
	}
	
	@Override
	public void update()
	{		
		super.update();
		// Default button appearance is to simply update
		// the label appearance based on animation frame.
		if(label == null)
			return;
		
		if(status == skin.HIGHLIGHT_DISABLED || status == skin.DISABLED)
			label.setAlpha(0.5f);
	}
}