package org.example;

import org.flixel.FlxEmitter;
import org.flixel.FlxG;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.flixel.FlxU;

import android.graphics.Paint.Align;

public class PlayState2 extends FlxState
{
	private final float _blur = 0.35f;
	private FlxSprite _helper;
	private FlxText _fps;

	@Override
	public void create()
	{
		// Title text, nothing crazy here!
		FlxText t = new FlxText(0, FlxG.height / 2 - 20, FlxG.width, "FlxBlur");
		t.setSize(32);
		t.setAlignment(Align.CENTER);
		add(t);

		// This is a big black sprite set to _blur% transparency.
		// We'll use this instead of the fill command to create the
		// motion blur effect in the pre-process hook below.
		_helper = new FlxSprite();
		_helper.createGraphic(FlxG.width, FlxG.height, 0xff000000, true);
		_helper.setAlpha(_blur);

		// This is the particle emitter that spews things off the bottom of the
		// screen.
		// I'm not going to go over it in too much detail here, but basically we
		// create the emitter, then we create 50 16x16 sprites and add them to
		// it.
		FlxEmitter e = new FlxEmitter();
		e.width = FlxG.width;
		e.y = FlxG.height + 8;
		e.delay = 0.16f;
		e.gravity = -40;
		e.setXSpeed();
		e.setYSpeed(-50, 0);
		FlxSprite s;
		int particles = 25;
		for(int i = 0; i < particles; i++)
		{
			s = new FlxSprite();
			s.createGraphic(24, 24);
			s.exists = false;
			e.add(s);
		}
		e.start(false);
		
		//Instructions and stuff
		_fps = new FlxText(FlxG.width-100,0,100).setFormat(null, 16, 0xFF49637A, Align.RIGHT);
		_fps.scrollFactor.x = _fps.scrollFactor.y = 0;
		add(_fps);
		
		add(e);
	}
	
	//This pre-processing hook allows us to manipulate the game screen
	// before it is cleared or rendered onto again.
	@Override
	public void preProcess()
	{
		screen.draw(_helper);
	}
	
	@Override
	public void update()
	{
		_fps.setText((int)FlxU.floor(1/FlxG.elapsed)+" fps");
		super.update();
	}
}
