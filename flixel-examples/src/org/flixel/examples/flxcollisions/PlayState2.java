package org.flixel.examples.flxcollisions;

import org.flixel.*;
import org.flixel.event.AFlxButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;

public class PlayState2 extends FlxState
{
	private String ImgGibs = "examples/flxcollisions/pack:gibs";
	
	protected FlxText _fps;
	protected FlxSprite _focus;

	public FlxGroup blocks;
	public FlxEmitter dispenser;

	@Override
	public void create()
	{
		//Background
		FlxG.setBgColor(0xffacbcd7);

		//A bunch of blocks
		FlxSprite block;
		blocks = new FlxGroup();
		for(int i = 0; i < 300; i++)
		{
			block = new FlxSprite(FlxU.floor(FlxG.random()*40)*16,FlxU.floor(FlxG.random()*30)*16).makeGraphic(16,16,0xff233e58);
			block.immovable = true;
			block.moves = false;
			block.active = false;
			blocks.add(block);
		}
		add(blocks);

		//Shoot nuts and bolts all over
		dispenser = new FlxEmitter();
		dispenser.gravity = 0;
		dispenser.setSize(640,480);
		dispenser.setXSpeed(-100,100);
		dispenser.setYSpeed(-100,100);
		dispenser.bounce = 0.65f;
		dispenser.makeParticles(ImgGibs,300,16,true,0.8f);
		dispenser.start(false,10,0.05f);
		add(dispenser);

		//Camera tracker
		_focus = new FlxSprite(FlxG.width/2,FlxG.height/2).loadGraphic(ImgGibs,true);
		_focus.setFrame(3);
		_focus.setSolid(false);
		add(_focus);
		FlxG.camera.follow(_focus);
		FlxG.camera.setBounds(0,0,640,480,true);

		if (Gdx.app.getType() == ApplicationType.Android)
		{
			FlxButton nextButton = new FlxButton(0, FlxG.height - 20, "Next", new AFlxButton(){@Override public void onUp(){FlxG.switchState(new PlayState3());}});
			nextButton.scrollFactor.x = nextButton.scrollFactor.y = 0;
			nextButton.setSolid(false);
			add(nextButton);
		}
		else
		{
			//Instructions and stuff
			FlxText tx;
			tx = new FlxText(2,FlxG.height-12,FlxG.width,"Interact with ARROWS + SPACE, or press ENTER for next demo.");
			tx.scrollFactor.x = tx.scrollFactor.y = 0;
			tx.setColor(0xFF49637A);
			add(tx);
		}
	}

	@Override
	public void update()
	{
		//camera controls
		_focus.velocity.x = 0;
		_focus.velocity.y = 0;
		float focusSpeed = 200;
		if(FlxG.keys.LEFT)
			_focus.velocity.x -= focusSpeed;
		if(FlxG.keys.RIGHT)
			_focus.velocity.x += focusSpeed;
		if(FlxG.keys.UP)
			_focus.velocity.y -= focusSpeed;
		if(FlxG.keys.DOWN)
			_focus.velocity.y += focusSpeed;
		
		if (FlxG.mouse.pressed())
		{
			_focus.velocity.x = FlxG.mouse.x - _focus.x;
			_focus.velocity.y = FlxG.mouse.y - _focus.y;
		}

		super.update();
		FlxG.collide();
		if(FlxG.keys.justReleased("ENTER"))
			FlxG.switchState(new PlayState3());
	}
}