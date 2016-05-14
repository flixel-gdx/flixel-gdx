package org.flixel;

import org.flixel.event.IFlxObject;

import com.badlogic.gdx.utils.Array;

import flash.display.Graphics;

/**
 * This is the base class for most of the display objects (<code>FlxSprite</code>, <code>FlxText</code>, etc).
 * It includes some basic attributes about game objects, including retro-style flickering,
 * basic state information, sizes, scrolling, and basic physics and motion.
 * 
 * @author Ka Wing Chin
 * @author Thomas Weston
 */
public class FlxObject extends FlxBasic
{
	/**
	 * Generic value for "left" Used by <code>facing</code>, <code>allowCollisions</code>, and <code>touching</code>.
	 */
	static public final int LEFT = 0x000100;
	/**
	 * Generic value for "right" Used by <code>facing</code>, <code>allowCollisions</code>, and <code>touching</code>.
	 */
	static public final int RIGHT = 0x001000;
	/**
	 * Generic value for "up" Used by <code>facing</code>, <code>allowCollisions</code>, and <code>touching</code>.
	 */
	static public final int UP = 0x010000;
	/**
	 * Generic value for "down" Used by <code>facing</code>, <code>allowCollisions</code>, and <code>touching</code>.
	 */
	static public final int DOWN = 0x100000;

	/**
	 * Special-case constant meaning no collisions, used mainly by <code>allowCollisions</code> and <code>touching</code>.
	 */
	static public final int NONE = 0;
	/**
	 * Special-case constant meaning up, used mainly by <code>allowCollisions</code> and <code>touching</code>.
	 */
	static public final int CEILING = UP;
	/**
	 * Special-case constant meaning down, used mainly by <code>allowCollisions</code> and <code>touching</code>.
	 */
	static public final int FLOOR = DOWN;
	/**
	 * Special-case constant meaning only the left and right sides, used mainly by <code>allowCollisions</code> and <code>touching</code>.
	 */
	static public final int WALL = LEFT | RIGHT;
	/**
	 * Special-case constant meaning any direction, used mainly by <code>allowCollisions</code> and <code>touching</code>.
	 */
	static public final int ANY = LEFT | RIGHT | UP | DOWN;

	/**
	 * Handy constant used during collision resolution (see <code>separateX()</code> and <code>separateY()</code>).
	 */
	static public final float OVERLAP_BIAS = 4;

	/**
	 * Path behavior controls: move from the start of the path to the end then stop.
	 */
	static public final int PATH_FORWARD = 0x000000;
	/**
	 * Path behavior controls: move from the end of the path to the start then stop.
	 */
	static public final int PATH_BACKWARD = 0x000001;
	/**
	 * Path behavior controls: move from the start of the path to the end then directly back to the start, and start over.
	 */
	static public final int PATH_LOOP_FORWARD = 0x000010;
	/**
	 * Path behavior controls: move from the end of the path to the start then directly back to the end, and start over.
	 */
	static public final int PATH_LOOP_BACKWARD = 0x000100;
	/**
	 * Path behavior controls: move from the start of the path to the end then turn around and go back to the start, over and over.
	 */
	static public final int PATH_YOYO = 0x001000;
	/**
	 * Path behavior controls: ignores any vertical component to the path data, only follows side to side.
	 */
	static public final int PATH_HORIZONTAL_ONLY = 0x010000;
	/**
	 * Path behavior controls: ignores any horizontal component to the path data, only follows up and down.
	 */
	static public final int PATH_VERTICAL_ONLY = 0x100000;

	/**
	 * X position of the upper left corner of this object in world space.
	 */
	public float x;
	/**
	 * Y position of the upper left corner of this object in world space.
	 */
	public float y;
	/**
	 * The width of this object.
	 */
	public float width;
	/**
	 * The height of this object.
	 */
	public float height;

	/**
	 * Whether an object will move/alter position after a collision.
	 */
	public boolean immovable;

	/**
	 * The basic speed of this object.
	 */
	public FlxPoint velocity;
	/**
	 * The virtual mass of the object. Default value is 1.
	 * Currently only used with <code>elasticity</code> during collision resolution.
	 * Change at your own risk; effects seem crazy unpredictable so far!
	 */
	public float mass;
	/**
	 * The bounciness of this object.  Only affects collisions.  Default value is 0, or "not bouncy at all."
	 */
	public float elasticity;
	/**
	 * How fast the speed of this object is changing.
	 * Useful for smooth movement and gravity.
	 */
	public FlxPoint acceleration;
	/**
	 * This isn't drag exactly, more like deceleration that is only applied
	 * when acceleration is not affecting the sprite.
	 */
	public FlxPoint drag;
	/**
	 * If you are using <code>acceleration</code>, you can use <code>maxVelocity</code> with it 
	 * to cap the speed automatically (very useful!).
	 */
	public FlxPoint maxVelocity;
	/**
	 * Set the angle of a sprite to rotate it.
	 * WARNING: rotating sprites decreases rendering
	 * performance for this sprite by a factor of 10x!
	 */
	public float angle;
	/**
	 * This is how fast you want this sprite to spin.
	 */
	public float angularVelocity;
	/**
	 * How fast the spin speed should change.
	 */
	public float angularAcceleration;
	/**
	 * Like <code>drag</code> but for spinning.
	 */
	public float angularDrag;
	/**
	 * Use in conjunction with <code>angularAcceleration</code> for fluid spin speed control.
	 */
	public float maxAngular;
	/**
	 * Should always represent (0,0) - useful for different things, for avoiding unnecessary <code>new</code> calls.
	 */
	static protected final FlxPoint _pZero = new FlxPoint();

