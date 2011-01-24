package flash.geom;

import android.graphics.RectF;



public class Rectangle extends RectF
{
	protected float x;
	protected float y;
	protected float height;
	protected float width;

	public void setWidth(float value)
	{
		this.width = value;
		this.right = this.left + this.width;
	}

	public float getWidth()
	{
		return width;
	}

	public void setHeight(float value)
	{
		this.height = value;
		this.bottom = this.top + this.height;
	}

	public float getHeight()
	{
		return height;
	}

	public void setX(float value)
	{
		this.x = value;
		this.left = this.x;
		this.right = this.left + this.width;
	}

	public float getX()
	{
		return x;
	}

	public void setY(float value)
	{
		this.y = value;
		this.top = this.y;
		this.bottom = this.top + this.height;
	}

	public float getY()
	{
		return y;
	}

	public Rectangle()
	{

	}

	public Rectangle(float x, float y, float width, float height)
	{
		super(x, y, x + width, y + height);
		updateVariables();
	}

	protected void updateVariables()
	{
		this.x = this.left;
		this.y = this.top;
		this.width = this.width();
		this.height = this.height();
	}
}
