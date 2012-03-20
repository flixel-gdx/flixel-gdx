package org.flixel.system.input;

import org.flixel.FlxG;

import com.badlogic.gdx.Input.Keys;

/**
 * Keeps track of what keys are pressed and how with handy booleans or strings.
 * 
 * @author Ka Wing Chin
 */
public class Keyboard extends Input
{
	public boolean ESCAPE;
	public boolean F1;
	public boolean F2;
	public boolean F3;
	public boolean F4;
	public boolean F5;
	public boolean F6;
	public boolean F7;
	public boolean F8;
	public boolean F9;
	public boolean F10;
	public boolean F11;
	public boolean F12;
	public boolean ONE;
	public boolean TWO;
	public boolean THREE;
	public boolean FOUR;
	public boolean FIVE;
	public boolean SIX;
	public boolean SEVEN;
	public boolean EIGHT;
	public boolean NINE;
	public boolean ZERO;
	public boolean NUMPADONE;
	public boolean NUMPADTWO;
	public boolean NUMPADTHREE;
	public boolean NUMPADFOUR;
	public boolean NUMPADFIVE;
	public boolean NUMPADSIX;
	public boolean NUMPADSEVEN;
	public boolean NUMPADEIGHT;
	public boolean NUMPADNINE;
	public boolean NUMPADZERO;
	public boolean PAGEUP;
	public boolean PAGEDOWN;
	public boolean HOME;
	public boolean END;
	public boolean INSERT;
	public boolean MINUS;
	public boolean NUMPADMINUS;
	public boolean PLUS;
	public boolean NUMPADPLUS;
	public boolean DELETE;
	public boolean BACKSPACE;
	public boolean TAB;
	public boolean Q;
	public boolean W;
	public boolean E;
	public boolean R;
	public boolean T;
	public boolean Y;
	public boolean U;
	public boolean I;
	public boolean O;
	public boolean P;
	public boolean LBRACKET;
	public boolean RBRACKET;
	public boolean BACKSLASH;
	public boolean CAPSLOCK;
	public boolean A;
	public boolean S;
	public boolean D;
	public boolean F;
	public boolean G;
	public boolean H;
	public boolean J;
	public boolean K;
	public boolean L;
	public boolean SEMICOLON;
	public boolean QUOTE;
	public boolean ENTER;
	public boolean SHIFT;
	public boolean Z;
	public boolean X;
	public boolean C;
	public boolean V;
	public boolean B;
	public boolean N;
	public boolean M;
	public boolean COMMA;
	public boolean PERIOD;
	public boolean NUMPADPERIOD;
	public boolean SLASH;
	public boolean NUMPADSLASH;
	public boolean CONTROL;
	public boolean ALT;
	public boolean SPACE;
	public boolean UP;
	public boolean DOWN;
	public boolean LEFT;
	public boolean RIGHT;
	
