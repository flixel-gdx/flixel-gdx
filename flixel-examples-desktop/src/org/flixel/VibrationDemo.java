package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class VibrationDemo
{
	public static void main(String[] args)
	{
		new LwjglApplication(new org.flixel.examples.vibration.VibrationDemo(), "", 480, 320, false);
	}
}
