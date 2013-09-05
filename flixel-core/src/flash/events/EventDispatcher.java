package flash.events;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class replicates some of the EventDispatcher functionality from Flash.
 * 
 * @author Ka Wing Chin
 */
public class EventDispatcher implements IEventDispatcher
{
	protected Map<String, List<IEventListener>> _listeners;
	
	public EventDispatcher()
	{
		_listeners = new HashMap<String, List<IEventListener>>();
	}
	
	@Override
	public void addEventListener(String type, IEventListener listener, boolean useCapture, int priority, boolean useWeakReference)
	{
		List<IEventListener> listenersForType = _listeners.get(type);
		
		if(listenersForType == null)
		{
			listenersForType = new LinkedList<IEventListener>();
			_listeners.put(type, listenersForType);
		}
		listenersForType.add(listener);
	}

	@Override
	public void addEventListener(String type, IEventListener listener, boolean useCapture, int priority)
	{
		addEventListener(type, listener, useCapture, priority, false);
	}

	@Override
	public void addEventListener(String type, IEventListener listener, boolean useCapture)
	{
		addEventListener(type, listener, useCapture, 0, false);
	}

	@Override
	public void addEventListener(String type, IEventListener listener)
	{
		addEventListener(type, listener, false, 0, false);
	}

	@Override
	public boolean dispatchEvent(Event event)
	{
		List<IEventListener> listenersForType = _listeners.get(event.type);
		
		if(listenersForType == null || listenersForType.size() <= 0)
			return false;
		
		List<IEventListener> listenersForTypeCopy = new LinkedList<IEventListener>(listenersForType);
		
		for(IEventListener listener : listenersForTypeCopy)
			listener.onEvent(event);
		
		return true;
	}
	
	@Override
	public boolean hasEventListener(String type)
	{
		List<IEventListener> listenersForType = _listeners.get(type);
		return listenersForType != null && listenersForType.size() > 0;
	}

	@Override
	public void removeEventListener(String type, IEventListener listener, boolean useCapture)
	{
		List<IEventListener> listenersForType = _listeners.get(type);
		
		if(listenersForType == null)
			return;
		
		while(listenersForType.remove(listener));
	}

	@Override
	public void removeEventListener(String type, IEventListener listener)
	{
		removeEventListener(type, listener, false);
	}
}