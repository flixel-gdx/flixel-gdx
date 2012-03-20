package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class SplitScreenDemo 
{
	public static void main(String[] args)
	{
		new LwjglApplication(new org.examples.splitscreen.SplitScreen(), "", 480, 320, false);
	}
}
