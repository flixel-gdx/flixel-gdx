package org.flixel.system;

import org.flixel.FlxG;
import org.flixel.FlxGroup;
import org.flixel.FlxSprite;
import org.flixel.FlxText;
import com.badlogic.gdx.graphics.ManagedTextureData;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;


/**
 * This is the default flixel pause screen. It can be overridden with your own
 * <code>FlxGroup</code> object.
 * @author Ka Wing Chin
 */
public class FlxPause extends FlxGroup
{
	public FlxPause()
	{
		super();
		int x = FlxG.width/2;
		int y = FlxG.height/2;
		int w = 160;
		int h = 60;
		visible = false;
		ignoreDrawDebug = true;

		FlxSprite s;
		Pixmap p = new Pixmap(MathUtils.nextPowerOfTwo(w), MathUtils.nextPowerOfTwo(h), Pixmap.Format.RGBA8888);
		//p.setColor(FlxU.colorFromHex(0xFF000000));
		p.fillRectangle(0, 0, w, h);
		s = new FlxSprite(x-w/2, y-h/2);//, new TextureRegion(new Texture(new FlxTextureData(p))));
		s.setPixels(new TextureRegion(new Texture(new ManagedTextureData(p)),w, h));
		s.setAlpha(0.85f);
		s.setSolid(false);
		s.scrollFactor.x = s.scrollFactor.y = 0;
		s.ignoreDrawDebug = true;
		add(s);

		add(s = (new FlxText(s.x+40, s.y+10, w, "this game is")));
		s.scrollFactor.x = s.scrollFactor.y = 0;
		s.ignoreDrawDebug = true;

		add((s = new FlxText(s.x+20, s.y+20, w, "PAUSED")));
		s.scrollFactor.x = s.scrollFactor.y = 0;
		s.ignoreDrawDebug = true;
		
		s = new FlxSprite(s.x-20, s.y);
		s.immovable = true;
		s.allowCollisions = FlxSprite.NONE;
		s.scrollFactor.x = s.scrollFactor.y = 0;
		s.ignoreDrawDebug = true;
		add(s);
	}
	
	@Override
	public void destroy()
	{
		
	}
}
