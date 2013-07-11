package org.flixel;

import org.flixel.event.IFlxReplay;
import org.flixel.plugin.TimerManager;
import org.flixel.system.FlxDebugger;
import org.flixel.system.FlxReplay;
import org.flixel.system.FlxSplashScreen;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;

import flash.display.Graphics;
import flash.display.Stage;
import flash.events.MouseEvent;

/**
 * FlxGame is the heart of all flixel games, and contains a bunch of basic game loops and things.
 * It is a long and sloppy file that you shouldn't have to worry about too much!
 * It is basically only used to create your game object in the first place,
 * after that FlxG and FlxState have all the useful stuff you actually need.
 * 
 * @author	Ka Wing Chin
 * @author	Thomas Weston
 */
public class FlxGame implements ApplicationListener, InputProcessor
{	
	/**
	 * Sets 0, -, and + to control the global volume sound volume.
	 * @default true
	 */
	public boolean useSoundHotKeys;
	/**
	 * Tells flixel to use the default system mouse cursor instead of custom Flixel mouse cursors.
	 * @default false
	 */
	public boolean useSystemCursor;
	/**
	 * Initialize and allow the flixel debugger overlay even in release mode.
	 * Also useful if you don't use FlxPreloader!
	 * @default false
	 */
	public boolean forceDebugger;
	
	/**
	 * Current game state.
	 */
	FlxState _state;
	/**
	 * Mouse cursor.
	 */
	TextureRegion _mouse;
	
	/**
	 * Class type of the initial/first game state for the game, usually MenuState or something like that.
	 */
	Class<? extends FlxState> _iState;
	/**
	 * Whether the game object's basic initialization has finished yet.
	 */
	protected boolean _created;
	
	/**
	 * Total number of milliseconds elapsed since game start.
	 */
	protected long _total;
	/**
	 * Total number of milliseconds elapsed since last update loop.
	 * Counts down as we step through the game loop.
	 */
	protected int _accumulator;
	/**
	 * Whether the Flash player lost focus.
	 */
	protected boolean _lostFocus;
	/**
	 * Milliseconds of time per step of the game loop.  FlashEvent.g. 60 fps = 16ms.
	 */	
	int _step;
	/**
	 * Framerate of the Flash player (NOT the game loop). Default = 30.
	 */
	int _flashFramerate;
	/**
	 * Max allowable accumulation (see _accumulator).
	 * Should always (and automatically) be set to roughly 2x the flash player framerate.
	 */
	int _maxAccumulation;
	/**
	 * If a state change was requested, the new state object is stored here until we switch to it.
	 */
	FlxState _requestedState;
	/**
	 * A flag for keeping track of whether a game reset was requested or not.
	 */
	boolean _requestedReset;
	
	/**
	 * The "focus lost" screen (see <code>createFocusScreen()</code>).
	 */
	protected TextureRegion _focus;
	/**
	 * The sound tray display container (see <code>createSoundTray()</code>).
	 */
	protected FlxSprite _soundTray;
	/**
	 * Helps us auto-hide the sound tray after a volume change.
	 */
	protected float _soundTrayTimer;
	/**
	 * Helps display the volume bars on the sound tray.
	 */
	protected Array<FlxSprite> _soundTrayBars;
	/**
	 * The debugger overlay object.
	 */
	FlxDebugger _debugger;
	/**
	 * A handy boolean that keeps track of whether the debugger exists and is currently visible.
	 */
	boolean _debuggerUp;
	
	/**
	 * Container for a game replay object.
	 */
	FlxReplay _replay;
	/**
	 * Flag for whether a playback of a recording was requested.
	 */
	boolean _replayRequested;
	/**
	 * Flag for whether a new recording was requested.
	 */
	boolean _recordingRequested;
	/**
	 * Flag for whether a replay is currently playing.
	 */
	boolean _replaying;
	/**
	 * Flag for whether a new recording is being made.
	 */
	boolean _recording;
	/**
	 * Array that keeps track of keypresses that can cancel a replay.
	 * Handy for skipping cutscenes or getting out of attract modes!
	 */
	Array<String> _replayCancelKeys;
	/**
	 * Helps time out a replay if necessary.
	 */
	int _replayTimer;
	/**
	 * This function, if set, is triggered when the callback stops playing.
	 */
	IFlxReplay _replayCallback;
	
