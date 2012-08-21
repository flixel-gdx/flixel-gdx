package flash.display;

import com.badlogic.gdx.utils.Array;

import flash.events.Event;
import flash.events.Listener;

/**
 * This class replicates some of the stage functionality from Flash.
 * 
 * @author Thomas Weston
 */
public class Stage 
{
	/**
	 * The current height, in pixels, of the Stage.
	 */
	public int stageHeight;
	
	/**
	 * The current width, in pixels, of the Stage.
	 */
	public int stageWidth;
	
	/**
	 * A collection of Event Listeners.
	 */
	private Array<Listener> _listeners;
	
	/**
	 * Creates a new stage with the specified width and height.
	 * 
	 * @param	width	The width of the stage in pixels.
	 * @param	height	The height of the stage in pixels.
	 */
	public Stage(int width, int height)
	{
		stageWidth = width;
		stageHeight = height;
		
		_listeners = new Array<Listener>();
	}
	
	/**
	 * Registers an event listener object with an EventDispatcher object so that the listener receives notification of an event.
	 * 
	 * @param type				The type of event.
	 * @param listener			The listener function that processes the event. This function must accept an Event object as its only parameter and must return nothing.
	 * @param useCapture		Determines whether the listener works in the capture phase or the target and bubbling phases.
	 * @param priority			The priority level of the event listener.
	 * @param useWeakReference	Determines whether the reference to the listener is strong or weak.
	 */
	public void addEventListener(String type, Listener listener, boolean useCapture, int priority, boolean useWeakReference)
	{
		listener.type = type;
		_listeners.add(listener);
	}
	
	/**
	 * Registers an event listener object with an EventDispatcher object so that the listener receives notification of an event.
	 * 
	 * @param type				The type of event.
	 * @param listener			The listener function that processes the event. This function must accept an Event object as its only parameter and must return nothing.
	 * @param useCapture		Determines whether the listener works in the capture phase or the target and bubbling phases.
	 * @param priority			The priority level of the event listener.
	 */
	public void addEventListener(String type, Listener listener, boolean useCapture, int priority)
	{
		addEventListener(type, listener, useCapture, priority, false);
	}
	
	/**
	 * Registers an event listener object with an EventDispatcher object so that the listener receives notification of an event.
	 * 
	 * @param type				The type of event.
	 * @param listener			The listener function that processes the event. This function must accept an Event object as its only parameter and must return nothing.
	 * @param useCapture		Determines whether the listener works in the capture phase or the target and bubbling phases.
	 */
	public void addEventListener(String type, Listener listener, boolean useCapture)
	{
		addEventListener(type, listener, useCapture, 0, false);
	}
	
	/**
	 * Registers an event listener object with an EventDispatcher object so that the listener receives notification of an event.
	 * 
	 * @param type				The type of event.
	 * @param listener			The listener function that processes the event. This function must accept an Event object as its only parameter and must return nothing.
	 */
	public void addEventListener(String type, Listener listener)
	{
		addEventListener(type, listener, false, 0, false);
	}
	
	/**
	 * Removes a listener from the EventDispatcher object. If there is no matching listener registered with the EventDispatcher object, a call to this method has no effect.
	 * 
	 * @param type			The type of event.
	 * @param listener		The listener object to remove.
	 * @param useCapture	Specifies whether the listener was registered for the capture phase or the target and bubbling phases.
	 */
	public void removeEventListener(String type, Listener listener, boolean useCapture)
	{
		listener.type = type;
		_listeners.removeValue(listener, false);
	}
	
	/**
	 * Removes a listener from the EventDispatcher object. If there is no matching listener registered with the EventDispatcher object, a call to this method has no effect.
	 * 
	 * @param type			The type of event.
	 * @param listener		The listener object to remove.
	 */
	public void removeEventListener(String type, Listener listener)
	{
		removeEventListener(type, listener, false);
	}
	
	/**
	 * Dispatches an event into the event flow.
	 * 
	 * @param event		The Event object that is dispatched into the event flow.
	 * @return			A value of true if the event was successfully dispatched.
	 */
	public boolean dispatchEvent(Event event)
	{
		for (Listener listener : _listeners)
		{
			if (event.type.equals(listener.type))
			{
				listener.onEvent(event);
			}
		}
		return true;
	}	
}