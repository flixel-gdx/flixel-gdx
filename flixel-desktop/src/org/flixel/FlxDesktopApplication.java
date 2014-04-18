package org.flixel;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class FlxDesktopApplication extends LwjglApplication
{
    /**
     * Use variant without UseGL2 argument. GL2 is required since libgdx 1.0.
     */
    @Deprecated
	public FlxDesktopApplication(FlxGame Game, int Width, int Height, boolean UseGL2)
	{
		super((ApplicationListener)Game.stage, FlxG.getLibraryName(), Width, Height);
	}
	
	public FlxDesktopApplication(FlxGame Game, int Width, int Height)
	{
        super((ApplicationListener)Game.stage, FlxG.getLibraryName(), Width, Height);
	}

    public FlxDesktopApplication(FlxGame Game, String title, int Width, int Height)
    {
        super((ApplicationListener)Game.stage, title, Width, Height);
    }

    /**
     * Use variant without UseGL2 argument. GL2 is required since libgdx 1.0.
     */
    @Deprecated
	public FlxDesktopApplication(FlxGame Game, String title, int Width, int Height, boolean UseGL2)
	{
	    super((ApplicationListener)Game.stage, title, Width, Height);
	}
}
