package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class Tilemap2Demo {
	public static void main(String[] args)
	{
		new LwjglApplication(new org.flixel.examples.tiledmap2.TiledMap2Demo(), "", 480, 320, false);
	}
}
