package org.flixel.plugin;

import org.flixel.FlxBasic;
import org.flixel.FlxG;
import org.flixel.system.input.Gamepad;
import org.flixel.system.input.GamepadMapping;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Manages the controllers and listen to input events.
 * 
 * @author Ka Wing Chin
 */
public class GamepadManager extends FlxBasic implements ControllerListener
{
	/**
	 * A self reference of the listener.
	 */
	public static GamepadManager listener;
	/**
	 * A map that keeps the controller and gamepad together.
	 */
	public static ObjectMap<Controller, Gamepad> controllers;
	/**
	 * The button mappings that will be used with <code>GamePad</code>.
	 */
	private static Array<GamepadMapping> mappings;
	/**
	 * An array that contains <code>Gamepad</code>s for quick access.
	 */
	private static Array<Gamepad> pads;

	/**
	 * Creates a new <code>GamepadManager</code> object.
	 * Needs to be added to the plugin, <code>FlxG.addPlugin()</code>.
	 */
	public GamepadManager()
	{
		// TODO: error gdx-controllers-android can't be found.
		// SharedLibraryLoader loader = new SharedLibraryLoader();
		// if(FlxG.mobile)
		// { /*loader.load("gdx-controllers-android");*/}
		// else if(Gdx.app.getType() == ApplicationType.WebGL)
		// { /*loader.load("gdx-controllers-gwt");*/}
		// else
		// loader.load("gdx-controllers-desktop");

		listener = this;
		controllers = new ObjectMap<Controller, Gamepad>();
		mappings = new Array<GamepadMapping>();
		addMapping(new GamepadMapping("generic"));
		pads = new Array<Gamepad>();
	}

	@Override
	public void destroy()
	{
		for(Controller c : Controllers.getControllers())
		{
			c.removeListener(listener);
		}
		listener = null;
		controllers.clear();
		controllers = null;
		for(int i = 0; i < mappings.size; i++)
			mappings.get(i).destroy();
		mappings.clear();
		mappings = null;
		for(int i = 0; i < pads.size; i++)
			pads.get(i).destroy();
		pads.clear();
		pads = null;
	}

	@Override
	public void update()
	{
		for(Gamepad pad : pads)
		{
			if(pad.connected)
				pad.update();
		}
	}

	/**
	 * Add new mapping.
	 * 
	 * @param	Mapping		The mapping that will be added.
	 */
	public static void addMapping(GamepadMapping Mapping)
	{
		//Don't bother adding a mapping twice.
		for(int i = 0; i < mappings.size; i++)
		{
			if(Mapping == mappings.get(i))
				return;
		}
		mappings.add(Mapping);
	}

	/**
	 * Add a new gamepad.
	 * 
	 * @param	Gamepad		The gamepad that will be added.
	 */
	public static void addGamepad(Gamepad Gamepad)
	{
		//Don't bother adding a gamepad twice.
		for(int i = 0; i < pads.size; i++)
		{
			if(Gamepad == pads.get(i))
				return;
		}
		pads.add(Gamepad);

		//Check if there is enough controllers available to hook a gamepad to it.
		if(pads.size > Controllers.getControllers().size)
		{
			FlxG.log("Gamepad > Available controllers: " + Controllers.getControllers().size);
			Gamepad.setMapping(mappings.get(0));
		}
		else
		{
			Controller c = Controllers.getControllers().get(pads.size - 1);
			c.addListener(listener);
			controllers.put(c, Gamepad);
			String controllerName = c.getName().toLowerCase();
			String mappingName;
			boolean mappingFound = false;
			for(int i = 0; i < mappings.size; i++)
			{
				if(mappings.get(i).ID != null)
				{
					if(controllerName.equals(mappings.get(i).ID.toLowerCase()))
					{
						Gamepad.setMapping(mappings.get(i));
						Gamepad.connected = true;
						mappingFound = true;
						break;
					}
				}
				if(mappings.get(i).IDs != null)
				{
					GamepadMapping mapping = mappings.get(i);
					for(int ii = 0; ii < mapping.IDs.length; ii++)
					{
						mappingName = mapping.IDs[ii].toLowerCase();
						if(controllerName.equals(mappingName))
						{
							Gamepad.setMapping(mapping);
							Gamepad.connected = true;
							mappingFound = true;
							break;
						}
					}
				}
			}
			if(!mappingFound)
			{
				FlxG.log("No mapping found for controller: " + c.getName() + "\nUse default mapping.");
				Gamepad.setMapping(mappings.get(0));
			}
		}
	}

