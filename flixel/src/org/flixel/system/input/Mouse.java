package org.flixel.system.input;

import org.flixel.FlxCamera;
import org.flixel.FlxG;
import org.flixel.FlxPoint;
import org.flixel.system.replay.MouseRecord;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * This class helps contain and track the pointers in your game.
 * Automatically accounts for parallax scrolling, etc.
 * For multi-touch devices the <code>x</code>, <code>y</code>, <code>screenX</code>, and <code>screenY</code>
 * properties refer to the first pointer.
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
	 * Current X position of the pointer on the screen. For multi-touch devices this is the first pointer.
	 */
	public int screenX;
	/**
	 * Current Y position of the pointer on the screen. For multi-touch devices this is the first pointer.
	 */
	public int screenY;	
	
	/**
	 * Helper variables for recording purposes.
	 */
	protected int _lastWheel;
	protected FlxPoint _point;
	
	protected Array<Pointer> _pointers;
	
	/**
     * The current active pointers.
     */
    public int activePointers;
	
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
	 * Either show an existing cursor or load a new one.
	 * 
	 * @param	Graphic		The image you want to use for the cursor.
	 * @param	Scale		Change the size of the cursor.  Default = 1, or native size.  2 = 2x as big, 0.5 = half size, etc.
	 * @param	XOffset		The number of pixels between the mouse's screen position and the graphic's top left corner.
	 * @param	YOffset		The number of pixels between the mouse's screen position and the graphic's top left corner. 
	 */
	public void show(TextureRegion Graphic,float Scale,int XOffset,int YOffset)
	{
		//_cursorContainer.visible = true;
		//if(Graphic != null)
			//load(Graphic,Scale,XOffset,YOffset);
		//else if(_cursor == null)
			//load();
	}
	
	/**
	 * Either show an existing cursor or load a new one.
	 * 
	 * @param	Graphic		The image you want to use for the cursor.
	 * @param	Scale		Change the size of the cursor.  Default = 1, or native size.  2 = 2x as big, 0.5 = half size, etc.
	 * @param	XOffset		The number of pixels between the mouse's screen position and the graphic's top left corner.
	 */
	public void show(TextureRegion Graphic,float Scale,int XOffset)
	{
		show(Graphic, Scale, XOffset, 0);
	}
	
	/**
	 * Either show an existing cursor or load a new one.
	 * 
	 * @param	Graphic		The image you want to use for the cursor.
	 * @param	Scale		Change the size of the cursor.  Default = 1, or native size.  2 = 2x as big, 0.5 = half size, etc.
	 */
	public void show(TextureRegion Graphic,float Scale)
	{
		show(Graphic, Scale, 0, 0);
	}
	
	/**
	 * Either show an existing cursor or load a new one.
	 * 
	 * @param	Graphic		The image you want to use for the cursor.
	 */
	public void show(TextureRegion Graphic)
	{
		show(Graphic, 1, 0, 0);
	}
	
	/**
	 * Show the cursor.
	 */
	public void show()
	{
		show(null, 1, 0, 0);
	}
	
	/**
	 * Hides the mouse cursor
	 */
	public void hide()
	{
		//_cursorContainer.visible = false;
	}
	
	/**
	 * Read only, check visibility of mouse cursor.
	 */
	public boolean getVisible()
	{
		return false;//_cursorContainer.visible;
	}
	
	/**
	 * Load a new mouse cursor graphic
	 * 
	 * @param	Graphic		The image you want to use for the cursor.
	 * @param	Scale		Change the size of the cursor.
	 * @param	XOffset		The number of pixels between the mouse's screen position and the graphic's top left corner.
	 * @param	YOffset		The number of pixels between the mouse's screen position and the graphic's top left corner. 
	 */
	public void load(TextureRegion Graphic,float Scale,int XOffset,int YOffset)
	{
		/*
		if(_cursor != null)
			_cursorContainer.removeChild(_cursor);

		if(Graphic == null)
			Graphic = ImgDefaultCursor;
		_cursor = new Graphic();
		_cursor.x = XOffset;
		_cursor.y = YOffset;
		_cursor.scaleX = Scale;
		_cursor.scaleY = Scale;
		
		_cursorContainer.addChild(_cursor);
		*/
	}
	
	/**
	 * Load a new mouse cursor graphic
	 * 
	 * @param	Graphic		The image you want to use for the cursor.
	 * @param	Scale		Change the size of the cursor.
	 * @param	XOffset		The number of pixels between the mouse's screen position and the graphic's top left corner.
	 */
	public void load(TextureRegion Graphic,float Scale,int XOffset)
	{
		load(Graphic, Scale, XOffset, 0);
	}
	
	/**
	 * Load a new mouse cursor graphic
	 * 
	 * @param	Graphic		The image you want to use for the cursor.
	 * @param	Scale		Change the size of the cursor.
	 */
	public void load(TextureRegion Graphic,float Scale)
	{
		load(Graphic, Scale, 0, 0);
	}
	
	/**
	 * Load a new mouse cursor graphic
	 * 
	 * @param	Graphic		The image you want to use for the cursor.
	 */
	public void load(TextureRegion Graphic)
	{
		load(Graphic, 1, 0, 0);
	}
	
	/**
	 * Load a new mouse cursor graphic 
	 */
	public void load()
	{
		load(null, 1, 0, 0);
	}
	
	/**
	 * Unload the current cursor graphic.  If the current cursor is visible,
	 * then the default system cursor is loaded up to replace the old one.
	 */
	public void unload()
	{
		/*
		if(_cursor != null)
		{
			if(_cursorContainer.visible)
				load();
			else
			{
				_cursorContainer.removeChild(_cursor)
				_cursor = null;
			}
		}
		*/
	}
	
	/**
	 * Called by the internal game loop to update the pointers positions in the game world.
	 * Also updates the just pressed/just released flags.
	 */
	public void update()
	{	
		Pointer o;
		int i = 0;
		int l = _pointers.size;
		
		while (i < l)
		{
			o = _pointers.get(i);
			
			o.screenPosition.x = Gdx.input.getX(i) / FlxG.difWidth;
			o.screenPosition.y = Gdx.input.getY(i) / FlxG.difHeight;
			
			if((o.last == -1) && (o.current == -1))
				o.current = 0;
			else if((o.last == 2) && (o.current == 2))
				o.current = 1;
			o.last = o.current;
			
			++i;
		}

		updateCursor();
	}
	
	/**
	 * Internal function for helping to update the cursor graphic and world coordinates.
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
		screenX = (int) ((o.screenPosition.x - camera.x)/camera.getZoom());
		screenY = (int) ((o.screenPosition.y - camera.y)/camera.getZoom());
		x = screenX + camera.scroll.x;
		y = screenY + camera.scroll.y;
	}
	
	/**
	 * Fetch the world position of the specified pointer on any given camera.
	 *
	 * @param Pointer	The pointer id.
	 * @param Camera	If unspecified, first/main global camera is used instead.
	 * @param Point		An existing point object to store the results (if you don't want a new one created). 
	 *
	 * @return The pointer's location in world space.
	 */
	public FlxPoint getWorldPosition(int Pointer, FlxCamera Camera, FlxPoint Point)
	{
		if(Camera == null)
			Camera = FlxG.camera;
		if(Point == null)
			Point = new FlxPoint();
		getScreenPosition(Pointer,Camera,_point);
		Point.x = _point.x + Camera.scroll.x;
		Point.y = _point.y + Camera.scroll.y;
		
		return Point;
	}
	
	/**
	 * Fetch the world position of the specified pointer on any given camera.
	 * 
	 * @param Point		The pointer id.
	 * @param Camera	If unspecified, first/main global camera is used instead.
	 * 
	 * @return The pointer's location in world space.
	 */
	public FlxPoint getWorldPosition(int Pointer, FlxCamera Camera)
	{
		return getWorldPosition(Pointer, Camera, null);
	}
	
	/**
	 * Fetch the world position of the specified pointer.
	 * 
	 * @param Pointer	The pointer id.
	 * 
	 * @return The pointer's location in world space.
	 */
	public FlxPoint getWorldPosition(int Pointer)
	{
		return getWorldPosition(Pointer, null, null);
	}
	
	/**
	 * Fetch the world position of the first pointer on any given camera.
	 * NOTE: Mouse.x and Mouse.y also store the world position of this pointer on the main camera.
	 * 
	 * @param Camera	If unspecified, first/main global camera is used instead.
	 * @param Point		An existing point object to store the results (if you don't want a new one created). 
	 * 
	 * @return The pointer's location in world space.
	 */
	public FlxPoint getWorldPosition(FlxCamera Camera, FlxPoint Point)
	{
		return getWorldPosition(0, Camera, Point);
	}
	
	/**
	 * Fetch the world position of the first pointer on any given camera.
	 * NOTE: Mouse.x and Mouse.y also store the world position of this pointer on the main camera.
	 * 
	 * @param Camera	If unspecified, first/main global camera is used instead.
	 * 
	 * @return The pointer's location in world space.
	 */
	public FlxPoint getWorldPosition(FlxCamera Camera)
	{
		return getWorldPosition(0, Camera, null);
	}
	
	/**
	 * Fetch the world position of the first pointer on any given camera.
	 * NOTE: Mouse.x and Mouse.y also store the world position of this pointer on the main camera.
	 * 
	 * @return The pointer's location in world space.
	 */
	public FlxPoint getWorldPosition()
	{
		return getWorldPosition(0, null, null);
	}
	
	/**
	 * Fetch the screen position of the specified pointer on any given camera.
	 *
	 * @param Pointer	The pointer id.
	 * @param Camera	If unspecified, first/main global camera is used instead.
	 * @param Point		An existing point object to store the results (if you don't want a new one created). 
	 * 
	 * @return The pointer's location in screen space.
	 */
	public FlxPoint getScreenPosition(int Pointer, FlxCamera Camera, FlxPoint Point)
	{
		if(Camera == null)
			Camera = FlxG.camera;
		if(Point == null)
			Point = new FlxPoint();
		
		if (Pointer >= _pointers.size)
			return Point;
		
		Pointer o = _pointers.get(Pointer);
		
		Point.x = (o.screenPosition.x - Camera.x)/Camera.getZoom();
		Point.y = (o.screenPosition.y - Camera.y)/Camera.getZoom();
		
		return Point;
	}
	
	/**
	 * Fetch the screen position of the specified pointer on any given camera.
	 * 
	 * @param Pointer	The pointer id.
	 * @param Camera	If unspecified, first/main global camera is used instead.
	 * 
	 * @return The pointer's location in screen space.
	 */
	public FlxPoint getScreenPosition(int Pointer,FlxCamera Camera)
	{
		return getScreenPosition(Pointer, Camera, null);
	}
	
	/**
	 * Fetch the screen position of the specified pointer on the main camera.
	 * 
	 * @param Pointer	The pointer id.
	 * 
	 * @return The pointer's location in screen space.
	 */
	public FlxPoint getScreenPosition(int Pointer)
	{
		return getScreenPosition(Pointer, null, null);
	}
	
	/**
	 * Fetch the screen position of the first pointer on any given camera.
	 * NOTE: Mouse.screenX and Mouse.screenY also store the screen position of this pointer on the main camera.
	 * 
	 * @param Camera	If unspecified, first/main global camera is used instead.
	 * @param Point		An existing point object to store the results (if you don't want a new one created). 
	 * 
	 * @return The pointer's location in screen space.
	 */
	public FlxPoint getScreenPosition(FlxCamera Camera,FlxPoint Point)
	{
		return getScreenPosition(0, Camera, Point);
	}
	
	/**
	 * Fetch the screen position of the first pointer on any given camera.
	 * NOTE: Mouse.screenX and Mouse.screenY also store the screen position of this pointer on the main camera.
	 * 
	 * @param Camera	If unspecified, first/main global camera is used instead.
	 * 
	 * @return The pointer's location in screen space.
	 */
	public FlxPoint getScreenPosition(FlxCamera Camera)
	{
		return getScreenPosition(0, Camera, null);
	}
	
	/**
	 * Fetch the screen position of the first pointer on the main camera.
	 * NOTE: Mouse.screenX and Mouse.screenY also store the screen position of this pointer on the main camera.
	 * 
	 * @return The pointer's location in screen space.
	 */
	public FlxPoint getScreenPosition()
	{
		return getScreenPosition(0, null, null);
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
	 * @param Pointer	The pointer id.
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
	 * @param	Pointer	The pointer id.
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
	 * @param	Pointer	The pointer id.
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
	 * Event handler so FlxGame can update the pointer.
	 * 
	 * @param	X	The x position of the pointer.
	 * @param	Y	The y position of the pointer.
	 * @param	Pointer	The pointer id.
	 * @param	Button	Which mouse button was released.
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
	}
	
	
	/**
	 * Event handler so FlxGame can update the pointer.
	 * 
	 * @param	X	The x position of the pointer.
	 * @param	Y	The y position of the pointer.
	 * @param	Pointer	The pointer id.
	 * @param	Button	Which mouse button was pressed.
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
	}
	
	/**
	 * Event handler so FlxGame can update the pointer.
	 * 
	 * @param	Amount	The amount the wheel was scrolled.
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
	//TODO: This should record all pointers, not just the first one.
	public MouseRecord record()
	{
		Pointer o = _pointers.get(0);
		
		if((o.lastX == o.screenPosition.x) && (o.lastY == o.screenPosition.y) && (o.current == 0) && (_lastWheel == wheel))
			return null;
		o.lastX = (int) o.screenPosition.x;
		o.lastY = (int) o.screenPosition.y;
		_lastWheel = wheel;
		return new MouseRecord(o.lastX,o.lastY,o.current,_lastWheel);
	}
	
	/**
	 * Part of the keystroke recording system.
	 * Takes data about key presses and sets it into array.
	 * 
	 * @param	KeyStates	Array of data about key states.
	 */
	//TODO: This should play all pointers, not just the first one.
	public void playback(MouseRecord Record)
	{
		Pointer o = _pointers.get(0);
		
		o.current = Record.button;
		wheel = Record.wheel;
		o.screenPosition.x = Record.x;
		o.screenPosition.y = Record.y;
		updateCursor();
	}
	
	/**
	 * An internal helper class to store the state of the pointers in game.
	 */
	protected class Pointer
	{
		/**
		 * The current pressed state of the pointer.
		 */
		public int current;
		/**
		 * The last pressed state of the pointer.
		 */
		public int last;
		/**
		 * The current position of the pointer in screen space.
		 */
		public FlxPoint screenPosition;
		/**
		 * The last X position of the pointer in screen space.
		 */
		public int lastX;
		/**
		 * The last Y position of the pointer in screen space.
		 */
		public int lastY;
		
		public Pointer()
		{
			current = 0;
			last = 0;
			screenPosition = new FlxPoint();
			lastX = 0;
			lastY = 0;
		}
	}
}