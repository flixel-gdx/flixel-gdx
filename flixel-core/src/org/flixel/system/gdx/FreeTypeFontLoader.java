package org.flixel.system.gdx;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;
import com.badlogic.gdx.utils.Array;

/**
 * <code>AssetLoader</code> to create a <code>BitmapFont</code> from a ttf file. Passing a <code>BitmapFontParameter</code> to
 * <code>AssetManager::load(String, Class, AssetLoaderParameters)</code> allows to specify whether the font
 * should be flipped on the y-axis or not.
 * 
 * @author Thomas Weston
 */
public class FreeTypeFontLoader extends SynchronousAssetLoader<BitmapFont, FreeTypeFontLoader.FreeTypeFontParameter> 
{	
	public FreeTypeFontLoader (FileHandleResolver resolver)
	{
		super(resolver);
	}

	@Override
	public BitmapFont load(AssetManager assetManager, String fileName, FreeTypeFontParameter parameter)
	{
		if (parameter == null)
			parameter = new FreeTypeFontParameter();
		if (parameter.characters == null)
			parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS;

		String[] split = fileName.split(":");
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(resolve(split[0]));
		FreeTypeBitmapFontData data = generator.generateData(Integer.parseInt(split[1]), parameter.characters, parameter.flip);
		generator.dispose();
		return new BitmapFont(data, data.getTextureRegion(), true);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FreeTypeFontParameter parameter)
	{
		return null;
	}

	static public class FreeTypeFontParameter extends AssetLoaderParameters<BitmapFont> 
	{
        /** Which characters to create in the font **/
        public String characters = null;
        /** Whether the font should be flipped on the y axis or not **/
        public boolean flip = true;
	}
}