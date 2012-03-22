package org.flixel;

import org.flixel.event.AFlxCamera;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


/**
 * The camera class is used to display the game's visuals in the Flash player.
 * By default one camera is created automatically, that is the same size as the Flash player.
 * You can add more cameras or even replace the main camera using utilities in <code>FlxG</code>.
 * 
 * @author Ka Wing Chin
 */
public class FlxCamera extends FlxBasic
{
	/**
	 * Camera "follow" style preset: camera has no deadzone, just tracks the focus object directly.
	 */
	static public final int STYLE_LOCKON = 0;
	/**
	 * Camera "follow" style preset: camera deadzone is narrow but tall.
	 */
	static public final int STYLE_PLATFORMER = 1;
	/**
	 * Camera "follow" style preset: camera deadzone is a medium-size square around the focus object.
	 */
	static public final int STYLE_TOPDOWN = 2;
	/**
	 * Camera "follow" style preset: camera deadzone is a small square around the focus object.
	 */
	static public final int STYLE_TOPDOWN_TIGHT = 3;
	
	/**
	 * Camera "shake" effect preset: shake camera on both the X and Y axes.
	 */
	static public final int SHAKE_BOTH_AXES = 0;
	/**
	 * Camera "shake" effect preset: shake camera on the X axis only.
	 */
	static public final int SHAKE_HORIZONTAL_ONLY = 1;
	/**
	 * Camera "shake" effect preset: shake camera on the Y axis only.
	 */
	static public final int SHAKE_VERTICAL_ONLY = 2;
	
	/**
	 * While you can alter the zoom of each camera after the fact,
	 * this variable determines what value the camera will start at when created.
	 */
	static public float defaultZoom;
	
	/**
	 * The X position of this camera's display.  Zoom does NOT affect this number.
	 * Measured in pixels from the left side of the flash window.
	 */
	public int x;
	/**
	 * The Y position of this camera's display.  Zoom does NOT affect this number.
	 * Measured in pixels from the top of the flash window.
	 */
	public int y;
	/**
	 * How wide the camera display is, in game pixels.
	 */
	public int width;
	/**
	 * How tall the camera display is, in game pixels.
	 */
	public int height;
	/**
	 * Tells the camera to follow this <code>FlxObject</code> object around.
	 */
	public FlxObject target;
	/**
	 * You can assign a "dead zone" to the camera in order to better control its movement.
	 * The camera will always keep the focus object inside the dead zone,
	 * unless it is bumping up against the bounds rectangle's edges.
	 * The deadzone's coordinates are measured from the camera's upper left corner in game pixels.
	 * For rapid prototyping, you can use the preset deadzones (e.g. <code>STYLE_PLATFORMER</code>) with <code>follow()</code>.
	 */
	public FlxRect deadzone;
	/**
	 * The edges of the camera's range, i.e. where to stop scrolling.
	 * Measured in game pixels and world coordinates.
	 */
	public FlxRect bounds;
	
	/**
	 * Stores the basic parallax scrolling values.
	 */
	public FlxPoint scroll;
	/**
	 * The actual bitmap data of the camera display itself.
	 */
	public OrthographicCamera glCamera;
	/**
	 * The natural background color of the camera. Defaults to FlxG.bgColor.
	 * NOTE: can be transparent for crazy FX!
	 */
	public int bgColor;
	/**
	 * Indicates how far the camera is zoomed in.
	 */
	protected float _zoom;
	/**
	 * Internal, to help avoid costly allocations.
	 */
	protected FlxPoint _point;
	/**
	 * Internal, help with color transforming the flash bitmap.
	 */
	protected int _color;
	
