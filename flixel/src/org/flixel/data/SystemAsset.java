package org.flixel.data;

import org.flixel.FlxTilemap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


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
	static public TextureRegion ImgDefault;
	static public TextureRegion ImgButton;
	static public TextureRegion ImgControlBase;
	static public TextureRegion ImgControlKnob;
	static public TextureRegion ImgButtonA;
	static public TextureRegion ImgButtonB;
	static public TextureRegion ImgButtonC;	
	static public TextureRegion ImgButtonY;
	static public TextureRegion ImgButtonX;
	static public TextureRegion ImgButtonLeft;
	static public TextureRegion ImgButtonUp;
	static public TextureRegion ImgButtonRight;
	static public TextureRegion ImgButtonDown;
	
	
	static public void createSystemAsset()
	{
		TextureAtlas atlas = new TextureAtlas(Gdx.files.classpath("org/flixel/data/pack"));
		ImgDefault = atlas.findRegion("default");
		ImgButton = atlas.findRegion("button");
		ImgControlBase = new TextureRegion(new Texture(Gdx.files.classpath("org/flixel/data/control_base.png")));
		ImgControlKnob = new TextureRegion(new Texture(Gdx.files.classpath("org/flixel/data/control_knob.png")));
		ImgButtonA = atlas.findRegion("button_a");
		ImgButtonB = atlas.findRegion("button_b");
		ImgButtonC = atlas.findRegion("button_c");
		ImgButtonY = atlas.findRegion("button_y");
		ImgButtonX = atlas.findRegion("button_x");
		ImgButtonLeft = atlas.findRegion("button_left");
		ImgButtonUp = atlas.findRegion("button_up");
		ImgButtonRight = atlas.findRegion("button_right");
		ImgButtonDown = atlas.findRegion("button_down");

		FlxTilemap.ImgAuto = atlas.findRegion("autotiles");
		FlxTilemap.ImgAutoAlt = atlas.findRegion("autotiles_alt");
	
		system = new BitmapFont(Gdx.files.classpath("org/flixel/data/font/nokiafc22.fnt"), Gdx.files.classpath("org/flixel/data/font/nokiafc22.png"), true);
		systemFileHandle = Gdx.files.classpath("org/flixel/data/font/nokiafc22.ttf");
		//FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.classpath("org/flixel/data/font/nokiafc22.ttf"));
		//system = generator.generateFont(8, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890\"!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*", true);
		//generator.dispose();
	}
}
