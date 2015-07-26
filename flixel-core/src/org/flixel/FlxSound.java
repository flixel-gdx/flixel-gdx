package org.flixel;

import org.flixel.system.gdx.audio.GdxMusic;
import org.flixel.system.gdx.audio.GdxSound;

import flash.events.Event;
import flash.events.IEventListener;
import flash.media.Sound;
import flash.media.SoundChannel;
import flash.media.SoundTransform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * This is the universal flixel sound object, used for streaming, music, and sound effects.
 * 
 * @author Ka Wing Chin
 * @author Thomas Weston
 */
public class FlxSound extends FlxBasic
{
	/**
	 * Automatically determine the type of file.
	 */
	static public final int AUTO = 0;
	/**
	 * A short audio clip.
	 */
	static public final int SFX = 1;
	/**
	 * A large music file.
	 */
	static public final int MUSIC = 2;

	/**
	 * The X position of this sound in world coordinates.
	 * Only really matters if you are doing proximity/panning stuff.
	 */
	public float x;
	/**
	 * The Y position of this sound in world coordinates.
	 * Only really matters if you are doing proximity/panning stuff.
	 */
	public float y;
	/**
	 * Whether or not this sound should be automatically destroyed when you switch states.
	 */
	public boolean survive;
	/**
	 * The ID3 song name.  Defaults to null.  Currently only works for streamed sounds.
	 */
	public String name;
	/**
	 * The ID3 artist name.  Defaults to null.  Currently only works for streamed sounds.
	 */
	public String artist;
	/**
	 * Stores the average wave amplitude of both stereo channels
	 */
	public float amplitude;
	/**
	 * Just the amplitude of the left stereo channel
	 */
	public float amplitudeLeft;
	/**
	 * Just the amplitude of the left stereo channel
	 */
	public float amplitudeRight;
	/**
	 * Whether to call destroy() when the sound has finished.
	 */
	public boolean autoDestroy;

	/**
	 * Internal tracker for a Flash sound object.
	 */
	protected Sound _sound;
	/**
	 * Internal tracker for a Flash sound channel object.
	 */
	protected SoundChannel _channel;
	/**
	 * Internal tracker for a Flash sound transform object.
	 */
	protected SoundTransform _transform;
	/**
	 * Internal tracker for the position in runtime of the music playback.
	 */
	protected float _position;
	/**
	 * Internal tracker for how loud the sound is.
	 */
	protected float _volume;
	/**
	 * Internal tracker for total volume adjustment.
	 */
	protected float _volumeAdjust;
	/**
	 * Internal tracker for how fast or how slow the sound is.
	 */
	private float _pitch;
	/**
	 * Internal tracker for whether the sound is looping or not.
	 */
	protected boolean _looped;
	/**
	 * Internal tracker for whether the sound is paused by focus lost.
	 */
	boolean _isPausedOnFocusLost;
	/**
	 * Internal tracker for the sound's "target" (for proximity and panning).
	 */
	protected FlxObject _target;
	/**
	 * Internal tracker for the maximum effective radius of this sound (for proximity and panning).
	 */
	protected float _radius;
	/**
	 * Internal tracker for whether to pan the sound left and right.  Default is false.
	 */
	protected boolean _pan;
	/**
	 * Internal timer used to keep track of requests to fade out the sound playback.
	 */
	protected float _fadeOutTimer;
	/**
	 * Internal helper for fading out sounds.
	 */
	protected float _fadeOutTotal;
	/**
	 * Internal flag for whether to pause or stop the sound when it's done fading out.
	 */
	protected boolean _pauseOnFadeOut;
	/**
	 * Internal timer for fading in the sound playback.
	 */
	protected float _fadeInTimer;
	/**
	 * Internal helper for fading in sounds.
	 */
	protected float _fadeInTotal;

	/**
	 * The FlxSound constructor gets all the variables initialized, but NOT ready to play a sound yet.
	 */
	public FlxSound()
	{
		super();
		createSound();
	}

	/**
	 * An internal function for clearing all the variables used by sounds.
	 */
	protected void createSound()
	{
		destroy();
		x = 0;
		y = 0;
		if(_transform == null)
			_transform = new SoundTransform();
		_transform.pan = 0f;
		_sound = null;
		_position = 0;
		_volume = 1.0f;
		_volumeAdjust = 1.0f;
		_pitch = 1.0f;
		_looped = false;
		_target = null;
		_radius = 0;
		_pan = false;
		_fadeOutTimer = 0;
		_fadeOutTotal = 0;
		_pauseOnFadeOut = false;
		_fadeInTimer = 0;
		_fadeInTotal = 0;
		exists = false;
		active = false;
		visible = false;
		name = null;
		artist = null;
		amplitude = 0;
		amplitudeLeft = 0;
		amplitudeRight = 0;
		autoDestroy = false;
		survive = false;
	}

