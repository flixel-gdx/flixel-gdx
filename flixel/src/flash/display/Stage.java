package flash.display;

public class Stage 
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
		stageWidth = width;
		stageHeight = height;
	}
}
