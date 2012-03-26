package org.flixel.examples.audio;

import org.flixel.FlxButton;
import org.flixel.FlxG;
import org.flixel.FlxSound;
import org.flixel.FlxState;
import org.flixel.event.AFlxButton;

import com.badlogic.gdx.Input.Keys;


/**
 * Just a simple demo to play music and sound.
 * Take a look at Music and Sound class for more features.
 * 
 * @author Ka Wing Chin
 */
public class PlayState extends FlxState
{
	FlxSound flixel;
	private FlxButton _btnPlayMusic;
	protected boolean _paused;

	@Override
	public void create()
	{
		// Background
		FlxG.setBgColor(0xFF000000);
		
		// Add a play/pause button for the music.
		add(_btnPlayMusic = new FlxButton(10, 10, "play music", playMusic));
		// Add a stop button for the music.
		FlxButton button;
		add(button = new FlxButton(100, 10, "stop music", stopMusic));
		button.setSounds(Asset.SndButtonHover, 1.0f, null, 1.0f, Asset.SndButtonDown);
		
		// Add a play button for the sound.
		add(new FlxButton(10, 100, "play sound", playSound));	
		
		// Load the sound in the memory, it will be recycled when needed.
		flixel = FlxG.loadSound(Asset.Sndflixel, 1f);
	}

	@Override
	public void update()
	{
		if(FlxG.mouse.justPressed(Keys.A))
		{
			FlxG.log("switch state");
			if(FlxG.music != null)
			{
				FlxG.music.stop();
				FlxG.music.destroy();				
			}
			FlxG.switchState(new EmptyState());
		}		
		super.update();
	}
	
	
	@Override
	public void destroy() 
	{		
		super.destroy();
	}
	
	AFlxButton playMusic = new AFlxButton()
	{
		public void onUp() 
		{			
			if(FlxG.music == null)
			{
				_btnPlayMusic.label.setText("pause music");
				FlxG.playMusic(Asset.SndMode, 1f);
			}
			else if(!FlxG.music.active)
			{
				_btnPlayMusic.label.setText("pause music");
				if(_paused)
					FlxG.music.resume();
				else
					FlxG.music.play();
				_paused = false;				
			}
			else
			{
				_btnPlayMusic.label.setText("play music");
				FlxG.music.pause();
				_paused = true;
			}
		};
	};
	
	
	AFlxButton stopMusic = new AFlxButton()
	{
		public void onUp() 
		{
			if(FlxG.music != null)
			{
				_btnPlayMusic.label.setText("play music");
				FlxG.music.stop();
				_paused = false;
			}
		};
	};
	
	AFlxButton playSound = new AFlxButton()
	{
		public void onUp() 
		{
			flixel.play(true);
			// it can also be done by this:
//			FlxG.play(Asset.Sndflixel, 1, true);
			// but you can't force to restart.
		};
	};
	
	
	
}
