package org.flixel.event;

/**
 * Implement this to an object that needs to listen to mouse inputs.
 * 
 * @author Ka Wing Chin
 */
public interface IMouseObserver
{
	// Fires when the IMouseSubject::notifyObserver() is called.
	public void updateListener();
}