	/**
	 * A point that can store numbers from 0 to 1 (for X and Y independently)
	 * that governs how much this object is affected by the camera subsystem.
	 * 0 means it never moves, like a HUD element or far background graphic.
	 * 1 means it scrolls along a the same speed as the foreground layer.
	 * scrollFactor is initialized as (1,1) by default.
	 */
	public FlxPoint scrollFactor;
	/**
	 * Internal helper used for retro-style flickering.
	 */
	protected boolean _flicker;
	/**
	 * Internal helper used for retro-style flickering.
	 */
	protected float _flickerTimer;
	/**
	 * Handy for storing health percentage or armor points or whatever.
	 */
	public float health;
	/**
	 * This is just a pre-allocated x-y point container to be used however you like
	 */
	protected FlxPoint _point;
	/**
	 * This is just a pre-allocated rectangle container to be used however you like
	 */
	protected FlxRect _rect;
	/**
	 * Set this to false if you want to skip the automatic motion/movement stuff (see <code>updateMotion()</code>).
	 * FlxObject, FlxSprite and FlxText default to true.
	 * FlxTileblock and FlxTilemap default to false.
	 */
	public boolean moves;
	/**
	 * Bit field of flags (use with UP, DOWN, LEFT, RIGHT, etc) indicating surface contacts.
	 * Use bitwise operators to check the values stored here, or use touching(), justStartedTouching(), etc.
	 * You can even use them broadly as boolean values if you're feeling saucy!
	 */
	public int touching;
	/**
	 * Bit field of flags (use with UP, DOWN, LEFT, RIGHT, etc) indicating surface contacts from the previous game loop step.
	 * Use bitwise operators to check the values stored here, or use touching(), justStartedTouching(), etc.
	 * You can even use them broadly as boolean values if you're feeling saucy!
	 */
	public int wasTouching;
	/**
	 * Bit field of flags (use with UP, DOWN, LEFT, RIGHT, etc) indicating collision directions.
	 * Use bitwise operators to check the values stored here.
	 * Useful for things like one-way platforms (e.g. allowCollisions = UP;)
	 * The accessor "solid" just flips this variable between NONE and ANY.
	 */
	public int allowCollisions;

	/**
	 * Important variable for collision processing.
	 * By default this value is set automatically during <code>preUpdate()</code>.
	 */
	public FlxPoint last;

	/**
	 * A reference to a path object.  Null by default, assigned by <code>followPath()</code>.
	 */
	public FlxPath path;
	/**
	 * The speed at which the object is moving on the path.
	 * When an object completes a non-looping path circuit,
	 * the pathSpeed will be zeroed out, but the <code>path</code> reference
	 * will NOT be nulled out.  So <code>pathSpeed</code> is a good way
	 * to check if this object is currently following a path or not.
	 */
	public float pathSpeed;
	/**
	 * The angle in degrees between this object and the next node, where 0 is
	 * directly upward, and 90 is to the right.
	 */
	public float pathAngle;
	/**
	 * Internal helper, tracks which node of the path this object is moving toward.
	 */
	protected int _pathNodeIndex;
	/**
	 * Internal tracker for path behavior flags (like looping, horizontal only, etc).
	 */
	protected int _pathMode;
	/**
	 * Internal helper for node navigation, specifically yo-yo and backwards movement.
	 */
	protected int _pathInc;
	/**
	 * Internal flag for whether the object's angle should be adjusted to the path angle during path follow behavior.
	 */
	protected boolean _pathRotate;

	/**
	 * Instantiates a <code>FlxObject</code>.
	 * 
	 * @param	X		The X-coordinate of the point in space.
	 * @param	Y		The Y-coordinate of the point in space.
	 * @param	Width	Desired width of the rectangle.
	 * @param	Height	Desired height of the rectangle.
	 */
	public FlxObject(float X,float Y,int Width,int Height)
	{
		x = X;
		y = Y;
		last = new FlxPoint(x,y);
		width = Width;
		height = Height;
		mass = 1.0f;
		elasticity = 0.0f;

		health = 1;

		immovable = false;
		moves = true;

		touching = NONE;
		wasTouching = NONE;
		allowCollisions = ANY;

		velocity = new FlxPoint();
		acceleration = new FlxPoint();
		drag = new FlxPoint();
		maxVelocity = new FlxPoint(10000,10000);

		angle = 0;
		angularVelocity = 0;
		angularAcceleration = 0;
		angularDrag = 0;
		maxAngular = 10000;

		scrollFactor = new FlxPoint(1.0f,1.0f);
		_flicker = false;
		_flickerTimer = 0;

		_point = new FlxPoint();
		_rect = new FlxRect();

		path = null;
		pathSpeed = 0;
		pathAngle = 0;
	}

	/**
	 * Instantiates a <code>FlxObject</code>.
	 * 
	 * @param	X		The X-coordinate of the point in space.
	 * @param	Y		The Y-coordinate of the point in space.
	 * @param	Width	Desired width of the rectangle.
	 */
	public FlxObject(float X,float Y,int Width)
	{
		this(X,Y,Width,0);
	}

	/**
	 * Instantiates a <code>FlxObject</code>.
	 * 
	 * @param	X		The X-coordinate of the point in space.
	 * @param	Y		The Y-coordinate of the point in space.
	 */
	public FlxObject(float X,float Y)
	{
		this(X,Y,0,0);
	}

	/**
	 * Instantiates a <code>FlxObject</code>.
	 * 
	 * @param	X		The X-coordinate of the point in space.
	 */
	public FlxObject(float X)
	{
		this(X,0,0,0);
	}

	/**
	 * Instantiates a <code>FlxObject</code>.
	 */
	public FlxObject()
	{
		this(0,0,0,0);
	}

	/**
	 * Override this function to null out variables or
	 * manually call destroy() on class members if necessary.
	 * Don't forget to call super.destroy()!
	 */
	@Override
	public void destroy()
	{
		velocity = null;
		acceleration = null;
		drag = null;
		maxVelocity = null;
		scrollFactor = null;
		_point = null;
		_rect = null;
		last = null;
		cameras = null;
		if(path != null)
			path.destroy();
		path = null;
	}

	/**
	 * Pre-update is called right before <code>update()</code> on each object in the game loop.
	 * In <code>FlxObject</code> it controls the flicker timer,
	 * tracking the last coordinates for collision purposes,
	 * and checking if the object is moving along a path or not.
	 */
	@Override
	public void preUpdate()
	{
		_ACTIVECOUNT++;

		if(_flickerTimer != 0)
		{
			_flicker = !_flicker;
			if(_flickerTimer > 0)
			{
				_flickerTimer = _flickerTimer - FlxG.elapsed;
				if(_flickerTimer <= 0)
				{
					_flickerTimer = 0;
					_flicker = false;
				}
			}
		}

		last.x = x;
		last.y = y;

		if((path != null) && (pathSpeed != 0) && (path.nodes.get(_pathNodeIndex) != null))
			updatePathMotion();
	}

	/**
	 * Post-update is called right after <code>update()</code> on each object in the game loop.
	 * In <code>FlxObject</code> this function handles integrating the objects motion
	 * based on the velocity and acceleration settings, and tracking/clearing the <code>touching</code> flags.
	 */
	@Override
	public void postUpdate()
	{
		if(moves)
			updateMotion();

		wasTouching = touching;
		touching = NONE;
	}

