package org.flixel;

import java.util.ArrayList;

import org.flixel.data.FlxAnim;

import android.graphics.Bitmap;

import flash.display.BitmapData;
import flash.geom.ColorTransform;
import flash.geom.Matrix;
import flash.geom.Point;
import flash.geom.Rectangle;

public class FlxSprite extends FlxObject
{
	
	/**
	 * Useful for controlling flipped animations and checking player orientation.
	 */
	static public final int LEFT = 0;
	/**
	 * Useful for controlling flipped animations and checking player orientation.
	 */
	static public final int RIGHT = 1;
	/**
	 * Useful for checking player orientation.
	 */
	static public final int UP = 2;
	/**
	 * Useful for checking player orientation.
	 */
	static public final int DOWN = 3;
		
	/**
	* If you changed the size of your sprite object to shrink the bounding box,
	* you might need to offset the new bounding box from the top-left corner of the sprite.
	*/
	public FlxPoint offset;
	
	/**
	 * Change the size of your sprite's graphic.
	 * NOTE: Scale doesn't currently affect collisions automatically,
	 * you will need to adjust the width, height and offset manually.
	 * WARNING: scaling sprites decreases rendering performance for this sprite by a factor of 10x!
	 */
	public FlxPoint scale;	
	/**
	 * Blending modes, just like Photoshop!
	 * E.g. "multiply", "screen", etc.
	 * @default null
	 */
	public String blend;
	/**
	 * Controls whether the object is smoothed when rotated, affects performance.
	 * @default false
	 */
	public boolean antialiasing;
	/**
	 * Whether the current animation has finished its first (or only) loop.
	 */
	public boolean finished;
	/**
	 * The width of the actual graphic or image being displayed (not necessarily the game object/bounding box).
	 * NOTE: Edit at your own risk!!  This is intended to be read-only.
	 */
	public int frameWidth;
	/**
	 * The height of the actual graphic or image being displayed (not necessarily the game object/bounding box).
	 * NOTE: Edit at your own risk!!  This is intended to be read-only.
	 */
	public int frameHeight;
	/**
	 * The total number of frames in this image (assumes each row is full).
	 */
	public int frames;
	
	//Animation helpers
	protected ArrayList<FlxAnim> _animations;
	protected int _flipped;
	protected FlxAnim _curAnim;
	protected int _curFrame;
	protected int _caf;
	protected float _frameTimer;
	protected FlxAnimationListener _callback;
	protected int _facing;
	protected float _bakedRotation;
	
	//Various rendering helpers
	protected Rectangle _flashRect;
	protected Rectangle _flashRect2;
	protected Point _flashPointZero;
	protected BitmapData _pixels;
	protected BitmapData _framePixels;
	protected float _alpha;
	protected int _color;
	protected ColorTransform _ct;
	protected Matrix _mtx;
	protected BitmapData _bbb;
	protected boolean _boundsVisible;
	public boolean scaleAndRotateFromCentre = true;
	static protected BitmapData _gfxSprite;
	static protected Bitmap _gfx;
	
	/**
	 * Creates a white 8x8 square <code>FlxSprite</code> at the specified position.
	 * Optionally can load a simple, one-frame graphic instead.
	 * 
	 * @param	X				The initial X position of the sprite.
	 * @param	Y				The initial Y position of the sprite.
	 * @param	SimpleGraphic	The graphic you want to display (OPTIONAL - for simple stuff only, do NOT use for animated images!).
	 */
	public FlxSprite(float X, float Y, Integer SimpleGraphic)
	{
		super();
		constructor(X, Y, SimpleGraphic);
	}
	
	public FlxSprite(float X, float Y)
	{		
		super();
		constructor(X, Y, null);
	}
	
	public FlxSprite()
	{
		super();
		constructor(0, 0, null);
	}
	
