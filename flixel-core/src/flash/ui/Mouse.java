package flash.ui;

import org.flixel.system.gdx.GdxStage;

/**
 * The methods of the Mouse class are used to hide and show the mouse pointer,
 * or to set the pointer to a specific style. The Mouse class is a top-level class
 * whose properties and methods you can access without using a constructor.
 * The pointer is visible by default, but you can hide it and implement a custom pointer.
 * 
 * @author Thomas Weston
 */
public class Mouse
{	
	/**
	 * Hides the pointer. The pointer is visible by default.
	 */
	public static void hide()
	{
		GdxStage.hideMouse();
	}
	
	/**
	 * Displays the pointer. The pointer is visible by default.
	 */
	public static void show()
	{
		GdxStage.showMouse();
	}
}
