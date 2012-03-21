package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class TilemapDemo 
{
	public static void main(String[] args)
	{
		new LwjglApplication(new org.examples.tilemap.Tilemap(), "", 480, 320, false);
	}
}