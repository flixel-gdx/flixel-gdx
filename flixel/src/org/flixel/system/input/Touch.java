package org.flixel.system.input;

import org.flixel.FlxCamera;
import org.flixel.FlxG;
import org.flixel.FlxPoint;

import com.badlogic.gdx.InputAdapter;

/**
 * This class helps contain and track the mouse pointer in your game.
 * Automatically accounts for parallax scrolling, etc.
 * 
 * @author Ka Wing Chin
 */
public class Touch extends InputAdapter
{
	/**
	 * Current X position of the touch pointer in the game world.
	 */
	public float x;
	/**
	 * Current Y position of the touch in the game world.
	 */
	public float y;	
	/**
	 * Current X position of the mouse pointer on the screen.
	 */
	public int screenX;
	/**
	 * Current Y position of the mouse pointer on the screen.
	 */
	public int screenY;	
	
	/**
	 * Helper variable for tracking whether the mouse was just pressed or just released.
	 */
	protected int _current;
	/**
	 * Helper variable for tracking whether the mouse was just pressed or just released.
	 */
	protected int _last;
	/**
	 * Helper variables for recording purposes.
	 */
	protected int _lastX;
	protected int _lastY;
	protected FlxPoint _point;
	private FlxPoint _globalScreenPosition;
	
	/**
	 * Constructor
	 */
	public Touch()
	{
		x = 0;
		y = 0;
		_current = 0;
		_last = 0;		
		_point = new FlxPoint();
		_globalScreenPosition = new FlxPoint();
	}

	
	/**
	 * Called by the internal game loop to update the mouse pointer's position in the game world.
	 * Also updates the just pressed/just released flags.
	 * 
	 * @param	X			The current X position of the mouse in the window.
	 * @param	Y			The current Y position of the mouse in the window.
	 */
	public void update(float X, float Y)
	{
		//update the x, y, screenX, and screenY variables based on the default camera.
		//This is basically a combination of getWorldPosition() and getScreenPosition()		
		_globalScreenPosition.x = X;
		_globalScreenPosition.y = Y;
		FlxCamera camera = FlxG.camera;
		screenX = (int) ((_globalScreenPosition.x - camera.x) / camera.getZoom());
		screenY = (int) ((_globalScreenPosition.y - camera.y) / camera.getZoom());
		
		x = screenX + camera.scroll.x;
		y = screenY + camera.scroll.y;
		
		if((_last == -1) && (_current == -1))
			_current = 0;
		else if((_last == 2) && (_current == 2))
			_current = 1;
		_last = _current;
	}
	
	
	/**
	 * Fetch the world position of the mouse on any given camera.
	 * NOTE: Mouse.x and Mouse.y also store the world position of the mouse cursor on the main camera.
	 * 
	 * @param Camera	If unspecified, first/main global camera is used instead.
	 * @param Point		An existing point object to store the results (if you don't want a new one created). 
	 * 
	 * @return The mouse's location in world space.
	 */
	public FlxPoint getWorldPosition(FlxCamera Camera, FlxPoint Point)
	{
		if(Camera == null)
			Camera = FlxG.camera;
		if(Point == null)
			Point = new FlxPoint();
		getScreenPosition(Camera,_point);
		Point.x = _point.x + Camera.scroll.x;
		Point.y = _point.y + Camera.scroll.y;
		return Point;
	}
	
	
	/**
	 * Fetch the screen position of the mouse on any given camera.
	 * NOTE: Mouse.screenX and Mouse.screenY also store the screen position of the mouse cursor on the main camera.
	 * 
	 * @param Camera	If unspecified, first/main global camera is used instead.
	 * @param Point		An existing point object to store the results (if you don't want a new one created). 
	 * 
	 * @return The mouse's location in screen space.
	 */
	public FlxPoint getScreenPosition(FlxCamera Camera,FlxPoint Point)
	{
		if(Camera == null)
			Camera = FlxG.camera;
		if(Point == null)
			Point = new FlxPoint();
		Point.x = (_globalScreenPosition.x - Camera.x)/Camera.getZoom();
		Point.y = (_globalScreenPosition.y - Camera.y)/Camera.getZoom();
		return Point;
	}
	
	
	/**
	 * Resets the just pressed/just released flags and sets mouse to not pressed.
	 */
	public void reset()
	{
		_current = 0;
		_last = 0;
	}
	
	
	/**
	 * Check to see if the mouse is pressed.
	 * 
	 * @return	Whether the screen is pressed.
	 */
	public boolean pressed()
	{
		return _current > 0;
	}
	
	
	/**
	 * Check to see if the screen was just pressed.
	 * 
	 * @return Whether the screen was just pressed.
	 */
	public boolean justPressed()
	{
		return _current == 2;
	}
	
	
	/**
	 * Check to see if the screen was just released.
	 * 
	 * @return	Whether the screen was just released.
	 */
	public boolean justReleased()
	{
		return _current == -1;
	}
	
	
	/**
	 * Event handler so FlxGame can update the touch.
	 * Don't call this.
	 */
	@Override
	public boolean touchUp(int x, int y, int pointer, int button)
	{
		if(_current > 0) _current = -1;
		else _current = 0;
		return false;
	}
	
	
	/**
	 * Event handler so FlxGame can update the touch.
	 * Don't call this.
	 */
	@Override
	public boolean touchDown(int x, int y, int pointer, int button)
	{
		if(_current > 0) _current = 1;
		else _current = 2;
		return false;
	}
}
