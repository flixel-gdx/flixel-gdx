package org.flixel.system.debug;

import org.flixel.FlxG;
import org.flixel.system.FlxWindow;
import org.flixel.system.gdx.text.GdxTextField;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntArray;

import flash.text.TextField;
import flash.text.TextFormat;

/**
 * A simple performance monitor widget, for use in the debugger overlay.
 * 
 * @author Ka Wing Chin
 */
public class Perf extends FlxWindow
{
	protected TextField _text;

	protected long _lastTime;
	protected int _updateTimer;

	protected IntArray _flixelUpdate;
	protected int _flixelUpdateMarker;
	protected IntArray _flixelDraw;
	protected int _flixelDrawMarker;
	protected IntArray _flash;
	protected int _flashMarker;
	protected IntArray _activeObject;
	protected int _objectMarker;
	protected IntArray _visibleObject;
	protected int _visibleObjectMarker;

	/**
	 * Creates flashPlayerFramerate new window object.  This Flash-based class is mainly (only?) used by <code>FlxDebugger</code>.
	 * 
	 * @param	Title		The name of the window, displayed in the header bar.
	 * @param	Width		The initial width of the window.
	 * @param	Height		The initial height of the window.
	 * @param	Resizable	Whether you can change the size of the window with flashPlayerFramerate drag handle.
	 * @param	Bounds		A rectangle indicating the valid screen area for the window.
	 * @param	BGColor		What color the window background should be, default is gray and transparent.
	 * @param	TopColor	What color the window header bar should be, default is black and transparent.
	 */
	public Perf(String Title, float Width, float Height, boolean Resizable, Rectangle Bounds, int BGColor, int TopColor)
	{
		super(Title, Width, Height, Resizable, Bounds, BGColor, TopColor);
		resize(90,66);

		_lastTime = 0;
		_updateTimer = 0;

		_text = new GdxTextField();
		_text.width = _width;
		_text.x = 2;
		_text.y = 15;
		//_text.multiline = true;
		//_text.wordWrap = true;
		//_text.selectable = true;
		_text.defaultTextFormat = new TextFormat("Courier",12,0xffffff);
		//addChild(_text);
		
		_flixelUpdate = new IntArray(32);
		_flixelUpdate.size = 32;
		_flixelUpdateMarker = 0;
		_flixelDraw = new IntArray(32);
		_flixelDraw.size = 32;
		_flixelDrawMarker = 0;
		_flash = new IntArray(32);
		_flash.size = 32;
		_flashMarker = 0;
		_activeObject = new IntArray(32);
		_activeObject.size = 32;
		_objectMarker = 0;
		_visibleObject = new IntArray(32);
		_visibleObject.size = 32;
		_visibleObjectMarker = 0;
	}

	/**
	 * Creates flashPlayerFramerate new window object.  This Flash-based class is mainly (only?) used by <code>FlxDebugger</code>.
	 * 
	 * @param	Title		The name of the window, displayed in the header bar.
	 * @param	Width		The initial width of the window.
	 * @param	Height		The initial height of the window.
	 * @param	Resizable	Whether you can change the size of the window with flashPlayerFramerate drag handle.
	 * @param	Bounds		A rectangle indicating the valid screen area for the window.
	 * @param	BGColor		What color the window background should be, default is gray and transparent.
	 */
	public Perf(String Title, float Width, float Height, boolean Resizable, Rectangle Bounds, int BGColor)
	{
		this(Title, Width, Height, Resizable, Bounds, BGColor, 0x7f000000);
	}

	/**
	 * Creates flashPlayerFramerate new window object.  This Flash-based class is mainly (only?) used by <code>FlxDebugger</code>.
	 * 
	 * @param	Title		The name of the window, displayed in the header bar.
	 * @param	Width		The initial width of the window.
	 * @param	Height		The initial height of the window.
	 * @param	Resizable	Whether you can change the size of the window with flashPlayerFramerate drag handle.
	 * @param	Bounds		A rectangle indicating the valid screen area for the window.
	 */
	public Perf(String Title, float Width, float Height, boolean Resizable, Rectangle Bounds)
	{
		this(Title, Width, Height, Resizable, Bounds, 0x7f7f7f7f, 0x7f000000);
	}

