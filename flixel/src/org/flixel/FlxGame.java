package org.flixel;

import org.flixel.data.SystemAsset;
import org.flixel.event.AFlxReplay;
import org.flixel.event.IMouseObserver;
import org.flixel.event.IMouseSubject;
import org.flixel.plugin.TimerManager;
import org.flixel.system.FlxDebugger;
import org.flixel.system.FlxPause;
import org.flixel.system.FlxReplay;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import flash.display.Stage;

/**
 * FlxGame is the heart of all flixel games, and contains a bunch of basic game loops and things.
 * It is a long and sloppy file that you shouldn't have to worry about too much!
 * It is basically only used to create your game object in the first place,
 * after that FlxG and FlxState have all the useful stuff you actually need.
 * 
 * @author	Ka Wing Chin
 * @author	Thomas Weston
 */
public class FlxGame implements ApplicationListener, InputProcessor, IMouseSubject
{
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
	 * Total number of milliseconds elapsed since game start.
	 */
	protected long _total;
	/**
	 * Total number of milliseconds elapsed since last update loop.
	 * Counts down as we step through the game loop.
	 */
	protected int _accumulator;
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
	String[] _replayCancelKeys;
	/**
	 * Helps time out a replay if necessary.
	 */
	int _replayTimer;
	/**
	 * This function, if set, is triggered when the callback stops playing.
	 */
	AFlxReplay _replayCallback;

	
	private BitmapFont font;
		
	private GL10 gl;
	private FlxPause _pauseState;
	
	public Stage stage;
		
	private Array<IMouseObserver> observers;
	/**
	 * Instantiate a new game object.
	 * 
	 * @param	GameSizeX		The width of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	GameSizeY		The height of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	StageSizeX		The width of your game in actual pixels. In AS3, this would be in the SWF meta tag.
	 * @param	StageSizeY		The height of your game in actual pixels. In AS3, this would be in the SWF meta tag.
	 * @param	InitialState	The class name of the state you want to create and switch to first (e.g. MenuState).
	 * @param	Zoom			The default level of zoom for the game's cameras (e.g. 2 = all pixels are now drawn at 2x).  Default = 1.
	 * @param	GameFramerate	How frequently the game should update (default is 30 times per second).
	 * @param	FlashFramerate	Sets the actual display framerate for Flash player (default is 30 times per second).
	 * @param	UseSystemCursor	Whether to use the default OS mouse pointer, or to use custom flixel ones.
	 */
	public FlxGame(int GameSizeX, int GameSizeY, int StageSizeX, int StageSizeY, Class<? extends FlxState> InitialState, float Zoom, int GameFramerate, int FlashFramerate, boolean UseSystemCursor)
	{
		// basic display and update setup stuff
		FlxG.init(this, GameSizeX, GameSizeY, Zoom);
		FlxG.setFramerate(GameFramerate);
		FlxG.setFlashFramerate(FlashFramerate);
		observers = new Array<IMouseObserver>();
		// if no stage size has been specified, set it to the game size
		if (StageSizeX == 0 && StageSizeY == 0)
		{
			StageSizeX = GameSizeX;
			StageSizeY = GameSizeY;
		}
			
		stage = new Stage(StageSizeX, StageSizeY);
		
		_accumulator = (int) _step;
		_total = 0;
		_state = null;
		//useSoundHotKeys = true;
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
		this(GameSizeX, GameSizeY, 0, 0, InitialState, Zoom, GameFramerate, FlashFramerate, UseSystemCursor);
	}
	
	/**
	 * Instantiate a new game object.
	 * 
	 * @param	GameSizeX		The width of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	GameSizeY		The height of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	StageSizeX		The width of your game in actual pixels. In AS3, this would be in the SWF meta tag.
	 * @param	StageSizeY		The height of your game in actual pixels. In AS3, this would be in the SWF meta tag.
	 * @param	InitialState	The class name of the state you want to create and switch to first (e.g. MenuState).
	 * @param	Zoom			The default level of zoom for the game's cameras (e.g. 2 = all pixels are now drawn at 2x).  Default = 1.
	 * @param	GameFramerate	How frequently the game should update (default is 30 times per second).
	 * @param	FlashFramerate	Sets the actual display framerate for Flash player (default is 30 times per second).
	 */
	public FlxGame(int GameSizeX, int GameSizeY, int StageSizeX, int StageSizeY, Class<? extends FlxState> InitialState, float Zoom, int GameFramerate, int FlashFramerate)
	{
		this(GameSizeX, GameSizeY, StageSizeX, StageSizeY, InitialState, Zoom, GameFramerate, FlashFramerate, false);
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
		this(GameSizeX, GameSizeY, 0, 0, InitialState, Zoom, GameFramerate, FlashFramerate, false);
	}
	
