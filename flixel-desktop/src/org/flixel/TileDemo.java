package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class TileDemo
{
	static public void main(String[] args)
	{
		new LwjglApplication(new org.examples.tile.TileDemo(), "", 480, 320, false);
	}
}
