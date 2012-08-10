package com.badlogic.gdx.assets.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;
import com.badlogic.gdx.utils.Array;

/**
 * <code>AssetLoader</code> to create a <code>BitmapFont</code> from a ttf file. Passing a <code>BitmapFontParameter</code> to
 * <code>AssetManager::load(String, Class, AssetLoaderParameters)</code> allows to specify whether the font
 * should be flipped on the y-axis or not.
 * 
 * @author Thomas Weston
 */
public class DynamicPixmapLoader extends SynchronousAssetLoader<Pixmap, DynamicPixmapLoader.DynamicPixmapParameter> 
{	
	FreeTypeBitmapFontData data;
	
	public DynamicPixmapLoader()
	{
		super(null);
	}

	@Override
	public Pixmap load(AssetManager assetManager, String fileName, DynamicPixmapParameter parameter)
	{
		return parameter.pixmap;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, DynamicPixmapParameter parameter)
	{
		return null;
	}
	
	static public class DynamicPixmapParameter extends AssetLoaderParameters<Pixmap> 
	{
        public Pixmap pixmap;
	}
}