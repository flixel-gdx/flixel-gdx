package org.example;

import org.flixel.FlxButton;
import org.flixel.FlxButtonListener;
import org.flixel.FlxEmitter;
import org.flixel.FlxG;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.flixel.FlxU;
import org.flixel.data.R;

import android.graphics.Paint.Align;
import android.util.Log;


public class PlayState3 extends FlxState
{
	protected FlxText _fps;

	protected FlxSprite _platform;
	
	protected FlxEmitter _dispenser;

	private FlxButton _btnDispenser;
	private FlxButton _btnBounds;
	
	private boolean _emitterA;

	private FlxText _txtStop;
	private FlxText _txtBounds;
	
	
	@Override
	public void create()
	{
		//FlxG.showBounds = true;
		
		//Background
		FlxState.bgColor = 0xffacbcd7;
		
		//The thing you can move around
		_platform = new FlxSprite((FlxG.width-64)/2,200).createGraphic(64,16,0xff233e58);
		_platform.setFixed(true);
		add(_platform);
		
		//Pour nuts and bolts out of the air
		_dispenser = new FlxEmitter((FlxG.width-64)/2,-64);
		_dispenser.gravity = 200;
		_dispenser.setSize(64,64);
		_dispenser.setXSpeed(-10,10);
		_dispenser.setYSpeed(50,150);
		_dispenser.createSprites(R.drawable.gibs,200,16,true,0.5f);
		//_dispenser.start(false,0.1f);
		add(_dispenser);
		
		FlxText text = new FlxText(10, FlxG.height - 35, 300, "Collision example");
		text.setFormat(null, 20, 0xFF06F4FF, Align.LEFT, 0xFF000000);		
		add(text);
		
		_emitterA = true;		
		_txtStop = new FlxText(0, 30, 70, "Stop").setFormat(null, 16, 0xFF49637A, Align.CENTER);
		_btnDispenser = new FlxButton(10, 20, btnHandler, 70, 70);
		_btnDispenser.loadText(_txtStop);
		_btnDispenser.setTouchMove(false);
		add(_btnDispenser);
		
		_txtBounds = new FlxText(0, 30, 70, "Bounds").setFormat(null, 16, 0xFF49637A, Align.CENTER);
		_btnBounds = new FlxButton(10, 200, boundsHandler, 70, 70);
		_btnBounds.loadText(_txtBounds);
		add(_btnBounds);
		
		FlxG.createDPad(FlxG.width-276, FlxG.height-276);
		FlxG.dpad.show();
		
		//Instructions and stuff
		_fps = new FlxText(FlxG.width-100,0,100).setFormat(null, 16, 0xFF49637A, Align.RIGHT);
		_fps.scrollFactor.x = _fps.scrollFactor.y = 0;
		add(_fps);
		
		FlxG.play(R.raw.mode);
	}
	
	
	@Override
	public void update()
	{
		_fps.setText((int)FlxU.floor(1/FlxG.elapsed)+" fps");
		
		int v = 100;
		_platform.velocity.x = 0;
		_platform.velocity.y = 0;
		if(FlxG.keys.justReleased("LEFT"))
			_platform.velocity.x -= v;
		if(FlxG.keys.justReleased("RIGHT"))
			_platform.velocity.x += v;
		if(FlxG.keys.justReleased("DOWN"))
			_platform.velocity.y += v;
		if(FlxG.keys.justReleased("UP"))
			_platform.velocity.y -= v;
		
		if(FlxG.dpad != null)
		{			
			if(FlxG.dpad.pressed("RIGHT"))
				_platform.velocity.x += v;
			if(FlxG.dpad.pressed("LEFT"))
			{			
				_platform.velocity.x -= v;
			}
			if(FlxG.dpad.justTouched("LEFT"))
			{
				Log.i("just pressed", "left button");
			}
			if(FlxG.dpad.justRemoved("LEFT"))
			{
				Log.i("Removed", "left button");
			}
			if(FlxG.dpad.pressed("DOWN"))
			{
				_platform.velocity.y += v;
			}
			if(FlxG.dpad.pressed("UP"))
			{
				_platform.velocity.y -= v;
			}
		}
		
		super.update();
		collide();
	}
	
	
	FlxButtonListener btnHandler = new FlxButtonListener()
	{		
		@Override
		public boolean onTouchUp()
		{		
			//_btn.destroy();
			if(_emitterA)
			{
				_emitterA = false;
				_dispenser.stop();
			}
			else
			{
				_emitterA = true;
				_dispenser.start(false,0.1f);				
			}			
			return false;
		}
	};
	
	FlxButtonListener boundsHandler = new FlxButtonListener()
	{		
		@Override
		public boolean onTouchUp()
		{			
			FlxG.showBounds = !FlxG.showBounds;
			return false;
		}
	};
	
}
