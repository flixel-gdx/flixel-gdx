package org.examples.pathfinding;

import org.flixel.FlxGame;

/**
 * Pathfinding just means figuring out how to (or if you can) get from A to B.
 * FlxTilemap has a new function FlxTilemap.findPath() which returns a FlxPath object,
 * which is just a collection of "nodes", or FlxPoint objects.
 * Think of it as a list of (X,Y) coordinates in space, going from the starting
 * location to the ending location. Once you have a valid path, you can pass that
 * data to any FlxObject using FlxObject.followPath(). That function tells the object
 * to start following the path, and you can specify the speed, direction (backward, yoyo, etc),
 * and even tell the object to only follow the path horizontally (handy for objects with gravity applied).
 * These flags mean that you can use paths for more than just character AI - they're also useful
 * for elevators, moving platforms, and looping background animations.
 * 
 * @author Thomas Weston
 */
public class PathFindingDemo extends FlxGame
{
	public PathFindingDemo()
	{
		super(400, 300, PlayState.class, 1, 20, 20);
	}
	
	@Override
	public void create()
	{
		Asset.create();
		super.create();
	}
}
