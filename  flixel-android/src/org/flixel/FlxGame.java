package org.flixel;

import org.flixel.data.FlxPause;
import org.flixel.data.FlxSplashScreen;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceView;
import flash.display.BitmapData;
import flash.geom.Point;

/**
 * 
 * @author Ka Wing Chin
 *
 */
public class FlxGame
{
	
	/**
	 * Sets 0, -, and + to control the global volume and P to pause.
	 * @default true
	 */
	public boolean useDefaultHotKeys;
	
	/**
	 * Displayed whenever the game is paused.
	 * Override with your own <code>FlxLayer</code> for hot custom pause action!
	 * Defaults to <code>data.FlxPause</code>.
	 */
	public FlxGroup pause;
	
	// startup
	boolean _running;
	
	// basic display stuff
	FlxState _state;
	Canvas _screen;
	BitmapData _buffer;
	int _gameXOffset;
	int _gameYOffset;
	Point _zeroPoint;
	
	// basic update stuff
	float _elapsed;
	long _total;
	boolean _paused;
	
	public SurfaceView stage;
	
	
	
	
	/**
	 * Game object constructor - sets up the basic properties of your game.
	 * 
	 * @param	GameSizeX		The width of your game in pixels (e.g. 320).
	 * @param	GameSizeY		The height of your game in pixels (e.g. 240).
	 * @param	InitialState	The class name of the state you want to create and switch to first (e.g. MenuState).
	 */
	public FlxGame(int GameSizeX, int GameSizeY, Class<? extends FlxState> InitialState, Context context, Class<? extends Object> Resource)
	{
		constructor(GameSizeX, GameSizeY, InitialState, context, Resource);
	}
	
	void constructor(int GameSizeX, int GameSizeY, Class<? extends FlxState> InitialState, Context context, Class<? extends Object> Resource)
	{		
		FlxResource.context = context;
		FlxResource.R = Resource; 
		_running = true;
				
		FlxState.bgColor = 0xff000000;
		FlxG.setGameData(this, GameSizeX, GameSizeY);
		_elapsed = 0;
		_total = 0;
		pause = new FlxPause();
		_state = null;
		FlxG.initialGameState = InitialState;
		_zeroPoint = new Point();
		
		useDefaultHotKeys = true;
		
		_gameXOffset = 0;
		_gameYOffset = 0;
		
		_paused = false;		
		
		create();
	}
	
	
	/**
	 * Makes the little volume tray slide out.
	 * 
	 * @param	Silent	Whether or not it should beep.
	 */
	public void showSoundTray(boolean Silent)
	{
		//if(!Silent)
			//FlxG.play(SndBeep);
		//_soundTrayTimer = 1;
/*		_soundTray.y = _gameYOffset*_zoom;
		_soundTray.visible = true;
		var gv:uint = Math.round(FlxG.volume*10);
		if(FlxG.mute)
			gv = 0;
		for (var i:uint = 0; i < _soundTrayBars.length; i++)
		{
			if(i < gv) _soundTrayBars[i].alpha = 1;
			else _soundTrayBars[i].alpha = 0.5;
		}*/
	}
	
	public void showSoundTray()
	{
		
	}

	/**
	 * Internal event handler for input and focus.
	 */
	protected void onFocus()
	{
		if(FlxG.getPause())
			FlxG.setPause(false);
	}
	
	
	/**
	 * Internal event handler for input and focus.
	 */
	protected void onFocusLost()
	{
		FlxG.setPause(true);
	}
	
	/**
	 * Internal function to help with basic pause game functionality.
	 */
	void unpauseGame()
	{
		FlxG.resetInput();
		_paused = false;
	}
	
	/**
	 * Internal function to help with basic pause game functionality.
	 */
	void pauseGame()
	{			
		_paused = true;
	}
	
	/**
	 * Switch from one <code>FlxState</code> to another.
	 * Usually called from <code>FlxG</code>.
	 * 
	 * @param	flxState		The class name of the state you want (e.g. PlayState)
	 */
	public void switchState(Class<? extends FlxState> State)
	{
		//Basic reset stuff
		//FlxG.panel.hide();
		FlxG.unfollow();		
		FlxG.resetInput();
		FlxG.destroySounds();
//		FlxG.flash.stop();
//		FlxG.fade.stop();
//		FlxG.quake.stop();
		//_screen.x = 0; // canvas
		//_screen.y = 0; // canvas
		
		
		FlxState newState = null;		
		if (State != null)
		{
			try 
			{
				newState = State.newInstance();
			} 
			catch (IllegalAccessException e) 
			{
				e.printStackTrace();
			} 
			catch (InstantiationException e) 
			{
				e.printStackTrace();
			}
		}

		_state = newState;
//		_state.scale(_zoom, _zoom);
		
		//Finally, create the new state
		_state.create();
	}
	
