package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class ThousandParticlesDemo
{
	public static void main(String[] args)
	{
		new LwjglApplication(new org.examples.thousandparticles.ThousandParticlesDemo(), "", 480, 320, false);
	}
}
