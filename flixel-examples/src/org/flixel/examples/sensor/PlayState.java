package org.flixel.examples.sensor;

import org.flixel.FlxG;
import org.flixel.FlxGroup;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.flixel.FlxU;
import org.flixel.data.SystemAsset;

import com.badlogic.gdx.Gdx;


/**
 * A simple demo how to use the sensor.
 * 
 * @author Ka Wing Chin
 */
public class PlayState extends FlxState
{
	private FlxText _text;
	private FlxGroup _sprites;
	

	@Override
	public void create()
	{		
		FlxG.setBgColor(0xffacbcd7);
		
		_text = new FlxText(10, 10, FlxG.width-20);
		add(_text);
		
		
		_sprites = new FlxGroup();
		FlxSprite s;
		for(int i = 0; i < 10; i++)
		{	
			_sprites.add(s = new FlxSprite(FlxU.floor(FlxG.random()*40)*7,FlxU.floor(FlxG.random()*30)*7, SystemAsset.ImgDefault));			
		}
		add(_sprites);
		
		
		// Add some wall around the edges.		
		add(s = new FlxSprite(0, 0).makeGraphic(FlxG.width, 2));
		s.immovable = true;
		add(s = new FlxSprite(0, FlxG.height-2).makeGraphic(FlxG.width, 2));
		s.immovable = true;
		add(s = new FlxSprite(0, 0).makeGraphic(2, FlxG.height));
		s.immovable = true;
		add(s = new FlxSprite(FlxG.width-2, 0).makeGraphic(2, FlxG.height));
		s.immovable = true;
	}
	
	@Override
	public void update()
	{	
		_text.setText(		"X:" + Gdx.input.getAccelerometerX()
						+ "\nY:" + Gdx.input.getAccelerometerY()
						+ "\nZ:" + Gdx.input.getAccelerometerZ() 
						+ "\n" + "orientation: " + Gdx.input.getNativeOrientation()
						+ "\n" + "rotation: "
						+ Gdx.input.getRotation() 
						+ "\n" + "wh: " + Gdx.graphics.getDesktopDisplayMode() + "\n");
		
		for(int i = 0; i < _sprites.length; i++)
		{
			FlxSprite s = (FlxSprite) _sprites.members.get(i);
			s.velocity.x -= FlxG.sensor.x;
			s.velocity.y += FlxG.sensor.y;
		}
		
		FlxG.collide();
		super.update();
	}

}