	/**
	 * Clean up memory.
	 */
	public void destroy()
	{
		kill();

		_transform = null;
		_sound = null;
		_channel = null;
		_target = null;
		name = null;
		artist = null;
		
		super.destroy();
	}

	/**
	 * Handles fade out, fade in, panning, proximity, and amplitude operations each frame.
	 */
	@Override
	public void update()
	{
		float radial = 1.0f;
		float fade = 1.0f;
		
		//Distance-based volume control
		if(_target != null)
		{
			radial = 1 - FlxU.getDistance(new FlxPoint(_target.x,_target.y),new FlxPoint(x,y))/_radius;
			if(radial < 0) radial = 0;
			if(radial > 1) radial = 1;

			if(_pan)
			{
				float d = (x-_target.x)/_radius;
				if(d < -1) d = -1;
				else if(d > 1) d = 1;
				_transform.pan = d;
			}
		}

		//Cross-fading volume control
		if(_fadeOutTimer > 0)
		{
			_fadeOutTimer -= FlxG.elapsed;
			if(_fadeOutTimer <= 0)
			{
				if(_pauseOnFadeOut)
					pause();
				else
					stop();
			}
			fade = _fadeOutTimer/_fadeOutTotal;
			if(fade < 0) fade = 0;
		}
		else if(_fadeInTimer > 0)
		{
			_fadeInTimer -= FlxG.elapsed;
			fade = _fadeInTimer/_fadeInTotal;
			if(fade < 0) fade = 0;
			fade = 1 - fade;
		}

		_volumeAdjust = radial*fade;
		updateTransform();

		if((_transform.volume > 0) && (_channel != null))
		{
			amplitudeLeft = _channel.getLeftPeak()/_transform.volume;
			amplitudeRight = _channel.getRightPeak()/_transform.volume;
			amplitude = (amplitudeLeft+amplitudeRight)*0.5f;
		}
	}

	@Override
	public void kill()
	{
		super.kill();
		if(_channel != null)
			stop();
	}

	/**
	 * One of two main setup functions for sounds, this function loads a sound from an embedded MP3.
	 * 
	 * @param	EmbeddedSound	An embedded Class object representing an MP3 file.
	 * @param	Looped			Whether or not this sound should loop endlessly.
	 * @param	AutoDestroy		Whether or not this <code>FlxSound</code> instance should be destroyed when the sound finishes playing.  Default value is false, but FlxG.play() and FlxG.stream() will set it to true by default.
	 * @param	Type			Whether this sound is a sound effect or a music track.
	 * 
	 * @return	This <code>FlxSound</code> instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSound loadEmbedded(String EmbeddedSound, boolean Looped, boolean AutoDestroy, int Type)
	{
		stop();
		createSound();

		switch(Type)
		{
			case SFX:
				_sound = new GdxSound(EmbeddedSound);
				break;

			case MUSIC:
				_sound = new GdxMusic(EmbeddedSound);
				break;

			case AUTO:
			default:
				//If the type is not specified, make a guess based on the file size.
				FileHandle file = Gdx.files.internal(EmbeddedSound);
				Type = file.length() < 24576 ? SFX : MUSIC;
				return loadEmbedded(EmbeddedSound, Looped, AutoDestroy, Type);
		}

		//NOTE: can't pull ID3 info from embedded sound currently
		_looped = Looped;
		autoDestroy = AutoDestroy;
		updateTransform();
		exists = true;
		return this;
	}

	/**
	 * One of two main setup functions for sounds, this function loads a sound from an embedded MP3.
	 * 
	 * @param	EmbeddedSound	An embedded Class object representing an MP3 file.
	 * @param	Looped			Whether or not this sound should loop endlessly.
	 * @param	AutoDestroy		Whether or not this <code>FlxSound</code> instance should be destroyed when the sound finishes playing.  Default value is false, but FlxG.play() and FlxG.stream() will set it to true by default.
	 * 
	 * @return	This <code>FlxSound</code> instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSound loadEmbedded(String EmbeddedSound, boolean Looped, boolean AutoDestroy)
	{
		return loadEmbedded(EmbeddedSound, Looped, AutoDestroy, AUTO);
	}

	/**
	 * One of two main setup functions for sounds, this function loads a sound from an embedded MP3.
	 * 
	 * @param	EmbeddedSound	An embedded Class object representing an MP3 file.
	 * @param	Looped			Whether or not this sound should loop endlessly.
	 * 
	 * @return	This <code>FlxSound</code> instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSound loadEmbedded(String EmbeddedSound, boolean Looped)
	{
		return loadEmbedded(EmbeddedSound, Looped, false, AUTO);
	}

	/**
	 * One of two main setup functions for sounds, this function loads a sound from an embedded MP3.
	 * 
	 * @param	EmbeddedSound	An embedded Class object representing an MP3 file.
	 * 
	 * @return	This <code>FlxSound</code> instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSound loadEmbedded(String EmbeddedSound)
	{
		return loadEmbedded(EmbeddedSound, false, false, AUTO);
	}

	/**
	 * One of two main setup functions for sounds, this function loads a sound from a URL.
	 * 
	 * @param	SoundURL	A string representing the URL of the MP3 file you want to play.
	 * @param	Looped		Whether or not this sound should loop endlessly.
	 * @param	AutoDestroy	Whether or not this <code>FlxSound</code> instance should be destroyed when the sound finishes playing.  Default value is false, but FlxG.play() and FlxG.stream() will set it to true by default.
	 * 
	 * @return	This <code>FlxSound</code> instance (nice for chaining stuff together, if you're into that).
	 */
	// TODO: Load a sound from an URL
	public FlxSound loadStream(String SoundURL, boolean Looped, boolean AutoDestroy)
	{
		stop();
		createSound();
		// _sound = new Sound();
		// _sound.addEventListener(Event.ID3, gotID3);
		// _sound.load(new URLRequest(SoundURL));
		_looped = Looped;
		autoDestroy = AutoDestroy;
		updateTransform();
		exists = true;
		return this;
	}