	/**
	 * Internal function for updating the position and speed of this object.
	 * Useful for cases when you need to update this but are buried down in too many supers.
	 * Does a slightly fancier-than-normal integration to help with higher fidelity framerate-independenct motion.
	 */
	protected void updateMotion()
	{
		float delta;
		float velocityDelta;

		velocityDelta = (FlxU.computeVelocity(angularVelocity,angularAcceleration,angularDrag,maxAngular) - angularVelocity)/2f;
		angularVelocity += velocityDelta;
		angle += angularVelocity*FlxG.elapsed;
		angularVelocity += velocityDelta;

		velocityDelta = (FlxU.computeVelocity(velocity.x,acceleration.x,drag.x,maxVelocity.x) - velocity.x)/2f;
		velocity.x += velocityDelta;
		delta = velocity.x*FlxG.elapsed;
		velocity.x += velocityDelta;
		x += delta;

		velocityDelta = (FlxU.computeVelocity(velocity.y,acceleration.y,drag.y,maxVelocity.y) - velocity.y)/2f;
		velocity.y += velocityDelta;
		delta = velocity.y*FlxG.elapsed;
		velocity.y += velocityDelta;
		y += delta;
	}

	/**
	 * Rarely called, and in this case just increments the visible objects count and calls <code>drawDebug()</code> if necessary.
	 */
	@Override
	public void draw()
	{
		FlxCamera camera = FlxG._activeCamera;

		if(cameras == null)
			cameras = FlxG.cameras;

		if(!cameras.contains(camera, true))
			return;

		if(!onScreen(camera))
			return;
		_VISIBLECOUNT++;
		if(FlxG.visualDebug && !ignoreDrawDebug)
			drawDebug(camera);
	}

	/**
	 * Override this function to draw custom "debug mode" graphics to the
	 * specified camera while the debugger's visual mode is toggled on.
	 * 
	 * @param	Camera	Which camera to draw the debug visuals to.
	 */
	@Override
	public void drawDebug(FlxCamera Camera)
	{
		if(Camera == null)
			Camera = FlxG.camera;

		//get bounding box coordinates
		float boundingBoxX = x - (Camera.scroll.x*scrollFactor.x); //copied from getScreenXY()
		float boundingBoxY = y - (Camera.scroll.y*scrollFactor.y);
		boundingBoxX = (boundingBoxX + ((boundingBoxX > 0)?0.0000001f:-0.0000001f));
		boundingBoxY = (boundingBoxY + ((boundingBoxY > 0)?0.0000001f:-0.0000001f));
		int boundingBoxWidth = (int)((width != (int)width)?width:width-1);
		int boundingBoxHeight = (int)((height != (int)height)?height:height-1);

		//fill static graphics object with square shape
		Graphics gfx = FlxG.flashGfx;
		int boundingBoxColor;
		if(allowCollisions > 0)
		{
			if(allowCollisions != ANY)
				boundingBoxColor = FlxG.PINK;
			if(immovable)
				boundingBoxColor = FlxG.GREEN;
			else
				boundingBoxColor = FlxG.RED;
		}
		else
			boundingBoxColor = FlxG.BLUE;
		gfx.lineStyle(1f,boundingBoxColor,0.5f);
		gfx.drawRect(boundingBoxX, boundingBoxY, boundingBoxWidth, boundingBoxHeight);
	}

	/**
	 * Call this function to give this object a path to follow.
	 * If the path does not have at least one node in it, this function
	 * will log a warning message and return.
	 * 
	 * @param	Path		The <code>FlxPath</code> you want this object to follow.
	 * @param	Speed		How fast to travel along the path in pixels per second.
	 * @param	Mode		Optional, controls the behavior of the object following the path using the path behavior constants.  Can use multiple flags at once, for example PATH_YOYO|PATH_HORIZONTAL_ONLY will make a object move back and forth along the X axis of the path only.
	 * @param	AutoRotate	Automatically point the object toward the next node.  Assumes the graphic is pointing upward.  Default behavior is false, or no automatic rotation.
	 */
	public void followPath(FlxPath Path,float Speed,int Mode,boolean AutoRotate)
	{
		if(Path == null || Path.nodes.size <= 0)
		{
			FlxG.log("WARNING: Paths need at least one node in them to be followed.");
			return;
		}

		path = Path;
		pathSpeed = FlxU.abs(Speed);
		_pathMode = Mode;
		_pathRotate = AutoRotate;

		//get starting node
		if((_pathMode == PATH_BACKWARD) || (_pathMode == PATH_LOOP_BACKWARD))
		{
			_pathNodeIndex = path.nodes.size - 1;
			_pathInc = -1;
		}
		else
		{
			_pathNodeIndex = 0;
			_pathInc = 1;
		}
	}

	/**
	 * Call this function to give this object a path to follow.
	 * If the path does not have at least one node in it, this function
	 * will log a warning message and return.
	 * 
	 * @param	Path		The <code>FlxPath</code> you want this object to follow.
	 * @param	Speed		How fast to travel along the path in pixels per second.
	 * @param	Mode		Optional, controls the behavior of the object following the path using the path behavior constants.  Can use multiple flags at once, for example PATH_YOYO|PATH_HORIZONTAL_ONLY will make a object move back and forth along the X axis of the path only.
	 */
	public void followPath(FlxPath Path,float Speed,int Mode)
	{
		followPath(Path,Speed,Mode,false);
	}

	/**
	 * Call this function to give this object a path to follow.
	 * If the path does not have at least one node in it, this function
	 * will log a warning message and return.
	 * 
	 * @param	Path		The <code>FlxPath</code> you want this object to follow.
	 * @param	Speed		How fast to travel along the path in pixels per second.
	 */
	public void followPath(FlxPath Path,float Speed)
	{
		followPath(Path,Speed,PATH_FORWARD,false);
	}

	/**
	 * Call this function to give this object a path to follow.
	 * If the path does not have at least one node in it, this function
	 * will log a warning message and return.
	 * 
	 * @param	Path		The <code>FlxPath</code> you want this object to follow.
	 */
	public void followPath(FlxPath Path)
	{
		followPath(Path,100,PATH_FORWARD,false);
	}

