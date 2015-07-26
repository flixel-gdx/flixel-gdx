package org.flixel.system.gdx;

import org.flixel.system.gdx.utils.KeyboardEventPool;
import org.flixel.system.gdx.utils.MouseEventPool;
import org.flixel.system.gdx.utils.TouchEventPool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.IntIntMap;

import flash.events.IEventDispatcher;
import flash.events.KeyboardEvent;
import flash.events.MouseEvent;
import flash.events.TouchEvent;
import flash.ui.Keyboard;

/**
 * Converts libgdx input to Flash events.
 * 
 * @author Thomas Weston
 */
public class GdxInput extends InputMultiplexer implements InputProcessor
{
	/**
	 * Maps libgdx key codes to their Flash equivalents.
	 */
	private final IntIntMap _map;

	private IEventDispatcher _eventDispatcher;

	private KeyboardEventPool _keyboardEvents;
	private MouseEventPool _mouseEvents;
	private TouchEventPool _touchEvents;
	
	/**
	 * Cache the keycode and pass this in the <code>KEY_TYPED</code> event.
	 */
	private int _keyCode;

	public GdxInput(IEventDispatcher eventDispatcher)
	{
		_eventDispatcher = eventDispatcher;

		_keyboardEvents = new KeyboardEventPool(8);
		_mouseEvents = new MouseEventPool(8);
		_touchEvents = new TouchEventPool(8);

		_map = new IntIntMap(150);

		_map.put(Keys.A, Keyboard.A);
		_map.put(Keys.B, Keyboard.B);
		_map.put(Keys.BACK, Keyboard.BACK);
		//_map.put(Keys.BACKQUOTE, Keyboard.BACKQUOTE);
		_map.put(Keys.BACKSLASH, Keyboard.BACKSLASH);
		_map.put(Keys.BACKSPACE, Keyboard.BACKSPACE);
		_map.put(Keys.C, Keyboard.C);
		//_map.put(Keys.CAPS_LOCK, Keyboard.CAPS_LOCK);
		_map.put(Keys.COMMA, Keyboard.COMMA);
		_map.put(Keys.CONTROL_LEFT, Keyboard.CONTROL);
		_map.put(Keys.CONTROL_RIGHT, Keyboard.CONTROL);
		_map.put(Keys.D, Keyboard.D);
		_map.put(Keys.DEL, Keyboard.DELETE);
		_map.put(Keys.DOWN, Keyboard.DOWN);
		_map.put(Keys.E, Keyboard.E);
		_map.put(Keys.END, Keyboard.END);
		_map.put(Keys.ENTER, Keyboard.ENTER);
		_map.put(Keys.EQUALS, Keyboard.EQUAL);
		_map.put(Keys.ESCAPE, Keyboard.ESCAPE);
		_map.put(Keys.F, Keyboard.F);
		_map.put(Keys.F1, Keyboard.F1);
		_map.put(Keys.F10, Keyboard.F10);
		_map.put(Keys.F11, Keyboard.F11);
		_map.put(Keys.F12, Keyboard.F12);
		_map.put(Keys.F2, Keyboard.F2);
		_map.put(Keys.F3, Keyboard.F3);
		_map.put(Keys.F4, Keyboard.F4);
		_map.put(Keys.F5, Keyboard.F5);
		_map.put(Keys.F6, Keyboard.F6);
		_map.put(Keys.F7, Keyboard.F7);
		_map.put(Keys.F8, Keyboard.F8);
		_map.put(Keys.F9, Keyboard.F9);
		_map.put(Keys.G, Keyboard.G);
		_map.put(Keys.H, Keyboard.H);
		_map.put(Keys.HOME, Keyboard.HOME);
		_map.put(Keys.I, Keyboard.I);
		_map.put(Keys.INSERT, Keyboard.INSERT);
		_map.put(Keys.J, Keyboard.J);
		_map.put(Keys.K, Keyboard.K);
		_map.put(Keys.L, Keyboard.L);
		_map.put(Keys.LEFT, Keyboard.LEFT);
		_map.put(Keys.LEFT_BRACKET, Keyboard.LEFTBRACKET);
		_map.put(Keys.M, Keyboard.M);
		_map.put(Keys.MENU, Keyboard.MENU);
		_map.put(Keys.MINUS, Keyboard.MINUS);
		_map.put(Keys.N, Keyboard.N);
		_map.put(Keys.NUM_0, Keyboard.NUMBER_0);
		_map.put(Keys.NUM_1, Keyboard.NUMBER_1);
		_map.put(Keys.NUM_2, Keyboard.NUMBER_2);
		_map.put(Keys.NUM_3, Keyboard.NUMBER_3);
		_map.put(Keys.NUM_4, Keyboard.NUMBER_4);
		_map.put(Keys.NUM_5, Keyboard.NUMBER_5);
		_map.put(Keys.NUM_6, Keyboard.NUMBER_6);
		_map.put(Keys.NUM_7, Keyboard.NUMBER_7);
		_map.put(Keys.NUM_8, Keyboard.NUMBER_8);
		_map.put(Keys.NUM_9, Keyboard.NUMBER_9);
		_map.put(Keys.NUMPAD_0, Keyboard.NUMPAD_0);
		_map.put(Keys.NUMPAD_1, Keyboard.NUMPAD_1);
		_map.put(Keys.NUMPAD_2, Keyboard.NUMPAD_2);
		_map.put(Keys.NUMPAD_3, Keyboard.NUMPAD_3);
		_map.put(Keys.NUMPAD_4, Keyboard.NUMPAD_4);
		_map.put(Keys.NUMPAD_5, Keyboard.NUMPAD_5);
		_map.put(Keys.NUMPAD_6, Keyboard.NUMPAD_6);
		_map.put(Keys.NUMPAD_7, Keyboard.NUMPAD_7);
		_map.put(Keys.NUMPAD_8, Keyboard.NUMPAD_8);
		_map.put(Keys.NUMPAD_9, Keyboard.NUMPAD_9);
		//_map.put(Keys.NUMPAD_ADD, Keyboard.NUMPAD_ADD);
		//_map.put(Keys.NUMPAD_DECIMAL, Keyboard.NUMPAD_DECIMAL);
		//_map.put(Keys.NUMPAD_DIVIDE, Keyboard.NUMPAD_DIVIDE);
		//_map.put(Keys.NUMPAD_ENTER, Keyboard.NUMPAD_ENTER);
		//_map.put(Keys.NUMPAD_MULTIPLY, Keyboard.NUMPAD_MULTIPLY);
		//_map.put(Keys.NUMPAD_SUBTRACT, Keyboard.NUMPAD_SUBTRACT);
		_map.put(Keys.O, Keyboard.O);
		_map.put(Keys.P, Keyboard.P);
		_map.put(Keys.PAGE_DOWN, Keyboard.PAGE_DOWN);
		_map.put(Keys.PAGE_UP, Keyboard.PAGE_UP);
		_map.put(Keys.PERIOD, Keyboard.PERIOD);
		_map.put(Keys.Q, Keyboard.Q);
		_map.put(Keys.APOSTROPHE, Keyboard.QUOTE);
		_map.put(Keys.R, Keyboard.R);
		_map.put(Keys.RIGHT, Keyboard.RIGHT);
		_map.put(Keys.RIGHT_BRACKET, Keyboard.RIGHTBRACKET);
		_map.put(Keys.S, Keyboard.S);
		_map.put(Keys.SEARCH, Keyboard.SEARCH);
		_map.put(Keys.SEMICOLON, Keyboard.SEMICOLON);
		_map.put(Keys.SHIFT_LEFT, Keyboard.SHIFT);
		_map.put(Keys.SHIFT_RIGHT, Keyboard.SHIFT);
		_map.put(Keys.SLASH, Keyboard.SLASH);
		_map.put(Keys.SPACE, Keyboard.SPACE);
		_map.put(Keys.T, Keyboard.T);
		_map.put(Keys.TAB, Keyboard.TAB);
		_map.put(Keys.U, Keyboard.U);
		_map.put(Keys.UP, Keyboard.UP);
		_map.put(Keys.V, Keyboard.V);
		_map.put(Keys.W, Keyboard.W);
		_map.put(Keys.X, Keyboard.X);
		_map.put(Keys.Y, Keyboard.Y);
		_map.put(Keys.Z, Keyboard.Z);
	}

