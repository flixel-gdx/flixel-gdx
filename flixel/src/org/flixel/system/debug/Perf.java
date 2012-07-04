package org.flixel.system.debug;

import java.text.DecimalFormat;

import org.flixel.FlxG;
import org.flixel.FlxU;
import org.flixel.data.SystemAsset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.IntArray;


/**
 * A simple performance monitor widget, for use in the debugger overlay.
 * 
 * @author Ka Wing Chin
 */
public class Perf
{
	protected BitmapFont _text;

	protected int _lastTime;
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

	private long _startTime;
	public String output;

	/**
	 * Creates flashPlayerFramerate new window object.  This Flash-based class is mainly (only?) used by <code>FlxDebugger</code>.
	 */ 
	public Perf()
	{
		_startTime = System.currentTimeMillis();
		
		_lastTime = 0;
		_updateTimer = 0;
		
		_text = SystemAsset.system;
		
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
	 * Clean up memory.
	 */
	public void destroy()
	{
		_text = null;
		_flixelUpdate = null;
		_flixelDraw = null;
		_flash = null;
		_activeObject = null;
		_visibleObject = null;
	}
	
	
	/**
	 * Called each frame, but really only updates once every second or so, to save on performance.
	 * Takes all the data in the accumulators and parses it into useful performance data.
	 */
	public void update()
	{	
		int time = (int) (System.currentTimeMillis() - _startTime);
		int elapsed = time - _lastTime;
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
			while (i < _flashMarker)
				flashPlayerFramerate += _flash.get(i++);
			flashPlayerFramerate /= _flashMarker;
			output.append((int)(1/(flashPlayerFramerate/1000)) + "/" + FlxG.getFramerate() + "fps\n"); //TODO: FlashFramerate is 0 :), changed to getFramerate().
		
			DecimalFormat twoDForm = new DecimalFormat("#.##");
//			output.append((Gdx.app.getJavaHeap() * 0.000000954) + "MB\n"); // TODO: 0.000000954...
			output.append(twoDForm.format((Gdx.app.getNativeHeap() * 0.000000954)) + "MB\n"); // TODO: 0.000000954...
	
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
			if(_objectMarker != 0)
				activeCount /= _objectMarker;
			
			output.append("U:" + activeCount + " " + (updateTime/_flixelDrawMarker) + "ms\n");
			
			int drawTime = 0;
			i = 0;
			while(i < _flixelDrawMarker)
				drawTime += _flixelDraw.get(i++);
			
			visibleCount = 0;
			i = 0;
			while(i < _visibleObjectMarker)
				visibleCount += _visibleObject.get(i++);
			visibleCount /= _visibleObjectMarker;
	
			output.append("D:" + visibleCount + " " + (drawTime/_flixelDrawMarker) + "ms");
			
			//_text.text = output; can't draw here now.
			this.output = output.toString();
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
	 * @param Time	How long this update took.
	 */
	public void flixelUpdate(int Time)
	{
		if(_flixelUpdate.size <= _flixelUpdateMarker)
			return;
		_flixelUpdate.set(_flixelUpdateMarker++, Time);
	}
	
	
	/**
	 * Keep track of how long renders take.
	 * 
	 * @param	Time	How long this render took.
	 */
	public void flixelDraw(int Time)
	{
		if(_flixelDraw.size <= _flixelDrawMarker)
			return;
		_flixelDraw.set(_flixelDrawMarker++, Time);
	}
	
	
	/**
	 * Keep track of how long the Flash player and browser take.
	 * 
	 * @param Time	How long Flash/browser took.
	 */
	public void flash(int Time)
	{
		if(_flash.size <= _flashMarker)
			return;
		_flash.set(_flashMarker++, Time);
	}
	
	
	/**
	 * Keep track of how many objects were updated.
	 * 
	 * @param Count	How many objects were updated.
	 */
	public void activeObjects(int Count)
	{
		if(_activeObject.size <= _objectMarker)
			return;
		_activeObject.set(_objectMarker++, Count);
	}
	
	
	/**
	 * Keep track of how many objects were updated.
	 *  
	 * @param Count	How many objects were updated.
	 */
	public void visibleObjects(int Count)
	{
		if(_visibleObject.size <= _visibleObjectMarker)
			return;
		_visibleObject.set(_visibleObjectMarker++, Count);
	}


	public void draw()
	{
		if(output != null)
		{
			ShapeRenderer flashGfx = FlxG.flashGfx;
			flashGfx.setProjectionMatrix(FlxG.camera.glCamera.combined);
			flashGfx.begin(ShapeType.FilledRectangle);
			flashGfx.setColor(FlxU.colorFromHex(0x22222222));
			flashGfx.filledRect(FlxG.width-60, 0, 60, 50);
			flashGfx.end();
			FlxG.batch.begin();
			_text.drawMultiLine(FlxG.batch, output, FlxG.width-55, 5);
			FlxG.batch.end();
		}
	}
}
