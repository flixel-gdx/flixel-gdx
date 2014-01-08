package org.flixel.ui;

import org.flixel.FlxG;
import org.flixel.FlxSprite;

import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;

/**
 *
 * @author Ka Wing Chin
 */
public class FlxNinePatch extends FlxSprite
{
	public static final int TOP_LEFT = 0;
	public static final int TOP_CENTER = 1;
	public static final int TOP_RIGHT = 2;
	public static final int MIDDLE_LEFT = 3;
	public static final int MIDDLE_CENTER = 4;
	public static final int MIDDLE_RIGHT = 5;
	public static final int BOTTOM_LEFT = 6;
	public static final int BOTTOM_CENTER = 7;
	public static final int BOTTOM_RIGHT = 8;
	
	public int position;
	
	private TiledDrawable _tiled;
	
	/**
	 * Create a new <code>FlxNinePatch</code> object.
	 * @param Position	One of the 9 positions of the ninepatch, e.g. <code>FlxNinePatch.TOP_LEFT</code>.
	 * @param Img		The image of the nine patch.
	 * @param Width		The width of the nine patch.
	 * @param Height	The height of the nine patch.
	 */
	public FlxNinePatch(int Position, String Img, int Width, int Height)
	{
		super();
		loadGraphic(Img, true, false, Width, Height);
		_tiled = new TiledDrawable(framePixels);
	}
	
	@Override
	public void renderSprite()
	{
		_tiled.draw(FlxG.batch, x, y, width, height);
	}
}

