package org.flixel.examples.draganddrop;

import org.flixel.FlxGame;

public class DragAndDropDemo extends FlxGame
{
	public DragAndDropDemo()
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