	/**
	 * Internal, used to render buffer to screen space.
	 */
	Sprite _flashBitmap;
	/**
	 * Internal, used to render buffer to screen space.
	 */
	Sprite _flashSprite;
	/**
	 * Internal, used to control the "flash" special effect.
	 */
	protected int _fxFlashColor;
	/**
	 * Internal, used to control the "flash" special effect.
	 */
	protected float _fxFlashDuration;
	/**
	 * Internal, used to control the "flash" special effect.
	 */
	protected AFlxCamera _fxFlashComplete;
	/**
	 * Internal, used to control the "flash" special effect.
	 */
	protected float _fxFlashAlpha;
	/**
	 * Internal, used to control the "fade" special effect.
	 */
	protected int _fxFadeColor;
	/**
	 * Internal, used to control the "fade" special effect.
	 */
	protected float _fxFadeDuration;
	/**
	 * Internal, used to control the "fade" special effect.
	 */
	protected AFlxCamera _fxFadeComplete;
	/**
	 * Internal, used to control the "fade" special effect.
	 */
	protected float _fxFadeAlpha;
	/**
	 * Internal, used to control the "shake" special effect.
	 */
	protected float _fxShakeIntensity;
	/**
	 * Internal, used to control the "shake" special effect.
	 */
	protected float _fxShakeDuration;
	/**
	 * Internal, used to control the "shake" special effect.
	 */
	protected AFlxCamera _fxShakeComplete;
	/**
	 * Internal, used to control the "shake" special effect.
	 */
	protected FlxPoint _fxShakeOffset;
	/**
	 * Internal, used to control the "shake" special effect.
	 */
	protected int _fxShakeDirection;
	/**
	 * Internal helper to store the angle of the camera.
	 */
	private float _angle;

	
	/**
	 * Instantiates a new camera at the specified location, with the specified size and zoom level.
	 * 
	 * @param X			X location of the camera's display in pixels. Uses native, 1:1 resolution, ignores zoom.
	 * @param Y			Y location of the camera's display in pixels. Uses native, 1:1 resolution, ignores zoom.
	 * @param Width		The width of the camera display in pixels.
	 * @param Height	The height of the camera display in pixels.
	 * @param Zoom		The initial zoom level of the camera.  A zoom level of 2 will make all pixels display at 2x resolution.
	 */
	public FlxCamera(int X, int Y, int Width, int Height, float Zoom)
	{
		x = X;
		y = Y;
		width = Width;
		height = Height;
		target = null;
		deadzone = null;
		scroll = new FlxPoint();
		_point = new FlxPoint();
		bounds = null;

		
		setZoom(Zoom); //sets the scale of flash sprite, which in turn loads flashoffset values
		
				
		bgColor = FlxG.getBgColor();
		_color = 0xFFFFFFFF;
		
		Pixmap p = new Pixmap(FlxU.ceilPowerOfTwo((int) (Width*getZoom())), FlxU.ceilPowerOfTwo((int) (Height*getZoom())), Format.RGBA8888);
		p.setColor(1, 1, 1, 1);
		p.fillRectangle(0, 0, Width, Height);
		_flashBitmap = new Sprite(new Texture(p), width, height);
		_flashBitmap.setPosition(x, y);
		_flashBitmap.setColor(0,0,0,0);
		p.dispose();
		
		//TODO: this is the background of the camera, but it is replaced by FlxGame.draw() --> gl.glClearColor().
//		Pixmap p = new Pixmap(FlxU.ceilPowerOfTwo((int) (Width*getZoom())), FlxU.ceilPowerOfTwo((int) (Height*getZoom())), Format.RGBA8888);
//		p.setColor(1, 1, 1, 1);
//		p.fillRectangle(-x, -y, ((int)(Width*2)), ((int)(Height*2)));
//		_flashSprite = new Sprite(new Texture(p), ((int)(width)), ((int)(height)));
//		_flashSprite.setPosition(0, 0);
//		_flashSprite.flip(false, true);
//		p.dispose();
		setColor(bgColor);
				
		_fxFlashColor = 0;
		_fxFlashDuration = 0.0f;
		_fxFlashComplete = null;
		_fxFlashAlpha = 0.0f;
		
		_fxFadeColor = 0;
		_fxFadeDuration = 0.0f;
		_fxFadeComplete = null;
		_fxFadeAlpha = 0.0f;
		
		_fxShakeIntensity = 0.0f;
		_fxShakeDuration = 0.0f;
		_fxShakeComplete = null;
		_fxShakeOffset = new FlxPoint();
		_fxShakeDirection = 0;
	}

