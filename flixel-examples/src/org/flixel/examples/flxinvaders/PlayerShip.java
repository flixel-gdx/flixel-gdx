package org.flixel.examples.flxinvaders;

import org.flixel.FlxButton;
import org.flixel.FlxG;
import org.flixel.FlxGamePad;
import org.flixel.FlxSprite;

public class PlayerShip extends FlxSprite		//Class declaration for the player's little ship
{
	private FlxGamePad _pad;
	private boolean _justPressed;
	
	private static final String ImgShip = "examples/flxinvaders/pack:ship";	//Graphic of the player's ship
	
	//Constructor for the player - just initializing a simple sprite using a graphic.
	public PlayerShip(FlxGamePad Pad)
	{
		//This initializes this sprite object with the graphic of the ship and
		// positions it in the middle of the screen.
		super(FlxG.width/2-6, FlxG.height-12, ImgShip);
		
		_pad = Pad;
		_justPressed = false;
	}
	
	//Basic game loop function again!
	@Override
	public void update()
	{
		//Controls!
		velocity.x = 0;				//Default velocity to zero
		if(FlxG.keys.LEFT || _pad.buttonLeft.status == FlxButton.PRESSED)
			velocity.x -= 150;		//If the player is pressing left, set velocity to left 150
		if(FlxG.keys.RIGHT || _pad.buttonRight.status == FlxButton.PRESSED)	
			velocity.x += 150;		//If the player is pressing right, then right 150
		
		//Just like in PlayState, this is easy to forget but very important!
		//Call this to automatically evaluate your velocity and position and stuff.
		super.update();
		
		//Here we are stopping the player from moving off the screen,
		// with a little border or margin of 4 pixels.
		if(x > FlxG.width-width-4)
			x = FlxG.width-width-4; //Checking and setting the right side boundary
		if(x < 4)
			x = 4;					//Checking and setting the left side boundary
		
		//Finally, we gotta shoot some bullets amirite?  First we check to see if the
		// space bar was just pressed (no autofire in space invaders you guys)
		if(FlxG.keys.SPACE || _pad.buttonA.status == FlxButton.PRESSED)
		{
			if (!_justPressed)
			{	
				//Space bar was pressed!  FIRE A BULLET
				FlxSprite bullet = (FlxSprite) ((PlayState)FlxG.getState()).playerBullets.recycle(FlxSprite.class);
				bullet.reset(x + width/2 - bullet.width/2, y);
				bullet.velocity.y = -140;
				_justPressed = true;
			}
		}
		else
			_justPressed = false;
	}
}
