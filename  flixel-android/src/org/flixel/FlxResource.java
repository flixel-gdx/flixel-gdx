package org.flixel;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.drawable.Drawable;
import flash.display.BitmapData;

public class FlxResource
{
	static public Context context = null;
	static public Class<? extends Object> R = null;

	static BitmapData getImage(int resource)
	{
		Drawable image = context.getResources().getDrawable(resource);
		// Bitmap image = BitmapFactory.decodeResource(context.getResources(),
		// resource);
		BitmapData bitmapData = new BitmapData(image.getIntrinsicWidth(), image.getIntrinsicHeight());
		image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
		image.draw(bitmapData.getCanvas());
		return bitmapData;
	}

	@SuppressWarnings("unchecked")
	static protected int getResource(String resourceClass, String name)
	{
		try
		{
			Class[] classes = R.getDeclaredClasses();
			for(Class declaredClass : classes)
			{
				if(declaredClass.getSimpleName().equals(resourceClass))
				{
					Field field = declaredClass.getDeclaredField(name);
					return field.getInt(null);
				}
			}

		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}

		return -1;
	}

	static public int getDrawableResource(String name)
	{
		return getResource("drawable", name);
	}

	static public int getRawResource(String name)
	{
		return getResource("raw", name);
	}
}
