package flash.events;

/**
 * This interface replicates some of the <code>IEventDispatcher</code> functionality from Flash.
 * 
 * @author Ka Wing Chin
 */
public interface IEventDispatcher
{
	/**
     * Registers an event listener object with an <code>EventDispatcher</code> object so that the listener receives notification of an event.
     *
     * @param	type              The type of event.
     * @param	listener          The listener function that processes the event. This function must accept an Event object as its only parameter and must return nothing.
     * @param	useCapture        Determines whether the listener works in the capture phase or the target and bubbling phases.
     * @param	priority          The priority level of the event listener.
     * @param	useWeakReference	Determines whether the reference to the listener is strong or weak.
     */
	public void addEventListener(String type, IEventListener listener, boolean useCapture, int priority, boolean useWeakReference);

	/**
     * Registers an event listener object with an <code>EventDispatcher</code> object so that the listener receives notification of an event.
     *
     * @param	type			The type of event.
     * @param	listener		The listener function that processes the event. This function must accept an Event object as its only parameter and must return nothing.
     * @param	useCapture	Determines whether the listener works in the capture phase or the target and bubbling phases.
     * @param	priority		The priority level of the event listener.
     */
	public void addEventListener(String type, IEventListener listener, boolean useCapture, int priority);

	/**
     * Registers an event listener object with an <code>EventDispatcher</code> object so that the listener receives notification of an event.
     *
     * @param	type			The type of event.
     * @param	listener		The listener function that processes the event. This function must accept an Event object as its only parameter and must return nothing.
     * @param	useCapture	Determines whether the listener works in the capture phase or the target and bubbling phases.
     */
	public void addEventListener(String type, IEventListener listener, boolean useCapture);
	
	/**
     * Registers an event listener object with an <code>EventDispatcher</code> object so that the listener receives notification of an event.
     *
     * @param	type			The type of event.
     * @param	listener		The listener function that processes the event. This function must accept an Event object as its only parameter and must return nothing.
     */
	public void addEventListener(String type, IEventListener listener);

	/**
     * Dispatches an event into the event flow.
     *
     * @param	event			The <code>Event</code> object that is dispatched into the event flow.
     * @return	A value of <code>true</code> if the event was successfully dispatched.
     */
	public boolean dispatchEvent(Event event);

	/**
	 * Checks whether the <code>EventDispatcher</code> object has any listeners registered for a specific type of event.
	 * 
	 * @param	type			The type of event.
	 */
	public boolean hasEventListener(String type);

	/**
     * Removes a listener from the <code>EventDispatcher</code> object. If there is no matching listener registered with the EventDispatcher object, a call to this method has no effect.
     *
     * @param	type			The type of event.
     * @param	listener		The listener object to remove.
     * @param	useCapture	Specifies whether the listener was registered for the capture phase or the target and bubbling phases.
     */
	public void removeEventListener(String type, IEventListener listener, boolean useCapture);

	/**
     * Removes a listener from the <code>EventDispatcher</code> object. If there is no matching listener registered with the EventDispatcher object, a call to this method has no effect.
     *
     * @param	type			The type of event.
     */
	public void removeEventListener(String type, IEventListener listener);
}
