package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class EZPlatformer 
{
	public static void main(String[] args)
	{
		new LwjglApplication(new org.flixel.examples.ezplatformer.EZPlatformer(), "EZPlatformer", 640, 480, false);
	}
}