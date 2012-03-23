package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class PathFinding 
{
	public static void main(String[] args)
	{
		new LwjglApplication(new org.flixel.examples.pathfinding.PathFinding(), "", 400, 300, false);
	}
}