	private void constructor(float X, float Y, Integer SimpleGraphic)
	{		
		x = X;
		y = Y;

		_flashRect = new Rectangle();
		_flashRect2 = new Rectangle();
		_flashPointZero = new Point();
		offset = new FlxPoint();
		
		scale = new FlxPoint(1,1);
		_alpha = 1;
		_color = 0x00ffffff;
		blend = null;
		antialiasing = false;
		
		finished = false;
		_facing = RIGHT;
		_animations = new ArrayList<FlxAnim>();
		_flipped = 0;
		_curAnim = null;
		_curFrame = 0;
		_caf = 0;
		_frameTimer = 0;

		_mtx = new Matrix();
		_callback = null;
		/*if(_gfxSprite == null)
		{
			_gfxSprite = new BitmapData();
			_gfx = _gfxSprite.getBitmap();
		}*/
		
		if(SimpleGraphic == null)
			createGraphic(8, 8);
		else
			loadGraphic(SimpleGraphic);
	}

	/**
	 * Load an image from an embedded graphic file.
	 * 
	 * @param	Graphic		The image you want to use.
	 * @param	Animated	Whether the Graphic parameter is a single sprite or a row of sprites.
	 * @param	Reverse		Whether you need this class to generate horizontally flipped versions of the animation frames.
	 * @param	Width		OPTIONAL - Specify the width of your sprite (helps FlxSprite figure out what to do with non-square sprites or sprite sheets).
	 * @param	Height		OPTIONAL - Specify the height of your sprite (helps FlxSprite figure out what to do with non-square sprites or sprite sheets).
	 * @param	Unique		Whether the graphic should be a unique instance in the graphics cache.
	 * 
	 * @return	This FlxSprite instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSprite loadGraphic(int Graphic, boolean Animated, boolean Reverse, int Width, int Height, boolean Unique)
	{
		_bakedRotation = 0;
		_pixels = FlxG.addBitmap(Graphic, Reverse, Unique);
		if(Reverse)
			_flipped = _pixels.width >> 1;
		else
			_flipped = 0;
		
		if(Width == 0)
		{
			if(Animated)
				Width = _pixels.height;
			else if(_flipped > 0)
				Width = (int)(_pixels.width * .5f);
			else
				Width = _pixels.width;
		}
		width = frameWidth = Width;
		if(Height == 0)
		{
			if(Animated)
				Height = width;
			else
				Height = _pixels.height;
		}
		height = frameHeight = Height;
		
		resetHelpers();
		return this;
	}
	
	public FlxSprite loadGraphic(int Graphic, boolean Animated, boolean Reverse, int Width, int Height)
	{
		return loadGraphic(Graphic, Animated, Reverse, Width, Height, false);
	}
	
	public FlxSprite loadGraphic(int Graphic, boolean Animated, boolean Reverse, int Width)
	{
		return loadGraphic(Graphic, Animated, Reverse, Width, 0, false);
	}
	
	public FlxSprite loadGraphic(int Graphic, boolean Animated, boolean Reverse)
	{
		return loadGraphic(Graphic, Animated, Reverse, 0, 0, false);
	}
	
	public FlxSprite loadGraphic(int Graphic, boolean Animated)
	{
		return loadGraphic(Graphic, Animated, false, 0, 0, false);
	}
	
	/**
	 * Load an image from an embedded graphic file.
	 * 
	 * @param	Graphic		The image you want to use.
	 * 
	 * @return	This FlxSprite instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSprite loadGraphic(int Graphic)
	{
		return loadGraphic(Graphic, false, false, 0, 0, false);
	}
	
	
	/**
	 * Create a pre-rotated sprite sheet from a simple sprite.
	 * This can make a huge difference in graphical performance!
	 * 
	 * @param	Graphic			The image you want to rotate & stamp.
	 * @param	Frames			The number of frames you want to use (more == smoother rotations).
	 * @param	Offset			Use this to select a specific frame to draw from the graphic.
	 * @param	AntiAliasing	Whether to use high quality rotations when creating the graphic.
	 * @param	AutoBuffer		Whether to automatically increase the image size to accomodate rotated corners.
	 * 
	 * @return	This FlxSprite instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSprite loadRotatedGraphic(int Graphic, int Rotations, int Frame, boolean AntiAliasing, boolean AutoBuffer)
	{
		//Create the brush and canvas
		int rows = (int) Math.sqrt(Rotations);
		BitmapData brush = FlxG.addBitmap(Graphic, false, false);
		if(Frame >= 0)
		{
			//Using just a segment of the graphic - find the right bit here
			BitmapData full = brush;
			brush = new BitmapData(full.height,full.height);
			int rx = Frame*brush.width;
			int ry = 0;
			int fw = full.width;
			if(rx >= fw)
			{
				ry = (rx/fw)*brush.height;
				rx %= fw;
			}
			_flashRect.setX(rx);
			_flashRect.setY(ry);
			_flashRect.setWidth(brush.width);
			_flashRect.setHeight(brush.height);
			brush.copyPixels(full,_flashRect,_flashPointZero);
		}
		
		int max = brush.width;
		if(brush.height > max)
			max = brush.height;
		if(AutoBuffer)
			max *= 1.5;
		int cols = (int) FlxU.ceil(Rotations/rows);
		width = max*cols;
		height = max*rows;
		String key = Graphic + ":" + Frame + ":" + width + "x" + height;
		boolean skipGen = FlxG.checkBitmapCache(key);
		_pixels = FlxG.createBitmap(width, height, 0, true, key);
		width = frameWidth = _pixels.width;
		height = frameHeight = _pixels.height;
		_bakedRotation = 360/Rotations;
		
		//Generate a new sheet if necessary, then fix up the width & height
		if(!skipGen)
		{
			int r = 0;
			int c;
			float ba = 0;
			int bw2 = (int) (brush.width*0.5);
			int bh2 = (int) (brush.height*0.5);
			int gxc = (int) (max*0.5);
			int gyc = (int) (max*0.5);
			while(r < rows)
			{
				c = 0;
				while(c < cols)
				{
					_mtx.identity();
					_mtx.translate(-bw2,-bh2);
					_mtx.rotate(ba*0.017453293f);
					_mtx.translate(max*c+gxc, gyc);
					ba += _bakedRotation;
					_pixels.draw(brush,_mtx,null,null,null,AntiAliasing);
					c++;
				}
				gyc += max;
				r++;
			}
		}
		frameWidth = frameHeight = width = height = max;
		resetHelpers();
		return this;
	}
	
	public FlxSprite loadRotatedGraphic(int Graphic, int Rotations, int Frame, boolean AntiAliasing)
	{
		return loadRotatedGraphic(Graphic, Rotations, Frame, AntiAliasing, false);
	}
	
	public FlxSprite loadRotatedGraphic(int Graphic, int Rotations, int Frame)
	{
		return loadRotatedGraphic(Graphic, Rotations, Frame, false, false);
	}
	
	public FlxSprite loadRotatedGraphic(int Graphic, int Rotations)
	{
		return loadRotatedGraphic(Graphic, Rotations, -1, false, false);
	}
	
	public FlxSprite loadRotatedGraphic(int Graphic)
	{
		return loadRotatedGraphic(Graphic, 16, -1, false, false);
	}
	
	/**
	 * This function creates a flat colored square image dynamically.
	 * 
	 * @param	Width		The width of the sprite you want to generate.
	 * @param	Height		The height of the sprite you want to generate.
	 * @param	Color		Specifies the color of the generated block.
	 * @param	Unique		Whether the graphic should be a unique instance in the graphics cache.
	 * @param	Key			Optional parameter - specify a string key to identify this graphic in the cache.  Trumps Unique flag.
	 * 
	 * @return	This FlxSprite instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxSprite createGraphic(int Width, int Height, int Color, boolean Unique, String Key)
	{
		_bakedRotation = 0;
		_pixels = FlxG.createBitmap(Width, Height, Color, Unique, Key);
		width = frameWidth = _pixels.width;
		height = frameHeight = _pixels.height;
		
		resetHelpers();
		return this;
	}
	
	public FlxSprite createGraphic(int Width, int Height, int Color, boolean Unique)
	{
		return createGraphic(Width, Height, Color, Unique, null);		
	}
	
	public FlxSprite createGraphic(int Width, int Height, int Color)
	{
		return createGraphic(Width, Height, Color, false, null);		
	}
	
	public FlxSprite createGraphic(int Width, int Height)
	{
		return createGraphic(Width, Height, 0xffffffff, false, null);		
	}
	
	
	/**
	 * Set <code>pixels</code> to any <code>BitmapData</code> object.
	 * Automatically adjust graphic size and render helpers.
	 */
	public BitmapData getPixels()
	{
		return _pixels;
	}
	
