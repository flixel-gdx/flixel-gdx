package org.flixel.system;

import org.flixel.system.gdx.ManagedTextureData;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
	protected ObjectMap<String, Texture> _textures;
	protected ObjectMap<String, TextureAtlas> _atlases;
	protected ObjectMap<String, BitmapFont> _fonts;
	protected ObjectMap<String, Disposable> _sounds;
	
	public FlxAssetCache()
	{		
		_textures = new ObjectMap<String, Texture>();
		_atlases = new ObjectMap<String, TextureAtlas>();
		_fonts = new ObjectMap<String, BitmapFont>();
		_sounds = new ObjectMap<String, Disposable>();
	}
	
	/**
	 * Loads a <code>Texture</code> from an image file.
	 * 
	 * @param Path		The path to the image file.
	 * @return			The <code>Texture</code> if found.
	 */
	public Texture loadTexture(String Path)
	{
		if (!_textures.containsKey(Path))
			_textures.put(Path, new Texture(getFileHandle(Path)));
		
		return _textures.get(Path);
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
		if (!_textures.containsKey(Key))
		{
			_textures.put(Key, new Texture(new ManagedTextureData(Image)));
		}
		return _textures.get(Key);
	}
	
	/**
	 * Disposes a <code>Texture</code> and removes it from the cache.
	 * 
	 * @param Path		The path to the image file.
	 */
	public void disposeTexture(String Path)
	{
		Texture texture = _textures.remove(Path);
		
		if(texture != null)
			texture.dispose();
	}
	
	/**
	 * Loads a <code>TextureAtlas</code> from a pack file.
	 * 
	 * @param Path		The path to the pack file.
	 * @return			The <code>TextureAtlas</code> if found.
	 */
	public TextureAtlas loadTextureAtlas(String Path)
	{
		if (!_atlases.containsKey(Path))
			_atlases.put(Path, new TextureAtlas(getFileHandle(Path)));
		
		return _atlases.get(Path);
	}
	
	/**
	 * Disposes a <code>TextureAtlas</code> and removes it from the cache.
	 * 
	 * @param Path
	 */
	public void disposeTextureAtlas(String Path)
	{
		TextureAtlas atlas = _atlases.get(Path);
		
		if (atlas != null)
			atlas.dispose();
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
		return _textures.get(Key);
	}
	
	/**
	 * Whether or not the cache contains a <code>TextureRegion</code> with this key.
	 * 
	 * @param Key	The key to check.
	 * @return		Whether or not the key exists.
	 */
	public boolean containsTexture(String Key)
	{
		return _textures.containsKey(Key);
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
		if (!_fonts.containsKey(Path + Size))
		{
			FreeTypeFontGenerator generator = new FreeTypeFontGenerator(getFileHandle(Path));
			FreeTypeBitmapFontData data = generator.generateData(Size, FreeTypeFontGenerator.DEFAULT_CHARS, true);
			generator.dispose();
			_fonts.put(Path + Size, new BitmapFont(data, data.getTextureRegion(), true));
		}
		
		return _fonts.get(Path + Size);
	}
	
	/**
	 * Disposes a font and removes it from the cache.
	 * 
	 * @param Path	The path to the font file.
	 * @param Size	The size of the font.
	 */
	public void disposeFont(String Path, int Size)
	{
		BitmapFont font = _fonts.remove(Path + Size);
		
		if (font != null)
			font.dispose();
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
	 * Disposes the specified sound or music and removes it from the cache.
	 * @param Path
	 */
	public void disposeSound(String Path)
	{
		Disposable sound = _sounds.remove(Path);
		
		if (sound != null)
			sound.dispose();
	}
	
	/**
	 * Disposes all textures currently contained in the cache.
	 */
	public void disposeTextures()
	{
		for (Texture texture : _textures.values())
			texture.dispose();
		
		_textures.clear();
	}
	
	/**
	 * Disposes all texture atlases currently contained in the cache.
	 */
	public void disposeTextureAtlases()
	{
		for (TextureAtlas atlas : _atlases.values())
			atlas.dispose();
		
		_atlases.clear();
	}
	
	/**
	 * Disposes all texture fonts currently contained in the cache.
	 */
	public void disposeFonts()
	{
		for (BitmapFont font : _fonts.values())
			font.dispose();
		
		_fonts.clear();
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
	 * Disposes the cache.
	 */
	public void dispose()
	{
		clear();
		_textures = null;
		_atlases = null;
		_fonts = null;
		_sounds = null;
	}
	
	/**
	 * Disposes all assets currently contained in the cache.
	 */
	public void clear()
	{
		disposeTextures();
		disposeTextureAtlases();
		disposeFonts();
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