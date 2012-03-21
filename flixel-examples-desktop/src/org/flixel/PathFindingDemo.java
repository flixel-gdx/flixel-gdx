package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class PathFindingDemo 
{
	public static void main(String[] args)
	{
		new LwjglApplication(new org.examples.pathfinding.PathFindingDemo(), "", 480, 320, false);
	}
}
