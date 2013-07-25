package flash.events;

/**
 * Base class for event objects.
 * 
 * @author Ka Wing Chin
 */
public class Event 
{
	/**
	 * The Event.SOUND_COMPLETE constant defines the value of the type property of a soundComplete event object.
	 */
	public static final String SOUND_COMPLETE = "soundComplete";
	
	/** 
	 * The type of event.
	 */
	public String type;
	
	/**
	 * Creates an <code>Event</code> object to pass as a parameter to event listeners.
	 * 
	 * @param type	The type of event.
	 */
	public Event(String type)
	{
		this.type = type;
	}
}
