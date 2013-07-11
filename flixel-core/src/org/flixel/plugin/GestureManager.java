
package org.flixel.plugin;

import org.flixel.FlxBasic;
import org.flixel.FlxGesture;

import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class GestureManager extends FlxBasic implements GestureListener {

	protected Array<FlxGesture> _gestures;
	private boolean afterLongPress = false;

	public GestureManager () {
		_gestures = new Array<FlxGesture>();
		visible = false;
	}

	@Override
	public void destroy () {
		clear();
		_gestures = null;
		super.destroy();
	}

	public void add (FlxGesture Gesture) {
		_gestures.add(Gesture);
	}

	public void remove (FlxGesture Gesture) {
		int index = _gestures.indexOf(Gesture, true);
		if (index >= 0) _gestures.removeIndex(index);
	}

	public void clear () {
		int i = _gestures.size - 1;
		FlxGesture gesture;
		while (i >= 0) {
			gesture = _gestures.get(i--);
			if (gesture != null) gesture.destroy();
		}
		_gestures.clear();
	}

	private void updateGestures (int Gesture) {
		int i = _gestures.size - 1;
		FlxGesture gesture;
		while (i >= 0) {
			gesture = _gestures.get(i--);
			if (gesture != null) gesture.callback(Gesture);
		}
	}

	@Override
	public boolean touchDown (float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap (float x, float y, int count, int button) {
		if (afterLongPress)
			afterLongPress = false;
		else if (count == 1)
			updateGestures(FlxGesture.TAP);
		else if (count == 2) updateGestures(FlxGesture.DOUBLE_TAP);
		return false;
	}

	@Override
	public boolean longPress (float x, float y) {
		updateGestures(FlxGesture.LONG_PRESS);
		afterLongPress = true;
		return false;
	}

	@Override
	public boolean fling (float velocityX, float velocityY, int button) {
		if (Math.abs(velocityX) > Math.abs(velocityY)) {
			if (velocityX > 0) {
				updateGestures(FlxGesture.DIRECTION_RIGHT);
			} else {
				updateGestures(FlxGesture.DIRECTION_LEFT);
			}
		} else {
			if (velocityY > 0) {
				updateGestures(FlxGesture.DIRECTION_DOWN);
			} else {
				updateGestures(FlxGesture.DIRECTION_UP);
			}
		}
		return false;
	}

	@Override
	public boolean pan (float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom (float originalDistance, float currentDistance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch (Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

}
