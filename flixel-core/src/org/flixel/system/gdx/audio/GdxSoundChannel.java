package org.flixel.system.gdx.audio;

import org.flixel.system.gdx.utils.EventPool;
import org.flixel.system.gdx.utils.RotationPool;

import com.badlogic.gdx.audio.Sound;

import flash.events.Event;
import flash.events.EventDispatcher;
import flash.media.SoundChannel;
import flash.media.SoundTransform;

public class GdxSoundChannel extends EventDispatcher implements SoundChannel
{
	private SoundTransform _soundTransform;

	private Sound _sound;
	private long _soundId;

	private static final RotationPool<GdxSoundChannel> _channels;
	private static final EventPool _events;

	static
	{
		_channels = new RotationPool<GdxSoundChannel>(16)
		{
			@Override
			protected GdxSoundChannel newObject()
			{
				return new GdxSoundChannel();
			}
		};
		_events = new EventPool(2);
	}

	public static GdxSoundChannel getNew()
	{
		GdxSoundChannel channel = _channels.obtain();
		if(channel._sound != null)
		{
			channel.stop();
			channel.dispatchEvent(_events.obtain(Event.SOUND_COMPLETE));
		}
		return channel;
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
	public float getLeftPeak()
	{
		return 0;
	}
	
	@Override
	public float getRightPeak()
	{
		return 0;
	}
	
	@Override
	public void setSoundTransform(SoundTransform soundTransform)
	{
		_soundTransform = soundTransform;
		_sound.setPan(_soundId, _soundTransform.pan, _soundTransform.volume);
		_sound.setPitch(_soundId, _soundTransform.pitch);
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
	}

	@Override
	public void pause()
	{
		_sound.pause(_soundId);
	}

	@Override
	public void resume()
	{
		_sound.resume(_soundId);
	}

	/**
	 * On Android, the sound file is not guaranteed to be loaded by the time
	 * play is called. Our current workaround is to block the application until
	 * the file is successfully played. Usually this delay is not noticeable.
	 */
	private long attemptPlay(Sound sound, float startTime, int loops, SoundTransform sndTransform)
	{
		final int PLAY_TRY_LIMIT = 5000;

		int i = 0;
		long soundId = -1;
		while(soundId == -1 && i++ < PLAY_TRY_LIMIT)
			soundId = loops > 0 ? sound.loop(sndTransform.volume) : sound.play(sndTransform.volume);
		setSoundTransform(sndTransform);
		return soundId;
	}
}