	/**
	 * Temporary font to display the fps.
	 */
	private BitmapFont _font;
	/**
	 * Temporary text buffer for the fps.
	 */
	private StringBuffer _stringBuffer;
	/**
	 * Temporary camera to display the fps.
	 */
	private OrthographicCamera _fontCamera;
	
	/**
	 * Represents the Flash stage.
	 */
	public Stage stage;
	/**
	 * Internal, a pre-allocated <code>MouseEvent</code> to prevent <code>new</code> calls.
	 */
	private MouseEvent _mouseEvent;
	
	protected boolean showSplashScreen = false;
	private boolean _splashScreenShown = false;
		
	/**
	 * Instantiate a new game object.
	 * 
	 * @param	GameSizeX		The width of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	GameSizeY		The height of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	InitialState	The class name of the state you want to create and switch to first (e.g. MenuState).
	 * @param	Zoom			The default level of zoom for the game's cameras (e.g. 2 = all pixels are now drawn at 2x).  Default = 1.
	 * @param	GameFramerate	How frequently the game should update (default is 30 times per second).
	 * @param	FlashFramerate	Sets the actual display framerate for Flash player (default is 30 times per second).
	 * @param	UseSystemCursor	Whether to use the default OS mouse pointer, or to use custom flixel ones.
	 * @param	ScaleMode		How to scale the stage to fit the display (default is stretch).
	 */
	public FlxGame(int GameSizeX, int GameSizeY, Class<? extends FlxState> InitialState, float Zoom, int GameFramerate, int FlashFramerate, boolean UseSystemCursor, int ScaleMode)
	{
		//super high priority init stuff (focus, mouse, etc)
		_lostFocus = false;
		
		// basic display and update setup stuff
		FlxG.init(this, GameSizeX, GameSizeY, Zoom, ScaleMode);
		FlxG.setFramerate(GameFramerate);
		FlxG.setFlashFramerate(FlashFramerate);
			
		stage = new Stage((int)(GameSizeX * Zoom), (int)(GameSizeY * Zoom));
		
		_accumulator = (int) _step;
		_total = 0;
		_state = null;
		useSoundHotKeys = true;
		useSystemCursor = UseSystemCursor;
		//if(!useSystemCursor)
			//Gdx.input.setCursorCatched(true);
		forceDebugger = false;
		_debuggerUp = false;
		
		//replay data
		_replay = new FlxReplay();
		_replayRequested = false;
		_recordingRequested = false;
		_replaying = false;
		_recording = false;
		
		// then get ready to create the game object for real;
		_iState = InitialState;
		_requestedState = null;
		_requestedReset = true;
		_created = false;
	}
	
	/**
	 * Instantiate a new game object.
	 * 
	 * @param	GameSizeX		The width of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	GameSizeY		The height of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	InitialState	The class name of the state you want to create and switch to first (e.g. MenuState).
	 * @param	Zoom			The default level of zoom for the game's cameras (e.g. 2 = all pixels are now drawn at 2x).  Default = 1.
	 * @param	GameFramerate	How frequently the game should update (default is 30 times per second).
	 * @param	FlashFramerate	Sets the actual display framerate for Flash player (default is 30 times per second).
	 * @param	UseSystemCursor	Whether to use the default OS mouse pointer, or to use custom flixel ones.
	 */
	public FlxGame(int GameSizeX, int GameSizeY, Class<? extends FlxState> InitialState, float Zoom, int GameFramerate, int FlashFramerate, boolean UseSystemCursor)
	{
		this(GameSizeX, GameSizeY, InitialState, Zoom, GameFramerate, FlashFramerate, UseSystemCursor, FlxCamera.STRETCH);
	}
	
