package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;


public class FlxInvaders 
{
	public static void main(String[] args)
	{
		new LwjglApplication(new org.flixel.examples.flxinvaders.FlxInvaders(), "", 320, 240, false);
	}
}
