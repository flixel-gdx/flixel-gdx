package org.flixel.system;

import org.flixel.system.gdx.FlxFileHandleResolver;
import org.flixel.system.gdx.FreeTypeFontLoader;
import org.flixel.system.gdx.ManagedTextureData;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**
 * This class provides an easy way to load and store textures, fonts,
 * sounds and music.
 * 
 * @author Thomas Weston
 */
public class FlxAssetManager 
{
	protected AssetManager _assetManager;
	
	public FlxAssetManager()
	{	
		FileHandleResolver resolver = new FlxFileHandleResolver();
		_assetManager = new AssetManager(resolver);
		_assetManager.setLoader(BitmapFont.class, new FreeTypeFontLoader(resolver));
	}
	
	/**
	 * Loads a <code>Texture</code> from an image file.
	 * 
	 * @param Path		The path to the image file.
	 * @return			The <code>Texture</code> if found.
	 */
	public Texture loadTexture(String Path)
	{
		if (!_assetManager.isLoaded(Path, Texture.class))
		{
			_assetManager.load(Path, Texture.class);
			_assetManager.finishLoading();
		}
		
		return _assetManager.get(Path, Texture.class);
	}
	
	/**
	 * Converts a <code>Pixmap</code> into a <code>Texture</code> and stores it in the cache.
	 * 
	 * @param Key		The key to store the <code>Pixmap</code>.
	 * @param Image		The <code>Pixmap</code> to store.
	 * @return			The stored <code>Pixamp</code>.
	 */
	public Texture loadTexture(String Key, Pixmap Image)
	{	
		if (!_assetManager.isLoaded(Key, Pixmap.class))
		{
			TextureLoader.TextureParameter parameter = new TextureLoader.TextureParameter();
			parameter.textureData = new ManagedTextureData(Image);
			_assetManager.load(Key, Texture.class, parameter);
			_assetManager.finishLoading();
		}
		
		return _assetManager.get(Key, Texture.class);
	}
	
	/**
	 * Disposes a <code>Texture</code> and removes it from the cache.
	 * 
	 * @param Path		The path to the image file.
	 */
	public void disposeTexture(String Path)
	{
		_assetManager.unload(Path);
	}
	
	/**
	 * Loads a <code>TextureAtlas</code> from a pack file.
	 * 
	 * @param Path		The path to the pack file.
	 * @return			The <code>TextureAtlas</code> if found.
	 */
	public TextureAtlas loadTextureAtlas(String Path)
	{
		if (!_assetManager.isLoaded(Path, TextureAtlas.class))
		{
			_assetManager.load(Path, TextureAtlas.class);
			_assetManager.finishLoading();
		}
		
		return _assetManager.get(Path, TextureAtlas.class);
	}
	
	/**
	 * Disposes a <code>TextureAtlas</code> and removes it from the cache.
	 * 
	 * @param Path
	 */
	public void disposeTextureAtlas(String Path)
	{
		_assetManager.unload(Path);
	}
	
	/**
	 * Loads a <code>TextureRegion</code> from a <code>TextureAtlas</code>.
	 * 
	 * @param Path		The path to the <code>TextureAtlas</code>.
	 * @param Region	The name of the <code>TextureRegion</code>.
	 * @return			The <code>TextureRegion</code> if found.
	 */
	public TextureRegion loadTextureRegion(String Path, String Region)
	{
		return new TextureRegion(loadTextureAtlas(Path).findRegion(Region));
	}
	
	/**
	 * Loads a stored <code>Texture</code>.
	 * 
	 * @param Key	The Key that was used to store this <code>Texture</code>.
	 * @return		The <code>Texture</code>.
	 */
	public Texture getTexture(String Key)
	{
		return _assetManager.get(Key, Texture.class);
	}
	
	/**
	 * Whether or not the cache contains a <code>Texture</code> with this key.
	 * 
	 * @param Key	The key to check.
	 * @return		Whether or not the key exists.
	 */
	public boolean containsTexture(String Key)
	{
		return _assetManager.isLoaded(Key, Texture.class);
	}
	
	/**
	 * Whether or not the cache contains this sound.
	 * 
	 * @param Sound		The sound to check.
	 * @return	Whether or not the sound exists.
	 */
	public boolean containsSound(Disposable Sound)
	{
		return _assetManager.containsAsset(Sound);
	}
	
