package org.flixel.system.replay;

/**
 * A helper class for the frame records, part of the replay/demo/recording system.
 * 
 * @author Thomas Weston
 */
public class MouseRecord
{
	/**
	 * The main X value of the mouse in screen space.
	 */
	public int x;
	/**
	 * The main Y value of the mouse in screen space.
	 */
	public int y;
	/**
	 * The state of the left mouse button.
	 */
	public int button;
	/**
	 * The state of the mouse wheel.
	 */
	public int wheel;

	/**
	 * Instantiate a new mouse input record.
	 * 
	 * @param	X			The main X value of the mouse in screen space.
	 * @param	Y			The main Y value of the mouse in screen space.
	 * @param	Button		The state of the left mouse button.
	 * @param	Wheel		The state of the mouse wheel.
	 */
	public MouseRecord(int X,int Y,int Button,int Wheel)
	{
		x = X;
		y = Y;
		button = Button;
		wheel = Wheel;
	}
}
