package org.flixel.system.input;

import org.flixel.FlxCamera;
import org.flixel.FlxG;
import org.flixel.FlxPoint;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * This class helps contain and track the mouse pointer in your game.
 * Automatically accounts for parallax scrolling, etc.
 * 
 * @author Ka Wing Chin
 */
public class Mouse extends FlxPoint
{
	/**
	 * Current "delta" value of mouse wheel.  If the wheel was just scrolled up, it will have a positive value.  If it was just scrolled down, it will have a negative value.  If it wasn't just scroll this frame, it will be 0.
	 */
	public int wheel;
	/**
	 * Current X position of the mouse pointer on the screen.
	 */
	public int screenX;
	/**
	 * Current Y position of the mouse pointer on the screen.
	 */
	public int screenY;	
	
	/**
	 * Helper variables for recording purposes.
	 */
	protected int _lastWheel;
	protected FlxPoint _point;
	
	protected Array<Pointer> _pointers;
	
	/**
	 * Constructor
	 */
	public Mouse()
	{
		super();
		screenX = 0;
		screenY = 0;
		_lastWheel = wheel = 0;	
		_point = new FlxPoint();
		_pointers = new Array<Pointer>();
		_pointers.add(new Pointer());
	}

	/**
	 * Clean up memory.
	 */
	public void destroy()
	{
		_point = null;
		_pointers.clear();
		_pointers = null;
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
		updateCursor();
	
		for (Pointer o : _pointers)
		{
			if((o.last == -1) && (o.current == -1))
				o.current = 0;
			else if((o.last == 2) && (o.current == 2))
				o.current = 1;
			o.last = o.current;
		}
	}
	
	/**
	 * Internal function for helping to update the mouse cursor and world coordinates.
	 */
	protected void updateCursor()
	{
		Pointer o = _pointers.get(0);
		
		//actually position the flixel mouse cursor graphic
		//_cursorContainer.x = o.globalScreenPosition.x;
		//_cursorContainer.y = o.globalScreenPosition.y;
		
		//update the x, y, screenX, and screenY variables based on the default camera.
		//This is basically a combination of getWorldPosition() and getScreenPosition()
		FlxCamera camera = FlxG.camera;
		screenX = (int) ((o.globalScreenPosition.x - camera.x)/camera.getZoom());
		screenY = (int) ((o.globalScreenPosition.y - camera.y)/camera.getZoom());
		x = screenX + camera.scroll.x;
		y = screenY + camera.scroll.y;
	}
	
	/**
	 * Fetch the world position of the mouse on any given camera.
	 * NOTE: Mouse.x and Mouse.y also store the world position of the mouse cursor on the main camera.
	 * 
	 * @param Camera	If unspecified, first/main global camera is used instead.
	 * @param Point		An existing point object to store the results (if you don't want a new one created). 
	 * @param Pointer	The pointer.
	 * 
	 * @return The mouse's location in world space.
	 */
	public FlxPoint getWorldPosition(FlxCamera Camera, FlxPoint Point, int Pointer)
	{
		if(Camera == null)
			Camera = FlxG.camera;
		if(Point == null)
			Point = new FlxPoint();
		getScreenPosition(Camera,_point, Pointer);
		Point.x = _point.x + Camera.scroll.x;
		Point.y = _point.y + Camera.scroll.y;
		return Point;
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
		return getWorldPosition(Camera, Point, 0);
	}
	
	/**
	 * Fetch the world position of the mouse on any given camera.
	 * NOTE: Mouse.x and Mouse.y also store the world position of the mouse cursor on the main camera.
	 * 
	 * @param Camera	If unspecified, first/main global camera is used instead.
	 * 
	 * @return The mouse's location in world space.
	 */
	public FlxPoint getWorldPosition(FlxCamera Camera)
	{
		return getWorldPosition(Camera, null, 0);
	}
	
