package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class TimerDemo
{
	public static void main(String args[])
	{
		new LwjglApplication(new org.flixel.examples.timer.TimerDemo(), "", 480, 320, false);
	}
}
