package org.flixel;

import android.graphics.Canvas;
import android.view.SurfaceHolder;


/**
 * This should not be created except by FlxGameView. It should be an inner class actually.
 */
public class FlxThread extends Thread
{
	private SurfaceHolder _surfaceHolder;
	private FlxGameView _gameView;
	private FlxGame _game;
	private boolean _run = false;

	public FlxThread(SurfaceHolder surfaceHolder, FlxGameView gameView, FlxGame game)
	{
		_surfaceHolder = surfaceHolder;
		_gameView = gameView;
		_game = game;

	}

	/**
	 * Runs the thread.
	 * @param run
	 */
	public void setRunning(Boolean run)
	{
		_run = run;
	}

	@Override
	public void run()
	{		
		Canvas c;
		while(_run)
		{
			c = null;
			try
			{
				c = _surfaceHolder.lockCanvas(null);
				synchronized(_surfaceHolder)
				{
					if(!_game.update(c))
					{		
						_run = false;
						_gameView.shutdown();
					}
				}
			}
			finally
			{
				// do this in a finally so that if an exception is thrown
				// during the above, we don't leave the Surface in an
				// inconsistent state
				if(c != null)
				{
					_surfaceHolder.unlockCanvasAndPost(c);
				}
			}
		}
	}
	
	
	/**
	 * Get the SurfaceHolder
	 * @return SurfaceHolder
	 */
	public SurfaceHolder getSurfaceHolder()
	{
		return _surfaceHolder;
	}
}
