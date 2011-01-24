package flash.geom;

public class ColorTransform 
{
	public int red = 0;
	public int green = 0;
	public int blue = 0;
	public int alpha = 0;
	
	public ColorTransform(float r, float g, float b, float a) 
	{
		red = (int) (r * 255);
		green = (int) (g * 255);
		blue = (int) (b * 255);
		alpha = (int) (a * 255);
	}

}
