package org.flixel.examples.bloom;

import org.flixel.FlxEmitter;
import org.flixel.FlxG;
import org.flixel.FlxParticle;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.FlxText;

import flash.display.BlendMode;

public class PlayState extends FlxState
{
	public boolean toggle;

	protected final int _bloom = 6;	//How much light bloom to have - larger numbers = more
	protected FlxSprite _fx;		//Our helper sprite - basically a mini screen buffer (see below)
	
	@Override
	public void create()
	{
		//Title text, nothing crazy here!
		FlxText text;
		text = new FlxText(FlxG.width/4,FlxG.height/2-20,FlxG.width/2,"FlxBloom",true);
		text.setFormat(null,32,FlxG.WHITE,"center");
		add(text);
		text = new FlxText(FlxG.width/4,FlxG.height/2+20,FlxG.width/2,"press space to toggle",true);
		text.setFormat(null,16,FlxG.BLUE,"center");
		add(text);
		
		//This is the sprite we're going to use to help with the light bloom effect
		//First, we're going to initialize it to be a fraction of the screens size
		_fx = new FlxSprite();
		_fx.makeGraphic(FlxG.width/_bloom,FlxG.height/_bloom,0,true);
		_fx.setOriginToCorner();	//Zero out the origin so scaling goes from top-left, not from center
		_fx.scale.x = _bloom;		//Scale it up to be the same size as the screen again
		_fx.scale.y = _bloom;		//Scale it up to be the same size as the screen again
		_fx.antialiasing = true;	//Set AA to true for maximum blurry
		_fx.blend = BlendMode.SCREEN;		//Set blend mode to "screen" to make the blurred copy transparent and brightening
		//Note that we do not add it to the game state!  It's just a helper, not a "real" sprite.
		
		//Then we scale the screen buffer down, so it draws a smaller version of itself
		// into our tiny FX buffer, which is already scaled up.  The net result of this operation
		// is a blurry image that we can render back over the screen buffer to create the bloom.
		//FlxG.camera.screen.scale.x = 1/_bloom; // TODO: screen.
		//FlxG.camera.screen.scale.y = 1/_bloom; // TODO: screen.
		
		//This is the particle emitter that spews things off the bottom of the screen.
		//I'm not going to go over it in too much detail here, but basically we
		// create the emitter, then we create 50 16x16 sprites and add them to it.
		int particles = 50;
		FlxEmitter emitter = new FlxEmitter(0,FlxG.height+8,particles);
		emitter.width = FlxG.width;
		emitter.y = FlxG.height+20;
		emitter.gravity = -40;
		emitter.setXSpeed(-20,20);
		emitter.setYSpeed(-75,-25);
		FlxParticle particle;
		int[] colors = new int[]{FlxG.BLUE, (FlxG.BLUE | FlxG.GREEN), FlxG.GREEN, (FlxG.GREEN | FlxG.RED), FlxG.RED};
		for(int i = 0; i < particles; i++)
		{
			particle = new FlxParticle();
			particle.makeGraphic(32,32,colors[(int) (FlxG.random()*colors.length)]);
			particle.exists = false;
			emitter.add(particle);
		}
		emitter.start(false,0,0.1f);
		add(emitter);
		
		//Allows users to toggle the effect on and off with the space bar. Effect starts on.
		toggle = true;
	}
	
	@Override
	public void update()
	{
		if(FlxG.keys.justPressed("SPACE"))
			toggle = !toggle;
		
		super.update();
	}
	
	//This is where we do the actual drawing logic for the game state
	@Override
	public void draw()
	{
		//This draws all the game objects
		super.draw();
		
		if(toggle)
		{
			//The actual blur process is quite simple now.
			//First we draw the contents of the screen onto the tiny FX buffer:
			//_fx.stamp(FlxG.camera.screen); // TODO: screen.
			//Then we draw the scaled-up contents of the FX buffer back onto the screen:
			_fx.draw();
		}
	}
}
