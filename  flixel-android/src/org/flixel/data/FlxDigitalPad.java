package org.flixel.data;

import java.util.Hashtable;

import org.flixel.FlxButton;
import org.flixel.FlxG;
import org.flixel.FlxGroup;
import org.flixel.FlxSprite;

public class FlxDigitalPad extends FlxGroup
{
	/**
	 * @private
	 */
	private Hashtable<String, FlxButton> _map;

	/**
	 * Constructor
	 * @param X		The x-pos on screen.
	 * @param Y		The y-pos on screen.
	 * @param Scale	The size of the d-pad.
	 */
	public FlxDigitalPad(float X, float Y)
	{			
		x = X;
		y = Y;
		scrollFactor.x = 0;
		scrollFactor.y = 0;
		setSolid(false);
		setFixed(true);
		_map = new Hashtable<String, FlxButton>();	
		
		// BASE
		FlxSprite base = new FlxSprite();
		base.loadGraphic(R.drawable.control_base, false, false, 138, 138);
		
		base.setSolid(false);
		base.setFixed(true);
		add(base, true);
		
		
		// Button UP
		FlxSprite s = new FlxSprite();
		s.loadGraphic(R.drawable.dpad_up, false, false, 98, 49);
		
		FlxSprite s2 = new FlxSprite();
		s2.loadGraphic(R.drawable.dpad_up_pressed, false, false, 98, 49);
		createButton((base.width/2)-(s.width/2), 0, "UP", s, s2);
		
		
		
		// Button RIGHT
		s = new FlxSprite();
		s.loadGraphic(R.drawable.dpad_right, false, false, 49, 98);
		s2 = new FlxSprite();
		s2.loadGraphic(R.drawable.dpad_right_pressed, false, false, 49, 98);
		createButton(base.width-s.width, (base.height/2)-(s.width), "RIGHT", s, s2);
		
		
		// Button DOWN
		s = new FlxSprite();
		s.loadGraphic(R.drawable.dpad_down, false, false, 98, 49);		
		s2 = new FlxSprite();
		s2.loadGraphic(R.drawable.dpad_down_pressed, false, false, 98, 49);
		createButton((base.width/2)-(s.width/2), (base.height-s.height), "DOWN", s, s2);
		
		
		// Button LEFT
		s = new FlxSprite();
		s.loadGraphic(R.drawable.dpad_left, false, false, 49, 98);		
		s2 = new FlxSprite();
		s2.loadGraphic(R.drawable.dpad_left_pressed, false, false, 49, 98);
		createButton(0, (base.height/2)-(s.height/2), "LEFT", s, s2);
		
	}
		
	
	private FlxButton createButton(float X, float Y, String Name, FlxSprite Up, FlxSprite Down)
	{
		FlxButton btn = new FlxButton(X, Y);
		btn.loadGraphic(Up, Down);
		btn.setSolid(false);
		btn.setFixed(true);
		add(btn, true);
		addButton(Name, btn);
		return btn;
	}
	
	@Override
	public void update()
	{		
		if(FlxG.dpad.visible && !FlxG.getPause())
		{			
			FlxG.dpad.render();
			super.update();
		}
	}

	
	public boolean pressed(String name)
	{
		if(_map.containsKey(name))		
		{
			return _map.get(name).getPressed();
		}
		return false;
	}
	
	
	public boolean justTouched(String name)
	{
		if(_map.containsKey(name))
		{
			return _map.get(name).getTouched();
		}
		return false;
	}
	
	
	public boolean justRemoved(String name)
	{
		if(_map.containsKey(name))
		{
			return _map.get(name).getRemoved();
		}
		return false;
	}
	


	/**
	 * An internal helper function used to build the key array.
	 * 
	 * @param KeyName String name of the key (e.g. "LEFT" or "A")
	 * @param KeyCode The numeric Flash code for this key.
	 */
	protected void addButton(String ButtonName, FlxButton Button)
	{
		_map.put(ButtonName, Button);		
	}
	
	public void setPosition(float X, float Y)
	{
		x = X;
		y = Y;
	}

	public void show()
	{
		visible = true;
		for(FlxButton o : _map.values())
		{
			o.active = true;
		}
	}
	
	public void hide()
	{
		visible = false;
		for(FlxButton o : _map.values())
		{
			o.active = false;
		}
	}
	
	public void setScale(float x, float y)
	{
		for(FlxButton o : _map.values())
		{
			o.width /= 2;
			o.height /= 2;
		}
	}
}
