package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class CollisionDemo
{
	public static void main(String[] args)
	{
		new LwjglApplication(new org.flixel.examples.collision.CollisionDemo(), "", 400, 300, false);
	}
}
