package org.flixel.plugin.flxbox2d.collision.shapes;

import java.util.ArrayList;

import org.flixel.FlxCamera;
import org.flixel.FlxG;
import org.flixel.FlxPoint;
import org.flixel.FlxSprite;
import org.flixel.FlxU;
import org.flixel.plugin.flxbox2d.B2FlxB;
import org.flixel.plugin.flxbox2d.common.math.B2FlxMath;
import org.flixel.plugin.flxbox2d.dynamics.joints.B2FlxJoint;
import org.flixel.plugin.flxbox2d.system.debug.B2FlxDebug;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * This is an abstract or parent class for all shapes. It contains the required variables for 
 * creating the body and debug drawing.
 * 
 * @author Ka Wing Chin
 */
public abstract class B2FlxShape extends FlxSprite
{	
	/**
	 * The ratio from meters to pixels. 
	 */
	public final float RATIO = B2FlxB.RATIO;	
	/**
	 * A body which isn't affected by world forces and it does not react to collisions. Can't move.
	 */
	public static final BodyType STATIC = BodyType.StaticBody;
	/**
	 * A hybrid body which is the same as STATIC, but can moved with a linear velocity like a DYNAMIC.
	 */
	public static final BodyType KINEMATIC = BodyType.KinematicBody;
	/**
	 * A body which is affected by world forces and react to collisions.
	 */
	public static final BodyType DYNAMIC = BodyType.DynamicBody;
	
	/**
	 * A shape used for collision detection. Will be disposed after create() is called.
	 */
	protected Shape shape;
	/**
	 * This stores the density, friction, bounciness (restitution) and the shape of the object.
	 */
	public FixtureDef fixtureDef;
	/**
	 * A fixture is used to attach a shape to a body for collision detection.
	 */
	public Fixture fixture;
	/**
	 * This stores the position, angle and the type of the body.
	 */
	public BodyDef bodyDef;
	/**
	 * A rigid body.
	 */
	public Body body;
	/**
	 * The position in box2d world where the body is located.
	 */
	public Vector2 position;
	/**
	 * The center in real world coordinates.
	 */
	public FlxPoint center;
	/**
	 * Whether the body can be dragged or not.
	 */
	private boolean _draggable;
	/**
	 * Holds the user data.
	 */
	public ObjectMap<String, Object>userData;
	/**
	 * Holds all joints that are attached to this body.
	 */
	public Array<B2FlxJoint> joints;
	/**
	 * A list of fixtures that are attached to the body.
	 */
	private static ArrayList<Fixture> _fixtures;
	/**
	 * The fixture that is used for debug drawing.
	 */
	private Fixture _fixtureDebug;
	/**
	 * Internal, for debug purpose only. Destroying these shouldn't be done in this class.
	 */
	final static Vector2 f = new Vector2();
	final static Vector2 v = new Vector2();
	final static Vector2 lv = new Vector2();
	final static Vector2 t = new Vector2();
	final static Vector2 lower = new Vector2();
	final static Vector2 upper = new Vector2();
	final static Vector2 axis = new Vector2(); 
	final static FlxPoint p1 = new FlxPoint();
	final static FlxPoint p2 = new FlxPoint();
	
	/**
	 * Constructor
	 * @param x		The X-coordinate of the point in space.
	 * @param y 	The Y-coordinate of the point in space.
	 */
	public B2FlxShape(float x, float y)
	{
		super(x, y);
		init();
		center = new FlxPoint(width*.5f, height*.5f);
		
		userData = new ObjectMap<String, Object>();
		userData.put("draggable", _draggable);
		
		joints = new Array<B2FlxJoint>();
		userData.put("joints", joints);
	}
	
	/**
	 * Constructor
	 * @param x		The X-coordinate of the point in space.
	 */
	public B2FlxShape(float x)
	{
		this(x, 0);
	}
	
	/**
	 * Constructor
	 */
	public B2FlxShape()
	{
		this(0, 0);
	}

