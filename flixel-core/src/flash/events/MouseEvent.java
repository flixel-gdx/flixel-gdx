package flash.events;

/**
 * A copy of Flash's <code>MouseEvent</code>.
 * 
 * @author Ka Wing Chin
 */
public class MouseEvent extends Event
{
	/**
	 * Defines the value of the type property of a mouseUp event object.
	 */
	static public final String MOUSE_UP = "mouseUp";
	/**
	 * Defines the value of the type property of a mouseDown event object.
	 */
	static public final String MOUSE_DOWN = "mouseDown";
	
	/**
	 * The horizontal coordinate at which the event occurred in global Stage coordinates.
	 */
	public int stageX;
	/**
	 * The vertical coordinate at which the event occurred in global Stage coordinates.
	 */
	public int stageY;
	
	/**
	 * Creates an Event object that contains information about mouse events.
	 * 
	 * @param type		The type of event.
	 * @param stageX	The horizontal coordinate at which the event occurred in global Stage coordinates.
	 * @param stageY	The vertical coordinate at which the event occurred in global Stage coordinates.
	 */
	public MouseEvent(String type, int stageX, int stageY)
	{
		super(type);
		this.stageX = stageX;
		this.stageY = stageY;
	}	
}