	/**
	 * Instantiate a new game object.
	 * 
	 * @param	GameSizeX		The width of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	GameSizeY		The height of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	InitialState	The class name of the state you want to create and switch to first (e.g. MenuState).
	 * @param	Zoom			The default level of zoom for the game's cameras (e.g. 2 = all pixels are now drawn at 2x).  Default = 1.
	 * @param	GameFramerate	How frequently the game should update (default is 30 times per second).
	 * @param	FlashFramerate	Sets the actual display framerate for Flash player (default is 30 times per second).
	 */
	public FlxGame(int GameSizeX, int GameSizeY, Class<? extends FlxState> InitialState, float Zoom, int GameFramerate, int FlashFramerate)
	{
		this(GameSizeX, GameSizeY, InitialState, Zoom, GameFramerate, FlashFramerate, false, FlxCamera.STRETCH);
	}
	
	/**
	 * Instantiate a new game object.
	 * 
	 * @param	GameSizeX		The width of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	GameSizeY		The height of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	InitialState	The class name of the state you want to create and switch to first (e.g. MenuState).
	 * @param	Zoom			The default level of zoom for the game's cameras (e.g. 2 = all pixels are now drawn at 2x).  Default = 1.
	 * @param	GameFramerate	How frequently the game should update (default is 30 times per second).
	 */
	public FlxGame(int GameSizeX, int GameSizeY, Class<? extends FlxState> InitialState, float Zoom, int GameFramerate)
	{
		this(GameSizeX, GameSizeY, InitialState, Zoom, GameFramerate, 30, false, FlxCamera.STRETCH);
	}
	
	/**
	 * Instantiate a new game object.
	 * 
	 * @param	GameSizeX		The width of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	GameSizeY		The height of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	InitialState	The class name of the state you want to create and switch to first (e.g. MenuState).
	 * @param	Zoom			The default level of zoom for the game's cameras (e.g. 2 = all pixels are now drawn at 2x).  Default = 1.
	 */
	public FlxGame(int GameSizeX, int GameSizeY, Class<? extends FlxState> InitialState, float Zoom)
	{
		this(GameSizeX, GameSizeY, InitialState, Zoom, 30, 30, false, FlxCamera.STRETCH);
	}
	
	/**
	 * Instantiate a new game object.
	 * 
	 * @param	GameSizeX		The width of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	GameSizeY		The height of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	InitialState	The class name of the state you want to create and switch to first (e.g. MenuState).
	 */
	public FlxGame(int GameSizeX, int GameSizeY, Class<? extends FlxState> InitialState)
	{
		this(GameSizeX, GameSizeY, InitialState, 1, 30, 30, false, FlxCamera.STRETCH);
	}
	
	/**
	 * Makes the little volume tray slide out.
	 * 
	 * @param	Silent	Whether or not it should beep.
	 */
	void showSoundTray(boolean Silent)
	{
		//if(!Silent)
			//FlxG.play(SndBeep);
		
		if(_soundTray != null)
		{
			_soundTrayTimer = 1;
			_soundTray.y = 0;
			_soundTray.visible = true;
			int globalVolume = Math.round(FlxG.getVolume()*10);
			if(FlxG.mute)
				globalVolume = 0;
			for (int i = 0; i < _soundTrayBars.size; i++)
			{
				if(i < globalVolume) _soundTrayBars.get(i).setAlpha(1);
				else _soundTrayBars.get(i).setAlpha(0.5f);
			}
		}
	}
	
	/**
	 * Makes the little volume tray slide out.
	 */
	void showSoundTray()
	{
		showSoundTray(false);
	}
	