	/**
	 * @private
	 */
	public void setPixels(BitmapData Pixels)
	{
		_pixels = Pixels;
		width = frameWidth = _pixels.width;
		height = frameHeight = _pixels.height;
		resetHelpers();
	}
	
	/**
	 * Resets some important variables for sprite optimization and rendering.
	 */
	protected void resetHelpers()
	{
		_boundsVisible = false;
		_flashRect.setX(0);
		_flashRect.setY(0);
		_flashRect.setWidth(frameWidth);
		_flashRect.setHeight(frameHeight);
		_flashRect2.setX(0);
		_flashRect2.setY(0);
		_flashRect2.setWidth(_pixels.width);
		_flashRect2.setHeight(_pixels.height);
		if((_framePixels == null) || (_framePixels.width != width) || (_framePixels.height != height))
			_framePixels = new BitmapData(width,height);
		if((_bbb == null) || (_bbb.width != width) || (_bbb.height != height))
			_bbb = new BitmapData(width,height);
		origin.x = (float) (frameWidth*0.5);
		origin.y = (float) (frameHeight*0.5);
		_framePixels.copyPixels(_pixels,_flashRect,_flashPointZero);
		frames = (int) ((_flashRect2.getWidth() / _flashRect.getWidth()) * (_flashRect2.getHeight() / _flashRect.getHeight()));
		if(_ct != null) 
			_framePixels.colorTransform(_flashRect,_ct);
		if(FlxG.showBounds)
			drawBounds();
		_caf = 0;
		refreshHulls();
	}
	
	
	/**
	 * @private
	 */
	@Override
	public void setSolid(boolean Solid) 
	{
		boolean os = _solid;
		_solid = Solid;
		if((os != _solid) && FlxG.showBounds)
			calcFrame();
	}
	
