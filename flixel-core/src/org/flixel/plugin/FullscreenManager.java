package org.flixel.plugin;
 
import com.badlogic.gdx.Gdx;
 
import org.flixel.FlxBasic;
import org.flixel.FlxG;
 
/**
 * A simple plugin for switching between fullscreen and windowed mode
 * @author Noah Greenberg
 */
public class FullscreenManager extends FlxBasic {
	public String hotkey;
	
	private boolean inFullscreen = false;
	private int fullscreenWidth, fullscreenHeight, windowedWidth, windowedHeight;
	
	public FullscreenManager(int fullscreenWidth, int fullscreenHeight, int windowedWidth, int windowedHeight)
	{
		this.fullscreenWidth = fullscreenWidth;
		this.fullscreenHeight = fullscreenHeight;
		this.windowedWidth = windowedWidth;
		this.windowedHeight = windowedHeight;
	}
	
	public FullscreenManager(int fullscreenWidth, int fullscreenHeight, int windowedWidth, int windowedHeight, String hotkey)
	{
		this.fullscreenWidth = fullscreenWidth;
		this.fullscreenHeight = fullscreenHeight;
		this.windowedWidth = windowedWidth;
		this.windowedHeight = windowedHeight;
		this.hotkey = hotkey;
	}
	
	/**
	 * If in fullscreen mode, the game will enter windowed mode.
	 * If in windowed mode, the game will enter fullscreen mode.
	 */
	public void toggle()
	{
		if(inFullscreen)
			Gdx.app.getGraphics().setDisplayMode(windowedWidth, windowedHeight, false);
		else
			Gdx.app.getGraphics().setDisplayMode(fullscreenWidth, fullscreenHeight, true);
		inFullscreen = !inFullscreen;
	}
	
	@Override
	public void update()
	{
		if(hotkey != null && FlxG.keys.justReleased(hotkey))
			toggle();
	}
}