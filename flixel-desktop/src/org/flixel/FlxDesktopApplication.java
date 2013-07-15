package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class FlxDesktopApplication extends LwjglApplication
{
	public FlxDesktopApplication(FlxGame Game, int Width, int Height, boolean UseGL2)
	{
		super(Game, FlxG.getLibraryName(), Width, Height, UseGL2);
	}
	
	public FlxDesktopApplication(FlxGame Game, int Width, int Height)
	{
		this(Game, Width, Height, false);
	}
}
