package org.flixel;

import org.flixel.event.AFlxCamera;
import org.flixel.event.AFlxG;
import org.flixel.event.AFlxObject;
import org.flixel.event.AFlxReplay;
import org.flixel.plugin.TimerManager;
import org.flixel.system.FlxQuadTree;
import org.flixel.system.FlxTextureData;
import org.flixel.system.input.Keyboard;
import org.flixel.system.input.Mouse;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap.Entries;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * This is a global helper class full of useful functions for audio,
 * input, basic info, and the camera system among other things.
 * Utilities for maths and color and things can be found in <code>FlxU</code>.
 * <code>FlxG</code> is specifically for Flixel-specific properties.
 * 
 * @author	Ka Wing Chin
 */
public class FlxG
{
	/**
	 * If you build and maintain your own version of flixel,
	 * you can give it your own name here.
	 */
	static public String LIBRARY_NAME = "flixel-android";
	/**
	 * Assign a major version to your library.
	 * Appears before the decimal in the console.
	 */
	static public int LIBRARY_MAJOR_VERSION = 2;
	/**
	 * Assign a minor version to your library.
	 * Appears after the decimal in the console.
	 */
	static public int LIBRARY_MINOR_VERSION = 55;
	
	/**
	 * Debugger overlay layout preset: Wide but low windows at the bottom of the screen.
	 */
	static public final int DEBUGGER_STANDARD = 0;
	/**
	 * Debugger overlay layout preset: Tiny windows in the screen corners.
	 */
	static public final int DEBUGGER_MICRO = 1;
	/**
	 * Debugger overlay layout preset: Large windows taking up bottom half of screen.
	 */
	static public final int DEBUGGER_BIG = 2;
	/**
	 * Debugger overlay layout preset: Wide but low windows at the top of the screen.
	 */
	static public final int DEBUGGER_TOP = 3;
	/**
	 * Debugger overlay layout preset: Large windows taking up left third of screen.
	 */
	static public final int DEBUGGER_LEFT = 4;
	/**
	 * Debugger overlay layout preset: Large windows taking up right third of screen.
	 */
	static public final int DEBUGGER_RIGHT = 5;
	
	/**
	 * Some handy color presets.  Less glaring than pure RGB full values.
	 * Primarily used in the visual debugger mode for bounding box displays.
	 * Red is used to indicate an active, movable, solid object.
	 */
	static public final int RED = 0xffff0012;
	/**
	 * Green is used to indicate solid but immovable objects.
	 */
	static public final int GREEN = 0xff00f225;
	/**
	 * Blue is used to indicate non-solid objects.
	 */
	static public final int BLUE = 0xff0090e9;
	/**
	 * Pink is used to indicate objects that are only partially solid, like one-way platforms.
	 */
	static public final int PINK = 0xfff01eff;
	/**
	 * White... for white stuff.
	 */
	static public final int WHITE = 0xffffffff;
	/**
	 * And black too.
	 */
	static public final int BLACK = 0xff000000;

	/**
	 * Internal tracker for game object.
	 */
	static FlxGame _game;
	/**
	 * Handy shared variable for implementing your own pause behavior.
	 */
	static public boolean paused;
	/**
	 * Whether you are running in Debug or Release mode.
	 * Set automatically by <code>FlxPreloader</code> during startup.
	 */
	static public boolean debug;
	
	/**
	 * Represents the amount of time in seconds that passed since last frame.
	 */
	static public float elapsed;
	/**
	 * How fast or slow time should pass in the game; default is 1.0.
	 */
	static public float timeScale;
	/**
	 * The width of the screen in game pixels.
	 */
	static public int width;
	/**
	 * The height of the screen in game pixels.
	 */
	static public int height;
	/**
	 * The dimensions of the game world, used by the quad tree for collisions and overlap checks.
	 */
	static public FlxRect worldBounds;
	/**
	 * The width in pixels of the display surface.
	 */
	static public int resWidth; 
	/**
	 * The height in pixels of the display surface.
	 */
	static public int resHeight;
	static public float difWidth;
	static public float difHeight;
	/**
	 * How many times the quad tree should divide the world on each axis.
	 * Generally, sparse collisions can have fewer divisons,
	 * while denser collision activity usually profits from more.
	 * Default value is 6.
	 */
	static public int worldDivisions;
	/**
	 * Whether to show visual debug displays or not.
	 * Default = false.
	 */
	static public boolean visualDebug;
	/**
	 * The global random number generator seed (for deterministic behavior in recordings and saves).
	 */
	static public float globalSeed;
	/**
	 * <code>FlxG.levels</code> and <code>FlxG.scores</code> are generic
	 * global variables that can be used for various cross-state stuff.
	 */
	static public Array<Object> levels;
	static public int level;
	static public Array<Object> scores;
	static public int score;
	/**
	 * <code>FlxG.saves</code> is a generic bucket for storing
	 * FlxSaves so you can access them whenever you want.
	 */
	static public Array<FlxSave> saves; 
	static public int save;

	/**
	 * A reference to a <code>FlxMouse</code> object.  Important for input!
	 */
	static public Mouse mouse;
	/**
	 * A reference to a <code>FlxKeyboard</code> object.  Important for input!
	 */
	static public Keyboard keys;
	
	/**
	 * A handy container for a background music object.
	 */
	static public FlxSound music;
	/**
	 * A list of all the sounds being played in the game.
	 */
	static public FlxGroup sounds;
	/**
	 * Whether or not the game sounds are muted.
	 */
	static public boolean mute;
	/**
	 * Internal volume level, used for global sound control.
	 */
	static protected float _volume;

	/**
	 * An array of <code>FlxCamera</code> objects that are used to draw stuff.
	 * By default flixel creates one camera the size of the screen.
	 */
	static public Array<FlxCamera> cameras;
	/**
	 * By default this just refers to the first entry in the cameras array
	 * declared above, but you can do what you like with it.
	 */
	static public FlxCamera camera;
	/**
	 * Allows you to possibly slightly optimize the rendering process IF
	 * you are not doing any pre-processing in your game state's <code>draw()</code> call.
	 * @default false
	 */
	static public boolean useBufferLocking;
	/**
	 * Internal helper variable for clearing the cameras each frame.
	 */
	static protected FlxRect _cameraRect;
	
	/**
	 * An array container for plugins.
	 * By default flixel uses a couple of plugins:
	 * DebugPathDisplay, and TimerManager.
	 */
	 static public Array<FlxBasic> plugins;
	 
	/**
	 * Set this hook to get a callback whenever the volume changes.
	 * Function should take the form <code>myVolumeHandler(Volume:Number)</code>.
	 */
//	static public var volumeHandler:Function; TODO: volumeHandler interface is not needed. There will be a display by the phone itself.
	
	/**
	 * Useful helper objects for doing Flash-specific rendering.
	 * Primarily used for "debug visuals" like drawing bounding boxes directly to the screen buffer.
	 */
	//static public Sprite flashGfxSprite;
	static public ShapeRenderer flashGfx;

	/**
	 * Internal storage system to prevent graphics from being used repeatedly in memory.
	 */
	static protected ObjectMap<String, TextureRegion> _cache;
	
	/**
	 * Global <code>SpriteBatch</code> for rendering sprites to the screen.
	 */
	static public SpriteBatch batch;
	
	static public String getLibraryName()
	{
		return FlxG.LIBRARY_NAME + " v" + FlxG.LIBRARY_MAJOR_VERSION + "." + FlxG.LIBRARY_MINOR_VERSION;
	}
	
	/**
	 * Log data to the debugger.
	 * 
	 * @param	tag		Handy if you want to use filter in LogCat.
	 * @param	data	The message you want to log to the console.
	 */
	public static void log(String tag, Object data)
	{
		if((_game != null)) // && (_game._debugger != null))
		{
			if(data != null)
				Gdx.app.log(tag, data.toString());
			else
				Gdx.app.log(tag, "null");
		}
	}
	
