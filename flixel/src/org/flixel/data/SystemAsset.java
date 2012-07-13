package org.flixel.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * This class is only for internal use for flixel self.
 * 
 * @author Ka Wing Chin
 */
public class SystemAsset
{
	/**
	 * Default font (Nokia Cellphone FC)
	 */
	static public BitmapFont system;
	static public FileHandle systemFileHandle;
	
	static public void createSystemAsset()
	{
		//ImgControlBase = new TextureRegion(new Texture(Gdx.files.classpath("org/flixel/data/control_base.png")));
		//ImgControlKnob = new TextureRegion(new Texture(Gdx.files.classpath("org/flixel/data/control_knob.png")));
	
		system = new BitmapFont(Gdx.files.classpath("org/flixel/data/font/nokiafc22.fnt"), Gdx.files.classpath("org/flixel/data/font/nokiafc22.png"), true);
		systemFileHandle = Gdx.files.classpath("org/flixel/data/font/nokiafc22.ttf");
		//FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.classpath("org/flixel/data/font/nokiafc22.ttf"));
		//system = generator.generateFont(8, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890\"!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*", true);
		//generator.dispose();
	}
}