	/**
	 * @private
	 */
	@Override
	public void setFixed(boolean Fixed)
	{
		boolean of = _fixed;
		_fixed = Fixed;
		if((of != _fixed) && FlxG.showBounds)
			calcFrame();
	}
	
	/**
	 * Set <code>facing</code> using <code>FlxSprite.LEFT</code>,<code>RIGHT</code>,
	 * <code>UP</code>, and <code>DOWN</code> to take advantage of
	 * flipped sprites and/or just track player orientation more easily.
	 */
	public int getFacing()
	{
		return _facing;
	}
	
	/**
	 * @private
	 */
	public void setFacing(int Direction)
	{
		boolean c = _facing != Direction;
		_facing = Direction;
		if(c) calcFrame();
	}
	
	/**
	 * Set <code>alpha</code> to a number between 0 and 1 to change the opacity of the sprite.
	 */
	public float getAlpha()
	{
		return _alpha;
	}
	
	/**
	 * @private
	 */
	public void setAlpha(float Alpha)
	{
		if(Alpha > 1) 
			Alpha = 1;
		if(Alpha < 0) 
			Alpha = 0;
		if(Alpha == _alpha) 
			return;
		_alpha = Alpha;
		if((_alpha != 1) || (_color != 0x00ffffff)) 
			_ct = new ColorTransform((_color>>16)*0.00392f,(_color>>8&0xff)*0.00392f,(_color&0xff)*0.00392f,_alpha);
		else 
			_ct = null;
		calcFrame();
	}
	
	/**
	 * Set <code>color</code> to a number in this format: 0xRRGGBB.
	 * <code>color</code> IGNORES ALPHA.  To change the opacity use <code>alpha</code>.
	 * Tints the whole sprite to be this color (similar to OpenGL vertex colors).
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
		Color &= 0x00ffffff;
		if(_color == Color) 
			return;
		_color = Color;
		if((_alpha != 1) || (_color != 0x00ffffff)) 
			_ct = new ColorTransform((_color>>16)*0.00392f,(_color>>8&0xff)*0.00392f,(_color&0xff)*0.00392f,_alpha);
		else 
			_ct = null;
		calcFrame();
	}
	
	/**
	 * Change the RGB of the sprite.
	 * @param r
	 * @param g
	 * @param b
	 */
	public void changeRGB(int r, int g, int b)
	{			
		_ct = new ColorTransform(r, g, b, _alpha);
		calcFrame();
	}
	
