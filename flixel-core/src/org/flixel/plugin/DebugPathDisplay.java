package org.flixel.plugin;

import org.flixel.FlxBasic;
import org.flixel.FlxCamera;
import org.flixel.FlxG;
import org.flixel.FlxPath;

import com.badlogic.gdx.utils.Array;

/**
 * A simple manager for tracking and drawing FlxPath debug data to the screen.
 * 
 * @author Ka Wing Chin
 */
public class DebugPathDisplay extends FlxBasic
{
	protected Array<FlxPath> _paths;

	/**
	 * Instantiates a new debug path display manager.
	 */
	public DebugPathDisplay()
	{
		_paths = new Array<FlxPath>();
		active = false; //don't call update on this plugin
	}

	/**
	 * Clean up memory.
	 */
	@Override
	public void destroy()
	{
		super.destroy();
		clear();
		_paths = null;
	}

	/**
	 * Called by <code>FlxG.drawPlugins()</code> after the game state has been drawn.
	 * Cycles through cameras and calls <code>drawDebug()</code> on each one.
	 */
	@Override
	public void draw()
	{
		FlxCamera camera = FlxG.getActiveCamera();

		if(cameras == null)
			cameras = FlxG.cameras;

		if(!cameras.contains(camera, true))
			return;

		if(FlxG.visualDebug && !ignoreDrawDebug)
			drawDebug(camera);
	}

	/**
	 * Similar to <code>FlxObject</code>'s <code>drawDebug()</code> functionality,
	 * this function calls <code>drawDebug()</code> on each <code>FlxPath</code> for the specified camera.
	 * Very helpful for debugging!
	 * 
	 * @param	Camera	Which <code>FlxCamera</code> object to draw the debug data to.
	 */
	@Override
	public void drawDebug(FlxCamera Camera)
	{
		if(Camera == null)
			Camera = FlxG.camera;

		int i = _paths.size-1;
		FlxPath path;
		while(i >= 0)
		{
			path = _paths.get(i--);
			if((path != null) && !path.ignoreDrawDebug)
				path.drawDebug(Camera);
		}
	}

	/**
	 * Similar to <code>FlxObject</code>'s <code>drawDebug()</code> functionality,
	 * this function calls <code>drawDebug()</code> on each <code>FlxPath</code> for the specified camera.
	 * Very helpful for debugging! 
	 */
	@Override
	public void drawDebug()
	{
		drawDebug(null);
	}

	/**
	 * Add a path to the path debug display manager.
	 * Usually called automatically by <code>FlxPath</code>'s constructor.
	 * 
	 * @param	Path	The <code>FlxPath</code> you want to add to the manager.
	 */
	public void add(FlxPath Path)
	{
		_paths.add(Path);
	}

	/**
	 * Remove a path from the path debug display manager.
	 * Usually called automatically by <code>FlxPath</code>'s <code>destroy()</code> function.
	 * 
	 * @param	Path	The <code>FlxPath</code> you want to remove from the manager.
	 */
	public void remove(FlxPath Path)
	{
		int index = _paths.indexOf(Path,true);
		if(index >= 0)
			_paths.removeIndex(index);
	}

	/**
	 * Removes all the paths from the path debug display manager.
	 */
	public void clear()
	{
		int i = _paths.size - 1;
		FlxPath path;
		while(i >= 0)
		{
			path = _paths.get(i--);
			if(path != null)
				path.destroy();
		}
		_paths.clear();
	}
}
