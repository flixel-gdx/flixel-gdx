package org.flixel.system.input.gamepad;

import org.flixel.FlxBasic;
import org.flixel.FlxG;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.SharedLibraryLoader;

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
	 * Needs to added to the plugin, <code>FlxG.addPlugin()</code>.
	 */
	public GamepadManager()
	{
		SharedLibraryLoader loader = new SharedLibraryLoader();
		if(FlxG.mobile)
			loader.load("gdx-controllers-android");
		else
			loader.load("gdx-controllers-desktop");
	
		listener = this;
		controllers = new ObjectMap<Controller, Gamepad>();
		mappings = new Array<GamepadMapping>();
		pads = new Array<Gamepad>();
	}
	
	@Override
	public void destroy()
	{
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
			pad.update();
		}
	}
	
	/**
	 * Add new mapping.
	 * @param mapping	The mapping that will be added.
	 */
	public static void addMapping(GamepadMapping mapping)
	{
		// Don't bother adding a mapping twice.
		for(int i = 0; i < mappings.size; i++)
		{
			if(mapping == mappings.get(i))
				return;
		}		
		mappings.add(mapping);		
	}
	
	/**
	 * Add a new gamepad.
	 * @param gamepad	The gamepad that will be added.
	 */
	public static void addGamepad(Gamepad gamepad)
	{
		// Don't bother adding a gamepad twice.
		for(int i = 0; i < pads.size; i++)
		{
			if(gamepad == pads.get(i))
				return;
		}
		pads.add(gamepad);
		
		// Check if there is enough controllers available to hook a gamepad to it.
		if(pads.size > Controllers.getControllers().size)
			FlxG.log("Gamepad > Available controllers: " + Controllers.getControllers().size);
		else
		{
			Controller c = Controllers.getControllers().get(pads.size-1);
			c.addListener(listener);
			controllers.put(c, gamepad);
			String controllerName = c.getName().toLowerCase();
			String mappingName;
			boolean mappingFound = false;
			for(int i = 0; i < mappings.size; i++)
			{
				if(mappings.get(i).ID != null)
				{
					if(controllerName.equals(mappings.get(i).ID.toLowerCase()))
					{
						gamepad.setMapping(mappings.get(i));
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
							gamepad.setMapping(mapping);
							mappingFound = true;
							break;
						}
					}
				}
			}
			if(!mappingFound)
				FlxG.log("No mapping found for controller: " + c.getName());
		}
	}
	
	/**
	 * Remove a gamepad.
	 * @param gamepad	The gamepad that will be moved.
	 */
	public static void removeGamepad(Gamepad gamepad)
	{		
		if(pads.removeValue(gamepad, true))
		{
			Controller c = controllers.findKey(gamepad, true);
			if(c != null)
				controllers.remove(c);
		}
	}	
	
	/**
	 * Get a gamepad by index.
	 * @param index		The index.
	 * @return			The gamepad.
	 */
	public static Gamepad get(int index)
	{
		if(index < pads.size)
			return controllers.get(Controllers.getControllers().get(index));
		return null;
	}

	@Override
	public void connected(Controller controller)
	{
		FlxG.log("connected: " + controller.getName());
	}

	@Override
	public void disconnected(Controller controller)
	{
		FlxG.log("disconnected: " + controller.getName());
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode)
	{
		controllers.get(controller).handleKeyDown(buttonCode);
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode)
	{
		controllers.get(controller).handleKeyUp(buttonCode);
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value)
	{
		controllers.get(controller).axisData.put(axisCode, value);
		return false;
	}

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value)
	{
		switch(value)
		{
			case north:controllers.get(controller).handleKeyDown(GamepadMapping.NORTH);break;
			case northEast:controllers.get(controller).handleKeyDown(GamepadMapping.NORTH_EAST);break;
			case northWest:controllers.get(controller).handleKeyDown(GamepadMapping.NORTH_WEST);break;
			case east:controllers.get(controller).handleKeyDown(GamepadMapping.EAST);break;
			case south:controllers.get(controller).handleKeyDown(GamepadMapping.SOUTH);break;
			case southEast:controllers.get(controller).handleKeyDown(GamepadMapping.SOUTH_EAST);break;
			case southWest:controllers.get(controller).handleKeyDown(GamepadMapping.SOUTH_WEST);break;
			case west:controllers.get(controller).handleKeyDown(GamepadMapping.WEST);break;				
			default:break;
		}
		if(value == PovDirection.center)
		{
			controllers.get(controller).handleKeyUp(GamepadMapping.NORTH);
			controllers.get(controller).handleKeyUp(GamepadMapping.NORTH_EAST);
			controllers.get(controller).handleKeyUp(GamepadMapping.NORTH_WEST);
			controllers.get(controller).handleKeyUp(GamepadMapping.EAST);
			controllers.get(controller).handleKeyUp(GamepadMapping.SOUTH);
			controllers.get(controller).handleKeyUp(GamepadMapping.SOUTH_EAST);
			controllers.get(controller).handleKeyUp(GamepadMapping.SOUTH_WEST);
			controllers.get(controller).handleKeyUp(GamepadMapping.WEST);
		}
		return false;
	}

	@Override
	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value)
	{
//		FlxG.log(sliderCode + " " + value);
		return false;
	}

	@Override
	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value)
	{
//		FlxG.log(sliderCode + " " + value);
		return false;
	}

	@Override
	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value)
	{
		controllers.get(controller).x = value.x;
		controllers.get(controller).y = value.y;
		controllers.get(controller).z = value.z;		
		return false;
	}
}