	/**
	 * Log data to the debugger. The tag is "flixel".
	 * 
	 * @param	Data	The message you want to log to the console.
	 */
	public static void log(Object data)
	{
		log("flixel", data);
	}
	
	/**
	 * Add a variable to the watch list in the debugger.
	 * This lets you see the value of the variable all the time.
	 * 
	 * @param	AnyObject		A reference to any object in your game, e.g. Player or Robot or this.
	 * @param	VariableName	The name of the variable you want to watch, in quotes, as a string: e.g. "speed" or "health".
	 * @param	DisplayName		Optional, display your own string instead of the class name + variable name: e.g. "enemy count".
	 */
	static public void watch(Object AnyObject,String VariableName,String DisplayName)
	{
		//if((_game != null) && (_game._debugger != null))
			//_game._debugger.watch.add(AnyObject,VariableName,DisplayName);
	}
	
	/**
	 * Add a variable to the watch list in the debugger.
	 * This lets you see the value of the variable all the time.
	 * 
	 * @param	AnyObject		A reference to any object in your game, e.g. Player or Robot or this.
	 * @param	VariableName	The name of the variable you want to watch, in quotes, as a string: e.g. "speed" or "health".
	 */
	static public void watch(Object AnyObject,String VariableName)
	{
		watch(AnyObject, VariableName, null);
	}
	
	/**
	 * Remove a variable from the watch list in the debugger.
	 * Don't pass a Variable Name to remove all watched variables for the specified object.
	 * 
	 * @param	AnyObject		A reference to any object in your game, e.g. Player or Robot or this.
	 * @param	VariableName	The name of the variable you want to watch, in quotes, as a string: e.g. "speed" or "health".
	 */
	static public void unwatch(Object AnyObject,String VariableName)
	{
		//if((_game != null) && (_game._debugger != null))
			//_game._debugger.watch.remove(AnyObject,VariableName);
	}
	
	/**
	 * Remove a variable from the watch list in the debugger.
	 * Don't pass a Variable Name to remove all watched variables for the specified object.
	 * 
	 * @param	AnyObject		A reference to any object in your game, e.g. Player or Robot or this.
	 */
	static public void unwatch(Object AnyObject)
	{
		unwatch(AnyObject, null);
	}
	
	/**
	 * How many times you want your game to update each second.
	 * More updates usually means better collisions and smoother motion.
	 * NOTE: This is NOT the same thing as the Flash Player framerate!
	 */
	static public float getFramerate()
	{
		return 1000/_game._step;
	}
	
	
	/**
	 * @private
	 */
	static public void setFramerate(int Framerate)
	{
		_game._step = 1000/Framerate;
		if(_game._maxAccumulation < _game._step)
				_game._maxAccumulation = (int) _game._step;
	}
	
	/**
	 * How many times you want your game to update each second.
	 * More updates usually means better collisions and smoother motion.
	 * NOTE: This is NOT the same thing as the Flash Player framerate!
	 */
	static public float getFlashFramerate()
	{
		//if(_game.root != null)
			//return _game.stage.frameRate;
		//else
			return 0;
	}
	
	/**
	 * @private
	 */
	static public void setFlashFramerate(int Framerate)
	{
		_game._flashFramerate = Framerate;
		_game._maxAccumulation = 2000/_game._flashFramerate - 1;
		if(_game._maxAccumulation < _game._step)
			_game._maxAccumulation = (int) _game._step;
	}

	
	/**
	 * Generates a random number.  Deterministic, meaning safe
	 * to use if you want to record replays in random environments.
	 * 
	 * @return	A <code>Number</code> between 0 and 1.
	 */
	static public float random()
	{
		return globalSeed = FlxU.srand(globalSeed);
	}
	
	/**
	 * Shuffles the entries in an array into a new random order.
	 * <code>FlxG.shuffle()</code> is deterministic and safe for use with replays/recordings.
	 * HOWEVER, <code>FlxU.shuffle()</code> is NOT deterministic and unsafe for use with replays/recordings.
	 * @param <T>
	 * 
	 * @param	A				A Flash <code>Array</code> object containing...stuff.
	 * @param	HowManyTimes	How many swaps to perform during the shuffle operation.  Good rule of thumb is 2-4 times as many objects are in the list.
	 * 
	 * @return	The same Flash <code>Array</code> object that you passed in in the first place.
	 */
	static public <T> Array<T> shuffle(Array<T> Objects,int HowManyTimes)
	{
		int i = 0;
		int index1;
		int index2;
		T object;
		while(i < HowManyTimes)
		{
			index1 = (int) (FlxG.random()*Objects.size);
			index2 = (int) (FlxG.random()*Objects.size);
			object = Objects.get(index2);
			Objects.set(index2, Objects.get(index1));
			Objects.set(index1, object);
			i++;
		}
		return Objects;
	}
	
	/**
	 * Fetch a random entry from the given array.
	 * Will return null if random selection is missing, or array has no entries.
	 * <code>FlxG.getRandom()</code> is deterministic and safe for use with replays/recordings.
	 * HOWEVER, <code>FlxU.getRandom()</code> is NOT deterministic and unsafe for use with replays/recordings.
	 * 
	 * @param	Objects		A Flash array of objects.
	 * @param	StartIndex	Optional offset off the front of the array. Default value is 0, or the beginning of the array.
	 * @param	Length		Optional restriction on the number of values you want to randomly select from.
	 * 
	 * @return	The random object that was selected.
	 */
	static public <T> T getRandom(Array<T> Objects,int StartIndex,int Length)
	{
		if(Objects != null)
		{
			int l = Length;
			if((l == 0) || (l > Objects.size - StartIndex))
				l = Objects.size - StartIndex;
			if(l > 0)
				return Objects.get(StartIndex + (int)(FlxG.random()*l));
		}
		return null;
	}
	
	/**
	 * Fetch a random entry from the given array.
	 * Will return null if random selection is missing, or array has no entries.
	 * <code>FlxG.getRandom()</code> is deterministic and safe for use with replays/recordings.
	 * HOWEVER, <code>FlxU.getRandom()</code> is NOT deterministic and unsafe for use with replays/recordings.
	 * 
	 * @param	Objects		A Flash array of objects.
	 * @param	StartIndex	Optional offset off the front of the array. Default value is 0, or the beginning of the array.
	 * 
	 * @return	The random object that was selected.
	 */
	static public <T> T getRandom(Array<T> Objects,int StartIndex)
	{
		return getRandom(Objects, StartIndex, 0);
	}
	
	/**
	 * Fetch a random entry from the given array.
	 * Will return null if random selection is missing, or array has no entries.
	 * <code>FlxG.getRandom()</code> is deterministic and safe for use with replays/recordings.
	 * HOWEVER, <code>FlxU.getRandom()</code> is NOT deterministic and unsafe for use with replays/recordings.
	 * 
	 * @param	Objects		A Flash array of objects.
	 * 
	 * @return	The random object that was selected.
	 */
	static public <T> T getRandom(Array<T> Objects)
	{
		return getRandom(Objects, 0, 0);
	}
	

	/**
	 * Load replay data from a string and play it back.
	 * 
	 * @param	Data		The replay that you want to load.
	 * @param	State		Optional parameter: if you recorded a state-specific demo or cutscene, pass a new instance of that state here.
	 * @param	CancelKeys	Optional parameter: an array of string names of keys (see FlxKeyboard) that can be pressed to cancel the playback, e.g. ["ESCAPE","ENTER"].  Also accepts 2 custom key names: "ANY" and "MOUSE" (fairly self-explanatory I hope!).
	 * @param	Timeout		Optional parameter: set a time limit for the replay.  CancelKeys will override this if pressed.
	 * @param	Callback	Optional parameter: if set, called when the replay finishes.  Running to the end, CancelKeys, and Timeout will all trigger Callback(), but only once, and CancelKeys and Timeout will NOT call FlxG.stopReplay() if Callback is set!
	 */
	static public void loadReplay(String Data,FlxState State,String[] CancelKeys,float Timeout,AFlxReplay Callback)
	{
		_game._replay.load(Data);
		if(State == null)
			FlxG.resetGame();
		else
			FlxG.switchState(State);
		_game._replayCancelKeys = CancelKeys;
		_game._replayTimer = (int) (Timeout*1000);
		_game._replayCallback = Callback;
		_game._replayRequested = true;
	}
	
