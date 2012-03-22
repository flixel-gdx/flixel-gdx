package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class TileDemo 
{
	public static void main(String[] args)
	{
		new LwjglApplication(new org.flixel.examples.tile.TileDemo(), "", 480, 320, false);
	}
}