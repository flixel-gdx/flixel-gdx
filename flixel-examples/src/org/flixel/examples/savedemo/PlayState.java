package org.flixel.examples.savedemo;

import org.flixel.*;
import org.flixel.event.AFlxButton;

import com.badlogic.gdx.utils.Array;

public class PlayState extends FlxState
{
	//Here's the FlxSave variable this is what we're going to be saving to.
	private FlxSave gameSave;

	//We're just going to drop a bunch of boxes into a group
	private FlxGroup boxGroup;
	private int numBoxes = 20;

	//We'll use these variables for the dragging
	private FlxPoint dragOffset;
	private boolean dragging = false;
	private FlxObject dragTarget;

	//Buttons for the demo
	private FlxButton demoSave;
	private FlxButton demoLoad;
	private FlxButton demoClear;

	//The top text that yells at you
	private FlxText topText;

	@Override
	public void create()
	{
		FlxG.setBgColor(0xFF000000);
		FlxG.setFramerate(60);
		FlxG.setFlashFramerate(60);

		//So here's the core of this demo - the FlxSave you have to instantiate a new one before you can use it
		gameSave = new FlxSave();
		//And then you have to bind it to the save data, you can use different bind strings in different parts of your game
		//you MUST bind the save before it can be used.
		gameSave.bind("SaveDemo");

		//Since we need the text before the usual end of the demo we'll initialize it up here.
		topText = new FlxText(0, 2, FlxG.width, "Welcome!");
		topText.setAlignment("center");

		//This just makes some dim text with instructions
		FlxText dragText = new FlxText(5, FlxG.height / 2 -20, FlxG.width, "Click to Drag");
		dragText.setColor(0x10101010);
		dragText.setSize(50);
		add(dragText);

		//Set out offset to non-null here
		dragOffset = new FlxPoint(0, 0);

		//Make a group to place the boxes in
		boxGroup = new FlxGroup();
		//And let's make some boxes!
		
		FlxButton box;
		FlxPoint[] boxPositions = gameSave.get( "boxPositions", FlxPoint[].class);
		for (int i = 0; i < numBoxes; i++) {
			//If we already have some save data to work with, then let's go ahead and put it to use	
			if (boxPositions != null){
				 box = new FlxButton(boxPositions[i].x, boxPositions[i].y, String.valueOf(i + 1));
				//I'm using a FlxButton in this instance because I can use if(button.state == FlxButton.PRESSED) 
				//to detect if the mouse is held down on a button
				topText.setText("Loaded positions");
			}
			//If not, oh well we'll just put them in the default locations
			else{
				box = new FlxButton((i * 35) + 9, 50, String.valueOf(i+1));
				if (i * 35 > 360){
					box.y = 85;
					box.x = (i * 35 -2) - 339;
				}
				topText.setText("No save found, using default positions");
			}
			box.makeGraphic(32, 32, 0xFFAAAAAA); //Make a graphic for our button, instead of the default
			boxGroup.add(box); //And add it to the group
		}
		add(boxGroup);//Add the group to the state

		//Get out buttons set up along the bottom of the screen
		demoSave = new FlxButton(2, FlxG.height -22, "Save Locations", new AFlxButton(){@Override public void callback(){onSave();}});
		add(demoSave);
		demoLoad = new FlxButton(82, FlxG.height -22, "Load Locations", new AFlxButton(){@Override public void callback(){onLoad();}});
		add(demoLoad);
		demoClear = new FlxButton(202, FlxG.height -22, "Clear Save", new AFlxButton(){@Override public void callback(){onClear();}});
		add(demoClear);

		//Let's not forget about our old text, which needs to be above everything else
		add(topText);

		//Let's re show the cursors
		FlxG.mouse.show();
	}

	@Override
	public void update()
	{
		//This is just to make the text at the top fade out
		if (topText.getAlpha() > 0) {
			topText.setAlpha(topText.getAlpha() - .005f);
		}

		super.update();
		//if you've clicked, lets see if you clicked on a button
		//Note something like this needs to be after super.update() that way the button's state has updated to reflect the mouse event
		if (FlxG.mouse.justPressed()) {
			for (FlxBasic a : boxGroup.members) {
				FlxButton b = (FlxButton) a;
				if (b.status == FlxButton.PRESSED) {
					dragOffset.x = b.x - FlxG.mouse.x; //The offset is used to make the box stick to the cursor and not snap to the corner
					dragOffset.y = b.y - FlxG.mouse.y;
					dragging = true;
					dragTarget = b;
				}
			}
		}
		//If you let go, then release that box!
		if (FlxG.mouse.justReleased()) {
			dragTarget = null;
			dragging = false;
		}
		//And lets move the box around
		if (dragging) {
			dragTarget.x = FlxG.mouse.x + dragOffset.x;
			dragTarget.y = FlxG.mouse.y + dragOffset.y;
		}
	}

	//Called when the user clicks the 'Save Locations' button
	private void onSave() {
		Array<FlxPoint> boxPositions; 
		//Do we already have a save? if not then we need to make one
		if (!gameSave.contains("boxPositions")) {
			//lets make a new array at the location data/
			boxPositions = new Array<FlxPoint>();
			for (FlxBasic a : boxGroup.members) {
				//Cast the boxPositions as an array, you don't have to - but i like my FlashDevelop to highlight so i know im doing it right.
				boxPositions.add(new FlxPoint(((FlxButton)a).x, ((FlxButton)a).y));
			}
			topText.setText("Created a new save, and saved positions");
			topText.setAlpha(1);
		}else {
			//So we already have some save data? lets overwrite the data WITHOUT ASKING! oooh so bad :P
			boxPositions = new Array<FlxPoint>(gameSave.get("boxPositions", FlxPoint[].class));
			//Now we're not doing a real for-loop here, because i REALLY like for each, so we'll need our own index count
			int tempCount = 0;
			//For each button in the group boxGroup - I'm sure you see why I like this already
			for (FlxBasic a : boxGroup.members) {
				boxPositions.set(tempCount, new FlxPoint(((FlxButton)a).x, ((FlxButton)a).y));
				tempCount++;
			}
			topText.setText("Overwrote old positions");
			topText.setAlpha(1);
		}
		boxPositions.shrink();
		gameSave.put("boxPositions", boxPositions.items);
		gameSave.flush();
	}

	//Called when the user clicks the 'Load Locations' button
	private void onLoad() {
		//Loading what? There's no save data!
		FlxPoint[] boxPositions = gameSave.get("boxPositions", FlxPoint[].class);
		if (boxPositions == null){
			topText.setText("Failed to load - There's no save");
			topText.setAlpha(1);
		}else {
			//Note that above I saved the positions as an array of FlxPoints, When the SWF is closed and re-opened the Types in the
			//array lose their type, and for some reason cannot be re-cast as a FlxPoint. They become regular Flash Objects with the correct
			//variables though, so you're safe to use them - just your IDE won't highlight recognize and highlight the variables
			int tempCount = 0;
			for (FlxBasic a : boxGroup.members) {
				((FlxButton)a).x = boxPositions[tempCount].x;
				((FlxButton)a).y = boxPositions[tempCount].y;
				tempCount++;
			}
			topText.setText("Loaded positions");
			topText.setAlpha(1);
		}
	}

	//Called when the user clicks the 'Clear Save' button
	private void onClear() {
		//Lets just wipe the whole boxPositions array
		gameSave.erase();
		topText.setText("Save erased");
		topText.setAlpha(1);
	}
}