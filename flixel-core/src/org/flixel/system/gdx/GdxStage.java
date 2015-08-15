package org.flixel.system.gdx;

import org.flixel.system.gdx.utils.EventPool;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FileTextureData;

import flash.display.Graphics;
import flash.display.Stage;
import flash.events.Event;
import flash.events.EventDispatcher;

/**
 * This class replicates some of the <code>Stage</code> functionality from Flash using libgdx.
 * 
 * @author Thomas Weston
 */
public class GdxStage extends EventDispatcher implements Stage, ApplicationListener
{
	private EventPool _applicationEvents;

	private GdxInput _input;
	private GdxGraphics _graphics;

	private static Pixmap _blankCursor;

	/**
	 * Creates a new <code>Stage</code> with the specified width and height. 
	 */
	public GdxStage()
	{
		_input = new GdxInput(this);
		_graphics = new GdxGraphics();

		_applicationEvents = new EventPool(8);
	}

	@Override
	public int getStageWidth()
	{
		return Gdx.graphics.getWidth();
	}

	@Override
	public int getStageHeight()
	{
		return Gdx.graphics.getHeight();
	}

	@Override
	public Graphics getGraphics()
	{
		return _graphics;
	}

	@Override
	public void create()
	{
		// Enable non power of two textures.
		FileTextureData.copyToPOT = true;

		// Set OpenGL features
		//Gdx.gl.glHint(GL10.PER GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);
		Gdx.gl.glDisable(GL20.GL_DITHER);
		//Gdx.gl.glDisable(GL10.GL_LIGHTING);
		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
		//Gdx.gl.glDisable(GL10.GL_FOG);
		Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);

		_graphics.init();

		Gdx.input.setInputProcessor(_input);

		dispatchEvent(_applicationEvents.obtain(Event.ADDED_TO_STAGE));
	}

	@Override
	public void resize(int width, int height)
	{
		dispatchEvent(_applicationEvents.obtain(Event.RESIZE));
	}

	@Override
	public void render()
	{
		Gdx.gl.glScissor(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0, 0, 0, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		dispatchEvent(_applicationEvents.obtain(Event.ENTER_FRAME));
	}

	@Override
	public void pause()
	{
		dispatchEvent(_applicationEvents.obtain(Event.DEACTIVATE));
	}

	@Override
	public void resume()
	{
		dispatchEvent(_applicationEvents.obtain(Event.ACTIVATE));
	}

	@Override
	public void dispose()
	{
		_graphics.dispose();
		_graphics = null;

		if(_blankCursor != null)
			_blankCursor.dispose();
		_blankCursor = null;

		dispatchEvent(_applicationEvents.obtain(Event.REMOVED_FROM_STAGE));
	}

	public static void hideMouse()
	{
		if(_blankCursor == null)
			_blankCursor = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
		Gdx.input.setCursorImage(_blankCursor, 0, 0);
	}

	public static void showMouse()
	{
		Gdx.input.setCursorImage(null, 0, 0);
	}

	public GdxInput getInput()
	{
		return _input;
	}
}