	/**
	 * Initialize the fixtureDef, bodyDef, and set the default values.
	 */
	private void init()
	{
		fixtureDef = new FixtureDef();
		bodyDef = new BodyDef();
		bodyDef.type = DYNAMIC;
		defineDefaults();
		setDefaults();
	}
	
	/**
	 * Define the defaults.
	 */
	private void defineDefaults()
	{			
		_draggable = false;
	}
	
	/**
	 * Override this in child class to hook for object specific params.
	 */
	abstract public void setDefaults();
	
	/**
	 * Override this in child class to create specific shape.
	 */
	abstract public void createShape();
	
	/**
	 * Override this in child class to create a specific body.
	 */
	abstract protected void createBody();

	/**
	 * Creates a shapeless body.
	 * @return	This object.
	 */
	public B2FlxShape create()
	{
		createBody();
		userData.put("exists", exists);
		body.setUserData(userData);
		_fixtureDebug = fixture;
		return this;
	}
	
	/**
	 * Main loop. Calculates the position and angle, and updates the mouse joint if it is used.
	 */
	@Override
	public void update()
	{
		if(body != null)
		{
			Vector2 position = body.getPosition();
			x = position.x * RATIO - width * .5f;
			y = position.y * RATIO - height * .5f;			
			
			angle = body.getAngle() * B2FlxMath.RADDEG;
			// TODO: This resets the angle, prevents increase of the angle value. 
			// sure if setTransform is alright, it breaks the contacts.
			// This doesn't work with shapes that are joined with gear joint.
			if(angle >= 360 || angle <= -360)
				body.setTransform(position.x, position.y, 0);
		}
	}

	/**
	 * Kills the body and destroy all joints that are attached to this body.
	 */
	@Override
	public void kill()
	{
		if(!B2FlxB.world.isLocked() && exists && body != null)
		{
			for(int i = 0; i < joints.size; i++)
			{
				joints.get(i).kill();
			}
			B2FlxB.world.destroyBody(body);
			body = null;
			super.kill();
			userData.put("exists", exists);
		}
	}
	
	/**
	 * Cleans up the memory. Don't call this during update.
	 */
	@Override
	public void destroy()
	{
		super.destroy();
		if(body != null)
		{
			kill();
		}
		joints.clear();
		body = null;		
		bodyDef = null;
		fixture = null;
		fixtureDef = null;
		disposeShape();
		position = null;
		if(userData != null)
			userData.clear();
		userData = null;
		_fixtureDebug = null;
	}
	
	/**
	 * Revives the body. Joints that were attached to this body won't revived along.
	 * The angle is set 0.
	 */
	@Override
	public void revive()
	{
		if(body == null)
		{
			createShape();
			create();			
		}
		angle = 0;
		framePixels.setRotation(angle);
		body.setLinearVelocity(new Vector2(0,0));
		body.setAngularVelocity(0);
		body.setTransform(x/RATIO, y/RATIO, angle);
		exists = alive = true;
		userData.put("exists", exists);
	}
	
	/**
	 * The coordinates are put first, so t hat super.reset() revives will be called 
	 * with the correct coordinates.
	 * @param 	X	The new X position of this object.
	 * @param	Y	The new Y position of this object.
	 */
	@Override
	public void reset(float X, float Y)
	{		
		x = X;
		y = Y;
		super.reset(X, Y);
	}
	
