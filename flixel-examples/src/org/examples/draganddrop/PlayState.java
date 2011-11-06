package org.examples.draganddrop;

import org.flixel.FlxG;
import org.flixel.FlxSprite;
import org.flixel.FlxState;

/**
 * A demo that let you grab crates.
 * 
 * @author Ka Wing Chin
 */
public class PlayState extends FlxState
{
	
	@Override
	public void create()
	{
		// Background
		FlxG.setBgColor(0xFF333333);
		
		// A shiny flixel logo, untouchable.
		FlxSprite s = new FlxSprite(100, 20).loadGraphic(Asset.ImgLogo, true, 60, 60);
		s.addAnimation("shine", new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,3,4}, 14);
		s.acceleration.y = 400;
		s.setSolid(true);
		s.play("shine");
		add(s);
		
		// Create 4 crates.
		for(int i = 0; i < 4; i++)
			add(new Crate(50+100*i, 100));
		
		// Add some wall around the edges.
		s = new FlxSprite(0, FlxG.height-5).makeGraphic(FlxG.width, 5);
		s.immovable = true;
		add(s);
		s = new FlxSprite(0, 0).makeGraphic(5, FlxG.height);
		s.immovable = true;
		add(s);
		s = new FlxSprite(FlxG.width-5, 0).makeGraphic(5, FlxG.height);
		s.immovable = true;
		add(s);
	}
	
	@Override
	public void update()
	{		
		super.update();
		FlxG.collide();
	}
}