	/**
	 * Instantiates a new camera at the specified location, with the specified size and zoom level.
	 * 
	 * @param X			X location of the camera's display in pixels. Uses native, 1:1 resolution, ignores zoom.
	 * @param Y			Y location of the camera's display in pixels. Uses native, 1:1 resolution, ignores zoom.
	 * @param Width		The width of the camera display in pixels.
	 * @param Height	The height of the camera display in pixels.
	 */
	public FlxCamera(int X, int Y, int Width, int Height)
	{
		this(X, Y, Width, Height, 0);
	}
	
	
	@Override
	public void destroy()
	{
		target = null;
		scroll = null;
		deadzone = null;
		bounds = null;
		glCamera = null;
		_fxFlashComplete = null;
		_fxFadeComplete = null;
		_fxShakeComplete = null;
		_fxShakeOffset = null;
	}
	
	
	/**
	 * Updates the camera scroll as well as special effects like screen-shake or fades.
	 */
	@Override
	public void update()
	{
		//Either follow the object closely, 
		//or doublecheck our deadzone and update accordingly.
		if(target != null)
		{
			if(deadzone == null)
				focusOn(target.getMidpoint(_point));
			else
			{
				float edge;
				float targetX = (target.x + ((target.x > 0)?0.0000001f:-0.0000001f));
				float targetY = (target.y + ((target.y > 0)?0.0000001f:-0.0000001f));
				
				edge = targetX - deadzone.x;
				if(scroll.x > edge)
					scroll.x = edge;
				edge = targetX + target.width - deadzone.x - deadzone.width;
				if(scroll.x < edge)
					scroll.x = edge;
				
				edge = targetY - deadzone.y;
				if(scroll.y > edge)
					scroll.y = edge;
				edge = targetY + target.height - deadzone.y - deadzone.height;
				if(scroll.y < edge)
					scroll.y = edge;
			}
		}
		
		//Make sure we didn't go outside the camera's bounds
		if(bounds != null)
		{
			if(scroll.x < bounds.getLeft())
				scroll.x = bounds.getLeft();
			if(scroll.x > bounds.getRight() - width)
				scroll.x = bounds.getRight() - width;
			if(scroll.y < bounds.getTop())
				scroll.y = bounds.getTop();
			if(scroll.y > bounds.getBottom() - height)
				scroll.y = bounds.getBottom() - height;
		}
		
		//Update the "flash" special effect
		if(_fxFlashAlpha > 0.0)
		{
			_fxFlashAlpha -= FlxG.elapsed/_fxFlashDuration;
			if((_fxFlashAlpha <= 0) && (_fxFlashComplete != null))
				_fxFlashComplete.onFlashComplete();
		}
		
		//Update the "fade" special effect
		if((_fxFadeAlpha > 0.0) && (_fxFadeAlpha < 1.0))
		{
			_fxFadeAlpha += FlxG.elapsed/_fxFadeDuration;
			if(_fxFadeAlpha >= 1.0)
			{
				_fxFadeAlpha = 1.0f;
				if(_fxFadeComplete != null)
					_fxFadeComplete.onFadeComplete();
			}
		}
		
		//Update the "shake" special effect
		if(_fxShakeDuration > 0)
		{
			_fxShakeDuration -= FlxG.elapsed;
			if(_fxShakeDuration <= 0)
			{
				_fxShakeOffset.make();
				// Putting the camera back to its original place.
				glCamera.position.x = (FlxG.width/2f-x);
				glCamera.position.y = (FlxG.height/2f-y);
				glCamera.update();
				if(_fxShakeComplete != null)
					_fxShakeComplete.onShakeComplete();
			}
			else
			{
				if((_fxShakeDirection == SHAKE_BOTH_AXES) || (_fxShakeDirection == SHAKE_HORIZONTAL_ONLY))
					_fxShakeOffset.x = (float) ((FlxG.random()* (_fxShakeIntensity*width)*2-(_fxShakeIntensity*width))*_zoom);
				if((_fxShakeDirection == SHAKE_BOTH_AXES) || (_fxShakeDirection == SHAKE_VERTICAL_ONLY))
					_fxShakeOffset.y = (float) ((FlxG.random()*_fxShakeIntensity*height*2-_fxShakeIntensity*height)*_zoom);
			}
		}
	}
	
	
	/**
	 * Draw the background of the camera.
	 */
	//@Override
	//public void draw()
	//{
		//_flashSprite.draw(FlxG.batch);
	//}
	
