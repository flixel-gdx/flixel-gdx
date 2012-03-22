package org.flixel.examples.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Asset
{
	public static Music SndMode;
	public static Sound Sndflixel;

	public static void create()
	{
		SndMode = Gdx.audio.newMusic(Gdx.files.internal("examples/audio/mode.ogg"));
		Sndflixel = Gdx.audio.newSound(Gdx.files.classpath("org/flixel/data/flixel.ogg"));
	}

}
