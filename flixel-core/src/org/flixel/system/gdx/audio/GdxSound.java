package org.flixel.system.gdx.audio;

import org.flixel.FlxG;

import com.badlogic.gdx.audio.Sound;

import flash.media.SoundChannel;
import flash.media.SoundTransform;

public class GdxSound implements flash.media.Sound
{
	private Sound _sound;

	public GdxSound(String path)
	{
		_sound = FlxG._cache.load(path, Sound.class);
	}

	@Override
	public SoundChannel play(float startTime, int loops, SoundTransform sndTransform)
	{
		GdxSoundChannel channel = GdxSoundChannel.getNew();
		if(channel.play(_sound, startTime, loops, sndTransform) != -1)
			return channel;
		else
			return null;
	}
}