	/**
	 * Tells this object to stop following the path its on.
	 * 
	 * @param	DestroyPath		Tells this function whether to call destroy on the path object.  Default value is false.
	 */
	public void stopFollowingPath(boolean DestroyPath)
	{
		pathSpeed = 0;
		velocity.x = 0;
		velocity.y = 0;
		if(DestroyPath && (path != null))
		{
			path.destroy();
			path = null;
		}
	}

	/**
	 * Tells this object to stop following the path its on.
	 */
	public void stopFollowingPath()
	{
		stopFollowingPath(false);
	}

	/**
	 * Internal function that decides what node in the path to aim for next based on the behavior flags.
	 * 
	 * @return	The node (a <code>FlxPoint</code> object) we are aiming for next.
	 */
	protected FlxPoint advancePath(boolean Snap)
	{
		if(Snap)
		{
			FlxPoint oldNode = path.nodes.get(_pathNodeIndex);
			if(oldNode != null)
			{
				if((_pathMode & PATH_VERTICAL_ONLY) == 0)
					x = oldNode.x - width*0.5f;
				if((_pathMode & PATH_HORIZONTAL_ONLY) == 0)
					y = oldNode.y - height*0.5f;
			}
		}

		_pathNodeIndex += _pathInc;

		if((_pathMode & PATH_BACKWARD) > 0)
		{
			if(_pathNodeIndex < 0)
			{
				_pathNodeIndex = 0;
				stopFollowingPath(false);

			}
		}
		else if((_pathMode & PATH_LOOP_FORWARD) > 0)
		{
			if(_pathNodeIndex >= path.nodes.size)
				_pathNodeIndex = 0;
		}
		else if((_pathMode & PATH_LOOP_BACKWARD) > 0)
		{
			if(_pathNodeIndex < 0)
			{
				_pathNodeIndex = path.nodes.size - 1;
				if(_pathNodeIndex < 0)
					_pathNodeIndex = 0;
			}
		}
		else if((_pathMode & PATH_YOYO) > 0)
		{
			if(_pathInc > 0)
			{
				if(_pathNodeIndex >= path.nodes.size)
				{
					_pathNodeIndex = path.nodes.size - 2;
					if(_pathNodeIndex < 0)
						_pathNodeIndex = 0;
					_pathInc = -_pathInc;
				}
			}
			else if(_pathNodeIndex < 0)
			{
				_pathNodeIndex = 1;
				if(_pathNodeIndex >= path.nodes.size)
					_pathNodeIndex = path.nodes.size - 1;
				if(_pathNodeIndex < 0)
					_pathNodeIndex = 0;
				_pathInc = -_pathInc;
			}
		}
		else
		{
			if(_pathNodeIndex >= path.nodes.size)
			{
				_pathNodeIndex = path.nodes.size - 1;
				stopFollowingPath(false);

			}
		}

		return path.nodes.get(_pathNodeIndex);
	}

	/**
	 * Internal function that decides what node in the path to aim for next based on the behavior flags.
	 * 
	 * @return	The node (a <code>FlxPoint</code> object) we are aiming for next.
	 */
	protected FlxPoint advancePath()
	{
		return advancePath(true);
	}

	/**
	 * Internal function for moving the object along the path.
	 * Generally this function is called automatically by <code>preUpdate()</code>.
	 * The first half of the function decides if the object can advance to the next node in the path,
	 * while the second half handles actually picking a velocity toward the next node.
	 */
	protected void updatePathMotion()
	{
		//first check if we need to be pointing at the next node yet
		_point.x = x + width*0.5f;
		_point.y = y + height*0.5f;
		FlxPoint node = path.nodes.get(_pathNodeIndex);
		float deltaX = node.x - _point.x;
		float deltaY = node.y - _point.y;

		boolean horizontalOnly = (_pathMode & PATH_HORIZONTAL_ONLY) > 0;
		boolean verticalOnly = (_pathMode & PATH_VERTICAL_ONLY) > 0;

		if(horizontalOnly)
		{
			if(((deltaX>0)?deltaX:-deltaX) < pathSpeed*FlxG.elapsed)
				node = advancePath();
		}
		else if(verticalOnly)
		{
			if(((deltaY>0)?deltaY:-deltaY) < pathSpeed*FlxG.elapsed)
				node = advancePath();
		}
		else
		{
			if(Math.sqrt(deltaX*deltaX + deltaY*deltaY) < pathSpeed*FlxG.elapsed)
				node = advancePath();
		}

		//then just move toward the current node at the requested speed
		if(pathSpeed != 0)
		{
			//set velocity based on path mode
			_point.x = x + width*0.5f;
			_point.y = y + height*0.5f;
			if(horizontalOnly || (_point.y == node.y))
			{
				velocity.x = (_point.x < node.x)?pathSpeed:-pathSpeed;
				if(velocity.x < 0)
					pathAngle = -90;
				else
					pathAngle = 90;
				if(!horizontalOnly)
					velocity.y = 0;
			}
			else if(verticalOnly || (_point.x == node.x))
			{
				velocity.y = (_point.y < node.y)?pathSpeed:-pathSpeed;
				if(velocity.y < 0)
					pathAngle = 0;
				else
					pathAngle = 180;
				if(!verticalOnly)
					velocity.x = 0;
			}
			else
			{
				pathAngle = FlxU.getAngle(_point,node);
				FlxU.rotatePoint(0,pathSpeed,0,0,pathAngle,velocity);
			}

			//then set object rotation if necessary
			if(_pathRotate)
			{
				angularVelocity = 0;
				angularAcceleration = 0;
				angle = pathAngle;
			}
		}
	}

