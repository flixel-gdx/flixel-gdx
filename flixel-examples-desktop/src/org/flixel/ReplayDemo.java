package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class ReplayDemo
{
	public static void main(String[] args)
	{
		new LwjglApplication(new org.flixel.examples.replay.ReplayDemo(), "", 400, 300, false);
	}
}
