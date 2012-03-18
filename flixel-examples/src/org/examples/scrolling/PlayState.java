package org.examples.scrolling;

import org.flixel.FlxEmitter;
import org.flixel.FlxG;
import org.flixel.FlxGroup;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.flixel.FlxU;


/**
 * A demo that let you scroll through the level.
 * This demo is from flixel's FlxCollision created by Adam Atomic.
 */
public class PlayState extends FlxState
{
	protected FlxSprite _focus;

	public FlxGroup blocks;
	public FlxEmitter dispenser;


	@Override
	public void create()
	{
		// Background
		FlxG.setBgColor(0xffacbcd7);

		// A bunch of blocks
		FlxSprite block;
		blocks = new FlxGroup(300);
		for (int i = 0; i < 300; i++)
		{
			block = new FlxSprite((int)	FlxU.floor((float) (FlxG.random() * 40)) * 16,
								  (int) FlxU.floor((float) (FlxG.random() * 30)) * 16).loadGraphic(Asset.ImgBlock);
			block.immovable = true;
			block.moves = false;
			block.active = false;
			blocks.add(block);
		}
		add(blocks);

		// Shoot nuts and bolts all over
		dispenser = new FlxEmitter(0, 0, 300);
		dispenser.gravity = 0;
		dispenser.setSize(640, 480);
		dispenser.setXSpeed(-100, 100);
		dispenser.setYSpeed(-100, 100);
		dispenser.bounce = 0.65f;
		dispenser.makeParticles(Asset.ImgGibs, 300, 16, true, 0.8f);
		dispenser.start(false, 10, 0.05f);
		add(dispenser);

		// Camera tracker
		_focus = new FlxSprite(FlxG.width / 2, FlxG.height / 2).loadGraphic(Asset.ImgGibs, true);
		_focus.setFrame(3);
		_focus.setSolid(false);
		add(_focus);
		FlxG.camera.follow(_focus);
		FlxG.camera.setBounds(0, 0, 640, 480, true);

		// Instructions and stuff
		FlxText tx;
		tx = new FlxText(2, FlxG.height - 20, FlxG.width, "Interact with TOUCH.");
		tx.scrollFactor.x = tx.scrollFactor.y = 0;
//		tx.setColor(0xFF49637a);
		add(tx);
	}

	
	@Override
	public void update()
	{
		// camera controls
		if(FlxG.mouse.pressed())
		{
//			FlxG.mouse.getWorldPosition(FlxG.camera);
//			FlxG.log(FlxG.difWidth + " : " + FlxG.difHeight);
//			FlxG.log(FlxG.mouse.x + " : " + FlxG.mouse.y);
			//FlxG.log(FlxG.mouse._pointers.get(0).x + " : " + FlxG.mouse._pointers.get(0).y);
			_focus.x += (FlxG.mouse.x -_focus.x)/20f;
			_focus.y += (FlxG.mouse.y -_focus.y)/20f;
		}
		//if(FlxG.mouse.justPressed())
		{
//			FlxG.log(FlxG.mouse._pointers.get(0).x + " : " + FlxG.mouse._pointers.get(0).y);
			
		}
		
//		_focus.velocity.x = 0;
//		_focus.velocity.y = 0;
//		float focusSpeed = 200;
//		if(FlxG.keys.pressed(Keys.LEFT))
//			_focus.velocity.x -= focusSpeed;
//		if(FlxG.keys.pressed(Keys.RIGHT))
//			_focus.velocity.x += focusSpeed;
//		if(FlxG.keys.pressed(Keys.UP))
//			_focus.velocity.y -= focusSpeed;
//		if(FlxG.keys.pressed(Keys.DOWN))
//			_focus.velocity.y += focusSpeed;
		
		super.update();
		FlxG.collide();
	}
}