	/**
	 * Checks to see if some <code>FlxObject</code> or <code>FlxGroup</code> overlaps this <code>FlxObject</code>.
	 * If the group has a LOT of things in it, it might be faster to use <code>FlxG.overlaps()</code>.
	 * WARNING: Currently tilemaps do NOT support screen space overlap checks!
	 * 
	 * @param	ObjectOrGroup	The object or group being tested.
	 * @param	InScreenSpace	Whether to take scroll factors into account when checking for overlap.  Default is false, or "only compare in world space."
	 * @param	Camera			Specify which game camera you want.  If null getScreenXY() will just grab the first global camera.
	 * 
	 * @return	Whether or not the two objects overlap.
	 */
	public boolean overlaps(FlxBasic ObjectOrGroup,boolean InScreenSpace,FlxCamera Camera)
	{
		if(ObjectOrGroup instanceof FlxGroup)
		{
			boolean results = false;
			int i = 0;
			Array<FlxBasic> members = ((FlxGroup)ObjectOrGroup).members;
			int length = ((FlxGroup)ObjectOrGroup).length;
			while(i < length)
			{
				if(overlaps(members.get(i++),InScreenSpace,Camera))
					results = true;
			}
			return results;
		}

		if(ObjectOrGroup instanceof FlxTilemap)
		{
			//Since tilemap's have to be the caller, not the target, to do proper tile-based collisions,
			// we redirect the call to the tilemap overlap here.
			return ((FlxTilemap) ObjectOrGroup).overlaps(this,InScreenSpace,Camera);
		}

		FlxObject object = (FlxObject) ObjectOrGroup;
		if(!InScreenSpace)
		{
			return (object.x + object.width > x) && (object.x < x + width) &&
							(object.y + object.height > y) && (object.y < y + height);
		}

		if(Camera == null)
			Camera = FlxG.camera;
		FlxPoint objectScreenPos = object.getScreenXY(null,Camera);
		getScreenXY(_point,Camera);
		return (objectScreenPos.x + object.width > _point.x) && (objectScreenPos.x < _point.x + width) && 
						(objectScreenPos.y + object.height > _point.y) && (objectScreenPos.y < _point.y + height);
	}

	/**
	 * Checks to see if some <code>FlxObject</code> or <code>FlxGroup</code> overlaps this <code>FlxObject</code>.
	 * If the group has a LOT of things in it, it might be faster to use <code>FlxG.overlaps()</code>.
	 * WARNING: Currently tilemaps do NOT support screen space overlap checks!
	 * 
	 * @param	ObjectOrGroup	The object or group being tested.
	 * @param	InScreenSpace	Whether to take scroll factors into account when checking for overlap.  Default is false, or "only compare in world space."
	 * 
	 * @return	Whether or not the two objects overlap.
	 */
	public boolean overlaps(FlxBasic ObjectOrGroup,boolean InScreenSpace)
	{
		return overlaps(ObjectOrGroup,InScreenSpace,null);
	}

	/**
	 * Checks to see if some <code>FlxObject</code> or <code>FlxGroup</code> overlaps this <code>FlxObject</code>.
	 * If the group has a LOT of things in it, it might be faster to use <code>FlxG.overlaps()</code>.
	 * WARNING: Currently tilemaps do NOT support screen space overlap checks!
	 * 
	 * @param	ObjectOrGroup	The object or group being tested.
	 * 
	 * @return	Whether or not the two objects overlap.
	 */
	public boolean overlaps(FlxBasic ObjectOrGroup)
	{
		return overlaps(ObjectOrGroup,false,null);
	}

	/**
	 * Checks to see if this <code>FlxObject</code> were located at the given position, would it overlap the <code>FlxObject</code> or <code>FlxGroup</code>?
	 * This is distinct from overlapsPoint(), which just checks that point, rather than taking the object's size into account.
	 * WARNING: Currently tilemaps do NOT support screen space overlap checks!
	 * 
	 * @param	X				The X position you want to check.  Pretends this object (the caller, not the parameter) is located here.
	 * @param	Y				The Y position you want to check.  Pretends this object (the caller, not the parameter) is located here.
	 * @param	ObjectOrGroup	The object or group being tested.
	 * @param	InScreenSpace	Whether to take scroll factors into account when checking for overlap.  Default is false, or "only compare in world space."
	 * @param	Camera			Specify which game camera you want.  If null getScreenXY() will just grab the first global camera.
	 * 
	 * @return	Whether or not the two objects overlap.
	 */
	public boolean overlapsAt(float X,float Y,FlxBasic ObjectOrGroup,boolean InScreenSpace,FlxCamera Camera)
	{
		if(ObjectOrGroup instanceof FlxGroup)
		{
			boolean results = false;
			int i = 0;
			Array<FlxBasic> members = ((FlxGroup)ObjectOrGroup).members;
			int length = ((FlxGroup)ObjectOrGroup).length;
			while(i < length)
			{
				if(overlapsAt(X,Y,members.get(i++),InScreenSpace,Camera))
					results = true;
			}
			return results;
		}

		if(ObjectOrGroup instanceof FlxTilemap)
		{
			//Since tilemap's have to be the caller, not the target, to do proper tile-based collisions,
			// we redirect the call to the tilemap overlap here.
			//However, since this is overlapsAt(), we also have to invent the appropriate position for the tilemap.
			//So we calculate the offset between the player and the requested position, and subtract that from the tilemap.
			FlxTilemap tilemap = (FlxTilemap) ObjectOrGroup;
			return tilemap.overlapsAt(tilemap.x - (X - x), tilemap.y - (Y - y),this,InScreenSpace,Camera);
		}

		FlxObject object = (FlxObject) ObjectOrGroup;
		if(!InScreenSpace)
		{
			return (object.x + object.width > X) && (object.x < X + width) &&
							(object.y + object.height > Y) && (object.y < Y + height);
		}

		if(Camera == null)
			Camera = FlxG.camera;
		FlxPoint objectScreenPos = object.getScreenXY(null,Camera);
		_point.x = X - (Camera.scroll.x*scrollFactor.x); //copied from getScreenXY()
		_point.y = Y - (Camera.scroll.y*scrollFactor.y);
		_point.x += (_point.x > 0)?0.0000001f:-0.0000001f;
		_point.y += (_point.y > 0)?0.0000001f:-0.0000001f;
		return (objectScreenPos.x + object.width > _point.x) && (objectScreenPos.x < _point.x + width) &&
				(objectScreenPos.y + object.height > _point.y) && (objectScreenPos.y < _point.y + height);
	}

	/**
	 * Checks to see if this <code>FlxObject</code> were located at the given position, would it overlap the <code>FlxObject</code> or <code>FlxGroup</code>?
	 * This is distinct from overlapsPoint(), which just checks that point, rather than taking the object's size into account.
	 * WARNING: Currently tilemaps do NOT support screen space overlap checks!
	 * 
	 * @param	X				The X position you want to check.  Pretends this object (the caller, not the parameter) is located here.
	 * @param	Y				The Y position you want to check.  Pretends this object (the caller, not the parameter) is located here.
	 * @param	ObjectOrGroup	The object or group being tested.
	 * @param	InScreenSpace	Whether to take scroll factors into account when checking for overlap.  Default is false, or "only compare in world space."
	 * 
	 * @return	Whether or not the two objects overlap.
	 */
	public boolean overlapsAt(float X,float Y,FlxBasic ObjectOrGroup,boolean InScreenSpace)
	{
		return overlapsAt(X,Y,ObjectOrGroup,InScreenSpace,null);
	}

