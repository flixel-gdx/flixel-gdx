package org.flixel.examples.particles;

import org.flixel.FlxButton;
import org.flixel.FlxEmitter;
import org.flixel.FlxG;
import org.flixel.FlxGroup;
import org.flixel.FlxParticle;
import org.flixel.FlxPoint;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.flixel.event.AFlxButton;


public class PlayState extends FlxState
{
	// Our emmiter
	private FlxEmitter theEmitter;

	// Our white pixel (This is to prevent creating 200 new pixels all to a new variable each loop)
	private FlxParticle whitePixel;

	// Some buttons
	private FlxButton collisionButton;
	private FlxButton gravityButton;

	// some walls stuff
	private FlxGroup collisionGroup;
	private FlxSprite wall;
	private FlxSprite floor;

	// We'll use these to track the current state of gravity and collision
	private boolean isGravityOn = false;
	private boolean isCollisionOn = false;

	// Just a useful flxText for notifications
	private FlxText topText;

	@Override
	public void create()
	{
		FlxG.setFramerate(60);
		FlxG.setFlashFramerate(60);
		
		//Here we actually initialize out emitter
		//The parameters are X Y Size (Maximum number of particles the emitter can store)
		theEmitter = new FlxEmitter(10, FlxG.height / 2, 200);

		//Now by default the emitter is going to have some properties set on it and can be used immediately
		//but we're going to change a few things.

		//First this emitter is on the side of the screen, and we want to show off the movement of the particles
		//so lets make them launch to the right.
		theEmitter.setXSpeed(100, 200);

		//and lets funnel it a tad
		theEmitter.setYSpeed( -50, 50);

		//Let's also make our pixels rebound off surfaces
		theEmitter.bounce = .8f;

		//Now let's add the emitter to the state.
		add(theEmitter);

		//Now it's almost ready to use, but first we need to give it some pixels to spit out!
		//Lets fill the emitter with some white pixels
		for (int i = 0; i < theEmitter.getMaxSize()/2; i++) 
		{
			whitePixel = new FlxParticle();
			whitePixel.makeGraphic(2, 2, 0xFFFFFFFF);
			whitePixel.visible = false; // Make sure the particle doesn't show up at (0, 0)
			theEmitter.add(whitePixel);
			whitePixel = new FlxParticle();
			whitePixel.makeGraphic(1, 1, 0xFFFFFFFF);
			whitePixel.visible = false;
			theEmitter.add(whitePixel);
		}

		//Now let's setup some buttons for messing with the emitter.
		collisionButton = new FlxButton(2, FlxG.height - 68, "Collision", onCollision);
		add(collisionButton);
		gravityButton = new FlxButton(82, FlxG.height - 68, "Gravity", onGravity);
		add(gravityButton);

		//I'll just leave this here
		topText = new FlxText(0, 2, FlxG.width, "Welcome");
//		topText.alignment = "center";
		add(topText);

		//Lets setup some walls for our pixels to collide against
		collisionGroup = new FlxGroup();
		wall= new FlxSprite(100, FlxG.height/2-100/2);
		wall.makeGraphic(10, 100, 0x50FFFFFF);//Make it darker - easier on the eyes :)
		wall.visible = false;
		wall.setSolid(false);//Set both the visibility AND the solidity to false, in one go
		wall.immovable = true;//Lets make sure the pixels don't push out wall away! (though it does look funny)
		collisionGroup.add(wall);
		//Duplicate our wall but this time it's a floor to catch gravity affected particles
		floor = new FlxSprite(10, 267);
		floor.makeGraphic(FlxG.width - 20, 10, 0x50FFFFFF);
		floor.visible = false;
		floor.setSolid(false);
		floor.immovable = true;
		collisionGroup.add(floor);

    //Please note that this demo makes the walls themselves not collide, for the sake of simplicity.
    //Normally you would make the particles have solid = true or false to make them collide or not on creation,
    //because in a normal environment your particles probably aren't going to change solidity at a mouse
    //click. If they did, you would probably be better suited with emitter.setAll("solid", true)
    //I just don't feel that setAll is applicable here(Since I would still have to toggle the walls anyways)

		//Don't forget to add the group to the state(Like I did :P)
		add(collisionGroup);

		//Now lets set our emitter free.
		//Params: Explode, Particle Lifespan, Emit rate(in seconds)
		theEmitter.start(false, 3, .01f);
	}
	
	
	@Override
	public void update()
	{
		super.update();
		FlxG.collide(theEmitter, collisionGroup);
	}
	
	
	AFlxButton onCollision = new AFlxButton()
	{
		@Override
		public void onUp()
		{
			isCollisionOn = !isCollisionOn;
			if(isCollisionOn)
			{
				if(isGravityOn)
				{
					floor.setSolid(true); // Set the floor to the 'active' collision barrier
					floor.visible = true;
					wall.setSolid(false);
					wall.visible = false;
				}
				else
				{
					floor.setSolid(false); // Set the wall to the 'active' collision barrier
					floor.visible = false;
					wall.setSolid(true);
					wall.visible = true;
				}
				topText.setText("Collision: ON");
			}
			else
			{
				// Turn off the wall and floor, completely
				wall.setSolid(false);
				floor.setSolid(false);
				wall.visible = floor.visible = false;
				topText.setText("Collision: OFF");
			}
			topText.setAlpha(1f);
			FlxG.log("Toggle Collision");
		}
	};
	
	
	AFlxButton onGravity = new AFlxButton()
	{
		@Override
		public void onUp()
		{
			isGravityOn = !isGravityOn;
			if(isGravityOn)
			{
				theEmitter.gravity = 200;
				if(isCollisionOn)
				{
					floor.visible = true;
					floor.setSolid(true);
					wall.visible = false;
					wall.setSolid(false);
				}
				// Just for the sake of completeness let's go ahead and make this change happen
				// to all of the currently emitted particles as well.
				theEmitter.setAll("acceleration", new FlxPoint(0, 200));
				topText.setText("Gravity: ON");
			}
			else
			{
				theEmitter.gravity = 0;
				if(isCollisionOn)
				{
					wall.visible = true;
					wall.setSolid(true);
					floor.visible = false;
					floor.setSolid(false);
				}
				theEmitter.setAll("acceleration", new FlxPoint(0, 0));
				topText.setText("Gravity: OFF");
			}
			topText.setAlpha(1);
			FlxG.log("Toggle Gravity");
		}
	};
}
