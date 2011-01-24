package org.flixel;

import java.util.ArrayList;
import java.util.Hashtable;

import org.flixel.data.FlxDigitalPad;
import org.flixel.data.FlxFade;
import org.flixel.data.FlxFlash;
import org.flixel.data.FlxKeyboard;
import org.flixel.data.FlxMouse;
import org.flixel.data.FlxPanel;
import org.flixel.data.FlxQuake;
import org.flixel.data.FlxTouch;
import org.flixel.data.R;

import android.view.SurfaceView;
import flash.display.BitmapData;
import flash.geom.Matrix;
import flash.geom.Point;

public class FlxG
{
	
	public static Class<? extends FlxState> initialGameState;
	/**
	 * Internal tracker for game object (so we can pause & unpause)
	 */
	static protected FlxGame _game;
	/**
	 * Internal tracker for game pause state.
	 */
	static protected boolean _pause;
	/**
	 * Internal tracker for disable pause state.
	 */
	static protected boolean _disablePause;
	/**
	 * Whether you are running in Debug or Release mode. Set automatically by
	 * <code>FlxFactory</code> during startup.
	 */
	static public boolean debug;
	/**
	 * Set <code>showBounds</code> to true to display the bounding boxes of the
	 * in-game objects.
	 */
	static public boolean showBounds;

	/**
	 * Represents the amount of time in seconds that passed since last frame.
	 */
	static public float elapsed;
	/**
	 * Essentially locks the framerate to a minimum value - any slower and
	 * you'll get slowdown instead of frameskip; default is 1/30th of a second.
	 */
	static public float maxElapsed;
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
	 * The specification width of the mobile
	 */
	static public int specWidth;
	/**
	 * The specification height of the mobile
	 */
	static public int specHeight;
	/**
	 * The ratio
	 */
	static public float ratio;
	/**
	 * <code>FlxG.levels</code> and <code>FlxG.scores</code> are generic global
	 * variables that can be used for various cross-state stuff.
	 */
	static public ArrayList<Integer> levels;
	static public int level;
	static public ArrayList<Integer> scores;
	static public int score;
	/**
	 * <code>FlxG.saves</code> is a generic bucket for storing FlxSaves so you
	 * can access them whenever you want.
	 */
	static public ArrayList<FlxSave> saves;
	static public int save;

	/**
	 * A reference to a <code>FlxMouse</code> object. Important for input!
	 */
	static public FlxMouse mouse;
	/**
	 * A reference to a <code>FlxKeyboard</code> object. Important for input!
	 */
	static public FlxKeyboard keys;
	/**
	 * A reference to a <code>FlxTouch</code> object. Important for input!
	 */
	static public FlxTouch touch;	
	/**
	 * A reference to a <code>FlxDigitalPad</code> object. Important for input!
	 */
	static public FlxDigitalPad dpad;

	/**
	 * A handy container for a background music object.
	 */
	static public FlxSound music;
	/**
	 * A list of all the sounds being played in the game.
	 */
	static public ArrayList<FlxSound> sounds;
	/**
	 * Internal flag for whether or not the game is muted.
	 */
	static protected boolean _mute;
	/**
	 * Internal volume level, used for global sound control.
	 */
	static protected float _volume;

	/**
	 * Tells the camera to follow this <code>FlxCore</code> object around.
	 */
	static public FlxObject followTarget;
	/**
	 * Used to force the camera to look ahead of the <code>followTarget</code>.
	 */
	static public Point followLead;
	/**
	 * Used to smoothly track the camera as it follows.
	 */
	static public float followLerp;
	/**
	 * Stores the top and left edges of the camera area.
	 */
	static public Point followMin;
	/**
	 * Stores the bottom and right edges of the camera area.
	 */
	static public Point followMax;
	/**
	 * Internal, used to assist camera and scrolling.
	 */
	static protected Point _scrollTarget;

	/**
	 * Stores the basic parallax scrolling values.
	 */
	static public Point scroll;
	/**
	 * Reference to the active graphics buffer. Can also be referenced via
	 * <code>FlxState.screen</code>.
	 */
	static public BitmapData buffer;
	/**
	 * Internal storage system to prevent graphics from being used repeatedly in
	 * memory.
	 */
	static protected Hashtable<String, BitmapData> _cache;

