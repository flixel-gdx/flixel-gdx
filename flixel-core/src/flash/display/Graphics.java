package flash.display;

import org.flixel.FlxU;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

/**
 * This class just wraps the libgdx <code>ShapeRenderer</code> to make it look more like
 * the Flash graphics API. If you need access to the actual <code>ShapeRenderer</code>, use
 * the <code>getShapeRenderer</code> method.
 * 
 * @author Thomas Weston
 */
public class Graphics 
{	
	/**
	 * The <code>ShapeRenderer</code> instance.
	 */
	private ShapeRenderer _shapeRenderer;
	
	/**
	 * The current drawing position.
	 */
	private Vector2 _drawingPosition;
	
	/**
	 * Create a new <code>Graphics</code> object.
	 */
	public Graphics()
	{
		_shapeRenderer = new ShapeRenderer(10000);	
		_drawingPosition = new Vector2();
	}
	
	/**
	 * Clears the graphics that were drawn to this Graphics object,
	 * and resets fill and line style settings.
	 */
	public void clear()
	{
		lineStyle();
	}
	
	/**
	 * Moves the current drawing position to (x, y).
	 * 
	 * @param x			A number that indicates the horizontal position relative to the registration point of the parent display object (in pixels).
	 * @param y			A number that indicates the vertical position relative to the registration point of the parent display object (in pixels).
	 */
	public void moveTo(float x, float y)
	{
		_drawingPosition.set(x, y);
	}
	
	/**
	 * Draws a line using the current line style from the current drawing position to (x, y);
	 * the current drawing position is then set to (x, y). If you call lineTo() before any calls
	 * to the moveTo() method, the default position for the current drawing is (0, 0).
	 * 
	 * @param x			A number that indicates the horizontal position relative to the registration point of the parent display object (in pixels)
	 * @param y			A number that indicates the vertical position relative to the registration point of the parent display object (in pixels)
	 */
	public void lineTo(float x, float y)
	{
		_shapeRenderer.line(_drawingPosition.x, _drawingPosition.y, x, y);
		_drawingPosition.set(x, y);
	}
	
	/**
	 * Draws a rectangle. Set the line style, fill, or both before you call the drawRect() method,
	 * by calling the linestyle(), lineGradientStyle(), beginFill(), beginGradientFill(), or
	 * beginBitmapFill() method.
	 * 
	 * @param x			A number indicating the horizontal position relative to the registration point of the parent display object (in pixels).
	 * @param y			A number indicating the vertical position relative to the registration point of the parent display object (in pixels).
	 * @param width		The width of the rectangle (in pixels).
	 * @param height	The height of the rectangle (in pixels).
	 */
	public void drawRect(float x, float y, float width, float height)
	{
		_shapeRenderer.line(x, y, x + width, y);
		_shapeRenderer.line(x + width, y, x + width, y + height);
		_shapeRenderer.line(x + width, y + height, x, y + height);
		_shapeRenderer.line(x, y + height, x, y);
	}
	
	/**
	 * Specifies a line style used for subsequent calls to Graphics methods such
	 * as the lineTo() method or the drawCircle() method.
	 * 
	 * @param thickness	An integer that indicates the thickness of the line in points; valid values are 0-255. If a number is not specified, or if the parameter is undefined, a line is not drawn. If a value of less than 0 is passed, the default is 0. The value 0 indicates hairline thickness; the maximum thickness is 255. If a value greater than 255 is passed, the default is 255.
	 * @param color		A hexadecimal color value of the line; for example, red is 0xFF0000, blue is 0x0000FF, and so on. If a value is not indicated, the default is 0x000000 (black). Optional.
	 * @param alpha		A number that indicates the alpha value of the color of the line; valid values are 0 to 1. If a value is not indicated, the default is 1 (solid). If the value is less than 0, the default is 0. If the value is greater than 1, the default is 1.
	 */
	public void lineStyle(float thickness, int color, float alpha)
	{
		float[] rgba = FlxU.getRGBA(color);
		Gdx.gl.glLineWidth(thickness);
		_shapeRenderer.setColor(rgba[0] * 0.00392f, rgba[1] * 0.00392f, rgba[2] * 0.00392f, alpha);
	}
	
	/**
	 * Specifies a line style used for subsequent calls to Graphics methods such
	 * as the lineTo() method or the drawCircle() method.
	 * 
	 * @param thickness	An integer that indicates the thickness of the line in points; valid values are 0-255. If a number is not specified, or if the parameter is undefined, a line is not drawn. If a value of less than 0 is passed, the default is 0. The value 0 indicates hairline thickness; the maximum thickness is 255. If a value greater than 255 is passed, the default is 255.
	 * @param color		A hexadecimal color value of the line; for example, red is 0xFF0000, blue is 0x0000FF, and so on. If a value is not indicated, the default is 0x000000 (black). Optional.
	 */
	public void lineStyle(float thickness, int color)
	{
		lineStyle(thickness, color, 1.0f);
	}
	
	/**
	 * Specifies a line style used for subsequent calls to Graphics methods such
	 * as the lineTo() method or the drawCircle() method.
	 * 
	 * @param thickness	An integer that indicates the thickness of the line in points; valid values are 0-255. If a number is not specified, or if the parameter is undefined, a line is not drawn. If a value of less than 0 is passed, the default is 0. The value 0 indicates hairline thickness; the maximum thickness is 255. If a value greater than 255 is passed, the default is 255.
	 */
	public void lineStyle(float thickness)
	{
		lineStyle(thickness, 0, 1.0f);
	}
	
	/**
	 * Specifies a line style used for subsequent calls to Graphics methods such
	 * as the lineTo() method or the drawCircle() method.
	 */
	public void lineStyle()
	{
		lineStyle(0, 0, 1.0f);
	}
	
	/**
	 * Sets the projection matrix to be used for rendering.
	 * 
	 * @param matrix	The matrix to set.
	 */
	public void setProjectionMatrix(Matrix4 matrix)
	{
		_shapeRenderer.setProjectionMatrix(matrix);
	}
	
	/**
	 * Starts a new drawing batch. The call to this method must be paired with a call to end(). 
	 */
	public void begin()
	{
		_shapeRenderer.begin(ShapeType.Line);
	}
	
	/**
	 * Finishes the current drawing batch and ensures it gets rendered.
	 */
	public void end()
	{
		Gdx.gl.glEnable(GL10.GL_BLEND);
		_shapeRenderer.end();
	}
	
	/**
	 * Gets the libgdx <code>ShapeRenderer</code>.
	 * 
	 * @return		The <code>ShapeRenderer</code>.
	 */
	public ShapeRenderer getShapeRenderer()
	{
		return _shapeRenderer;
	}
	
	/**
	 * Dispose this <code>Graphics</code> instance.
	 */
	public void dispose()
	{
		_shapeRenderer.dispose();
		_shapeRenderer = null;
		_drawingPosition = null;
	}
}
