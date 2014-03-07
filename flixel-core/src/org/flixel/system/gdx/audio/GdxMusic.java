package org.flixel.system.gdx.audio;

import org.flixel.FlxG;

import com.badlogic.gdx.audio.Music;

import flash.media.Sound;
import flash.media.SoundChannel;
import flash.media.SoundTransform;

public class GdxMusic implements Sound
{
	private Music _music;

	public GdxMusic(String path)
	{
		_music = FlxG._cache.load(path, Music.class);
	}

	@Override
	public SoundChannel play(float startTime, int loops, SoundTransform sndTransform)
	{
		if(_music.isPlaying())
			return null;
		GdxMusicChannel channel = GdxMusicChannel.getNew();
		channel.play(_music, startTime, loops, sndTransform);
		return channel;
	}
}
