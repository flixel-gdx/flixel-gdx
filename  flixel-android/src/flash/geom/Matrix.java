package flash.geom;

public class Matrix	extends android.graphics.Matrix
{
	public void translate(float x, float y)
	{
		this.postTranslate(x, y);
	}

	public void scale(float x, float y)
	{
		this.postScale(x, y);
	}

	public void identity()
	{
		this.reset();
	}

	public void rotate(float d)
	{
		this.postRotate(d);
	}
}
