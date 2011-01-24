package org.flixel.data;

import org.flixel.FlxG;
import org.flixel.FlxGroup;
import org.flixel.FlxSprite;
import org.flixel.FlxText;

import android.graphics.Paint.Align;

/**
 * This is the default flixel pause screen. It can be overridden with your own
 * <code>FlxLayer</code> object.
 */
public class FlxPause extends FlxGroup
{
	/**
	 * Constructor.
	 */
	public FlxPause()
	{
		super();
		scrollFactor.x = 0;
		scrollFactor.y = 0;
		int w = 160;
		int h = 120;

		x = (FlxG.width - w) / 2;
		y = (FlxG.height - h) / 2;

		FlxSprite s;
		s = new FlxSprite().createGraphic(w, h, 0xFF000000, true);
		s.setAlpha(1);
		s.setSolid(false);
		add(s, true);

		add((new FlxText(0, 0, w, "this game is")).setFormat(null, 16, 0xFFFFFFFF, Align.CENTER), true);
		add((new FlxText(0, 20, w, "PAUSED")).setFormat(null, 16, 0xffffffff, Align.CENTER), true);
	}

}
