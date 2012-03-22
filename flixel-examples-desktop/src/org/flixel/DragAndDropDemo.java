package org.flixel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class DragAndDropDemo
{
	static public void main(String[] args)
	{
		new LwjglApplication(new org.flixel.examples.draganddrop.DragAndDropDemo(), "", 480, 320, false);
	}
}
