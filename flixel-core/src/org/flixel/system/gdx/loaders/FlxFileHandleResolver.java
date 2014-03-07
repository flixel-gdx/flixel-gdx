package org.flixel.system.gdx.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

public class FlxFileHandleResolver implements FileHandleResolver 
{
	@Override
	public FileHandle resolve(String fileName)
	{
		if(fileName.startsWith("org/flixel"))
			return Gdx.files.classpath(fileName);
		else
			return Gdx.files.internal(fileName);
	}

}
