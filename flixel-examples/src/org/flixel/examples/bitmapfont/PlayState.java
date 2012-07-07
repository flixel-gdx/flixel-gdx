package org.flixel.examples.bitmapfont;

import org.flixel.FlxG;
import org.flixel.FlxState;
import org.flixel.FlxText;

public class PlayState extends FlxState
{

	private FlxText text;

	@Override
	public void create()
	{
		FlxG.setBgColor(0xff131c1b);
		
		text = new FlxText(10, 10, 200, "flixel-android");
//		text.setSize(2);
		add(text);
		
		
		FlxText text2 = new FlxText(10, 100, 200, "flixel-\nandroid");
		text2.setColor(0xFF0000FF);
//		text2.setFormat(0.5f, 0xFFFF0000, HAlignment.LEFT);
		add(text2);
		
		FlxText text3 = new FlxText(10, 140, 200, "FlxText");
		text3.setColor(0xFF0000FF);
		text3.setAlignment("center");
		text3.setShadow(0xFFFFFFFF);
		add(text3);
		
		FlxText text4 = new FlxText(10, 160, 200);
		
		text4.setFormat(null, 32);text4.setText("012345");
		text4.setColor(0xFFFF0000);
		text4.setShadow(0xFFFFFFFF);
		add(text4);
	}
}
