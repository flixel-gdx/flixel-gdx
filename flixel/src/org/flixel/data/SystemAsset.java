package org.flixel.data;

import org.flixel.FlxTilemap;

import com.badlogic.gdx.Gdx;
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
	static public TextureRegion ImgDefault;
	static public TextureRegion ImgButton;
	
	static public void createSystemAsset()
	{
		TextureAtlas atlas = new TextureAtlas(Gdx.files.classpath("org/flixel/data/pack"));
		ImgDefault = atlas.findRegion("default");
		ImgButton = atlas.findRegion("button");
		FlxTilemap.ImgAuto = atlas.findRegion("autotiles");
		FlxTilemap.ImgAutoAlt = atlas.findRegion("autotiles_alt");
		system = new BitmapFont(Gdx.files.classpath("org/flixel/data/font/nokiafc22.fnt"), Gdx.files.classpath("org/flixel/data/font/nokiafc22.png"), true);
		//FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.classpath("org/flixel/data/font/nokiafc22.ttf"));
		//system = generator.generateFont(8, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890\"!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*", true);
		//generator.dispose();
	}
}
