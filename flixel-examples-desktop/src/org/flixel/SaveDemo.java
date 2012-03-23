package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class SaveDemo
{
	public static void main(String[] args)
	{
		new LwjglApplication(new org.flixel.examples.savedemo.SaveDemo(), "", 400, 300, false);
	}
}
