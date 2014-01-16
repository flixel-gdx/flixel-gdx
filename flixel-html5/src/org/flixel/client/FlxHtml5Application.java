package org.flixel.client;

import org.flixel.FlxGame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.backends.gwt.preloader.Preloader.PreloaderCallback;
import com.badlogic.gdx.backends.gwt.preloader.Preloader.PreloaderState;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

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
	
	@Override
	public PreloaderCallback getPreloaderCallback()
	{
		final Panel preloaderPanel = new VerticalPanel();
		preloaderPanel.setStyleName("gdx-preloader");
		final Image logo = new Image(GWT.getHostPageBaseURL() + "assets/org/flixel/client/logo.png");
		logo.setStyleName("logo");		
		preloaderPanel.add(logo);
		final Panel meterPanel = new SimplePanel();
		meterPanel.setStyleName("gdx-meter");
		meterPanel.addStyleName("red");
		final InlineHTML meter = new InlineHTML();
		final Style meterStyle = meter.getElement().getStyle();
		meterStyle.setWidth(0, Unit.PCT);
		meterPanel.add(meter);
		preloaderPanel.add(meterPanel);
		getRootPanel().add(preloaderPanel);
		
		return new PreloaderCallback()
		{
			
			@Override
			public void update(PreloaderState state)
			{
				meterStyle.setWidth(100f * state.getProgress(), Unit.PCT);
			}
			
			@Override
			public void error(String file)
			{
				System.out.println("error: " + file);
			}
		};
	}
}