	/**
	 * Instantiate a new game object.
	 * 
	 * @param	GameSizeX		The width of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	GameSizeY		The height of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	StageSizeX		The width of your game in actual pixels. In AS3, this would be in the SWF meta tag.
	 * @param	StageSizeY		The height of your game in actual pixels. In AS3, this would be in the SWF meta tag.
	 * @param	InitialState	The class name of the state you want to create and switch to first (e.g. MenuState).
	 * @param	Zoom			The default level of zoom for the game's cameras (e.g. 2 = all pixels are now drawn at 2x).  Default = 1.
	 * @param	GameFramerate	How frequently the game should update (default is 30 times per second).
	 */
	public FlxGame(int GameSizeX, int GameSizeY, int StageSizeX, int StageSizeY, Class<? extends FlxState> InitialState, float Zoom, int GameFramerate)
	{
		this(GameSizeX, GameSizeY, StageSizeX, StageSizeY, InitialState, Zoom, GameFramerate, 30, false);
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
		this(GameSizeX, GameSizeY, 0, 0, InitialState, Zoom, GameFramerate, 30, false);
	}
	
	/**
	 * Instantiate a new game object.
	 * 
	 * @param	GameSizeX		The width of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	GameSizeY		The height of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	StageSizeX		The width of your game in actual pixels. In AS3, this would be in the SWF meta tag.
	 * @param	StageSizeY		The height of your game in actual pixels. In AS3, this would be in the SWF meta tag.
	 * @param	InitialState	The class name of the state you want to create and switch to first (e.g. MenuState).
	 * @param	Zoom			The default level of zoom for the game's cameras (e.g. 2 = all pixels are now drawn at 2x).  Default = 1.
	 */
	public FlxGame(int GameSizeX, int GameSizeY, int StageSizeX, int StageSizeY, Class<? extends FlxState> InitialState, float Zoom)
	{
		this(GameSizeX, GameSizeY, StageSizeX, StageSizeY, InitialState, Zoom, 30, 30, false);
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
		this(GameSizeX, GameSizeY, 0, 0, InitialState, Zoom, 30, 30, false);
	}
	
	/**
	 * Instantiate a new game object.
	 * 
	 * @param	GameSizeX		The width of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	GameSizeY		The height of your game in game pixels, not necessarily final display pixels (see Zoom).
	 * @param	StageSizeX		The width of your game in actual pixels. In AS3, this would be in the SWF meta tag.
	 * @param	StageSizeY		The height of your game in actual pixels. In AS3, this would be in the SWF meta tag.
	 * @param	InitialState	The class name of the state you want to create and switch to first (e.g. MenuState).
	 */
	public FlxGame(int GameSizeX, int GameSizeY, int StageSizeX, int StageSizeY, Class<? extends FlxState> InitialState)
	{
		this(GameSizeX, GameSizeY, StageSizeX, StageSizeY, InitialState, 1, 30, 30, false);
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
		this(GameSizeX, GameSizeY, 0, 0, InitialState, 1, 30, 30, false);
	}
	