	/**
	 * Load replay data from a string and play it back.
	 * 
	 * @param	Data		The replay that you want to load.
	 * @param	State		Optional parameter: if you recorded a state-specific demo or cutscene, pass a new instance of that state here.
	 * @param	CancelKeys	Optional parameter: an array of string names of keys (see FlxKeyboard) that can be pressed to cancel the playback, e.g. ["ESCAPE","ENTER"].  Also accepts 2 custom key names: "ANY" and "MOUSE" (fairly self-explanatory I hope!).
	 * @param	Timeout		Optional parameter: set a time limit for the replay.  CancelKeys will override this if pressed.
	 */
	static public void loadReplay(String Data,FlxState State,String[] CancelKeys,float Timeout)
	{
		loadReplay(Data, State, CancelKeys, Timeout, null);
	}
	
	/**
	 * Load replay data from a string and play it back.
	 * 
	 * @param	Data		The replay that you want to load.
	 * @param	State		Optional parameter: if you recorded a state-specific demo or cutscene, pass a new instance of that state here.
	 * @param	CancelKeys	Optional parameter: an array of string names of keys (see FlxKeyboard) that can be pressed to cancel the playback, e.g. ["ESCAPE","ENTER"].  Also accepts 2 custom key names: "ANY" and "MOUSE" (fairly self-explanatory I hope!).
	 */
	static public void loadReplay(String Data,FlxState State,String[] CancelKeys)
	{
		loadReplay(Data, State, CancelKeys, 0, null);
	}
	
	/**
	 * Load replay data from a string and play it back.
	 * 
	 * @param	Data		The replay that you want to load.
	 * @param	State		Optional parameter: if you recorded a state-specific demo or cutscene, pass a new instance of that state here.
	 */
	static public void loadReplay(String Data,FlxState State)
	{
		loadReplay(Data, State, null, 0, null);
	}
	
	/**
	 * Load replay data from a string and play it back.
	 * 
	 * @param	Data		The replay that you want to load.
	 */
	static public void loadReplay(String Data)
	{
		loadReplay(Data, null, null, 0, null);
	}
	
	/**
	 * Resets the game or state and replay requested flag.
	 * 
	 * @param	StandardMode	If true, reload entire game, else just reload current game state.
	 */
	static public void reloadReplay(boolean StandardMode)
	{
		if(StandardMode)
			FlxG.resetGame();
		else
			FlxG.resetState();
		if(_game._replay.frameCount > 0)
			_game._replayRequested = true;
	}
	
	/**
	 * Resets the game or state and replay requested flag. 
	 */
	static public void reloadReplay()
	{
		reloadReplay(true);
	}
	
	/**
	 * Stops the current replay.
	 */
	static public void stopReplay()
	{
		_game._replaying = false;
		//if(_game._debugger != null)
			//_game._debugger.vcr.stopped();
		resetInput();
	}
	
	/**
	 * Resets the game or state and requests a new recording.
	 * 
	 * @param	StandardMode	If true, reset the entire game, else just reset the current state.
	 */
	static public void recordReplay(boolean StandardMode)
	{
		if(StandardMode)
			FlxG.resetGame();
		else
			FlxG.resetState();
		_game._recordingRequested = true;
	}
	
	/**
	 * Resets the game or state and requests a new recording.
	 * 
	 * @param	StandardMode	If true, reset the entire game, else just reset the current state.
	 */
	static public void recordReplay()
	{
		recordReplay(true);
	}
	
	/**
	 * Stop recording the current replay and return the replay data.
	 * 
	 * @return	The replay data in simple ASCII format (see <code>FlxReplay.save()</code>).
	 */
	static public String stopRecording()
	{
		_game._recording = false;
		//if(_game._debugger != null)
			//_game._debugger.vcr.stopped();
		return _game._replay.save();
	}
	
	/**
	 * Request a reset of the current game state.
	 */
	static public void resetState()
	{		
		try
		{
			_game._requestedState = (FlxState) _game._state.getClass().newInstance();
		}
		catch (Exception e) 
		{
			FlxG.log("FlxG.resetGame", e.getMessage());
		}
	}
	
	
	/**
	 * Like hitting the reset button on a game console, this will re-launch the game as if it just started.
	 */
	static public void resetGame()
	{
		_game._requestedReset = true;
	}
	
	/**
	 * Reset the input helper objects (useful when changing screens or states)
	 */
	static public void resetInput()
	{
		keys.reset();
		mouse.reset();
	}
	
	/**
	 * Set up and play a looping background soundtrack.
	 * 
	 * @param Music The sound file you want to loop in the background.
	 * @param Volume How loud the sound should be, from 0 to 1.
	 */
	static public void playMusic(Music sound, float Volume)
	{
		if(music == null)
			music = new FlxSound();
		if(music.active)
			music.stop();
		music.loadEmbedded(sound, true);
		music.setVolume(Volume);
		music.survive = true;
		music.play();
	}

	/**
	 * Set up and play a looping background soundtrack.
	 * 
	 * @param Music The sound file you want to loop in the background.
	 */
	static public void playMusic(Music sound)
	{
		playMusic(sound, 1);
	}
	
	
	/**
	 * Creates a new sound object.
	 * 
	 * @param	EmbeddedSound	The embedded sound resource you want to play.  To stream, use the optional URL parameter instead.
	 * @param	Volume			How loud to play it (0 to 1).
	 * @param	Looped			Whether to loop this sound.
	 * @param	AutoDestroy		Whether to destroy this sound when it finishes playing.  Leave this value set to "false" if you want to re-use this <code>FlxSound</code> instance.
	 * @param	AutoPlay		Whether to play the sound.
	 * 
	 * @return	A <code>FlxSound</code> object.
	 */
	static public FlxSound loadSound(Sound EmbeddedSound, float Volume, boolean Looped, boolean AutoDestroy, boolean AutoPlay)
	{
		if((EmbeddedSound == null))
		{
			FlxG.log("WARNING: FlxG.loadSound() requires an embedded sound to work.");
			return null;
		}
		FlxSound sound = null;
		try
		{
			sound = (FlxSound) sounds.recycle(FlxSound.class);
		}
		catch (Exception e) 
		{
			FlxG.log("FlxG", e.getMessage());
		}
		if(EmbeddedSound != null)
			sound.loadEmbedded(EmbeddedSound,Looped,AutoDestroy);
		sound.setVolume(Volume);
		if(AutoPlay)
			sound.play();
		return sound;
	}

	
	/**
	 * Creates a new sound object.
	 * 
	 * @param	EmbeddedSound	The embedded sound resource you want to play.  To stream, use the optional URL parameter instead.
	 * @param	Volume			How loud to play it (0 to 1).
	 * @param	Looped			Whether to loop this sound.
	 * @param	AutoDestroy		Whether to destroy this sound when it finishes playing.  Leave this value set to "false" if you want to re-use this <code>FlxSound</code> instance.
	 * 
	 * @return	A <code>FlxSound</code> object.
	 */
	static public FlxSound loadSound(Sound EmbeddedSound, float Volume, boolean Looped, boolean AutoDestroy)
	{
		return loadSound(EmbeddedSound, Volume, Looped, AutoDestroy, false);
	}
	
	
	/**
	 * Creates a new sound object.
	 * 
	 * @param	EmbeddedSound	The embedded sound resource you want to play.  To stream, use the optional URL parameter instead.
	 * @param	Volume			How loud to play it (0 to 1).
	 * @param	Looped			Whether to loop this sound.
	 * 
	 * @return	A <code>FlxSound</code> object.
	 */
	static public FlxSound loadSound(Sound EmbeddedSound, float Volume, boolean Looped)
	{
		return loadSound(EmbeddedSound, Volume, Looped, false, false);
	}
	