	/**
	 * The bounding box is checked from the AABB rather than from the framewidth and height.
	 * If the frame dimension is bigger than the bounding box, the object will disappear even
	 * if a part of the frame is still on the screen. Edge and Chain shape will always be true
	 * for drawing debug lines. These two don't have a bounding box and can't be checked whether
	 * if they are on screen or not.
	 * @param camera	Specify which game camera you want.
	 * @return	Whether the object is on screen or not.
	 */
	@Override
	public boolean onScreen(FlxCamera camera)
	{
		if(!exists || (body == null))
			return false;
		
		if(camera == null)
			camera = FlxG.camera;
		
		int length = body.getFixtureList().size();
		_fixtures = body.getFixtureList();
		boolean onScreen = false;
		for(int i = 0; i < length; i++)
		{
			_fixtureDebug = _fixtures.get(i);			
			if(onScreen == false)
			{
				Vector2[] vertices = B2FlxB.vertices;
				Transform transform = body.getTransform();
				if(_fixtureDebug.getType() == Type.Circle)
				{
					CircleShape shape = (CircleShape) _fixtureDebug.getShape();
					float radius = shape.getRadius();
					vertices[0].set(shape.getPosition());
					vertices[0].rotate(transform.getRotation()).add(transform.getPosition());
					lower.set(vertices[0].x - radius, vertices[0].y - radius);
					upper.set(vertices[0].x + radius, vertices[0].y + radius);
				}
				else if(_fixtureDebug.getType() == Type.Polygon)
				{
					PolygonShape shape = (PolygonShape) _fixtureDebug.getShape();
					int vertexCount = shape.getVertexCount();
					shape.getVertex(0, vertices[0]);
					lower.set(transform.mul(vertices[0]));
					upper.set(lower);
					for(int j = 1; j < vertexCount; j++)
					{
						shape.getVertex(j, vertices[j]);
						transform.mul(vertices[j]);
						lower.x = FlxU.min(lower.x, vertices[j].x);
						lower.y = FlxU.min(lower.y, vertices[j].y);
						upper.x = FlxU.max(upper.x, vertices[j].x);
						upper.y = FlxU.max(upper.y, vertices[j].y);
					}
				}
				else if (_fixtureDebug.getType() == Type.Edge) 
				{
					return true;
				}
				else if (_fixtureDebug.getType() == Type.Chain) 
				{
					return true;
				}
				else
					return false;
				
				lower.mul(RATIO);
				upper.mul(RATIO);
				
				p1.x = lower.x - camera.scroll.x * scrollFactor.x;
				p1.y = lower.y - camera.scroll.y * scrollFactor.y;
				p2.x = upper.x - camera.scroll.x * scrollFactor.x;
				p2.y = upper.y - camera.scroll.y * scrollFactor.y;
				
				// Check whether the bounding box are within the camera.
				if( ((p1.x >= 0 && p1.x <= camera.width) || (p2.x >= 0 && p2.x <= camera.width)) &&
					((p1.y >= 0 && p1.y <= camera.height) ||(p2.y >= 0 && p2.y <= camera.height)))
				{
					onScreen = true;
				}
			}
		}			
				
		// It's out side the camera.
		return onScreen;
	}
	
	/**
	 * The bounding box is checked from the AABB rather than from the framewidth and height.
	 * If the frame dimension is bigger than the bounding box, the object will disappear even
	 * if a part of the frame is still on the screen. Edge and Chain shape will always be true
	 * for drawing debug lines. These two don't have a bounding box and can't be checked whether
	 * if they are on screen or not.	 * 
	 * @return	Whether the object is on screen or not.
	 */
	@Override
	public boolean onScreen()
	{		
		return onScreen(null);
	}
	
