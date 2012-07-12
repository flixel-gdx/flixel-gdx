package org.flixel;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
	
/**
 * This is the universal flixel sound object, used for streaming, music, and sound effects.
 * 
 * @author	Ka Wing Chin
 */
public class FlxSound extends FlxBasic
{
	/**
	 * A short audio clip.
	 */
	static public final int SFX = 0;
	/**
	 * A large music file.
	 */
	static public final int MUSIC = 1;
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
	 * Internal tracker for a Gdx sound object.
	 */
	protected Sound _sound;
	/**
	 * Internal tracker for a Gdx music object.
	 */
	protected Music _music;
	/**
	 * Internal tracker for a Flash sound channel object.
	 */
	//protected var _channel:SoundChannel;
	/**
	 * Internal tracker for a Flash sound transform object.
	 */
	//protected var _transform:SoundTransform;
	/**
	 * Internal tracker for the id of the currently playing sound.
	 */
	protected long _soundId;
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
	 * Internal tracker for whether the sound is looping or not.
	 */
	protected boolean _looped;
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
	 * Internal tracker for the left to right panning of the sound.
	 */
	protected float _panAmount;
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
		_panAmount = 0;
		_sound = null;
		_music = null;
		_soundId = -1;
		_position = 0;
		_volume = 1.0f;
		_volumeAdjust = 1.0f;
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
	}
		
	/**
	 * Clean up memory.
	 */
	public void destroy()
	{
		kill();
		
		if (_sound != null)
			FlxG.assetManager.unload(FlxG.assetManager.getAssetFileName(_sound));
		
		if (_music != null)
			FlxG.assetManager.unload(FlxG.assetManager.getAssetFileName(_music));
		
		_sound = null;
		_music = null;
		_soundId = -1;
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
		if(_position != 0)
			return;
		
		float radial = 1.0f;
		float fade = 1.0f;
		
		//Distance-based volume control
		if(_target != null)
		{
			radial = FlxU.getDistance(new FlxPoint(_target.x,_target.y),new FlxPoint(x,y))/_radius;
			if(radial < 0) radial = 0;
			if(radial > 1) radial = 1;
			
			if(_pan)
			{
				float d = (_target.x-x)/_radius;
				if(d < -1) d = -1;
				else if(d > 1) d = 1;
				_panAmount = d;
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
		
		// TODO: Amplitude data
		//if(_transform.volume > 0)
		//{
		//	amplitudeLeft = _channel.leftPeak/_transform.volume;
		//	amplitudeRight = _channel.rightPeak/_transform.volume;
		//	amplitude = (amplitudeLeft+amplitudeRight)*0.5;
		//}
	}
		
	@Override 
	public void kill()
	{
		super.kill();
		if (_sound != null && _soundId != -1 || _music != null && _music.isPlaying())
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
		//If the type is not specified, make a guess based on the file size.
		if (Type == -1)
		{
			//FileHandle file = Gdx.files.internal(EmbeddedSound);
			Type = MUSIC;//file.length() < 24576 ? SFX : MUSIC;
		}
		
		Class<?> classType = Type == SFX ? Sound.class : Music.class;
		
		if (!FlxG.assetManager.isLoaded(EmbeddedSound, classType))
		{
			FlxG.assetManager.load(EmbeddedSound, classType);
			FlxG.assetManager.finishLoading();
		}
		
		stop();
		createSound();
		
		if (classType == Sound.class)
			_sound = FlxG.assetManager.get(EmbeddedSound, Sound.class);
		else
			_music = FlxG.assetManager.get(EmbeddedSound, Music.class);
		
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
		return loadEmbedded(EmbeddedSound, Looped, AutoDestroy, -1);
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
		return loadEmbedded(EmbeddedSound, Looped, false, -1);
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
		return loadEmbedded(EmbeddedSound, false, false, -1);
	}
	
	/**
	 * One of two main setup functions for sounds, this function loads a sound from a URL.
	 * 
	 * @param	EmbeddedSound	A string representing the URL of the MP3 file you want to play.
	 * @param	Looped			Whether or not this sound should loop endlessly.
	 * @param	AutoDestroy		Whether or not this <code>FlxSound</code> instance should be destroyed when the sound finishes playing.  Default value is false, but FlxG.play() and FlxG.stream() will set it to true by default.
	 * 
	 * @return	This <code>FlxSound</code> instance (nice for chaining stuff together, if you're into that).
	 */
	//TODO: Load a sound from an URL
	public FlxSound loadStream(String SoundURL, boolean Looped, boolean AutoDestroy)
	{
		stop();
		createSound();
		//_sound = new Sound();
		//_sound.addEventListener(Event.ID3, gotID3);
		//_sound.load(new URLRequest(SoundURL));
		//_looped = Looped;
		//autoDestroy = AutoDestroy;
		//updateTransform();
		
		exists = true;
		return this;
	}
	
	/**
	 * One of two main setup functions for sounds, this function loads a sound from a URL.
	 * 
	 * @param	EmbeddedSound	A string representing the URL of the MP3 file you want to play.
	 * @param	Looped			Whether or not this sound should loop endlessly.
	 * @param	AutoDestroy		Whether or not this <code>FlxSound</code> instance should be destroyed when the sound finishes playing.  Default value is false, but FlxG.play() and FlxG.stream() will set it to true by default.
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
	 * @param	EmbeddedSound	A string representing the URL of the MP3 file you want to play.
	 * @param	Looped			Whether or not this sound should loop endlessly.
	 * @param	AutoDestroy		Whether or not this <code>FlxSound</code> instance should be destroyed when the sound finishes playing.  Default value is false, but FlxG.play() and FlxG.stream() will set it to true by default.
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
	public FlxSound proximity(float X, float Y, FlxObject Object, float Radius, boolean Pan)
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
	 * 
	 * @return	This FlxSound instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSound proximity(float X, float Y, FlxObject Object, float Radius)
	{
		return proximity(X, Y, Object, Radius, true);
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
				if(_sound != null && _soundId == -1)
				{
					_soundId = _sound.loop();
				
					if(_soundId == -1)
						exists = false;
				}
				else if(_music != null && !_music.isPlaying())
				{
					_music.setLooping(true);
					_music.play();
				}
			}
			else
			{
				if(_sound != null)
				{
					_soundId = _sound.loop();
					if(_soundId == -1)
						exists = false;
					//else
						//_channel.addEventListener(Event.SOUND_COMPLETE, looped);
				}
				else if(_music != null)
				{
					_music.setLooping(true);
					_music.play();
				}
			}
		}
		else
		{
			if(_position == 0)
			{
				if(_sound != null && _soundId == -1)
				{
					_soundId = _sound.play();
					if(_soundId == -1)
						exists = false;
					//else
						//TODO: Detect when Sound/Music is finished.
						//_channel.addEventListener(Event.SOUND_COMPLETE, stopped);
				}
				else if(_music != null && !_music.isPlaying())
				{
					_music.setLooping(false);
					_music.play();
				}
			}
			else
			{
				if(_sound != null)
				{
					_soundId = _sound.play();
					if(_soundId == -1)
						exists = false;
				}
				else if(_music != null)
				{
					_music.setLooping(false);
					_music.play();
				}
			}
		}
		
		updateTransform();
		//TODO: _music soundId will always get -1. It should be active = true then.
		if(_music != null && _soundId == -1)
			active = true;
		else		
			active = (_soundId != -1);
		_position = 0;
	}
	
	/**
	 * Call this function to play the sound - also works on paused sounds.
	 * 
	 */
	public void play()
	{
		play(false);
	}
		
	/**
	 * Unpause a sound.  Only works on sounds that have been paused.
	 */
	public void resume()
	{
		if(_position <= 0)
			return;
		if(_looped)
		{
			if(_sound != null)
			{
				_soundId = _sound.loop();
				if(_soundId == -1)
					exists = false;
				//else
					//_channel.addEventListener(Event.SOUND_COMPLETE, looped);
			}
			else if(_music != null)
			{
				_music.setLooping(true);
				_music.play();
			}
		}
		else
		{
			if(_sound != null)
			{
				_soundId = _sound.play();
				if(_soundId == -1)
					exists = false;
			}
			else if(_music != null)
			{
				_music.setLooping(false);
				_music.play();
			}
		}
		
		updateTransform();
		//TODO: _music soundId will always get -1. It should be active = true then.
		if(_music != null && _soundId == -1)
			active = true;
		else		
			active = (_soundId != -1);
	}
		
	/**
	 * Call this function to pause this sound.
	 */
	public void pause()
	{
		/*
		if(_soundId == -1)
		{
			_position = -1;
			return;
		}
		_position = _channel.position;
		_channel.stop();
		if(_looped)
		{
			while(_position >= _sound.length)
				_position -= _sound.length;
		}
		if(_position <= 0)
			_position = 1;
		_channel = null;
		active = false;
		*/
		if(_sound != null)
		{
			_sound.stop(); // TODO: pause of sound. Now it stops it.
		}
		if(_music != null)
		{
			if(!_music.isPlaying())
			{
				_position = -1;
				return;
			}
			_position = _music.getPosition();
			_music.pause();
			active = false;
		}
	}
		
	/**
	 * Call this function to stop this sound.
	 */
	public void stop()
	{
		_position = 0;
		if (_sound != null && _soundId != -1)
		{
			_sound.stop(_soundId);
			stopped();
		}
		if (_music != null && _music.isPlaying())
		{
			_music.stop();
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
		fadeOut(Seconds, false);
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
	 * @private
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
	 * Returns the currently selected "real" volume of the sound (takes fades and proximity into account).
	 * 
	 * @return	The adjusted volume of the sound.
	 */
	public float getActualVolume()
	{
		return _volume*_volumeAdjust;
	}
		
	/**
	 * Call after adjusting the volume to update the sound channel's settings.
	 */
	protected void updateTransform()
	{
		float volume = (FlxG.mute?0:1)*_volume*_volumeAdjust;
		
		if (_sound != null)
			_sound.setPan(_soundId, _pan ? _panAmount : 0, volume);
		else if (_music != null)
			_music.setVolume(volume);
	}
		
	/**
	 * An internal helper function used to help Flash resume playing a looped sound.
	 * 
	 * @param	event		An <code>Event</code> object.
	 */
	protected void looped()
	{
		/*
	    if (_channel == null)
	    	return;
	       _channel.removeEventListener(Event.SOUND_COMPLETE,looped);
	       _channel = null;
		play();*/
	}

	/**
	 * An internal helper function used to help Flash clean up and re-use finished sounds.
	 * 
	 * @param	event		An <code>Event</code> object.
	 */
	protected void stopped()
	{
	    _soundId = -1;
		active = false;
		if(autoDestroy)
			destroy();
	}
		
	/**
	 * Internal event handler for ID3 info (i.e. fetching the song name).
	 * 
	 * @param	event	An <code>Event</code> object.
	 */
	protected void gotID3()
	{
		/*
		FlxG.log("got ID3 info!");
		if(_sound.id3.songName.length > 0)
			name = _sound.id3.songName;
		if(_sound.id3.artist.length > 0)
			artist = _sound.id3.artist;
		_sound.removeEventListener(Event.ID3, gotID3);
		*/
	}
}