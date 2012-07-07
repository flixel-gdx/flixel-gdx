package org.flixel.examples.collision;

import org.flixel.*;
import org.flixel.event.AFlxButton;

public  class PlayState extends FlxState
{
	//This is for our messages
	private FlxText topText;

	//This is our elevator, for smashing the crates
	private FlxSprite elevator;

	//We'll reuse this when we make a bunch of crates
	private FlxSprite crate;

	//We'll make 100 per group crates to smash about
	private int numCrates = 100;

	//these are the groups that will hold all of our crates
	private FlxGroup crateStormGroup;
	private FlxGroup crateStormGroup2;
	private FlxGroup crateStormMegaGroup;

	//We'll make a sweet flixel logo to ride the elevator for option #2
	private FlxSprite flixelRider;

	//Here we have a few buttons for use in altering the demo
	private FlxButton crateStorm;
	private FlxButton crateStormG1;
	private FlxButton crateStormG2;
	private FlxButton flxRiderButton;
	private FlxButton groupCollision;

	//Some toggle variables for use with the buttons
	private boolean isCrateStormOn = true;
	private boolean isFlxRiderOn = false;
	private boolean collideGroups = false;
	private boolean redGroup = true;
	private boolean blueGroup = true;
	private boolean rising = true;

	@Override
	public void create()
	{
		FlxG.setBgColor(0xff000000);
		
		//Kick the framerate back up
		FlxG.setFramerate(60);
		FlxG.setFlashFramerate(60);

		//Let's setup our elevator, for some wonderful crate bashing goodness
		elevator = new FlxSprite((FlxG.width / 2) - 100, 250, Asset.ElevatorPNG);
		//Make it able to collide, and make sure it's not tossed around
		elevator.setSolid(elevator.immovable = true);
		//And add it to the state
		add(elevator);

		//Now lets get some crates to smash around, normally I would use an emitter for this
		//kind of scene, but for this demo I wanted to use regular sprites 
		//(See ParticlesDemo for an example of an emitter with colliding particles)
		//We'll need a group to place everything in - this helps a lot with collisions
		crateStormGroup = new FlxGroup();
		for (int i = 0; i < numCrates; i++) {
			crate = new FlxSprite((FlxG.random() * 200) + 100, 20);
			crate.loadRotatedGraphic(Asset.CratePNG, 16, 0); //This loads in a graphic, and 'bakes' some rotations in so we don't waste resources computing real rotations later
			crate.angularVelocity = FlxG.random() * 50-150; //Make it spin a tad
			crate.acceleration.y = 300; //Gravity
			crate.acceleration.x = -50; //Some wind for good measure
			crate.maxVelocity.y = 500; //Don't fall at 235986mph
			crate.maxVelocity.x = 200; //"      fly  "  "
			crate.elasticity = FlxG.random(); //Let's make them all bounce a little bit differently
			crateStormGroup.add(crate);
		}
		add(crateStormGroup);
		//And another group, this time - Red crates
		crateStormGroup2 = new FlxGroup();
		for (int i = 0; i < numCrates; i++) {
			crate = new FlxSprite((FlxG.random() * 200) + 100, 20);
			crate.loadRotatedGraphic(Asset.CratePNG, 16, 1);
			crate.angularVelocity = FlxG.random() * 50-150;
			crate.acceleration.y = 300;
			crate.acceleration.x = 50;
			crate.maxVelocity.y = 500;
			crate.maxVelocity.x = 200;
			crate.elasticity = FlxG.random();
			crateStormGroup2.add(crate);
		}
		add(crateStormGroup2);

		//Now what we're going to do here is add both of those groups to a new containter group
		//This is useful if you had something like, coins, enemies, special tiles, etc.. that would all need
		//to check for overlaps with something like a player.
		crateStormMegaGroup = new FlxGroup();
		crateStormMegaGroup.add(crateStormGroup);
		crateStormMegaGroup.add(crateStormGroup2);

		//Cute little flixel logo that will ride the elevator
		flixelRider = new FlxSprite((FlxG.width / 2) - 13, 0, Asset.FlixelRiderPNG);
		flixelRider.setSolid(flixelRider.visible = flixelRider.exists = false); //But we don't want him on screen just yet...
		flixelRider.acceleration.y = 800;
		add(flixelRider);

		//This is for the text at the top of the screen
		topText = new FlxText(0, 2, FlxG.width, "Welcome");
		topText.setAlignment("center");
		add(topText);

		//Lets make a bunch of buttons! YEAH!!!
		crateStorm = new FlxButton(2, FlxG.height - 22, "Crate Storm", new AFlxButton(){@Override public void onUp(){onCrateStorm();}});
		add(crateStorm);
		flxRiderButton = new FlxButton(82, FlxG.height - 22, "Flixel Rider", new AFlxButton(){@Override public void onUp(){onFlixelRider();}});
		add(flxRiderButton);
		crateStormG1 = new FlxButton(162, FlxG.height - 22, "Blue Group", new AFlxButton(){@Override public void onUp(){onBlue();}});
		add(crateStormG1);
		crateStormG2 = new FlxButton(242, FlxG.height - 22, "Red Group", new AFlxButton(){@Override public void onUp(){onRed();}});
		add(crateStormG2);
		groupCollision = new FlxButton(320, FlxG.height - 22, "Collide Groups", new AFlxButton(){@Override public void onUp(){onCollideGroups();}});
		add(groupCollision);

		//And lets get the flixel cursor visible again
		FlxG.mouse.show();
	}

