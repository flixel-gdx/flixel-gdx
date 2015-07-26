package org.flixel.system.input;

import org.flixel.FlxG;

import com.badlogic.gdx.utils.reflect.ClassReflection;

import flash.events.KeyboardEvent;

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
	public boolean BACK;
	public boolean MENU;
	public boolean SEARCH;
	public boolean VOLUME_DOWN;
	public boolean VOLUME_UP;

	/**
	 * Creates a new <code>Keyboard</code> object.
	 */
	public Keyboard()
	{
		int i;

		//LETTERS (A-Z)
		i = 65;
		addKey("A",i++);
		addKey("B",i++);
		addKey("C",i++);
		addKey("D",i++);
		addKey("E",i++);
		addKey("F",i++);
		addKey("G",i++);
		addKey("H",i++);
		addKey("I",i++);
		addKey("J",i++);
		addKey("K",i++);
		addKey("L",i++);
		addKey("M",i++);
		addKey("N",i++);
		addKey("O",i++);
		addKey("P",i++);
		addKey("Q",i++);
		addKey("R",i++);
		addKey("S",i++);
		addKey("T",i++);
		addKey("U",i++);
		addKey("V",i++);
		addKey("W",i++);
		addKey("X",i++);
		addKey("Y",i++);
		addKey("Z",i++);

		//NUMBERS (0-9)
		i = 48;
		addKey("ZERO",i++);
		addKey("ONE",i++);
		addKey("TWO",i++);
		addKey("THREE",i++);
		addKey("FOUR",i++);
		addKey("FIVE",i++);
		addKey("SIX",i++);
		addKey("SEVEN",i++);
		addKey("EIGHT",i++);
		addKey("NINE",i++);
		i = 96;
		addKey("NUMPADZERO",i++);
		addKey("NUMPADONE",i++);
		addKey("NUMPADTWO",i++);
		addKey("NUMPADTHREE",i++);
		addKey("NUMPADFOUR",i++);
		addKey("NUMPADFIVE",i++);
		addKey("NUMPADSIX",i++);
		addKey("NUMPADSEVEN",i++);
		addKey("NUMPADEIGHT",i++);
		addKey("NUMPADNINE",i++);
		addKey("PAGEUP", 33);
		addKey("PAGEDOWN", 34);
		addKey("HOME", 36);
		addKey("END", 35);
		addKey("INSERT", 45);

		//FUNCTION KEYS
		i = 1;
		while(i <= 12)
			addKey("F"+i,111+(i++));

		//SPECIAL KEYS + PUNCTUATION
		addKey("ESCAPE",27);
		addKey("MINUS",189);
		addKey("NUMPADMINUS",109);
		addKey("PLUS",187);
		addKey("NUMPADPLUS",107);
		addKey("DELETE",46);
		addKey("BACKSPACE",8);
		addKey("LBRACKET",219);
		addKey("RBRACKET",221);
		addKey("BACKSLASH",220);
		addKey("CAPSLOCK",20);
		addKey("SEMICOLON",186);
		addKey("QUOTE",222);
		addKey("ENTER",13);
		addKey("SHIFT",16);
		addKey("COMMA",188);
		addKey("PERIOD",190);
		addKey("NUMPADPERIOD",110);
		addKey("SLASH",191);
		addKey("NUMPADSLASH",191);
		addKey("CONTROL",17);
		addKey("ALT",18);
		addKey("SPACE",32);
		addKey("UP",38);
		addKey("DOWN",40);
		addKey("LEFT",37);
		addKey("RIGHT",39);
		addKey("TAB",9);

		//MOBILE KEYS
		addKey("BACK",flash.ui.Keyboard.BACK);
		addKey("MENU",flash.ui.Keyboard.MENU);
		addKey("SEARCH",flash.ui.Keyboard.SEARCH);
		//addKey("VOLUME_DOWN", flash.ui.Keyboard.VOLUME_DOWN);
		//addKey("VOLUME_UP", flash.ui.Keyboard.VOLUME_UP);
	}

	/**
	 * Event handler so FlxGame can toggle keys.
	 * 
	 * @param	FlashEvent	A <code>KeyboardEvent</code> object.
	 */
	public void handleKeyDown(KeyboardEvent FlashEvent)
	{
		KeyState object = _map.get(FlashEvent.keyCode);
		if(object == null) return;
		if(object.current > 0) object.current = 1;
		else object.current = 2;

		try
		{ // TODO: Reflection is fairly slow, could we use a BooleanMap instead?
			ClassReflection.getField(Keyboard.class, object.name).set(this, true);
		}
		catch(Exception e)
		{
			FlxG.log("Keyboard", e.getMessage());
		}
	}

	/**
	 * Event handler so FlxGame can toggle keys.
	 * 
	 * @param	FlashEvent	A <code>KeyboardEvent</code> object.
	 */
	public void handleKeyUp(KeyboardEvent FlashEvent)
	{
		KeyState object = _map.get(FlashEvent.keyCode);
		if(object == null) return;
		if(object.current > 0) object.current = -1;
		else object.current = 0;

		try
		{
			ClassReflection.getField(Keyboard.class, object.name).set(this, false);
		}
		catch(Exception e)
		{
			FlxG.log("Keyboard", e.getMessage());
		}
	}
}