	public Keyboard()
	{
		super();
		
		// LETTERS (A-Z)
		addKey("A", Keys.A);
		addKey("B", Keys.B);
		addKey("C", Keys.C);
		addKey("D", Keys.D);
		addKey("E", Keys.E);
		addKey("F", Keys.F);
		addKey("G", Keys.G);
		addKey("H", Keys.H);
		addKey("I", Keys.I);
		addKey("J", Keys.J);
		addKey("K", Keys.K);
		addKey("L", Keys.L);
		addKey("M", Keys.M);
		addKey("N", Keys.N);
		addKey("O", Keys.O);
		addKey("P", Keys.P);
		addKey("Q", Keys.Q);
		addKey("R", Keys.R);
		addKey("S", Keys.S);
		addKey("T", Keys.T);
		addKey("U", Keys.U);
		addKey("V", Keys.V);
		addKey("W", Keys.W);
		addKey("X", Keys.X);
		addKey("Y", Keys.Y);
		addKey("Z", Keys.Z);
		
		//NUMBERS (0-9)
		//addKey("ZERO");
		addKey("NUMPADZERO",Keys.NUM_0);
		addKey("NUMPADONE",Keys.NUM_1);
		addKey("NUMPADTWO",Keys.NUM_2);
		addKey("NUMPADTHREE",Keys.NUM_3);
		addKey("NUMPADFOUR",Keys.NUM_4);
		addKey("NUMPADFIVE",Keys.NUM_5);
		addKey("NUMPADSIX",Keys.NUM_6);
		addKey("NUMPADSEVEN",Keys.NUM_7);
		addKey("NUMPADEIGHT",Keys.NUM_8);
		addKey("NUMPADNINE",Keys.NUM_9);
		addKey("PAGEUP", Keys.PAGE_UP);
		addKey("PAGEDOWN", Keys.PAGE_DOWN);
		addKey("HOME", Keys.HOME);
		addKey("END", Keys.END);
		addKey("INSERT", Keys.INSERT);
		
		//FUNCTION KEYS (F1-F12)
		addKey("F1", Keys.F1);
		addKey("F2", Keys.F2);
		addKey("F3", Keys.F3);
		addKey("F4", Keys.F4);
		addKey("F5", Keys.F5);
		addKey("F6", Keys.F6);
		addKey("F7", Keys.F7);
		addKey("F8", Keys.F8);
		addKey("F9", Keys.F9);
		addKey("F10", Keys.F10);
		addKey("F11", Keys.F11);
		addKey("F12", Keys.F12);
		
		//SPECIAL KEYS + PUNCTUATION
		addKey("ESCAPE",Keys.ESCAPE);
		addKey("MINUS",Keys.MINUS);
		//addKey("NUMPADMINUS", Keys.MINUS);
		addKey("PLUS",Keys.PLUS);
		//addKey("NUMPADPLUS",Keys.PLUS);
		addKey("DELETE",Keys.DEL);
		addKey("BACKSPACE",Keys.BACKSPACE);
		addKey("LBRACKET",Keys.LEFT_BRACKET);
		addKey("RBRACKET",Keys.RIGHT_BRACKET);
		addKey("BACKSLASH",Keys.BACKSLASH);
		//addKey("CAPSLOCK",Keys.CAPS_LOCK);
		addKey("SEMICOLON",Keys.SEMICOLON);
		//addKey("QUOTE",Keys.QUOTE);
		addKey("ENTER",Keys.ENTER);
		addKey("SHIFT",Keys.SHIFT_LEFT);
		addKey("COMMA",Keys.COMMA);
		addKey("PERIOD",Keys.PERIOD);
		//addKey("NUMPADPERIOD",Keys.PERIOD);
		addKey("SLASH",Keys.SLASH);
		//addKey("NUMPADSLASH",Keys.SLASH);
		addKey("CONTROL",Keys.CONTROL_LEFT);
		addKey("ALT",Keys.ALT_LEFT);
		addKey("SPACE",Keys.SPACE);
		addKey("UP",Keys.UP);
		addKey("DOWN",Keys.DOWN);
		addKey("LEFT",Keys.LEFT);
		addKey("RIGHT",Keys.RIGHT);
		addKey("TAB",Keys.TAB);
	}
		
	/**
	 * Event handler so FlxGame can toggle keys.
	 * 
	 * @param	KeyCode	The key code of the pressed key.
	 */
	public void handleKeyDown(int KeyCode)
	{
		KeyState o = _map.get(KeyCode);
		if(o == null)
			return;
		
		if(o.current > 0)
			o.current = 1;
		else
			o.current = 2;
		
		try {
			Keyboard.class.getField(o.name).setBoolean(this, true);
		} catch (Exception e) {
			FlxG.log("Keyboard", e.getMessage());
		}
	}
	
	/**
	 * Event handler so FlxGame can toggle keys.
	 * 
	 * @param	KeyCode	The key code of the pressed key.
	 */
	public void handleKeyUp(int KeyCode)
	{
		KeyState o = _map.get(KeyCode);
		if(o == null)
			return;
		
		if(o.current > 0)
			o.current = -1;
		else
			o.current = 0;
		
		try {
			Keyboard.class.getField(o.name).setBoolean(this, false);
		} catch (Exception e) {
			FlxG.log("Keyboard", e.getMessage());
		}
	}
}
