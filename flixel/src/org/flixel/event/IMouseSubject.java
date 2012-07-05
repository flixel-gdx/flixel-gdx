package org.flixel.event;

/**
 * A separated event listener for touch input. 
 * Touches are no longer fired in the update loop.
 * 
 * @author Ka Wing Chin
 */
public interface IMouseSubject
{
	// Subscribe the observer to the event source.
	public void addObserver(IMouseObserver o);
	// Remove the observer from the event source.
	public void removeObserver(IMouseObserver o);
	// Notify the observer from the event source.
	public void notifyObserver();
}
