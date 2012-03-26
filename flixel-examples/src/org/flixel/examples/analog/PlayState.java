package org.flixel.examples.analog;

import org.flixel.FlxAnalog;
import org.flixel.FlxG;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.data.SystemAsset;

public class PlayState extends FlxState
{
	private FlxSprite _player;
	private FlxAnalog _analog;
	
	@Override
	public void create()
	{
		FlxG.setBgColor(0xFFFAAAF);

		_player = new FlxSprite(10, 10, SystemAsset.ImgDefault);
		add(_player);
		
		// Add some wall around the edges.
		FlxSprite s;
		add(s = new FlxSprite(0, 0).makeGraphic(FlxG.width, 2));
		s.immovable = true;
		add(s = new FlxSprite(0, FlxG.height-2).makeGraphic(FlxG.width, 2));
		s.immovable = true;
		add(s = new FlxSprite(0, 0).makeGraphic(2, FlxG.height));
		s.immovable = true;
		add(s = new FlxSprite(FlxG.width-2, 0).makeGraphic(2, FlxG.height));
		s.immovable = true;
		
		add(_analog = new FlxAnalog(0,0));
	}
	
	
	@Override
	public void update()
	{
		if(_analog.pressed)
		{
			_player.x = _player.x + (_analog.accel.x);
			_player.y = _player.y + (_analog.accel.y);
		}
		FlxG.collide();
		super.update();
	}

}
