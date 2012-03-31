package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class AnalogDemo
{
	public static void main(String[] args)
	{
		new LwjglApplication(new org.flixel.examples.analog.AnalogDemo(), "", 800, 480, false);
	}
}
