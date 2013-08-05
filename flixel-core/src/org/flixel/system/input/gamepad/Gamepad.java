package org.flixel.system.input.gamepad;

import org.flixel.FlxG;
import org.flixel.FlxPoint;
import org.flixel.system.input.Input;

import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;

/**
 * A gamepad with common buttons. Keeps track of what keys are pressed and how with 
 * handy booleans or strings.
 * 
 * @author Ka Wing Chin
 */
public class Gamepad extends Input
{
	public boolean NORTH;
	public boolean NORTH_EAST;
	public boolean NORTH_WEST;	
	public boolean EAST;
	public boolean SOUTH;
	public boolean SOUTH_EAST;
	public boolean SOUTH_WEST;
	public boolean WEST;
	public boolean CENTER;
	public boolean BUTTON_DPAD_UP;
	public boolean BUTTON_DPAD_RIGHT;
	public boolean BUTTON_DPAD_DOWN;
	public boolean BUTTON_DPAD_LEFT;
	public boolean BUTTON_DPAD_CIRCLE;
	public boolean BUTTON_A;
	public boolean BUTTON_B;
	public boolean BUTTON_X;
	public boolean BUTTON_Y;
	public boolean BUTTON_Z;
	public boolean BUTTON_L1;
	public boolean BUTTON_R1;
	public boolean BUTTON_L2;
	public boolean BUTTON_R2;
	public boolean BUTTON_L3;
	public boolean BUTTON_R3;
	public boolean BUTTON_THUMBL;
	public boolean BUTTON_THUMBR;
	public boolean BUTTON_START;
	public boolean BUTTON_SELECT;
	public boolean BUTTON_MODE;
	private int AXIS_LEFT_X;
	private int AXIS_LEFT_Y;
	private int AXIS_RIGHT_X;
	private int AXIS_RIGHT_Y;
		
	/**
	 * This is just a pre-allocated x-y point container to be used however you like.
	 */
	private FlxPoint _point;
	/**
	 * Internal, put acceleration data from controller.
	 */
	IntMap<Float> axisData;
	/**
	 * The sensitivity of the L-axis. Default 100.
	 */
	public float sensitivityL = 100;
	/**
	 * The sensitivity of the R-axis. Default 100.
	 */
	public float sensitivityR = 100;
	/**
	 * The value of the accelerometer on its x-axis.
	 */
	public float x;
	/**
	 * The value of the accelerometer on its x-axis.
	 */
	public float y;
	/**
	 * The value of the accelerometer on its y-axis.
	 */
	public float z;
	/**
	 * The ID of the gamepad which is equal to the ID of the <code>Controller</code>.
	 */
	public String ID;
		
	/**
	 * Creates a new <code>Gamepad</code> object.
	 */
	public Gamepad()
	{
		_map.shrink();
		_point = new FlxPoint();
		axisData = new IntMap<Float>(4);
	}
	
	@Override
	public void destroy()
	{
		super.destroy();
		_point = null;
		axisData.clear();
		axisData = null;
	}

	@Override
	public void reset()
	{
		for(KeyState o : _map)
		{
			if (o.name.length() == 0)
				continue;			
			try 
			{
				ClassReflection.getField(Gamepad.class, o.name).set(this, false);
			} 
			catch (Exception e) 
			{
				FlxG.log("Gamepad", e.getMessage());
			}
			o.current = 0;
			o.last = 0;
		}
	}

	/**
	 * Event handler so GamepadManager can toggle keys.
	 * @param buttonCode	The button code of the pressed key.
	 */
	public void handleKeyDown(int buttonCode)
	{
		KeyState o = _map.get(buttonCode);
		if(o.name.length() == 0)
			return;

		if(o.current > 0)
			o.current = 1;
		else
			o.current = 2;
		
		try
		{
			ClassReflection.getField(Gamepad.class, o.name).set(this, true);
		}
		catch(Exception e)
		{
			FlxG.log("Gamepad", e.getMessage());
		}
	}
	
