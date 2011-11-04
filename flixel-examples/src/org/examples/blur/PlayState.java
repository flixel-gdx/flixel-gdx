package org.examples.blur;

import org.flixel.FlxEmitter;
import org.flixel.FlxG;
import org.flixel.FlxParticle;
import org.flixel.FlxState;
import org.flixel.FlxText;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


/**
 * A demo that shows colored squares. The squares leave a trail.
 * This demo is from flixel's FlxBlur created by Adam Atomic.
 */
public class PlayState extends FlxState
{

	public boolean toggle;

	@Override
	public void create()
	{
		// Background
		FlxG.setBgColor(0x11000000);

		// Title text, nothing crazy here!
		FlxText text;
		text = new FlxText(FlxG.width / 4f, FlxG.height / 2f - 20, FlxG.width / 2, "FlxBlur");
		// text.setFormat(null,32,FlxG.WHITE,"center");
		add(text);
		text = new FlxText(FlxG.width / 4f, FlxG.height / 2f + 20, FlxG.width / 2, "touch to toggle");
		// text.setFormat(null,16,FlxG.BLUE,"center");
		add(text);

		// This is the particle emitter that spews things off the bottom of the screen.
		// I'm not going to go over it in too much detail here, but basically we
		// create the emitter, then we create 50 32x32 sprites and add them to it.
		FlxEmitter emitter = new FlxEmitter(0, FlxG.height + 20, 50);
		emitter.width = FlxG.width;
		emitter.gravity = -40;
		emitter.setXSpeed(-20, 20);
		emitter.setYSpeed(-75, -25);
		emitter.setRotation(-360, 360);
		FlxParticle particle;
		int particles = 50;
//		int[] colors = {FlxG.BLUE, (FlxG.BLUE | FlxG.GREEN), FlxG.GREEN, (FlxG.GREEN | FlxG.RED), FlxG.RED};
		TextureRegion[] colors = {	Asset.Img0090e9, Asset.Img00f2ed, Asset.ImgFFF237, Asset.ImgFF0012, Asset.ImgCC00FF, 
									Asset.Img00F225, Asset.ImgFF6600, Asset.Img801E98};
		for (int i = 0; i < particles; i++)
		{
			particle = new FlxParticle();
//			particle.makeGraphic(32, 32, colors[(int) (FlxG.random() * colors.length)]);
			particle.loadGraphic(colors[(int) (FlxG.random() * colors.length)]);
			particle.exists = false;
			emitter.add(particle);
		}
		// emitter.makeParticles(Asset.ImgColors, particles, 16, true); // Or you could load the all colors immediately.
		emitter.start(false, 0, 0.1f);
		add(emitter);

		// Let the player toggle the effect with the space bar. Effect starts on.
		toggle = true;
	}


	@Override
	public void update()
	{
		if(Gdx.input.justTouched())
			toggle = !toggle;
		if(toggle)
		{
			// By setting the background color to a value with a low transparency,
			// we can generate a cool "blur" effect.
			FlxG.setBgColor(0x11000000);
		}
		else
		{
			// Setting it to an opaque color will turn the effect back off.
			FlxG.setBgColor(FlxG.BLACK);
		}

		super.update();
	}
}