	/**
	 * This function draws or stamps one <code>FlxSprite</code> onto another.
	 * This function is NOT intended to replace <code>render()</code>!
	 * 
	 * @param	Brush		The image you want to use as a brush or stamp or pen or whatever.
	 * @param	X			The X coordinate of the brush's top left corner on this sprite.
	 * @param	Y			They Y coordinate of the brush's top left corner on this sprite.
	 */
	public void draw(FlxSprite Brush, int X, int Y)
	{
		BitmapData b = Brush._framePixels;
		
		//Simple draw
		if(((Brush.angle == 0) || (Brush._bakedRotation > 0)) && (Brush.scale.x == 1) && (Brush.scale.y == 1) && (Brush.blend == null))
		{
			_flashPoint.x = X;
			_flashPoint.y = Y;
			_flashRect2.setWidth(b.width);
			_flashRect2.setHeight(b.height);
			_pixels.clearBitmap();
			_pixels.copyPixels(b,_flashRect2,_flashPoint,null,null,true);
			_flashRect2.setWidth(_pixels.width);
			_flashRect2.setHeight(_pixels.height);
			calcFrame();
			return;
		}

		//Advanced draw
		_mtx.identity();
		_mtx.translate(-Brush.origin.x,-Brush.origin.y);
		_mtx.scale(Brush.scale.x,Brush.scale.y);
		if(Brush.angle != 0)
			_mtx.rotate(Brush.angle * 0.017453293f);
		_mtx.translate(X+Brush.origin.x,Y+Brush.origin.y);
		_pixels.draw(b,_mtx,null,Brush.blend,null,Brush.antialiasing);
		calcFrame();
	}
	
	public void draw(FlxSprite Brush, int X)
	{
		draw(Brush, X, 0);
	}
	
	public void draw(FlxSprite Brush)
	{
		draw(Brush, 0, 0);
	}
	
	/**
	 * This function draws a line on this sprite from position X1,Y1
	 * to position X2,Y2 with the specified color.
	 * 
	 * @param	StartX		X coordinate of the line's start point.
	 * @param	StartY		Y coordinate of the line's start point.
	 * @param	EndX		X coordinate of the line's end point.
	 * @param	EndY		Y coordinate of the line's end point.
	 * @param	Color		The line's color.
	 * @param	Thickness	How thick the line is in pixels (default value is 1).
	 */
	public void drawLine(float StartX, float StartY, float EndX, float EndY, int Color, int Thickness)
	{
		//Draw line
		/*_gfx.clear();
		_gfx.moveTo(StartX,StartY);
		_gfx.lineStyle(Thickness,Color);
		_gfx.lineTo(EndX,EndY);*/
		
		//Cache line to bitmap
		/*_pixels.draw(_gfxSprite);
		calcFrame();*/ // TODO drawLine
	}
	
	
	/**
	 * Fills this sprite's graphic with a specific color.
	 * 
	 * @param	Color		The color with which to fill the graphic, format 0xAARRGGBB.
	 */
	public void fill(int Color)
	{
		_pixels.fillRect(_flashRect2,Color);
		if(_pixels != _framePixels)
			calcFrame();
	}
	
