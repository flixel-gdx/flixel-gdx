package flash.display;

import flash.events.IEventDispatcher;

/**
 * The Stage class represents the main drawing area.
 * 
 * @author Thomas Weston
 */
public interface Stage extends IEventDispatcher
{
	/**
	 * The current width, in pixels, of the Stage.
	 */
	public int getStageWidth();

	/**
	 * The current height, in pixels, of the Stage.
	 */
	public int getStageHeight();

	/**
	 * Specifies the Graphics object that belongs to this sprite where vector drawing commands can occur.
	 */
	public Graphics getGraphics();
}
