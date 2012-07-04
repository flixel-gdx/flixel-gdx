package org.flixel.examples.flxinvaders;

import org.flixel.FlxG;
import org.flixel.FlxSprite;
import org.flixel.event.AFlxButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;

public class PlayerShip extends FlxSprite		//Class declaration for the player's little ship
{
	public AFlxButton fire;
	public AFlxButton left;
	public AFlxButton right;
	
	//Constructor for the player - just initializing a simple sprite using a graphic.
	public PlayerShip()
	{
		//This initializes this sprite object with the graphic of the ship and
		// positions it in the middle of the screen.
		super(FlxG.width/2-6, FlxG.height-12, Asset.ImgShip);
		
		if(Gdx.app.getType() == ApplicationType.Android)
		{
			fire = new AFlxButton(){@Override public void onDown(){shootBullet();}};
			left = new AFlxButton()
			{
				@Override	public void onPressed(){moveLeft();}
				@Override	public void onUp(){velocity.x=0;}
				@Override	public void onOut(){velocity.x=0;}
			};
			right = new AFlxButton()
			{
				@Override 	public void onPressed(){moveRight();}
				@Override	public void onUp(){velocity.x=0;}
				@Override	public void onOut(){velocity.x=0;}
			};
		}		
	}
	
	//Basic game loop function again!
	@Override
	public void update()
	{
		//Controls!
//		velocity.x = 0;				//Default velocity to zero
		if(FlxG.keys.LEFT)
			moveLeft();		//If the player is pressing left, set velocity to left 150
		if(FlxG.keys.RIGHT)	
			moveRight();		//If the player is pressing right, then right 150
		if(FlxG.keys.justReleased(Keys.LEFT) || FlxG.keys.justReleased(Keys.RIGHT)) // Default velocity to zero
			velocity.x = 0;
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
		if(FlxG.keys.justPressed("SPACE"))
		{
			//Space bar was pressed!  FIRE A BULLET
			shootBullet();
		}
	}
	
	public void moveLeft()
	{
		velocity.x = 0;
		velocity.x -= 150;
	}
	
	public void moveRight()
	{
		velocity.x = 0;
		velocity.x = 150;
	}
	
	public void shootBullet()
	{
		FlxSprite bullet = (FlxSprite) ((PlayState)FlxG.getState()).playerBullets.recycle(FlxSprite.class);
		bullet.reset(x + width/2 - bullet.width/2, y);
		bullet.velocity.y = -140;
	}
}
