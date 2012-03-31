package org.flixel.system;

import org.flixel.system.debug.Perf;

/**
 * Container for the new debugger overlay. Most of the functionality is in the
 * debug folder widgets, but this class instantiates the widgets and handles
 * their basic formatting and arrangement.
 * 
 * @author Ka Wing Chin
 */
public class FlxDebugger
{
	/**
	 * Container for the performance monitor widget.
	 */
	public Perf perf;
	public boolean visible;
	
	/**
	 * Instantiates the debugger overlay.
	 * 
	 * @param Width		The width of the screen.
	 * @param Height	The height of the screen.
	 */
	public FlxDebugger()
	{
		perf = new Perf();
	}
	
	
	/**
	 * Clean up memory.
	 */
	public void destroy()
	{
		perf.destroy();
		perf = null;
	}
	
}
