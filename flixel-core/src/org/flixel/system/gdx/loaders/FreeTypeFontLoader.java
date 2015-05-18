package org.flixel.system.gdx.loaders;

import org.flixel.FlxG;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Array;

/**
 * <code>AssetLoader</code> to create a <code>BitmapFont</code> from a ttf file.
 * Passing a <code>BitmapFontParameter</code> to
 * <code>AssetManager::load(String, Class, AssetLoaderParameters)</code> allows
 * to specify whether the font should be flipped on the y-axis or not.
 * 
 * @author Thomas Weston
 */
public class FreeTypeFontLoader extends AsynchronousAssetLoader<BitmapFont, BitmapFontParameter>
{
	public FreeTypeFontLoader(FileHandleResolver resolver)
	{
		super(resolver);
	}

	@Override
	public BitmapFont loadSync(AssetManager manager, String fileName, FileHandle file, BitmapFontParameter parameter)
	{
		String[] split = fileName.split(":");
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(resolve(split[1]));
		FreeTypeFontParameter param = new FreeTypeFontParameter();
		param.size = Integer.parseInt(split[0]);
		param.flip = (parameter != null) ? parameter.flip : true;
		param.characters = FlxG.freeTypeFontChars;
		FreeTypeBitmapFontData data = generator.generateData(param);
		BitmapFont font = generator.generateFont(param);
		generator.dispose();
		return font;
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, BitmapFontParameter parameter)
	{

	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, BitmapFontParameter parameter)
	{
		return new Array<AssetDescriptor>();
	}
}