	/**
	 * Creates a new sound object.
	 * 
	 * @param	EmbeddedSound	The embedded sound resource you want to play.  To stream, use the optional URL parameter instead.
	 * @param	Volume			How loud to play it (0 to 1).
	 * 
	 * @return	A <code>FlxSound</code> object.
	 */
	static public FlxSound loadSound(Sound EmbeddedSound, float Volume)
	{
		return loadSound(EmbeddedSound, Volume, false, false, false);
	}
	
	
	/**
	 * Creates a new sound object.
	 * 
	 * @param	EmbeddedSound	The embedded sound resource you want to play.  To stream, use the optional URL parameter instead.
	 * 
	 * @return	A <code>FlxSound</code> object.
	 */
	static public FlxSound loadSound(Sound EmbeddedSound)
	{
		return loadSound(EmbeddedSound, 1.0f, false, false, false);
	}
	
	
	/**
	 * Set <code>volume</code> to a number between 0 and 1 to change the global volume.
	 * 
	 * @default 0.5
	 */
	 static public float getVolume()
	 {
		 return _volume;
	 }
	 
	/**
	 * @private
	 */
	static public void setVolume(float Volume)
	{
		_volume = Volume;
		if(_volume < 0)
			_volume = 0;
		else if(_volume > 1)
			_volume = 1;
//		if(volumeHandler != null)
//			volumeHandler(FlxG.mute?0:_volume);
	}
	
	
	/**
	 * Called by FlxGame on state changes to stop and destroy sounds.
	 * 
	 * @param	ForceDestroy		Kill sounds even if they're flagged <code>survive</code>.
	 */
	static void destroySounds(boolean ForceDestroy)
	{
		if((music != null) && (ForceDestroy || !music.survive))
		{
			music.destroy();
			music = null;
		}
		int i = 0;
		FlxSound sound;
		int l = sounds.members.size;
		while(i < l)
		{
			sound = (FlxSound) sounds.members.get(i++);
			if((sound != null) && (ForceDestroy || !sound.survive))
			{
				sound.destroy();
			}
		}
	}

	/**
	 * Called by FlxGame on state changes to stop and destroy sounds.
	 * 
	 * @param	ForceDestroy		Kill sounds even if they're flagged <code>survive</code>.
	 */
	static void destroySounds()
	{
		destroySounds(false);
	}
	
	static public void init(FlxGame Game, int Width, int Height, float Zoom)
	{
		_game = Game;
		_cache = new ObjectMap<String, TextureRegion>();
		width = Width;
		height = Height;
		
		
		mute = false;
		_volume = 0.5f;
		sounds = new FlxGroup();
//		volumeHandler = null;
		
		clearBitmapCache();
		
		FlxCamera.defaultZoom = 1;//Zoom;
//		FlxG._cameraRect = new Rectangle();
		FlxG.cameras = new Array<FlxCamera>();
		FlxG.visualDebug = false;
//		useBufferLocking = false;
//		
		plugins = new Array<FlxBasic>();
//		addPlugin(new DebugPathDisplay());
		addPlugin(new TimerManager());
//		
		FlxG.mouse = new Mouse();
		FlxG.keys = new Keyboard();
	}
	
	public static void reset()
	{
		FlxG.clearBitmapCache();
		resetInput();
		destroySounds(true);
		paused = false;
		timeScale = 1.0f;
		elapsed = 0;
		globalSeed = (float) Math.random();
		worldBounds = new FlxRect(-10,-10,FlxG.width+20,FlxG.height+20);
		worldDivisions = 6;
	}
	
	
	/**
	 * Called by the game object to update the keyboard and mouse input tracking objects.
	 */
	public static void updateInput()
	{
		FlxG.keys.update();		
		FlxG.mouse.update();
	}
	
	/**
	 * Called by the game object to lock all the camera buffers and clear them for the next draw pass.  
	 */
	public static void lockCameras()
	{
		FlxCamera cam;
		Array<FlxCamera> cams = FlxG.cameras;
		int i = 0;
		int l = cams.size;
		while(i < l)
		{
			cam = cams.get(i++);
			if((cam != null) && cam.exists)
			{
				//if(cam.active)
					//cam.draw();
			}
		}
	}
	
	
	/**
	 * Called by the game object to draw the special FX and unlock all the camera buffers.
	 */
	public static void unlockCameras()
	{
		FlxCamera cam;
		Array<FlxCamera> cams = FlxG.cameras;
		int i = 0;
		int l = cams.size;
		while(i < l)
		{
			cam = cams.get(i++);
			if((cam == null) || !cam.exists || !cam.visible)
				continue;
			cam.drawFX();
//			if(useBufferLocking)
//				cam.buffer.unlock();
		}
	}

	
	/**
	 * Called by the game object to update the cameras and their tracking/special effects logic.
	 */
	public static void updateCameras()
	{
		FlxCamera cam;
		Array<FlxCamera> cams = FlxG.cameras;
		int i = 0;
		int l = cams.size;
		while(i < l)
		{
			cam = cams.get(i++);
			if((cam != null) && cam.exists)
			{
				if(cam.active)
					cam.update();
//				cam.buffer.translate(cam.x + cam._flashOffsetX, cam.y + cam._flashOffsetY, 1);
//				cam._flashSprite.x = cam.x + cam._flashOffsetX;
//				cam._flashSprite.y = cam.y + cam._flashOffsetY;
//				cam._flashSprite.visible = cam.visible;
				// This is working, it updates to fast?
//				cam.buffer.position.x = (FlxG.width/2-cam.x);
//				cam.buffer.position.y = (FlxG.height/2-cam.y);		
//				cam.buffer.update();
			}
		}
	}
	
	

	
	
	/**
	 * Check the local bitmap cache to see if a bitmap with this key has been
	 * loaded already.
	 * 
	 * @param Key The string key identifying the bitmap.
	 * 
	 * @return Whether or not this file can be found in the cache.
	 */
	static public boolean checkBitmapCache(String Key)
	{
		return (_cache.get(Key) != null);
	}

	/**
	 * Generates a new <code>BitmapData</code> object (a colored square) and
	 * caches it.
	 * 
	 * @param Width 	How wide the square should be.
	 * @param Height 	How high the square should be.
	 * @param color 	What color the square should be (0xAARRGGBB)
	 * @param Unique	Ensures that the bitmap data uses a new slot in the cache.
	 * @param Key		Force the cache to use a specific Key to index the bitmap.
	 * 
	 * @return The <code>BitmapData</code> we just created.
	 *///TODO: Bug: using this will hit the performance. The Texture won't be dupilcated, so how come?
	static public TextureRegion createBitmap(int Width, int Height, long Color, boolean Unique, String Key)
	{		
		if(Key == null)
		{
			Key = Width+"x"+Height+":"+Color;
			if(Unique && (_cache.get(Key) != null))
			{
				// Generate a unique key
				int inc = 0;
				String ukey;
				do
				{
					ukey = Key + inc++;
				}
				while((_cache.get(ukey) != null));
				Key = ukey;
			}
		}
		if(!checkBitmapCache(Key))
		{
			Pixmap p = new Pixmap(FlxU.ceilPowerOfTwo(Width), FlxU.ceilPowerOfTwo(Height), Format.RGBA8888);			
			p.setColor(FlxU.colorFromHex(Color));
			p.fillRectangle(0, 0, Width, Height);
			_cache.put(Key, new TextureRegion(new Texture(new FlxTextureData(p))));
		}
		return _cache.get(Key);
	}
	
