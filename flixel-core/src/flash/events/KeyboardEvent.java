package flash.events;

/**
 * A KeyboardEvent object id dispatched in response to user input through a keyboard.
 * 
 * @author Ka Wing Chin
 * @author Thomas Weston
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
	 * Creates an Event object that contains information about keyboard events.
	 * 
	 * @param	type			The type of event. Possible values are: <code>KeyboardEvent.KEY_DOWN</code> and <code>KeyboardEvent.KEY_UP</code>
	 * @param	bubbles			Determines whether the Event object participates in the bubbling stage of the event flow.
	 * @param	cancelable		Determines whether the Event object can be cancelled.
	 * @param	charCodeValue	The character code value of the key pressed or released. The character code values returned are English keyboard values. For example, if you press Shift+3, the <code>Keyboard.charCode()</code> property returns # on a Japanese or a German keyboard, just as it does on an English keyboard.
	 * @param	keyCodeValue	The key code value of the key pressed or released.
	 */
	public KeyboardEvent(String type, boolean bubbles, boolean cancelable, char charCodeValue, int keyCodeValue)
	{
		super(type, bubbles, cancelable);
		charCode = charCodeValue;
		keyCode = keyCodeValue;
	}

	/**
	 * Creates an Event object that contains information about keyboard events.
	 * 
	 * @param	type			The type of event. Possible values are: <code>KeyboardEvent.KEY_DOWN</code> and <code>KeyboardEvent.KEY_UP</code>.
	 * @param	bubbles			Determines whether the Event object participates in the bubbling stage of the event flow.
	 * @param	cancelable		Determines whether the Event object can be cancelled.
	 * @param	charCodeValue	The character code value of the key pressed or released. The character code values returned are English keyboard values. For example, if you press Shift+3, the <code>Keyboard.charCode()</code> property returns # on a Japanese or a German keyboard, just as it does on an English keyboard.
	 */
	public KeyboardEvent(String type, boolean bubbles, boolean cancelable, char charCodeValue)
	{
		this(type, bubbles, cancelable, charCodeValue, 0);
	}

	/**
	 * Creates an Event object that contains information about keyboard events.
	 * 
	 * @param	type			The type of event. Possible values are: <code>KeyboardEvent.KEY_DOWN</code> and <code>KeyboardEvent.KEY_UP</code>.
	 * @param	bubbles			Determines whether the Event object participates in the bubbling stage of the event flow.
	 * @param	cancelable		Determines whether the Event object can be cancelled.
	 */
	public KeyboardEvent(String type, boolean bubbles, boolean cancelable)
	{
		this(type, bubbles, cancelable, ' ', 0);
	}

	/**
	 * Creates an Event object that contains information about keyboard events.
	 * 
	 * @param	type			The type of event. Possible values are <code>KeyboardEvent.KEY_DOWN</code> and <code>KeyboardEvent.KEY_UP</code>.
	 * @param	bubbles			Determines whether the Event object participates in the bubbling stage of the event flow.
	 */
	public KeyboardEvent(String type, boolean bubbles)
	{
		this(type, bubbles, false, ' ', 0);
	}

	/**
	 * Creates an Event object that contains information about keyboard events.
	 * 
	 * @param	type			The type of event. Possible values are: <code>KeyboardEvent.KEY_DOWN</code> and <code>KeyboardEvent.KEY_UP</code>.
	 */
	public KeyboardEvent(String type)
	{
		this(type, true, false, ' ', 0);
	}
}
