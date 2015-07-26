package org.flixel.system.gdx.audio;

import org.flixel.system.gdx.utils.EventPool;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.utils.Pool;

import flash.events.Event;
import flash.events.EventDispatcher;
import flash.media.SoundChannel;
import flash.media.SoundTransform;

public class GdxMusicChannel extends EventDispatcher implements SoundChannel, OnCompletionListener
{
	private SoundTransform _soundTransform;

	private Music _music;

	private static final Pool<GdxMusicChannel> _channels;
	private static final EventPool _events;

	static
	{
		_channels = new Pool<GdxMusicChannel>()
		{
			@Override
			protected GdxMusicChannel newObject()
			{
				return new GdxMusicChannel();
			}
		};
		_events = new EventPool(2);
	}

	public static GdxMusicChannel getNew()
	{
		return _channels.obtain();
	}

	private GdxMusicChannel()
	{
	}

	void play(Music music, float startTime, int loops, SoundTransform sndTransform)
	{
		_music = music;

		if(loops > 0)
			_music.setLooping(true);
		else
			_music.setLooping(false);

		setSoundTransform(sndTransform);

		_music.setOnCompletionListener(this);
		_music.play();
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
		_music.setPan(soundTransform.pan, soundTransform.volume);
	}

	@Override
	public SoundTransform getSoundTransform()
	{
		return _soundTransform;
	}

	@Override
	public void stop()
	{
		_music.stop();
		_channels.free(this);
	}

	@Override
	public void pause()
	{
		_music.pause();
	}

	@Override
	public void resume()
	{
		if(!_music.isPlaying())
			_music.play();
	}

	@Override
	public void onCompletion(Music music)
	{
		stop();
		dispatchEvent(_events.obtain(Event.SOUND_COMPLETE));
	}
}
