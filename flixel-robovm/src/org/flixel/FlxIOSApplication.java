package org.flixel;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;
 
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

/**
 * Launch your iOS app by calling {@link #main(String[], FlxGame)}.
 * If you need to set other config options than the default
 * landscape-only mode, with accelerometer and compas disabled,
 * you shoud tweak the static {@link #config} member before calling
 * {@link #main(String[], FlxGame)}.
 * <p/>
 * This class is instantiated through the RoboVM runtime; you should never
 * do it yourself.
 * <p/>
 * Example:
 * <pre>
 * public class MyIOSGame {
 *     public static void main(String[] args) {
 *         FlxIOSApplication.config.orientationPortrait = true;
 *         FlxIOSApplication.main(args, new MyFlixelGame());
 *     }
 * }
 * </pre>
 * 
 * @author kamstrup
 */
public class FlxIOSApplication extends IOSApplication.Delegate
{
	private static FlxGame _game;
	
	public static final IOSApplicationConfiguration config = createConfig();
	
	/**
	 * Run a given {@link FlxGame} instance as an iOS app with a given set
	 * of command line arguments (normally passed directly from outer main()). 
	 * @param	Args	Command line args.
	 * @param	Game	The game instance to run.
	 */
	public static void main(String[] Args, FlxGame Game)
	{
		_game = Game;
		NSAutoreleasePool pool = new NSAutoreleasePool();
		UIApplication.main(Args, null, FlxIOSApplication.class);
		pool.close();
	}

	@Override
	protected IOSApplication createApplication()
	{		
		return new IOSApplication((ApplicationListener) _game.stage, config);
	}
	
	private static IOSApplicationConfiguration createConfig() {
		IOSApplicationConfiguration cfg = new IOSApplicationConfiguration();
		cfg.orientationLandscape = true;
		cfg.orientationPortrait = false;
		return cfg;
	}
}