	/**
	 * The support panel (twitter, reddit, stumbleupon, paypal, etc) visor thing
	 */
	static public FlxPanel panel;
	/**
	 * A special effect that shakes the screen. Usage: FlxG.quake.start();
	 */
	static public FlxQuake quake;
	/**
	 * A special effect that flashes a color on the screen. Usage:
	 * FlxG.flash.start();
	 */
	static public FlxFlash flash;
	/**
	 * A special effect that fades a color onto the screen. Usage:
	 * FlxG.fade.start();
	 */
	static public FlxFade fade;


	/**
	 * Set <code>pause</code> to true to pause the game, all sounds, and display
	 * the pause popup.
	 */
	static public boolean getPause()
	{
		return _pause;
	}

	/**
	 * @private
	 */
	static public void setPause(boolean Pause)
	{
		if(_disablePause)
			return;
		
		boolean op = _pause;
		_pause = Pause;
		if(_pause != op)
		{
			if(_pause)
			{
				_game.pauseGame();
				pauseSounds();
				play(R.raw.pausein);
			}
			else
			{
				_game.unpauseGame();
				playSounds();
				play(R.raw.pauseout);
			}
		}
	}
	
	static public void setDisablePause(boolean Pause)
	{
		_disablePause = Pause;
	}

	
	/**
	 * Reset the input helper objects (useful when changing screens or states)
	 */
	static public void resetInput()
	{
		keys.reset();
		touch.reset();
	}

	/**
	 * Set up and play a looping background soundtrack.
	 * 
	 * @param Music The sound file you want to loop in the background.
	 * @param Volume How loud the sound should be, from 0 to 1.
	 */
	static public void playMusic(int Music, float Volume)
	{
		if(music == null)
			music = new FlxSound();
		else if(music.active)
			music.stop();
		music.loadEmbedded(Music, true);
		music.setVolume(Volume);
		music.survive = true;
		music.play();
	}

	static public void playMusic(int Music)
	{
		playMusic(Music, 1);
	}

	/**
	 * Creates a new sound object from an embedded <code>Class</code> object.
	 * 
	 * @param EmbeddedSound The sound you want to play.
	 * @param Volume How loud to play it (0 to 1).
	 * @param Looped Whether or not to loop this sound.
	 * 
	 * @return A <code>FlxSound</code> object.
	 */
	static public FlxSound play(int EmbeddedSound, float Volume, boolean Looped)
	{
		int i = 0;
		int sl = sounds.size();
		while(i < sl)
		{
			if(!sounds.get(i).active)
				break;
			i++;
		}		
		if(sl == i)
			sounds.add(i, new FlxSound());
		FlxSound s = sounds.get(i);
		s.loadEmbedded(EmbeddedSound, Looped);
		s.setVolume(Volume);
		s.play();
		return s;
	}

	static public FlxSound play(int EmbeddedSound, float Volume)
	{
		return play(EmbeddedSound, Volume, false);
	}

	static public FlxSound play(int EmbeddedSound)
	{
		return play(EmbeddedSound, 1, false);
	}

	/**
	 * Creates a new sound object from a URL.
	 * 
	 * @param EmbeddedSound The sound you want to play.
	 * @param Volume How loud to play it (0 to 1).
	 * @param Looped Whether or not to loop this sound.
	 * 
	 * @return A FlxSound object.
	 */
	static public FlxSound stream(String URL, float Volume, boolean Looped)
	{
		int i = 0;
		int sl = sounds.size();
		while(i < sl)
		{
			if(!sounds.get(i).active)
				break;
			i++;
		}
		if(sl == i)
			sounds.add(i, new FlxSound());
		FlxSound s = sounds.get(i);
		s.loadStream(URL, Looped);
		s.setVolume(Volume);
		s.play();
		return s;
	}

	static public FlxSound stream(String URL, float Volume)
	{
		return stream(URL, Volume, false);
	}

	static public FlxSound stream(String URL)
	{
		return stream(URL, 1, false);
	}

	/**
	 * Set <code>mute</code> to true to turn off the sound.
	 * 
	 * @default false
	 */
	static public boolean getMute()
	{
		return _mute;
	}

