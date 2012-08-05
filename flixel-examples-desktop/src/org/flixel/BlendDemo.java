package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class BlendDemo
{
	public static void main(String[] args)
	{
		new LwjglApplication(new org.flixel.examples.blend.BlendDemo(), "", 400, 400, false);
	}
}