	/**
	 * Event handler so GamepadManager can toggle keys.
	 * @param buttonCode	The button code of the pressed key.
	 */
	public void handleKeyUp(int buttonCode)
	{
		KeyState o = _map.get(buttonCode);
		if(o.name.length() == 0)
			return;

		if(o.current > 0)
			o.current = -1;
		else
			o.current = 0;

		try
		{
			ClassReflection.getField(Gamepad.class, o.name).set(this, false);
		}
		catch(Exception e)
		{
			FlxG.log("Gamepad", e.getMessage());
		}
	}
	
	/**
	 * Set the button mapping provided by <code>GamepadMapping</code>.
	 * @param mapping	The mapping that will be set to this gamepad.
	 */
	public void setMapping(GamepadMapping mapping)
	{
		ID = mapping.ID;		
		addKey("NORTH", GamepadMapping.NORTH);
		addKey("NORTH_EAST", GamepadMapping.NORTH_EAST);
		addKey("NORTH_WEST", GamepadMapping.NORTH_WEST);
		addKey("EAST", GamepadMapping.EAST);
		addKey("SOUTH", GamepadMapping.SOUTH);
		addKey("SOUTH_EAST", GamepadMapping.SOUTH_EAST);
		addKey("SOUTH_WEST", GamepadMapping.SOUTH_WEST);
		addKey("WEST", GamepadMapping.WEST);		
		addKey("CENTER", GamepadMapping.CENTER);
		addKey("BUTTON_DPAD_UP", mapping.BUTTON_DPAD_UP);
		addKey("BUTTON_DPAD_RIGHT", mapping.BUTTON_DPAD_RIGHT);
		addKey("BUTTON_DPAD_DOWN", mapping.BUTTON_DPAD_DOWN);
		addKey("BUTTON_DPAD_LEFT", mapping.BUTTON_DPAD_LEFT);
		addKey("BUTTON_CIRCLE", mapping.BUTTON_CIRCLE);
		addKey("BUTTON_A", mapping.BUTTON_A);
		addKey("BUTTON_B", mapping.BUTTON_B);
		addKey("BUTTON_X", mapping.BUTTON_X);
		addKey("BUTTON_Y", mapping.BUTTON_Y);
		addKey("BUTTON_Z", mapping.BUTTON_Z);
		addKey("BUTTON_L1", mapping.BUTTON_L1);
		addKey("BUTTON_R1", mapping.BUTTON_R1);
		addKey("BUTTON_L2", mapping.BUTTON_L2);
		addKey("BUTTON_R2", mapping.BUTTON_R2);
		addKey("BUTTON_THUMBL", mapping.BUTTON_L3);
		addKey("BUTTON_THUMBR", mapping.BUTTON_R3);
		addKey("BUTTON_START", mapping.BUTTON_START);
		addKey("BUTTON_SELECT", mapping.BUTTON_SELECT);
		addKey("BUTTON_MODE", mapping.BUTTON_MODE);
		
		AXIS_LEFT_X = mapping.AXIS_LEFT_X;
		AXIS_LEFT_Y = mapping.AXIS_LEFT_Y;
		AXIS_RIGHT_X = mapping.AXIS_RIGHT_X;
		AXIS_RIGHT_Y = mapping.AXIS_RIGHT_Y;
		
		axisData.put(mapping.AXIS_LEFT_X, 0f);
		axisData.put(mapping.AXIS_LEFT_Y, 0f);
		axisData.put(mapping.AXIS_RIGHT_X, 0f);
		axisData.put(mapping.AXIS_RIGHT_Y, 0f);
	}
	
	/**
	 * Get the data from L-axis.
	 * @return	A point which contains the x and y value.
	 */
	public FlxPoint getAxisL()
	{
		if(ID == null || AXIS_LEFT_X == -1 || AXIS_LEFT_Y == -1)
			return _point;
		_point.x = axisData.get(AXIS_LEFT_X) * sensitivityL;
		_point.y = axisData.get(AXIS_LEFT_Y) * sensitivityL;
		return _point;
	}
	
	/**
	 * Get the data from R-axis.
	 * @return	A point which contains the x and y value.
	 */
	public FlxPoint getAxisR()
	{
		if(ID == null || AXIS_RIGHT_X == -1 || AXIS_RIGHT_Y == -1)
			return _point;
		_point.x = axisData.get(AXIS_RIGHT_X) * sensitivityR;
		_point.y = axisData.get(AXIS_RIGHT_Y) * sensitivityR;
		return _point;
	}
}