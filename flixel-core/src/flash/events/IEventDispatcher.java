package flash.events;

/**
 * This interface replicates some of the IEventDispatcher functionality from Flash.
 * 
 * @author Ka Wing Chin
 */
public interface IEventDispatcher
{
	public void addEventListener(String type, Listener listener, boolean useCapture, int priority, boolean useWeakReference);

	public void addEventListener(String type, Listener listener, boolean useCapture, int priority);

	public void addEventListener(String type, Listener listener, boolean useCapture);
	
	public void addEventListener(String type, Listener listener);

	public boolean dispatchEvent(Event event);

	public boolean hasEventListener(String type);

	public void removeEventListener(String type, Listener listener, boolean useCapture);

	public void removeEventListener(String type, Listener listener);
}

