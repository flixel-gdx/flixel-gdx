package org.example;

import org.flixel.FlxEmitter;
import org.flixel.FlxG;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.flixel.FlxTileblock;
import org.flixel.FlxU;
import org.flixel.data.R;

import android.graphics.Paint.Align;

public class PlayState5 extends FlxState
{

	protected FlxText _fps;

	protected FlxSprite _player;
	protected FlxSprite _elevator;

	@Override
	public void create()
	{
		// Background
		FlxState.bgColor = 0xffacbcd7;
		FlxSprite decoration = new FlxSprite(256, 159, R.drawable.bg);
		decoration.moves = false;
		decoration.setSolid(false);
		add(decoration);
		add(new FlxText(32, 36, 96, "collision").setFormat(null, 16, 0xFF778ea1, Align.CENTER));
		add(new FlxText(32, 60, 96, "DEMO").setFormat(null, 24, 0xFF778ea1, Align.CENTER));

		// Add game objects
		add(new Elevator(208, 80, 112));
		add(new Pusher(96, 208, 56));
		add(new Droid(32, 176));
		add(new Crate(64, 208));
		add(new Crate(108, 176));
		add(new Crate(140, 176));
		add(new Crate(192, 208));
		add(new Crate(272, 48));

		// This is the thing that spews nuts and bolts
		FlxEmitter dispenser = new FlxEmitter(32, 32);
		dispenser.setSize(8, 24);
		dispenser.setXSpeed(100, 400);
		dispenser.setYSpeed(-50, 50);
		dispenser.createSprites(R.drawable.gibs, 120, 16, true, 0.8f);
		dispenser.start(false, 0.035f);
		add(dispenser);

		// Basic level structure
		// var b:BitmapData = new ImgMap();

//		FlxTilemap t = new FlxTilemap();
//		t.auto = FlxTilemap.ALT;
//		t.loadMap(FlxTilemap.pngToCSV(R.drawable.map, false, 1), R.drawable.tiles);
//		t.follow();
//		add(t);
		
		FlxTileblock t;
		
		// TOP
		t = new FlxTileblock(0, 0, 16, 16);
		t.createGraphic(320, 16, 0xFF233e58);
		add(t);
		
		// LEFT
		t = new FlxTileblock(0, 16, 16, 16);
		t.createGraphic(16, 240, 0xFF233e58);
		add(t);
		
		// RIGHT
		t = new FlxTileblock(320-16, 16, 16, 240 - 16);
		t.createGraphic(16, 240, 0xFF233e58);
		add(t);
		
		// BOTTOM
		t = new FlxTileblock(0, 240 - 16, 16, 16);
		t.createGraphic(320, 16, 0xFF233e58);
		add(t);
		
		// WALL EMITTER
		t = new FlxTileblock(16, 240/2-16, 16, 16);
		t.createGraphic(320/2, 16, 0xFF233e58);
		add(t);
		
		t = new FlxTileblock(320/2, 240/2-48, 16, 16);
		t.createGraphic(16, 32, 0xFF233e58);
		add(t);
		
		t = new FlxTileblock(320/2, 16, 16, 16);
		t.createGraphic(16, 32, 0xFF233e58);
		add(t);
		

		// Instructions and stuff
		_fps = new FlxText(FlxG.width - 40, 0, 40).setFormat(null, 8, 0xFF778ea1, Align.RIGHT, 0x233e58);
		_fps.scrollFactor.x = _fps.scrollFactor.y = 0;
		add(_fps);
		
		FlxText tx;
		tx = new FlxText(2, 0, FlxG.width, "flixel android v0.1").setFormat(null, 8, 0xFF778ea1, null, 0xFF233e58);
		tx.scrollFactor.x = tx.scrollFactor.y = 0;
		add(tx);
		
		
		FlxG.createDPad(FlxG.width-140, FlxG.height-140);
		FlxG.dpad.show();
	}

	@Override
	public void update()
	{
		_fps.setText((int)FlxU.floor(1/FlxG.elapsed)+" fps");
		super.update();
		collide();
	}
}