	/**
	 * Checks to see if this <code>FlxObject</code> were located at the given position, would it overlap the <code>FlxObject</code> or <code>FlxGroup</code>?
	 * This is distinct from overlapsPoint(), which just checks that point, rather than taking the object's size into account.
	 * WARNING: Currently tilemaps do NOT support screen space overlap checks!
	 * 
	 * @param	X				The X position you want to check.  Pretends this object (the caller, not the parameter) is located here.
	 * @param	Y				The Y position you want to check.  Pretends this object (the caller, not the parameter) is located here.
	 * @param	ObjectOrGroup	The object or group being tested.
	 * 
	 * @return	Whether or not the two objects overlap.
	 */
	public boolean overlapsAt(float X,float Y,FlxBasic ObjectOrGroup)
	{
		return overlapsAt(X,Y,ObjectOrGroup,false,null);
	}

	/**
	 * Checks to see if a point in 2D world space overlaps this <code>FlxObject</code> object.
	 * 
	 * @param	Point			The point in world space you want to check.
	 * @param	InScreenSpace	Whether to take scroll factors into account when checking for overlap.
	 * @param	Camera			Specify which game camera you want.  If null getScreenXY() will just grab the first global camera.
	 * 
	 * @return	Whether or not the point overlaps this object.
	 */
	public boolean overlapsPoint(FlxPoint Point,boolean InScreenSpace,FlxCamera Camera)
	{
		if(!InScreenSpace)
			return (Point.x > x) && (Point.x < (x + width)) && (Point.y > y) && (Point.y < (y + height));

		if(Camera == null)
			Camera = FlxG.camera;
		float X = Point.x - Camera.scroll.x;
		float Y = Point.y - Camera.scroll.y;
		getScreenXY(_point,Camera);
		return (X > _point.x) && (X < _point.x + width) && (Y > _point.y) && (Y < _point.y + height);
	}

	/**
	 * Checks to see if a point in 2D world space overlaps this <code>FlxObject</code> object.
	 * 
	 * @param	Point			The point in world space you want to check.
	 * @param	InScreenSpace	Whether to take scroll factors into account when checking for overlap.
	 * 
	 * @return	Whether or not the point overlaps this object.
	 */
	public boolean overlapsPoint(FlxPoint Point,boolean InScreenSpace)
	{
		return overlapsPoint(Point,InScreenSpace,null);
	}

	/**
	 * Checks to see if a point in 2D world space overlaps this <code>FlxObject</code> object.
	 * 
	 * @param	Point			The point in world space you want to check.
	 * 
	 * @return	Whether or not the point overlaps this object.
	 */
	public boolean overlapsPoint(FlxPoint Point)
	{
		return overlapsPoint(Point,false,null);
	}

	/**
	 * Check and see if this object is currently on screen.
	 * 
	 * @param	Camera		Specify which game camera you want.  If null getScreenXY() will just grab the first global camera.
	 * 
	 * @return	Whether the object is on screen or not.
	 */
	public boolean onScreen(FlxCamera Camera)
	{
		if(Camera == null)
			Camera = FlxG.camera;
		getScreenXY(_point,Camera);
		return (_point.x + width > 0) && (_point.x < Camera.width) && (_point.y + height > 0) && (_point.y < Camera.height);
	}

	/**
	 * Check and see if this object is currently on screen.
	 */
	public boolean onScreen()
	{
		return onScreen(null);
	}

	/**
	 * Call this function to figure out the on-screen position of the object.
	 * 
	 * @param	Point		Takes a <code>FlxPoint</code> object and assigns the post-scrolled X and Y values of this object to it.
	 * @param	Camera		Specify which game camera you want.  If null getScreenXY() will just grab the first global camera.
	 * 
	 * @return	The <code>Point</code> you passed in, or a new <code>Point</code> if you didn't pass one, containing the screen X and Y position of this object.
	 */
	public FlxPoint getScreenXY(FlxPoint Point,FlxCamera Camera)
	{
		if(Point == null)
			Point = new FlxPoint();
		if(Camera == null)
			Camera = FlxG.camera;
		Point.x = x - (Camera.scroll.x*scrollFactor.x);
		Point.y = y - (Camera.scroll.y*scrollFactor.y);
		Point.x += (Point.x > 0)?0.0000001f:-0.0000001f;
		Point.y += (Point.y > 0)?0.0000001f:-0.0000001f;
		return Point;
	}

	/**
	 * Call this function to figure out the on-screen position of the object.
	 * 
	 * @param	Point		Takes a <code>FlxPoint</code> object and assigns the post-scrolled X and Y values of this object to it.
	 * 
	 * @return	The <code>Point</code> you passed in, or a new <code>Point</code> if you didn't pass one, containing the screen X and Y position of this object.
	 */
	public FlxPoint getScreenXY(FlxPoint Point)
	{
		return getScreenXY(Point,null);
	}

	/**
	 * Call this function to figure out the on-screen position of the object.
	 *  
	 * @return	The <code>Point</code> you passed in, or a new <code>Point</code> if you didn't pass one, containing the screen X and Y position of this object.
	 */
	public FlxPoint getScreenXY()
	{
		return getScreenXY(null,null);
	}

	/**
	 * Tells this object to flicker, retro-style.
	 * Pass a negative value to flicker forever.
	 * 
	 * @param	Duration	How many seconds to flicker for.
	 */
	public void flicker(float Duration)
	{
		_flickerTimer = Duration;
		if(_flickerTimer == 0)
			_flicker = false;
	}

	/**
	 * Tells this object to flicker, retro-style.
	 * Pass a negative value to flicker forever.
	 */
	public void flicker()
	{
		flicker(1);
	}

	/**
	 * Check to see if the object is still flickering.
	 * 
	 * @return	Whether the object is flickering or not.
	 */
	public boolean getFlickering()
	{
		return _flickerTimer != 0;
	}

	/**
	 * Whether the object collides or not. For more control over what directions
	 * the object will collide from, use collision constants (like LEFT, FLOOR, etc)
	 * to set the value of allowCollisions directly.
	 */
	public boolean getSolid()
	{
		return (allowCollisions & ANY) > NONE;
	}