	/**
	 * Called by game loop, updates then blits or renders current frame of animation to the screen.
	 * It's a modified version of FlxSprite::draw().
	 */
	@Override
	public void draw()
	{
		if(_flickerTimer != 0)
		{
			_flicker = !_flicker;
			if(_flicker)
				return;
		}
		
		if(dirty)	//rarely 
			calcFrame();
		
		if(_newTextureData != null)	//even more rarely
		{
			_pixels.getTexture().load(_newTextureData);
			_newTextureData = null;
		}
		
		FlxCamera camera = FlxG.getActiveCamera();
		
		if (cameras != null && !cameras.contains(camera, true))
			return;
				
		// Check whether it's outside the screen.
		if(!onScreen(camera))
			return;
		
		// Don't draw if the fixture is an edge or a chain.
		if(_fixtureDebug.getType() != Type.Edge || _fixtureDebug.getType() != Type.Chain)
		{
			_point.x = x - (camera.scroll.x * scrollFactor.x) - offset.x;
			_point.y = y - (camera.scroll.y * scrollFactor.y) - offset.y;
			_point.x += (_point.x > 0) ? 0.0000001f : -0.0000001f;
			_point.y += (_point.y > 0) ? 0.0000001f : -0.0000001f;
			
			//tinting
			int tintColor = FlxU.multiplyColors(_color, camera.getColor());
			framePixels.setColor(((tintColor >> 16) & 0xFF) * 0.00392f, ((tintColor >> 8) & 0xFF) * 0.00392f, (tintColor & 0xFF) * 0.00392f, _alpha);
			
			if(((angle == 0) || (_bakedRotation > 0)) && (scale.x == 1) && (scale.y == 1) && (blend == null))
			{ 	//Simple render
				framePixels.setPosition(_point.x, _point.y);
				framePixels.draw(FlxG.batch);
			}
			else
			{ 	//Advanced render
				framePixels.setOrigin(origin.x, origin.y);
				framePixels.setScale(scale.x, scale.y);
				if((angle != 0) && (_bakedRotation <= 0))
					framePixels.setRotation(angle);
				framePixels.setPosition(_point.x, _point.y);
				if(blend != null)
				{
					FlxG.batch.setBlendFunction(blend[0], blend[1]);
					framePixels.draw(FlxG.batch);
					FlxG.batch.setBlendFunction(0x0302, 0x0303);
				}
				else
				{
					framePixels.draw(FlxG.batch);
				}
			}			
		}		
		if(FlxG.visualDebug && !ignoreDrawDebug)
			drawDebug(camera);
	}
	
	/**
	 * Draws body lines and AABB.
	 * @param camera	Specify which game camera you want.
	 */
	@Override
	public void drawDebug(FlxCamera camera)
	{				
		if(B2FlxDebug.drawBodies || B2FlxDebug.drawAABBs)
		{
			if (body.isActive() && B2FlxDebug.drawInactiveBodies)
				return;			
			Transform transform = body.getTransform();
			if(B2FlxDebug.drawBodies)
			{
				for(int i = 0; i < _fixtures.size(); i++)
				{
					_fixtureDebug = _fixtures.get(i);
					if(body.isActive() == false)
						drawShape(_fixtureDebug, transform, B2FlxDebug.SHAPE_NOT_ACTIVE);
					else if(body.getType() == BodyType.StaticBody)
						drawShape(_fixtureDebug, transform, B2FlxDebug.SHAPE_STATIC);
					else if(body.getType() == BodyType.KinematicBody)
						drawShape(_fixtureDebug, transform, B2FlxDebug.SHAPE_KINEMATIC);
					else if(body.isAwake() == false)
						drawShape(_fixtureDebug, transform, B2FlxDebug.SHAPE_NOT_AWAKE);
					else
						drawShape(_fixtureDebug, transform, B2FlxDebug.SHAPE_AWAKE);
					if(B2FlxDebug.drawAABBs)
					{
						if(_fixtureDebug.getType() != Type.Edge && _fixtureDebug.getType() != Type.Chain)
							drawAABB(_fixtureDebug, transform);
					}
				}
			}
		}
	}
	
	/**
	 * Debug-only: draws the shape line.
	 * @param fixture
	 * @param transform
	 * @param color
	 */
	protected void drawShape(Fixture fixture, Transform transform, int color)
	{
		if(fixture.getType() == Type.Circle)
		{
			CircleShape circle = (CircleShape) fixture.getShape();
			t.set(circle.getPosition());
			transform.mul(t);
			drawSolidCircle(t, circle.getRadius(), axis.set(transform.vals[Transform.COS], transform.vals[Transform.SIN]), color);			
		}
		else if(fixture.getType() == Type.Polygon)
		{
			PolygonShape poly = (PolygonShape) fixture.getShape();
			int vertexCount = poly.getVertexCount();
			for(int i = 0; i < vertexCount; i++)
			{
				poly.getVertex(i, B2FlxB.vertices[i]);
				transform.mul(B2FlxB.vertices[i]);
			}
			drawSolidPolygon(B2FlxB.vertices, vertexCount, color);
		}
		else if(fixture.getType() == Type.Edge)
		{
			EdgeShape edge = (EdgeShape) fixture.getShape();
			edge.getVertex1(B2FlxB.vertices[0]);
			edge.getVertex2(B2FlxB.vertices[1]);
			transform.mul(B2FlxB.vertices[0]);
			transform.mul(B2FlxB.vertices[1]);
			drawSolidPolygon(B2FlxB.vertices, 2, color);
		}
		else if(fixture.getType() == Type.Chain)
		{
			ChainShape chain = (ChainShape) fixture.getShape();
			int vertexCount = chain.getVertexCount();
			for(int i = 0; i < vertexCount; i++)
			{
				chain.getVertex(i, B2FlxB.vertices[i]);
				transform.mul(B2FlxB.vertices[i]);
			}
			drawSolidPolygon(B2FlxB.vertices, vertexCount, color);
		}
	}
	
