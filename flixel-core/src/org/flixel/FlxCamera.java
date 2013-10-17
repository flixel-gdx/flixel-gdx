package org.flixel;

import org.flixel.event.IFlxCamera;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import flash.display.Stage;

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
	 * Camera "scale" mode preset: The game is not scaled.
	 */
	public static final int NO_SCALE = 0;
	/**
	 * Camera "scale" mode preset: Scales the stage to fill the display
	 * in the x direction without stretching. 
	 */
	public static final int FILL_X = 1;
	/**
	 * Camera "scale" mode preset: Scales the stage to fill the display
	 * in the y direction without stretching.
	 */
	public static final int FILL_Y = 2;
	/**
	 * Camera "scale" mode preset: Stretches the game to fill the entire screen.
	 */
	public static final int STRETCH = 3;
	
	/**
	 * While you can alter the zoom of each camera after the fact,
	 * this variable determines what value the camera will start at when created.
	 */
	static public float defaultZoom;
	
	/**
	 * While you can alter the scale mode of each camera after the fact,
	 * this variable determines what value the camera will start at when created.
	 */
	static public int defaultScaleMode;
	
	/**
	 * The X position of this camera's display.  Zoom does NOT affect this number.
	 * Measured in pixels from the left side of the flash window.
	 */
	public float x;
	/**
	 * The Y position of this camera's display.  Zoom does NOT affect this number.
	 * Measured in pixels from the top of the flash window.
	 */
	public float y;
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
	 * The actual libgdx camera.
	 */
	OrthographicCamera _glCamera;
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
	 * Decides how flixel handles different screen sizes.
	 */
	protected int _scaleMode;
	
	//TODO:comment these properly
	public float _screenScaleFactorX;
	public float _screenScaleFactorY;
	public int viewportWidth;
	public int viewportHeight;
	
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
	float _flashOffsetX;
	/**
	 * Internal, used to render buffer to screen space.
	 */
	float _flashOffsetY;
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
	protected IFlxCamera _fxFlashComplete;
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
	protected IFlxCamera _fxFadeComplete;
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
	protected IFlxCamera _fxShakeComplete;
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
	protected float _angle;
	/**
	 * Internal helper to store the alpha value of the camera.
	 */
	protected float _alpha;
	
	/**
	 * Instantiates a new camera at the specified location, with the specified size and zoom level.
	 * 
	 * @param X			X location of the camera's display in pixels. Uses native, 1:1 resolution, ignores zoom.
	 * @param Y			Y location of the camera's display in pixels. Uses native, 1:1 resolution, ignores zoom.
	 * @param Width		The width of the camera display in pixels.
	 * @param Height	The height of the camera display in pixels.
	 * @param Zoom		The initial zoom level of the camera.  A zoom level of 2 will make all pixels display at 2x resolution.
	 * @param ScaleMode	The initial scale mode of the camera.
	 */
	public FlxCamera(int X, int Y, int Width, int Height, float Zoom, int ScaleMode)
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
		_glCamera = new OrthographicCamera();						
		bgColor = FlxG.getBgColor();
		_color = 0xFFFFFF;
		_alpha = 1.0f;
		
		setScaleMode(ScaleMode);
		setZoom(Zoom); //sets the scale of flash sprite, which in turn loads flashoffset values
		_flashOffsetX = viewportWidth * 0.5f;
		_flashOffsetY = viewportHeight * 0.5f;
		
		_glCamera.position.x = _flashOffsetX - (x / getZoom());
		_glCamera.position.y = _flashOffsetY - (y / getZoom());	
		
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
	 * @param Zoom		The initial zoom level of the camera.  A zoom level of 2 will make all pixels display at 2x resolution.
	 */
	public FlxCamera(int X, int Y, int Width, int Height, float Zoom)
	{
		this(X, Y, Width, Height, Zoom, 0);
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
		this(X, Y, Width, Height, 0, 0);
	}
	
	/**
	 * Clean up memory.
	 */
	@Override
	public void destroy()
	{
		target = null;
		scroll = null;
		deadzone = null;
		bounds = null;
		_glCamera = null;
		_fxFlashComplete = null;
		_fxFadeComplete = null;
		_fxShakeComplete = null;
		_fxShakeOffset = null;
		super.destroy();
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
				float targetX = target.x + ((target.x > 0)?0.0000001f:-0.0000001f);
				float targetY = target.y + ((target.y > 0)?0.0000001f:-0.0000001f);
				
				if(target.isSimpleRender())
				{
					targetX = FlxU.ceil(targetX);
					targetY = FlxU.ceil(targetY);					
				}
				
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
		if(_fxFlashAlpha > 0.0f)
		{
			_fxFlashAlpha -= FlxG.elapsed/_fxFlashDuration;
			if((_fxFlashAlpha <= 0) && (_fxFlashComplete != null))
				_fxFlashComplete.callback();
		}
		
		//Update the "fade" special effect
		if((_fxFadeAlpha > 0.0f) && (_fxFadeAlpha < 1.0f))
		{
			_fxFadeAlpha += FlxG.elapsed/_fxFadeDuration;
			if(_fxFadeAlpha >= 1.0f)
			{
				_fxFadeAlpha = 1.0f;
				if(_fxFadeComplete != null)
					_fxFadeComplete.callback();
			}
		}
		
		//Update the "shake" special effect
		if(_fxShakeDuration > 0)
		{
			_fxShakeDuration -= FlxG.elapsed;
			if(_fxShakeDuration <= 0)
			{
				_fxShakeOffset.make();
				if(_fxShakeComplete != null)
					_fxShakeComplete.callback();
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
	 * Tells this camera object what <code>FlxObject</code> to track.
	 * 
	 * @param	Target		The object you want the camera to track.  Set to null to not follow anything.
	 * @param	Style		Leverage one of the existing "deadzone" presets.  If you use a custom deadzone, ignore this parameter and manually specify the deadzone after calling <code>follow()</code>.
	 */
	public void follow(FlxObject Target, int Style)
	{
		target = Target;
		float helper;
		float w = 0;
		float h = 0;
		switch(Style)
		{
			case STYLE_PLATFORMER:
				w = width/8f;
				h = height/3f;
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
				if (target != null) 
				{	
					w = target.width;
					h = target.height;
				}
				deadzone = new FlxRect((width-w)/2f,((height-h)/2f),w,h);
				break;
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
		Point.x += (Point.x > 0)?0.0000001f:-0.0000001f;
		Point.y += (Point.y > 0)?0.0000001f:-0.0000001f;
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
	public void setBounds(float X, float Y, float Width, float Height, boolean UpdateWorld)
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
	public void setBounds(float X, float Y, float Width, float Height)
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
	public void setBounds(float X, float Y, float Width)
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
	public void flash(int Color, float Duration, IFlxCamera OnComplete, boolean Force)
	{
		if(!Force && (_fxFlashAlpha > 0.0f))
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
	public void flash(int Color, float Duration, IFlxCamera OnComplete)
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
	public void fade(int Color, float Duration, IFlxCamera OnComplete, boolean Force)
	{
		if(!Force && (_fxFadeAlpha > 0.0f))
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
	public void fade(int Color, float Duration, IFlxCamera OnComplete)
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
	public void shake(float Intensity, float Duration, IFlxCamera OnComplete, boolean Force, int Direction)
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
	public void shake(float Intensity, float Duration, IFlxCamera OnComplete, boolean Force)
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
	public void shake(float Intensity, float Duration, IFlxCamera OnComplete)
	{
		shake(Intensity, Duration, OnComplete, true, SHAKE_BOTH_AXES);
	}
	
	/**
	 * A simple screen-shake effect.
	 * 
	 * @param	Intensity	Percentage of screen size representing the maximum distance that the screen can move while shaking.
	 * @param	Duration	The length in seconds that the shaking effect should last.
	 */
	public void shake(float Intensity, float Duration)
	{
		shake(Intensity, Duration, null, true, SHAKE_BOTH_AXES);
	}
	
	/**
	 * A simple screen-shake effect.
	 * 
	 * @param	Intensity	Percentage of screen size representing the maximum distance that the screen can move while shaking.
	 */
	public void shake(float Intensity)
	{
		shake(Intensity, 0.5f, null, true, SHAKE_BOTH_AXES);
	}
	
	/**
	 * A simple screen-shake effect.
	 */
	public void shake()
	{
		shake(0.05f, 0.5f, null, true, SHAKE_BOTH_AXES);
	}
	
	/**
	 * Just turns off all the camera effects instantly.
	 */
	public void stopFX()
	{
		_fxFlashAlpha = 0.0f;
		_fxFadeAlpha = 0.0f;
		_fxShakeDuration = 0;
		_glCamera.position.x = _flashOffsetX - (x / getZoom());
		_glCamera.position.y = _flashOffsetY - (y / getZoom());
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
	 * The scale mode of this camera.
	 */
	public int getScaleMode()
	{
		return _scaleMode;
	}
	
	/**
	 * @private
	 */
	public void setScaleMode(int ScaleMode)
	{
		if(ScaleMode == 0)
			_scaleMode = defaultScaleMode;
		else
			_scaleMode = ScaleMode;
		setScale(_zoom, _zoom);
	}
	
	/**
	 * The alpha value of this camera display (a Number between 0.0 and 1.0).
	 */
	public float getAlpha()
	{
		return _alpha;
	}
	
	/**
	 * The alpha value of this camera display (a Number between 0.0 and 1.0).
	 */ 
	public void setAlpha(float Alpha)
	{		
		_alpha = Alpha;
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
	 * The angle of the camera display (in degrees).
	 * Currently yields weird display results,
	 * since cameras aren't nested in an extra display object yet.
	 */
	public void setAngle(float Angle)
	{
		_angle = Angle;
		_glCamera.rotate(Angle, 0, 0, 1);
	}
	
	/**
	 * The color tint of the camera display. 
	 */
	public int getColor()
	{
		return _color;
	}
	
	/**
	 * The color tint of the camera display.
	 */
	public void setColor(int Color)
	{
		_color = Color;
	}
	
	/**
	 * Whether the camera display is smooth and filtered, or chunky and pixelated.
	 * Default behavior is chunky-style.
	 */
	public boolean getAntialiasing()
	{
		return true;
	}

	/**
	 * Whether the camera display is smooth and filtered, or chunky and pixelated.
	 * Default behavior is chunky-style.
	 */
	public void setAntialiasing(boolean Antialiasing)
	{
		
	}
	
	/**
	 * The scale of the camera object, irrespective of zoom.
	 * Currently yields weird display results,
	 * since cameras aren't nested in an extra display object yet.
	 */
	public FlxPoint getScale()
	{
		Stage stage = FlxG.getStage();
		return _point.make(stage.stageWidth/_glCamera.viewportWidth,stage.stageHeight/_glCamera.viewportHeight);
	}
	
	/**
	 * The scale of the camera object, irrespective of zoom.
	 * Currently yields weird display results,
	 * since cameras aren't nested in an extra display object yet.
	 */
	public void setScale(float X, float Y)
	{
		Stage stage = FlxG.getStage();
		float screenAspectRatio = FlxG.screenWidth / (float)FlxG.screenHeight;
		
		switch(_scaleMode)
		{
			case NO_SCALE:
				_screenScaleFactorY = 1;
				_screenScaleFactorX = 1;
				viewportWidth = (int) (FlxG.screenWidth/X);
				viewportHeight = (int) (FlxG.screenHeight/Y);
				break;
				
			default:	
			case STRETCH:
				_screenScaleFactorY = (float)FlxG.screenHeight / stage.stageHeight;
				_screenScaleFactorX = (float)FlxG.screenWidth / stage.stageWidth;
				viewportWidth = (int) (stage.stageWidth/X);
				viewportHeight = (int) (stage.stageHeight/Y);
				break;
				
			case FILL_X:
				_screenScaleFactorX = (float)FlxG.screenWidth / (stage.stageHeight * screenAspectRatio);
				_screenScaleFactorY = (float)FlxG.screenHeight / stage.stageHeight;
				viewportWidth = (int) ((stage.stageHeight * screenAspectRatio)/X);
				viewportHeight = (int) (stage.stageHeight/Y);
				break;
				
			case FILL_Y:
				_screenScaleFactorX = (float)FlxG.screenWidth / stage.stageWidth;
				_screenScaleFactorY = (float)FlxG.screenHeight / (stage.stageWidth / screenAspectRatio);
				viewportWidth = (int) (stage.stageWidth/X);
				viewportHeight = (int) ((stage.stageWidth / screenAspectRatio)/Y);
				break;
		}

		_glCamera.setToOrtho(true, viewportWidth, viewportHeight);
	}
	
	/**
	 * Fill the camera with the specified color.
	 * 
	 * @param	Color		The color to fill with in 0xAARRGGBB hex format.
	 * @param	BlendAlpha	Whether to blend the alpha value or just wipe the previous contents.  Default is true.
	 */
	public void fill(int Color, boolean BlendAlpha)
	{
		if (BlendAlpha)
		{
			FlxG._gl.glEnable(GL10.GL_BLEND);
			FlxG._gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		}
		else
			FlxG._gl.glDisable(GL10.GL_BLEND);
		
		ShapeRenderer flashGfx = FlxG.flashGfx.getShapeRenderer();
		flashGfx.setProjectionMatrix(_glCamera.combined);
		flashGfx.begin(ShapeType.Filled);
		int color = FlxU.multiplyColors(Color, _color);
		flashGfx.setColor(((color >> 16) & 0xFF) * 0.00392f, ((color >> 8) & 0xFF) * 0.00392f, (color & 0xFF) * 0.00392f, ((Color >> 24) & 0xFF) * 0.00392f);
		flashGfx.rect(0, 0, width, height);
		flashGfx.end();
	}
	
	/**
	 * Fill the camera with the specified color.
	 * 
	 * @param	Color		The color to fill with in 0xAARRGGBB hex format.
	 */
	public void fill(int Color)
	{
		fill(Color, true);
	}
	
	/**
	 * Internal helper function, handles the actual drawing of all the special effects.
	 */
	void drawFX()
	{
		float alphaComponent;
		
		//Draw the "flash" special effect onto the buffer
		if(_fxFlashAlpha > 0.0f)
		{
			alphaComponent = _fxFlashColor>>24;
			fill(((int)(((alphaComponent <= 0)?0xff:alphaComponent)*_fxFlashAlpha)<<24)+(_fxFlashColor&0x00ffffff));
		}
		
		//Draw the "fade" special effect onto the buffer
		if(_fxFadeAlpha > 0.0f)
		{
			alphaComponent = _fxFadeColor>>24;
			fill(((int)(((alphaComponent <= 0)?0xff:alphaComponent)*_fxFadeAlpha)<<24)+(_fxFadeColor&0x00ffffff));
		}
		
		//Changing the camera position after drawing causes problems.
		//Shake offset is now applied in FlxG.lockCameras instead.
		//if((_fxShakeOffset.x != 0) || (_fxShakeOffset.y != 0))
		//{
			//_glCamera.position.x = _flashOffsetX - (x / getZoom()) + _fxShakeOffset.x;
			//_glCamera.position.y = _flashOffsetY - (y / getZoom()) + _fxShakeOffset.y;
		//}
	}	
}