	/**
	 * Internal function for updating the sprite's animation.
	 * Useful for cases when you need to update this but are buried down in too many supers.
	 * This function is called automatically by <code>FlxSprite.update()</code>.
	 */
	protected void updateAnimation()
	{
		if(_bakedRotation > 0)
		{
			int oc = _caf;
			int ta = (int) (angle%360);
			if(ta < 0)
				ta += 360;
			_caf = (int) (ta/_bakedRotation);
			if(oc != _caf)
				calcFrame();
			return;
		}
		if((_curAnim != null) && (_curAnim.delay > 0) && (_curAnim.looped || !finished))
		{
			_frameTimer += FlxG.elapsed;
			while(_frameTimer > _curAnim.delay)
			{
				_frameTimer = _frameTimer - _curAnim.delay;
				if(_curFrame == _curAnim.frames.length-1)
				{
					if(_curAnim.looped) _curFrame = 0;
					finished = true;
				}
				else
					_curFrame++;
				_caf = _curAnim.frames[_curFrame];
				calcFrame();
			}
		}
	}
	
	/**
	 * Main game loop update function.  Override this to create your own sprite logic!
	 * Just don't forget to call super.update() or any of the helper functions.
	 */
	@Override
	public void update()
	{
		updateMotion();
		updateAnimation();
		updateFlickering();
	}
	
	
	/**
	 * Internal function that performs the actual sprite rendering, called by render().
	 */
	protected void renderSprite()
	{
		if(FlxG.showBounds != _boundsVisible)
			calcFrame();
		
		getScreenXY(_point);
		_flashPoint.x = _point.x;
		_flashPoint.y = _point.y;
		
		//Simple render
		if(((angle == 0) || (_bakedRotation > 0)) && (scale.x == 1) && (scale.y == 1) && (blend == null))
		{
			FlxG.buffer.copyPixels(_framePixels,_flashRect,_flashPoint,null,null,true);
			return;
		}
		
		Point matrixOrigin = new Point();
		if (scaleAndRotateFromCentre)
		{
			matrixOrigin.x = origin.x;
			matrixOrigin.y = matrixOrigin.y;
		}
		
	
		//Advanced render
		_mtx.identity();
		_mtx.translate(-matrixOrigin.x, -matrixOrigin.y);
		_mtx.scale(scale.x,scale.y);
		if(angle != 0) 
			_mtx.setRotate(angle * 0.017453293f, width*.5f, height*.5f); //_mtx.rotate(angle * 0.017453293f);	
		_mtx.translate(_point.x + matrixOrigin.x, _point.y + matrixOrigin.y);
		FlxG.buffer.draw(_framePixels,_mtx,null,blend,null,antialiasing);
		
		//Advanced render
//		_mtx.identity();
//		_mtx.translate(-origin.x,-origin.y);
//		_mtx.scale(scale.x,scale.y);
//		if(angle != 0)
//			_mtx.setRotate(angle * 0.017453293f, width*.5f, height*.5f); //_mtx.rotate(angle * 0.017453293f);		
//		_mtx.translate(_point.x+origin.x,_point.y+origin.y);
//		FlxG.buffer.draw(_framePixels, _mtx, null, blend, null, antialiasing);
	}
	
	
	/**
	 * Called by game loop, updates then blits or renders current frame of animation to the screen
	 */
	@Override
	public void render() 
	{
		renderSprite();
	}
	
	/**
	 * Checks to see if a point in 2D space overlaps this FlxCore object.
	 * 
	 * @param	X			The X coordinate of the point.
	 * @param	Y			The Y coordinate of the point.
	 * @param	PerPixel	Whether or not to use per pixel collision checking.
	 * 
	 * @return	Whether or not the point overlaps this object.
	 */
	@Override
	public boolean overlapsPoint(float X, float Y, boolean PerPixel) 
	{
		X = X + FlxU.floor(FlxG.scroll.x);
		Y = Y + FlxU.floor(FlxG.scroll.y);
		getScreenXY(_point);
		if(PerPixel)
			return _framePixels.hitTest(new Point(0,0),0xFF,new Point(X-_point.x,Y-_point.y));
		else if((X <= _point.x) || (X >= _point.x+frameWidth) || (Y <= _point.y) || (Y >= _point.y+frameHeight))
			return false;
		return true;
	}
	
	@Override
	public boolean overlapsPoint(float X, float Y) 
	{
		return overlapsPoint(X, Y, false);
	}
	
	
	/**
	 * Triggered whenever this sprite is launched by a <code>FlxEmitter</code>.
	 */
	void onEmit() { }
	
