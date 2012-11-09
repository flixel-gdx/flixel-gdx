package flash.events;

import com.badlogic.gdx.utils.Array;

/**
 * This class replicates some of the EventDispatcher functionality from Flash.
 * 
 * @author Ka Wing Chin
 */
public class EventDispatcher implements IEventDispatcher
{
	/**
	 * A collection of Event Listeners.
	 */
	protected Array<Listener> _listeners;
	
	/**
	 * Constructor
	 */
	public EventDispatcher()
	{
		_listeners = new Array<Listener>();
	}
	
	/**
     * Registers an event listener object with an EventDispatcher object so that the listener receives notification of an event.
     *
     * @param type              The type of event.
     * @param listener          The listener function that processes the event. This function must accept an Event object as its only parameter and must return nothing.
     * @param useCapture        Determines whether the listener works in the capture phase or the target and bubbling phases.
     * @param priority          The priority level of the event listener.
     * @param useWeakReference	Determines whether the reference to the listener is strong or weak.
     */
	@Override
	public void addEventListener(String type, Listener listener, boolean useCapture, int priority, boolean useWeakReference)
	{
		listener.type = type;
		_listeners.add(listener);
	}

	/**
     * Registers an event listener object with an EventDispatcher object so that the listener receives notification of an event.
     *
     * @param type          The type of event.
     * @param listener      The listener function that processes the event. This function must accept an Event object as its only parameter and must return nothing.
     * @param useCapture    Determines whether the listener works in the capture phase or the target and bubbling phases.
     * @param priority		The priority level of the event listener.
     */
	@Override
	public void addEventListener(String type, Listener listener, boolean useCapture, int priority)
	{
		addEventListener(type, listener, useCapture, priority, false);
	}

	/**
     * Registers an event listener object with an EventDispatcher object so that the listener receives notification of an event.
     *
     * @param type          The type of event.
     * @param listener      The listener function that processes the event. This function must accept an Event object as its only parameter and must return nothing.
     * @param useCapture    Determines whether the listener works in the capture phase or the target and bubbling phases.
     */
	@Override
	public void addEventListener(String type, Listener listener, boolean useCapture)
	{
		addEventListener(type, listener, useCapture, 0, false);
	}

	/**
     * Registers an event listener object with an EventDispatcher object so that the listener receives notification of an event.
     *
     * @param type          The type of event.
     * @param listener      The listener function that processes the event. This function must accept an Event object as its only parameter and must return nothing.
     */
	@Override
	public void addEventListener(String type, Listener listener)
	{
		addEventListener(type, listener, false, 0, false);
	}

	/**
     * Dispatches an event into the event flow.
     *
     * @param event	The Event object that is dispatched into the event flow.
     * @return      A value of true if the event was successfully dispatched.
     */
	@Override
	public boolean dispatchEvent(Event event)
	{
		for(Listener listener : _listeners)
		{
			if(event.type.equals(listener.type))
			{
				listener.onEvent(event);
			}
		}
		return true;
	}
	
	/**
	 * Checks whether the EventDispatcher object has any listeners registered for a specific type of event.
	 * @param type	The type of event.
	 */
	@Override
	public boolean hasEventListener(String type)
	{
		for(Listener listener : _listeners)
		{
			if(type.equals(listener.type))
				return true;
		}
		return false;
	}
	
	/**
     * Removes a listener from the EventDispatcher object. If there is no matching listener registered with the EventDispatcher object, a call to this method has no effect.
     *
     * @param type         	The type of event.
     * @param listener     	The listener object to remove.
     * @param useCapture	Specifies whether the listener was registered for the capture phase or the target and bubbling phases.
     */
	@Override
	public void removeEventListener(String type, Listener listener, boolean useCapture)
	{
		listener.type = type;
		_listeners.removeValue(listener, false);		
	}

	/**
     * Removes a listener from the EventDispatcher object. If there is no matching listener registered with the EventDispatcher object, a call to this method has no effect.
     *
     * @param type	The type of event.
     */
	@Override
	public void removeEventListener(String type, Listener listener)
	{
		removeEventListener(type, listener, false);
	}
}

