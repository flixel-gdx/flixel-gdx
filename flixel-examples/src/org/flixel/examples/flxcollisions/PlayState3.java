package org.flixel.examples.flxcollisions;

import org.flixel.*;
import org.flixel.event.AFlxButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;

public class PlayState3 extends FlxState
{
	private String ImgGibs = "examples/flxcollisions/pack:gibs";
	
	protected FlxText _fps;

	protected FlxSprite _platform;

	@Override
	public void create()
	{
		//Limit collision boundaries to just this screen (since we don't scroll in this one)
		FlxG.worldBounds.make(0,0,FlxG.width,FlxG.height);

		//Background
		FlxG.setBgColor(0xffacbcd7);

		//The thing you can move around
		_platform = new FlxSprite((FlxG.width-64)/2,200).makeGraphic(64,16,0xff233e58);
		_platform.immovable = true;
		add(_platform);

		//Pour nuts and bolts out of the air
		FlxEmitter dispenser = new FlxEmitter((FlxG.width-64)/2,-64);
		dispenser.gravity = 200;
		dispenser.setSize(64,64);
		dispenser.setXSpeed(-20,20);
		dispenser.setYSpeed(50,100);
		dispenser.setRotation(-720,720);
		//dispenser.bounce = 0.1;
		dispenser.makeParticles(ImgGibs,300,16,true,0.5f);
		dispenser.start(false,5,0.025f);
		add(dispenser);

		if (Gdx.app.getType() == ApplicationType.Android)
		{
			FlxButton nextButton = new FlxButton(0, FlxG.height - 20, "Next", new AFlxButton(){@Override public void onUp(){FlxG.switchState(new PlayState());}});
			nextButton.setSolid(false);
			add(nextButton);
		}
		else
		{
			//Instructions
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
		//Platform controls
		float v = 50;
		if(FlxG.keys.SPACE)
			v *= 3;
		_platform.velocity.x = 0;
		if(FlxG.keys.LEFT || (FlxG.mouse.pressed() && FlxG.mouse.x < (FlxG.width / 2)))
			_platform.velocity.x -= v;
		if(FlxG.keys.RIGHT || (FlxG.mouse.pressed() && FlxG.mouse.x > (FlxG.width / 2)))
			_platform.velocity.x += v;

		super.update();
		FlxG.collide();
		if(FlxG.keys.justReleased("ENTER"))
			FlxG.switchState(new PlayState());
	}
}