	/**
	 * Debug-only: draws the AABB (bounding box).
	 * @param fixture
	 * @param transform
	 */
	private void drawAABB(Fixture fixture, Transform transform)
	{
		Vector2[] vertices = B2FlxB.vertices;
		if (fixture.getType() == Type.Circle) 
		{
			CircleShape shape = (CircleShape)fixture.getShape();
			float radius = shape.getRadius();
			vertices[0].set(shape.getPosition());
			vertices[0].rotate(transform.getRotation()).add(transform.getPosition());
			lower.set(vertices[0].x - radius, vertices[0].y - radius);
			upper.set(vertices[0].x + radius, vertices[0].y + radius);

			// define vertices in ccw fashion...
			vertices[0].set(lower.x, lower.y);
			vertices[1].set(upper.x, lower.y);
			vertices[2].set(upper.x, upper.y);
			vertices[3].set(lower.x, upper.y);

			drawSolidPolygon(vertices, 4, B2FlxDebug.AABB_COLOR);
		}
		else if (fixture.getType() == Type.Polygon) 
		{
			PolygonShape shape = (PolygonShape)fixture.getShape();
			int vertexCount = shape.getVertexCount();

			shape.getVertex(0, vertices[0]);
			lower.set(transform.mul(vertices[0]));
			upper.set(lower);
			for (int i = 1; i < vertexCount; i++) 
			{
				shape.getVertex(i, vertices[i]);
				transform.mul(vertices[i]);
				lower.x = Math.min(lower.x, vertices[i].x);
				lower.y = Math.min(lower.y, vertices[i].y);
				upper.x = Math.max(upper.x, vertices[i].x);
				upper.y = Math.max(upper.y, vertices[i].y);
			}

			// define vertices in ccw fashion...
			vertices[0].set(lower.x, lower.y);
			vertices[1].set(upper.x, lower.y);
			vertices[2].set(upper.x, upper.y);
			vertices[3].set(lower.x, upper.y);

			drawSolidPolygon(vertices, 4, B2FlxDebug.AABB_COLOR);
		}
	}
	
	/**
	 * Debug-only: draw a line for polygon shapes (B2FlxBox, B2FlxPolygon, B2FlxEdge and B2FlxChain).
	 * @param vertices		An array which contains the vertices.
	 * @param vertexCount	The amount of of vertices.
	 * @param color			The color of the line.
	 */
	protected void drawSolidPolygon (Vector2[] vertices, int vertexCount, int color) 
	{				
		FlxCamera camera = FlxG.getActiveCamera();
		if(camera == null)
			camera = FlxG.camera;
		
		ShapeRenderer renderer = FlxG.flashGfx.getShapeRenderer();
		FlxG.flashGfx.lineStyle(1, color, 1);
		for(int i = 0; i < vertexCount; i++) 
		{
			Vector2 v = vertices[i].mul(RATIO);
			v.x -= (camera.scroll.x * scrollFactor.x) - offset.x;
			v.y -= (camera.scroll.y * scrollFactor.y) - offset.y;
			if(i == 0) 
			{
				lv.set(v);
				f.set(v);
				continue;
			}			
			renderer.line(lv.x, lv.y, v.x, v.y);
			lv.set(v);
		}
		renderer.line(f.x, f.y, lv.x, lv.y);
	}
	
