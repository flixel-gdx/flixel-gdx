package flash.events;

public class KeyboardEvent extends Event
{
	public static final String KEY_DOWN = "keyDown";
	public static final String KEY_UP = "keyUp";
	public static final String KEY_TYPED = "keyTyped";
	public char charCode;
	public int keyCode;

	public KeyboardEvent(String type)
	{
		super(type);
	}
}

