package org.flixel.system.debug;

import org.flixel.system.FlxWindow;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * A Visual Studio-style "watch" window, for use in the debugger overlay.
 * Track the values of any public variable in real-time, and/or edit their values on the fly.
 * 
 * @author Thomas Weston
 */
public class Watch extends FlxWindow
{
	static protected final int MAX_LOG_LINES = 1024;
	static protected final int LINE_HEIGHT = 15;

	/**
	 * Whether a watch entry is currently being edited or not.
	 */
	public boolean editing;

	//protected var _names:Sprite;
	//protected var _values:Sprite;
	protected Array<WatchEntry> _watching;

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
	public Watch(String Title, float Width, float Height, boolean Resizable, Rectangle Bounds, int BGColor, int TopColor)
	{
		super(Title, Width, Height, Resizable, Bounds, BGColor, TopColor);

		//_names = new Sprite();
		//_names.x = 2;
		//_names.y = 15;
		//addChild(_names);

		//_values = new Sprite();
		//_values.x = 2;
		//_values.y = 15;
		//addChild(_values);

		_watching = new Array<WatchEntry>(true, 16);

		editing = false;

		removeAll();
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
	public Watch(String Title, float Width, float Height, boolean Resizable, Rectangle Bounds, int BGColor)
	{
		this(Title, Width, Height, Resizable, Bounds, BGColor, 0x7f000000);
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
	public Watch(String Title, float Width, float Height, boolean Resizable, Rectangle Bounds)
	{
		this(Title, Width, Height, Resizable, Bounds, 0x7f7f7f7f, 0x7f000000);
	}

	/**
	 * Creates a new window object.  This Flash-based class is mainly (only?) used by <code>FlxDebugger</code>.
	 * 
	 * @param	Title		The name of the window, displayed in the header bar.
	 * @param	Width		The initial width of the window.
	 * @param	Height		The initial height of the window.
	 * @param	Resizable	Whether you can change the size of the window with a drag handle.
	 */
	public Watch(String Title, float Width, float Height, boolean Resizable)
	{
		this(Title, Width, Height, Resizable, null, 0x7f7f7f7f, 0x7f000000);
	}

	/**
	 * Creates a new window object.  This Flash-based class is mainly (only?) used by <code>FlxDebugger</code>.
	 * 
	 * @param	Title		The name of the window, displayed in the header bar.
	 * @param	Width		The initial width of the window.
	 * @param	Height		The initial height of the window.
	 */
	public Watch(String Title, float Width, float Height)
	{
		this(Title, Width, Height, true, null, 0x7f7f7f7f, 0x7f000000);
	}

	/**
	 * Clean up memory.
	 */
	@Override
	public void destroy()
	{
		//removeChild(_names);
		//_names = null;
		//removeChild(_values);
		//_values = null;
		int i = 0;
		int l = _watching.size;
		while(i < l)
			_watching.get(i++).destroy();
		_watching = null;
		super.destroy();
	}

	/**
	 * Add a new variable to the watch window.
	 * Has some simple code in place to prevent
	 * accidentally watching the same variable twice.
	 * 
	 * @param	AnyObject		The <code>Object</code> containing the variable you want to track, e.g. this or Player.velocity.
	 * @param	VariableName	The <code>String</code> name of the variable you want to track, e.g. "width" or "x".
	 * @param	DisplayName		Optional <code>String</code> that can be displayed in the watch window instead of the basic class-name information.
	 */
	public void add(Object AnyObject,String VariableName,String DisplayName)
	{
		//Don't add repeats
		WatchEntry watchEntry;
		int i = 0;
		int l = _watching.size;
		while(i < l)
		{
			watchEntry = _watching.get(i++);
			if((watchEntry.object == AnyObject) && (watchEntry.field == VariableName))
				return;
		}

		//Good, no repeats, add away!
		watchEntry = new WatchEntry(_watching.size*LINE_HEIGHT,_width/2,_width/2-10,AnyObject,VariableName,DisplayName);
		//_names.addChild(watchEntry.nameDisplay);
		//_values.addChild(watchEntry.valueDisplay);
		_watching.add(watchEntry);
	}

	/**
	 * Add a new variable to the watch window.
	 * Has some simple code in place to prevent
	 * accidentally watching the same variable twice.
	 * 
	 * @param	AnyObject		The <code>Object</code> containing the variable you want to track, e.g. this or Player.velocity.
	 * @param	VariableName	The <code>String</code> name of the variable you want to track, e.g. "width" or "x".
	 */
	public void add(Object AnyObject,String VariableName)
	{
		add(AnyObject,VariableName,null);
	}

	/**
	 * Remove a variable from the watch window.
	 * 
	 * @param	AnyObject		The <code>Object</code> containing the variable you want to remove, e.g. this or Player.velocity.
	 * @param	VariableName	The <code>String</code> name of the variable you want to remove, e.g. "width" or "x".  If left null, this will remove all variables of that object.
	 */
	public void remove(Object AnyObject,String VariableName)
	{
		//splice out the requested object
		WatchEntry watchEntry;
		int i = _watching.size-1;
		while(i >= 0)
		{
			watchEntry = _watching.get(i);
			if((watchEntry.object == AnyObject) && ((VariableName == null) || (watchEntry.field == VariableName)))
			{
				_watching.removeIndex(i);
				//_names.removeChild(watchEntry.nameDisplay);
				//_values.removeChild(watchEntry.valueDisplay);
				watchEntry.destroy();
			}
			i--;
		}
		watchEntry = null;

		//reset the display heights of the remaining objects
		i = 0;
		int l = _watching.size;
		while(i < l)
		{
			_watching.get(i).setY(i*LINE_HEIGHT);
			i++;
		}
	}

	/**
	 * Remove a variable from the watch window.
	 * 
	 * @param	AnyObject		The <code>Object</code> containing the variable you want to remove, e.g. this or Player.velocity.
	 */
	public void remove(Object AnyObject)
	{
		remove(AnyObject, null);
	}

	/**
	 * Remove everything from the watch window.
	 */
	public void removeAll()
	{
		WatchEntry watchEntry;
		int i = 0;
		int l = _watching.size;
		while(i < l)
		{
			watchEntry = _watching.pop();
			//_names.removeChild(watchEntry.nameDisplay);
			//_values.removeChild(watchEntry.valueDisplay);
			watchEntry.destroy();
			i++;
		}
		_watching.size = 0;
	}

	/**
	 * Update all the entries in the watch window.
	 */
	public void update()
	{
		editing = false;
		int i = 0;
		int l = _watching.size;
		while(i < l)
		{
			if(!_watching.get(i++).updateValue())
				editing = true;
		}
	}

	/**
	 * Force any watch entries currently being edited to submit their changes.
	 */
	public void submit()
	{
		int i = 0;
		int l = _watching.size;
		WatchEntry watchEntry;
		while(i < l)
		{
			watchEntry = _watching.get(i++);
			if(watchEntry.editing)
				watchEntry.submit();
		}
		editing = false;
	}

	/**
	 * Update the Flash shapes to match the new size, and reposition the header, shadow, and handle accordingly.
	 * Also adjusts the width of the entries and stuff, and makes sure there is room for all the entries.
	 */
	@Override
	protected void updateSize()
	{
		if(_height < _watching.size*LINE_HEIGHT + 17)
			_height = _watching.size*LINE_HEIGHT + 17;

		super.updateSize();

		//_values.x = _width/2 + 2;

		int i = 0;
		int l = _watching.size;
		while(i < l)
			_watching.get(i++).updateWidth(_width/2, _width/2-10);
	}
}
