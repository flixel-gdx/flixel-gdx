package org.flixel.ui;

import org.flixel.FlxButton;
import org.flixel.FlxG;
import org.flixel.FlxGroup;
import org.flixel.FlxSprite;
import org.flixel.event.IFlxButton;

/**
 * A gamepad which contains 4 directional buttons and 4 action buttons. It's
 * easy to set the callbacks and to customize the layout.
 * 
 * @author Ka Wing Chin
 */
public class FlxVirtualPad extends FlxGroup
{
	private String ImgButtonA = "org/flixel/data/pack:button_a";
	private String ImgButtonB = "org/flixel/data/pack:button_b";
	private String ImgButtonC = "org/flixel/data/pack:button_c";
	private String ImgButtonY = "org/flixel/data/pack:button_y";
	private String ImgButtonX = "org/flixel/data/pack:button_x";
	private String ImgButtonLeft = "org/flixel/data/pack:button_left";
	private String ImgButtonUp = "org/flixel/data/pack:button_up";
	private String ImgButtonRight = "org/flixel/data/pack:button_right";
	private String ImgButtonDown = "org/flixel/data/pack:button_down";
	private String ImgCenter = "org/flixel/data/pack:dpad_center";

	/**
	 * Don't use any directional button.
	 */
	public static final int DPAD_NONE = 0;
	/**
	 * Use the set of 4 directions or A, B, X, and Y.
	 */
	public static final int DPAD_FULL = 1;
	/**
	 * Use UP and DOWN direction buttons.
	 */
	public static final int UP_DOWN = 2;
	/**
	 * Use LEFT and RIGHT direction buttons.
	 */
	public static final int LEFT_RIGHT = 3;
	/**
	 * Use UP, LEFT and RIGHT direction buttons.
	 */
	public static final int UP_LEFT_RIGHT = 4;
	/**
	 * Don't use any action buttons.
	 */
	public static final int ACTION_NONE = 0;
	/**
	 * Use only A button.
	 */
	public static final int A = 1;
	/**
	 * Use A and B button.
	 */
	public static final int A_B = 2;
	/**
	 * Use A, B and C button.
	 */
	public static final int A_B_C = 3;
	/**
	 * Use A, B, X and Y button.
	 */
	public static final int A_B_X_Y = 4;

	/**
	 * Button A
	 */
	public FlxButton buttonA;
	/**
	 * Button B
	 */
	public FlxButton buttonB;
	/**
	 * Button C
	 */
	public FlxButton buttonC;
	/**
	 * Button Y
	 */
	public FlxButton buttonY;
	/**
	 * Button X
	 */
	public FlxButton buttonX;
	/**
	 * Button LEFT DIRECTION
	 */
	public FlxButton buttonLeft;
	/**
	 * Button UP DIRECTION
	 */
	public FlxButton buttonUp;
	/**
	 * Button RIGHT DIRECTION
	 */
	public FlxButton buttonRight;
	/**
	 * BUTTON DOWN DIRECTION
	 */
	public FlxButton buttonDown;

	/**
	 * Group of directions buttons.
	 */
	public FlxGroup dPad;
	/**
	 * Group of action buttons.
	 */
	public FlxGroup actions;