	/**
	 * Fetch the world position of the mouse on any given camera.
	 * NOTE: Mouse.x and Mouse.y also store the world position of the mouse cursor on the main camera.
	 * 	 * 
	 * @return The mouse's location in world space.
	 */
	public FlxPoint getWorldPosition()
	{
		return getWorldPosition(null, null, 0);
	}
	
	/**
	 * Fetch the screen position of the mouse on any given camera.
	 * NOTE: Mouse.screenX and Mouse.screenY also store the screen position of the mouse cursor on the main camera.
	 * 
	 * @param Camera	If unspecified, first/main global camera is used instead.
	 * @param Point		An existing point object to store the results (if you don't want a new one created). 
	 * @param Pointer	The pointer.
	 * 
	 * @return The mouse's location in screen space.
	 */
	public FlxPoint getScreenPosition(FlxCamera Camera, FlxPoint Point, int Pointer)
	{
		if(Camera == null)
			Camera = FlxG.camera;
		if(Point == null)
			Point = new FlxPoint();
		
		if (Pointer >= _pointers.size)
			return Point;
		
		Pointer o = _pointers.get(Pointer);
		
		Vector3 screenPoint = new Vector3();
		screenPoint.x = (o.globalScreenPosition.x - Camera.x)/Camera.getZoom();
		screenPoint.y = (o.globalScreenPosition.y - Camera.y)/Camera.getZoom();
		
		Camera.buffer.unproject(screenPoint);
		
		Point.x = screenPoint.x;
		Point.y = screenPoint.y;
		
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
		return getScreenPosition(Camera, Point, 0);
	}
	
	/**
	 * Fetch the screen position of the mouse on any given camera.
	 * NOTE: Mouse.screenX and Mouse.screenY also store the screen position of the mouse cursor on the main camera.
	 * 
	 * @param Camera	If unspecified, first/main global camera is used instead.
	 * 
	 * @return The mouse's location in screen space.
	 */
	public FlxPoint getScreenPosition(FlxCamera Camera)
	{
		return getScreenPosition(Camera, null, 0);
	}
	
	/**
	 * Fetch the screen position of the mouse on any given camera.
	 * NOTE: Mouse.screenX and Mouse.screenY also store the screen position of the mouse cursor on the main camera.
	 * 
	 * @return The mouse's location in screen space.
	 */
	public FlxPoint getScreenPosition()
	{
		return getScreenPosition(null, null, 0);
	}
	
	/**
	 * Resets the just pressed/just released flags and sets mouse to not pressed.
	 */
	public void reset()
	{
		_pointers.clear();
		_pointers.add(new Pointer());
	}
	
	/**
	 * Check to see if the mouse is pressed.
	 * 
	 * @param Pointer	The pointer id
	 * 
	 * @return	Whether the screen is pressed.
	 */
	public boolean pressed(int Pointer)
	{
		if (Pointer >= _pointers.size)
			return false;		
		return _pointers.get(Pointer).current > 0;
	}
	
	/**
	 * Check to see if the mouse is pressed.
	 * 
	 * @return	Whether the screen is pressed.
	 */
	public boolean pressed()
	{
		return pressed(0);
	}
	
	/**
	 * Check to see if the screen was just pressed.
	 * 
	 * @param	Pointer	The pointer id
	 * 
	 * @return Whether the screen was just pressed.
	 */
	public boolean justPressed(int Pointer)
	{
		if (Pointer >= _pointers.size)
			return false;
		return _pointers.get(Pointer).current == 2;
	}
	
	/**
	 * Check to see if the screen was just pressed.
	 * 
	 * @return Whether the screen was just pressed.
	 */
	public boolean justPressed()
	{
		return justPressed(0);
	}
	
	/**
	 * Check to see if the screen was just released.
	 * 
	 * @param	Pointer	The pointer id
	 * 
	 * @return	Whether the screen was just released.
	 */
	public boolean justReleased(int Pointer)
	{
		if (Pointer >= _pointers.size)
			return false;
		return _pointers.get(Pointer).current == -1;
	}
	
