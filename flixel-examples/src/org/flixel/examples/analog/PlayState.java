package org.flixel.examples.analog;

import org.flixel.FlxAnalog;
import org.flixel.FlxG;
import org.flixel.FlxSprite;
import org.flixel.FlxState;

public class PlayState extends FlxState
{
	private FlxSprite _player;
	private FlxAnalog _analog;
	final float radiansToDegrees = (float) (180/Math.PI);
	
	
	@Override
	public void create()
	{
		FlxG.setBgColor(0xFF131C1B);
		_player = new FlxSprite(10, 10);
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
		
		add(_analog = new FlxAnalog(10,FlxG.height-110));
	}
	
	
	@Override
	public void update()
	{
		super.update();
		_player.velocity.x = _player.velocity.y = 0;
		//if(_analog.pressed)
		{
			_player.velocity.x = 40*(_analog.accel.x);
			_player.velocity.y = 40*(_analog.accel.y);
			playerRotate();
		}
		FlxG.collide();
	}


	private void playerRotate()
	{		
		float DiffX = 0 + _player.x;
		float DiffY = 0 - _player.y;
		float angle = (float) Math.atan2((_analog.accel.y - DiffY)-_player.y,(_analog.accel.x+DiffX)-_player.x);
		angle *= radiansToDegrees;
		
		if(_analog.pressed)
			_player.angle = angle;
	}

}
