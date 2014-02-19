package org.flixel;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class FlxDesktopApplication extends LwjglApplication
{
	public FlxDesktopApplication(FlxGame Game, int Width, int Height, boolean UseGL2)
	{
		super((ApplicationListener)Game.stage, FlxG.getLibraryName(), Width, Height, UseGL2);
	}
	
	public FlxDesktopApplication(FlxGame Game, int Width, int Height)
	{
		this(Game, Width, Height, false);
	}
	
	public FlxDesktopApplication(FlxGame Game, String title, int Width, int Height, boolean UseGL2)
	{
	    super((ApplicationListener)Game.stage, title, Width, Height, UseGL2);
	}
}
