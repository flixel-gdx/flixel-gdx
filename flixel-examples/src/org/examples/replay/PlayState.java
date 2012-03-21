package org.examples.replay;

import org.flixel.*;
import org.flixel.event.AFlxReplay;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayState extends FlxState
{

	//===========embed resources===========
	private TextureRegion img_autoChange = Asset.ImgTiles;

	private String map_simple = Asset.MapData;

	//===========declare UI stuff===========
	private FlxText hintText;

	//===========declare others===========
	private FlxTilemap simpleTilemap;

	/**
	 * the blue block player controls
	 */
	private FlxSprite thePlayer;

	/**
	 * the red block represents mouse
	 */
	private FlxSprite theCursor;

	//===========declare stuff around replay===========
	/*
	 * We use these to tell which mode we are at, recording or replaying
	 */
	private static boolean recording =false;
	private static boolean replaying =false;


	@Override 
	public void create()
	{
		FlxG.setFramerate(60);
		FlxG.setFlashFramerate(60);
		FlxG.mouse.hide();

		//Set up the TILEMAP
		simpleTilemap = new FlxTilemap();
		simpleTilemap.loadMap(map_simple,img_autoChange,25,25,FlxTilemap.AUTO);
		add(simpleTilemap);
		simpleTilemap.y -= 15;

		//Set up the cursor
		theCursor = new FlxSprite().makeGraphic(6, 6, 0xFFFF0000);
		add(theCursor);

		//Set up the Player
		thePlayer = new FlxSprite().makeGraphic(12, 12, 0xFF8CF1FF);
		thePlayer.maxVelocity.x = 80;   // Theses are pysics settings,
		thePlayer.maxVelocity.y = 200;  // controling how the players behave
		thePlayer.acceleration.y = 300; // in the game
		thePlayer.drag.x = thePlayer.maxVelocity.x * 4;
		thePlayer.x = 30;
		thePlayer.y = 200;
		add(thePlayer);

		//Set up UI
		hintText =  new FlxText(0, 268, 400);
		hintText.setColor(0xFF000000);
		//hintText.setSize(12);
		add(hintText);

		//adjust things according to different modes
		init();
	}

	@Override
	public void update()
	{
		FlxG.collide(simpleTilemap, thePlayer);

		//Update the player
		thePlayer.acceleration.x = 0;
		if(FlxG.keys.LEFT)
		{
			thePlayer.acceleration.x -= thePlayer.drag.x;
		}
		else if(FlxG.keys.RIGHT)
		{
			thePlayer.acceleration.x +=thePlayer.drag.x;
		}
		if(FlxG.keys.justPressed("X") && thePlayer.velocity.y == 0)
		{
			thePlayer.velocity.y = -200;
		}

		if(!PlayState.recording && !PlayState.replaying)
		{
			start_record();
		}

		/**
		 * Notice that I add "&&recording", because recording will recording every input
		 * so R key for replay will also be recorded and
		 * be triggered at replaying
		 * Please pay attention to the inputs that are not supposed to be recorded
		 */
		if (FlxG.keys.justPressed("R") && PlayState.recording)
		{
			start_play();
		}

		super.update();

		//Update the red block cursor
		theCursor.scale = new FlxPoint(1, 1);
		if (FlxG.mouse.pressed()) 
		{
			theCursor.scale = new FlxPoint(2, 2);
		}
		theCursor.x = FlxG.mouse.screenX;
		theCursor.y = FlxG.mouse.screenY;
	}

	/**
	 * I use this function to do the init differs from recording to replaying
	 */
	private void init()
	{
		if (PlayState.recording)
		{
			thePlayer.setAlpha(1);
			theCursor.setAlpha(1);
			hintText.setText("Recording: Arrow Keys : move, X : jump, R : replay\nMouse move and click will also be recorded");
		}
		else if (PlayState.replaying)
		{
			thePlayer.setAlpha(0.5f);
			theCursor.setAlpha(0.5f);
			hintText.setText("Replaying: Press any key or mouse button to stop and record again");
		}
	}

	private void start_record()
	{
		PlayState.recording = true;
		PlayState.replaying = false;

		/*
		 *Note FlxG.recordReplay will restart the game or state
		 *This function will trigger a flag in FlxGame
		 *and let the internal FlxReplay to record input on every frame
		 */
		FlxG.recordReplay(false);
	}

	private void start_play()
	{
		PlayState.replaying = true;
		PlayState.recording = false;

		/*
		 * Here we get a string from stopRecoding()
		 * which records all the input during recording
		 * Then we load the save
		 */

		String save = FlxG.stopRecording();
		//trace(save);

		/**
		 * NOTE "ANY" or other key wont work under debug mode!
		 */
		FlxG.loadReplay(save, new PlayState(), new String[]{"ANY", "MOUSE"}, 0, new AFlxReplay(){@Override public void onFinished(){start_record();}});

	}
}