	/**
	 * Draw the front of the camera. Use for fade and flash.
	 */
	public void drawFront()
	{
		_flashBitmap.draw(FlxG.batch);
	}

	
	/**
	 * Tells this camera object what <code>FlxObject</code> to track.
	 * 
	 * @param	Target		The object you want the camera to track.  Set to null to not follow anything.
	 * @param	Style		Leverage one of the existing "deadzone" presets.  If you use a custom deadzone, ignore this parameter and manually specify the deadzone after calling <code>follow()</code>.
	 */
	public void follow(FlxObject Target, int Style)
	{
		target = Target;
		float helper;
		switch(Style)
		{
			case STYLE_PLATFORMER:
				float w = width/8f;
				float h = height/3f;
				deadzone = new FlxRect((width-w)/2f,(height-h)/2f - h*0.25f,w,h);
				break;
			case STYLE_TOPDOWN:
				helper = FlxU.max(width,height)/4;
				deadzone = new FlxRect((width-helper)/2f,(height-helper)/2f,helper,helper);
				break;
			case STYLE_TOPDOWN_TIGHT:
				helper = FlxU.max(width,height)/8;
				deadzone = new FlxRect((width-helper)/2f,(height-helper)/2f,helper,helper);
				break;
			case STYLE_LOCKON:
			default:
				deadzone = null;
				break;
		}
	}
	
	/**
	 * Tells this camera object what <code>FlxObject</code> to track.
	 * 
	 * @param	Target		The object you want the camera to track.  Set to null to not follow anything.
	 */
	public void follow(FlxObject Target)
	{
		follow(Target, STYLE_LOCKON);
	}
	
	
	/**
	 * Move the camera focus to this location instantly.
	 * 
	 * @param	Point		Where you want the camera to focus.
	 */
	public void focusOn(FlxPoint Point)
	{
		Point.x += (Point.x > 0)?0.0000001:-0.0000001;
		Point.y += (Point.y > 0)?0.0000001:-0.0000001;
		scroll.make(Point.x - width*0.5f,Point.y - height*0.5f);
	}
	
	/**
	 * Specify the boundaries of the level or where the camera is allowed to move.
	 * 
	 * @param	X				The smallest X value of your level (usually 0).
	 * @param	Y				The smallest Y value of your level (usually 0).
	 * @param	Width			The largest X value of your level (usually the level width).
	 * @param	Height			The largest Y value of your level (usually the level height).
	 * @param	UpdateWorld		Whether the global quad-tree's dimensions should be updated to match (default: false).
	 */
	public void setBounds(float X, float Y, int Width, int Height, boolean UpdateWorld)
	{
		if(bounds == null)
			bounds = new FlxRect();
		bounds.make(X,Y,Width,Height);
		if(UpdateWorld)
			FlxG.worldBounds.copyFrom(bounds);
		update();
	}
	
	/**
	 * Specify the boundaries of the level or where the camera is allowed to move.
	 * 
	 * @param	X				The smallest X value of your level (usually 0).
	 * @param	Y				The smallest Y value of your level (usually 0).
	 * @param	Width			The largest X value of your level (usually the level width).
	 * @param	Height			The largest Y value of your level (usually the level height).
	 */
	public void setBounds(float X, float Y, int Width, int Height)
	{
		setBounds(X, Y, Width, Height, false);
	}
	
	/**
	 * Specify the boundaries of the level or where the camera is allowed to move.
	 * 
	 * @param	X				The smallest X value of your level (usually 0).
	 * @param	Y				The smallest Y value of your level (usually 0).
	 * @param	Width			The largest X value of your level (usually the level width).
	 */
	public void setBounds(float X, float Y, int Width)
	{
		setBounds(X, Y, Width, 0, false);
	}
	