	/**
	 * Loads a bitmap font.
	 * 
	 * @param Path	The path to the font file.
	 * @param Size	The size of the font.
	 * @return		The font.
	 */
	public BitmapFont loadFont(String Path, int Size)
	{
		String Key = Path + ":" + Size;
		if (!_assetManager.isLoaded(Key))
		{
			_assetManager.load(Key, BitmapFont.class);
			_assetManager.finishLoading();
		}
		
		return _assetManager.get(Key, BitmapFont.class);
	}
	
	/**
	 * Disposes a font and removes it from the cache.
	 * 
	 * @param Path	The path to the font file.
	 * @param Size	The size of the font.
	 */
	public void disposeFont(String Path, int Size)
	{
		_assetManager.unload(Path + ":" + Size);
	}
	
	/**
	 * Loads a <code>Sound</code> from a file.
	 * @param Path	The path to the file.
	 * @return		The sound.
	 */
	public Sound loadSound(String Path)
	{
		if (!_assetManager.isLoaded(Path))
		{
			_assetManager.load(Path, Sound.class);
			_assetManager.finishLoading();
		}
		
		return _assetManager.get(Path, Sound.class);
	}
	
	/**
	 * Loads a <code>Music</code> instance from a file.
	 * @param Path	The path to the file.
	 * @return		The music.
	 */
	public Music loadMusic(String Path)
	{
		if (!_assetManager.isLoaded(Path))
		{
			_assetManager.load(Path, Music.class);
			_assetManager.finishLoading();
		}
		
		return _assetManager.get(Path, Music.class);
	}
	
	/**
	 * Disposes the specified sound or music and removes it from the cache.
	 * @param Sound
	 */
	public void disposeSound(Disposable Sound)
	{
		String fileName = _assetManager.getAssetFileName(Sound);
		if (fileName != null)
			_assetManager.unload(fileName);
	}
	
	/**
	 * Disposes the specified sound or music and removes it from the cache.
	 * @param Path
	 */
	public void disposeSound(String Path)
	{
		_assetManager.unload(Path);
	}
	
	/**
	 * Disposes all run time textures currently contained in the cache.
	 */
	public void disposeTextures()
	{
		// Dispose TextureAtlases first, to prevent conflicts with textures that are parts of atlases.
		Array<String> assetNames = _assetManager.getAssetNames();
	
		// Cycle through all the assets looking for TextureAtlases. If any of an atlas' textures were
		// dynamically created, dispose the whole thing.
		for (String assetName : assetNames)
		{
			if (_assetManager.isLoaded(assetName) && _assetManager.getAssetType(assetName).equals(TextureAtlas.class))
			{
				Array<String> dependencies = _assetManager.getDependencies(assetName);
				for (String dependency : dependencies)
				{
					Texture texture = _assetManager.get(dependency, Texture.class);
					// Quickest way to check if Texture was created at run time or not.
					if (!texture.getTextureData().disposePixmap())
					{
						_assetManager.unload(assetName);
						break;
					}
				}
			}
		}
		
		// Now safe to dispose all other managed Textures
		assetNames = _assetManager.getAssetNames();
		
		for (String assetName : assetNames)
		{
			if (_assetManager.getAssetType(assetName).equals(Texture.class))
			{
				Texture texture = _assetManager.get(assetName, Texture.class);
				if (!texture.getTextureData().disposePixmap())
					_assetManager.unload(assetName);
			}
		}
	}
	
	/**
	 * Disposes all texture fonts currently contained in the cache.
	 */
	public void disposeFonts()
	{
		Array<String> assetNames = _assetManager.getAssetNames();
		
		for (String assetName : assetNames)
		{
			if (_assetManager.getAssetType(assetName).equals(BitmapFont.class))
				_assetManager.unload(assetName);
		}
	}
	
	/**
	 * Disposes all sounds and music currently contained in the cache.
	 */
	public void disposeSounds()
	{
		Array<String> assetNames = _assetManager.getAssetNames();
		
		for (String assetName : assetNames)
		{
			if (_assetManager.getAssetType(assetName).equals(Sound.class) || _assetManager.getAssetType(assetName).equals(Music.class))
				_assetManager.unload(assetName);
		}
	}
	
	/**
	 * Disposes the cache.
	 */
	public void dispose()
	{
		_assetManager.dispose();
	}
	
	/**
	 * Disposes all assets currently contained in the cache.
	 */
	public void clear()
	{
		_assetManager.clear();		
	}
}