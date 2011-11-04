package org.examples.audio;

import org.flixel.FlxGame;

public class AudioDemo extends FlxGame
{
	public AudioDemo()
	{
		super(480, 320, PlayState.class, 1, 30, 30);
	}
	
	@Override
	public void create()
	{
		Asset.create();
		super.create();
	}
}