	/**
	 * One of two main setup functions for sounds, this function loads a sound from a URL.
	 * 
	 * @param	SoundURL	A string representing the URL of the MP3 file you want to play.
	 * @param	Looped		Whether or not this sound should loop endlessly.
	 * 
	 * @return	This <code>FlxSound</code> instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSound loadStream(String SoundURL, boolean Looped)
	{
		return loadStream(SoundURL, Looped, false);
	}

	/**
	 * One of two main setup functions for sounds, this function loads a sound from a URL.
	 * 
	 * @param	SoundURL	A string representing the URL of the MP3 file you want to play.
	 * 
	 * @return	This <code>FlxSound</code> instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSound loadStream(String SoundURL)
	{
		return loadStream(SoundURL, false, false);
	}

	/**
	 * Call this function if you want this sound's volume to change
	 * based on distance from a particular FlxCore object.
	 * 
	 * @param	X		The X position of the sound.
	 * @param	Y		The Y position of the sound.
	 * @param	Object	The object you want to track.
	 * @param	Radius	The maximum distance this sound can travel.
	 * @param	Pan		Whether the sound should pan in addition to the volume changes (default: true).
	 * 
	 * @return	This FlxSound instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSound proximity(float X,float Y,FlxObject Object,float Radius,boolean Pan)
	{
		x = X;
		y = Y;
		_target = Object;
		_radius = Radius;
		_pan = Pan;
		return this;
	}

	/**
	 * Call this function if you want this sound's volume to change
	 * based on distance from a particular FlxCore object.
	 * 
	 * @param	X		The X position of the sound.
	 * @param	Y		The Y position of the sound.
	 * @param	Object	The object you want to track.
	 * @param	Radius	The maximum distance this sound can travel.
	 * @param	Pan		Whether the sound should pan in addition to the volume changes (default: true).
	 * 
	 * @return	This FlxSound instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSound proximity(float X,float Y,FlxObject Object,float Radius)
	{
		return proximity(X,Y,Object,Radius,true);
	}

	/**
	 * Call this function to play the sound - also works on paused sounds.
	 * 
	 * @param	ForceRestart	Whether to start the sound over or not.  Default value is false, meaning if the sound is already playing or was paused when you call <code>play()</code>, it will continue playing from its current position, NOT start again from the beginning.
	 */
	public void play(boolean ForceRestart)
	{
		if(_position < 0)
			return;
		if(ForceRestart)
		{
			boolean oldAutoDestroy = autoDestroy;
			autoDestroy = false;
			stop();
			autoDestroy = oldAutoDestroy;
		}
		if(_looped)
		{
			if(_position == 0)
			{
				if(_channel == null)
					_channel = _sound.play(0f,9999,_transform);
				if(_channel == null)
					exists = false;
				else
					_channel.addEventListener(Event.SOUND_COMPLETE, stoppedListener);
			}
			else
				_channel.resume();
		}
		else
		{
			if(_position == 0)
			{
				if(_channel == null)
				{
					_channel = _sound.play(0f,0,_transform);
					if(_channel == null)
						exists = false;
					else
						_channel.addEventListener(Event.SOUND_COMPLETE, stoppedListener);
				}
			}
			else
				_channel.resume();
		}

		active = (_channel != null);
		_position = 0;
	}