	/**
	 * Specify the boundaries of the level or where the camera is allowed to move.
	 * 
	 * @param	X				The smallest X value of your level (usually 0).
	 * @param	Y				The smallest Y value of your level (usually 0).
	 */
	public void setBounds(float X, float Y)
	{
		setBounds(X, Y, 0, 0, false);
	}
	
	/**
	 * Specify the boundaries of the level or where the camera is allowed to move.
	 * 
	 * @param	X				The smallest X value of your level (usually 0).
	 */
	public void setBounds(float X)
	{
		setBounds(X, 0, 0, 0, false);
	}
	
	/**
	 * Specify the boundaries of the level or where the camera is allowed to move.
	 */
	public void setBounds()
	{
		setBounds(0, 0, 0, 0, false);
	}
	
	
	/**
	 * The screen is filled with this color and gradually returns to normal.
	 * 
	 * @param	Color		The color you want to use.
	 * @param	Duration	How long it takes for the flash to fade.
	 * @param	OnComplete	A function you want to run when the flash finishes.
	 * @param	Force		Force the effect to reset.
	 */
	public void flash(int Color, float Duration, AFlxCamera OnComplete, boolean Force)
	{
		if(!Force && (_fxFlashAlpha > 0.0))
			return;
		_fxFlashColor = Color;
		if(Duration <= 0)
			Duration = Float.MIN_VALUE;
		_fxFlashDuration = Duration;
		_fxFlashComplete = OnComplete;
		_fxFlashAlpha = 1.0f;
	}
	
	/**
	 * The screen is filled with this color and gradually returns to normal.
	 * 
	 * @param	Color		The color you want to use.
	 * @param	Duration	How long it takes for the flash to fade.
	 * @param	OnComplete	A function you want to run when the flash finishes.
	 */
	public void flash(int Color, float Duration, AFlxCamera OnComplete)
	{
		flash(Color, Duration, OnComplete, false);
	}
	
	/**
	 * The screen is filled with this color and gradually returns to normal.
	 * 
	 * @param	Color		The color you want to use.
	 * @param	Duration	How long it takes for the flash to fade.
	 */
	public void flash(int Color, float Duration)
	{
		flash(Color, Duration, null, false);
	}
	
	/**
	 * The screen is filled with this color and gradually returns to normal.
	 * 
	 * @param	Color		The color you want to use.
	 */
	public void flash(int Color)
	{
		flash(Color, 1, null, false);
	}
	
	/**
	 * The screen is filled with this color and gradually returns to normal.
	 */
	public void flash()
	{
		flash(0xFFFFFFFF, 1, null, false);
	}

	
	/**
	 * The screen is gradually filled with this color.
	 * 
	 * @param	Color		The color you want to use.
	 * @param	Duration	How long it takes for the fade to finish.
	 * @param	OnComplete	A function you want to run when the fade finishes.
	 * @param	Force		Force the effect to reset.
	 */
	public void fade(int Color, float Duration, AFlxCamera OnComplete, boolean Force)
	{
		if(!Force && (_fxFadeAlpha > 0.0))
			return;
		_fxFadeColor = Color;
		if(Duration <= 0)
			Duration = Float.MIN_VALUE;
		_fxFadeDuration = Duration;
		_fxFadeComplete = OnComplete;
		_fxFadeAlpha = Float.MIN_VALUE;
	}
	
	/**
	 * The screen is gradually filled with this color.
	 * 
	 * @param	Color		The color you want to use.
	 * @param	Duration	How long it takes for the fade to finish.
	 * @param	OnComplete	A function you want to run when the fade finishes.
	 */
	public void fade(int Color, float Duration, AFlxCamera OnComplete)
	{
		fade(Color, Duration, OnComplete, false);
	}
	
	/**
	 * The screen is gradually filled with this color.
	 * 
	 * @param	Color		The color you want to use.
	 * @param	Duration	How long it takes for the fade to finish.
	 */
	public void fade(int Color, float Duration)
	{
		fade(Color, Duration, null, false);
	}
	
