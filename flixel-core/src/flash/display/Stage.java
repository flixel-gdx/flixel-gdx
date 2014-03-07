package flash.display;

import flash.events.IEventDispatcher;

/**
 * This class replicates some of the <code>Stage</code> functionality from
 * Flash.
 * 
 * @author Thomas Weston
 */
public interface Stage extends IEventDispatcher
{
	/**
	 * The current width, in pixels, of the <code>Stage</code>.
	 */
	public int getStageWidth();

	/**
	 * The current height, in pixels, of the <code>Stage</code>.
	 */
	public int getStageHeight();

	/**
	 * Specifies the Graphics object that belongs to this sprite where vector
	 * drawing commands can occur.
	 */
	public Graphics getGraphics();
}