	/**
	 * Call this function to play the sound - also works on paused sounds.
	 */
	public void play()
	{
		play(false);
	}

	/**
	 * Unpause a sound. Only works on sounds that have been paused.
	 */
	public void resume()
	{
		if(_position <= 0)
			return;
		_channel.resume();
		active = (_channel != null);
	}

	/**
	 * Call this function to pause this sound.
	 */
	public void pause()
	{
		if(_channel == null)
		{
			_position = -1;
			return;
		}
		_position = 1;
		_channel.pause();
		active = false;
	}

	/**
	 * Call this function to stop this sound.
	 */
	public void stop()
	{
		_position = 0;
		if(_channel != null)
		{
			_channel.stop();
			stopped();
		}
	}

	/**
	 * Call this function to make this sound fade out over a certain time interval.
	 * 
	 * @param	Seconds			The amount of time the fade out operation should take.
	 * @param	PauseInstead	Tells the sound to pause on fadeout, instead of stopping.
	 */
	public void fadeOut(float Seconds,boolean PauseInstead)
	{
		_pauseOnFadeOut = PauseInstead;
		_fadeInTimer = 0;
		_fadeOutTimer = Seconds;
		_fadeOutTotal = _fadeOutTimer;
	}

	/**
	 * Call this function to make this sound fade out over a certain time interval.
	 * 
	 * @param	Seconds			The amount of time the fade out operation should take.
	 */
	public void fadeOut(float Seconds)
	{
		fadeOut(Seconds,false);
	}

	/**
	 * Call this function to make a sound fade in over a certain
	 * time interval (calls <code>play()</code> automatically).
	 * 
	 * @param	Seconds		The amount of time the fade-in operation should take.
	 */
	public void fadeIn(float Seconds)
	{
		_fadeOutTimer = 0;
		_fadeInTimer = Seconds;
		_fadeInTotal = _fadeInTimer;
		play();
	}

	/**
	 * Set <code>volume</code> to a value between 0 and 1 to change how this sound is.
	 */
	public float getVolume()
	{
		return _volume;
	}

	/**
	 * Set <code>volume</code> to a value between 0 and 1 to change how this sound is.
	 * 
	 * @param	Volume	The volume of the sound.
	 */
	public void setVolume(float Volume)
	{
		_volume = Volume;
		if(_volume < 0)
			_volume = 0;
		else if(_volume > 1)
			_volume = 1;
		updateTransform();
	}

	/**
	 * Returns the currently selected "real" volume of the sound (takes fades
	 * and proximity into account).
	 * 
	 * @return	The adjusted volume of the sound.
	 */
	public float getActualVolume()
	{
		return _volume*_volumeAdjust;
	}

	/**
	 * Set the pitch multiplier, 1 == default, >1 == faster, <1 == slower.
	 * The value has to be between 0.5 and 2.0.
	 * 
	 * @param	Pitch	The pitch multiplier.
	 */
	public void setPitch(float Pitch)
	{
		_pitch = Pitch;
		if(_pitch < 0)
			_pitch = 0;
		else if(_pitch > 2)
			_pitch = 2;
		updateTransform();
	}

	/**
	 * Returns the current pitch of the sound.
	 * 
	 * @return	The pitch of the sound.
	 */
	public float getPitch()
	{
		return _pitch;
	}

	/**
	 * Call after adjusting the volume to update the sound channel's settings.
	 */
	protected void updateTransform()
	{
		_transform.volume = (FlxG.mute?0:1)*FlxG.getVolume()*_volume*_volumeAdjust;
		_transform.pitch = _pitch;
		if(_channel != null)
			_channel.setSoundTransform(_transform);
	}

	/**
	 * An internal helper function used to help Flash clean up and re-use finished sounds.
	 */
	protected void stopped()
	{
		_channel.removeEventListener(Event.SOUND_COMPLETE,stoppedListener);
		_channel = null;
		active = false;
		_isPausedOnFocusLost = false;
		if(autoDestroy)
			destroy();
	}

	/**
	 * Internal event handler for ID3 info (i.e. fetching the song name).
	 */
	// TODO: ID3 info
	protected void gotID3()
	{
		/*
		 * FlxG.log("got ID3 info!"); if(_sound.id3.songName.length > 0) name =
		 * _sound.id3.songName; if(_sound.id3.artist.length > 0) artist =
		 * _sound.id3.artist; _sound.removeEventListener(Event.ID3, gotID3);
		 */
	}
	
	/**
	 * Internal event listener.
	 */
	protected final IEventListener stoppedListener = new IEventListener()
	{
		@Override
		public void onEvent(Event e)
		{
			stopped();
		}
	};
}
