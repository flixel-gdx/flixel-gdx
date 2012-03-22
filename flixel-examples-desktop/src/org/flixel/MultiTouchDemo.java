package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class MultiTouchDemo
{
	static public void main(String[] args)
	{
		new LwjglApplication(new org.flixel.examples.multitouch.MultiTouchDemo(), "", 480, 320, false);
	}
}