	/**
	 * Internal event handler for input and focus.
	 */
	protected void onKeyDown(int KeyCode)
	{
		Log.i("keyCode", Integer.toString(KeyCode));
		FlxG.keys.handleKeyDown(KeyCode);
	}
	
	
	/**
	 * Internal event handler for input and focus.
	 */
	public void onKeyUp(int KeyCode)
	{
		if(useDefaultHotKeys)
		{
			switch(KeyCode)
			{
				case 48:
				case 96:
					FlxG.setMute(!FlxG.getMute());
//					showSoundTray();
					return;
				case 109:
				case 189:
					//FlxG.mute = false;
		    		//FlxG.volume = FlxG.volume - 0.1;
		    		//showSoundTray();
					return;
				case 107:
				case 187:
					//FlxG.mute = false;
		    		//FlxG.volume = FlxG.volume + 0.1;
		    		//showSoundTray();
					return;
				case 82: // MENU button
					FlxG.setPause(!FlxG.getPause());
				default: break;
			}
		}
		FlxG.keys.handleKeyUp(KeyCode);
	}
	
	
	/**
	 * This is the main game loop.  It controls all the updating and rendering.
	 */
	protected boolean update(Canvas canvas)
	{	
		long mark = System.currentTimeMillis();

		//Frame timing		
		float ems = mark - _total;
		_elapsed = ems / 1000;
		_total = mark;
		FlxG.elapsed = _elapsed;
		if(FlxG.elapsed > FlxG.maxElapsed)
			FlxG.elapsed = FlxG.maxElapsed;
		FlxG.elapsed *= FlxG.timeScale;	
		
		
		
		
		//State updating
		FlxG.updateInput();
		FlxG.updateSounds();
		if(_paused)
			pause.update();
		else
		{
			//Update the camera and game state
			FlxG.doFollow();
			_state.update();
			canvas.drawBitmap(	FlxG.buffer.getBitmap(), 
								FlxG.buffer.getBitmapRect(), 
								canvas.getClipBounds(), 
								null);
			
			//Update the various special effects
//			if(FlxG.flash.exists)
//				FlxG.flash.update();
//			if(FlxG.fade.exists)
//				FlxG.fade.update();
//			FlxG.quake.update();
//			_screen.translate(FlxG.quake.x, FlxG.quake.y); // Changing _screen will only work before the canvas got drawn.
			//Render game content, special fx, and overlays
			_state.preProcess();
			_state.render();
		}
	
				
//		if(FlxG.flash.exists)
//			FlxG.flash.render();
//		if(FlxG.fade.exists)
//			FlxG.fade.render();
//		if(FlxG.panel.visible)
//			FlxG.panel.render();
		
		_state.postProcess();
		
		if(_paused)
		{
			canvas.drawBitmap(	FlxG.buffer.getBitmap(), 
								FlxG.buffer.getBitmapRect(), 
								canvas.getClipBounds(),
								null);
			//Render game content, special fx, and overlays
			_state.preProcess();
			_state.render();
			pause.render();
		}
		return _running;
	}
	
	
	/**
	 * Used to instantiate the guts of flixel once we have a valid pointer to the root.
	 */
	protected void create()
	{
		//Set up the view window and double buffering	
		_screen = new Canvas();
		BitmapData tmp = new BitmapData(FlxG.width, FlxG.height, true, FlxState.bgColor);
		FlxG.buffer = tmp;

		
		//All set!
		switchState(FlxSplashScreen.class); //TODO: put initialGameState if you don't want to see the splash screen.
//		switchState(initialGameState);
		FlxState.screen.unsafeBind(FlxG.buffer);
		
	}
	
	
	public void shutdown(boolean forceSoundShutdown)
	{
		_running = false;
		FlxG.unfollow();
		FlxG.keys.reset();
		if(FlxG.dpad != null)
			FlxG.dpad.hide();
		if(FlxG.music != null)
		{
			FlxG.destroySounds(forceSoundShutdown);
			FlxG.music.stop();			
		}
//		FlxG.flash.stop();
//		FlxG.fade.stop();
//		FlxG.quake.stop();
		
		
		System.gc();		
	}
	
	public void resume()
	{
		_running = true;
		FlxG.unfollow();
		FlxG.keys.reset();
		
//		FlxG.flash.start();
//		FlxG.fade.start();
//		FlxG.quake.start();
	}

	public FlxState getState()
	{
		return _state;
	}
}
