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
	
	@Override
	public void addEventListener(String type, Listener listener, boolean useCapture, int priority, boolean useWeakReference)
	{
		listener.type = type;
		_listeners.add(listener);
	}

	@Override
	public void addEventListener(String type, Listener listener, boolean useCapture, int priority)
	{
		addEventListener(type, listener, useCapture, priority, false);
	}

	@Override
	public void addEventListener(String type, Listener listener, boolean useCapture)
	{
		addEventListener(type, listener, useCapture, 0, false);
	}

	@Override
	public void addEventListener(String type, Listener listener)
	{
		addEventListener(type, listener, false, 0, false);
	}

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

	@Override
	public void removeEventListener(String type, Listener listener, boolean useCapture)
	{
		listener.type = type;
		_listeners.removeValue(listener, false);		
	}

	@Override
	public void removeEventListener(String type, Listener listener)
	{
		removeEventListener(type, listener, false);
	}
}

