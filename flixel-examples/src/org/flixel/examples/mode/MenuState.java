package org.flixel.examples.mode;

import org.flixel.*;
import org.flixel.event.AFlxButton;
import org.flixel.event.AFlxCamera;
import org.flixel.event.AFlxReplay;

public class MenuState extends FlxState
{
	//Some graphics and sounds
	protected String ImgEnemy = "examples/mode/pack:bot";
	public String ImgGibs = "examples/mode/pack:spawner_gibs";
	public String ImgCursor = "examples/mode/pack:cursor";
	public String SndHit = "examples/mode/menu_hit.mp3";
	public String SndHit2 = "examples/mode/menu_hit_2.mp3";

	//Replay data for the "Attract Mode" gameplay demos
	public String Attract1 = "examples/mode/attract1.fgr";
	public String Attract2 = "examples/mode/attract2.fgr";
	
	public FlxEmitter gibs;
	public FlxButton playButton;
	public FlxText title1;
	public FlxText title2;
	public boolean fading;
	public float timer;
	public boolean attractMode;

	@Override
	public void create()
	{
		FlxG.width = (int) (FlxG.getStage().stageWidth / FlxCamera.defaultZoom);
		FlxG.resetCameras();
		
		FlxG.setBgColor(0xff131c1b);

		//Simple use of flixel save game object.
		//Tracks number of times the game has been played.
		FlxSave save = new FlxSave();
		if(save.bind("Mode"))
		{
			if(!save.contains("plays"))
				save.put("plays", 0);
			else
				save.put("plays", save.get("plays", Integer.class) + 1);
			FlxG.log("Number of plays: "+save.get("plays", Integer.class));
			//save.erase();
			save.close();
		}

		//All the bits that blow up when the text smooshes together
		gibs = new FlxEmitter(FlxG.width/2-50,FlxG.height/2-10);
		gibs.setSize(100,30);
		gibs.setYSpeed(-200,-20);
		gibs.setRotation(-720,720);
		gibs.gravity = 100;
		gibs.makeParticles(ImgGibs,650,32,true,0);
		add(gibs);

		//the letters "mo"
		title1 = new FlxText(FlxG.width + 16,FlxG.height/3,64,"mo");
		title1.setSize(32);
		title1.setColor(0x3a5c39);
		title1.antialiasing = true;
		title1.velocity.x = -FlxG.width;
		add(title1);

		//the letters "de"
		title2 = new FlxText(-60,title1.y,title1.width,"de");
		title2.setSize(title1.getSize());
		title2.setColor(title1.getColor());
		title2.antialiasing = title1.antialiasing;
		title2.velocity.x = FlxG.width;
		add(title2);

		fading = false;
		timer = 0;
		attractMode = false;
			
		FlxG.mouse.show(ImgCursor,2);
	}

	@Override
	public void destroy()
	{
		super.destroy();
		gibs = null;
		playButton = null;
		title1 = null;
		title2 = null;
	}

	@Override
	public void update()
	{			
		super.update();

		if(title2.x > title1.x + title1.width - 4)
		{
			//Once mo and de cross each other, fix their positions
			title2.x = title1.x + title1.width - 4;
			title1.velocity.x = 0;
			title2.velocity.x = 0;

			//Then, play a cool sound, change their color, and blow up pieces everywhere
			FlxG.play(SndHit);
			FlxG.flash(0xffd8eba2,0.5f);
			FlxG.shake(0.035f,0.5f);
			title1.setColor(0xd8eba2);
			title2.setColor(0xd8eba2);
			gibs.start(true,5);
			title1.angle = FlxG.random()*30-15;
			title2.angle = FlxG.random()*30-15;

			//Then we're going to add the text and buttons and things that appear
			//If we were hip we'd use our own button animations, but we'll just recolor
			//the stock ones for now instead.
			FlxText text;
			text = new FlxText(FlxG.width/2-50,FlxG.height/3+39,100,"by Adam Atomic");
			text.setAlignment("center");
			text.setColor(0x3a5c39);
			add(text);

			FlxButton flixelButton = new FlxButton(FlxG.width/2-40,FlxG.height/3+54,"flixel.org",new AFlxButton(){@Override public void onUp(){onFlixel();}});
			flixelButton.setColor(0xff729954);
			flixelButton.label.setColor(0xffd8eba2);
			add(flixelButton);

			FlxButton dannyButton = new FlxButton(flixelButton.x,flixelButton.y + 22,"music: dannyB",new AFlxButton(){@Override public void onUp(){onDanny();}});
			dannyButton.setColor(flixelButton.getColor());
			dannyButton.label.setColor(flixelButton.label.getColor());
			add(dannyButton);

			text = new FlxText(FlxG.width/2-40,FlxG.height/3+139,80,"X+C TO PLAY");
			text.setColor(0x729954);
			text.setAlignment("center");
			add(text);

			playButton = new FlxButton(flixelButton.x,flixelButton.y + 82,"CLICK HERE",new AFlxButton(){@Override public void onUp(){onPlay();}});
			playButton.setColor(flixelButton.getColor());
			playButton.label.setColor(flixelButton.label.getColor());
			add(playButton);
		}

		//X + C were pressed, fade out and change to play state.
		//OR, if we sat on the menu too long, launch the attract mode instead!
		timer += FlxG.elapsed;
		if(timer >= 10) //go into demo mode if no buttons are pressed for 10 seconds
			attractMode = true;
		if(!fading && ((FlxG.keys.X && FlxG.keys.C) || attractMode)) 
		{
			fading = true;
			FlxG.play(SndHit2);
			FlxG.flash(0xffd8eba2,0.5f);
			FlxG.fade(0xff131c1b,1,new AFlxCamera(){@Override public void onFadeComplete(){onFade();}});
		}
	}

	//These are all "event handlers", or "callbacks".
	//These first three are just called when the
	//corresponding buttons are pressed with the mouse.
	protected void onFlixel()
	{
		FlxU.openURL("http://flixel.org");
	}

	protected void onDanny()
	{
		FlxU.openURL("http://dbsoundworks.com");
	}

	protected void onPlay()
	{
		playButton.exists = false;
		FlxG.play(SndHit2);
	}

	//This function is passed to FlxG.fade() when we are ready to go to the next game state.
	//When FlxG.fade() finishes, it will call this, which in turn will either load
	//up a game demo/replay, or let the player start playing, depending on user input.
	protected void onFade()
	{
		if(attractMode)
			FlxG.loadReplay((FlxG.random()<0.5)?FlxG.loadString(Attract1):FlxG.loadString(Attract2),new PlayState(),new String[]{"ANY"},22,new AFlxReplay(){@Override public void onComplete(){onDemoComplete();}});
		else
			FlxG.switchState(new PlayState());
	}

	//This function is called by FlxG.loadReplay() when the replay finishes.
	//Here, we initiate another fade effect.
	protected void onDemoComplete()
	{
		FlxG.fade(0xff131c1b,1,new AFlxCamera(){@Override public void onFadeComplete(){onDemoFaded();}});
	}

	//Finally, we have another function called by FlxG.fade(), this time
	//in relation to the callback above.  It stops the replay, and resets the game
	//once the gameplay demo has faded out.
	protected void onDemoFaded()
	{
		FlxG.stopReplay();
		FlxG.resetGame();
	}
}