	/**
	 * Adds a new animation to the sprite.
	 * 
	 * @param	Name		What this animation should be called (e.g. "run").
	 * @param	Frames		An array of numbers indicating what frames to play in what order (e.g. 1, 2, 3).
	 * @param	FrameRate	The speed in frames per second that the animation should play at (e.g. 40 fps).
	 * @param	Looped		Whether or not the animation is looped or just plays once.
	 */
	public void addAnimation(String Name, int[] Frames, float FrameRate, boolean Looped)
	{
		_animations.add(new FlxAnim(Name,Frames,FrameRate,Looped));
	}
	
	public void addAnimation(String Name, int[] Frames, float FrameRate)
	{
		addAnimation(Name, Frames, FrameRate, true);
	}
	
	public void addAnimation(String Name, int[] Frames)
	{
		addAnimation(Name, Frames, 0, true);
	}
	
	/**
	 * Pass in a function to be called whenever this sprite's animation changes.
	 * 
	 * @param	AnimationCallback		A function that has 3 parameters: a string name, a uint frame number, and a uint frame index.
	 */
	public void addAnimationCallback(FlxAnimationListener AnimationCallback)
	{
		_callback = AnimationCallback;
	}
	
	/**
	 * Modified by Hectate
	 * Plays an existing animation (e.g. "run").
	 * If you call an animation that is already playing it will be ignored.
	 * If the requested starting frame is greater than the available frames, it will revert to frame 0.
	 * 
	 * @param	AnimName	The string name of the animation you want to play.
	 * @param	Force		Whether to force the animation to restart.
	 * @param	StartFrame	Which frame of the animation to start from if possible.
	 */
	public void play(String AnimName, boolean Force, int StartFrame)
	{
		if(!Force && (_curAnim != null) && (AnimName.equals(_curAnim.name)) && (_curAnim.looped || !finished)) 
			return;
		if(StartFrame <= _animations.size())
			_curFrame = StartFrame;
		else
			_curFrame = 0;
		_caf = 0;
		_frameTimer = 0;
		int i = 0;
		int al = _animations.size();
		while(i < al)
		{
			if(_animations.get(i).name.equals(AnimName))
			{
				_curAnim = _animations.get(i);
				if(_curAnim.delay <= 0)
					finished = true;
				else
					finished = false;				
				_caf = _curAnim.frames[_curFrame];
				calcFrame();
				return;
			}
			i++;
		}
	}
	
	public void play(String AnimName, boolean Force)
	{
		play(AnimName, Force, 0);
	}
	
	public void play(String AnimName)
	{
		play(AnimName, false, 0);
	}
	
	
	/**
	 * Tell the sprite to change to a random frame of animation
	 * Useful for instantiating particles or other weird things.
	 */
	public void randomFrame()
	{
		_curAnim = null;
		_caf = (int) (FlxU.random()*(_pixels.width/frameWidth));
		calcFrame();
	}
	
	/**
	 * Tell the sprite to change to a specific frame of animation.
	 * 
	 * @param	Frame	The frame you want to display.
	 */
	public int getFrame()
	{
		return _caf;
	}
	
	/**
	 * @private
	 */
	public void setFrame(int Frame)
	{
		_curAnim = null;
		_caf = Frame;
		calcFrame();
	}
	
	/**
	 * Call this function to figure out the on-screen position of the object.
	 * 
	 * @param	P	Takes a <code>Point</code> object and assigns the post-scrolled X and Y values of this object to it.
	 * 
	 * @return	The <code>Point</code> you passed in, or a new <code>Point</code> if you didn't pass one, containing the screen X and Y position of this object.
	 */
	@Override
	public FlxPoint getScreenXY(FlxPoint Point)
	{
		if(Point == null) 
			Point = new FlxPoint();
		Point.x = FlxU.floor((float) (x + FlxU.roundingError))+FlxU.floor(FlxG.scroll.x*scrollFactor.x) - offset.x;
		Point.y = FlxU.floor((float) (y + FlxU.roundingError))+FlxU.floor(FlxG.scroll.y*scrollFactor.y) - offset.y;
		return Point;
	}
	
