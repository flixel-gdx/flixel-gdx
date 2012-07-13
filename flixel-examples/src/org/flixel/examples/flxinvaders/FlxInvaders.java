package org.flixel.examples.flxinvaders;

import org.flixel.FlxG;
import org.flixel.FlxGame;
import org.flixel.examples.flxinvaders.PlayState;

/**
 * A shabby space invaders clone.
 * 
 * @author Adam Atomic
 * @author Ka Wing Chin
 */
public class FlxInvaders extends FlxGame
{
	public FlxInvaders()
	{
		super(320, 240, 320, 240, PlayState.class, 1); //Create a new FlxGame object at 320x240 with 2x pixels, then load PlayState
		forceDebugger = true;
		
		//Here we are just displaying the cursor to encourage people to click the game,
		// which will give Flash the browser focus and let the keyboard work.
		//Normally we would do this in say the main menu state or something,
		// but FlxInvaders has no menu :P
		FlxG.mouse.show();
	}
}
