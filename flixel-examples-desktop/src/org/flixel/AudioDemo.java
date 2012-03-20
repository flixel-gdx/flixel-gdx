package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class AudioDemo
{
	static public void main(String[] args)
	{
		new LwjglApplication(new org.examples.audio.AudioDemo(), "", 480, 320, false);
	}
}
