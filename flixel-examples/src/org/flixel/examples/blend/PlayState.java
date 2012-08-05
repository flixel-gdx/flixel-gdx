package org.flixel.examples.blend;

import org.flixel.FlxG;
import org.flixel.FlxSprite;
import org.flixel.FlxState;

import flash.display.BlendMode;

/**
 * A simply demo to test the various blending modes.
 * Graphics from www.interfacelift.com
 * 
 * @author Ka Wing Chin
 */
public class PlayState extends FlxState
{
	private String ImgBg = "examples/blend/blend:flamingobg";
	private String ImgFlamingo = "examples/blend/blend:flamingos";

	private FlxSprite _flamingo;
	
	private int[][] _blends;
	private int _count;
	
	@Override
	public void create()
	{			
		add(new FlxSprite().loadGraphic(ImgBg));
		add(_flamingo = new FlxSprite().loadGraphic(ImgFlamingo));
		_count = 0;
		_blends = new int[][]{BlendMode.NORMAL, BlendMode.LINEAR_DODGE, BlendMode.SCREEN, BlendMode.MULTIPLY};
	}

	@Override
	public void update()
	{
		if(FlxG.mouse.justPressed())
		{
			if(_blends.length == ++_count)
				_count = 0;
			_flamingo.blend = _blends[_count];	
		}
		super.update();
	}
}
