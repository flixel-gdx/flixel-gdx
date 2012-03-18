package org.examples.bitmapfont;

import org.flixel.FlxState;
import org.flixel.FlxText;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class PlayState extends FlxState
{

	private FlxText text;

	@Override
	public void create()
	{
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
		
		
		FlxText text4 = new FlxText(10, 160, 200, "Warning");
		text4.setFormat(new BitmapFont(Gdx.files.internal("flixel/data/font/nokiafc22.fnt"), Gdx.files.internal("flixel/data/font/nokiafc22.png"), true), 2f);
		text4.setColor(0xFFFF0000);
		text4.setShadow(0xFFFFFFFF);
		add(text4);
	}
}
