package flash.display;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import flash.geom.ColorTransform;
import flash.geom.Matrix;
import flash.geom.Point;
import flash.geom.Rectangle;

public class BitmapData
{
	public int width = 0;
	public int height = 0;

	protected Canvas canvas = null;
	protected Bitmap bitmap = null;

	
	
	protected void constructor(int width, int height, boolean transparent, int color)
	{
		bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		canvas.drawColor(color, PorterDuff.Mode.SRC);
		updateVariables();
	}

	public BitmapData(int width, int height, boolean transparent, int color)
	{
		constructor(width, height, transparent, color);
	}

	public BitmapData(int width, int height)
	{
		constructor(width, height, true, 0x00000000);
	}

	public BitmapData(Bitmap bitmap)
	{
		this.bitmap = bitmap;
		canvas = new Canvas(bitmap);
		updateVariables();
	}



	protected void updateVariables()
	{
		width = bitmap.getWidth();
		height = bitmap.getHeight();
	}

	public void copyPixels(BitmapData sourceBitmapData, Rectangle sourceRect, Point destPoint, BitmapData alphaBitmapData, Point alphaPoint, boolean mergeAlpha)
	{
		Rect source = new Rect((int) sourceRect.left, (int) sourceRect.top, (int) sourceRect.right,	(int) sourceRect.bottom);
		Rect dest = new Rect((int) destPoint.x, (int) destPoint.y, (int) destPoint.x + (int) sourceRect.getWidth(),	(int) destPoint.y + (int) sourceRect.getHeight());
		
		Paint paint = null;
		
		canvas.drawBitmap(sourceBitmapData.getBitmap(), source, dest, paint);
	}

	public void copyPixels(BitmapData sourceBitmapData, Rectangle sourceRect, Point destPoint,
			BitmapData alphaBitmapData, Point alphaPoint)
	{
		copyPixels(sourceBitmapData, sourceRect, destPoint, alphaBitmapData, alphaPoint, false);
	}

	public void copyPixels(BitmapData sourceBitmapData, Rectangle sourceRect, Point destPoint,
			BitmapData alphaBitmapData)
	{
		copyPixels(sourceBitmapData, sourceRect, destPoint, alphaBitmapData, null, false);
	}

	public void copyPixels(BitmapData sourceBitmapData, Rectangle sourceRect, Point destPoint)
	{
		copyPixels(sourceBitmapData, sourceRect, destPoint, null, null, false);
	}

	public void fillRect(Rectangle rect, int color)
	{
		Paint paint = new Paint();
		paint.setColor(color);
		canvas.drawRect(rect, paint);
	}

	public void clearBitmap()
	{
		canvas.drawColor(0, PorterDuff.Mode.SRC);
	}

	public boolean hitTest(Point point, int i, Point point2)
	{
		return false;
	}

	public void draw(BitmapData source, Matrix matrix, ColorTransform colorTransform, String blendMode,	Rectangle clipRect, Boolean smoothing)
	{
		Paint paint = null;
		if(colorTransform != null)
		{
			paint = new Paint();
			paint.setARGB(colorTransform.alpha, colorTransform.red, colorTransform.green, colorTransform.blue);
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
		}

		canvas.drawBitmap(source.getBitmap(), matrix, paint);
	}

	public void draw(BitmapData source, Matrix matrix, ColorTransform colorTransform, String blendMode,	Rectangle clipRect)
	{
		draw(source, matrix, colorTransform, blendMode, clipRect, false);
	}

	public void draw(BitmapData source, Matrix matrix, ColorTransform colorTransform, String blendMode)
	{
		draw(source, matrix, colorTransform, blendMode, null, false);
	}

	public void draw(BitmapData source, Matrix matrix, ColorTransform colorTransform)
	{
		draw(source, matrix, colorTransform, null, null, false);
	}

	public void draw(BitmapData source, Matrix matrix)
	{
		draw(source, matrix, null, null, null, false);
	}

	public void draw(BitmapData source)
	{
		draw(source, new Matrix(), null, null, null, false);
	}

	public void colorTransform(Rectangle r, ColorTransform colorTransform)
	{
		Paint paint = new Paint();
		paint.setARGB(colorTransform.alpha, colorTransform.red, colorTransform.green, colorTransform.blue);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
		canvas.drawPaint(paint);
	}

	public Bitmap getBitmap()
	{
		return bitmap;
	}

	public Canvas getCanvas()
	{
		return canvas;
	}

	public Rect getBitmapRect()
	{
		return new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	}

	public int getPixel(int c, int r)
	{
		return bitmap.getPixel(c, r);
	}
}
