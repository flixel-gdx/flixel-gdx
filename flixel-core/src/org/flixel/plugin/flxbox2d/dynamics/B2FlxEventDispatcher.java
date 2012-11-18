package org.flixel.plugin.flxbox2d.dynamics;

import com.badlogic.gdx.utils.Array;


/**
 * A contact-event dispatcher of FlxBox2D.
 *
 * @author Ka Wing Chin
 */
public class B2FlxEventDispatcher
{
	/**
	 * A collection of event B2FlxListeners.
	 */
	protected Array<B2FlxListener> _listeners;
	
	public B2FlxEventDispatcher()
	{
		_listeners = new Array<B2FlxListener>();
	}

	public void addEventListener(String type, B2FlxListener listener)
	{
		listener.type = type;
		_listeners.add(listener);
	}


	public boolean dispatchEvent(B2FlxContactEvent event)
	{
		for(B2FlxListener listener : _listeners)
		{
			if(event.type.equals(listener.type))
			{
				listener.onContact(event.sprite1, event.sprite2, event.contact);
			}
		}
		return true;
	}

	public boolean hasEventListener(String type)
	{
		for(B2FlxListener listener : _listeners)
		{
			if(type.equals(listener.type))
				return true;
		}
		return false;
	}

	public void removeEventListener(String type, B2FlxListener listener)
	{
		listener.type = type;
		_listeners.removeValue(listener, false);	
	}
	
	public void removeAllListeners()
	{		
		_listeners.clear();
	}
	
	public void destroy()
	{
		removeAllListeners();
		_listeners = null;
	}
}

