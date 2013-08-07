package org.flixel.system.gdx;

import com.badlogic.gdx.Gdx;
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
 * <code>AssetLoader</code> to create a <code>BitmapFont</code> from a ttf file. Passing a <code>BitmapFontParameter</code> to
 * <code>AssetManager::load(String, Class, AssetLoaderParameters)</code> allows to specify whether the font
 * should be flipped on the y-axis or not.
 * 
 * @author Thomas Weston
 */
public class FreeTypeFontLoader extends AsynchronousAssetLoader<BitmapFont, FreeTypeFontLoader.FreeTypeFontParameter> 
{	
	private BitmapFontLoader _bitmapFontLoader;
	
	public FreeTypeFontLoader (FileHandleResolver resolver)
	{
		super(resolver);
		_bitmapFontLoader = new BitmapFontLoader(resolver);
	}

	@Override
	public BitmapFont loadSync(AssetManager manager, String fileName, FileHandle file, FreeTypeFontParameter parameter)
	{		
		BitmapFont font = _bitmapFontLoader.loadSync(manager, fileName, file, parameter);
		if(parameter == null)
			font.getData().flipped = true;
		return font;
	}
	
	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, FreeTypeFontParameter parameter)
	{
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, FreeTypeFontParameter parameter)
	{
		if (fileName.endsWith(".ttf"))
		{
			Gdx.app.log("flixel", "TrueTypeFonts not supported in HTML5: " + fileName);
			fileName = "org/flixel/data/font/nokiafc22.fnt";
		}
		
		return _bitmapFontLoader.getDependencies(fileName, file, parameter);
	}

	static public class FreeTypeFontParameter extends BitmapFontParameter 
	{
        /** Which characters to create in the font **/
        public String characters = null;
	}
}