	/**
	 * Internal event handler for input and focus.
	 * 
	 * @param	KeyCode		A libgdx key code.
	 */
	@Override
	public boolean keyUp(int KeyCode)
	{		
		if (_debuggerUp && _debugger.watch.editing)
			return false;
		if (!FlxG.mobile)
		{
			if((_debugger != null) && ((KeyCode == Keys.APOSTROPHE) || (KeyCode == Keys.BACKSLASH)))
			{
				_debugger.visible = !_debugger.visible;
				_debuggerUp = _debugger.visible;
			
				/*if(_debugger.visible)
					flash.ui.Mouse.show();
				else if(!useSystemCursor)
					flash.ui.Mouse.hide();*/
				//_console.toggle();
				return true;
			}
			if(useSoundHotKeys)
			{
				int c = KeyCode;
				//String code = String.fromCharCode(KeyCode);
				switch(c)
				{
					case Keys.NUM_0:
					//case Keys.NUM_0:
						FlxG.mute = !FlxG.mute;
						if(FlxG.volumeHandler != null)
							FlxG.volumeHandler.callback(FlxG.mute?0:FlxG.getVolume());
						showSoundTray();
						return true;
					case Keys.MINUS:
					//case Keys.UNDERSCORE:
						FlxG.mute = false;
						FlxG.setVolume(FlxG.getVolume() - 0.1f);
						showSoundTray();
						return true;
					case Keys.PLUS:
					case Keys.EQUALS:
						FlxG.mute = false;
						FlxG.setVolume(FlxG.getVolume() + 0.1f);
						showSoundTray();
						return true;
					default:
						break;
				}
			}
		}
		if(_replaying)
			return true;
		
		FlxG.keys.handleKeyUp(KeyCode);
		
		return true;
	}
	
	/**
	 * Internal event handler for input and focus.
	 * 
	 * @param	KeyCode	libgdx key code.
	 */
	@Override
	public boolean keyDown(int KeyCode)
	{		
		if(_debuggerUp && _debugger.watch.editing)
			return false;
		if(_replaying && (_replayCancelKeys != null) && (_debugger == null) && /*(KeyCode != Keys.TILDE) && */(KeyCode != Keys.BACKSLASH))
		{
			//boolean cancel = false;
			String replayCancelKey;
			int i = 0;
			int l = _replayCancelKeys.size;
			while(i < l)
			{
				replayCancelKey = _replayCancelKeys.get(i++);
				if((replayCancelKey == "ANY") || (FlxG.keys.getKeyCode(replayCancelKey) == KeyCode))
				{
					if(_replayCallback != null)
					{
						_replayCallback.callback();
						_replayCallback = null;
					}
					else
						FlxG.stopReplay();
					break;
				}
			}
			return true;
		}
		
		FlxG.keys.handleKeyDown(KeyCode);
		return true;
	}

	@Override
	public boolean keyTyped(char character)
	{
		return false;
	}

	/**
	 * Internal event handler for input and focus.
	 */
	@Override
	public boolean touchDown(int X, int Y, int Pointer, int Button)
	{
		if(_debuggerUp)
		{
			if(_debugger.hasMouse)
				return false;
			if(_debugger.watch.editing)
				_debugger.watch.submit();
		}
		if(_replaying && (_replayCancelKeys != null))
		{
			String replayCancelKey;
			int i = 0;
			int l = _replayCancelKeys.size;
			while(i < l)
			{
				replayCancelKey = _replayCancelKeys.get(i++);
				if((replayCancelKey == "MOUSE") || (replayCancelKey == "ANY"))
				{
					if(_replayCallback != null)
					{
						_replayCallback.callback();
						_replayCallback = null;
					}
					else
						FlxG.stopReplay();
					break;
				}
			}
			return true;
		}
		FlxG.mouse.handleMouseDown(X, Y, Pointer, Button);
		_mouseEvent.type = MouseEvent.MOUSE_DOWN;
		_mouseEvent.stageX = X;
		_mouseEvent.stageY = Y;
		stage.dispatchEvent(_mouseEvent);
		return true;
	}

	/**
	 * Internal event handler for input and focus.
	 */
	@Override
	public boolean touchUp(int X, int Y, int Pointer, int Button)
	{
		if((_debuggerUp && _debugger.hasMouse) || _replaying)
			return true;
		FlxG.mouse.handleMouseUp(X, Y, Pointer, Button);
		_mouseEvent.type = MouseEvent.MOUSE_UP;
		_mouseEvent.stageX = X;
		_mouseEvent.stageY = Y;
		stage.dispatchEvent(_mouseEvent);
		return true;
	}

