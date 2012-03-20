package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class ScrollingDemo
{
	public static void main(String[] args)
	{
		new LwjglApplication(new org.examples.scrolling.ScrollingDemo(), "", 480, 320, false);
	}
}
