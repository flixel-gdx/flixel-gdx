package org.flixel.examples.thousandparticles;

import org.flixel.FlxGame;

public class ThousandParticlesDemo extends FlxGame
{
	public ThousandParticlesDemo()
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
