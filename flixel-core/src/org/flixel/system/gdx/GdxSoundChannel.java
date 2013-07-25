package org.flixel.system.gdx;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Pool;

import flash.events.EventDispatcher;
import flash.media.SoundChannel;
import flash.media.SoundTransform;

public class GdxSoundChannel extends EventDispatcher implements SoundChannel 
{
	private SoundTransform _soundTransform;
	
	private Sound _sound;
	private long _soundId;
	
	private static final Pool<GdxSoundChannel> _pool = new Pool<GdxSoundChannel>(){@Override protected GdxSoundChannel newObject() {return new GdxSoundChannel();}};
	
	public static GdxSoundChannel getNew()
	{
		return _pool.obtain();
	}
	
	private GdxSoundChannel()
	{
		
	}
	
	long play(Sound sound, float startTime, int loops, SoundTransform sndTransform)
	{
		_sound = sound;
		_soundId = attemptPlay(sound, startTime, loops, sndTransform);
		if(_soundId == -1)
			stop();
		return _soundId;
	}
	
	@Override
	public void setSoundTransform(SoundTransform soundTransform)
	{
		_soundTransform = soundTransform;
		_sound.setPan(_soundId, _soundTransform.pan, _soundTransform.volume);
	}

	@Override
	public SoundTransform getSoundTransform()
	{
		return _soundTransform;
	}

	@Override
	public void stop()
	{
		_sound.stop(_soundId);
		_pool.free(this);
	}
	
	@Override
	public void pause()
	{
		_sound.stop(_soundId);
		//TODO: Sound.pause() and Sound.resume are available in the latest libgdx nightlies.
		//make sure this is changed to use them next time we update
	}
	
	@Override
	public void resume()
	{
		_soundId = _sound.play(_soundTransform.volume, 1f, _soundTransform.pan);
	}
	
	/**
	 * On Android, the sound file is not guaranteed to be loaded
	 * by the time play is called. Our current workaround is to block
	 * the application until the file is successfully played. Usually this
	 * delay is not noticeable.
	 */
	private long attemptPlay(Sound sound, float startTime, int loops, SoundTransform sndTransform)
	{
		final int PLAY_TRY_LIMIT = 5000;
				
		int i = 0;
		long soundId = -1;
		while (soundId == -1 && i++ < PLAY_TRY_LIMIT)
			soundId = loops > 0 ? sound.loop() : sound.play();
		setSoundTransform(sndTransform);
		return soundId;
	}
}