	/**
	 * The screen is gradually filled with this color.
	 * 
	 * @param	Color		The color you want to use.
	 */
	public void fade(int Color)
	{
		fade(Color, 1, null, false);
	}
	
	/**
	 * The screen is gradually filled with this color.
	 */
	public void fade()
	{
		fade(0xFF000000, 1, null, false);
	}
	
	
	/**
	 * A simple screen-shake effect.
	 * 
	 * @param	Intensity	Percentage of screen size representing the maximum distance that the screen can move while shaking.
	 * @param	Duration	The length in seconds that the shaking effect should last.
	 * @param	OnComplete	A function you want to run when the shake effect finishes.
	 * @param	Force		Force the effect to reset (default = true, unlike flash() and fade()!).
	 * @param	Direction	Whether to shake on both axes, just up and down, or just side to side (use class constants SHAKE_BOTH_AXES, SHAKE_VERTICAL_ONLY, or SHAKE_HORIZONTAL_ONLY).
	 */
	public void shake(float Intensity, float Duration, AFlxCamera OnComplete, boolean Force, int Direction)
	{
		if(!Force && ((_fxShakeOffset.x != 0) || (_fxShakeOffset.y != 0)))
			return;
		_fxShakeIntensity = Intensity;
		_fxShakeDuration = Duration;
		_fxShakeComplete = OnComplete;
		_fxShakeDirection = Direction;
		_fxShakeOffset.make();
	}
	
	/**
	 * A simple screen-shake effect.
	 * 
	 * @param	Intensity	Percentage of screen size representing the maximum distance that the screen can move while shaking.
	 * @param	Duration	The length in seconds that the shaking effect should last.
	 * @param	OnComplete	A function you want to run when the shake effect finishes.
	 * @param	Force		Force the effect to reset (default = true, unlike flash() and fade()!).
	 */
	public void shake(float Intensity, float Duration, AFlxCamera OnComplete, boolean Force)
	{
		shake(Intensity, Duration, OnComplete, Force, SHAKE_BOTH_AXES);
	}
	
	/**
	 * A simple screen-shake effect.
	 * 
	 * @param	Intensity	Percentage of screen size representing the maximum distance that the screen can move while shaking.
	 * @param	Duration	The length in seconds that the shaking effect should last.
	 * @param	OnComplete	A function you want to run when the shake effect finishes.
	 */
	public void shake(float Intensity, float Duration, AFlxCamera OnComplete)
	{
		shake(Intensity, Duration, OnComplete, false, SHAKE_BOTH_AXES);
	}
	
	/**
	 * A simple screen-shake effect.
	 * 
	 * @param	Intensity	Percentage of screen size representing the maximum distance that the screen can move while shaking.
	 * @param	Duration	The length in seconds that the shaking effect should last.
	 */
	public void shake(float Intensity, float Duration)
	{
		shake(Intensity, Duration, null, false, SHAKE_BOTH_AXES);
	}
	
	/**
	 * A simple screen-shake effect.
	 * 
	 * @param	Intensity	Percentage of screen size representing the maximum distance that the screen can move while shaking.
	 */
	public void shake(float Intensity)
	{
		shake(Intensity, 1, null, false, SHAKE_BOTH_AXES);
	}
	
	/**
	 * A simple screen-shake effect.
	 * 
	 * @param	Intensity	Percentage of screen size representing the maximum distance that the screen can move while shaking.
	 */
	public void shake()
	{
		shake(0.05f, 1, null, false, SHAKE_BOTH_AXES);
	}
	
