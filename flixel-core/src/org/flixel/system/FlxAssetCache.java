package org.flixel.system;

import org.flixel.FlxG;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.ManagedTextureData;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * This class provides an easy way to load and store textures, fonts,
 * sounds and music.
 * 
 * @author Thomas Weston
 */
public class FlxAssetCache 
{
	protected ObjectMap<String, Disposable> _atlases;
	protected ObjectMap<String, TextureRegion> _textureRegions;
	protected ObjectMap<String, Disposable> _sounds;
	protected PixmapPacker _pixmapPacker;
	
	public FlxAssetCache()
	{		
		_atlases = new ObjectMap<String, Disposable>();
		_textureRegions = new ObjectMap<String, TextureRegion>();
		_sounds = new ObjectMap<String, Disposable>();
	}
	
	/**
	 * Loads a <code>TextureRegion</code> from a <code>TextureAtlas</code>.
	 * 
	 * @param Path		The path to the <code>TextureAtlas</code>.
	 * @param Region	The name of the <code>TextureRegion</code>.
	 * @return			The <code>TextureRegion</code> if found.
	 */
	public TextureRegion loadTexture(String Path, String Region)
	{
		String Key = Path + ":" + Region;
		if (!_atlases.containsKey(Key))
		{
			if (!_atlases.containsKey(Path))
			{		
				_atlases.put(Path, new TextureAtlas(getFileHandle(Path)));
			}
			_textureRegions.put(Key, ((TextureAtlas) _atlases.get(Path)).findRegion(Region));
		}
		return loadTexture(Key);
	}
	
	/**
	 * Converts a <code>Pixmap</code> into a <code>TextureRegion</code> and stores it in the cache.
	 * 
	 * @param Key		The key to store the <code>Pixmap</code>.
	 * @param Image		The <code>Pixmap</code> to store.
	 * @param Width		The width of the region.
	 * @param Height	The height of the region.
	 * @return			The stored <code>Pixamp</code>.
	 */
	public TextureRegion loadTexture(String Key, Pixmap Image, int Width, int Height)
	{	
		if (!_textureRegions.containsKey(Key))
		{
			_textureRegions.put(Key, new TextureRegion(new Texture(new ManagedTextureData(Image)), 0, 0, Width, Height));
		}
		return loadTexture(Key);
	}
	
	/**
	 * Loads a stored <code>TextureRegion</code>.
	 * 
	 * @param Key	The Key that was used to store this <code>TextureRegion</code>.
	 * @return
	 */
	public TextureRegion loadTexture(String Key)
	{
		return new TextureRegion(_textureRegions.get(Key));
	}
	
	/**
	 * Whether or not the cache contains a <code>TextureRegion</code> with this key.
	 * 
	 * @param Key	The key to check.
	 * @return		Whether or not the key exists.
	 */
	public boolean containsTexture(String Key)
	{
		return _textureRegions.containsKey(Key);
	}
	
	/**
	 * Whether or not the cache contains this sound.
	 * 
	 * @param Sound		The sound to check.
	 * @return	Whether or not the sound exists.
	 */
	public boolean containsSound(Disposable Sound)
	{
		return _sounds.containsValue(Sound, true);
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
		if (!_atlases.containsKey(Path + Size))
		{
			FreeTypeFontGenerator generator = new FreeTypeFontGenerator(getFileHandle(Path));
			FreeTypeBitmapFontData data = generator.generateData(Size, FreeTypeFontGenerator.DEFAULT_CHARS, true);
			generator.dispose();
			_atlases.put(Path + Size, new BitmapFont(data, data.getTextureRegion(), true));
		}
		
		return (BitmapFont) _atlases.get(Path + Size);
	}
	
	/**
	 * Loads a <code>Sound</code> from a file.
	 * @param Path	The path to the file.
	 * @return		The sound.
	 */
	public Sound loadSound(String Path)
	{
		if (!_sounds.containsKey(Path))
		{
			_sounds.put(Path, Gdx.audio.newSound(getFileHandle(Path)));
		}
		
		return (Sound) _sounds.get(Path);
	}
	
	/**
	 * Loads a <code>Music</code> instance from a file.
	 * @param Path	The path to the file.
	 * @return		The music.
	 */
	public Music loadMusic(String Path)
	{
		if (!_sounds.containsKey(Path))
		{
			_sounds.put(Path, Gdx.audio.newMusic(getFileHandle(Path)));
		}
		
		return (Music) _sounds.get(Path);
	}
	
	/**
	 * Disposes all textures and fonts currently contained in the cache.
	 */
	public void disposeTextures()
	{
		for (Disposable atlas : _atlases.values())
			atlas.dispose();
		
		_atlases.clear();
		_textureRegions.clear();
	}
	
	/**
	 * Disposes all sounds and music currently contained in the cache.
	 */
	public void disposeSounds()
	{
		for (Disposable sound : _sounds.values())
			sound.dispose();
		
		_sounds.clear();
	}
	
	/**
	 * Disposes the specified sound or music and removes it from the cache.
	 * @param Sound
	 */
	public void disposeSound(Disposable Sound)
	{
		String key = _sounds.findKey(Sound, true);
		if (key == null)
			return;
		
		Sound.dispose();
		_sounds.remove(key);	
	}
	
	/**
	 * Disposes the cache.
	 */
	public void dispose()
	{
		clear();
	}
	
	/**
	 * Disposes all assets currently contained in the cache.
	 */
	public void clear()
	{
		disposeTextures();
		disposeSounds();		
	}
	
	/**
	 * Gets the correct <code>FileHandle</code> for the specified path.
	 * 
	 * @param Path	The path.
	 * @return		The file handle.
	 */
	protected FileHandle getFileHandle(String Path)
	{
		if (Path.startsWith("org/flixel"))
			return Gdx.files.classpath(Path);
		else
			return Gdx.files.internal(Path);
	}
}