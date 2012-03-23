package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class FlxTilemapDemo
{
	public static void main(String[] args)
	{
		new LwjglApplication(new org.flixel.examples.flxtilemap.FlxTilemap(), "FlxTilemap", 400, 300, false);
	}
}
