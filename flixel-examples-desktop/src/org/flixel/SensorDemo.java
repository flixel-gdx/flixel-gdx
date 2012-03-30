package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class SensorDemo
{

	public static void main(String[] args)
	{
		new LwjglApplication(new org.flixel.examples.sensor.SensorDemo(), "", 320, 480, false);
	}

}
