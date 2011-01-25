package org.flixel;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

/**
 * This is the universal flixel sound object, used for streaming, music, and sound effects.
 */
public class FlxSound extends FlxObject
{
	/**
	 * Whether or not this sound should be automatically destroyed when you switch states.
	 */
	public boolean survive;
	/**
	 * Whether the sound is currently playing or not.
	 */
	public boolean playing;
	/**
	 * The ID3 song name.  Defaults to null.  Currently only works for streamed sounds.
	 */
	public String name;
	/**
	 * The ID3 artist name.  Defaults to null.  Currently only works for streamed sounds.
	 */
	public String artist;
	
	protected boolean _init;
	protected MediaPlayer _mediaPlayer;
	protected float _volume;
	protected float _volumeAdjust;
	protected boolean _looped;
	protected FlxObject _core;
	protected float _radius;
	protected float _panValue;
	protected boolean _pan;
	protected float _fadeOutTimer;
	protected float _fadeOutTotal;
	protected boolean _pauseOnFadeOut;
	protected float _fadeInTimer;
	protected float _fadeInTotal;
	protected FlxPoint _point2;
	
	/**
	 * The FlxSound constructor gets all the variables initialized, but NOT ready to play a sound yet.
	 */
	public FlxSound()
	{
		super();
		_point2 = new FlxPoint();
		init();
		setFixed(true); //no movement usually
	}
	
	/**
	 * An internal function for clearing all the variables used by sounds.
	 */
	protected void init()
	{
		_volume = 1.0f;
		_volumeAdjust = 1.0f;
		_looped = false;
		_core = null;
		_radius = 0;
		_pan = false;
		_fadeOutTimer = 0;
		_fadeOutTotal = 0;
		_pauseOnFadeOut = false;
		_fadeInTimer = 0;
		_fadeInTotal = 0;
		active = false;
		visible = false;
		setSolid(false);
		playing = false;
		name = null;
		artist = null;
	}
	
	/**
	 * One of two main setup functions for sounds, this function loads a sound from an embedded MP3.
	 * 
	 * @param	EmbeddedSound	An embedded Class object representing an MP3 file.
	 * @param	Looped			Whether or not this sound should loop endlessly.
	 * 
	 * @return	This <code>FlxSound</code> instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSound loadEmbedded(int EmbeddedSound, boolean Looped)
	{
		stop();
		init();
		_mediaPlayer = MediaPlayer.create(FlxResource.context, EmbeddedSound);
		_looped = Looped;		
		if(!_looped)
			_mediaPlayer.setOnCompletionListener(stopped);
		updateTransform();
		active = true;
		return this;
	}
	
	/**
	 * One of two main setup functions for sounds, this function loads a sound from an embedded MP3.
	 * 
	 * @param	EmbeddedSound	An embedded Class object representing an MP3 file.
	 * 
	 * @return	This <code>FlxSound</code> instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSound loadEmbedded(int EmbeddedSound)
	{
		return loadEmbedded(EmbeddedSound, false);
	}
	
	
	/**
	 * One of two main setup functions for sounds, this function loads a sound from a URL.
	 * 
	 * @param	EmbeddedSound	A string representing the URL of the MP3 file you want to play.
	 * @param	Looped			Whether or not this sound should loop endlessly.
	 * 
	 * @return	This <code>FlxSound</code> instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSound loadStream(String SoundURL, boolean Looped)
	{
		stop();
		init();
		_mediaPlayer = new MediaPlayer();
		try
		{
			_mediaPlayer.setDataSource(SoundURL);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		try
		{
			_mediaPlayer.prepare();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		_looped = Looped;
		updateTransform();
		active = true;
		return this;
	}
	
	/**
	 * One of two main setup functions for sounds, this function loads a sound from a URL.
	 * 
	 * @param	EmbeddedSound	A string representing the URL of the MP3 file you want to play.
	 * 
	 * @return	This <code>FlxSound</code> instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSound loadStream(String SoundURL)
	{
		return loadStream(SoundURL, false);
	}
	
	
	/**
	 * Call this function if you want this sound's volume to change
	 * based on distance from a particular FlxCore object.
	 * 
	 * @param	X		The X position of the sound.
	 * @param	Y		The Y position of the sound.
	 * @param	Core	The object you want to track.
	 * @param	Radius	The maximum distance this sound can travel.
	 * @param	Pan
	 * 
	 * @return	This FlxSound instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSound proximity(float X, float Y, FlxObject Core, float Radius, boolean Pan)
	{
		x = X;
		y = Y;
		_core = Core;
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
	 * @param	Core	The object you want to track.
	 * @param	Radius	The maximum distance this sound can travel.
	 * 
	 * @return	This FlxSound instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSound proximity(float X, float Y, FlxObject Core, float Radius)
	{
		return proximity(X, Y, Core, Radius, true);
	}
	
	/**
	 * Call this function to play the sound.
	 */
	public synchronized void play()
	{
		if(_mediaPlayer == null)
			return;
		
		_mediaPlayer.setLooping(_looped);
		_mediaPlayer.start();
	}
	
