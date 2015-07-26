package org.flixel.system;

import org.flixel.gles20.FlxShaderProgram;
import org.flixel.system.gdx.loaders.FlxFileHandleResolver;
import org.flixel.system.gdx.loaders.FontLoader;
import org.flixel.system.gdx.loaders.ShaderLoader;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.ResolutionFileResolver;
import com.badlogic.gdx.assets.loaders.resolvers.ResolutionFileResolver.Resolution;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

/**
 * This class provides an easy way to load and store textures, fonts, sounds, music and shaders.
 * 
 * @author Thomas Weston
 */
public class FlxAssetManager
{
	/**
	 * Internal, the libgdx asset manager.
	 */
	protected AssetManager _assetManager;

	/**
	 * Create a new FlxAssetManager instance.
	 */
	public FlxAssetManager()
	{
		FileHandleResolver resolver = new FlxFileHandleResolver();
		_assetManager = new AssetManager(resolver);
		_assetManager.setLoader(BitmapFont.class, new FontLoader(resolver));
		_assetManager.setLoader(FlxShaderProgram.class, new ShaderLoader(resolver));
	}

	/**
	 * Loads an asset from a file.
	 * 
	 * @param	FileName	The path to the asset file.
	 * @param	Type		The type of asset.
	 * @param	Parameter	Optional parameters for the loader.
	 * 
	 * @return	The asset, if found.
	 */
	public <T> T load(String FileName, Class<T> Type, AssetLoaderParameters<T> Parameter)
	{
		if(!_assetManager.isLoaded(FileName, Type))
		{
			_assetManager.load(FileName, Type, Parameter);
			_assetManager.finishLoading();
		}

		return _assetManager.get(FileName, Type);
	}

	/**
	 * Loads an asset from a file.
	 * 
	 * @param	FileName	The path to the asset file.
	 * @param	Type		The type of asset.
	 * 
	 * @return	The asset, if found.
	 */
	public <T> T load(String FileName, Class<T> Type)
	{
		return load(FileName, Type, null);
	}

	/**
	 * Disposes the asset and removes it from the manager.
	 * 
	 * @param	FileName	The asset to dispose.
	 */
	public void unload(String FileName)
	{
		_assetManager.unload(FileName);
	}

	/**
	 * Add resolutions to the resolver.
	 * 
	 * @param	Resolutions		An array of resolutions.
	 */
	public void addResolutionResolver(Resolution[] Resolutions)
	{
		_assetManager.setLoader(Texture.class, new TextureLoader(new ResolutionFileResolver(new FlxFileHandleResolver(), Resolutions)));
	}

	/**
	 * Whether or not the cache contains an asset with this key.
	 * 
	 * @param	Key		The key to check.
	 * @param	Type	The type of asset.
	 * 
	 * @return	Whether or not the key exists.
	 */
	public <T> boolean containsAsset(String Key, Class<T> Type)
	{
		return _assetManager.isLoaded(Key, Type);
	}

	/**
	 * Whether or not the cache contains an asset with this key.
	 * 
	 * @param	Key		The key to check.
	 * 
	 * @return	Whether or not the key exists.
	 */
	public boolean containsAsset(String Key)
	{
		return _assetManager.isLoaded(Key);
	}

	/**
	 * Disposes all textures that were created at run time i.e. not loaded from an external file.
	 */
	public void disposeRunTimeTextures()
	{
		//Dispose TextureAtlases first, to prevent conflicts with textures that are parts of atlases.
		Array<String> assetNames = _assetManager.getAssetNames();

		//Cycle through all the assets looking for TextureAtlases. If any of an
		// atlas' textures were dynamically created, dispose the whole thing.
		for(String assetName : assetNames)
		{
			if(_assetManager.isLoaded(assetName) && _assetManager.getAssetType(assetName).equals(TextureAtlas.class))
			{
				Array<String> dependencies = _assetManager.getDependencies(assetName);
				boolean dispose = false;
				for(String dependency : dependencies)
				{
					Texture texture = _assetManager.get(dependency, Texture.class);
					TextureData textureData = texture.getTextureData();
					//Quickest way to check if Texture was created at run time or not.
					if(!textureData.disposePixmap())
					{
						dispose = true;

						if(!textureData.isPrepared())
							textureData.prepare();

						textureData.consumePixmap().dispose();
					}
				}
				if(dispose)
					_assetManager.unload(assetName);
			}
		}

		//Now safe to dispose all other managed Textures
		assetNames = _assetManager.getAssetNames();

		for(String assetName : assetNames)
		{
			if(_assetManager.getAssetType(assetName).equals(Texture.class))
			{
				Texture texture = _assetManager.get(assetName, Texture.class);
				TextureData textureData = texture.getTextureData();
				if(!textureData.disposePixmap())
				{
					_assetManager.unload(assetName);

					if(!textureData.isPrepared())
						textureData.prepare();

					textureData.consumePixmap().dispose();
				}
			}
		}
	}

	/**
	 * Disposes all assets of a certain type.
	 */
	public <T> void disposeAssets(Class<T> Type)
	{
		Array<String> assetNames = _assetManager.getAssetNames();

		for(String assetName : assetNames)
		{
			if(_assetManager.getAssetType(assetName).equals(Type))
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
	 * Clears and disposes all assets currently contained in the cache.
	 */
	public void clear()
	{
		_assetManager.clear();
	}

	/**
	 * The number of assets contained in the manager. Useful for debugging.
	 * 
	 * @return	The number of assets.
	 */
	public int getNumberOfAssets()
	{
		return _assetManager.getLoadedAssets();
	}

	/**
	 * Gets the names of all the assets contained in the manager. Useful for debugging.
	 * 
	 * @return	The names of all assets.
	 */
	public Array<String> getNamesOfAssets()
	{
		return _assetManager.getAssetNames();
	}
}