	/**
	 * Check to see if the screen was just released.
	 * 
	 * @return	Whether the screen was just released.
	 */
	public boolean justReleased()
	{
		return justReleased(0);
	}
	
	/**
	 * Event handler so FlxGame can update the touch.
	 * Don't call this.
	 */
	public void handleMouseUp(int X, int Y, int Pointer, int Button)
	{
		Pointer o;
		
		if (Pointer >= _pointers.size)
		{
			o = new Pointer();
			_pointers.add(o);
		}
		else
			o = _pointers.get(Pointer);
		
		if(o.current > 0)
			o.current = -1;
		else 
			o.current = 0;
		
		o.x = o.globalScreenPosition.x = X;
		o.y = o.globalScreenPosition.y = Y;
	}
	
	
	/**
	 * Event handler so FlxGame can update the touch.
	 * Don't call this.
	 */
	public void handleMouseDown(int X, int Y, int Pointer, int Button)
	{
		Pointer o;
		
		if (Pointer >= _pointers.size)
		{
			o = new Pointer();
			_pointers.add(o);
		}
		else
			o = _pointers.get(Pointer);
		
		if(o.current > 0) 
			o.current = 1;
		else
			o.current = 2;
		
		o.x = o.globalScreenPosition.x = X;
		o.y = o.globalScreenPosition.y = Y;
		
		/*FlxG.log(o.x + " : " + o.y);		
		FlxPoint p = getScreenPosition(FlxG.camera, _point, Pointer);
		x = o.x = p.x;
		y = o.y = p.y;
		FlxG.log(x + " : " + y);*/
	}
	
	/**
	 * Event handler so FlxGame can update the touch.
	 * Don't call this.
	 */
	public void handleMouseDragged(int X, int Y, int Pointer)
	{
		Pointer o;
		
		if (Pointer >= _pointers.size)
		{
			o = new Pointer();
			_pointers.add(o);
		}
		else
			o = _pointers.get(Pointer);
		
		o.x = o.globalScreenPosition.x = X;
		o.y = o.globalScreenPosition.y = Y;
	}
	
	public void handleMouseMoved(int X, int Y)
	{
		Pointer o = _pointers.get(0);
		
		o.x = o.globalScreenPosition.x = X;
		o.y = o.globalScreenPosition.y = Y;
	}
	
	/**
	 * Event handler so FlxGame can update the touch.
	 * Don't call this.
	 */
	public void handleMouseWheel(int Amount)
	{
		wheel = Amount;
	}
		
	/**
	 * If the mouse changed state or is pressed, return that info now
	 * 
	 * @return	An array of key state data.  Null if there is no data.
	 */
	/*public MouseRecord record()
	{
		if((_lastX == _globalScreenPosition.x) && (_lastY == _globalScreenPosition.y) && (_current == 0) && (_lastWheel == wheel))
			return null;
		_lastX = (int) _globalScreenPosition.x;
		_lastY = (int) _globalScreenPosition.y;
		_lastWheel = wheel;
		return new MouseRecord(_lastX,_lastY,_current,_lastWheel);
	}
	
	/**
	 * Part of the keystroke recording system.
	 * Takes data about key presses and sets it into array.
	 * 
	 * @param	KeyStates	Array of data about key states.
	 */
	/*public void playback(MouseRecord Record)
	{
		_current = Record.button;
		wheel = Record.wheel;
		_globalScreenPosition.x = Record.x;
		_globalScreenPosition.y = Record.y;
		updateCursor();
	}*/
	
	protected class Pointer
	{
		public float x = 0;
		public float y = 0;
		public int current = 0;
		public int last = 0;
		public int lastX = 0;
		public int lastY = 0;
		public FlxPoint globalScreenPosition = new FlxPoint();
	}
}

