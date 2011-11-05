package org.flixel;

import org.flixel.data.SystemAsset;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlxGame extends Game
{
	/**
	 * Current game state.
	 */
	FlxState _state;
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
	float _step;
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
	
	private BitmapFontCache font;
		
	private boolean _GL11Supported;
	private GL10 gl;
		
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
		// basic display and update setup stuff
		FlxG.init(this, GameSizeX, GameSizeY, Zoom);
		FlxG.setFramerate(GameFramerate);
		FlxG.setFlashFramerate(FlashFramerate);
		_accumulator = (int) _step;
		_total = 0;
		_state = null;
//		useSoundHotKeys = true;
//		useSystemCursor = UseSystemCursor;
		
		// then get ready to create the game object for real;
		_iState = InitialState;
		_requestedState = null;
		_requestedReset = true;
//		create(); it will automatically called.
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
		this(GameSizeX, GameSizeY, InitialState, Zoom, GameFramerate, 30);
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
		this(GameSizeX, GameSizeY, InitialState, Zoom, 30, 30);
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
		this(GameSizeX, GameSizeY, InitialState, 1, 30, 30);
	}

	@Override
	public void create()
	{
		SystemAsset.createSystemAsset();
//		Gdx.input.setCatchBackKey(true);
		Gdx.input.setInputProcessor(FlxG.touch);
//		Gdx.input.setInputProcessor(FlxG.keys);
		
		if(Gdx.graphics.isGL11Available())
		{
			gl = Gdx.graphics.getGL11();
			_GL11Supported = true;
		}
		else
			gl = Gdx.graphics.getGL10();
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);
		
		FlxG.batch = new SpriteBatch();
		font = SystemAsset.system;
//		Gdx.gl.glClearColor(0, 0, 0, 1);
		_total = System.currentTimeMillis();
	}

	@Override
	public void resize(int width, int height)
	{
//		float aspectRatio = (float) width / (float) height;
//		camera = new OrthographicCamera(2f * aspectRatio, 2f);
//		camera = new OrthographicCamera(480, 320);
//		camera.translate(480/2, 320/2, 1);		
//		camera.update();
	}

	@Override
	public void render()
	{
		long mark = System.currentTimeMillis();
		long elapsedMS = mark - _total;
		_total = mark;
		// updateSoundTray(elapsedMS);
		_accumulator += elapsedMS;
		if(_accumulator > _maxAccumulation)
			_accumulator = _maxAccumulation;
		while(_accumulator >= _step)
		{
			step();
			_accumulator = (int) (_accumulator - _step);
		}
		FlxBasic._VISIBLECOUNT = 0;
		draw();
	}
	
	
	/**
	 * Goes through the game state and draws all the game objects and special effects. // TODO: Move this to render().
	 */
	private void draw()
	{
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT); // Clear the screen. It's not needed because of FlxG.lockCameras().	
		float[] rgba = FlxU.getRGBA(FlxG.getBgColor());
		gl.glClearColor(rgba[0], rgba[1], rgba[2], rgba[3]);
		
		FlxG.batch.begin();
		FlxG.camera.buffer.update();
		if(_GL11Supported)
		{
			FlxG.camera.buffer.apply(Gdx.gl11);
			// TODO: performance boost for OpenGL ES 1.1?
		}
		else
		{
			FlxG.camera.buffer.apply(Gdx.gl10);
		}
//		FlxG.camera.draw();
//		FlxG.lockCameras();
		_state.draw();
//		FlxG.drawPlugins();
		font.setText("fps:"+Gdx.graphics.getFramesPerSecond(), 435, 0);
		font.draw(FlxG.batch);//TODO:delete FPS counter
		FlxG.camera.drawFX();
//		FlxG.unlockCameras();
		FlxG.batch.end();
	}

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
			catch (InstantiationException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
			FlxG.reset();			
		}
		
		// handle state switching requests
		if(_state != _requestedState)
			switchState();
		
		//finally actually step through the game physics
		FlxBasic._ACTIVECOUNT = 0;
		
		// Replay shizzle
		// else
		FlxG.updateInput();
		update();
		
	}

	
	/**
	 * This function is called by step() and updates the actual game state.
	 * May be called multiple times per "frame" or draw call.
	 */
	protected void update()
	{
		FlxG.elapsed = FlxG.timeScale*(_step/1000);
		FlxG.updateSounds();
		FlxG.updatePlugins();
		_state.update();
		FlxG.updateCameras();
	}

	private void switchState()
	{
		FlxG.resetCameras();
		FlxG.resetInput();
		FlxG.destroySounds();
		FlxG.clearBitmapCache();
		
		// Clear the debugger overlay's Watch window
		
		// Clear any timers left in the timer manager
//		TimerManager timerManager = FlxTimer.manager;
//		if(timerManager != null)
//			timerManager.clear();
		
		//Destroy the old state (if there is an old state)
		if(_state != null)
			_state.destroy(); //TODO: do not destroy, but give a optional parameter to do setScreen(..);
		
		//Finally assign and create the new state
		_state = _requestedState;
		_state.create();
	}

	@Override
	public void pause()
	{
		FlxG.log("pause");
		super.pause();
	}

	@Override
	public void resume()
	{
		FlxG.log("resume");
		super.resume();
	}
	
	@Override
	public void dispose()
	{
		FlxG.log("dispose");
		super.dispose();
	}
}
