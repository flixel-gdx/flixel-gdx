package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class TileMapDemo
{
	static public void main(String[] args)
	{
		new LwjglApplication(new org.examples.tiledmap.TiledMapDemo(), "", 480, 320, false);
	}
}