	/**
	 * Generates a new <code>BitmapData</code> object (a colored square) and
	 * caches it.
	 * 
	 * @param Width 	How wide the square should be.
	 * @param Height 	How high the square should be.
	 * @param Color 	What color the square should be (0xAARRGGBB)
	 * @param Unique	Ensures that the bitmap data uses a new slot in the cache.
	 * 
	 * @return The <code>BitmapData</code> we just created.
	 */
	static public TextureRegion createBitmap(int Width, int Height, int Color)
	{
		return createBitmap(Width, Height, Color, false, null);
	}
	
	/**
	 * Generates a new <code>BitmapData</code> object (a colored square) and
	 * caches it.
	 * 
	 * @param Width 	How wide the square should be.
	 * @param Height 	How high the square should be.
	 * @param Color 	What color the square should be (0xAARRGGBB)
	 * @param Unique	Ensures that the bitmap data uses a new slot in the cache.
	 * 
	 * @return The <code>BitmapData</code> we just created.
	 */
	static public TextureRegion createBitmap(int Width, int Height, int Color, boolean Unique)
	{
		return createBitmap(Width, Height, Color, Unique, null);
	}

	/**
	 * Loads a bitmap from a file, caches it, and generates a horizontally
	 * flipped version if necessary.
	 * 
	 * @param Graphic The image file that you want to load.
	 * @param Unique Make the bitmap unique, no duplicate allowed.
	 * @param Key
	 * 
	 * @return The <code>BitmapData</code> we just created.
	 */
	static public TextureRegion addBitmap(TextureRegion Graphic, boolean Unique, String Key)
	{
		if(Key == null)
		{	
			Key = String.valueOf(Graphic.hashCode());
			if(Unique && (_cache.get(Key) != null))
			{
				// Generate a unique key
				int inc = 0;
				String ukey;
				do
				{
					ukey = Key + inc++;
				}
				while((_cache.get(ukey) != null));
				Key = ukey;
			}
		}
		// If there is no data for this key, generate the requested graphic
		if(!checkBitmapCache(Key))
		{
			if (!Unique)
				_cache.put(Key, Graphic);
			else
			{
				TextureData textureData = Graphic.getTexture().getTextureData();
			
				if(!textureData.isPrepared())
					textureData.prepare();
			
				Pixmap newPixmap = new Pixmap(FlxU.ceilPowerOfTwo(Graphic.getRegionWidth()), FlxU.ceilPowerOfTwo(Graphic.getRegionHeight()), Pixmap.Format.RGBA8888);
				Pixmap graphicPixmap = textureData.consumePixmap();
				newPixmap.drawPixmap(graphicPixmap, 0, 0, Graphic.getRegionX(), Graphic.getRegionY(), Graphic.getRegionWidth(), Graphic.getRegionHeight());
			
				if (textureData.disposePixmap())
					graphicPixmap.dispose();
				
				_cache.put(Key, new TextureRegion(new Texture(new FlxTextureData(newPixmap))));
			}
		}
		return new TextureRegion(_cache.get(Key));
	}
	
	
	/**
	 * Loads a bitmap from a file, caches it, and generates a horizontally
	 * flipped version if necessary.
	 * 
	 * @param Graphic The image file that you want to load.
	 * @param Reverse Whether to generate a flipped version.
	 * @param Unique Make the bitmap unique, no duplicate allowed.
	 * 
	 * @return The <code>BitmapData</code> we just created.
	 */
	static public TextureRegion addBitmap(TextureRegion Graphic, boolean Unique)
	{
		return addBitmap(Graphic, Unique, null);
	}
	
	
	/**
	 * Loads a bitmap from a file, caches it, and generates a horizontally
	 * flipped version if necessary.
	 * 
	 * @param Graphic The image file that you want to load.
	 * 
	 * @return The <code>BitmapData</code> we just created.
	 */
	static public TextureRegion addBitmap(TextureRegion Graphic)
	{
		return addBitmap(Graphic, false, null);
	}
	
	/**
	 * Dumps the cache's image references.
	 */
	static public void clearBitmapCache()
	{
		Entries<String, TextureRegion> iter = _cache.entries();
		while(iter.hasNext())
		{
			Texture texture = iter.next().value.getTexture();
			Pixmap pixmap = null;
			if (!texture.getTextureData().disposePixmap())
				pixmap = texture.getTextureData().consumePixmap();
			if (pixmap != null)
			{
				pixmap.dispose();
				texture.dispose();
			}
		}
		_cache.clear();
	}
	
	
	/**
	 * Read-only: access the current game state from anywhere.
	 */
	static public FlxState getState()
	{
		return _game._state;
	}
	
	
	/**
	 * Switch from the current game state to the one specified here.
	 */
	static public void switchState(FlxState State)
	{
		_game._requestedState = State;
	}
	
	
	/**
	 * Add a new camera object to the game.
	 * Handy for PiP, split-screen, etc.
	 * 
	 * @param	NewCamera	The camera you want to add.
	 * 
	 * @return	This <code>FlxCamera</code> instance.
	 */
	static public FlxCamera addCamera(FlxCamera NewCamera)
	{			
		cameras.add(NewCamera);
		return NewCamera;
	}
	
	
	/**
	 * Remove a camera from the game.
	 * 
	 * @param	Camera	The camera you want to remove.
	 * @param	Destroy	Whether to call destroy() on the camera, default value is true.
	 */
	static public void removeCamera(FlxCamera Camera, boolean Destroy)
	{
		cameras.removeValue(Camera, true);
		if(Destroy)
			Camera.destroy();
	}
	
	
	/**
	 * Dumps all the current cameras and resets to just one camera.
	 * Handy for doing split-screen especially.
	 * 
	 * @param	NewCamera	Optional; specify a specific camera object to be the new main camera.
	 */
	static public void resetCameras(FlxCamera NewCamera)
	{
		FlxCamera cam;
		int i = 0;
		int l = cameras.size;
		while(i < l)
		{
			cam = FlxG.cameras.get(i++);
			cameras.removeValue(cam, false);
			cam.destroy();
		}
		FlxG.cameras.clear();
		
		if(NewCamera == null)
			NewCamera = new FlxCamera(0, 0, FlxG.width, FlxG.height);
		FlxG.camera = FlxG.addCamera(NewCamera);
	}
	
	/**
	 * Dumps all the current cameras and resets to just one camera.
	 * Handy for doing split-screen especially.
	 */ 
	public static void resetCameras()
	{
		resetCameras(null);
	}
	
	
	/**
	 * All screens are filled with this color and gradually return to normal.
	 * 
	 * @param	Color		The color you want to use.
	 * @param	Duration	How long it takes for the flash to fade.
	 * @param	OnComplete	A function you want to run when the flash finishes.
	 * @param	Force		Force the effect to reset.
	 */
	static public void flash(int Color, float Duration, AFlxCamera OnComplete, boolean Force)
	{
		FlxG.camera.flash(Color,Duration,OnComplete,Force);
	}
	
	/**
	 * All screens are filled with this color and gradually return to normal.
	 * 
	 * @param	Color		The color you want to use.
	 * @param	Duration	How long it takes for the flash to fade.
	 * @param	OnComplete	A function you want to run when the flash finishes.
	 */
	static public void flash(int Color, float Duration, AFlxCamera OnComplete)
	{
		flash(Color, Duration, OnComplete, false);
	}
	
	/**
	 * All screens are filled with this color and gradually return to normal.
	 * 
	 * @param	Color		The color you want to use.
	 * @param	Duration	How long it takes for the flash to fade.
	 */
	static public void flash(int Color, float Duration)
	{
		flash(Color, Duration, null, false);
	}
	
	/**
	 * All screens are filled with this color and gradually return to normal.
	 * 
	 * @param	Color		The color you want to use.
	 */
	static public void flash(int Color)
	{
		flash(Color, 1, null, false);
	}
	
	/**
	 * All screens are filled with this color and gradually return to normal.
	 */
	static public void flash()
	{
		flash(0xFFFFFFFF, 1, null, false);
	}
	
	
	/**
	 * The screen is gradually filled with this color.
	 * 
	 * @param	Color		The color you want to use.
	 * @param	Duration	How long it takes for the fade to finish.
	 * @param	OnComplete	A function you want to run when the fade finishes.
	 * @param	Force		Force the effect to reset.
	 */
	static public void fade(int Color, float Duration, AFlxCamera OnComplete, boolean Force)
	{
		FlxG.camera.fade(Color,Duration,OnComplete,Force);
	}
	
