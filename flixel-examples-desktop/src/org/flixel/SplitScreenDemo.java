package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class SplitScreenDemo 
{
	public static void main(String[] args)
	{
		new LwjglApplication(new org.flixel.examples.splitscreen.SplitScreen(), "", 400, 300, false);
	}
}
