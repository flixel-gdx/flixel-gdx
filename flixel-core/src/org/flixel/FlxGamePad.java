package org.flixel;

import org.flixel.event.AFlxButton;

/**
 * A gamepad which contains 4 directional buttons and 4 action buttons.
 * It's easy to set the callbacks and to customize the layout.
 * 
 * @author Ka Wing Chin
 */
public class FlxGamePad extends FlxGroup
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
	
	// Button A
	public FlxButton buttonA;
	// Button B
	public FlxButton buttonB;
	// Button C
	public FlxButton buttonC;
	// Button Y
	public FlxButton buttonY;
	// Button X
	public FlxButton buttonX;
	// Button LEFT DIRECTION
	public FlxButton buttonLeft;
	// Button UP DIRECTION
	public FlxButton buttonUp;
	// Button RIGHT DIRECTION
	public FlxButton buttonRight;
	// BUTTON DOWN DIRECTION
	public FlxButton buttonDown;
	
	// Don't use any button.
	public static final int NONE = 0;
	// Use the set of 4 directions or A, B, X, and Y.
	public static final int FULL = 1;
	// Use UP and DOWN direction buttons.
	public static final int UP_DOWN = 2;
	// Use LEFT and RIGHT direction buttons.
	public static final int LEFT_RIGHT = 3;
	// Use UP, LEFT and RIGHT direction buttons.
	public static final int UP_LEFT_RIGHT = 4;
	// Use only A button. 
	public static final int A = 5;
	// Use A and B button.
	public static final int A_B = 6;
	// Use A, B and C button.
	public static final int A_B_C = 7;
	
	// Group of directions buttons.
	public FlxGroup dPad;
	// Group of action buttons.
	public FlxGroup actions;
	
	/**
	 * Constructor
	 * @param DPad		The D-Pad mode. FlxGamePad.FULL for example.
	 * @param Action	The action buttons mode. FlxGamePad.A_B_C for example.
	 */
	public FlxGamePad(int DPad, int Action)
	{	
		dPad = new FlxGroup();
		actions = new FlxGroup();
		
		switch(DPad)
		{
			case FULL:
				dPad.add(add(buttonUp = createButton(35, FlxG.height-116, 44, 45, ImgButtonUp)));		
				dPad.add(add(buttonLeft = createButton(0, FlxG.height-81, 44, 45, ImgButtonLeft)));
				dPad.add(add(buttonRight = createButton(69, FlxG.height-81, 44, 45, ImgButtonRight)));	
				dPad.add(add(buttonDown = createButton(35, FlxG.height-45, 44, 45, ImgButtonDown)));
				break;
			case UP_DOWN:
				dPad.add(add(buttonUp = createButton(0, FlxG.height-85, 44, 45, ImgButtonUp)));
				dPad.add(add(buttonDown = createButton(0, FlxG.height-45, 44, 45, ImgButtonDown)));
				break;
			case LEFT_RIGHT:
				dPad.add(add(buttonLeft = createButton(0, FlxG.height-45, 44, 45, ImgButtonLeft)));
				dPad.add(add(buttonRight = createButton(42, FlxG.height-45, 44, 45, ImgButtonRight)));
				break;
			case UP_LEFT_RIGHT:
				dPad.add(add(buttonUp = createButton(35, FlxG.height-81, 44, 45, ImgButtonUp)));
				dPad.add(add(buttonLeft = createButton(0, FlxG.height-45, 44, 45, ImgButtonLeft)));
				dPad.add(add(buttonRight = createButton(69, FlxG.height-45, 44, 45, ImgButtonRight)));
				break;
			default:
				break;
		}
		
		switch(Action)
		{
			case FULL:
				actions.add(add(buttonY = createButton(FlxG.width-86, FlxG.height-85, 44, 45, ImgButtonY)));		
				actions.add(add(buttonX = createButton(FlxG.width-44, FlxG.height-85, 44, 45, ImgButtonX)));		
				actions.add(add(buttonB = createButton(FlxG.width-86, FlxG.height-45, 44, 45, ImgButtonB)));		
				actions.add(add(buttonA = createButton(FlxG.width-44, FlxG.height-45, 44, 45, ImgButtonA)));
				break;
			case A:
				actions.add(add(buttonA = createButton(FlxG.width-44, FlxG.height-45, 44, 45, ImgButtonA)));
				break;
			case A_B:
				actions.add(add(buttonA = createButton(FlxG.width-44, FlxG.height-45, 44, 45, ImgButtonA)));
				actions.add(add(buttonB = createButton(FlxG.width-86, FlxG.height-45, 44, 45, ImgButtonB)));		
				break;
			case A_B_C:
				actions.add(add(buttonA = createButton(FlxG.width-128, FlxG.height-45, 44, 45, ImgButtonA)));				
				actions.add(add(buttonB = createButton(FlxG.width-86, FlxG.height-45, 44, 45, ImgButtonB)));		
				actions.add(add(buttonC = createButton(FlxG.width-44, FlxG.height-45, 44, 45, ImgButtonC)));
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
	 * @param X			The x-position of the button.
	 * @param Y			The y-position of the button.
	 * @param Width		The width of the button.
	 * @param Height	The height of the button.
	 * @param Image		The image of the button. It must contains 3 frames (NORMAL, HIGHLIGHT, PRESSED).
	 * @param Callback	The callback for the button.
	 * @return			The button.
	 */
	public FlxButton createButton(float X, float Y, int Width, int Height, String Image, AFlxButton OnClick)
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
	
	/**
	 * Creates a button
	 * @param X			The x-position of the button.
	 * @param Y			The y-position of the button.
	 * @param Width		The width of the button.
	 * @param Height	The height of the button.
	 * @param Image		The image of the button. It must contains 3 frames (NORMAL, HIGHLIGHT, PRESSED).	 
	 * @return			The button.
	 */
	public FlxButton createButton(float X, float Y, int Width, int Height, String Image)
	{
		return createButton(X, Y, Width, Height, Image, null);
	}
	
		
	/**
	 * Set <code>alpha</code> to a number between 0 and 1 to change the opacity of the gamepad.
	 * @param Alpha
	 */
	public void setAlpha(float Alpha)
	{
		for(int i = 0; i < members.size; i++)
		{
			((FlxSprite) members.get(i)).setAlpha(Alpha);
		}
	}
}
