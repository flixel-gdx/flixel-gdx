package org.flixel.system;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.DynamicPixmapLoader;
import com.badlogic.gdx.assets.loaders.DynamicPixmapLoader.DynamicPixmapParameter;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.FreeTypeFontLoader;
import com.badlogic.gdx.assets.loaders.FreeTypeFontLoader.FreeTypeFontParameter;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.ManagedTextureData;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FlxAssetCache
{
	protected AssetManager _assetManager;
	
	public FlxAssetCache()
	{
		_assetManager = new AssetManager();
	}
	
	public TextureRegion loadTexture(String Path, String Region)
	{
		if (!_assetManager.isLoaded(Path, TextureAtlas.class))
		{
			FileHandleResolver resolver = getFileHandleResolver(Path);
			_assetManager.setLoader(TextureAtlas.class, new TextureAtlasLoader(resolver));
			_assetManager.setLoader(Texture.class, new TextureLoader(resolver));
			_assetManager.load(Path, TextureAtlas.class);
            _assetManager.finishLoading();
		}
		
		return new TextureRegion(_assetManager.get(Path, TextureAtlas.class).findRegion(Region));
	}
	
	public TextureRegion loadTexture(String Key, Pixmap Image, int Width, int Height)
	{		
		TextureParameter parameter = new TextureParameter();
		parameter.minFilter = parameter.magFilter = TextureFilter.Nearest;
		parameter.textureData = new ManagedTextureData(Image);
        
		_assetManager.load(Key, Texture.class, parameter);
		
		_assetManager.setLoader(Pixmap.class, new DynamicPixmapLoader());
		DynamicPixmapParameter pixmapParameter = new DynamicPixmapParameter();
		pixmapParameter.pixmap = Image;
		_assetManager.load(Key + ":pixmap", Pixmap.class, pixmapParameter);
		
		_assetManager.finishLoading();
		
        return new TextureRegion(_assetManager.get(Key, Texture.class), 0, 0, Width, Height);
	}
	
	public TextureRegion loadTexture(String Key, int Width, int Height)
	{
		return new TextureRegion(_assetManager.get(Key, Texture.class), 0, 0, Width, Height);
	}
	
	public BitmapFont loadFont(String Path, int Size)
	{
		String key = Path + ":" + Size;
		
		if (!_assetManager.isLoaded(key, BitmapFont.class))
		{
			_assetManager.setLoader(BitmapFont.class, new FreeTypeFontLoader(getFileHandleResolver(Path)));
			
			FreeTypeFontParameter parameter = new FreeTypeFontParameter();
            parameter.flip = true;
            
			_assetManager.load(key, BitmapFont.class, parameter);
			_assetManager.finishLoading();
		}
		
		return _assetManager.get(key, BitmapFont.class);
	}
	
	public Sound loadSound(String Path)
	{
		if (!_assetManager.isLoaded(Path, Sound.class))
		{
			_assetManager.load(Path, Sound.class);
			_assetManager.finishLoading();
		}
		
		return _assetManager.get(Path, Sound.class);
	}
	
	public Music loadMusic(String Path)
	{
		if (!_assetManager.isLoaded(Path, Music.class))
		{
			_assetManager.load(Path, Music.class);
			_assetManager.finishLoading();
		}
		
		return _assetManager.get(Path, Music.class);
	}
	
	public void dispose()
	{
		_assetManager.dispose();
	}
	
	public void clear()
	{
		//_assetManager.clear();
	}
	
	public boolean containsTexture(String Key)
	{
		return _assetManager.isLoaded(Key, Texture.class);
	}
	
	public FileHandleResolver getFileHandleResolver(String Path)
	{
		if (Path.startsWith("org/flixel"))
             return new ClasspathFileHandleResolver();
		else
             return new InternalFileHandleResolver();
	}
}