	/**
	 * Whether the object collides or not. For more control over what directions
	 * the object will collide from, use collision constants (like LEFT, FLOOR, etc)
	 * to set the value of allowCollisions directly.
	 */
	public void setSolid(boolean Solid)
	{
		if(Solid)
			allowCollisions = ANY;
		else
			allowCollisions = NONE;
	}

	/**
	 * Retrieve the midpoint of this object in world coordinates.
	 * 
	 * @param	Point	Allows you to pass in an existing <code>FlxPoint</code> object if you're so inclined. Otherwise a new one is created.
	 * 
	 * @return	A <code>FlxPoint</code> object containing the midpoint of this object in world coordinates.
	 */
	public FlxPoint getMidpoint(FlxPoint Point)
	{
		if(Point == null)
			Point = new FlxPoint();
		Point.x = x + width*0.5f;
		Point.y = y + height*0.5f;
		return Point;
	}

	/**
	 * Retrieve the midpoint of this object in world coordinates.
	 * 
	 * @return	A <code>FlxPoint</code> object containing the midpoint of this object in world coordinates.
	 */
	public FlxPoint getMidpoint()
	{
		return getMidpoint(null);
	}

	/**
	 * Handy function for reviving game objects.
	 * Resets their existence flags and position.
	 * 
	 * @param	X	The new X position of this object.
	 * @param	Y	The new Y position of this object.
	 */
	public void reset(float X, float Y)
	{
		revive();
		touching = NONE;
		wasTouching = NONE;
		x = X;
		y = Y;
		last.x = x;
		last.y = y;
		velocity.x = 0;
		velocity.y = 0;
	}

	/**
	 * Handy function for checking if this object is touching a particular surface.
	 * For slightly better performance you can just &amp; the value directly into <code>touching</code>.
	 * However, this method is good for readability and accessibility.
	 * 
	 * @param	Direction	Any of the collision flags (e.g. LEFT, FLOOR, etc).
	 * 
	 * @return	Whether the object is touching an object in (any of) the specified direction(s) this frame.
	 */
	public boolean isTouching(int Direction)
	{
		return (touching & Direction) > NONE;
	}

	/**
	 * Handy function for checking if this object is just landed on a particular surface.
	 * 
	 * @param	Direction	Any of the collision flags (e.g. LEFT, FLOOR, etc).
	 * 
	 * @return	Whether the object just landed on (any of) the specified surface(s) this frame.
	 */
	public boolean justTouched(int Direction)
	{
		return ((touching & Direction) > NONE) && ((wasTouching & Direction) <= NONE);
	}

	/**
	 * Reduces the "health" variable of this sprite by the amount specified in Damage.
	 * Calls kill() if health drops to or below zero.
	 * 
	 * @param	Damage	How much health to take away (use a negative number to give a health bonus).
	 */
	public void hurt(float Damage)
	{
		health = health - Damage;
		if(health <= 0)
			kill();
	}

	/**
	 * The main collision resolution function in flixel.
	 * 
	 * @param	Object1		Any <code>FlxObject</code>.
	 * @param	Object2		Any other <code>FlxObject</code>.
	 * 
	 * @return	Whether the objects in fact touched and were separated.
	 */
	static public boolean separate(FlxObject Object1, FlxObject Object2)
	{
		boolean separatedX = separateX(Object1,Object2);
		boolean separatedY = separateY(Object1,Object2);
		return separatedX || separatedY;
	}

	/**
	 * The X-axis component of the object separation process.
	 * 
	 * @param	Object1		Any <code>FlxObject</code>.
	 * @param	Object2		Any other <code>FlxObject</code>.
	 * 
	 * @return	Whether the objects in fact touched and were separated along the X axis.
	 */
	static public boolean separateX(FlxObject Object1, FlxObject Object2)
	{
		//can't separate two immovable objects
		boolean obj1immovable = Object1.immovable;
		boolean obj2immovable = Object2.immovable;
		if(obj1immovable && obj2immovable)
			return false;

		//If one of the objects is a tilemap, just pass it off.
		if(Object1 instanceof FlxTilemap)
			return ((FlxTilemap) (Object1)).overlapsWithCallback(Object2,separateX);
		if(Object2 instanceof FlxTilemap)
			return ((FlxTilemap) (Object2)).overlapsWithCallback(Object1,separateX,true);

		//First, get the two object deltas
		float overlap = 0;
		float obj1delta = Object1.x - Object1.last.x;
		float obj2delta = Object2.x - Object2.last.x;
		if(obj1delta != obj2delta)
		{
			//Check if the X hulls actually overlap
			float obj1deltaAbs = (obj1delta > 0)?obj1delta:-obj1delta;
			float obj2deltaAbs = (obj2delta > 0)?obj2delta:-obj2delta;
			float obj1x = Object1.x-((obj1delta > 0)?obj1delta:0);
			float obj1y = Object1.last.y;
			float obj1width = Object1.width+((obj1delta > 0)?obj1delta:-obj1delta);
			float obj1height = Object1.height;
			float obj2x = Object2.x -((obj2delta > 0)?obj2delta:0);
			float obj2y = Object2.last.y;
			float obj2width = Object2.width+((obj2delta > 0)?obj2delta:-obj2delta);
			float obj2height = Object2.height;
			if((obj1x + obj1width > obj2x) && (obj1x < obj2x + obj2width) && (obj1y + obj1height > obj2y) && (obj1y < obj2y + obj2height))
			{
				float maxOverlap = obj1deltaAbs + obj2deltaAbs + OVERLAP_BIAS;

				//If they did overlap (and can), figure out by how much and flip the corresponding flags
				if(obj1delta > obj2delta)
				{
					overlap = Object1.x + Object1.width - Object2.x;
					if((overlap > maxOverlap) || (Object1.allowCollisions & RIGHT) == 0 || (Object2.allowCollisions & LEFT) == 0)
						overlap = 0;
					else
					{
						Object1.touching |= RIGHT;
						Object2.touching |= LEFT;
					}
				}
				else if(obj1delta < obj2delta)
				{
					overlap = Object1.x - Object2.width - Object2.x;
					if((-overlap > maxOverlap) || (Object1.allowCollisions & LEFT) == 0 || (Object2.allowCollisions & RIGHT) == 0)
						overlap = 0;
					else
					{
						Object1.touching |= LEFT;
						Object2.touching |= RIGHT;
					}
				}
			}
		}

		//Then adjust their positions and velocities accordingly (if there was any overlap)
		if(overlap != 0)
		{
			float obj1v = Object1.velocity.x;
			float obj2v = Object2.velocity.x;

			if(!obj1immovable && !obj2immovable)
			{
				overlap *= 0.5f;
				Object1.x = Object1.x - overlap;
				Object2.x += overlap;

				float obj1velocity = (float) (Math.sqrt((obj2v * obj2v * Object2.mass)/Object1.mass) * ((obj2v > 0)?1:-1));
				float obj2velocity = (float) (Math.sqrt((obj1v * obj1v * Object1.mass)/Object2.mass) * ((obj1v > 0)?1:-1));
				float average = (obj1velocity + obj2velocity)*0.5f;
				obj1velocity -= average;
				obj2velocity -= average;
				Object1.velocity.x = average + obj1velocity * Object1.elasticity;
				Object2.velocity.x = average + obj2velocity * Object2.elasticity;
			}
			else if(!obj1immovable)
			{
				Object1.x = Object1.x - overlap;
				Object1.velocity.x = obj2v - obj1v*Object1.elasticity;
			}
			else if(!obj2immovable)
			{
				Object2.x += overlap;
				Object2.velocity.x = obj1v - obj2v*Object2.elasticity;
			}
			return true;
		}
		else
			return false;
	}