	/**
	 * Internal event handler for input and focus.
	 */
	@Override
	public boolean scrolled(int Amount)
	{
		if((_debuggerUp && _debugger.hasMouse) || _replaying)
			return false;
		FlxG.mouse.handleMouseWheel(Amount);
		return true;
	}
	
	/**
	 * Internal event handler for input and focus.
	 */
	@Override
	public boolean touchDragged(int X, int Y, int Pointer)
	{
		return false;
	}


	@Override
	public boolean mouseMoved(int x, int y)
	{
		return false;
	}
	
	/**
	 * Internal event handler for input and focus.
	 */
	@Override
	public void resume()
	{
		onFocus();
	}
	
	/**
	 * Internal event handler for input and focus.
	 */
	@Override
	public void pause()
	{
		onFocusLost();
	}

	/**
	 * Internal event handler for input and focus.
	 */
	protected void onFocus()
	{
		//if(!_debuggerUp && !useSystemCursor)
			//flash.ui.Mouse.hide();
		FlxG.resetInput();
		_lostFocus /*= _focus.visible*/ = false;
		//stage.frameRate = _flashFramerate;
		FlxG.resumeSounds();
	}
	
	/**
	 * Internal event handler for input and focus.
	 */
	protected void onFocusLost()
	{
		//flash.ui.Mouse.show();
		_lostFocus = /*_focus.visible =*/ true;
		//stage.frameRate = 10;
		FlxG.pauseSounds();
	}
	
	/**
	 * Handles the render call and figures out how many updates and draw calls to do.
	 */
	@Override
	public void render()
	{
		long mark = System.currentTimeMillis();
		long elapsedMS = mark - _total;
		_total = mark;
		updateSoundTray(elapsedMS);
		if (!_lostFocus)
		{
			if((_debugger != null) && _debugger.vcr.paused)
			{
				if(_debugger.vcr.stepRequested)
				{
					_debugger.vcr.stepRequested = false;
					step();
				}
			}
			else
			{
				_accumulator += elapsedMS;
				if(_accumulator > _maxAccumulation)
					_accumulator = _maxAccumulation;
				while(_accumulator >= _step)
				{
					step();
					_accumulator = _accumulator - _step; 
				}
			}
			
			FlxBasic._VISIBLECOUNT = 0;
			draw();
			
			if(_debuggerUp)
			{
				_debugger.perf.flash((int) elapsedMS);
				_debugger.perf.visibleObjects(FlxBasic._VISIBLECOUNT);
				_debugger.perf.update();
				_debugger.watch.update();
			}
		}
	}
	
	/**
	 * If there is a state change requested during the update loop,
	 * this function handles actual destroying the old state and related processes,
	 * and calls creates on the new state and plugs it into the game object.
	 */
	private void switchState()
	{
		//Basic reset stuff
		FlxG.resetCameras();
		FlxG.resetInput();
		FlxG.destroySounds();
		FlxG.clearBitmapCache();
		
		// Clear the debugger overlay's Watch window
		if(_debugger != null)
			_debugger.watch.removeAll();
		
		// Clear any timers left in the timer manager
		TimerManager timerManager = FlxTimer.getManager();
		if(timerManager != null)
			timerManager.clear();
		
		//Destroy the old state (if there is an old state)
		if(_state != null)
		{
			_state.destroy();
			_state = null;			
		}
		if(showSplashScreen && !_splashScreenShown)
		{
			_splashScreenShown = true;
			_state = new FlxSplashScreen(_requestedState);
			_requestedState = _state;
			_state.create();
		}
		else
		{
			//Finally assign and create the new state
			_state = _requestedState;
			_state.create();
		}
		
	}
	