	@Override
	public void update()
	{
		//This is just to make the text at the top fade out
		if (topText.getAlpha() > 0) {
			topText.setAlpha(topText.getAlpha() - .01f);
		}

		//Here we'll make the elevator rise and fall - all of the constants chosen here are just after tinkering
		if (rising) {
			elevator.velocity.y-= 10;
		}else {
			elevator.velocity.y+= 10;
		}
		if (elevator.velocity.y == -300) {
			rising = false;
		}else if (elevator.velocity.y == 300) {
			rising = true;
		}

		//Run through the groups, and if a crate is off screen, get it back!
		FlxSprite s;
		for (FlxBasic a : crateStormGroup.members) {
			s = (FlxSprite) a;
			if (s.x < -10)
				s.x = 400;
			if (s.x > 400)
				s.x = -10;
			if (s.y > 300)
				s.y = -10;
		}
		for (FlxBasic a : crateStormGroup2.members) {
			s = (FlxSprite) a;
			if (s.x > 400)
				s.x = -10;
			if (s.x < -10)
				s.x = 400;
			if (s.y > 300)
				s.y = -10;
		}
		super.update();

		//Here we call our simple collide() function, what this does is checks to see if there is a collision
		//between the two objects specified, But if you pass in a group then it checks the group against the object,
		//or group against a group, You can even check a group of groups against an object - You can see the possibilities this presents.
		//To use it, simply call FlxG.collide(Group/Object1, Group/Object2, Notification(optional))
		//If you DO pass in a notification it will fire the function you created when two objects collide - allowing for even more functionality.
		if(collideGroups)
			FlxG.collide(crateStormGroup, crateStormGroup2);
		if(isCrateStormOn)
			FlxG.collide(elevator, crateStormMegaGroup);
		if (isFlxRiderOn) 
			FlxG.collide(elevator, flixelRider);
		//We don't specify a callback here, because we aren't doing anything super specific - just using the default collide method.
	}

	//This calls our friend the Flixel Rider into play
	private void onFlixelRider() {
		if(!isFlxRiderOn){
			isFlxRiderOn = true; //Make the state aware that Flixel Rider is here
			isCrateStormOn = false; //Tell the state that the crates are off as of right now
			crateStormGroup.visible = crateStormGroup.exists = false; //Turn off the Blue crates
			crateStormGroup2.visible = crateStormGroup2.exists = false; //Turn off the Red crates
			flixelRider.setSolid(flixelRider.visible = flixelRider.exists = true); //Turn on the Flixel Rider
			flixelRider.y = flixelRider.velocity.y = 0; //Reset him at the top of the screen(Dont be like me and have him appear under the elevator :P)
			crateStormG1.visible = false; //Turn off the button for toggling the Blue group
			crateStormG2.visible = false; //Turn off the button for toggling the Red group
			groupCollision.visible = false; //Turn off the button for toggling group collision
			topText.setText("Flixel Elevator Rider!");
			topText.setAlpha(1.f);
		}
	}

	//Enable the CRATE STOOOOOORM!
	private void onCrateStorm() {
		isCrateStormOn = true;
		isFlxRiderOn = false;
		if(blueGroup)
			crateStormGroup.visible = crateStormGroup.exists = true;
		if(redGroup)
			crateStormGroup2.visible = crateStormGroup2.exists = true;
		flixelRider.setSolid(flixelRider.visible = flixelRider.exists =false);
		crateStormG1.visible = true;
		crateStormG2.visible = true;
		if(blueGroup && redGroup)
			groupCollision.visible = true;
		topText.setText("CRATE STOOOOORM!");
		topText.setAlpha(1.f);
	}

	//Toggle the Blue group
	private void onBlue() {
		blueGroup = !blueGroup;
		crateStormGroup.visible = crateStormGroup.exists = !crateStormGroup.exists;
		for (FlxBasic a : crateStormGroup.members) {
			((FlxObject) a).setSolid(!((FlxObject) a).getSolid());//Run through and make them not collide - I'm not sure if this is neccesary
		}
		if (blueGroup && redGroup) {
			groupCollision.visible = true;
		}else {
			groupCollision.visible = false;
		}
		if(!blueGroup){
			topText.setText("Blue Group: Disabled");
			topText.setAlpha(1.f);
		}else {
			topText.setText("Blue Group: Enabled");
			topText.setAlpha(1.f);
		}
	}

	//Toggle the Red group
	private void onRed() {
		redGroup = !redGroup;
		crateStormGroup2.visible = crateStormGroup2.exists = !crateStormGroup2.exists;
		for (FlxBasic a : crateStormGroup2.members) {
			((FlxObject) a).setSolid(!((FlxObject) a).getSolid());
		}
		if (blueGroup && redGroup) {
			groupCollision.visible = true;
		}else {
			groupCollision.visible = false;
		}
		if(!redGroup){
			topText.setText("Red Group: Disabled");
			topText.setAlpha(1.f);
		}else {
			topText.setText("Red Group: Enabled");
			topText.setAlpha(1.f);
		}
	}

	//Toggle the group collision
	private void onCollideGroups() {
		collideGroups = !collideGroups;
		if(!collideGroups){
			topText.setText("Group Collision: Disabled");
			topText.setAlpha(1.f);
		}else {
			topText.setText("Group Collision: Enabled");
			topText.setAlpha(1.f);
		}
	}
}