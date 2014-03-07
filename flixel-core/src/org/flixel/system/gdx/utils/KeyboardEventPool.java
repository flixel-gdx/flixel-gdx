package org.flixel.system.gdx.utils;

import flash.events.KeyboardEvent;

public class KeyboardEventPool extends RotationPool<KeyboardEvent>
{
	public KeyboardEventPool(int size)
	{
		super(size);
	}

	public KeyboardEvent obtain(String type, int keycode, char charcode)
	{
		KeyboardEvent event = obtain();
		event.type = type;
		event.keyCode = keycode;
		event.charCode = charcode;
		return event;
	}

	public KeyboardEvent obtain(String type, int keycode)
	{
		KeyboardEvent event = obtain();
		event.type = type;
		event.keyCode = keycode;
		return event;
	}

	@Override
	protected KeyboardEvent newObject()
	{
		return new KeyboardEvent("");
	}
}
