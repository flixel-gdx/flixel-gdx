package org.examples.scrolling;

import org.flixel.FlxGame;

public class ScrollingDemo extends FlxGame
{

	public ScrollingDemo()
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
