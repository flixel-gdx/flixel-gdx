package org.flixel.system;

import org.flixel.FlxG;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.FlxText;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * This class handles the 8-bit style preloader.
 */
public class FlxPreloader extends FlxState
{
	protected TextureAtlas atlas;
	/**
	 * @private
	 */
	// protected var _buffer:Sprite;
	/**
	 * @private
	 */
	// protected var _bmpBar:Bitmap;
	/**
	 * @private
	 */
	protected FlxText _text;
	/**
	 * Useful for storing "real" stage width if you're scaling your preloader graphics.
	 */
	protected int _width;
	/**
	 * Useful for storing "real" stage height if you're scaling your preloader graphics.
	 */
	protected int _height;
	/**
	 * @private
	 */
	protected FlxSprite _logo;
	/**
	 * @private
	 */
	// protected var _logoGlow:Bitmap;
	/**
	 * @private
	 */
	protected int _min;
	/**
	 * @private
	 */
	protected long _time;

	/**
	 * This should always be the name of your main project/document class (e.g. GravityHook).
	 */
	public String className;
	/**
	 * Set this to your game's URL to use built-in site-locking.
	 */
	public String myURL;
	/**
	 * Change this if you want the flixel logo to show for more or less time.  Default value is 0 seconds.
	 */
	public float minDisplayTime;

	/**
	 * Constructor
	 */
	public FlxPreloader()
	{
		minDisplayTime = 0;
	}

	@Override
	public void update()
	{
		/*
		 * FlxG._assetManager.update(); float percent =
		 * FlxG._assetManager.getProgress(); long time =
		 * System.currentTimeMillis() - _time; if((percent >= 1.0f) && (time >
		 * _min)) { FlxG.resetGame(); } else { if((_min > 0) && (percent >
		 * time/_min)) percent = time/_min; update(percent); }
		 */
	}

	/**
	 * Override this to create your own preloader objects.
	 * Highly recommended you also override update()!
	 */
	@Override
	public void create()
	{
		_min = 0;
		if(!FlxG.debug)
			_min = (int)(minDisplayTime*1000);
		_time = System.currentTimeMillis();

		FlxG.camera.setZoom(2);

		_width = Gdx.graphics.getWidth() / 2;
		_height = Gdx.graphics.getHeight() / 2;

		FlxG.setBgColor(0x00345e);

		atlas = new TextureAtlas(Gdx.files.classpath("org/flixel/data/preloader"));

		// FlxSprite bitmap = new FlxSprite((_width - _height) / 2,
		// 0,atlas.findRegion("logo_light"));
		// bitmap.scale.x = bitmap.scale.y = _width / (float) bitmap.width;
		// add(bitmap);
		/*
		 * _bmpBar = new Bitmap(new BitmapData(1,7,false,0x5f6aff)); _bmpBar.x =
		 * 4; _bmpBar.y = _height-11; _buffer.addChild(_bmpBar);
		 */
		_text = new FlxText(2, /* _bmpBar.y */4 - 11, 80);
		_text.setFormat(null, 8, 0x5f6aff);
		add(_text);

		// _logo = new FlxSprite(0, 0, atlas.findRegion("logo"));
		_logo.scale.x = _logo.scale.y = _height / 8.0f;
		_logo.x = (_width - _logo.width * _logo.scale.x) / 2.0f;
		_logo.y = (_height - _logo.height * _logo.scale.y) / 2.0f;
		add(_logo);
		/*
		 * _logoGlow = new ImgLogo(); _logoGlow.smoothing = true;
		 * _logoGlow.blendMode = "screen"; _logoGlow.scaleX = _logoGlow.scaleY =
		 * _height/8; _logoGlow.x = (_width-_logoGlow.width)/2; _logoGlow.y =
		 * (_height-_logoGlow.height)/2; _buffer.addChild(_logoGlow); bitmap =
		 * new ImgLogoCorners(); bitmap.smoothing = true; bitmap.width = _width;
		 * bitmap.height = _height; _buffer.addChild(bitmap); bitmap = new
		 * Bitmap(new BitmapData(_width,_height,false,0xffffff)); var i:uint =
		 * 0; var j:uint = 0; while(i < _height) { j = 0; while(j < _width)
		 * bitmap.bitmapData.setPixel(j++,i,0); i+=2; } bitmap.blendMode =
		 * "overlay"; bitmap.alpha = 0.25; _buffer.addChild(bitmap);
		 */
	}

	@Override
	public void destroy()
	{
		/*
		 * if(_buffer != null) removeChild(_buffer); _buffer = null; _bmpBar =
		 * null; _text = null; _logo = null; _logoGlow = null;
		 */
	}

	/**
	 * Override this function to manually update the preloader.
	 * 
	 * @param	Percent		How much of the program has loaded.
	 */
	protected void update(float Percent)
	{
		/*
		 * _bmpBar.scaleX = Percent*(_width-8); _text.text =
		 * "FLX v"+FlxG.LIBRARY_MAJOR_VERSION
		 * +"."+FlxG.LIBRARY_MINOR_VERSION+" "+Math.floor(Percent*100)+"%";
		 * _text.setTextFormat(_text.defaultTextFormat); if(Percent < 0.1) {
		 * _logoGlow.alpha = 0; _logo.alpha = 0; } else if(Percent < 0.15) {
		 * _logoGlow.alpha = Math.random(); _logo.alpha = 0; } else if(Percent <
		 * 0.2) { _logoGlow.alpha = 0; _logo.alpha = 0; } else if(Percent <
		 * 0.25) { _logoGlow.alpha = 0; _logo.alpha = Math.random(); } else
		 * if(Percent < 0.7) { _logoGlow.alpha = (Percent-0.45)/0.45;
		 * _logo.alpha = 1; } else if((Percent > 0.8) && (Percent < 0.9)) {
		 * _logoGlow.alpha = 1-(Percent-0.8)/0.1; _logo.alpha = 0; } else
		 * if(Percent > 0.9) { _buffer.alpha = 1-(Percent-0.9)/0.1; }
		 */
	}
}