	/*
	 * Debug-only: draw a circle for circle shape.
	 * @param center	The center of the circle.
	 * @param radius	The radius of the circle.
	 * @param axis		The axis of the circle.
	 * @param color		The color of the circle.
	 */
	protected void drawSolidCircle (Vector2 center, float radius, Vector2 axis, int color) 
	{		
		FlxCamera camera = FlxG.getActiveCamera();
		if(camera == null)
			camera = FlxG.camera;
		
		float angle = 0;
		float angleInc = 2 * MathUtils.PI / 20f;
		ShapeRenderer renderer = FlxG.flashGfx.getShapeRenderer();
		FlxG.flashGfx.lineStyle(1, color, 1);
		for(int i = 0; i < 20; i++, angle += angleInc) 
		{
			v.set(MathUtils.cos(angle) * radius + center.x, MathUtils.sin(angle) * radius + center.y).mul(RATIO);
			v.x -= (camera.scroll.x * scrollFactor.x) - offset.x;
			v.y -= (camera.scroll.y * scrollFactor.y) - offset.y;
			if (i == 0) 
			{
				lv.set(v);
				f.set(v);
				continue;
			}
			renderer.line(lv.x, lv.y, v.x, v.y);
			lv.set(v);
		}
		renderer.line(f.x, f.y, lv.x, lv.y);
		center.mul(RATIO);
		axis.mul(RATIO);
		center.x -= (camera.scroll.x * scrollFactor.x) - offset.x;
		center.y -= (camera.scroll.y * scrollFactor.y) - offset.y;
		renderer.line(center.x, center.y, 0, center.x + axis.x * radius, center.y + axis.y * radius, 0);
	}
	
	/**
	 * Dispose the shape. Use this when create() is not called. 
	 * After you use createFixture() for example.
	 * @return	This object. Handy for chaining stuff together.
	 */
	public B2FlxShape disposeShape()
	{
		if(shape != null)
		{
			shape.dispose();
			shape = null;			
		}
		return this;
	}
	
	
	/**
	 * Creates a fixture and attach to this body. 
	 * @param sprite	The sprite which contains a shape.
	 * @param density	The density of the fixture.
	 * @param dispose	Whether the shape needs to be dispose after creating the fixture.
	 * @return	The fixture that was created.
	 */
	public Fixture createFixture(B2FlxShape sprite, float density, boolean dispose)
	{
		fixture = body.createFixture(sprite.fixtureDef.shape, density);
		if(dispose)
			disposeShape();
		return fixture;
	}
	
	/**
	 * Creates a fixture and attach to this body. 
	 * @param sprite	The sprite which contains a shape.
	 * @param density	The density of the fixture.
	 * @return	The fixture that was created.
	 */
	public Fixture createFixture(B2FlxShape sprite, float density)
	{
		return createFixture(sprite, density, false);
	}
	
	/**
	 * Creates a fixture and attach to this body. 
	 * @param sprite	The sprite which contains a shape.
	 * @return	The fixture that was created.
	 */
	public Fixture createFixture(B2FlxShape sprite)
	{
		return createFixture(sprite, 0, false);
	}
	
	/**
	 * Creates a fixture and attach to this body.
	 * @param fixutreDef The fixtureDef which contains a shape.
	 * @return	The fixture that was created.
	 */
	public Fixture createFixture(FixtureDef fixtureDef, boolean dispose)
	{
		fixture = body.createFixture(fixtureDef);
		if(dispose)
			disposeShape();
		return fixture;
	}
	
	/**
	 * Creates a fixture and attach to this body.
	 * @return	The fixture that was created.
	 */
	public Fixture createFixture(FixtureDef fixtureDef)
	{
		return createFixture(fixtureDef, false);
	}

	/**
	 * Set user data. It can be any object.
	 * @param userData	The object that will be stored.
	 */
	public void setUserData(Object userData)
	{
		if(body != null)
			body.setUserData(userData);
	}
	
