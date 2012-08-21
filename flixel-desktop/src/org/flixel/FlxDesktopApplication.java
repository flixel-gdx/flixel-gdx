package org.flixel;

import org.flixel.system.FlxApplication;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglNet;

public class FlxDesktopApplication extends LwjglApplication implements FlxApplication
{
	protected LwjglNet _net;
	
	public FlxDesktopApplication(ApplicationListener listener, int width, int height)
	{
		super(listener, FlxG.getLibraryName(), width, height, false);
		_net = new LwjglNet();
	}

	@Override
	public Net getNet() 
	{
		return _net;
	}
}
