package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class BlurDemo
{
	public static void main(String[] args)
	{
		new LwjglApplication(new org.flixel.examples.blur.BlurDemo(), "", 640, 480, false);
	}
}
