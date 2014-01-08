package org.flixel.plugin;

import org.flixel.FlxBasic;
import org.flixel.FlxG;
import org.flixel.FlxGesture;
import org.flixel.system.gdx.GdxStage;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * A simple manager for tracking and updating gesture objects.
 */
public class GestureManager extends FlxBasic implements GestureListener
{
	/**
	 * An array that holds <code>FlxGestures</code> objects.
	 */
	protected Array<FlxGesture> _gestures;	
	/**
	 * Internal tracker for long press gesture.
	 */
	private boolean afterLongPress = false;
	/**
	 * Holds the gesture data.
	 */
	protected GestureData data;

	/**
	 * Creates a new <code>GestureManager<code> object.
	 */
	public GestureManager()
	{
		_gestures = new Array<FlxGesture>();
		visible = false;
		data = new GestureData();
		((GdxStage)FlxG.getStage()).getInput().addProcessor(new GestureDetector(this));
	}

	@Override
	public void destroy()
	{
		super.destroy();
		clear();
		_gestures = null;
		data.destroy();
		data = null;
	}

	/**
	 * Add a <code>FlxGesture</code> object.
	 * @param Gesture	The object that will be added.
	 */
	public void add(FlxGesture Gesture)
	{
		_gestures.add(Gesture);
	}

	/**
	 * Remove a <code>FlxGesture</code> object.
	 * @param Gesture	The object that will be removed.
	 */
	public void remove(FlxGesture Gesture)
	{
		int index = _gestures.indexOf(Gesture, true);
		if(index >= 0)
			_gestures.removeIndex(index);
	}

	/**
	 * Remove all gestures from the manager.
	 */
	public void clear()
	{
		int i = _gestures.size - 1;
		FlxGesture gesture;
		while(i >= 0)
		{
			gesture = _gestures.get(i--);
			if(gesture != null)
				gesture.destroy();
		}
		_gestures.clear();
	}
	
	/**
	 * Update gestures by gesture type, e.g. <code>FlxGesture.PAN, FlxGesture.DIRECTION_DOWN, etc.</code>.
	 * @param Gesture	The gesture type that needs to be updated.
	 */
	private void updateGestures(int Gesture, GestureData data)
	{
		int i = _gestures.size - 1;
		FlxGesture gesture;
		while(i >= 0)
		{
			gesture = _gestures.get(i--);
			if(gesture != null)
				gesture.callback(Gesture, data);
		}
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button)
	{
		data.clear();
		data.x = x;
		data.y = y;
		data.pointer = pointer;
		data.button = button;
		updateGestures(FlxGesture.TOUCH_DOWN, data);
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button)
	{
		if(afterLongPress)
			afterLongPress = false;
		else
		{
			data.clear();
			data.x = x;
			data.y = y;
			data.button = button;
			if(count == 1)
				updateGestures(FlxGesture.TAP, data);
			else if(count == 2)
				updateGestures(FlxGesture.DOUBLE_TAP, data);
		}
		return false;
	}

	@Override
	public boolean longPress(float x, float y)
	{
		data.clear();
		data.x = x;
		data.y = y;
		updateGestures(FlxGesture.LONG_PRESS, data);
		afterLongPress = true;
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button)
	{
		data.clear();
		data.velocityX = velocityX;
		data.velocityY = velocityY;
		data.button = button;
		if(Math.abs(velocityX) > Math.abs(velocityY))
		{
			if(velocityX > 0)
				updateGestures(FlxGesture.DIRECTION_RIGHT, data);
			else
				updateGestures(FlxGesture.DIRECTION_LEFT, data);
		}
		else
		{
			if(velocityY > 0)
				updateGestures(FlxGesture.DIRECTION_DOWN, data);
			else
				updateGestures(FlxGesture.DIRECTION_UP, data);
		}
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY)
	{
		data.clear();
		data.x = x;
		data.y = y;
		data.deltaX = deltaX;
		data.deltaY = deltaY;
		updateGestures(FlxGesture.PAN, data);
		return false;
	}
	
	@Override
	public boolean panStop(float x, float y, int pointer, int button)
	{
		data.clear();
		data.x = x;
		data.y = y;
		data.pointer = pointer;
		data.button = button;
		return false;
	}

	@Override
	public boolean zoom(float originalDistance, float currentDistance)
	{
		data.clear();
		data.originalDistance = originalDistance;
		data.currentDistance = currentDistance;
		updateGestures(FlxGesture.ZOOM, data);
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2)
	{
		data.clear();
		data.initialPointer1 = initialPointer1;
		data.initialPointer2 = initialPointer2;
		data.pointer1 = pointer1;
		data.pointer2 = pointer2;
		updateGestures(FlxGesture.PINCH, data);
		return false;
	}
	
	/**
	 * Holds the gesture data
	 */
	public class GestureData
	{
		/**
		 * The x position where the gesture is performed.
		 */
		public float x;
		/**
		 * The y position where the gesture is performed.
		 */
		public float y;
		/**
		 * The x velocity in seconds. Used by FLING.
		 */
		public float velocityX;
		/**
		 * The y velocity in seconds. Used by FLING.
		 */
		public float velocityY;
		/**
		 * The difference in pixels to the last drag event on x. Used by PAN.
		 */
		public float deltaX;
		/**
		 * The difference in pixels to the last drag event on Y. Used by PAN.
		 */
		public float deltaY;
		/**
		 * The distance between fingers when the gesture started. Used by ZOOM.
		 */
		public float originalDistance;
		/**
		 * The current distance between fingers. Used by ZOOM.
		 */
		public float currentDistance;
		/**
		 * The first initial positions of the two involved fingers. Used by PINCH.
		 */
		public Vector2 initialPointer1;
		/**
		 * The second initial positions of the two involved fingers. Used by PINCH.
		 */
		public Vector2 initialPointer2;
		/**
		 * The first current position of the two involved fingers. Used by PINCH.
		 */
		public Vector2 pointer1;
		/**
		 * The second current position of the two involved fingers. Used by PINCH.
		 */
		public Vector2 pointer2;
		/**
		 * Tracks the pointer which is used.
		 */
		public int pointer;
		/**
		 * Tracks the mouse button which is used.
		 */
		public int button;
		
		/**
		 * Creates a new <code>GestureData</code> object.
		 */
		public GestureData()
		{
			initialPointer1 = new Vector2();
			initialPointer2 = new Vector2();
			pointer1 = new Vector2();
			pointer2 = new Vector2();
		}
		
		/**
		 * Reset the values.
		 */
		public void clear()
		{
			x = y = velocityX = velocityY = deltaX = deltaY =  originalDistance = currentDistance = 0;
			pointer = 0;
			button = 0;
			initialPointer1.set(0, 0);
			initialPointer2.set(0, 0);
			pointer1.set(0, 0);
			pointer2.set(0, 0);
		}
		
		/**
		 * Free the memory.
		 */
		public void destroy()
		{
			initialPointer1 = null;
			initialPointer2 = null;
			pointer1 = null;
			pointer2 = null;
		}
	}
}