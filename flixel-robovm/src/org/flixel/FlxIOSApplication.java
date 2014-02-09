package org.flixel;

import org.robovm.cocoatouch.foundation.NSAutoreleasePool;
import org.robovm.cocoatouch.uikit.UIApplication;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

public class FlxIOSApplication extends IOSApplication.Delegate
{
	private FlxGame _game;
	private IOSApplicationConfiguration _config;
	
	public FlxIOSApplication(String[] args, FlxGame game, IOSApplicationConfiguration config)
	{
		_game = game;
		_config = config;
		
		NSAutoreleasePool pool = new NSAutoreleasePool();
		UIApplication.main(args, null, FlxIOSApplication.class);
		pool.drain();
	}

	@Override
	protected IOSApplication createApplication()
	{		
		return new IOSApplication((ApplicationListener) _game.stage, _config);
	}
}