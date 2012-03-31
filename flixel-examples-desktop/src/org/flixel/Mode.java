package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class Mode
{
	public static void main(String[] args)
	{
		new LwjglApplication(new org.flixel.examples.mode.Mode(), "", 620, 480, false);
	}
}