	/**
	 * The Y-axis component of the object separation process.
	 * 
	 * @param	Object1		Any <code>FlxObject</code>.
	 * @param	Object2		Any other <code>FlxObject</code>.
	 * 
	 * @return	Whether the objects in fact touched and were separated along the Y axis.
	 */
	static public boolean separateY(FlxObject Object1, FlxObject Object2)
	{
		//can't separate two immovable objects
		boolean obj1immovable = Object1.immovable;
		boolean obj2immovable = Object2.immovable;
		if(obj1immovable && obj2immovable)
			return false;

		//If one of the objects is a tilemap, just pass it off.
		if(Object1 instanceof FlxTilemap)
			return ((FlxTilemap) (Object1)).overlapsWithCallback(Object2,separateY);
		if(Object2 instanceof FlxTilemap)
			return ((FlxTilemap) (Object2)).overlapsWithCallback(Object1,separateY,true);

		//First, get the two object deltas
		float overlap = 0;
		float obj1delta = Object1.y - Object1.last.y;
		float obj2delta = Object2.y - Object2.last.y;
		if(obj1delta != obj2delta)
		{
			//Check if the Y hulls actually overlap
			float obj1deltaAbs = (obj1delta > 0)?obj1delta:-obj1delta;
			float obj2deltaAbs = (obj2delta > 0)?obj2delta:-obj2delta;
			float obj1x = Object1.x;
			float obj1y = Object1.y - ((obj1delta > 0)?obj1delta:0);
			float obj1width = Object1.width;
			float obj1height = Object1.height + obj1deltaAbs;
			float obj2x = Object2.x;
			float obj2y = Object2.y - ((obj2delta > 0)?obj2delta:0);
			float obj2width = Object2.width;
			float obj2height = Object2.height + obj2deltaAbs;
			if((obj1x + obj1width > obj2x) && (obj1x < obj2x + obj2width) && (obj1y + obj1height > obj2y) && (obj1y < obj2y + obj2height))
			{
				float maxOverlap = obj1deltaAbs + obj2deltaAbs + OVERLAP_BIAS;

				//If they did overlap (and can), figure out by how much and flip the corresponding flags
				if(obj1delta > obj2delta)
				{
					overlap = Object1.y + Object1.height - Object2.y;
					if((overlap > maxOverlap) || (Object1.allowCollisions & DOWN) == 0 || (Object2.allowCollisions & UP) == 0)
						overlap = 0;
					else
					{
						Object1.touching |= DOWN;
						Object2.touching |= UP;
					}
				}
				else if(obj1delta < obj2delta)
				{
					overlap = Object1.y - Object2.height - Object2.y;
					if((-overlap > maxOverlap) || (Object1.allowCollisions & UP) == 0 || (Object2.allowCollisions & DOWN) == 0)
						overlap = 0;
					else
					{
						Object1.touching |= UP;
						Object2.touching |= DOWN;
					}
				}
			}
		}

		//Then adjust their positions and velocities accordingly (if there was any overlap)
		if(overlap != 0)
		{
			float obj1v = Object1.velocity.y;
			float obj2v = Object2.velocity.y;

			if(!obj1immovable && !obj2immovable)
			{
				overlap *= 0.5f;
				Object1.y = Object1.y - overlap;
				Object2.y += overlap;

				float obj1velocity = (float) (Math.sqrt((obj2v * obj2v * Object2.mass)/Object1.mass) * ((obj2v > 0)?1:-1));
				float obj2velocity = (float) (Math.sqrt((obj1v * obj1v * Object1.mass)/Object2.mass) * ((obj1v > 0)?1:-1));
				float average = (obj1velocity + obj2velocity)*0.5f;
				obj1velocity -= average;
				obj2velocity -= average;
				Object1.velocity.y = average + obj1velocity * Object1.elasticity;
				Object2.velocity.y = average + obj2velocity * Object2.elasticity;
			}
			else if(!obj1immovable)
			{
				Object1.y = Object1.y - overlap;
				Object1.velocity.y = obj2v - obj1v*Object1.elasticity;
				//This is special case code that handles cases like horizontal moving platforms you can ride
				if(Object2.active && Object2.moves && (obj1delta > obj2delta))
					Object1.x += Object2.x - Object2.last.x;
			}
			else if(!obj2immovable)
			{
				Object2.y += overlap;
				Object2.velocity.y = obj1v - obj2v*Object2.elasticity;
				//This is special case code that handles cases like horizontal moving platforms you can ride
				if(Object1.active && Object1.moves && (obj1delta < obj2delta))
					Object2.x += Object1.x - Object1.last.x;
			}
			return true;
		}
		else
			return false;
	}

	/**
	 * Internal callback function for collision.
	 */
	protected static IFlxObject separateX = new IFlxObject()
	{
		@Override
		public boolean callback(FlxObject Object1, FlxObject Object2)
		{
			return separateX(Object1, Object2);
		}
	};

	/**
	 * Internal callback function for collision.
	 */
	protected static IFlxObject separateY = new IFlxObject()
	{
		@Override
		public boolean callback(FlxObject Object1, FlxObject Object2)
		{
			return separateY(Object1, Object2);
		}
	};
}
