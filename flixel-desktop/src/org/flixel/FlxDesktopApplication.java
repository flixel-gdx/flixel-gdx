package org.flixel;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class FlxDesktopApplication extends LwjglApplication
{
	public FlxDesktopApplication(ApplicationListener listener, int width, int height, boolean useGL2)
	{
		super(listener, FlxG.getLibraryName(), width, height, useGL2);
	}
	
	public FlxDesktopApplication(ApplicationListener listener, int width, int height)
	{
		this(listener, width, height, false);
	}
}
