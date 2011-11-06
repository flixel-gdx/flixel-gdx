package org.flixel;


import gnu.trove.map.hash.TIntObjectHashMap;

import org.flixel.event.AFlxCamera;
import org.flixel.event.AFlxG;
import org.flixel.event.AFlxObject;
import org.flixel.system.FlxQuadTree;
import org.flixel.system.input.Keyboard;
import org.flixel.system.input.Touch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class FlxG
{
	/**
	 * If you build and maintain your own version of flixel,
	 * you can give it your own name here.
	 */
	static public String LIBRARY_NAME = "flixel";
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
	 * A reference to a <code>Mouse</code> object.  Important for input!
	 */
	static public Touch touch;
	
	/**
	 * A reference to multiple <code>Mouse</code> objects. Important for multi-touch.
	 */
	static public Touch[] touches;
	
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
	static public double globalSeed;
	/**
	 * <code>FlxG.levels</code> and <code>FlxG.scores</code> are generic
	 * global variables that can be used for various cross-state stuff.
	 */
//	static public var levels:Array;
//	static public var level:int;
//	static public var scores:Array;
//	static public var score:int;
	/**
	 * <code>FlxG.saves</code> is a generic bucket for storing
	 * FlxSaves so you can access them whenever you want.
	 */
	//static public var saves:Array; 
	//static public var save:int;

	/**
	 * A reference to a <code>FlxMouse</code> object.  Important for input!
	 */
	//static public var mouse:Mouse;
	/**
	 * A reference to a <code>FlxKeyboard</code> object.  Important for input!
	 */
	static public Keyboard keys;
	
	/**
	 * A handy container for a background music object.
	 */
	static public FlxMusic music;
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
//	static protected var _cameraRect:Rectangle;
	
	/**
	 * An array container for plugins.
	 * By default flixel uses a couple of plugins:
	 * DebugPathDisplay, and TimerManager.
	 */
//	 static public var plugins:Array;
	 
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
	static public Graphics flashGfx;

	/**
	 * Internal storage system to prevent graphics from being used repeatedly in memory.
	 */
	static protected TIntObjectHashMap<TextureRegion> _cache;
	
	static public SpriteBatch batch;
	
//	public static volumeHandler:Function;
	
	
	
	
	
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
		String message;
		if(data instanceof Boolean)
			message = "" + ((Boolean) data).booleanValue();
		else if(data instanceof Character)
			message = "" + ((Character) data).charValue();
		else if(data instanceof Byte)
			message = "" + ((Byte) data).byteValue();
		else if(data instanceof Short)
			message = "" + ((Short) data).shortValue();
		else if(data instanceof Integer)
			message = "" + ((Integer) data).intValue();
		else if(data instanceof Long)
			message = "" + ((Long) data).doubleValue();
		else if(data instanceof Float)
			message = "" + ((Float) data).floatValue();
		else if(data instanceof Double)
			message = "" + ((Double) data).doubleValue();
		else
			message = "" + data;
		if((_game != null)) // && (_game._debugger != null))			
			Gdx.app.log(tag, message);
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
	static public double random()
	{
		return globalSeed = FlxU.srand(globalSeed);
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
	
	
	//TODO: this method can be deleted.
	static public void resetInput()
	{
		keys.reset();
//		mouse.reset();
		touches = null;
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
			music = new FlxMusic();
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
			FlxG.log(e.getMessage());
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
		_cache = new TIntObjectHashMap<TextureRegion>();
		width = Width;
		height = Height;
		
		
		mute = false;
		_volume = 0.5f;
		sounds = new FlxGroup();
//		volumeHandler = null;
		
		clearBitmapCache();
		
		FlxCamera.defaultZoom = Zoom;
//		FlxG._cameraRect = new Rectangle();
		FlxG.cameras = new Array<FlxCamera>();
//		useBufferLocking = false;
//		
//		plugins = new Array();
//		addPlugin(new DebugPathDisplay());
//		addPlugin(new TimerManager());
//		
		FlxG.touch = new Touch();
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
		if(touches == null)
			FlxG.touch.update(Gdx.input.getX(0), Gdx.input.getY(0));
		else
		{
			for (int i = 0; i < touches.length; i++)
				touches[i].update(Gdx.input.getX(i), Gdx.input.getY(i));
		}		
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
				if(cam.active)
					cam.draw();
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
		//FlxG.log("checkBitmapCache", Key);
//		if(_map.get(Key.hashCode()) != null)
//			FlxG.log("Hola");
		
		return (_cache.get(Key.hashCode()) != null);
	}

	/**
	 * Generates a new <code>BitmapData</code> object (a colored square) and
	 * caches it.
	 * 
	 * @param Width 	How wide the square should be.
	 * @param Height 	How high the square should be.
	 * @param Color 	What color the square should be (0xAARRGGBB)
	 * @param Unique	Ensures that the bitmap data uses a new slot in the cache.
	 * @param Key		Force the cache to use a specific Key to index the bitmap.
	 * 
	 * @return The <code>BitmapData</code> we just created.
	 *///TODO: Bug: using this will hit the performance. The Texture won't be dupilcated, so how come?
	static public TextureRegion createBitmap(int Width, int Height, int Color, boolean Unique, String Key)
	{		
		if(Key == null)
		{
			Key = Width+"x"+Height+":"+Color;
			if(Unique && (_cache.get(Key.hashCode()) != null))
			{
				// Generate a unique key
				int inc = 0;
				String ukey;
				do
				{
					ukey = Key + inc++;
				}
				while((_cache.get(ukey.hashCode()) != null));
				Key = ukey;
			}
		}
		if(!checkBitmapCache(Key))
		{
			Pixmap p = new Pixmap(Width, Height, Format.RGBA8888);			
			p.setColor(1, 1, 1, 1);
			p.fillRectangle(0, 0, Width, Height);
			_cache.put(Key.hashCode(), new TextureRegion(new Texture(p)));
			p.dispose();
		}
		return (TextureRegion) _cache.get(Key.hashCode());
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
			Key = Graphic.toString();
			if(Unique && (!_cache.get(Key.hashCode()).equals(null)))
			{
				// Generate a unique key
				int inc = 0;
				String ukey;
				do
				{
					ukey = Key + inc++;
				}
				while((_cache.get(ukey.hashCode()) != null));
				Key = ukey;
			}
		}
		// If there is no data for this key, generate the requested graphic
		if(!checkBitmapCache(Key))
			_cache.put(Key.hashCode(), Graphic);
		return _cache.get(Key.hashCode());
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
	 * @param	ProcessCallback	A function with two <code>FlxObject</code> parameters - e.g. <code>myOverlapFunction(Object1:FlxObject,Object2:FlxObject)</code> - that is called if those two objects overlap.  If a ProcessCallback is provided, then NotifyCallback will only be called if ProcessCallback returns true for those objects!
	 * 
	 * @return	Whether any oevrlaps were detected.
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
	 * @return	Whether any oevrlaps were detected.
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
	 * Called by the game loop to make sure the sounds get updated each frame.
	 */
	public static void updateSounds()
	{
		if((music != null) && music.active)
			music.update();
		if((sounds != null) && sounds.active)
			sounds.update();
	}

	public static void updatePlugins()
	{
		// TODO Auto-generated method stub
		
	}


	public static void drawPlugins()
	{
		// TODO Auto-generated method stub
		
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
	 * Enables multi-touch support.
	 * 
	 * @param amountOfPointers	The amount of allowed pointers. Default is 2.
	 */
	static public void enableMultiTouch(int amountOfPointers)
	{
		FlxG.touches = new Touch[amountOfPointers];
		FlxG.touches[0] = touch;
		for(int i = 1; i < touches.length; i++)
		{
			touches[i] = new Touch();
		}
	}
	
	/**
	 * Enables multi-touch support. Default is 2 pointers.
	 */
	static public void enableMultiTouch()
	{
		enableMultiTouch(2);
	}
}
