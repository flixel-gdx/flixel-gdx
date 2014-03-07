package org.flixel.system.gdx.utils;

import flash.events.Event;

public class EventPool extends RotationPool<Event>
{
	public EventPool(int size)
	{
		super(size);
	}

	public Event obtain(String type)
	{
		Event event = obtain();
		event.type = type;
		return event;
	}

	@Override
	protected Event newObject()
	{
		return new Event("");
	}
}
