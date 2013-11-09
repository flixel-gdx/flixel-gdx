package flash.events;

/**
 * Base class for event objects.
 * 
 * @author Ka Wing Chin
 */
public class Event 
{
	/**
	 * The <code>ACTIVATE</code> constant defines the value of the type property of an activate event object.
	 */
	public static final String ACTIVATE = "activate";
	/**
	 * The <code>Event.ADDED_TO_STAGE</code> constant defines the value of the type property of an addedToStage event object.
	 */
	public static final String ADDED_TO_STAGE = "addedToStage";
	/**
	 * The <code>Event.DEACTIVATE</code> constant defines the value of the type property of a deactivate event object.
	 */
	public static final String DEACTIVATE = "deactivate";
	/**
	 * The <code>Event.ENTER_FRAME</code> constant defines the value of the type property of an enterFrame event object.
	 */
	public static final String ENTER_FRAME = "enterFrame";
	/**
	 * The <code>Event.REMOVED_FROM_STAGE</code> constant defines the value of the type property of a removedFromStage event object.
	 */
	public static final String REMOVED_FROM_STAGE = "removedFromStage";
	/**
	 * The <code>Event.RESIZE</code> constant defines the value of the type property of a resize event object.
	 */
	public static final String RESIZE = "resize";
	/**
	 * The <code>Event.SOUND_COMPLETE</code> constant defines the value of the type property of a soundComplete event object.
	 */
	public static final String SOUND_COMPLETE = "soundComplete";
	
	/** 
	 * The type of event.
	 */
	public String type;
	
	/**
	 * Creates an <code>Event</code> object to pass as a parameter to event listeners.
	 * 
	 * @param	type			The type of event, accessible as <code>Event.type</code>.
	 * @param	bubbles		Determines whether the <code>Event</code> object participates in the bubbling stage of the event flow. The default value is <code>false</code>.
	 * @param	cancelable	Determines whether the <code>Event</code> object can be cancelled. The default values is <code>false</code>.
	 */
	public Event(String type, boolean bubbles, boolean cancelable)
	{
		this.type = type;
	}
	
	/**
	 * Creates an <code>Event</code> object to pass as a parameter to event listeners.
	 * 
	 * @param	type			The type of event, accessible as <code>Event.type</code>.
	 * @param	bubbles		Determines whether the <code>Event</code> object participates in the bubbling stage of the event flow. The default value is <code>false</code>.
	 */
	public Event(String type, boolean bubbles)
	{
		this(type, bubbles, false);
	}
	
	/**
	 * Creates an <code>Event</code> object to pass as a parameter to event listeners.
	 * 
	 * @param	type			The type of event, accessible as <code>Event.type</code>.
	 */
	public Event(String type)
	{
		this(type, false, false);
	}
}