	/**
	 * @private
	 */
	static public void setMute(boolean Mute)
	{
		_mute = Mute;
		changeSounds();
	}

	/**
	 * Get a number that represents the mute state that we can multiply into a
	 * sound transform.
	 * 
	 * @return An unsigned integer - 0 if muted, 1 if not muted.
	 */
	static public int getMuteValue()
	{
		if(_mute)
			return 0;
		else
			return 1;
	}

	/**
	 * Set <code>volume</code> to a number between 0 and 1 to change the global
	 * volume.
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
		changeSounds();
	}

	/**
	 * Called by FlxGame on state changes to stop and destroy sounds.
	 * 
	 * @param ForceDestroy Kill sounds even if they're flagged
	 *        <code>survive</code>.
	 */
	static void destroySounds(boolean ForceDestroy)
	{
		if(sounds == null)
			return;
		if((music != null) && (ForceDestroy || !music.survive))
			music.destroy();
		int i = 0;
		FlxSound s;
		int sl = sounds.size();
		while(i < sl)
		{
			s = sounds.get(i++);
			if((s != null) && (ForceDestroy || !s.survive))
				s.destroy();
		}
	}

	static void destroySounds()
	{
		destroySounds(false);
	}

	/**
	 * An internal function that adjust the volume levels and the music channel
	 * after a change.
	 */
	static protected void changeSounds()
	{
		if((music != null) && music.active)
			music.updateTransform();
		int i = 0;
		FlxSound s;
		int sl = sounds.size();
		while(i < sl)
		{
			s = sounds.get(i++);
			if((s != null) && s.active)
				s.updateTransform();
		}
	}

	/**
	 * Called by the game loop to make sure the sounds get updated each frame.
	 */
	static void updateSounds()
	{
		if((music != null) && music.active)
			music.update();
		int i = 0;
		FlxSound s;
		int sl = sounds.size();
		while(i < sl)
		{
			s = sounds.get(i++);
			if((s != null) && s.active)
				s.update();
		}
	}

	/**
	 * Internal helper, pauses all game sounds.
	 */
	static protected void pauseSounds()
	{
		if((music != null) && music.active)
			music.pause();
		int i = 0;
		FlxSound s;
		int sl = sounds.size();
		while(i < sl)
		{
			s = sounds.get(i++);
			if((s != null) && s.active)
				s.pause();
		}
	}

