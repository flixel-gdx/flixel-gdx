package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class AnimationDemo
{
	public static void main(String[] args)
	{
		new LwjglApplication(new org.examples.animation.AnimationDemo(), "", 480, 320, false);
	}
}
