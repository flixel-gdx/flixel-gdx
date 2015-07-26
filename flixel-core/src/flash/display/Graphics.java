package flash.display;

/**
 * The Graphics class contains a set of methods that you can use to create a vector shape.
 * 
 * @author Thomas Weston
 */
public interface Graphics
{
	/**
	 * Clears the graphics that were drawn to this Graphics object, and resets fill and line style settings.
	 */
	public void clear();

	/**
	 * Draws a circle.
	 * 
	 * @param	x		The x location of the center of the circle relative to the registration point of the parent display object (in pixels).
	 * @param	y		The y location of the center of the circle relative to the registration point of the parent display object (in pixels).
	 * @param	radius	The radius of the circle (in pixels).
	 */
	public void drawCircle(float x, float y, float radius);
	
	/**
	 * Draws a rectangle.
	 * 
	 * @param	x		A number indicating the horizontal position relative to the registration point of the parent display object (in pixels).
	 * @param	y		A number indicating the vertical position relative to the registration point of the parent display object (in pixels).
	 * @param	width	The width of the rectangle (in pixels).
	 * @param	height	The height of the rectangle (in pixels).
	 */
	public void drawRect(float x, float y, float width, float height);

	/**
	 * Specifies a line style used for subsequent calls to Graphics methods such as the <code>lineTo()</code> method or the <code>drawCircle()</code> method.
	 * 
	 * @param	thickness	An integer that indicates the thickness of the line in points; valid values are 0-255. If a number is not specified, or if the parameter is undefined, a line is not drawn. If a value of less than 0 is passed, the default is 0. The value 0 indicates hairline thickness; the maximum thickness is 255. If a value greater than 255 is passed, the default is 255.
	 * @param	color		A hexadecimal color value of the line; for example, red is 0xFF0000, blue is 0x0000FF, and so on. If a value is not indicated, the default is 0x000000 (black). Optional.
	 * @param	alpha		A number that indicates the alpha value of the color of the line; valid values are 0 to 1. If a value is not indicated, the default is 1 (solid). If the value is less than 0, the default is 0. If the value is greater than 1, the default is 1.
	 */
	public void lineStyle(float thickness, int color, float alpha);

	/**
	 * Specifies a line style used for subsequent calls to Graphics methods such as the <code>lineTo()</code> method or the <code>drawCircle()</code> method.
	 * 
	 * @param	thickness	An integer that indicates the thickness of the line in points; valid values are 0-255. If a number is not specified, or if the parameter is undefined, a line is not drawn. If a value of less than 0 is passed, the default is 0. The value 0 indicates hairline thickness; the maximum thickness is 255. If a value greater than 255 is passed, the default is 255.
	 * @param	color		A hexadecimal color value of the line; for example, red is 0xFF0000, blue is 0x0000FF, and so on. If a value is not indicated, the default is 0x000000 (black). Optional.
	 */
	public void lineStyle(float thickness, int color);

	/**
	 * Specifies a line style used for subsequent calls to Graphics methods such as the <code>lineTo()</code> method or the <code>drawCircle()</code> method.
	 * 
	 * @param	thickness	An integer that indicates the thickness of the line in points; valid values are 0-255. If a number is not specified, or if the parameter is undefined, a line is not drawn. If a value of less than 0 is passed, the default is 0. The value 0 indicates hairline thickness; the maximum thickness is 255. If a value greater than 255 is passed, the default is 255.
	 */
	public void lineStyle(float thickness);

	/**
	 * Specifies a line style used for subsequent calls to Graphics methods such as the <code>lineTo()</code> method or the <code>drawCircle()</code> method.
	 */
	public void lineStyle();
	
	/**
	 * Draws a line using the current line style from the current drawing position to (x, y); the current drawing position is then set to (x, y).
	 * If you call <code>lineTo()</code> before any calls to the <code>moveTo()</code> method, the default position for the current drawing is (0, 0).
	 * 
	 * @param	x	A number that indicates the horizontal position relative to the registration point of the parent display object (in pixels)
	 * @param	y	A number that indicates the vertical position relative to the registration point of the parent display object (in pixels)
	 */
	public void lineTo(float x, float y);
	
	/**
	 * Moves the current drawing position to (x, y).
	 * 
	 * @param	x	A number that indicates the horizontal position relative to the registration point of the parent display object (in pixels).
	 * @param	y	A number that indicates the vertical position relative to the registration point of the parent display object (in pixels).
	 */
	public void moveTo(float x, float y);
}
