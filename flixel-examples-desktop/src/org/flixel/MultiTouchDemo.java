package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class MultiTouchDemo
{
	static public void main(String[] args)
	{
		new LwjglApplication(new org.examples.multitouch.MultiTouchDemo(), "", 480, 320, false);
	}
}
