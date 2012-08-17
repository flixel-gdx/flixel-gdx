package org.flixel.examples.flxcollisions;

import org.flixel.*;
import org.flixel.event.AFlxButton;

import com.badlogic.gdx.utils.Array;

public class PlayState extends FlxState
{	
	private String ImgMap = "examples/flxcollisions/pack:map";
	private String ImgTiles = "examples/flxcollisions/pack:tiles";
	private String ImgBG = "examples/flxcollisions/pack:bg";
	private String ImgGibs = "examples/flxcollisions/pack:gibs";

	private String ImgPusher = "examples/flxcollisions/pack:pusher";
	private String ImgElevator = "examples/flxcollisions/pack:elevator";
	private String ImgCrate = "examples/flxcollisions/pack:crate";
	
	protected FlxTilemap _level;
	protected Player _player;

	@Override
	public void create()
	{		
		//Background
		FlxG.setBgColor(0xffacbcd7);
		FlxSprite decoration = new FlxSprite(256,159,ImgBG);
		decoration.moves = false;
		decoration.setSolid(false);
		add(decoration);
		add(new FlxText(32,36,96,"collision").setFormat(null,16,0xFF778ea1,"center"));
		add(new FlxText(32,60,96,"DEMO").setFormat(null,24,0xFF778ea1,"center"));
		
		FlxPath path;
		FlxSprite sprite;
		FlxPoint destination;

		//Create the elevator and put it on a up and down path
		sprite = new FlxSprite(208,80,ImgElevator);
		sprite.immovable = true;
		destination = sprite.getMidpoint();
		destination.y += 112;
		path = new FlxPath(new Array<FlxPoint>(new FlxPoint[]{sprite.getMidpoint(),destination}));
		sprite.followPath(path,40,FlxObject.PATH_YOYO);
		add(sprite);

		//Create the side-to-side pusher object and put it on a different path
		sprite = new FlxSprite(96,208,ImgPusher);
		sprite.immovable = true;
		destination = sprite.getMidpoint();
		destination.x += 56;
		path = new FlxPath(new Array<FlxPoint>(new FlxPoint[]{sprite.getMidpoint(),destination}));
		sprite.followPath(path,40,FlxObject.PATH_YOYO);
		add(sprite);

		//Then add the player, its own class with its own logic
		_player = new Player(32,176);
		add(_player);

		//Then create the crates that are sprinkled around the level
		FlxPoint[] crates = {new FlxPoint(64,208),
							new FlxPoint(108,176),
							new FlxPoint(140,176),
							new FlxPoint(192,208),
							new FlxPoint(272,48)};
		for(int i = 0; i < crates.length; i++)
		{
			sprite = new FlxSprite(crates[i].x,crates[i].y,ImgCrate);
			sprite.height = sprite.height-1;
			sprite.acceleration.y = 400;
			sprite.drag.x = 200;
			add(sprite);
		}

		//This is the thing that spews nuts and bolts
		FlxEmitter dispenser = new FlxEmitter(32,40);
		dispenser.setSize(8,40);
		dispenser.setXSpeed(100,240);
		dispenser.setYSpeed(-50,50);
		dispenser.gravity = 300;
		dispenser.bounce = 0.3f;
		dispenser.makeParticles(ImgGibs,100,16,true, 0.8f);
		dispenser.start(false,10,0.035f);
		add(dispenser);
		
		//Basic level structure
		_level = new FlxTilemap();
		_level.loadMap(FlxTilemap.imageToCSV(ImgMap,false,2),ImgTiles,0,0,FlxTilemap.ALT);
		_level.follow();
		add(_level);

		//Library label in upper left
		FlxText tx;
		tx = new FlxText(2,0,FlxG.width/3,FlxG.getLibraryName());
		tx.scrollFactor.x = tx.scrollFactor.y = 0;
		tx.setColor(0x778ea1);
		tx.setShadow(0x233e58);
		add(tx);

		if (FlxG.mobile)
		{
			FlxButton nextButton = new FlxButton(0, FlxG.height - 20, "Next", new AFlxButton(){@Override public void callback(){FlxG.switchState(new PlayState2());}});
			nextButton.setSolid(false);
			add(nextButton);
		}
		else
		{
			//Instructions
			tx = new FlxText(2,FlxG.height-12,FlxG.width,"Interact with ARROWS + SPACE, or press ENTER for next demo.");
			tx.scrollFactor.x = tx.scrollFactor.y = 0;
			tx.setColor(0x778ea1);
			tx.setShadow(0x233e58);
			add(tx);
		}
	}
	
	@Override
	public void destroy()
	{
		super.destroy();
		_level = null;
		_player = null;
	}

	@Override
	public void update()
	{
		super.update();
		FlxG.collide();
		if(FlxG.keys.justReleased("ENTER"))
			FlxG.switchState(new PlayState2());
	}
}