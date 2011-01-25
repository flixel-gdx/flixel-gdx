package org.flixel.data;

import org.flixel.FlxObject;

/**
 * The world's smallest linked list class.
 * Useful for optimizing time-critical or highly repetitive tasks!
 * See <code>FlxQuadTree</code> for how to use it, IF YOU DARE.
 */
public class FlxList
{
	/**
	 * Stores a reference to a <code>FlxObject</code>.
	 */
	public FlxObject object;
	/**
	 * Stores a reference to the next link in the list.
	 */
	public FlxList next;
	
	/**
	 * Creates a new link, and sets <code>object</code> and <code>next</code> to <code>null</null>.
	 */
	public FlxList()
	{
		object = null;
		next = null;
	}
}