	/**
	 * Internal helper, plays all game sounds.
	 */
	static protected void playSounds()
	{
		if((music != null) && music.active)
			music.play();
		int i = 0;
		FlxSound s;
		int sl = sounds.size();
		while(i < sl)
		{
			s = sounds.get(i++);
			if((s != null) && s.active)
				s.play();
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
	 * @param Width How wide the square should be.
	 * @param Height How high the square should be.
	 * @param Color What color the square should be (0xAARRGGBB)
	 * 
	 * @return The <code>BitmapData</code> we just created.
	 */
	static public BitmapData createBitmap(int Width, int Height, int Color, boolean Unique, String Key)
	{
		String key = Key;
		if(key == null)
		{
			key = Width + "x" + Height + ":" + Color;
			if(Unique && (_cache.get(key) != null))
			{
				// Generate a unique key
				int inc = 0;
				String ukey;
				do
				{
					ukey = key + inc++;
				}
				while((_cache.get(ukey) != null));
				key = ukey;
			}
		}
		if(!checkBitmapCache(key))
			_cache.put(key, new BitmapData(Width, Height, true, Color));
		return _cache.get(key);
	}

	/**
	 * Loads a bitmap from a file, caches it, and generates a horizontally
	 * flipped version if necessary.
	 * 
	 * @param Graphic The image file that you want to load.
	 * @param Reverse Whether to generate a flipped version.
	 * 
	 * @return The <code>BitmapData</code> we just created.
	 */
	static public BitmapData addBitmap(int Graphic, boolean Reverse, boolean Unique, String Key)
	{
		boolean needReverse = false;
		String key = "" + Graphic;
		if(_cache.get(key) == null)
		{
			key = "" + Graphic;
			if(Unique && (!_cache.get(key).equals(null)))
			{
				// Generate a unique key
				int inc = 0;
				String ukey;
				do
				{
					ukey = key + inc++;
				}
				while((_cache.get(ukey) != null));
				key = ukey;
			}
		}
		// If there is no data for this key, generate the requested graphic
		if(!checkBitmapCache(key))
		{
			_cache.put(key, FlxResource.getImage(Graphic));
			if(Reverse)
				needReverse = true;
		}
		BitmapData pixels = _cache.get(key);
		if(!needReverse && Reverse && (pixels.width == FlxResource.getImage(Graphic).getBitmap().getWidth()))
			needReverse = true;
		if(needReverse)
		{
			BitmapData newPixels = new BitmapData(pixels.width << 1, pixels.height, true, 0x00000000);
			newPixels.draw(pixels);
			Matrix mtx = new Matrix();
			mtx.scale(-1, 1);
			mtx.translate(newPixels.width, 0);
			newPixels.draw(pixels, mtx);
			pixels = newPixels;
		}
		return pixels;
	}

	static public BitmapData addBitmap(int Graphic, boolean Reverse, boolean Unique)
	{
		return addBitmap(Graphic, Reverse, Unique, null);
	}

	static public BitmapData addBitmap(int Graphic, boolean Reverse)
	{
		return addBitmap(Graphic, Reverse, false, null);
	}

	static public BitmapData addBitmap(int Graphic)
	{
		return addBitmap(Graphic, false, false, null);
	}

	/**
	 * Tells the camera subsystem what <code>FlxCore</code> object to follow.
	 * 
	 * @param Target The object to follow.
	 * @param Lerp How much lag the camera should have (can help smooth out the
	 *        camera movement).
	 */
	static public void follow(FlxObject Target, float Lerp)
	{
		followTarget = Target;
		followLerp = Lerp;
		_scrollTarget.x = (width >> 1) - followTarget.x - (followTarget.width >> 1);
		_scrollTarget.y = (height >> 1) - followTarget.y - (followTarget.height >> 1);
		scroll.x = _scrollTarget.x;
		scroll.y = _scrollTarget.y;
		doFollow();
	}

	/**
	 * Specify an additional camera component - the velocity-based "lead", or
	 * amount the camera should track in front of a sprite.
	 * 
	 * @param LeadX Percentage of X velocity to add to the camera's motion.
	 * @param LeadY Percentage of Y velocity to add to the camera's motion.
	 */
	static public void followAdjust(float LeadX, float LeadY)
	{
		followLead = new Point(LeadX, LeadY);
	}

	static public void followAdjust(float LeadX)
	{
		followAdjust(LeadX, 0);
	}

	static public void followAdjust()
	{
		followAdjust(0, 0);
	}

	/**
	 * Specify the boundaries of the level or where the camera is allowed to
	 * move.
	 * 
	 * @param MinX The smallest X value of your level (usually 0).
	 * @param MinY The smallest Y value of your level (usually 0).
	 * @param MaxX The largest X value of your level (usually the level width).
	 * @param MaxY The largest Y value of your level (usually the level height).
	 * @param UpdateWorldBounds Whether the quad tree's dimensions should be
	 *        updated to match.
	 */
	static public void followBounds(int MinX, int MinY, int MaxX, int MaxY, boolean UpdateWorldBounds)
	{
		followMin = new Point(-MinX, -MinY);
		followMax = new Point(-MaxX + width, -MaxY + height);
		if(followMax.x > followMin.x)
			followMax.x = followMin.x;
		if(followMax.y > followMin.y)
			followMax.y = followMin.y;
		if(UpdateWorldBounds)
			FlxU.setWorldBounds(MinX, MinY, MaxX - MinX, MaxY - MinY);
		doFollow();
	}
	
	static public void followBounds(int MinX, int MinY, int MaxX, int MaxY)
	{
		followBounds(MinX, MinY, MaxX, MaxY, true);
	}
	
	static public void followBounds(int MinX, int MinY, int MaxX)
	{
		followBounds(MinX, MinY, MaxX, 0, true);
	}
	
	static public void followBounds(int MinX, int MinY)
	{
		followBounds(MinX, MinY, 0, 0, true);
	}
	
	static public void followBounds(int MinX)
	{
		followBounds(MinX, 0, 0, 0, true);
	}
	
	static public void followBounds()
	{
		followBounds(0, 0, 0, 0, true);
	}

	/**
	 * Retrieves the Flash stage object (required for event listeners)
	 * 
	 * @return A Flash <code>MovieClip</code> object.
	 */

	static public SurfaceView getStage()
	{
		if(_game._state != null)
			return _game.stage;
		return null;
	}

	/**
	 * Safely access the current game state.
	 */
	static public FlxState getState()
	{
		return _game._state;
	}

	/**
	 * @private
	 */
	static public void setState(Class<? extends FlxState> State)
	{
		_game.switchState(State);
	}

	/**
	 * Stops and resets the camera.
	 */
	static public void unfollow()
	{
		followTarget = null;
		followLead = null;
		followLerp = 1;
		followMin = null;
		followMax = null;
		if(scroll == null)
			scroll = new Point();
		else
			scroll.x = scroll.y = 0;
		if(_scrollTarget == null)
			_scrollTarget = new Point();
		else
			_scrollTarget.x = _scrollTarget.y = 0;
	}

	/**
	 * Called by <code>FlxGame</code> to set up <code>FlxG</code> during
	 * <code>FlxGame</code>'s constructor.
	 */
	public static void setGameData(FlxGame Game, int Width, int Height)
	{
		_game = Game;
		_cache = new Hashtable<String, BitmapData>();
		width = Width;
		height = Height;
		_mute = false;
		_volume = 0.5f;
		sounds = new ArrayList<FlxSound>();
		keys = new FlxKeyboard();
		touch = new FlxTouch();
//		dpad = new FlxDigitalPad(FlxG.width/2, FlxG.height/2, 1);

		scroll = null;
		_scrollTarget = null;
		unfollow();
		FlxG.levels = new ArrayList<Integer>();
		FlxG.scores = new ArrayList<Integer>();
		level = 0;
		score = 0;
		setPause(false);
		timeScale = 1;
		maxElapsed = 0.0333333f;
		FlxG.elapsed = 0;
		showBounds = false;

//		panel = new FlxPanel(0, 0, 0, 0);
//		quake = new FlxQuake(1);
//		flash = new FlxFlash();
//		fade = new FlxFade();

		FlxU.setWorldBounds(0, 0, FlxG.width, FlxG.height);
	}

	/**
	 * Internal function that updates the camera and parallax scrolling.
	 */
	static void doFollow()
	{
		if(followTarget != null)
		{
			_scrollTarget.x = (width >> 1) - followTarget.x - (followTarget.width >> 1);
			_scrollTarget.y = (height >> 1) - followTarget.y - (followTarget.height >> 1);
			if((followLead != null) && (followTarget instanceof FlxSprite))
			{
				_scrollTarget.x -= ((FlxSprite) followTarget).velocity.x * followLead.x;
				_scrollTarget.y -= ((FlxSprite) followTarget).velocity.y * followLead.y;
			}
			scroll.x += (_scrollTarget.x - scroll.x) * followLerp * FlxG.elapsed;
			scroll.y += (_scrollTarget.y - scroll.y) * followLerp * FlxG.elapsed;

			if(followMin != null)
			{
				if(scroll.x > followMin.x)
					scroll.x = followMin.x;
				if(scroll.y > followMin.y)
					scroll.y = followMin.y;
			}

			if(followMax != null)
			{
				if(scroll.x < followMax.x)
					scroll.x = followMax.x;
				if(scroll.y < followMax.y)
					scroll.y = followMax.y;
			}
		}
	}

	/**
	 * Calls update on the keyboard and mouse input tracking objects.
	 */
	static void updateInput()
	{
		touch.update();
		keys.update();
		if(dpad != null)
			dpad.update();
	}
	
	
	/**
	 * Create a d-pad. It will be added on screen.
	 * @param X		The x-pos on screen.
	 * @param Y		The y-pos on screen.
	 * @param Scale	The size of the d-pad.
	 */
	public static void createDPad(float X, float Y)
	{
		if(dpad == null)
			dpad = new FlxDigitalPad(X, Y);
	}
	
	
	/**
	 * Shut down the game
	 */
	public static void shutdown(boolean forceSoundShutdown)
	{
		_game.shutdown(forceSoundShutdown);
	}

	public static void shutdown()
	{
		_game.shutdown(false);
	}

	public static void resume()
	{
		_game.resume();
	}
}
