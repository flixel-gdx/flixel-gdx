package flash.events;

/**
 * Base class for event objects.
 * 
 * @author Ka Wing Chin
 */
public class Event 
{
	/** 
	 * The type of event.
	 */
	public String type;
	
	/**
	 * Creates an Event object to pass as a parameter to event listeners.
	 * 
	 * @param type	The type of event.
	 */
	public Event(String type)
	{
		this.type = type;
	}
}