	/**
	 * Just turns off all the camera effects instantly.
	 */
	public void stopFX()
	{
		_fxFlashAlpha = 0.0f;
		_fxFadeAlpha = 0.0f;
		_fxShakeDuration = 0;
		// Putting the camera back to its original place.
		glCamera.position.x = (FlxG.width/2f-x);
		glCamera.position.y = (FlxG.height/2f-y);
		glCamera.update();
	}
	
	
	/**
	 * Copy the bounds, focus object, and deadzone info from an existing camera.
	 * 
	 * @param	Camera	The camera you want to copy from.
	 * 
	 * @return	A reference to this <code>FlxCamera</code> object.
	 */
	public FlxCamera copyFrom(FlxCamera Camera)
	{
		if(Camera.bounds == null)
			bounds = null;
		else
		{
			if(bounds == null)
				bounds = new FlxRect();
			bounds.copyFrom(Camera.bounds);
		}
		target = Camera.target;
		if(target != null)
		{
			if(Camera.deadzone == null)
				deadzone = null;
			else
			{
				if(deadzone == null)
					deadzone = new FlxRect();
				deadzone.copyFrom(Camera.deadzone);
			}
		}
		return this;
	}
	
	/**
	 * The zoom level of this camera. 1 = 1:1, 2 = 2x zoom, etc.
	 */
	public float getZoom()
	{
		return _zoom;
	}
	
	/**
	 * @private
	 */
	public void setZoom(float Zoom)
	{
		if(Zoom == 0)
			_zoom = defaultZoom;
		else
			_zoom = Zoom;
		setScale(_zoom, _zoom);
	}
	
	/**
	 * The alpha value of this camera display (a Number between 0.0 and 1.0).
	 */
	public float getAlpha()
	{
		return _flashBitmap.getColor().a;
	}
	
	
	/**
	 * @private
	 */ 
	public void setAlpha(float Alpha)
	{		
		Color color = _flashBitmap.getColor();
		_flashBitmap.setColor(color.r, color.g, color.b, Alpha);
		_flashBitmap.setColor(0,0,0,0);
	}
	
	
	/**
	 * The angle of the camera display (in degrees).
	 * Currently yields weird display results,
	 * since cameras aren't nested in an extra display object yet.
	 */
	public float getAngle()
	{
		return _angle;
	}

	/**
	 * @private
	 */
	public void setAngle(float Angle)
	{
		_angle = Angle;
		glCamera.rotate(Angle, 0, 0, 1);
		glCamera.update();
	}
	
	/**
	 * The color tint of the camera display. 
	 */
	public int getColor()
	{
		return _color;
	}
	
	/**
	 * @private
	 */
	public void setColor(int Color)
	{
		bgColor = Color;
//		_flashSprite.setColor(FlxU.colorFromHex(Color)); //TODO: the background color for the camera.
	}
	
	/**
	 * The scale of the camera object, irrespective of zoom.
	 * Currently yields weird display results,
	 * since cameras aren't nested in an extra display object yet.
	 */
//	public function getScale():FlxPoint
//	{
//		return _point.make(_flashSprite.scaleX,_flashSprite.scaleY);
//	}
	
	/**
	 * @private
	 */
	public void setScale(float X, float Y)
	{
		if(glCamera == null)
		{
			glCamera = new OrthographicCamera();						
		}
		glCamera.setToOrtho(true, FlxG.width/X, FlxG.height/Y);
		glCamera.update();
	}
	
	
	/**
	 * Internal helper function, handles the actual drawing of all the special effects.
	 */
	void drawFX()
	{
		float[] rgba;
		//Draw the "flash" special effect onto the buffer
		if(_fxFlashAlpha > 0.0)
		{
			rgba =  FlxU.getRGBA(_fxFlashColor);
			_flashBitmap.setColor(rgba[0],rgba[1],rgba[2],_fxFlashAlpha);
			drawFront();
		}
		
		//Draw the "fade" special effect onto the buffer
		if(_fxFadeAlpha > 0.0)
		{
			rgba =  FlxU.getRGBA(_fxFadeColor);
			_flashBitmap.setColor(rgba[0],rgba[1],rgba[2],_fxFadeAlpha);
			drawFront();
		}
		
		if((_fxShakeOffset.x != 0) || (_fxShakeOffset.y != 0))
		{
			glCamera.position.x = FlxG.width/2f-y + _fxShakeOffset.x;
			glCamera.position.y = FlxG.height/2f-x + _fxShakeOffset.y;
			glCamera.update();
		}
	}
	
}
