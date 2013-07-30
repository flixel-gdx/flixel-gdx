package org.flixel.event;

import org.flixel.plugin.GestureManager.GestureData;

public interface IFlxGesture
{
	public void callback(int Gesture, GestureData data);
}
