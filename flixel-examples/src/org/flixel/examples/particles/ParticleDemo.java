package org.flixel.examples.particles;

import org.flixel.FlxGame;

public class ParticleDemo extends FlxGame
{
	public ParticleDemo()
	{
		super(400, 300, PlayState.class, 1, 30, 30);
	}
	
	@Override
	public void create()
	{
		Asset.create();
		super.create();
	}
}