	@Override
	public boolean keyDown(int keycode)
	{
		_keyCode = keycode;
		return _eventDispatcher.dispatchEvent(_keyboardEvents.obtain(KeyboardEvent.KEY_DOWN, _map.get(keycode, -1)));
	}

	@Override
	public boolean keyUp(int keycode)
	{
		return _eventDispatcher.dispatchEvent(_keyboardEvents.obtain(KeyboardEvent.KEY_UP, _map.get(keycode, -1)));
	}

	@Override
	public boolean keyTyped(char character)
	{
		return _eventDispatcher.dispatchEvent(_keyboardEvents.obtain(KeyboardEvent.KEY_TYPED, _keyCode, character));
	}

	@Override
	// TODO: convert to pointer position to stage coordinates here
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		super.touchDown(screenX, screenY, pointer, button);
		boolean touchProcessed = _eventDispatcher.dispatchEvent(_touchEvents.obtain(TouchEvent.TOUCH_BEGIN, screenX, screenY, pointer));
		boolean mouseProcessed = _eventDispatcher.dispatchEvent(_mouseEvents.obtain(MouseEvent.MOUSE_DOWN, screenX, screenY, 0));
		return touchProcessed || mouseProcessed;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		super.touchUp(screenX, screenY, pointer, button);
		boolean touchProcessed = _eventDispatcher.dispatchEvent(_touchEvents.obtain(TouchEvent.TOUCH_END, screenX, screenY, pointer));
		boolean mouseProcessed = _eventDispatcher.dispatchEvent(_mouseEvents.obtain(MouseEvent.MOUSE_UP, screenX, screenY, 0));
		return touchProcessed || mouseProcessed;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		return super.touchDragged(screenX, screenY, pointer);
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		return super.mouseMoved(screenX, screenY);
	}

	@Override
	public boolean scrolled(int amount)
	{
		return _eventDispatcher.dispatchEvent(_mouseEvents.obtain(MouseEvent.MOUSE_WHEEL, Gdx.input.getX(), Gdx.input.getY(), amount));
	}
}
