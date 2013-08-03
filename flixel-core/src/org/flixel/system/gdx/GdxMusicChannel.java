package org.flixel.system.gdx;

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
	
	private static final Pool<GdxMusicChannel> _pool = new Pool<GdxMusicChannel>(){@Override protected GdxMusicChannel newObject() {return new GdxMusicChannel();}};
	
	public static GdxMusicChannel getNew()
	{
		return _pool.obtain();
	}
	
	public GdxMusicChannel()
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
		_pool.free(this);
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
		dispatchEvent(new Event(Event.SOUND_COMPLETE));
	}
}
