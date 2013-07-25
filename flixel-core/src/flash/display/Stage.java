package flash.display;

import flash.events.EventDispatcher;

/**
 * This class replicates some of the <code>Stage</code> functionality from Flash.
 * 
 * @author Thomas Weston
 */
public class Stage extends EventDispatcher
{
	/**
	 * The current height, in pixels, of the <code>Stage</code>.
	 */
	public int stageHeight;
	
	/**
	 * The current width, in pixels, of the <code>Stage</code>.
	 */
	public int stageWidth;	
	/**
	 * Creates a new <code>Stage</code> with the specified width and height.
	 * 
	 * @param	width	The width of the <code>Stage</code> in pixels.
	 * @param	height	The height of the <code>Stage</code> in pixels.
	 */
	public Stage(int width, int height)
	{
		super();
		stageWidth = width;
		stageHeight = height;
	}
}