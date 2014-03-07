package org.flixel.system.gdx.loaders;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;

/**
 * <code>AssetLoader</code> to create a <code>BitmapFont</code> from a ttf file.
 * Passing a <code>BitmapFontParameter</code> to
 * <code>AssetManager::load(String, Class, AssetLoaderParameters)</code> allows
 * to specify whether the font should be flipped on the y-axis or not.
 * 
 * @author Thomas Weston
 */
public class FontLoader extends AsynchronousAssetLoader<BitmapFont, BitmapFontParameter>
{
	private Map<String, AsynchronousAssetLoader<BitmapFont, BitmapFontParameter>> _loaders;

	public FontLoader(FileHandleResolver resolver)
	{
		super(resolver);
		_loaders = new HashMap<String, AsynchronousAssetLoader<BitmapFont, BitmapFontParameter>>();
		_loaders.put("ttf", new FreeTypeFontLoader(resolver));
		_loaders.put("fnt", new BitmapFontLoader(resolver));
	}

	@Override
	public BitmapFont loadSync(AssetManager manager, String fileName, FileHandle file, BitmapFontParameter parameter)
	{
		return getLoader(file).loadSync(manager, fileName, file, parameter);
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, BitmapFontParameter parameter)
	{
		getLoader(file).loadAsync(manager, fileName, file, parameter);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, BitmapFontParameter parameter)
	{
		return getLoader(file).getDependencies(fileName, file, parameter);
	}

	private AsynchronousAssetLoader<BitmapFont, BitmapFontParameter> getLoader(FileHandle file)
	{
		AsynchronousAssetLoader<BitmapFont, BitmapFontParameter> loader = _loaders.get(file.extension().toLowerCase());
		if(loader == null)
			throw new RuntimeException("Unknown font type: " + file);
		return loader;
	}
}