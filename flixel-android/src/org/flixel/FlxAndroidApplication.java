package org.flixel;

import android.os.Bundle;
import android.view.WindowManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class FlxAndroidApplication extends AndroidApplication
{
	private FlxGame _game;
	protected AndroidApplicationConfiguration cfg;
	
	public FlxAndroidApplication(FlxGame Game)
	{
		_game = Game;
		cfg = new AndroidApplicationConfiguration();
		cfg.useAccelerometer = false;
		cfg.useCompass = false;
	}
		
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{		
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);		
		initialize((ApplicationListener)_game.stage, cfg);				
	}
}