	/**
	 * The screen is gradually filled with this color.
	 * 
	 * @param	Color		The color you want to use.
	 * @param	Duration	How long it takes for the fade to finish.
	 * @param	OnComplete	A function you want to run when the fade finishes.
	 */
	static public void fade(int Color, float Duration, AFlxCamera OnComplete)
	{
		FlxG.camera.fade(Color,Duration,OnComplete,false);
	}
	
	/**
	 * The screen is gradually filled with this color.
	 * 
	 * @param	Color		The color you want to use.
	 * @param	Duration	How long it takes for the fade to finish.
	 */
	static public void fade(int Color, float Duration)
	{
		FlxG.camera.fade(Color,Duration,null,false);
	}
	
	/**
	 * The screen is gradually filled with this color.
	 * 
	 * @param	Color		The color you want to use.
	 */
	static public void fade(int Color)
	{
		FlxG.camera.fade(Color,1,null,false);
	}
	
	/**
	 * The screen is gradually filled with this color.
	 */
	static public void fade()
	{
		FlxG.camera.fade(0xFF000000,1,null,false);
	}
	
	
	/**
	 * A simple screen-shake effect.
	 * 
	 * @param	Intensity	Percentage of screen size representing the maximum distance that the screen can move while shaking.
	 * @param	Duration	The length in seconds that the shaking effect should last.
	 * @param	OnComplete	A function you want to run when the shake effect finishes.
	 * @param	Force		Force the effect to reset (default = true, unlike flash() and fade()!).
	 * @param	Direction	Whether to shake on both axes, just up and down, or just side to side (use class constants SHAKE_BOTH_AXES, SHAKE_VERTICAL_ONLY, or SHAKE_HORIZONTAL_ONLY).  Default value is SHAKE_BOTH_AXES (0).
	 */
	static public void shake(float Intensity, float Duration, AFlxCamera OnComplete, boolean Force, int Direction)
	{
		FlxG.camera.shake(Intensity,Duration,OnComplete,Force,Direction);
	}
	
	/**
	 * A simple screen-shake effect.
	 * 
	 * @param	Intensity	Percentage of screen size representing the maximum distance that the screen can move while shaking.
	 * @param	Duration	The length in seconds that the shaking effect should last.
	 * @param	OnComplete	A function you want to run when the shake effect finishes.
	 * @param	Force		Force the effect to reset (default = true, unlike flash() and fade()!).
	 */
	static public void shake(float Intensity, float Duration, AFlxCamera OnComplete, boolean Force)
	{
		FlxG.camera.shake(Intensity,Duration,OnComplete,Force,0);
	}
	
	/**
	 * A simple screen-shake effect.
	 * 
	 * @param	Intensity	Percentage of screen size representing the maximum distance that the screen can move while shaking.
	 * @param	Duration	The length in seconds that the shaking effect should last.
	 * @param	OnComplete	A function you want to run when the shake effect finishes.
	 */
	static public void shake(float Intensity, float Duration, AFlxCamera OnComplete)
	{
		FlxG.camera.shake(Intensity,Duration,OnComplete,false,0);
	}
	
	/**
	 * A simple screen-shake effect.
	 * 
	 * @param	Intensity	Percentage of screen size representing the maximum distance that the screen can move while shaking.
	 * @param	Duration	The length in seconds that the shaking effect should last.
	 */
	static public void shake(float Intensity, float Duration)
	{
		FlxG.camera.shake(Intensity,Duration,null,false,0);
	}
	
	/**
	 * A simple screen-shake effect.
	 * 
	 * @param	Intensity	Percentage of screen size representing the maximum distance that the screen can move while shaking.
	 */
	static public void shake(float Intensity)
	{
		FlxG.camera.shake(Intensity,0.5f,null,false,0);
	}
	
	/**
	 * A simple screen-shake effect.
	 */
	static public void shake()
	{
		FlxG.camera.shake(0.05f,0.5f,null,false,0);
	}
	
	
	/**
	 * Get and set the background color of the game.
	 * Get functionality is equivalent to FlxG.camera.bgColor.
	 * Set functionality sets the background color of all the current cameras.
	 */
	static public int getBgColor()
	{
		if(FlxG.camera == null)
			return 0xff000000;
		else
			return FlxG.camera.bgColor;
	}
	
	static public void setBgColor(int Color)
	{
		int i = 0;
		int l = FlxG.cameras.size;
		while(i < l)
			(FlxG.cameras.get(i++)).setColor(Color);
	}
	
	
	
	/**
	 *e Call this function to see if one <code>FlxObject</code> overlaps another.
	 * Can be called with one object and one group, or two groups, or two objects,
	 * whatever floats your boat! For maximum performance try bundling a lot of objects
	 * together using a <code>FlxGroup</code> (or even bundling groups together!).
	 * 
	 * <p>NOTE: does NOT take objects' scrollfactor into account, all overlaps are checked in world space.</p>
	 * 
	 * @param	ObjectOrGroup1	The first object or group you want to check.
	 * @param	ObjectOrGroup2	The second object or group you want to check.  If it is the same as the first, flixel knows to just do a comparison within that group.
	 * @param	NotifyCallback	A function with two <code>FlxObject</code> parameters - e.g. <code>myOverlapFunction(Object1:FlxObject,Object2:FlxObject)</code> - that is called if those two objects overlap.
	 * @param	ProcessCallback	A function with two <code>FlxObject</code> parameters - e.g. <code>myOverlapFunction(Object1:FlxObject,Object2:FlxObject)</code> - that is called if those two objects overlap.  If a ProcessCallback is provided, then NotifyCallback will only be called if ProcessCallback returns true for those objects!
	 * 
	 * @return	Whether any overlaps were detected.
	 */
	static public boolean overlap(FlxBasic ObjectOrGroup1, FlxBasic ObjectOrGroup2, AFlxG NotifyCallback, AFlxObject ProcessCallback)
	{
		if(ObjectOrGroup1 == null)
			ObjectOrGroup1 = FlxG.getState();
		if((ObjectOrGroup2 == ObjectOrGroup1) || ((ObjectOrGroup2 != null) && ObjectOrGroup2.equals(ObjectOrGroup1)))
			ObjectOrGroup2 = null;
		FlxQuadTree.divisions = FlxG.worldDivisions;
		FlxQuadTree quadTree = new FlxQuadTree(FlxG.worldBounds.x,FlxG.worldBounds.y,FlxG.worldBounds.width,FlxG.worldBounds.height);
		quadTree.load(ObjectOrGroup1,ObjectOrGroup2,NotifyCallback,ProcessCallback);
		boolean result = quadTree.execute();
		quadTree.destroy();
		return result;
	}
	
	
	/**
	 * Call this function to see if one <code>FlxObject</code> overlaps another.
	 * Can be called with one object and one group, or two groups, or two objects,
	 * whatever floats your boat! For maximum performance try bundling a lot of objects
	 * together using a <code>FlxGroup</code> (or even bundling groups together!).
	 * 
	 * <p>NOTE: does NOT take objects' scrollfactor into account, all overlaps are checked in world space.</p>
	 * 
	 * @param	ObjectOrGroup1	The first object or group you want to check.
	 * @param	ObjectOrGroup2	The second object or group you want to check.  If it is the same as the first, flixel knows to just do a comparison within that group.
	 * @param	NotifyCallback	A function with two <code>FlxObject</code> parameters - e.g. <code>myOverlapFunction(Object1:FlxObject,Object2:FlxObject)</code> - that is called if those two objects overlap.
	 * 
	 * @return	Whether any oevrlaps were detected.
	 */
	static public boolean overlap(FlxBasic ObjectOrGroup1, FlxBasic ObjectOrGroup2, AFlxG NotifyCallback)
	{
		return overlap(ObjectOrGroup1, ObjectOrGroup2, NotifyCallback, null);
	}
	
