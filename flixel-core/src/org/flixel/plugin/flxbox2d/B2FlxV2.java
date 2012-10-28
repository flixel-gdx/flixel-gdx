package org.flixel.plugin.flxbox2d;

import com.badlogic.gdx.math.Vector2;

/**
 * It's just an subclass of Vector2. It only does the pixels to meters ratio.
 *
 * @author Ka Wing Chin
 */
public class B2FlxV2 extends Vector2
{	
	private static final long serialVersionUID = -8590716581405868354L;

	public B2FlxV2(float x, float y)
	{
		super(x/B2FlxB.RATIO, y/B2FlxB.RATIO);
	}
	
	public B2FlxV2(float x)
	{
		this(x/B2FlxB.RATIO, 0);
	}
}