	public FlxPoint getScreenXY()
	{
		return getScreenXY(null);
	}
	
	
	/**
	 * Internal function to update the current animation frame.
	 */
	protected void calcFrame()
	{
		_boundsVisible = false;
		float rx = _caf * frameWidth;
		float ry = 0;

		// Handle sprite sheets
		int w = 0;
		if(_flipped == 0)
			w = 0;
		else
			w = _pixels.width;

		if(rx >= w)
		{
			ry = (rx / w) * frameHeight;
			rx %= w;
		}
		
		
		// handle reversed sprites
		if(_flipped > 0 && (_facing == LEFT))
			rx = (_flipped << 1) - rx - frameWidth;				
		
		// Update display bitmap
		_flashRect.setX(rx);
		_flashRect.setY(ry);		
		
		if(_flipped > 0) // TODO: this line should be deleted after the Reverse bug is solved.
			_framePixels.clearBitmap();
		_framePixels.copyPixels(_pixels, _flashRect, _flashPointZero);
		
		_flashRect.setX(0);
		_flashRect.setY(0);
		if(_ct != null)
			_framePixels.colorTransform(_flashRect, _ct);
		if(FlxG.showBounds)
			drawBounds();
		if(_callback != null)
			_callback.animationChanged(_curAnim.name, _curFrame, _caf);
	}
	
	
	protected void drawBounds()
	{
		_boundsVisible = true;
		if((_bbb == null) || (_bbb.width != width) || (_bbb.height != height))
			_bbb = new BitmapData(width,height);
		int bbbc = getBoundingColor();		
		_bbb.clearBitmap(); // Clear the traces.
		_bbb.fillRect(_flashRect,0);
		int ofrw = (int) _flashRect.getWidth();
		int ofrh = (int) _flashRect.getHeight();
		_flashRect.setWidth(width);
		_flashRect.setHeight(height);
		_bbb.fillRect(_flashRect,bbbc);
		_flashRect.setWidth(_flashRect.getWidth() - 2);
		_flashRect.setHeight(_flashRect.getHeight() - 2);
		_flashRect.setX(1);
		_flashRect.setY(1);
		_bbb.fillRect(_flashRect,0);
		_flashRect.setWidth(ofrw);
		_flashRect.setHeight(ofrh);
		_flashRect.setX(0); 
		_flashRect.setY(0);
		_flashPoint.x = offset.x;
		_flashPoint.y = offset.y;
		
		_framePixels.copyPixels(_bbb,_flashRect,_flashPoint,null,null,true);
	}
	
	/**
	 * Internal function, currently only used to quickly update FlxState.screen for post-processing.
	 * Potentially super-unsafe, since it doesn't call <code>resetHelpers()</code>!
	 * 
	 * @param	Pixels		The <code>BitmapData</code> object you want to point at.
	 */
	void unsafeBind(BitmapData Pixels)
	{
		_pixels = _framePixels = Pixels;
	}
	
	
	/**
	 * By Rolpege
	 * Call this to split up a sprite into little sprites.
	 * 
	 * @param Sprite The sprite you want to split.
	 * @param Width The width of the splitted sprites.
	 * @param Height The height of the splitted sprites.
	 * 
	 * @return An Array full of the splitted sprites
	 */
	public ArrayList<FlxSprite> splitSprite(FlxSprite obj, int Width, int Height)
	{
		int numberOfHorizontal = obj.width / Width;
		int numberOfVertical = obj.height / Height;
		
		ArrayList<FlxSprite> r = new ArrayList<FlxSprite>();
		for(int x = 0; x < numberOfHorizontal; x++)
		{
			for(int y = 0; y < numberOfVertical; y++)
			{
				BitmapData bmp = new BitmapData(Width, Height, true, 0x00000000);
				bmp.copyPixels(obj.getPixels(), new Rectangle(x*Width, y*Height, Width, Height), new Point(0, 0), null, null, true);
				FlxSprite s = new FlxSprite(x * Width + (int) obj.x, y * Height + (int) obj.y);
				s.setPixels(bmp);
				r.set(x, s);
			}
		}
		return r;
	}	
}
