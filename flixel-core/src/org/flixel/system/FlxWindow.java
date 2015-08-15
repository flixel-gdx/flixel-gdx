package org.flixel.system;

import org.flixel.FlxU;
import org.flixel.system.gdx.text.GdxTextField;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import flash.events.Event;
import flash.events.MouseEvent;
import flash.text.TextField;
import flash.text.TextFormat;

/**
 * A generic, Flash-based window class, created for use in <code>FlxDebugger</code>.
 * 
 * @author Thomas Weston
 */
public class FlxWindow
{
	protected String ImgHandle = "org/flixel/data/pack:handle";

	/**
	 * Minimum allowed X and Y dimensions for this window.
	 */
	public Vector2 minSize;
	/**
	 * Maximum allowed X and Y dimensions for this window.
	 */
	public Vector2 maxSize;

	/**
	 * Width of the window.  Using Sprite.width is super unreliable for some reason!
	 */
	protected int _width;
	/**
	 * Height of the window.  Using Sprite.height is super unreliable for some reason!
	 */
	protected int _height;
	/**
	 * Controls where the window is allowed to be positioned.
	 */
	protected Rectangle _bounds;

	/**
	 * Window display element.
	 */
	// protected var _background:Bitmap;
	/**
	 * Window display element.
	 */
	// protected var _header:Bitmap;
	/**
	 * Window display element.
	 */
	// protected var _shadow:Bitmap;
	/**
	 * Window display element.
	 */
	protected TextField _title;
	/**
	 * Window display element.
	 */
	// protected var _handle:Bitmap;

	/**
	 * Helper for interaction.
	 */
	protected boolean _overHeader;
	/**
	 * Helper for interaction.
	 */
	protected boolean _overHandle;
	/**
	 * Helper for interaction.
	 */
	protected Vector2 _drag;
	/**
	 * Helper for interaction.
	 */
	protected boolean _dragging;
	/**
	 * Helper for interaction.
	 */
	protected boolean _resizing;
	/**
	 * Helper for interaction.
	 */
	protected boolean _resizable;

	/**
	 * Creates a new window object.  This Flash-based class is mainly (only?) used by <code>FlxDebugger</code>.
	 * 
	 * @param	Title		The name of the window, displayed in the header bar.
	 * @param	Width		The initial width of the window.
	 * @param	Height		The initial height of the window.
	 * @param	Resizable	Whether you can change the size of the window with a drag handle.
	 * @param	Bounds		A rectangle indicating the valid screen area for the window.
	 * @param	BGColor		What color the window background should be, default is gray and transparent.
	 * @param	TopColor	What color the window header bar should be, default is black and transparent.
	 */
	public FlxWindow(String Title,float Width,float Height,boolean Resizable,Rectangle Bounds,int BGColor,int TopColor)
	{
		super();
		_width = (int)Width;
		_height = (int)Height;
		_bounds = Bounds;
		minSize = new Vector2(50,30);
		if(_bounds != null)
			maxSize = new Vector2(_bounds.width,_bounds.height);
		else
			maxSize = new Vector2(Float.MAX_VALUE,Float.MAX_VALUE);
		_drag = new Vector2();
		_resizable = Resizable;

		//_shadow = new Bitmap(new BitmapData(1,2,true,0xff000000));
		//addChild(_shadow);
		//_background = new Bitmap(new BitmapData(1,1,true,BGColor));
		//_background.y = 15;
		//addChild(_background);
		//_header = new Bitmap(new BitmapData(1,15,true,TopColor));
		//addChild(_header);

		_title = new GdxTextField();
		_title.x = 2;
		_title.height = 16;
		//_title.selectable = false;
		//_title.multiline = false;
		_title.defaultTextFormat = new TextFormat("Courier",12,0xffffff);
		_title.setText(Title);
		//addChild(_title);

		if(_resizable)
		{
			//_handle = new ImgHandle();
			//addChild(_handle);
		}

		if((_width != 0) || (_height != 0))
			updateSize();
		bound();

		// addEventListener(Event.ENTER_FRAME,init);
	}

	/**
	 * Creates a new window object.  This Flash-based class is mainly (only?) used by <code>FlxDebugger</code>.
	 * 
	 * @param	Title		The name of the window, displayed in the header bar.
	 * @param	Width		The initial width of the window.
	 * @param	Height		The initial height of the window.
	 * @param	Resizable	Whether you can change the size of the window with a drag handle.
	 * @param	Bounds		A rectangle indicating the valid screen area for the window.
	 * @param	BGColor		What color the window background should be, default is gray and transparent.
	 */
	public FlxWindow(String Title,float Width,float Height,boolean Resizable,Rectangle Bounds,int BGColor)
	{
		this(Title,Width,Height,Resizable,Bounds,BGColor,0x7f000000);
	}

	/**
	 * Creates a new window object.  This Flash-based class is mainly (only?) used by <code>FlxDebugger</code>.
	 * 
	 * @param	Title		The name of the window, displayed in the header bar.
	 * @param	Width		The initial width of the window.
	 * @param	Height		The initial height of the window.
	 * @param	Resizable	Whether you can change the size of the window with a drag handle.
	 * @param	Bounds		A rectangle indicating the valid screen area for the window.
	 */
	public FlxWindow(String Title,float Width,float Height,boolean Resizable,Rectangle Bounds)
	{
		this(Title,Width,Height,Resizable,Bounds,0x7f7f7f7f,0x7f000000);
	}

