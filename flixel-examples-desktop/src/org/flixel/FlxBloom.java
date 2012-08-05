package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class FlxBloom
{
	public static void main(String[] args)
	{
		new LwjglApplication(new org.flixel.examples.bloom.FlxBloom(), "", 640, 480, false);
	}
}