	/**
	 * Create a gamepad which contains 4 directional buttons and 4 action
	 * buttons.
	 * 
	 * @param DPad The D-Pad mode. FlxVirtualPad.FULL for example.
	 * @param Action The action buttons mode. FlxVirtualPad.A_B_C for example.
	 */
	public FlxVirtualPad(int DPad, int Action)
	{
		dPad = new FlxGroup();
		actions = new FlxGroup();

		switch(DPad)
		{
			case DPAD_FULL:
				dPad.add(add(buttonUp = createButton(35, FlxG.height - 99, 29, 36, ImgButtonUp)));
				dPad.add(add(buttonLeft = createButton(0, FlxG.height - 64, 35, 29, ImgButtonLeft)));
				dPad.add(add(buttonRight = createButton(64, FlxG.height - 64, 35, 29, ImgButtonRight)));
				dPad.add(add(buttonDown = createButton(35, FlxG.height - 39, 29, 39, ImgButtonDown)));
				dPad.add(add(createCenter(buttonLeft.x + buttonLeft.width, FlxG.height - 65, 29, 27, ImgCenter)));
				break;
			case UP_DOWN:
				dPad.add(add(buttonUp = createButton(35, FlxG.height - 101, 29, 36, ImgButtonUp)));
				dPad.add(add(buttonDown = createButton(35, FlxG.height - 39, 29, 39, ImgButtonDown)));
				dPad.add(add(createCenter(35, FlxG.height - 66, 29, 27, ImgCenter)));
				break;
			case LEFT_RIGHT:
				dPad.add(add(buttonLeft = createButton(0, FlxG.height - 44, 35, 29, ImgButtonLeft)));
				dPad.add(add(buttonRight = createButton(64, FlxG.height - 44, 35, 29, ImgButtonRight)));
				dPad.add(add(createCenter(buttonLeft.x + buttonLeft.width, FlxG.height - 45, 29, 27, ImgCenter)));
				break;
			case UP_LEFT_RIGHT:
				dPad.add(add(buttonUp = createButton(35, FlxG.height - 79, 29, 36, ImgButtonUp)));
				dPad.add(add(buttonLeft = createButton(0, FlxG.height - 44, 35, 29, ImgButtonLeft)));
				dPad.add(add(buttonRight = createButton(64, FlxG.height - 44, 35, 29, ImgButtonRight)));
				dPad.add(add(createCenter(buttonLeft.x + buttonLeft.width, FlxG.height - 45, 29, 27, ImgCenter)));
				break;
			default:
				break;
		}

		switch(Action)
		{
			case A:
				actions.add(add(buttonA = createButton(FlxG.width - 50, FlxG.height - 68, 37, 37, ImgButtonA)));
				break;
			case A_B:
				actions.add(add(buttonB = createButton(FlxG.width - 68, FlxG.height - 41, 37, 37, ImgButtonB)));
				actions.add(add(buttonA = createButton(FlxG.width - 37, FlxG.height - 72, 37, 37, ImgButtonA)));
				break;
			case A_B_C:
				actions.add(add(buttonA = createButton(FlxG.width - 99, FlxG.height - 41, 37, 37, ImgButtonA)));
				actions.add(add(buttonB = createButton(FlxG.width - 68, FlxG.height - 62, 37, 37, ImgButtonB)));
				actions.add(add(buttonC = createButton(FlxG.width - 37, FlxG.height - 83, 37, 37, ImgButtonC)));
			case A_B_X_Y:
				actions.add(add(buttonY = createButton(FlxG.width - 99, FlxG.height - 72, 37, 37, ImgButtonY)));
				actions.add(add(buttonX = createButton(FlxG.width - 68, FlxG.height - 103, 37, 37, ImgButtonX)));
				actions.add(add(buttonB = createButton(FlxG.width - 68, FlxG.height - 41, 37, 37, ImgButtonB)));
				actions.add(add(buttonA = createButton(FlxG.width - 37, FlxG.height - 72, 37, 37, ImgButtonA)));
				break;
			default:
				break;
		}
	}

	@Override
	public void destroy()
	{
		super.destroy();
		if(dPad != null)
			dPad.destroy();
		if(actions != null)
			actions.destroy();
		dPad = actions = null;
		buttonA = buttonB = buttonC = buttonY = buttonX = null;
		buttonLeft = buttonUp = buttonDown = buttonRight = null;
	}