	/**
	 * Call this function to see if one <code>FlxObject</code> overlaps another.
	 * Can be called with one object and one group, or two groups, or two objects,
	 * whatever floats your boat! For maximum performance try bundling a lot of objects
	 * together using a <code>FlxGroup</code> (or even bundling groups together!).
	 * 
	 * <p>NOTE: does NOT take objects' scrollfactor into account, all overlaps are checked in world space.</p>
	 * 
	 * @param	ObjectOrGroup1	The first object or group you want to check.
	 * @param	ObjectOrGroup2	The second object or group you want to check.  If it is the same as the first, flixel knows to just do a comparison within that group.
	 * 
	 * @return	Whether any oevrlaps were detected.
	 */
	static public boolean overlap(FlxBasic ObjectOrGroup1, FlxBasic ObjectOrGroup2)
	{
		return overlap(ObjectOrGroup1, ObjectOrGroup2, null, null);
	}
	
	/**
	 * Call this function to see if one <code>FlxObject</code> overlaps another.
	 * Can be called with one object and one group, or two groups, or two objects,
	 * whatever floats your boat! For maximum performance try bundling a lot of objects
	 * together using a <code>FlxGroup</code> (or even bundling groups together!).
	 * 
	 * <p>NOTE: does NOT take objects' scrollfactor into account, all overlaps are checked in world space.</p>
	 * 
	 * @param	ObjectOrGroup1	The first object or group you want to check.
	 * 
	 * @return	Whether any oevrlaps were detected.
	 */
	static public boolean overlap(FlxBasic ObjectOrGroup1)
	{
		return overlap(ObjectOrGroup1, null, null, null);
	}
	
	/**
	 * Call this function to see if one <code>FlxObject</code> overlaps another.
	 * Can be called with one object and one group, or two groups, or two objects,
	 * whatever floats your boat! For maximum performance try bundling a lot of objects
	 * together using a <code>FlxGroup</code> (or even bundling groups together!).
	 * 
	 * <p>NOTE: does NOT take objects' scrollfactor into account, all overlaps are checked in world space.</p>
	 * 
	 * @return	Whether any overlaps were detected.
	 */
	static public boolean overlap()
	{
		return overlap(null, null, null, null);
	}
	
	
	/**
	 * Call this function to see if one <code>FlxObject</code> collides with another.
	 * Can be called with one object and one group, or two groups, or two objects,
	 * whatever floats your boat! For maximum performance try bundling a lot of objects
	 * together using a <code>FlxGroup</code> (or even bundling groups together!).
	 * 
	 * <p>This function just calls FlxG.overlap and presets the ProcessCallback parameter to FlxObject.separate.
	 * To create your own collision logic, write your own ProcessCallback and use FlxG.overlap to set it up.</p>
	 * 
	 * <p>NOTE: does NOT take objects' scrollfactor into account, all overlaps are checked in world space.</p>
	 * 
	 * @param	ObjectOrGroup1	The first object or group you want to check.
	 * @param	ObjectOrGroup2	The second object or group you want to check.  If it is the same as the first, flixel knows to just do a comparison within that group.
	 * @param	NotifyCallback	A function with two <code>FlxObject</code> parameters - e.g. <code>myOverlapFunction(Object1:FlxObject,Object2:FlxObject)</code> - that is called if those two objects overlap.
	 * 
	 * @return	Whether any objects were successfully collided/separated.
	 */
	static public boolean collide(FlxBasic ObjectOrGroup1, FlxBasic ObjectOrGroup2, AFlxG NotifyCallback)
	{		
		return overlap(ObjectOrGroup1, ObjectOrGroup2, NotifyCallback, new AFlxObject()
		{				
			@Override
			public boolean onProcessCallback(FlxObject Object1, FlxObject Object2)
			{
				return FlxObject.separate(Object1, Object2);
			}
		});
	}
	
	/**
	 * Call this function to see if one <code>FlxObject</code> collides with another.
	 * Can be called with one object and one group, or two groups, or two objects,
	 * whatever floats your boat! For maximum performance try bundling a lot of objects
	 * together using a <code>FlxGroup</code> (or even bundling groups together!).
	 * 
	 * <p>This function just calls FlxG.overlap and presets the ProcessCallback parameter to FlxObject.separate.
	 * To create your own collision logic, write your own ProcessCallback and use FlxG.overlap to set it up.</p>
	 * 
	 * <p>NOTE: does NOT take objects' scrollfactor into account, all overlaps are checked in world space.</p>
	 * 
	 * @param	ObjectOrGroup1	The first object or group you want to check.
	 * @param	ObjectOrGroup2	The second object or group you want to check.  If it is the same as the first, flixel knows to just do a comparison within that group.
	 * 
	 * @return	Whether any objects were successfully collided/separated.
	 */
	static public boolean collide(FlxBasic ObjectOrGroup1, FlxBasic ObjectOrGroup2)
	{		
		return overlap(ObjectOrGroup1, ObjectOrGroup2, null, new AFlxObject()
		{				
			@Override
			public boolean onProcessCallback(FlxObject Object1, FlxObject Object2)
			{
				return FlxObject.separate(Object1, Object2);
			}
		});
	}
	
	/**
	 * Call this function to see if one <code>FlxObject</code> collides with another.
	 * Can be called with one object and one group, or two groups, or two objects,
	 * whatever floats your boat! For maximum performance try bundling a lot of objects
	 * together using a <code>FlxGroup</code> (or even bundling groups together!).
	 * 
	 * <p>This function just calls FlxG.overlap and presets the ProcessCallback parameter to FlxObject.separate.
	 * To create your own collision logic, write your own ProcessCallback and use FlxG.overlap to set it up.</p>
	 * 
	 * <p>NOTE: does NOT take objects' scrollfactor into account, all overlaps are checked in world space.</p>
	 * 
	 * @param	ObjectOrGroup1	The first object or group you want to check.
	 * 
	 * @return	Whether any objects were successfully collided/separated.
	 */
	static public boolean collide(FlxBasic ObjectOrGroup1)
	{
		return overlap(ObjectOrGroup1, null, null, new AFlxObject()
		{				
			@Override
			public boolean onProcessCallback(FlxObject Object1, FlxObject Object2)
			{
				return FlxObject.separate(Object1, Object2);
			}
		});
	}
	
