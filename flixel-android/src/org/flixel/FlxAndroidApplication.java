package org.flixel;

import org.flixel.system.FlxApplication;

import android.os.Bundle;
import android.view.WindowManager;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidNet;

public class FlxAndroidApplication extends AndroidApplication implements FlxApplication
{
	private FlxGame _game;
	protected AndroidNet _net;
	
	public FlxAndroidApplication(FlxGame Game)
	{
		_game = Game;
		_net = new AndroidNet(this);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{		
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		initialize(_game, false);		
	}

	@Override
	public Net getNet()
	{
		return _net;
	}
}