	/**
	 * Creates a button
	 * 
	 * @param X The x-position of the button.
	 * @param Y The y-position of the button.
	 * @param Width The width of the button.
	 * @param Height The height of the button.
	 * @param Image The image of the button. It must contains 3 frames (NORMAL,
	 *        HIGHLIGHT, PRESSED).
	 * @param Callback The callback for the button.
	 * @return The button.
	 */
	public FlxButton createButton(float X, float Y, int Width, int Height, String Image, IFlxButton OnClick)
	{
		FlxButton button = new FlxButton(X, Y);
		button.loadGraphic(Image, true, false, Width, Height);
		button.setSolid(false);
		button.immovable = true;
		button.ignoreDrawDebug = true;
		button.scrollFactor.x = button.scrollFactor.y = 0;
		if(OnClick != null)
			button.onDown = OnClick;
		return button;
	}

	public FlxSprite createCenter(float X, float Y, int Width, int Height, String Image)
	{
		FlxSprite center = new FlxSprite(X, Y).loadGraphic(Image, false, false, Width, Height);
		center.setSolid(false);
		center.immovable = true;
		center.ignoreDrawDebug = true;
		center.scrollFactor.x = center.scrollFactor.y = 0;
		return center;
	}

	/**
	 * Creates a button
	 * 
	 * @param X The x-position of the button.
	 * @param Y The y-position of the button.
	 * @param Width The width of the button.
	 * @param Height The height of the button.
	 * @param Image The image of the button. It must contains 3 frames (NORMAL,
	 *        HIGHLIGHT, PRESSED).
	 * @return The button.
	 */
	public FlxButton createButton(float X, float Y, int Width, int Height, String Image)
	{
		return createButton(X, Y, Width, Height, Image, null);
	}

	/**
	 * Set <code>alpha</code> to a number between 0 and 1 to change the opacity
	 * of the gamepad.
	 * 
	 * @param Alpha
	 */
	public void setAlpha(float Alpha)
	{
		for(int i = 0; i < members.size; i++)
		{
			((FlxSprite) members.get(i)).setAlpha(Alpha);
		}
	}

	/**
	 * Set the offset position of the D-PAD.
	 * 
	 * @param X The x-offset
	 * @param Y The y-offset
	 */
	public void setDPadPosition(float X, float Y)
	{
		for(int i = 0; i < dPad.members.size; i++)
		{
			((FlxSprite) dPad.members.get(i)).x += X;
			((FlxSprite) dPad.members.get(i)).y += Y;
		}
	}

	/**
	 * Set the x-offset position of the D-PAD.
	 * 
	 * @param X The x-offset
	 */
	public void setDPadPositionX(float X)
	{
		for(int i = 0; i < dPad.members.size; i++)
			((FlxSprite) dPad.members.get(i)).x += X;
	}

	/**
	 * Set the y-offset position of the D-PAD.
	 * 
	 * @param Y The y-offset
	 */
	public void setDPadPositionY(float Y)
	{
		for(int i = 0; i < dPad.members.size; i++)
			((FlxSprite) dPad.members.get(i)).y += Y;
	}

	/**
	 * Set the offset position of the action buttons.
	 * 
	 * @param X The x-offset
	 * @param Y The y-offset
	 */
	public void setActionPosition(float X, float Y)
	{
		for(int i = 0; i < actions.members.size; i++)
		{
			((FlxSprite) actions.members.get(i)).x += X;
			((FlxSprite) actions.members.get(i)).y += Y;
		}
	}

	/**
	 * Set the x-offset position of the action buttons.
	 * 
	 * @param X The x-offset
	 */
	public void setActionPositionX(float X)
	{
		for(int i = 0; i < actions.members.size; i++)
			((FlxSprite) actions.members.get(i)).x += X;
	}

	/**
	 * Set the y-offset position of the action buttons.
	 * 
	 * @param Y The y-offset
	 */
	public void setActionPositionY(float Y)
	{
		for(int i = 0; i < actions.members.size; i++)
			((FlxSprite) actions.members.get(i)).y += Y;
	}
}
