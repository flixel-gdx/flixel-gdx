package org.flixel;

import android.graphics.Canvas;


/**
 * This is the basic game "state" object - e.g. in a simple game
 * you might have a menu state and a play state.
 * It acts as a kind of container for all your game objects.
 * You can also access the game's background color
 * and screen buffer through this object.
 * FlxState is kind of a funny class from the technical side,
 * it is just a regular Flash Sprite display object,
 * with one member variable: a flixel <code>FlxGroup</code>.
 * This means you can load it up with regular Flash stuff
 * or with flixel elements, whatever works!
 */
public class FlxState extends Canvas
{
	/**
	 * This static variable holds the screen buffer, so you can draw to it
	 * directly if you want.
	 */
	static public FlxSprite screen;
	/**
	 * This static variable indicates the "clear color" or default background
	 * color of the game. Change it at ANY time using
	 * <code>FlxState.bgColor</code>.
	 */
	static public int bgColor;
	/**
	 * Internal group used to organize and display objects you add to this
	 * state.
	 */
	public FlxGroup defaultGroup;

	/**
	 * Creates a new <code>FlxState</code> object,
	 * instantiating <code>screen</code> if necessary.
	 */
	public FlxState()
	{
		super();
		defaultGroup = new FlxGroup();
		if(screen == null)
		{
			screen = new FlxSprite();
			screen.createGraphic(FlxG.width, FlxG.height, 0xFFFF0000, true);
			screen.origin.x = screen.origin.y = 0;
			screen.antialiasing = true;
			screen.exists = false;
			screen.setSolid(false);
			screen.setFixed(true);
		}
	}

	/**
	 * Override this function to set up your game state.
	 * This is where you create your groups and game objects and all that good stuff.
	 */
	public void create()
	{
		//nothing to create initially
	}
	
	/**
	 * Adds a new FlxCore subclass (FlxSprite, FlxBlock, etc) to the game loop.
	 * FlxState is adding this object to its built-in FlxGroup to automate updating and rendering.
	 * 
	 * @param	Core	The object you want to add to the game loop.
	 */
	public FlxObject add(FlxObject Core)
	{
		return defaultGroup.add(Core);
	}

	/**
	 * Override this function to do special pre-processing FX like motion blur.
	 * You can use scaling or blending modes or whatever you want against
	 * <code>FlxState.screen</code> to achieve all sorts of cool FX.
	 */
	public void preProcess()
	{
		screen.fill(bgColor);	//Default behavior - just overwrite buffer with background color
	}
	
	/**
	 * Automatically goes through and calls update on everything you added to the game loop,
	 * override this function to handle custom input and perform collisions/
	 */
	public void update()
	{
		defaultGroup.update();
	}
	
	/**
	 * This function collides <code>defaultGroup</code> against <code>defaultGroup</code>
	 * (basically everything you added to this state).
	 */
	public void collide()
	{
		FlxU.collide(defaultGroup,defaultGroup);
	}
	
	/**
	 * Automatically goes through and calls render on everything you added to the game loop,
	 * override this loop to manually control the rendering process.
	 */
	public void render()
	{
		defaultGroup.render();
	}

	/**
	 * Override this function to do special pre-processing FX like light bloom.
	 * You can use scaling or blending modes or whatever you want against
	 * <code>FlxState.screen</code> to achieve all sorts of cool FX.
	 */
	public void postProcess()
	{
		//no fx by default
	}

	/**
	 * Override this function to handle any deleting or "shutdown" type operations you
	 * might need (such as removing traditional Flash children like Sprite objects).
	 */
	public void destroy()
	{
		defaultGroup.destroy();
	}
}