	/**
	 * Remove a gamepad.
	 * 
	 * @param	Gamepad		The gamepad that will be moved.
	 */
	public static void removeGamepad(Gamepad Gamepad)
	{
		if(pads.removeValue(Gamepad, true))
		{
			Gamepad.connected = false;
			Controller c = controllers.findKey(Gamepad, true);
			if(c != null)
			{
				controllers.remove(c);
				c.removeListener(listener);
			}
		}
	}

	/**
	 * Get a gamepad by index.
	 * 
	 * @param	Index	The index.
	 * 
	 * @return	The gamepad.
	 */
	public static Gamepad get(int Index)
	{
		if(Index < pads.size)
			return controllers.get(Controllers.getControllers().get(Index));
		return null;
	}

	@Override
	public void connected(Controller Controller)
	{
		if(controllers.containsKey(Controller))
			controllers.get(Controller).connected = true;
		else
		{
			//add new controller
			for(int i = 0; i < pads.size; i++)
			{
				if(pads.get(i).ID == null)
					addGamepad(pads.get(i));
			}
		}
	}

	@Override
	public void disconnected(Controller Controller)
	{
		Gamepad gamepad = controllers.get(Controller);
		if(gamepad != null)
		{
			gamepad.connected = false;
			Controller c = controllers.findKey(gamepad, true);
			if(c != null)
			{
				controllers.remove(c);
				c.removeListener(listener);
			}
		}
	}

	@Override
	public boolean buttonDown(Controller Controller, int ButtonCode)
	{
		controllers.get(Controller).handleKeyDown(ButtonCode);
		return false;
	}

	@Override
	public boolean buttonUp(Controller Controller, int ButtonCode)
	{
		controllers.get(Controller).handleKeyUp(ButtonCode);
		return false;
	}

	@Override
	public boolean axisMoved(Controller Controller, int AxisCode, float Value)
	{
		controllers.get(Controller).axisData.put(AxisCode, Value);
		return false;
	}

	@Override
	public boolean povMoved(Controller Controller, int PovCode, PovDirection Value)
	{
		Gamepad pad = controllers.get(Controller);
		if(Value == PovDirection.center || pad.povDirection != Value)
		{
			pad.handleKeyUp(GamepadMapping.UP);
			pad.handleKeyUp(GamepadMapping.UP_RIGHT);
			pad.handleKeyUp(GamepadMapping.UP_LEFT);
			pad.handleKeyUp(GamepadMapping.RIGHT);
			pad.handleKeyUp(GamepadMapping.DOWN);
			pad.handleKeyUp(GamepadMapping.DOWN_RIGHT);
			pad.handleKeyUp(GamepadMapping.DOWN_LEFT);
			pad.handleKeyUp(GamepadMapping.LEFT);
		}
		switch(Value)
		{
			case north:
				pad.handleKeyDown(GamepadMapping.UP);
				break;
			case northEast:
				pad.handleKeyDown(GamepadMapping.UP_RIGHT);
				break;
			case northWest:
				pad.handleKeyDown(GamepadMapping.UP_LEFT);
				break;
			case east:
				pad.handleKeyDown(GamepadMapping.RIGHT);
				break;
			case south:
				pad.handleKeyDown(GamepadMapping.DOWN);
				break;
			case southEast:
				pad.handleKeyDown(GamepadMapping.DOWN_RIGHT);
				break;
			case southWest:
				pad.handleKeyDown(GamepadMapping.DOWN_LEFT);
				break;
			case west:
				pad.handleKeyDown(GamepadMapping.LEFT);
				break;
			default:
				break;
		}
		pad.povDirection = Value;
		return false;
	}

	@Override
	public boolean xSliderMoved(Controller Controller, int SliderCode, boolean Value)
	{
		// FlxG.log(sliderCode + " " + value);
		return false;
	}

	@Override
	public boolean ySliderMoved(Controller Controller, int SliderCode, boolean Value)
	{
		// FlxG.log(sliderCode + " " + value);
		return false;
	}

	@Override
	public boolean accelerometerMoved(Controller Controller, int AccelerometerCode, Vector3 Value)
	{
		controllers.get(Controller).x = Value.x;
		controllers.get(Controller).y = Value.y;
		controllers.get(Controller).z = Value.z;
		return false;
	}
}
