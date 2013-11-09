package flash.events;

/**
 * A copy of Flash's <code>TouchEvent</code>.
 * 
 * @author Thomas Weston
 */
public class TouchEvent extends Event
{
	/**
	 * Defines the value of the type property of a <code>TOUCH_END</code> touch event object.
	 */
	static public final String TOUCH_END = "touchEnd";
	/**
	 * Defines the value of the type property of a <code>TOUCH_BEGIN</code> touch event object.
	 */
	static public final String TOUCH_BEGIN = "touchBegin";
	
	/**
	 * The horizontal coordinate at which the event occurred in global <code>Stage</code> coordinates.
	 */
	public int stageX;
	/**
	 * The vertical coordinate at which the event occurred in global <code>Stage</code> coordinates.
	 */
	public int stageY;
	/**
	 * A unique identification number (as an int) assigned to the touch point.
	 */
	public int touchPointID;
	
	/**
	 * Creates an <code>Event</code> object that contains information about touch events.
	 * 
	 * @param	type				The type of event.
	 * @param	bubbles			Determines whether the <code>Event</code> object participates in the bubbling stage of the event flow. The default value is <code>true</code>.
	 * @param	cancelable		Determines whether the <code>Event</code> object can be cancelled. The default values is <code>false</code>.
	 * @param	touchPointID	A unique identification number (as an int) assigned to the touch point.
	 * @param	localX			The horizontal coordinate at which the event occurred relative to the containing sprite.
	 * @param	localY			The vertical coordinate at which the event occurred relative to the containing sprite.
	 */
	public TouchEvent(String type, boolean bubbles, boolean cancelable, int touchPointID, int localX, int localY)
	{
		super(type, bubbles, cancelable);
		this.touchPointID = touchPointID;
		this.stageX = localX;
		this.stageY = localY;
	}
	
	/**
	 * Creates an <code>Event</code> object that contains information about touch events.
	 * 
	 * @param	type				The type of event.
	 * @param	bubbles			Determines whether the <code>Event</code> object participates in the bubbling stage of the event flow. The default value is <code>true</code>.
	 * @param	cancelable		Determines whether the <code>Event</code> object can be cancelled. The default values is <code>false</code>.
 	 * @param	touchPointID	A unique identification number (as an int) assigned to the touch point.
	 * @param	localX			The horizontal coordinate at which the event occurred relative to the containing sprite.
	 */
	public TouchEvent(String type, boolean bubbles, boolean cancelable, int touchPointID, int localX)
	{
		this(type, bubbles, cancelable, touchPointID, localX, 0);
	}
	
	/**
	 * Creates an <code>Event</code> object that contains information about touch events.
	 * 
	 * @param	type				The type of event.
	 * @param	bubbles			Determines whether the <code>Event</code> object participates in the bubbling stage of the event flow. The default value is <code>true</code>.
	 * @param	cancelable		Determines whether the <code>Event</code> object can be cancelled. The default values is <code>false</code>.
	 * @param	touchPointID	A unique identification number (as an int) assigned to the touch point.
	 */
	public TouchEvent(String type, boolean bubbles, boolean cancelable, int touchPointID)
	{
		this(type, bubbles, cancelable, touchPointID, 0, 0);
	}
	
	/**
	 * Creates an <code>Event</code> object that contains information about touch events.
	 * 
	 * @param	type				The type of event.
	 * @param	bubbles			Determines whether the <code>Event</code> object participates in the bubbling stage of the event flow. The default value is <code>true</code>.
	 * @param	cancelable		Determines whether the <code>Event</code> object can be cancelled. The default values is <code>false</code>.
	 */
	public TouchEvent(String type, boolean bubbles, boolean cancelable)
	{
		this(type, bubbles, cancelable, 0, 0, 0);
	}
	
	/**
	 * Creates an <code>Event</code> object that contains information about touch events.
	 * 
	 * @param	type				The type of event.
	 * @param	bubbles			Determines whether the <code>Event</code> object participates in the bubbling stage of the event flow. The default value is <code>true</code>.
	 */
	public TouchEvent(String type, boolean bubbles)
	{
		this(type, bubbles, false, 0, 0, 0);
	}
	
	/**
	 * Creates an <code>Event</code> object that contains information about touch events.
	 * 
	 * @param	type				The type of event.
	 * @param	bubbles			Determines whether the <code>Event</code> object participates in the bubbling stage of the event flow. The default value is <code>true</code>.
	 */
	public TouchEvent(String type)
	{
		this(type, true, false, 0, 0, 0);
	}
}
