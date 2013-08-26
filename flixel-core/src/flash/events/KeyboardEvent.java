package flash.events;

/**
 * A copy of Flash's <code>KeyboardEvent</code>
 * 
 * @author Ka Wing Chin
 */
public class KeyboardEvent extends Event
{
	/**
	 * Defines the value of type property of a keyDown event object.
	 */
	public static final String KEY_DOWN = "keyDown";
	/**
	 * Defines the value of the type property of a keyUp event object.
	 */
	public static final String KEY_UP = "keyUp";
	/**
	 * Defines the value of the type property of a keyTyped event object.
	 */
	public static final String KEY_TYPED = "keyTyped";
	/**
	 * Contains the character code value of the key pressed or released.
	 */
	public char charCode;
	/**
	 * The key code value of the key pressed or released.
	 */
	public int keyCode;

	/**
	 * Creates an <code>Event</code> object that containts information about keyboard events.
	 * 
	 * @param type	The type of event.
	 */
	public KeyboardEvent(String type)
	{
		super(type);
	}
}

