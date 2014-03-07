package org.flixel.system.gdx.utils;

import flash.events.TouchEvent;

public class TouchEventPool extends RotationPool<TouchEvent>
{
	public TouchEventPool(int size)
	{
		super(size);
	}

	public TouchEvent obtain(String type, int stageX, int stageY, int pointer)
	{
		TouchEvent event = obtain();
		event.type = type;
		event.stageX = stageX;
		event.stageY = stageY;
		event.touchPointID = pointer;
		return event;
	}

	@Override
	protected TouchEvent newObject()
	{
		return new TouchEvent("");
	}
}