	/**
	 * Call this function to pause this sound.
	 */
	public synchronized void pause()
	{		
		if(_mediaPlayer == null)
			return;
		_mediaPlayer.pause();
	}

	/**
	 * Call this function to stop this sound.
	 */
	public synchronized void stop()
	{
		if(_mediaPlayer == null)
			return;
		_mediaPlayer.stop();
	}
	
	
	/**
	 * Call this function to make this sound fade out over a certain time interval.
	 * 
	 * @param	Seconds			The amount of time the fade out operation should take.
	 * @param	PauseInstead	Tells the sound to pause on fadeout, instead of stopping.
	 */
	public void fadeOut(float Seconds, boolean PauseInstead)
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
	 * 
	 * @return get the volume.
	 */
	public float getVolume()
	{
		return _volume;
	}
	
	/**
	 * Set <code>volume</code> to a value between 0 and 1 to change how this sound is.
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
	 * Internal function that performs the actual logical updates to the sound object.
	 * Doesn't do much except optional proximity and fade calculations.
	 */
	protected void updateSound()
	{		
		float radial = 1.0f;
		float fade = 1.0f;
		
		//Distance-based volume control
		if(_core != null)
		{
			FlxPoint _point = new FlxPoint();
			FlxPoint _point2 = new FlxPoint();
			_core.getScreenXY(_point);
			getScreenXY(_point2);
			float dx = _point.x - _point2.x;
			float dy = _point.y - _point2.y;
			radial = (float) ((_radius - Math.sqrt(dx*dx + dy*dy))/_radius);
			if(radial < 0) radial = 0;
			if(radial > 1) radial = 1;
			
			if(_pan)
			{
				_panValue = -dx / _radius;
				if(_panValue < -1)
					_panValue = -1;
				else if(_panValue > 1)
					_panValue = 1;
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
	}
	
	
	/**
	 * The basic game loop update function.  Just calls <code>updateSound()</code>.
	 */
	@Override
	public void update()
	{
		super.update();
		updateSound();	
	}
	
	
	/**
	 * The basic class destructor, stops the music and removes any leftover events.
	 */
	@Override
	public void destroy()
	{
		if(active)
			stop();
	}
	
	/**
	 * An internal function used to help organize and change the volume of the sound.
	 */
	void updateTransform()
	{
		float volume = FlxG.getMuteValue() * FlxG.getVolume() * _volume * _volumeAdjust;
		float normalisedPan = _panValue + 1;
		float panLeft = normalisedPan > 1.0f ? 2.0f - normalisedPan : 1.0f;
		float panRight = normalisedPan < 1.0f ? normalisedPan : 1.0f;
		_mediaPlayer.setVolume(volume * panLeft, volume * panRight);
	}
	
	/**
	 * An internal helper function used to help the game resume playing a looped sound.
	 */
	private OnCompletionListener stopped = new OnCompletionListener()
	{
		@Override
		public void onCompletion(MediaPlayer mp)
		{	
			if(_looped)
				mp.release();
			else
				mp.setOnCompletionListener(null);
			active = false;
			playing = false;
		}		
	};
}
