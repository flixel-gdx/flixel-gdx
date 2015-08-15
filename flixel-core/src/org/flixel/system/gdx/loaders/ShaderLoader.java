package org.flixel.system.gdx.loaders;

import org.flixel.FlxG;
import org.flixel.event.IFlxShaderProgram;
import org.flixel.gles20.FlxShaderProgram;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

/**
 * <code>AssetLoader</code> to create a <ProgramShader> from vertex and fragment file.
 * 
 * @author Ka Wing Chin
 */
public class ShaderLoader extends AsynchronousAssetLoader<FlxShaderProgram, ShaderLoader.ShaderProgramParameter>
{
	public ShaderLoader(FileHandleResolver resolver)
	{
		super(resolver);
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, ShaderProgramParameter parameter)
	{
	}

	@Override
	public FlxShaderProgram loadSync(AssetManager manager, String fileName, FileHandle file, ShaderProgramParameter parameter)
	{
		String vertex;
		String fragment;
		if(parameter.vertex.startsWith("org/flixel"))
			vertex = Gdx.files.classpath(parameter.vertex).readString();
		else
			vertex = Gdx.files.internal(parameter.vertex).readString();

		if(parameter.fragment.startsWith("org/flixel"))
			fragment = Gdx.files.classpath(parameter.fragment).readString();
		else
			fragment = Gdx.files.internal(parameter.fragment).readString();

		FlxShaderProgram shader = new FlxShaderProgram(vertex, fragment, parameter.callback);
		FlxG.isShaderCompiled(shader);
		shader.loadShaderSettings();
		return shader;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, ShaderProgramParameter parameter)
	{
		return null;
	}

	/**
	 * Parameter to be passed, in this case the path to the vertex and fragment
	 * files.
	 * 
	 * @author Ka Wing Chin
	 */
	static public class ShaderProgramParameter extends AssetLoaderParameters<FlxShaderProgram>
	{
		/**
		 * The path to the vertex file.
		 */
		public String vertex;
		/**
		 * The path to the fragment file.
		 */
		public String fragment;
		/**
		 * The callback that will be fired on resume.
		 */
		public IFlxShaderProgram callback;
	}
}
