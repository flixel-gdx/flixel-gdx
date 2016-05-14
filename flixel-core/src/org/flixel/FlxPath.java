package org.flixel;

import org.flixel.plugin.DebugPathDisplay;

import com.badlogic.gdx.utils.Array;

import flash.display.Graphics;

/**
 * This is a simple path data container. Basically a list of points that
 * a <code>FlxObject</code> can follow. Also has code for drawing debug visuals.
 * <code>FlxTilemap.findPath()</code> returns a path object, but you can
 * also just make your own, using the <code>add()</code> functions below
 * or by creating your own array of points.
 * 
 * @author Thomas Weston
 * @author Ka Wing Chin
 */
public class FlxPath
{
	/**
	 * The list of <code>FlxPoint</code>s that make up the path data.
	 */
	public Array<FlxPoint> nodes;
	/**
	 * Specify a debug display color for the path. Default is white.
	 */
	public int debugColor;
	/**
	 * Specify a debug display scroll factor for the path. Default is (1,1).
	 * NOTE: does not affect world movement! Object scroll factors take care of that.
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

		DebugPathDisplay debugPathDisplay = getManager();
		if(debugPathDisplay != null)
			debugPathDisplay.add(this);
	}

	/**
	 * Instantiate a new path object.
	 */
	public FlxPath()
	{
		this(null);
	}

	/**
	 * Clean up memory.
	 */
	public void destroy()
	{
		DebugPathDisplay debugPathDisplay = getManager();
		if(debugPathDisplay != null)
			debugPathDisplay.remove(this);

		debugScrollFactor = null;
		_point = null;
		nodes = null;
	}

	/**
	 * Add a new node to the end of the path at the specified location.
	 * 
	 * @param	X	X position of the new path point in world coordinates.
	 * @param	Y	Y position of the new path point in world coordinates.
	 */
	public void add(float X,float Y)
	{
		nodes.add(new FlxPoint(X,Y));
	}

	/**
	 * Add a new node to the path at the specified location and index within the path.
	 * 
	 * @param	X		X position of the new path point in world coordinates.
	 * @param	Y		Y position of the new path point in world coordinates.
	 * @param	Index	Where within the list of path nodes to insert this new point.
	 */
	public void addAt(float X, float Y, int Index)
	{
		if(Index > nodes.size)
			Index = nodes.size;
		nodes.insert(Index,new FlxPoint(X,Y));
	}

	/**
	 * Sometimes its easier or faster to just pass a point object instead of separate X and Y coordinates.
	 * This also gives you the option of not creating a new node but actually adding that specific
	 * <code>FlxPoint</code> object to the path.  This allows you to do neat things, like dynamic paths.
	 * 
	 * @param	Node			The point in world coordinates you want to add to the path.
	 * @param	AsReference		Whether to add the point as a reference, or to create a new point with the specified values.
	 */
	public void addPoint(FlxPoint Node,boolean AsReference)
	{
		if(AsReference)
			nodes.add(Node);
		else
			nodes.add(new FlxPoint(Node.x,Node.y));
	}

	/**
	 * Sometimes its easier or faster to just pass a point object instead of separate X and Y coordinates.
	 * This also gives you the option of not creating a new node but actually adding that specific
	 * <code>FlxPoint</code> object to the path.  This allows you to do neat things, like dynamic paths.
	 * 
	 * @param	Node			The point in world coordinates you want to add to the path.
	 */
	public void addPoint(FlxPoint Node)
	{
		addPoint(Node,false);
	}

	/**
	 * Sometimes its easier or faster to just pass a point object instead of separate X and Y coordinates.
	 * This also gives you the option of not creating a new node but actually adding that specific
	 * <code>FlxPoint</code> object to the path. This allows you to do neat things, like dynamic paths.
	 * 
	 * @param	Node			The point in world coordinates you want to add to the path.
	 * @param	Index			Where within the list of path nodes to insert this new point.
	 * @param	AsReference		Whether to add the point as a reference, or to create a new point with the specified values.
	 */
	public void addPointAt(FlxPoint Node, int Index, boolean AsReference)
	{
		if(Index > nodes.size)
			Index = nodes.size;
		if(AsReference)
			nodes.insert(Index,Node);
		else
			nodes.insert(Index,new FlxPoint(Node.x,Node.y));
	}

	/**
	 * Sometimes its easier or faster to just pass a point object instead of separate X and Y coordinates.
	 * This also gives you the option of not creating a new node but actually adding that specific
	 * <code>FlxPoint</code> object to the path. This allows you to do neat things, like dynamic paths.
	 * 
	 * @param	Node			The point in world coordinates you want to add to the path.
	 * @param	Index			Where within the list of path nodes to insert this new point.
	 */
	public void addPointAt(FlxPoint Node,int Index)
	{
		addPointAt(Node,Index,false);
	}

