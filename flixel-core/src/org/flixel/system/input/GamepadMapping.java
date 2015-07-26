package org.flixel.system.input;

import com.badlogic.gdx.Input.Keys;

/**
 * A generic controller mapping. By default it uses indices from <code>Keys</code>.
 * 
 * @author Ka Wing Chin
 */
public class GamepadMapping
{
	public static final int UP = 200;
	public static final int UP_RIGHT = 199;
	public static final int UP_LEFT = 198;
	public static final int RIGHT = 197;
	public static final int DOWN = 196;
	public static final int DOWN_RIGHT = 195;
	public static final int DOWN_LEFT = 194;
	public static final int LEFT = 193;
	public static final int CENTER = 192;

	public int BUTTON_DPAD_UP;
	public int BUTTON_DPAD_RIGHT;
	public int BUTTON_DPAD_DOWN;
	public int BUTTON_DPAD_LEFT;
	public int BUTTON_CIRCLE = Keys.BUTTON_CIRCLE;
	public int BUTTON_A = Keys.BUTTON_A;
	public int BUTTON_B = Keys.BUTTON_B;
	public int BUTTON_C = Keys.BUTTON_C;
	public int BUTTON_Y = Keys.BUTTON_Y;
	public int BUTTON_X = Keys.BUTTON_X;
	public int BUTTON_Z = Keys.BUTTON_Z;
	public int BUTTON_L1 = Keys.BUTTON_L1;
	public int BUTTON_R1 = Keys.BUTTON_R1;
	public int BUTTON_L2 = Keys.BUTTON_L2;
	public int BUTTON_R2 = Keys.BUTTON_R2;
	public int BUTTON_L3 = Keys.BUTTON_THUMBL;
	public int BUTTON_R3 = Keys.BUTTON_THUMBR;
	public int BUTTON_START = Keys.BUTTON_START;
	public int BUTTON_SELECT = Keys.BUTTON_SELECT;
	public int BUTTON_MODE = Keys.BUTTON_MODE;
	public int AXIS_LEFT_X = -1;
	public int AXIS_LEFT_Y = -1;
	public int AXIS_RIGHT_X = -1;
	public int AXIS_RIGHT_Y = -1;
	public String ID;
	public String[] IDs;

	/**
	 * Creates a new <code>GamepadMapping</code> object.
	 * 
	 * @param	ID		The ID that is equal to the <code>Controller.getName()</code>.
	 */
	public GamepadMapping(String ID)
	{
		this.ID = ID;
	}

	/**
	 * Creates a new <code>GamepadMapping</code> object. Some same controllers got different
	 * 
	 * @param	IDs		Multiple IDs that are equal to the
	 *        <code>Controller.getName()</code>.
	 */
	public GamepadMapping(String[] IDs)
	{
		this.IDs = IDs;
	}

	public void destroy()
	{
		IDs = null;
	}
}