	/**
	 * This is the main game update logic section.
	 * The onEnterFrame() handler is in charge of calling this
	 * the appropriate number of times each frame.
	 * This block handles state changes, replays, all that good stuff.
	 */
	protected void step()
	{
		// handle game reset request
		if(_requestedReset)
		{
			_requestedReset = false;
			try
			{
				_requestedState = ClassReflection.newInstance(_iState);
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
			_replayTimer = 0;
			_replayCancelKeys = null;
			if(showSplashScreen)
				_splashScreenShown = false;
			FlxG.reset();
		}
		
		//handle replay-related requests
		if(_recordingRequested)
		{
			_recordingRequested = false;
			_replay.create((float) FlxG.globalSeed);
			_recording = true;
			if(_debugger != null)
			{
				_debugger.vcr.recording();
				FlxG.log("FLIXEL: starting new flixel gameplay record.");
			}
		}
		else if(_replayRequested)
		{
			_replayRequested = false;
			_replay.rewind();
			FlxG.globalSeed = _replay.seed;
			if(_debugger != null)
				_debugger.vcr.playing();
			_replaying = true;
		}
		
		// handle state switching requests
		if(_state != _requestedState)
			switchState();
		
		//finally actually step through the game physics
		FlxBasic._ACTIVECOUNT = 0;
		if(_replaying)
		{
			_replay.playNextFrame();
			if(_replayTimer > 0)
			{
				_replayTimer -= _step;
				if(_replayTimer <= 0)
				{
					if(_replayCallback != null)
					{
						_replayCallback.callback();
						_replayCallback = null;
					}
					else
						FlxG.stopReplay();
				}
			}
			if(_replaying && _replay.finished)
			{
				FlxG.stopReplay();
				if(_replayCallback != null)
				{
					_replayCallback.callback();
					_replayCallback = null;
				}
			}
			if(_debugger != null)
				_debugger.vcr.updateRuntime(_step);
		}
		else
			FlxG.updateInput();
		if(_recording)
		{
			_replay.recordFrame();
			if(_debugger != null)
				_debugger.vcr.updateRuntime(_step);
		}
		update();
		FlxG.mouse.wheel = 0;
		if(_debuggerUp)
			_debugger.perf.activeObjects(FlxBasic._ACTIVECOUNT);
	}
	
	/**
	 * This function just updates the soundtray object.
	 */
	protected void updateSoundTray(float MS)
	{
		//animate stupid sound tray thing

		if(_soundTray != null)
		{
			if(_soundTrayTimer > 0)
				_soundTrayTimer -= MS/1000;
			else if(_soundTray.y > -_soundTray.height)
			{
				_soundTray.y -= (MS/1000)*FlxG.height*2;
				if(_soundTray.y <= -_soundTray.height)
				{
					_soundTray.visible = false;

					//Save sound preferences
					FlxSave soundPrefs = new FlxSave();
					if(soundPrefs.bind("flixel"))
					{
						soundPrefs.data.put("mute", FlxG.mute);
						soundPrefs.data.put("volume", FlxG.getVolume());
						soundPrefs.close();						
					}
				}
			}
		}
	}
	
	/**
	 * This function is called by step() and updates the actual game state.
	 * May be called multiple times per "frame" or draw call.
	 */
	protected void update()
	{
		long mark = System.currentTimeMillis();
		
		FlxG.elapsed = FlxG.timeScale*(_step/1000.f);
		FlxG.updateSounds();
		FlxG.updatePlugins();
		_state.update();
		FlxG.updateCameras();

		// TODO: temporary key for turning on debug, delete when FlxDebugger complete
		if(FlxG.keys.justPressed(Keys.F2) && (FlxG.debug || forceDebugger))
			FlxG.visualDebug = !FlxG.visualDebug;
		
		if(_debuggerUp)
			_debugger.perf.flixelUpdate((int)(System.currentTimeMillis()-mark));
	}
	
	/**
	 * Goes through the game state and draws all the game objects and special effects.
	 */
	private void draw()
	{
		long mark = System.currentTimeMillis();

		int i = 0;
		int l = FlxG._displayList.size;
		while (i < l)
		{
			FlxG._activeCamera = FlxG._displayList.get(i++);
			
			FlxG.lockCameras();
			_state.draw();
			FlxG.unlockCameras();
			
			FlxG.drawPlugins();
		}
				
		//Draw fps display TODO: needs to be deleted some day.
		if(FlxG.debug)
		{	
			FlxG.batch.setProjectionMatrix(_fontCamera.combined);
			FlxG.batch.begin();
			FlxG._gl.glScissor(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			_stringBuffer.delete(0, _stringBuffer.length());
			_stringBuffer.append("fps:");
			_stringBuffer.append(Gdx.graphics.getFramesPerSecond());
			_font.draw(FlxG.batch, _stringBuffer, Gdx.graphics.getWidth() - 80, 0);
			FlxG.batch.end();
		}
		if(_debuggerUp)
			_debugger.perf.flixelDraw((int) (System.currentTimeMillis()-mark));
	}
	
	/**
	 * Used to instantiate the guts of the flixel game object once we have a valid reference to the root. 
	 */
	@Override
	public void create()
	{	
		if (_created)
			return;
		
		_total = System.currentTimeMillis();
		
		//Set up OpenGL
		if(Gdx.graphics.isGL20Available())
		{
			FlxG._gl = Gdx.gl20;
		}
		else if(Gdx.graphics.isGL11Available())
		{
			FlxG._gl = Gdx.gl11;
		}
		else
			FlxG._gl = Gdx.gl10;
		
		// Common OpenGL
		if(!Gdx.graphics.isGL20Available())
			((GL10) FlxG._gl).glShadeModel(GL10.GL_FLAT);		
		FlxG._gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		FlxG._gl.glDisable(GL10.GL_CULL_FACE);
        FlxG._gl.glDisable(GL10.GL_DITHER);
        FlxG._gl.glDisable(GL10.GL_LIGHTING);
        FlxG._gl.glDisable(GL10.GL_DEPTH_TEST);
        FlxG._gl.glDisable(GL10.GL_FOG);
		FlxG._gl.glEnable(GL10.GL_SCISSOR_TEST);
		
		FileTextureData.copyToPOT = true;
		
		FlxG.batch = new SpriteBatch();
		FlxG.flashGfx = new Graphics();
		
		//Add basic input event listeners and mouse container
		FlxG.inputs.addProcessor(this);
		Gdx.input.setInputProcessor(FlxG.inputs);
		_mouseEvent = new MouseEvent(null, 0, 0);
		
		//Detect whether or not we're running on a mobile device
		FlxG.mobile = (Gdx.app.getType() != ApplicationType.Desktop);
		
		//Let mobile devs opt out of unnecessary overlays.
		if(!FlxG.mobile)
		{
			//Debugger overlay
			if(FlxG.debug || forceDebugger)
			{
				//_debugger = new FlxDebugger(FlxG.width*FlxCamera.defaultZoom,FlxG.height*FlxCamera.defaultZoom);
				//addChild(_debugger);
			}

			//Volume display tab
			createSoundTray();
			
			//Focus gained/lost monitoring
			createFocusScreen();
		}
		
		_created = true;
		
		_font = new BitmapFont(Gdx.files.classpath("org/flixel/data/font/nokiafc22.fnt"), Gdx.files.classpath("org/flixel/data/font/nokiafc22.png"), true);
		_font.setScale(2);
		_fontCamera = new OrthographicCamera();
		_fontCamera.setToOrtho(true);
		_stringBuffer = new StringBuffer();
	}
	
	/**
	 * Sets up the "sound tray", the little volume meter that pops down sometimes.
	 */
	//TODO: Sound tray
	protected void createSoundTray()
	{
		//_soundTray.visible = false;
		//_soundTray.scaleX = 2;
		//_soundTray.scaleY = 2;
		//var tmp:Bitmap = new Bitmap(new BitmapData(80,30,true,0x7F000000));
		//_soundTray.x = (FlxG.width/2)*FlxCamera.defaultZoom-(tmp.width/2)*_soundTray.scaleX;
		//_soundTray.addChild(tmp);

		//var text:TextField = new TextField();
		//text.width = tmp.width;
		//text.height = tmp.height;
		//text.multiline = true;
		//text.wordWrap = true;
		//text.selectable = false;
		//text.embedFonts = true;
		//text.antiAliasType = AntiAliasType.NORMAL;
		//text.gridFitType = GridFitType.PIXEL;
		//text.defaultTextFormat = new TextFormat("system",8,0xffffff,null,null,null,null,null,"center");;
		//_soundTray.addChild(text);
		//text.text = "VOLUME";
		//text.y = 16;

		//var bx:uint = 10;
		//var by:uint = 14;
		//_soundTrayBars = new Array();
		//var i:uint = 0;
		//while(i < 10)
		//{
			//tmp = new Bitmap(new BitmapData(4,++i,false,0xffffff));
			//tmp.x = bx;
			//tmp.y = by;
			//_soundTrayBars.push(_soundTray.addChild(tmp));
			//bx += 6;
			//by--;
		//}

		//_soundTray.y = -_soundTray.height;
		//_soundTray.visible = false;
		//addChild(_soundTray);

		//load saved sound preferences for this game if they exist
		FlxSave soundPrefs = new FlxSave();
		if(soundPrefs.bind("flixel"))// && (soundPrefs.data.get("sound") != null))
		{
			if(soundPrefs.data.get("volume", Float.class) != null)
				FlxG.setVolume(soundPrefs.data.get("volume", Float.class));
			if(soundPrefs.data.get("mute", Boolean.class) != null)
				FlxG.mute = soundPrefs.data.get("mute", Boolean.class);
			soundPrefs.destroy();
		}
	}
	
	/**
	 * Sets up the darkened overlay with the big white "play" button that appears when a flixel game loses focus.
	 */
	//TODO: Focus screen
	protected void createFocusScreen()
	{
		//var gfx:Graphics = _focus.graphics;
		//var screenWidth:uint = FlxG.width*FlxCamera.defaultZoom;
		//var screenHeight:uint = FlxG.height*FlxCamera.defaultZoom;

		//draw transparent black backdrop
		//gfx.moveTo(0,0);
		//gfx.beginFill(0,0.5);
		//gfx.lineTo(screenWidth,0);
		//gfx.lineTo(screenWidth,screenHeight);
		//gfx.lineTo(0,screenHeight);
		//gfx.lineTo(0,0);
		//gfx.endFill();

		//draw white arrow
		//var halfWidth:uint = screenWidth/2;
		//var halfHeight:uint = screenHeight/2;
		//var helper:uint = FlxU.min(halfWidth,halfHeight)/3;
		//gfx.moveTo(halfWidth-helper,halfHeight-helper);
		//gfx.beginFill(0xffffff,0.65);
		//gfx.lineTo(halfWidth+helper,halfHeight);
		//gfx.lineTo(halfWidth-helper,halfHeight+helper);
		//gfx.lineTo(halfWidth-helper,halfHeight-helper);
		//gfx.endFill();

		//var logo:Bitmap = new ImgLogo();
		//logo.scaleX = int(helper/10);
		//if(logo.scaleX < 1)
			//logo.scaleX = 1;
		//logo.scaleY = logo.scaleX;
		//logo.x -= logo.scaleX;
		//logo.alpha = 0.35;
		//_focus.addChild(logo);

		//addChild(_focus);
	}

	@Override
	public void resize(int Width, int Height)
	{		
		FlxG.screenWidth = Width;
		FlxG.screenHeight = Height;
		
		//reset all the cameras 
		for (FlxCamera camera : FlxG.cameras)
			camera.setScaleMode(camera.getScaleMode());
	}	
		
	@Override
	public void dispose()
	{
		_font.dispose();
		_stringBuffer = null;
		_state.destroy();
		_mouseEvent = null;
		FlxG.reset();
		FlxG.disposeAssetManager();
		FlxG.batch.dispose();
		FlxG.flashGfx.dispose();
	}
}