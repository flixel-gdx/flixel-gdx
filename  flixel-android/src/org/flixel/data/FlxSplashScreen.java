package org.flixel.data;

import org.flixel.FlxG;
import org.flixel.FlxSprite;
import org.flixel.FlxState;

public class FlxSplashScreen extends FlxState
{
	private float _logoTimer;
	
	@Override
	public void create()
	{		
		FlxG.setDisablePause(true);
		
		_logoTimer = 0;

		int scale = 1;
		if(FlxG.height > 200 && FlxG.height > FlxG.width)
			scale = 2;
		int pixelSize = 32 * scale;
		int top = FlxG.height/2 - pixelSize*3;
		int left = FlxG.width/2 - pixelSize;
		
		add(new FlxLogoPixel(left+pixelSize, top, pixelSize, 0, 0xFFFF2346, pixelSize));
		add(new FlxLogoPixel(left, top+pixelSize, pixelSize, 0, 0xFFFFBF37, pixelSize));
		add(new FlxLogoPixel(left, top+pixelSize*2,pixelSize, 0, 0xFF00B92B, pixelSize));
		add(new FlxLogoPixel(left+pixelSize, top+pixelSize*2, pixelSize, 0, 0xFF0BC8FF, pixelSize));
		add(new FlxLogoPixel(left, top+pixelSize*3, pixelSize, 0, 0xFF3B43FF, pixelSize));
		
		FlxSprite poweredBy = new FlxSprite(0,0,R.drawable.poweredby);		
		poweredBy.scale.x = poweredBy.scale.y = scale;
		poweredBy.x = FlxG.width/2 - poweredBy.width/2;
		poweredBy.y = top+pixelSize*4+16;
		add(poweredBy);		
		
		// Sound by Rob Muir.
		FlxG.play(R.raw.flixel_female);
	}
	
	@Override
	public void update()
	{		
		super.update();
		_logoTimer += FlxG.elapsed;
			
		if(_logoTimer > 2)
		{
			System.gc();
			FlxG.setDisablePause(false);
			FlxG.setState(FlxG.initialGameState);
		}
	}
}
