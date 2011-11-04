package org.flixel.system.input;

import com.badlogic.gdx.Input.Keys;

/**
 * Keeps track of what keys are pressed and how with handy booleans or strings.
 * 
 * @author Ka Wing Chin
 */
public class Keyboard extends Input
{
	/**
	 * Constructor
	 */
	public Keyboard()
	{
		super();
		
		// LETTERS (A-Z)
		int i = 29;
		while(i <= 54)
			addKey(i++);
		
		//NUMBERS (0-9)
		i = 7;
		while(i < 16)
			addKey(i++);
		
		//FUNCTION KEYS (F1-F12)
		i = 244;
		while(i <= 255)
			addKey(i++);
		
		//SPECIAL KEYS + PUNCTUATION
		addKey(Keys.ALT_LEFT);		
		addKey(Keys.ALT_RIGHT);
		addKey(Keys.APOSTROPHE);
		addKey(Keys.AT);
		addKey(Keys.BACK);
		addKey(Keys.BACKSLASH);
		addKey(Keys.CALL);
		addKey(Keys.CAMERA);
		addKey(Keys.CLEAR);
		addKey(Keys.COMMA);
		addKey(Keys.DEL);
		addKey(Keys.BACKSPACE);
		addKey(Keys.FORWARD_DEL);
		addKey(Keys.ENDCALL);
		addKey(Keys.ENTER);
		addKey(Keys.ENVELOPE);
		addKey(Keys.EQUALS);
		addKey(Keys.EXPLORER);
		addKey(Keys.FOCUS);
		addKey(Keys.GRAVE);
		addKey(Keys.HEADSETHOOK);
		addKey(Keys.HOME);
		addKey(Keys.LEFT_BRACKET);
		addKey(Keys.MEDIA_FAST_FORWARD);
		addKey(Keys.MEDIA_NEXT);
		addKey(Keys.MEDIA_PREVIOUS);
		addKey(Keys.MEDIA_REWIND);
		addKey(Keys.MEDIA_STOP);
		addKey(Keys.MENU);
		addKey(Keys.MINUS);
		addKey(Keys.MUTE);
		addKey(Keys.NOTIFICATION);
		addKey(Keys.NUM);
		addKey(Keys.PERIOD);
		addKey(Keys.PLUS);
		addKey(Keys.POUND);
		addKey(Keys.POWER);
		addKey(Keys.RIGHT_BRACKET);
		addKey(Keys.SEARCH);
		addKey(Keys.SEMICOLON);
		addKey(Keys.SHIFT_LEFT);
		addKey(Keys.SHIFT_RIGHT);
		addKey(Keys.SLASH);
		addKey(Keys.SOFT_LEFT);
		addKey(Keys.SOFT_RIGHT);
		addKey(Keys.SPACE);
		addKey(Keys.STAR);
		addKey(Keys.SYM);
		addKey(Keys.TAB);
		addKey(Keys.UNKNOWN);
		addKey(Keys.VOLUME_DOWN);
		addKey(Keys.VOLUME_UP);
		addKey(Keys.META_ALT_LEFT_ON);
		addKey(Keys.META_ALT_ON);
		addKey(Keys.META_ALT_RIGHT_ON);
		addKey(Keys.META_SHIFT_LEFT_ON);
		addKey(Keys.META_SHIFT_ON);
		addKey(Keys.META_SHIFT_RIGHT_ON);
		addKey(Keys.META_SYM_ON);
		addKey(Keys.CONTROL_LEFT);
		addKey(Keys.CONTROL_RIGHT);
		addKey(Keys.ESCAPE);
		addKey(Keys.END);
		addKey(Keys.INSERT);
		addKey(Keys.PAGE_UP);
		addKey(Keys.PAGE_DOWN);
		addKey(Keys.PICTSYMBOLS);
		addKey(Keys.SWITCH_CHARSET);
		addKey(Keys.BUTTON_CIRCLE);
		addKey(Keys.BUTTON_A);
		addKey(Keys.BUTTON_B);
		addKey(Keys.BUTTON_C);
		addKey(Keys.BUTTON_X);
		addKey(Keys.BUTTON_Y);
		addKey(Keys.BUTTON_Z);
		addKey(Keys.BUTTON_L1);
		addKey(Keys.BUTTON_R1);
		addKey(Keys.BUTTON_L2);
		addKey(Keys.BUTTON_R2);
		addKey(Keys.BUTTON_THUMBL);
		addKey(Keys.BUTTON_THUMBR);
		addKey(Keys.BUTTON_START);
		addKey(Keys.BUTTON_SELECT);
		addKey(Keys.BUTTON_MODE);
		addKey(Keys.COLON);
		
		// ARROWS
		addKey(Keys.CENTER);
		addKey(Keys.DOWN);
		addKey(Keys.LEFT);
		addKey(Keys.RIGHT);
		addKey(Keys.UP);
		
		// D-PAD
		addKey(Keys.DPAD_CENTER);
		addKey(Keys.DPAD_DOWN);
		addKey(Keys.DPAD_LEFT);
		addKey(Keys.DPAD_RIGHT);
		addKey(Keys.DPAD_UP);
	}
		

	@Override
	public boolean keyDown(int KeyCode)
	{
		KeyboardData o = _map.get(KeyCode);
		if(o == null)
			return false;
		
		if(o.current > 0)
			o.current = 1;
		else
			o.current = 2;		
		return false;
	}
	
	
	@Override
	public boolean keyUp(int KeyCode)
	{
		KeyboardData o = _map.get(KeyCode);
		if(o == null)
			return false;
		if(o.current > 0)
			o.current = -1;
		else
			o.current = 0;
		return false;
	}
}