	/**
	 * Internal event handler for input and focus.
	 * 
	 * @param	KeyCode		A libgdx key code.
	 */
	@Override
	public boolean keyUp(int KeyCode)
	{
		if(KeyCode == Keys.F2)
			FlxG.visualDebug = !FlxG.visualDebug;
		
		/*if(_debuggerUp && _debugger.watch.editing)
			return false;*/
		
		if((_debugger != null) && ((KeyCode == Keys.F3) || (KeyCode == Keys.BACKSLASH)))
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
		if(KeyCode == Keys.MENU || KeyCode == Keys.F1)
		{
			if(!FlxG.paused)
				onFocusLost();
			else
				onFocus();			
		}
		
		//if(_debuggerUp && _debugger.watch.editing)
			//return;
		if(_replaying && (_replayCancelKeys != null) && (_debugger == null) /*&& (KeyCode != 192)*/ && (KeyCode != Keys.BACKSLASH))
		{
			String replayCancelKey;
			int i = 0;
			int l = _replayCancelKeys.length;
			while(i < l)
			{
				replayCancelKey = _replayCancelKeys[i++];
				if((replayCancelKey == "ANY") || (FlxG.keys.getKeyCode(replayCancelKey) == KeyCode))
				{
					if(_replayCallback != null)
					{
						_replayCallback.onComplete();
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
			//if(_debugger.hasMouse)
				//return;
			//if(_debugger.watch.editing)
				//_debugger.watch.submit();
		}
		if(_replaying && (_replayCancelKeys != null))
		{
			String replayCancelKey;
			int i = 0;
			int l = _replayCancelKeys.length;
			while(i < l)
			{
				replayCancelKey = _replayCancelKeys[i++];
				if((replayCancelKey == "MOUSE") || (replayCancelKey == "ANY"))
				{
					if(_replayCallback != null)
					{
						_replayCallback.onComplete();
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
		FlxG.mouse.activePointers++;
		notifyObserver();
		return true;
	}

	/**
	 * Internal event handler for input and focus.
	 */
	@Override
	public boolean touchUp(int X, int Y, int Pointer, int Button)
	{
		if(/*(_debuggerUp && _debugger.hasMouse) ||*/ _replaying)
			return true;
		FlxG.mouse.handleMouseUp(X, Y, Pointer, Button);
		notifyObserver();
		FlxG.mouse.activePointers--;
		return true;
	}

	/**
	 * Internal event handler for input and focus.
	 */
	@Override
	public boolean touchDragged(int X, int Y, int Pointer)
	{
		FlxG.mouse.handleMouseDrag(X, Y, Pointer);
		notifyObserver();
		return true;
	}

	
	@Override
	public boolean touchMoved(int X, int Y)
	{
		FlxG.mouse.handleMouseMove(X, Y);
		notifyObserver();
		return true;
	}

	/**
	 * Internal event handler for input and focus.
	 */
	@Override
	public boolean scrolled(int Amount)
	{
		if(/*(_debuggerUp && _debugger.hasMouse) ||*/ _replaying)
			return true;
		FlxG.mouse.handleMouseWheel(Amount);
		return true;
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
		
		/*if((_debugger != null) && _debugger.vcr.paused)
		{
			//if(_debugger.vcr.stepRequested)
			//{
				//_debugger.vcr.stepRequested = false;
				step(); //TODO: ahw bugger, needs VCR to get the correct frames for debug :(. Disabled and use the normal steps.
			//}
		}
		else*/
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
			//_debugger.watch.update();
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
		FlxG.clearFontCache();
		
		// Clear the debugger overlay's Watch window
		//if(_debugger != null)
			//_debugger.watch.removeAll();
		
		// Clear any timers left in the timer manager
		TimerManager timerManager = FlxTimer.getManager();
		if(timerManager != null)
			timerManager.clear();
		
		//Destroy the old state (if there is an old state)
		if(_state != null)
		{
			//_state.remove(_pauseState, true);
			_state.destroy();
		}
		
		//Finally assign and create the new state
		_state = _requestedState;
		_state.create();
		_state.add(_pauseState);
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
				_requestedState = _iState.newInstance();
			}
			catch (Exception e)
			{
				FlxG.log("FlxGame", e.getMessage());
			}
			_replayTimer = 0;
			_replayCancelKeys = null;
			FlxG.reset();			
		}
		
		//handle replay-related requests
		if(_recordingRequested)
		{
			_recordingRequested = false;
			_replay.create(FlxG.globalSeed);
			_recording = true;
			if(_debugger != null)
			{
				//_debugger.vcr.recording();
				FlxG.log("FLIXEL: starting new flixel gameplay record.");
			}
		}
		else if(_replayRequested)
		{
			_replayRequested = false;
			_replay.rewind();
			FlxG.globalSeed = _replay.seed;
			//if(_debugger != null)
				//_debugger.vcr.playing();
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
						_replayCallback.onComplete();
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
					_replayCallback.onComplete();
					_replayCallback = null;
				}
			}
			//if(_debugger != null)
				//_debugger.vcr.updateRuntime(_step);
		}
		else
			FlxG.updateInput();
		if(_recording)
		{
			_replay.recordFrame();
			//if(_debugger != null)
				//_debugger.vcr.updateRuntime(_step);
		}
		
		update();
		FlxG.mouse.wheel = 0;
		if(_debuggerUp)
			_debugger.perf.activeObjects(FlxBasic._ACTIVECOUNT);
	}
	
	/**
	 * This function is called by step() and updates the actual game state.
	 * May be called multiple times per "frame" or draw call.
	 */
	protected void update()
	{
		long mark = System.currentTimeMillis();
		FlxG.elapsed = FlxG.timeScale*(_step/1000.f);
				
		if(FlxG.paused)
		{
			_pauseState.update();
			return;
		}
		
		FlxG.updateSounds();
		FlxG.updatePlugins();
		_state.update();
		FlxG.updateCameras();
		
		if(_debuggerUp)
			_debugger.perf.flixelUpdate((int)(System.currentTimeMillis()-mark));
	}
	
	/**
	 * Goes through the game state and draws all the game objects and special effects.
	 */
	private void draw()
	{
		long mark = System.currentTimeMillis();
		
		//Clear the background to solid white
		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		for (FlxCamera camera : FlxG.cameras)
		{
			FlxG.batch.begin();
			
			//Set the drawing area to the area of the camera
			//TODO: Only calculate this when needed. 
			gl.glScissor((int) (camera.x * FlxG.difWidth), Gdx.graphics.getHeight() - ((int) (camera.y * FlxG.difHeight) + (int) (camera.height * FlxG.difHeight * camera.getZoom())), (int) FlxU.ceil(camera.width * FlxG.difWidth * camera.getZoom()), (int) FlxU.ceil(camera.height * FlxG.difHeight * camera.getZoom()));
			
			//Clear the camera
			float[] rgba = FlxU.getRGBA(camera.bgColor);
			gl.glClearColor(rgba[0], rgba[1], rgba[2], rgba[3]);
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			
			FlxG.batch.setProjectionMatrix(camera.glCamera.combined);
			
			_state.draw(camera);
			
			FlxG.batch.end();
			
			camera.drawFX();
		}
		
		//Draw fps display TODO: needs to be deleted some day.
		FlxG.batch.begin();
		FlxG.batch.setProjectionMatrix(FlxG.camera.glCamera.combined);
		font.draw(FlxG.batch, "fps:"+Gdx.graphics.getFramesPerSecond(), FlxG.width - 45, 0);
		FlxG.batch.end();
		
		if(_debuggerUp)
		{
			_debugger.perf.flixelDraw((int) (System.currentTimeMillis()-mark));
			_debugger.perf.draw();
		}
	}
	
	/**
	 * Used to instantiate the guts of the flixel game object once we have a valid reference to the root. 
	 */
	@Override
	public void create()
	{
		_total = System.currentTimeMillis();
		
		SystemAsset.createSystemAsset();
		
		FlxG.resWidth = Gdx.graphics.getWidth();
		FlxG.resHeight = Gdx.graphics.getHeight();
		FlxG.difWidth = ((float)FlxG.resWidth / stage.stageWidth);
		FlxG.difHeight = ((float)FlxG.resHeight / stage.stageHeight);
		
		Gdx.input.setInputProcessor(this);
		
		_pauseState = new FlxPause();
		
		gl = Gdx.gl10;
		//gl.glEnable(GL10.GL_CULL_FACE);
		//gl.glCullFace(GL10.GL_BACK);
		gl.glEnable(GL10.GL_SCISSOR_TEST);
		
		FlxG.batch = new SpriteBatch();
		FlxG.flashGfx = new ShapeRenderer();
		
		font = SystemAsset.system;
		
		if(Gdx.app.getType() != ApplicationType.Android)
		{
			//Debugger overlay
			if(FlxG.debug || forceDebugger)
			{
				_debugger = new FlxDebugger();
			}			
		}
	}

	@Override
	public void resize(int width, int height)
	{
		FlxG.resWidth = width;
		FlxG.resHeight = height;
		FlxG.difWidth = ((float)FlxG.resWidth / stage.stageWidth);
		FlxG.difHeight = ((float)FlxG.resHeight / stage.stageHeight);
	}	
	
	protected void onFocusLost()
	{
		FlxG.paused = true;
		_pauseState.visible = true;
		FlxG.pauseSounds();
	}
	
	protected void onFocus()
	{
		FlxG.paused = false;
		_pauseState.visible = false;
		FlxG.resumeSounds();
	}
	
	
	@Override
	public void pause()
	{
		FlxG.log("pause");
		onFocusLost();
	}

	@Override
	public void resume()
	{
		FlxG.log("resume");
	}
	
	@Override
	public void dispose()
	{
		FlxG.log("dispose");
		FlxG.assetManager.dispose();
	}

	@Override
	public void addObserver(IMouseObserver o)
	{
		observers.add(o);
	}

	@Override
	public void removeObserver(IMouseObserver o)
	{
		observers.removeValue(o, false);
	}

	@Override
	public void notifyObserver()
	{
		int l = observers.size;
		for(int i = 0; i < l; i++)
		{
			observers.get(i).updateListener();
		}
		
	}
}
