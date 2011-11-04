package org.examples.collision;

import org.flixel.FlxButton;
import org.flixel.FlxEmitter;
import org.flixel.FlxG;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.event.AFlxButton;

import com.badlogic.gdx.Input.Keys;

/**
 * This demo is from flixel's FlxCollisions created by Adam Atomic.
 */
public class PlayState extends FlxState
{	
	private FlxSprite _platform;
	private FlxEmitter _dispenser;
	private FlxButton _btnSwitch;

	@Override
	public void create()
	{
		//Limit collision boundaries to just this screen (since we don't scroll in this one)
		FlxG.worldBounds.make(0,0,FlxG.width,FlxG.height);
		
		//Background
		FlxG.setBgColor(0xffacbcd7);
		
		//The thing you can move around
		_platform = new FlxSprite((FlxG.width-64)/2,200);
//		_platform.makeGraphic(64,16,0xff233e58);
		_platform.loadGraphic(Asset.platform);
		_platform.immovable = true;
		add(_platform);
		
		//Pour nuts and bolts out of the air
		_dispenser = new FlxEmitter((FlxG.width - 64) / 2 , -64, 300);
		_dispenser.gravity = 200;
		_dispenser.setSize(64,64);
		_dispenser.setXSpeed(-20,20);
		_dispenser.setYSpeed(50,100);
		_dispenser.setRotation(-720,720);
		_dispenser.makeParticles(Asset.gibs,300,16,true, 0.5f);
		_dispenser.start(false,5,0.025f);
		add(_dispenser);
		
		// A button to toggle the dispenser.
		_btnSwitch = new FlxButton(5, 5, "ON", toggleDispenser);
		_btnSwitch.setOn(true);
		add(_btnSwitch);
	}
	
	
	@Override
	public void update()
	{
		//Platform controls
		float v = 50;
		if(FlxG.keys.pressed(Keys.SPACE))
			v *= 3;
		_platform.velocity.x = 0;
		if(FlxG.keys.justReleased(Keys.DPAD_LEFT) || FlxG.keys.pressed(Keys.DPAD_LEFT))
			_platform.velocity.x -= v;
		if(FlxG.keys.justReleased(Keys.DPAD_RIGHT) || FlxG.keys.pressed(Keys.DPAD_RIGHT))
			_platform.velocity.x += v;			
		super.update();
			FlxG.collide();
	}
	
	AFlxButton toggleDispenser = new AFlxButton()
	{
		@Override
		public void onUp()
		{
			if(_btnSwitch.getOn())
			{
				_btnSwitch.label.setText("OFF");
				_btnSwitch.setOn(false);
				_dispenser.on = false;
			}
			else
			{
				_btnSwitch.label.setText("ON");
				_btnSwitch.setOn(true);		
				_dispenser.on = true;
			}				
		}
	};
}
