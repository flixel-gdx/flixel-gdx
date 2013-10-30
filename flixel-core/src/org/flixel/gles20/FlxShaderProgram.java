package org.flixel.gles20;

import org.flixel.event.IFlxShaderProgram;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * A shader program encapsulates a vertex and fragment shader pair linked to form a 
 * shader program usable with OpenGL ES 2.0. It restores the shader settings when the context
 * is lost.
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
	 * @param vertexShader		The path to the vertexShader.
	 * @param fragmentShader	The path to the fragmentShader.
	 * @param callback			The callback that will be used on resume.
	 */
	public FlxShaderProgram(String vertexShader, String fragmentShader, IFlxShaderProgram callback)
	{
		super(vertexShader, fragmentShader);
		this.callback = callback;
	}

	/**
	 * Creates a <code>ManagedShaderData</code> object.
	 * @param vertexShader		The path to the vertexShader.
	 * @param fragmentShader	The path to the fragmentShader.
	 * @param callback			The callback that will be used on resume.
	 */
	public FlxShaderProgram(FileHandle vertexShader, FileHandle fragmentShader, IFlxShaderProgram callback)
	{
		this(vertexShader.readString(), fragmentShader.readString(), callback);
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
