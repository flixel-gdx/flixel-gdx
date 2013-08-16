package org.flixel.system.gdx.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;

/**
 * <code>AssetLoader</code> to create a <code>BitmapFont</code> from a ttf file. Passing a <code>BitmapFontParameter</code> to
 * <code>AssetManager::load(String, Class, AssetLoaderParameters)</code> allows to specify whether the font
 * should be flipped on the y-axis or not.
 * 
 * @author Thomas Weston
 */
public class FreeTypeFontLoader extends AsynchronousAssetLoader<BitmapFont, BitmapFontParameter> 
{		
	public FreeTypeFontLoader (FileHandleResolver resolver)
	{
		super(resolver);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, BitmapFontParameter parameter)
	{
		throw new RuntimeException("ttf files are not supported in html5 - " + fileName);
	}
	
	@Override
	public BitmapFont loadSync(AssetManager manager, String fileName, FileHandle file, BitmapFontParameter parameter)
	{	
		throw new RuntimeException("ttf files are not supported in html5 - " + fileName);
	}
	
	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, BitmapFontParameter parameter)
	{
		throw new RuntimeException("ttf files are not supported in html5 - " + fileName);
	}
}