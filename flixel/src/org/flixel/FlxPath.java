package org.flixel;

import com.badlogic.gdx.utils.Array;

/**
 * This is a simple path data container.  Basically a list of points that
 * a <code>FlxObject</code> can follow.  Also has code for drawing debug visuals.
 * <code>FlxTilemap.findPath()</code> returns a path object, but you can
 * also just make your own, using the <code>add()</code> functions below
 * or by creating your own array of points.
 * 
 * @author	Ka Wing Chin
 */
public class FlxPath
{
	/**
	 * The list of <code>FlxPoint</code>s that make up the path data.
	 */
	public Array<FlxPoint> nodes;
	/**
	 * Specify a debug display color for the path.  Default is white.
	 */
	public int debugColor;
	/**
	 * Specify a debug display scroll factor for the path.  Default is (1,1).
	 * NOTE: does not affect world movement!  Object scroll factors take care of that.
	 */
	public FlxPoint debugScrollFactor;
	/**
	 * Setting this to true will prevent the object from appearing
	 * when the visual debug mode in the debugger overlay is toggled on.
	 * @default false
	 */
	public boolean ignoreDrawDebug;
	/**
	 * Internal helper for keeping new variable instantiations under control.
	 */
	protected FlxPoint _point;
	
	
	/**
	 * Instantiate a new path object.
	 * 
	 * @param	Nodes	Optional, can specify all the points for the path up front if you want.
	 */
	public FlxPath(Array<FlxPoint> Nodes)
	{
		if(Nodes == null)
			nodes = new Array<FlxPoint>();
		else
			nodes = Nodes;
		_point = new FlxPoint();
		debugScrollFactor = new FlxPoint(1.0f,1.0f);
		debugColor = 0xffffff;
		ignoreDrawDebug = false;
		
//		DebugPathDisplay debugPathDisplay = manager;
//		if(debugPathDisplay != null)
//			debugPathDisplay.add(this);
	}
	
	/**
	 * Instantiate a new path object.
	 */ 
	public FlxPath()
	{
		this(null);
	}
	
	public void destroy()
	{
		// TODO Auto-generated method stub
		
	}

	public void addPoint(FlxPoint node, boolean b)
	{
		// TODO Auto-generated method stub
		
	}

}
