package org.flixel.ui;

import org.flixel.FlxG;
import org.flixel.FlxSprite;

import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;

/**
 * The <code>NinePatch</code> for drawing 9-patches. It can repeat pattern, but
 * make it large enough so it won't affect too much performance.
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

	/**
	 * The position of the nine patch.
	 */
	public int position;

	/**
	 * Draws a TextureRegion repeatedly to fill the area, instead of stretching
	 * it.
	 */
	private TiledDrawable _tiled;
	/**
	 * Whether the pattern should repeat or not.
	 */
	private boolean _repeat;

	/**
	 * Create a new <code>FlxNinePatch</code> object.
	 * 
	 * @param Position One of the 9 positions of the ninepatch, e.g.
	 *        <code>FlxNinePatch.TOP_LEFT</code>.
	 * @param Img The image of the nine patch.
	 * @param Width The width of the nine patch.
	 * @param Height The height of the nine patch.
	 * @param Repeat Repeats the pattern of a ninepatch. Default is false.
	 */
	public FlxNinePatch(int Position, String Img, int Width, int Height, boolean Repeat)
	{
		super();
		loadGraphic(Img, true, false, Width, Height);
		_repeat = Repeat;
		if(_repeat)
			_tiled = new TiledDrawable(framePixels);
	}

	/**
	 * Create a new <code>FlxNinePatch</code> object.
	 * 
	 * @param Position One of the 9 positions of the ninepatch, e.g.
	 *        <code>FlxNinePatch.TOP_LEFT</code>.
	 * @param Img The image of the nine patch.
	 * @param Width The width of the nine patch.
	 * @param Height The height of the nine patch.
	 */
	public FlxNinePatch(int Position, String Img, int Width, int Height)
	{
		this(Position, Img, Width, Height, false);
	}

	@Override
	public void renderSprite()
	{
		if(_repeat)
			_tiled.draw(FlxG.batch, x, y, width, height);
		else
			FlxG.batch.draw(framePixels, x, y, width, height);
	}

	@Override
	public void destroy()
	{
		super.destroy();
		_tiled = null;
	}
}