	/**
	 * Call this function to see if one <code>FlxObject</code> collides with another.
	 * Can be called with one object and one group, or two groups, or two objects,
	 * whatever floats your boat! For maximum performance try bundling a lot of objects
	 * together using a <code>FlxGroup</code> (or even bundling groups together!).
	 * 
	 * <p>This function just calls FlxG.overlap and presets the ProcessCallback parameter to FlxObject.separate.
	 * To create your own collision logic, write your own ProcessCallback and use FlxG.overlap to set it up.</p>
	 * 
	 * <p>NOTE: does NOT take objects' scrollfactor into account, all overlaps are checked in world space.</p>
	 * 
	 * @return	Whether any objects were successfully collided/separated.
	 */
	static public boolean collide()
	{
		return overlap(null, null, null, new AFlxObject()
		{				
			@Override
			public boolean onProcessCallback(FlxObject Object1, FlxObject Object2)
			{
				return FlxObject.separate(Object1, Object2);
			}
		});
	}
	
	
	/**
	 * Adds a new plugin to the global plugin array.
	 * 
	 * @param	Plugin	Any object that extends FlxBasic. Useful for managers and other things.  See org.flixel.plugin for some examples!
	 * 
	 * @return	The same <code>FlxBasic</code>-based plugin you passed in.
	 */
	static public FlxBasic addPlugin(FlxBasic Plugin)
	{
		//Don't add repeats
		Array<FlxBasic> pluginList = FlxG.plugins;
		int i = 0;
		int l = pluginList.size;
		while(i < l)
		{
			if(pluginList.get(i++).toString().equals(Plugin.toString()))
				return Plugin;
		}
		
		//no repeats! safe to add a new instance of this plugin
		pluginList.add(Plugin);
		return Plugin;
	}

	
	/**
	 * Retrieves a plugin based on its class name from the global plugin array.
	 * 
	 * @param	ClassType	The class name of the plugin you want to retrieve. See the <code>FlxPath</code> or <code>FlxTimer</code> constructors for example usage.
	 * 
	 * @return	The plugin object, or null if no matching plugin was found.
	 */
	static public FlxBasic getPlugin(Class<?> ClassType)
	{
		Array<FlxBasic> pluginList = FlxG.plugins;
		int i = 0;
		int l = pluginList.size;
		while(i < l)
		{
			if(pluginList.get(i).getClass().equals(ClassType))
				return plugins.get(i);
			i++;
		}
		return null;
	}
	
	
	/**
	 * Removes an instance of a plugin from the global plugin array.
	 * 
	 * @param	Plugin	The plugin instance you want to remove.
	 * 
	 * @return	The same <code>FlxBasic</code>-based plugin you passed in.
	 */
	static public FlxBasic removePlugin(FlxBasic Plugin)
	{
		//Don't add repeats
		Array<FlxBasic> pluginList = FlxG.plugins;
		int i = pluginList.size-1;
		while(i >= 0)
		{
			if(pluginList.get(i).equals(Plugin))
				pluginList.removeIndex(i);
			i--;
		}
		return Plugin;
	}
	
	
	/**
	 * Removes an instance of a plugin from the global plugin array.
	 * 
	 * @param	ClassType	The class name of the plugin type you want removed from the array.
	 * 
	 * @return	Whether or not at least one instance of this plugin type was removed.
	 */
	static public boolean removePluginType(Class<?> ClassType)
	{
		//Don't add repeats
		boolean results = false;
		Array<FlxBasic> pluginList = FlxG.plugins;
		int i = pluginList.size-1;
		while(i >= 0)
		{
			if(pluginList.get(i).getClass().equals(ClassType))
			{
				pluginList.removeIndex(i);
				results = true;
			}
			i--;
		}
		return results;
	}
	
	/**
	 * Called by the game loop to make sure the sounds get updated each frame.
	 */
	public static void updateSounds()
	{
		if((music != null) && music.active)
			music.update();
		if((sounds != null) && sounds.active)
			sounds.update();
	}
	
	
	/**
	 * Pause all sounds currently playing.
	 */
	public static void pauseSounds()
	{
		if((music != null) && music.exists && music.active)
			music.pause();
		int i = 0;
		FlxSound sound;
		int l = sounds.length;
		while(i < l)
		{			
			sound = (FlxSound) sounds.members.get(i++);
			if((sound != null) && sound.exists && sound.active)
				sound.pause();
		}
	}


	/**
	 * Resume playing existing sounds.
	 */
	public static void resumeSounds()
	{
		if((music != null) && music.exists)
			music.play();
		int i = 0;
		FlxSound sound;
		int l = sounds.length;
		while(i < l)
		{
			sound = (FlxSound) sounds.members.get(i++);
			if((sound != null) && sound.exists)
				sound.resume();
		}
	}

	
	/**
	 * Used by the game object to call <code>update()</code> on all the plugins.
	 */
	public static void updatePlugins()
	{
		FlxBasic plugin;
		Array<FlxBasic> pluginList = FlxG.plugins;
		int i = 0;
		int l = pluginList.size;
		while(i < l)
		{
			plugin = pluginList.get(i++);
			if(plugin.exists && plugin.active)
				plugin.update();
		}
	}


	/**
	 * Used by the game object to call <code>draw()</code> on all the plugins.
	 */
	public static void drawPlugins()
	{
		FlxBasic plugin;
		Array<FlxBasic> pluginList = FlxG.plugins;
		int i = 0;
		int l = pluginList.size;
		while(i < l)
		{
			plugin = pluginList.get(i++);
			if(plugin.exists && plugin.visible)
				plugin.draw();
		}
	}

	/**
	 * Creates a new sound object from an embedded <code>Class</code> object.
	 * NOTE: Just calls FlxG.loadSound() with AutoPlay == true.
	 * 
	 * @param	EmbeddedSound	The sound you want to play.
	 * @param	Volume			How loud to play it (0 to 1).
	 * @param	Looped			Whether to loop this sound.
	 * @param	AutoDestroy		Whether to destroy this sound when it finishes playing.  Leave this value set to "false" if you want to re-use this <code>FlxSound</code> instance.
	 * 
	 * @return	A <code>FlxSound</code> object.
	 */
	static public FlxSound play(Sound EmbeddedSound, float Volume, boolean Looped, boolean AutoDestroy)
	{
		return FlxG.loadSound(EmbeddedSound, Volume, Looped, AutoDestroy, true);
	}

	/**
	 * Creates a new sound object from an embedded <code>Class</code> object.
	 * NOTE: Just calls FlxG.loadSound() with AutoPlay == true.
	 * 
	 * @param	EmbeddedSound	The sound you want to play.
	 * @param	Volume			How loud to play it (0 to 1).
	 * @param	Looped			Whether to loop this sound.
	 * 
	 * @return	A <code>FlxSound</code> object.
	 */
	public static FlxSound play(Sound EmbeddedSound, float Volume, boolean Looped)
	{
		return FlxG.loadSound(EmbeddedSound, Volume, Looped, false, true);
	}
	
	/**
	 * Creates a new sound object from an embedded <code>Class</code> object.
	 * NOTE: Just calls FlxG.loadSound() with AutoPlay == true.
	 * 
	 * @param	EmbeddedSound	The sound you want to play.
	 * @param	Volume			How loud to play it (0 to 1).
	 * 
	 * @return	A <code>FlxSound</code> object.
	 */
	public static FlxSound play(Sound EmbeddedSound, float Volume)
	{
		return FlxG.loadSound(EmbeddedSound, Volume, false, false, true);
	}

	/**
	 * Creates a new sound object from an embedded <code>Class</code> object.
	 * NOTE: Just calls FlxG.loadSound() with AutoPlay == true.
	 * 
	 * @param	EmbeddedSound	The sound you want to play.
	 * 
	 * @return	A <code>FlxSound</code> object.
	 */
	public static FlxSound play(Sound EmbeddedSound)
	{
		return FlxG.loadSound(EmbeddedSound, 1.0f, false, false, true);
	}
	
	
	/**
	 * Vibrates for the given amount of time. Note that you'll need the permission
	 * <code> <uses-permission android:name="android.permission.VIBRATE" /></code> in your manifest file in order for this to work.

	 * @param Milliseconds	The amount of time to vibrate for.
	 */
	public static void vibrate(int Milliseconds)
	{
		Gdx.input.vibrate(Milliseconds);
	}
	
	/**
	 * Vibrates for the given amount of time. Note that you'll need the permission
	 * <code> <uses-permission android:name="android.permission.VIBRATE" /></code> in your manifest file in order for this to work.
	 */
	public static void vibrate()
	{
		vibrate(1000);
	}

	/** 
	 * Vibrate with a given pattern. Pass in an array of ints that are the times at which to turn on or off the vibrator. The first
	 * one is how long to wait before turning it on, and then after that it alternates. If you want to repeat, pass the index into
	 * the pattern at which to start the repeat.
	 * 
	 * @param Pattern	An array of longs of times to turn the vibrator on or off.
	 * @param Repeat	The index into pattern at which to repeat, or -1 if you don't want to repeat. 
	 * */
	public static void vibrate(long[] Pattern, int Repeat)
	{
		Gdx.input.vibrate(Pattern, Repeat);
	}	
	
	/**
	 * Stops the vibrator.
	 * Or call directly Gdx.input.cancelVibrate().
	 */
	public static void stopVibrate()
	{
		Gdx.input.cancelVibrate();
	}
}
