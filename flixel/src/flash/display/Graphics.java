package flash.display;

import javax.microedition.khronos.opengles.GL10;

import org.flixel.FlxU;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class Graphics {
	
	static private ShapeRenderer _shapeRenderer;
	
	private Vector2 _drawingPosition;
	
	public Graphics()
	{
		if (_shapeRenderer == null)
			_shapeRenderer = new ShapeRenderer();
		else
			throw new RuntimeException("An instance of Graphics already exists.");
		
		_drawingPosition = new Vector2();
	}
	
	public void clear()
	{
		
	}
	
	public void moveTo(float x, float y)
	{
		_drawingPosition.set(x, y);
	}
	
	public void lineTo(float x, float y)
	{
		_shapeRenderer.line(_drawingPosition.x, _drawingPosition.y, x, y);
		_drawingPosition.set(x, y);
	}
	
	public void drawRect(float x, float y, float width, float height)
	{
		_shapeRenderer.line(x, y, x + width, y);
		_shapeRenderer.line(x + width, y, x + width, y + height);
		_shapeRenderer.line(x + width, y + height, x, y + height);
		_shapeRenderer.line(x, y + height, x, y);
	}
	
	public void lineStyle(float thickness, int color, float alpha)
	{
		float[] rgba = FlxU.getRGBA(color);
		_shapeRenderer.setColor(rgba[0], rgba[1], rgba[2], alpha);
	}
	
	public void lineStyle(float thickness, int color)
	{
		lineStyle(thickness, color, 1.0f);
	}
	
	public void lineStyle(float thickness)
	{
		lineStyle(thickness, 0, 1.0f);
	}
	
	public void lineStyle()
	{
		lineStyle(0, 0, 1.0f);
	}
	
	public void setProjectionMatrix(Matrix4 matrix)
	{
		_shapeRenderer.setProjectionMatrix(matrix);
	}
	
	public void begin()
	{
		_shapeRenderer.begin(ShapeType.Line);
	}
	
	public void end()
	{
		Gdx.gl.glEnable(GL10.GL_BLEND);
		_shapeRenderer.end();
	}
	
	public ShapeRenderer getShapeRenderer()
	{
		return _shapeRenderer;
	}
	
	public void dispose()
	{
		_shapeRenderer.dispose();
		_shapeRenderer = null;
		_drawingPosition = null;
	}
}
