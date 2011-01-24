package org.example;

import org.flixel.FlxGame;
import org.flixel.FlxGameView;
import android.content.Context;

public class GameView extends FlxGameView
{

	public GameView(Context context, Class<? extends Object> resource)
	{
		super(new FlxGame(400, 240, PlayState5.class, context, resource), context);
	}

}