	/**
	 * Remove a node from the path.
	 * NOTE: only works with points added by reference or with references from <code>nodes</code> itself!
	 * 
	 * @param	Node	The point object you want to remove from the path.
	 * 
	 * @return	The node that was excised. Returns null if the node was not found.
	 */
	public FlxPoint remove(FlxPoint Node)
	{
		int index = nodes.indexOf(Node, true);
		if(index >= 0)
			return nodes.removeIndex(index);
		else
			return null;
	}

	/**
	 * Remove a node from the path using the specified position in the list of path nodes.
	 * 
	 * @param	Index	Where within the list of path nodes you want to remove a node.
	 * 
	 * @return	The node that was excised. Returns null if there were no nodes in the path.
	 */
	public FlxPoint removeAt(int Index)
	{
		if(nodes.size <= 0)
			return null;
		if(Index >= nodes.size)
			Index = nodes.size - 1;
		return nodes.removeIndex(Index);
	}

	/**
	 * Get the first node in the list.
	 * 
	 * @return	The first node in the path.
	 */
	public FlxPoint head()
	{
		if(nodes.size > 0)
			return nodes.get(0);
		return null;
	}

	/**
	 * Get the last node in the list.
	 * 
	 * @return	The last node in the path.
	 */
	public FlxPoint tail()
	{
		if(nodes.size > 0)
			return nodes.get(nodes.size-1);
		return null;
	}

	/**
	 * While this doesn't override <code>FlxBasic.drawDebug()</code>, the behavior is very similar.
	 * Based on this path data, it draws a simple lines-and-boxes representation of the path
	 * if the visual debug mode was toggled in the debugger overlay.  You can use <code>debugColor</code>
	 * and <code>debugScrollFactor</code> to control the path's appearance.
	 * 
	 * @param	Camera		The camera object the path will draw to.
	 */
	public void drawDebug(FlxCamera Camera)
	{
		if(nodes.size <= 0)
			return;
		if(Camera == null)
			Camera = FlxG.camera;

		//Set up our global flash graphics object to draw out the path
		Graphics gfx = FlxG.flashGfx;
		gfx.clear();

		//Then fill up the object with node and path graphics
		FlxPoint node;
		FlxPoint nextNode;
		int i = 0;
		int l = nodes.size;
		while(i < l)
		{
			//get a reference to the current node
			node = nodes.get(i);

			//find the screen position of the node on this camera
			_point.x = node.x - (Camera.scroll.x*debugScrollFactor.x); //copied from getScreenXY()
			_point.y = node.y - (Camera.scroll.y*debugScrollFactor.y);
			_point.x = (_point.x + ((_point.x > 0)?0.0000001f:-0.0000001f));
			_point.y = (_point.y + ((_point.y > 0)?0.0000001f:-0.0000001f));

			//decide what color this node should be
			int nodeSize = 2;
			if((i == 0) || (i == l-1))
				nodeSize *= 2;
			int nodeColor = debugColor;
			if(l > 1)
			{
				if(i == 0)
					nodeColor = FlxG.GREEN;
				else if(i == l-1)
					nodeColor = FlxG.RED;
			}

			//draw a box for the node
			// gfx.beginFill(nodeColor,0.5);
			gfx.lineStyle(1f,nodeColor,0.5f);
			gfx.drawRect(_point.x-nodeSize*0.5f,_point.y-nodeSize*0.5f,nodeSize,nodeSize);
			// gfx.endFill();

			//then find the next node in the path
			float linealpha = 0.3f;
			if(i < l-1)
				nextNode = nodes.get(i+1);
			else
			{
				nextNode = nodes.get(0);
				linealpha = 0.15f;
			}

			//then draw a line to the next node
			gfx.moveTo(_point.x,_point.y);
			gfx.lineStyle(1f,debugColor,linealpha);
			_point.x = nextNode.x - (Camera.scroll.x*debugScrollFactor.x); //copied from getScreenXY()
			_point.y = nextNode.y - (Camera.scroll.y*debugScrollFactor.y);
			_point.x = (_point.x + ((_point.x > 0)?0.0000001f:-0.0000001f));
			_point.y = (_point.y + ((_point.y > 0)?0.0000001f:-0.0000001f));
			gfx.lineTo(_point.x,_point.y);

			i++;
		}
	}

	/**
	 * While this doesn't override <code>FlxBasic.drawDebug()</code>, the behavior is very similar.
	 * Based on this path data, it draws a simple lines-and-boxes representation of the path
	 * if the visual debug mode was toggled in the debugger overlay.  You can use <code>debugColor</code>
	 * and <code>debugScrollFactor</code> to control the path's appearance.
	 */
	public void drawDebug()
	{
		drawDebug(null);
	}

	static public DebugPathDisplay getManager()
	{
		return (DebugPathDisplay) FlxG.getPlugin(DebugPathDisplay.class);
	}
}