	/**
	 * Creates flashPlayerFramerate new window object.  This Flash-based class is mainly (only?) used by <code>FlxDebugger</code>.
	 * 
	 * @param	Title		The name of the window, displayed in the header bar.
	 * @param	Width		The initial width of the window.
	 * @param	Height		The initial height of the window.
	 * @param	Resizable	Whether you can change the size of the window with flashPlayerFramerate drag handle.
	 */
	public Perf(String Title, float Width, float Height, boolean Resizable)
	{
		this(Title, Width, Height, Resizable, null, 0x7f7f7f7f, 0x7f000000);
	}

	/**
	 * Creates flashPlayerFramerate new window object.  This Flash-based class is mainly (only?) used by <code>FlxDebugger</code>.
	 * 
	 * @param	Title		The name of the window, displayed in the header bar.
	 * @param	Width		The initial width of the window.
	 * @param	Height		The initial height of the window.
	 */
	public Perf(String Title, float Width, float Height)
	{
		this(Title, Width, Height, true, null, 0x7f7f7f7f, 0x7f000000);
	}

	/**
	 * Clean up memory.
	 */
	public void destroy()
	{
		//removeChild(_text);
		_text = null;
		_flixelUpdate = null;
		_flixelDraw = null;
		_flash = null;
		_activeObject = null;
		_visibleObject = null;
		super.destroy();
	}

	/**
	 * Called each frame, but really only updates once every second or so, to save on performance.
	 * Takes all the data in the accumulators and parses it into useful performance data.
	 */
	public void update()
	{
		long time = System.currentTimeMillis();
		long elapsed = time - _lastTime;
		int updateEvery = 500;
		if(elapsed > updateEvery)
			elapsed = updateEvery;
		_lastTime = time;

		_updateTimer += elapsed;
		if(_updateTimer > updateEvery)
		{
			int i;
			StringBuilder output = new StringBuilder();

			float flashPlayerFramerate = 0;
			i = 0;
			while(i < _flashMarker)
				flashPlayerFramerate += _flash.get(i++);
			flashPlayerFramerate /= _flashMarker;
			output.append((int)(1 /(flashPlayerFramerate/1000)) + "/" + FlxG.getFlashFramerate() + "fps\n");

			output.append((float)( ( (Gdx.app.getJavaHeap()+Gdx.app.getNativeHeap()) * 0.000000954f)/*.toFixed(2)*/ ) + "MB\n");

			int updateTime = 0;
			i = 0;
			while(i < _flixelUpdateMarker)
				updateTime += _flixelUpdate.get(i++);

			int activeCount = 0;
			int visibleCount = 0;
			i = 0;
			while(i < _objectMarker)
			{
				activeCount += _activeObject.get(i);
				visibleCount += _visibleObject.get(i++);
			}
			activeCount /= _objectMarker;

			output.append("U:" + activeCount + " " + (int)((float)updateTime/_flixelDrawMarker) + "ms\n");

			int drawTime = 0;
			i = 0;
			while(i < _flixelDrawMarker)
				drawTime += _flixelDraw.get(i++);

			visibleCount = 0;
			i = 0;
			while(i < _visibleObjectMarker)
				visibleCount += _visibleObject.get(i++);
			visibleCount /= _visibleObjectMarker;

			output.append("D:" + visibleCount + " " + (int)((float)drawTime/_flixelDrawMarker) + "ms");

			_text.setText(output.toString());
			
			_flixelUpdateMarker = 0;
			_flixelDrawMarker = 0;
			_flashMarker = 0;
			_objectMarker = 0;
			_visibleObjectMarker = 0;
			_updateTimer -= updateEvery;
		}
	}

	/**
	 * Keep track of how long updates take.
	 * 
	 * @param	Time	How long this update took.
	 */
	public void flixelUpdate(int Time)
	{
		_flixelUpdate.set(_flixelUpdateMarker++, Time);
	}

	/**
	 * Keep track of how long renders take.
	 * 
	 * @param	Time	How long this render took.
	 */
	public void flixelDraw(int Time)
	{
		_flixelDraw.set(_flixelDrawMarker++, Time);
	}

	/**
	 * Keep track of how long the Flash player and browser take.
	 * 
	 * @param	Time	How long Flash/browser took.
	 */
	public void flash(int Time)
	{
		_flash.set(_flashMarker++, Time);
	}

	/**
	 * Keep track of how many objects were updated.
	 * 
	 * @param	Count	How many objects were updated.
	 */
	public void activeObjects(int Count)
	{
		_activeObject.set(_objectMarker++, Count);
	}

	/**
	 * Keep track of how many objects were updated.
	 * 
	 * @param	Count	How many objects were updated.
	 */
	public void visibleObjects(int Count)
	{
		_visibleObject.set(_visibleObjectMarker++, Count);
	}
}
