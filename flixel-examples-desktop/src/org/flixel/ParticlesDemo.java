package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class ParticlesDemo
{
	public static void main(String[] args)
	{
		new LwjglApplication(new org.flixel.examples.particles.ParticleDemo(), "", 400, 300, false);
	}
}