/*package org.flixel.system;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;
import com.badlogic.gdx.utils.ObjectMap;

public class FlxAssetCache 
{
	protected ObjectMap<String, TextureAtlas> _atlases;
	protected ObjectMap<String, TextureRegion> _textureRegions;
	protected ObjectMap<String, BitmapFont> _fonts;
	protected ObjectMap<String, Sound> _sounds;
	protected ObjectMap<String, Music> _music;
	
	protected PixmapPacker _pixmapPacker;
	
	public FlxAssetCache()
	{
		_atlases = new ObjectMap<String, TextureAtlas>();
		_textureRegions = new ObjectMap<String, TextureRegion>();
		_fonts = new ObjectMap<String, BitmapFont>();
		_sounds = new ObjectMap<String, Sound>();
		_music = new ObjectMap<String, Music>();
		
		clear();
		_pixmapPacker = new PixmapPacker(1024, 1024, Format.RGBA8888, 0, false);
		TextureAtlas atlas = new TextureAtlas();
		_atlases.put("dynamic", atlas);
		Gdx.app.setLogLevel(Application.LOG_NONE);
	}
	
	public TextureRegion loadTexture(String Path, String Region)
	{
		String Key = Path + ":" + Region;
		if (!_textureRegions.containsKey(Key))
		{
			if (!_atlases.containsKey(Path))
			{		
				_atlases.put(Path, new TextureAtlas(getFileHandle(Path)));
			}
			_textureRegions.put(Key, _atlases.get(Path).findRegion(Region));
		}
		return loadTexture(Key);
	}
	
	public TextureRegion loadTexture(String Key, Pixmap Image)
	{	
		if (!_textureRegions.containsKey(Key))
		{
			_pixmapPacker.pack(Key, Image);
			_pixmapPacker.updateTextureAtlas(_atlases.get("dynamic"), TextureFilter.Nearest, TextureFilter.Nearest, false);
			_textureRegions.put(Key, _atlases.get("dynamic").findRegion(Key));
		}
		return loadTexture(Key);
	}
	
	public TextureRegion loadTexture(String Key)
	{
		return new TextureRegion(_textureRegions.get(Key));
	}
	
	public boolean containsTexture(String Key)
	{
		return _textureRegions.containsKey(Key);
	}
	
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
	
	public Sound loadSound(String Path)
	{
		if (!_sounds.containsKey(Path))
		{
			_sounds.put(Path, Gdx.audio.newSound(getFileHandle(Path)));
		}
		
		return _sounds.get(Path);
	}
	
	public Music loadMusic(String Path)
	{
		if (!_music.containsKey(Path))
		{
			_music.put(Path, Gdx.audio.newMusic(getFileHandle(Path)));
		}
		
		return _music.get(Path);
	}
	
	public void disposeTextures()
	{
		for (TextureAtlas atlas : _atlases.values())
			atlas.dispose();
		
		//_atlases.clear();
		
		_textureRegions.clear();
		
		//if (_pixmapPacker != null)
			//_pixmapPacker.dispose();
	}
	
	public void disposeFonts()
	{
		for (BitmapFont font : _fonts.values())
			font.dispose();
		
		_fonts.clear();
	}
	
	public void disposeSounds()
	{
		for (Sound sound : _sounds.values())
			sound.dispose();
		
		_sounds.clear();
	}
	
	public void disposeSound(Sound sound)
	{
		String key = _sounds.findKey(sound, true);
		if (key == null)
			return;
		
		sound.dispose();
		_sounds.remove(key);
	}
	
	public void disposeMusic()
	{
		for (Music music : _music.values())
			music.dispose();
		
		_music.clear();
	}
	
	public void disposeMusic(Music music)
	{
		String key = _music.findKey(music, true);
		if (key == null)
			return;
		
		music.dispose();
		_music.remove(key);
	}
	
	public void dispose()
	{
		disposeTextures();
		disposeFonts();
		disposeSounds();
		disposeMusic();
		
		_pixmapPacker = new PixmapPacker(1024, 1024, Format.RGBA8888, 0, false);
		TextureAtlas atlas = new TextureAtlas();
		_atlases.put("dynamic", atlas);
	}
	
	public void clear()
	{
		dispose();
		
		
	}
	
	protected FileHandle getFileHandle(String Path)
	{
		if (Path.startsWith("org/flixel"))
			return Gdx.files.classpath(Path);
		else
			return Gdx.files.internal(Path);
	}
}
*/