	/**
	 * Creates a new window object.  This Flash-based class is mainly (only?) used by <code>FlxDebugger</code>.
	 * 
	 * @param	Title		The name of the window, displayed in the header bar.
	 * @param	Width		The initial width of the window.
	 * @param	Height		The initial height of the window.
	 * @param	Resizable	Whether you can change the size of the window with a drag handle.
	 */
	public FlxWindow(String Title,float Width,float Height,boolean Resizable)
	{
		this(Title,Width,Height,Resizable,null,0x7f7f7f7f,0x7f000000);
	}

	/**
	 * Creates a new window object.  This Flash-based class is mainly (only?) used by <code>FlxDebugger</code>.
	 * 
	 * @param	Title		The name of the window, displayed in the header bar.
	 * @param	Width		The initial width of the window.
	 * @param	Height		The initial height of the window.
	 */
	public FlxWindow(String Title,float Width,float Height)
	{
		this(Title,Width,Height,true,null,0x7f7f7f7f,0x7f000000);
	}

	/**
	 * Clean up memory.
	 */
	public void destroy()
	{
		minSize = null;
		maxSize = null;
		_bounds = null;
		//removeChild(_shadow);
		//_shadow = null;
		//removeChild(_background);
		//_background = null;
		//removeChild(_header);
		//_header = null;
		//removeChild(_title);
		_title = null;
		//if(_handle != null)
		//	removeChild(_handle);
		//handle = null;
		_drag = null;
	}

	/**
	 * Resize the window.  Subject to pre-specified minimums, maximums, and bounding rectangles.
	 * 
	 * @param	Width		How wide to make the window.
	 * @param	Height		How tall to make the window.
	 */
	public void resize(float Width,float Height)
	{
		_width = (int)Width;
		_height = (int)Height;
		updateSize();
	}

	/**
	 * Change the position of the window.  Subject to pre-specified bounding rectangles.
	 * 
	 * @param	X		Desired X position of top left corner of the window.
	 * @param	Y		Desired Y position of top left corner of the window.
	 */
	public void reposition(float X, float Y)
	{
		//x = X;
		//y = Y;
		bound();
	}

	//***EVENT HANDLERS***//

	/**
	 * Used to set up basic mouse listeners.
	 * 
	 * @param	E		Flash event.
	 */
	protected void init(Event E)
	{
		//if(root == null)
		//	return;
		//removeEventListener(Event.ENTER_FRAME,init);

		//stage.addEventListener(MouseEvent.MOUSE_MOVE,onMouseMove);
		//stage.addEventListener(MouseEvent.MOUSE_DOWN,onMouseDown);
		//stage.addEventListener(MouseEvent.MOUSE_UP,onMouseUp);
	}

	/**
	 * Mouse movement handler.  Figures out if mouse is over handle or header bar or what.
	 * 
	 * @param	E		Flash mouse event.
	 */
	protected void onMouseMove(MouseEvent E)
	{
		if(_dragging) //user is moving the window around
		{
			_overHeader = true;
			// reposition(parent.mouseX - _drag.x, parent.mouseY - _drag.y);
		}
		else if(_resizing)
		{
			_overHandle = true;
			//resize(mouseX - _drag.x, mouseY - _drag.y);
		}
		//else if((mouseX >= 0) && (mouseX <= _width) && (mouseY >= 0) &&
		//(mouseY <= _height))
		//{ //not dragging, mouse is over the window
		//_overHeader = (mouseX <= _header.width) && (mouseY <=
		//_header.height);
		//if(_resizable)
		//_overHandle = (mouseX >= _width - _handle.width) && (mouseY >=
		//_height - _handle.height);
		//}
		else
		{
			//not dragging, mouse is NOT over window
			_overHandle = _overHeader = false;
		}
		
		updateGUI();
	}

	/**
	 * Figure out if window is being repositioned (clicked on header) or resized (clicked on handle).
	 * 
	 * @param	E		Flash mouse event.
	 */
	protected void onMouseDown(MouseEvent E)
	{
		if(_overHeader)
		{
			_dragging = true;
			//_drag.x = mouseX;
			//_drag.y = mouseY;
		}
		else if(_overHandle)
		{
			_resizing = true;
			//_drag.x = _width-mouseX;
			//_drag.y = _height-mouseY;
		}
	}

	/**
	 * User let go of header bar or handler (or nothing), so turn off drag and resize behaviors.
	 * 
	 * @param	E		Flash mouse event.
	 */
	protected void onMouseUp(MouseEvent E)
	{
		_dragging = false;
		_resizing = false;
	}

	//***MISC GUI MGMT STUFF***//

	/**
	 * Keep the window within the pre-specified bounding rectangle.
	 */
	protected void bound()
	{
		if(_bounds != null)
		{
			//x = FlxU.bound(x,_bounds.left,_bounds.right-_width);
			//y = FlxU.bound(y,_bounds.top,_bounds.bottom-_height);
		}
	}

	/**
	 * Update the Flash shapes to match the new size, and reposition the header, shadow, and handle accordingly.
	 */
	protected void updateSize()
	{
		_width = (int)FlxU.bound(_width,minSize.x,maxSize.x);
		_height = (int)FlxU.bound(_height,minSize.y,maxSize.y);

		//_header.scaleX = _width;
		//_background.scaleX = _width;
		//_background.scaleY = _height-15;
		//_shadow.scaleX = _width;
		//_shadow.y = _height;
		_title.width = _width-4;
		if(_resizable)
		{
			//_handle.x = _width-_handle.width;
			//_handle.y = _height-_handle.height;
		}
	}

	/**
	 * Figure out if the header or handle are highlighted.
	 */
	protected void updateGUI()
	{
		if(_overHeader || _overHandle)
		{
			if(_title.alpha != 1.0f)
				_title.alpha = 1.0f;
		}
		else
		{
			if(_title.alpha != 0.65f)
				_title.alpha = 0.65f;
		}
	}
}
