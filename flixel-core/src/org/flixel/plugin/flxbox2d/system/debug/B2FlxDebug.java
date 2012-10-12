package org.flixel.plugin.flxbox2d.system.debug;

import org.flixel.FlxBasic;
import org.flixel.FlxG;
import org.flixel.plugin.flxbox2d.B2FlxB;

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

/**
 * A plugin for rendering debug shapes.
 * 
 * @author Ka Wing Chin
 */
public class B2FlxDebug extends FlxBasic
{
	// The ratio from meters to pixels.
	private final float RATIO = B2FlxB.RATIO;
	private Box2DDebugRenderer renderer;
	
	public B2FlxDebug()
	{
		renderer = new Box2DDebugRenderer();
	}
	
	@Override
	public void draw()
	{
		renderer.render(B2FlxB.world, FlxG.batch.getProjectionMatrix().scale(RATIO, RATIO, 0));
	}
}

