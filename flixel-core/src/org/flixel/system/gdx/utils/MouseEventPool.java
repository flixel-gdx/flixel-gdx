package org.flixel.system.gdx.utils;

import flash.events.MouseEvent;

public class MouseEventPool extends RotationPool<MouseEvent>
{
	public MouseEventPool(int size)
	{
		super(size);
	}

	public MouseEvent obtain(String type, int stageX, int stageY, int delta)
	{
		MouseEvent event = obtain();
		event.type = type;
		event.stageX = stageX;
		event.stageY = stageY;
		event.delta = delta;
		return event;
	}

	@Override
	protected MouseEvent newObject()
	{
		return new MouseEvent("");
	}
}
