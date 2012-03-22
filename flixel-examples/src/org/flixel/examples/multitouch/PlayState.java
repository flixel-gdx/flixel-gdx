package org.flixel.examples.multitouch;

import org.flixel.FlxG;
import org.flixel.FlxGroup;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.FlxText;

/**
 * A simple demo to test your multi-touch support.
 * 
 * @author Ka Wing Chin
 */
public class PlayState extends FlxState
{
	private FlxText txtCounter;
	private FlxGroup sprites;
	private final int pointersToCheck = 10;

	@Override
	public void create()
	{
		FlxG.setBgColor(0xFF000000);

		txtCounter = new FlxText(20, 20, 200, "amount of fingers on screen: 0");
		add(txtCounter);
		
		sprites = new FlxGroup();
		
		FlxSprite square;
		for(int i = 0; i < pointersToCheck; ++i)
		{
			square = (FlxSprite) sprites.add(new FlxSprite().makeGraphic(80, 80));
			square.offset.x = square.width / 2;
			square.offset.y = square.height / 2;
			square.visible = false;
			sprites.add(square);
		}
		
		add(sprites);
	}
	
	@Override
	public void update()
	{
		int pointersDown = 0;
		
		for(int i = 0; i < pointersToCheck; i++)
		{	
			FlxSprite square = (FlxSprite) sprites.members.get(i);		
			if(FlxG.mouse.pressed(i))
			{
				square.x = FlxG.mouse.getWorldPosition(i).x;
				square.y = FlxG.mouse.getWorldPosition(i).y;
				square.visible = true;
				++pointersDown;
			}
			else
			{
				square.visible = false;
			}
		}
		
		txtCounter.setText("amount of fingers on screen: " + pointersDown);
		super.update();
	}
}