	/**
	 * Set the type of the body.
	 * @return	This object. Handy for chaining stuff together.
	 */
	public B2FlxShape setType(BodyType type)
	{
		bodyDef.type = type;
		return this;
	}
	
	/**
	 * Set the fixture definition.
	 * @param 	fixtureDef
	 * @return 	This object. Handy for chaining stuff together.
	 */
	public B2FlxShape setFixtureDef(FixtureDef fixtureDef)
	{
		this.fixtureDef = fixtureDef;
		createShape();
		return this;
	}
	
	/**
	 * Set the linear damping.
	 * @param 	linearDamping
	 * @return	This object. Handy for chaining stuff together.
	 */
	public B2FlxShape setLinearDamping(float linearDamping)
	{
		bodyDef.linearDamping = linearDamping;
		return this;
	}
	
	/**
	 * Set the linear velocity.
	 * @param linearVelocity
	 * @return	This object. Handy for chaining stuff together.
	 */
	public B2FlxShape setLinearVelocity(Vector2 linearVelocity)
	{
		bodyDef.linearVelocity.set(linearVelocity);
		return this;
	}
	
	/**
	 * Set the linear velocity.
	 * @param x	The X-velocity.
	 * @param y The Y-velocity.
	 * @return	This object. Handy for chaining stuff together.
	 */
	public B2FlxShape setLinearVelocity(float x, float y)
	{
		bodyDef.linearVelocity.set(x, y);
		return this;
	}
	
	/**
	 * Set the angular damping.
	 * @param angularDamping
	 * @return	This object. Handy for chaining stuff together.
	 */
	public B2FlxShape setAngularDamping(float angularDamping)
	{
		bodyDef.angularDamping = angularDamping;
		return this;
	}
	
	/**
	 * Set the angular velocity.
	 * @param angularVelocity
	 * @return	This object. Handy for chaining stuff together.
	 */
	public B2FlxShape setAngularVelocity(float angularVelocity)
	{
		bodyDef.angularVelocity = angularVelocity;
		return this;
	}
	
	/**
	 * Set the bullet.
	 * @param bullet
	 * @return	This object. Handy for chaining stuff together.
	 */
	public B2FlxShape setBullet(boolean bullet)
	{
		bodyDef.bullet = bullet;
		return this;
	}
	
	public B2FlxShape setFixedRotation(boolean fixedRotation)
	{
		bodyDef.fixedRotation = fixedRotation;
		return this;
	}
	
	public B2FlxShape allowSleep(boolean allowSleep)
	{
		bodyDef.allowSleep = allowSleep;
		return this;
	}
	
	public B2FlxShape setDensity(float density)
	{
		fixtureDef.density = density;
		return this;
	}
	
	public B2FlxShape setFriction(float friction)
	{
		fixtureDef.friction = friction;
		return this;
	}
	
	public B2FlxShape setRestitution(float restitution)
	{
		fixtureDef.restitution = restitution;
		return this;
	}
	
	public B2FlxShape setAngle(float angle)
	{
		bodyDef.angle = angle * B2FlxMath.DEGRAD;
		return this;
	}
	
	public B2FlxShape setGravityScale(float gravityScale)
	{
		bodyDef.gravityScale = gravityScale;
		return this;
	}
	
	public B2FlxShape setMaskBits(short maskBits)
	{
		fixtureDef.filter.maskBits = maskBits;
		return this;
	}
	
	public B2FlxShape setCategoryBits(short categoryBits)
	{
		fixtureDef.filter.categoryBits = categoryBits;
		return this;
	}
	
	public B2FlxShape setGroupIndex(short groupIndex)
	{
		fixtureDef.filter.groupIndex = groupIndex;
		return this;
	}
	
	public B2FlxShape setSensor(boolean sensor)
	{
		fixtureDef.isSensor = sensor;
		return this;
	}

	public B2FlxShape setDraggable(boolean draggable)
	{
		_draggable = draggable;
		if(_draggable)
			userData.put("draggable", true);
		else if(!_draggable)
			userData.put("draggable", false);
		return this;
	}
	
	public boolean getDraggable()
	{
		return _draggable;
	}
}
