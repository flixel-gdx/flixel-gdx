package org.examples.multitouch;

import org.flixel.FlxG;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.FlxText;

import com.badlogic.gdx.Gdx;

/**
 * A simple demo to test your multi-touch support.
 * 
 * @author Ka Wing Chin
 */
public class PlayState extends FlxState
{
	private int numberOfFingers;
	private FlxText txtCounter;
	private int length;
	private FlxSprite[] sprites;

	@Override
	public void create()
	{
		FlxG.setBgColor(0xFF000000);
		numberOfFingers = 0;
		txtCounter = new FlxText(20, 20, 200, "amount of fingers on screen: 0");
		add(txtCounter);
		length = 10;
		sprites = new FlxSprite[length];
		for(int i = 0; i<length; i++)
		{
			sprites[i] = new FlxSprite().makeGraphic(80, 80);
			sprites[i].offset.x = sprites[i].width/2;
			sprites[i].offset.y = sprites[i].height/2;
			sprites[i].visible = false;
			add(sprites[i]);
		}
	}
	
	@Override
	public void update()
	{
		for(int i = 0; i < length; i++)
		{			
			if(FlxG.mouse.pressed())
			{
				numberOfFingers++;
				sprites[i].x = FlxG.mouse.x;
				sprites[i].y = FlxG.mouse.y;
				sprites[i].visible = true;
			}
			else
			{
				sprites[i].visible = false;
			}
		}
		
//		if(FlxG.touch.justReleased())
//		{
//			FlxG.log("just released");
//		}
//		else if(FlxG.touch.justPressed())
//		{
//			FlxG.log("just pressed");
//		}
		
		txtCounter.setText("amount of fingers on screen: " + numberOfFingers);
		numberOfFingers = 0;
		super.update();
	}
}
