package org.flixel.client;

import org.flixel.FlxGame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class FlxHtml5Application extends GwtApplication
{
	private FlxGame _game;
	private int _width;
	private int _height;
	
	public FlxHtml5Application(FlxGame Game, int Width, int Height)
	{
		_game = Game;
		_width = Width;
		_height = Height;
	}
	
	@Override
	public GwtApplicationConfiguration getConfig()
	{
		return new GwtApplicationConfiguration(_width, _height);
	}

	@Override
	public ApplicationListener getApplicationListener()
	{
		return (ApplicationListener)_game.stage;
	}
}