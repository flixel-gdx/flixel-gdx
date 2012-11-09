package flash.display;

import flash.events.EventDispatcher;

/**
 * This class replicates some of the Stage functionality from Flash.
 * 
 * @author Thomas Weston
 */
public class Stage extends EventDispatcher
{
	/**
	 * The current height, in pixels, of the Stage.
	 */
	public int stageHeight;
	
	/**
	 * The current width, in pixels, of the Stage.
	 */
	public int stageWidth;	
	/**
	 * Creates a new stage with the specified width and height.
	 * 
	 * @param	width	The width of the stage in pixels.
	 * @param	height	The height of the stage in pixels.
	 */
	public Stage(int width, int height)
	{
		super();
		stageWidth = width;
		stageHeight = height;
	}
}