package org.flixel.gles20;

import org.flixel.event.IFlxShaderProgram;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * A shader program encapsulates a vertex and fragment shader pair linked to
 * form a shader program usable with OpenGL ES 2.0.
 * It restores the shader settings when the context is lost.
 * 
 * @author Ka Wing Chin
 */
public class FlxShaderProgram extends ShaderProgram
{
	/**
	 * The callback that will be fired when the game resume.
	 */
	public IFlxShaderProgram callback;

	/**
	 * Creates a <code>ManagedShaderData</code> object.
	 * 
	 * @param	VertexShader	The path to the vertexShader.
	 * @param	FragmentShader	The path to the fragmentShader.
	 * @param	Callback		The callback that will be used on resume.
	 */
	public FlxShaderProgram(String VertexShader, String FragmentShader, IFlxShaderProgram Callback)
	{
		super(VertexShader, FragmentShader);
		callback = Callback;
	}

	/**
	 * Creates a <code>ManagedShaderData</code> object.
	 * 
	 * @param	VertexShader	The path to the vertexShader.
	 * @param	FragmentShader	The path to the fragmentShader.
	 * @param	Callback		The callback that will be used on resume.
	 */
	public FlxShaderProgram(FileHandle VertexShader, FileHandle FragmentShader, IFlxShaderProgram Callback)
	{
		this(VertexShader.readString(), FragmentShader.readString(), Callback);
	}

	/**
	 * Load the shader settings.
	 */
	public void loadShaderSettings()
	{
		if(callback != null)
			callback.loadShaderSettings(this);
	}

	@Override
	public void dispose()
	{
		super.dispose();
		callback = null;
	}
}
