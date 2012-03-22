package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class FlxCollisions
{
	public static void main(String[] args)
	{
		new LwjglApplication(new org.flixel.examples.flxcollisions.FlxCollisions(), "FlxCollisions", 640, 480, false);
	}
}
