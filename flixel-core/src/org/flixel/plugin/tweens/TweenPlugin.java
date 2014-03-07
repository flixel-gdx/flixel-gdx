package org.flixel.plugin.tweens;

import org.flixel.FlxBasic;
import org.flixel.FlxG;
import org.flixel.FlxPoint;
import org.flixel.FlxSprite;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

/**
 * A tween plugin for flixel. It uses the Universal Tween Engine by Aurelien
 * Ribon. It supports different tweens, timeline, parallel and many callbacks.
 * By default the FlxSprite and TweenSprite accessor is register. To register
 * your own accessor you'll need CustomClass + Accessor.
 * 
 * Take a look at: http://code.google.com/p/java-universal-tween-engine/
 * 
 * @author Ka Wing Chin
 */
public class TweenPlugin extends FlxBasic
{
	/**
	 * A TweenManager updates all your tweens and timelines at once. Its main
	 * interest is that it handles the tween/timeline life-cycles for you, as
	 * well as the pooling constraints (if object pooling is enabled). Just give
	 * it a bunch of tweens or timelines and call update() periodically, you
	 * don't need to care for anything else! Relax and enjoy your animations.
	 */
	public static TweenManager manager;
	/**
	 * Whether the tween should updates or not when <code>FlxG.paused</code> is
	 * set to true.
	 */
	public static boolean ignorePause = false;

	/**
	 * Constructor
	 */
	public TweenPlugin()
	{
		manager = new TweenManager();
		Tween.registerAccessor(FlxSprite.class, new TweenSprite());
		Tween.registerAccessor(FlxPoint.class, new TweenPoint());
	}

	/**
	 * Main loop for the tweens.
	 */
	@Override
	public void update()
	{
		if(!FlxG.paused || ignorePause)
			manager.update(FlxG.elapsed);
	}

	/**
	 * Remove all tweens.
	 */
	public void clear()
	{
		manager.killAll();
	}

	/**
	 * Remove all tweens and free memory.
	 */
	@Override
	public void destroy()
	{
		manager.killAll();
		manager = null;
	}
}
