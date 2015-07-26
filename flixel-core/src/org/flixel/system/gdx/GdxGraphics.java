package org.flixel.system.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

import flash.display.Graphics;

/**
 * This class just wraps the libgdx <code>ShapeRenderer</code> to make it look
 * more like the Flash graphics API. If you need access to the actual
 * <code>ShapeRenderer</code>, use the <code>getShapeRenderer</code> method.
 * 
 * @author Thomas Weston
 */
public class GdxGraphics implements Graphics
{
	/**
	 * The <code>ShapeRenderer</code> instance.
	 */
	private ShapeRenderer _shapeRenderer;

	/**
	 * The current drawing position.
	 */
	private Vector2 _drawingPosition;

	public void init()
	{
		_shapeRenderer = new ShapeRenderer(10000);
		_drawingPosition = new Vector2();
	}

	@Override
	public void clear()
	{
		lineStyle();
	}

	@Override
	public void moveTo(float x, float y)
	{
		_drawingPosition.set(x, y);
	}

	@Override
	public void lineTo(float x, float y)
	{
		_shapeRenderer.line(_drawingPosition.x, _drawingPosition.y, x, y);
		_drawingPosition.set(x, y);
	}

	@Override
	public void drawCircle(float x, float y, float radius)
	{
		_shapeRenderer.circle(x, y, radius, 360);
	}

	@Override
	public void drawRect(float x, float y, float width, float height)
	{
		_shapeRenderer.rect(x, y, width, height);
	}

	@Override
	public void lineStyle(float thickness, int color, float alpha)
	{
		Gdx.gl.glLineWidth(thickness);
		_shapeRenderer.setColor(((color >> 16) & 0xFF) * 0.00392f, ((color >> 8) & 0xFF) * 0.00392f, (color & 0xFF) * 0.00392f, alpha);
	}

	@Override
	public void lineStyle(float thickness, int color)
	{
		lineStyle(thickness, color, 1.0f);
	}

	@Override
	public void lineStyle(float thickness)
	{
		lineStyle(thickness, 0, 1.0f);
	}

	@Override
	public void lineStyle()
	{
		lineStyle(0, 0, 1.0f);
	}

	/**
	 * Sets the projection matrix to be used for rendering.
	 * 
	 * @param	matrix	The matrix to set.
	 */
	public void setProjectionMatrix(Matrix4 matrix)
	{
		_shapeRenderer.setProjectionMatrix(matrix);
	}

	/**
	 * Starts a new drawing batch.
	 * The call to this method must be paired with a call to end().
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
		Gdx.gl.glEnable(GL20.GL_BLEND);
		_shapeRenderer.end();
	}

	/**
	 * Gets the libgdx <code>ShapeRenderer</code>.
	 * 
	 * @return	The <code>ShapeRenderer</code>.
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
