package org.flixel.examples.cameraeffects;

import org.flixel.FlxButton;
import org.flixel.FlxG;
import org.flixel.FlxState;
import org.flixel.event.AFlxButton;
import org.flixel.event.AFlxCamera;

public class PlayState extends FlxState
{

	@Override
	public void create()
	{		
		FlxG.setBgColor(0xff131c1b);
		
		add(new FlxButton(10, 10, "fade", onFade));
		add(new FlxButton(100, 10, "flash", onFlash));
		add(new FlxButton(190, 10, "shake", onShake));
	}

	AFlxButton onFade = new AFlxButton()
	{
		@Override
		public void onUp()
		{
			FlxG.log("fade!");
			FlxG.fade(0xFF000000, 2, new AFlxCamera(){ @Override public void onFadeComplete(){FlxG.camera.stopFX();}});
		}
	};
	
	AFlxButton onFlash = new AFlxButton()
	{
		@Override
		public void onUp()
		{
			FlxG.log("flash!");
			FlxG.flash(0xFFFFFFFF, 2);
		}
	};
	
	AFlxButton onShake = new AFlxButton()
	{
		@Override
		public void onUp()
		{
			FlxG.log("shake!");
			FlxG.shake(0.05f, 2);
		}
	};
}
