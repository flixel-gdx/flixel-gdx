package com.badlogic.gdx.assets.loaders.resolvers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

/**
 * A <code>FileHandleResolver</code> for assets stored internally.
 * 
 * @author Thomas Weston
 */
public class UniqueInternalFileHandleResolver implements FileHandleResolver
{
	@Override
	public FileHandle resolve (String fileName)
	{    	   	
		if (fileName.contains(":"))
			fileName = fileName.split(":")[0];
        return Gdx.files.internal(fileName);
	}
}