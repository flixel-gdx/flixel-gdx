package org.flixel.examples.audio;

import org.flixel.FlxButton;
import org.flixel.FlxG;
import org.flixel.FlxSound;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.flixel.event.AFlxButton;


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
		FlxG.setBgColor(0xFFFFFFFF);

		/*FlxText text = new FlxText(0, 0, 210, "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Cras sit amet" +
     "dui.  Nam sapien. Fusce vestibulum ornare metus. Maecenas ligula orci," +
    " consequat vitae, dictum nec, lacinia non, elit. Aliquam iaculis" +
    " molestie neque. Maecenas suscipit felis ut pede convallis malesuada." +
    " Aliquam erat volutpat. Nunc pulvinar condimentum nunc. Donec ac sem vel" +
    " leo bibendum aliquam. Pellentesque habitant morbi tristique senectus et" +
    " netus et malesuada fames ac turpis egestas.\n\n" +

    "Sed commodo. Nulla ut libero sit amet justo varius blandit. Mauris vitae" +
    " nulla eget lorem pretium ornare. Proin vulputate erat porta risus." +
    " Vestibulum malesuada, odio at vehicula lobortis, nisi metus hendrerit" +
    " est, vitae feugiat quam massa a ligula. Aenean in tellus. Praesent" +
    " convallis. Nullam vel lacus.  Aliquam congue erat non urna mollis" +
    " faucibus. Morbi vitae mauris faucibus quam condimentum ornare. Quisque" +
    " sit amet augue. Morbi ullamcorper mattis enim. Aliquam erat volutpat." +
    " Morbi nec felis non enim pulvinar lobortis.  Ut libero. Nullam id orci" +
    " quis nisl dapibus rutrum. Suspendisse consequat vulputate leo. Aenean" +
    " non orci non tellus iaculis vestibulum. Sed neque.\n\n");
		text.setColor(0xff000000);
		add(text);
		*/
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
