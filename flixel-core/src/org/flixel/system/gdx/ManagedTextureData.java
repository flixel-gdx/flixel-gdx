package org.flixel.system.gdx;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;

/**
 * This class simply allows us to make dynamic textures managed.
 * 
 * @author Thomas Weston
 */
public class ManagedTextureData extends PixmapTextureData
{
	public ManagedTextureData(Pixmap pixmap)
	{
		super(pixmap, null, false, false);
	}

	@Override
	public boolean isManaged()
	{
		return true;
	}
}
