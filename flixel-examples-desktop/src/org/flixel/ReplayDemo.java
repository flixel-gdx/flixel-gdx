package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class ReplayDemo
{
	public static void main(String[] args)
	{
		new LwjglApplication(new org.examples.replay.ReplayDemo(), "", 480, 320, false);
	}
}
