package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class PathFindingDemo 
{
	public static void main(String[] args)
	{
		new LwjglApplication(new org.flixel.examples.pathfinding.PathFindingDemo(), "", 400, 300, false);
	}
}
