package org.flixel;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;
 
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

/**
 * Implement this abstract class and instantiate it in your main()
 * to run on iOS.
 * <p/>
 * Example:
 * <pre>
 * public class MyIOSGame extends FlxIOSApplication {
 *     @Override public FlxGame createGame() { return new MyGame(); }
 * 
 *     public static void main (String[] args) {
 *         new MyIOSGame();
 *     } 
 *
 * }
 * </pre>
 */
public abstract class FlxIOSApplication extends IOSApplication.Delegate
{
	public FlxIOSApplication(String[] args)
	{
		NSAutoreleasePool pool = new NSAutoreleasePool();
		UIApplication.main(args, null, getClass());
		pool.close();
	}

	@Override
	protected IOSApplication createApplication()
	{		
		return new IOSApplication((ApplicationListener) createGame().stage, createConfig());
	}
	
	/**
	 * Override this method and create your FlxGame instance here.
	 * @return a new FlxGame instance
	 */
	protected abstract FlxGame createGame();
	
	/**
	 * Configuration factory for the app. Override if you need anything else
	 * than the default landscape-only mode.
	 * @return a new configuration for the iOS app.
	 */
	protected IOSApplicationConfiguration createConfig() {
		IOSApplicationConfiguration cfg = new IOSApplicationConfiguration();
		cfg.orientationLandscape = true;
		cfg.orientationPortrait = false;
		return cfg;
	}
}
