package org.flixel.plugin.flxbox2d.collision.shapes;

import org.flixel.FlxPoint;
import org.flixel.FlxSprite;
import org.flixel.plugin.flxbox2d.B2FlxB;
import org.flixel.plugin.flxbox2d.common.math.B2FlxMath;
import org.flixel.plugin.flxbox2d.dynamics.joints.B2FlxMouseJoint;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 * This class doesn't have any shape. It's a shapeless body which is used 
 * to combine multiple shapes together.
 * 
 * @author Ka Wing Chin
 */
public class B2FlxSprite extends FlxSprite
{
	public final float RATIO = B2FlxB.RATIO;
	
	// A body which isn't affected by world forces and it does not react to collisions. Can't move.
	public static final BodyType STATIC = BodyType.StaticBody;
	// A hybrid body which is the same as STATIC, but can moved with a linear velocity like a DYNAMIC.
	public static final BodyType KINEMATIC = BodyType.KinematicBody;
	// A body which is affected by world forces and react to collisions.
	public static final BodyType DYNAMIC = BodyType.DynamicBody;
	
	// A shape used for collision detection. Will be disposed after create() is called.
	protected Shape shape;
	// This stores the density, friction, bounciness (restitution) and the shape of the object.
	public FixtureDef fixtureDef;
	// A fixture is used to attach a shape to a body for collision detection.
	public Fixture fixture;
	// This stores the position, angle and the type of the body.
	public BodyDef bodyDef;
	// A rigid body.
	public Body body;
	// The position in box2d world where the body is located.
	public Vector2 position;
	// The center in real world coordinates.
	public FlxPoint center;
	// Whether the body is draggable or not.
	private boolean _draggable; //TODO this can be deleted or make it public.
	// Mouse Joint, used for mouse events on the body.
	protected B2FlxMouseJoint mouseJoint;
	
	/**
	 * It creates an shapeless body.
	 * @param x		The X-coordinate of the point in space.
	 * @param y 	The Y-coordinate of the point in space.
	 */
	public B2FlxSprite(float x, float y)
	{
		super(x, y);
		init();
		center = new FlxPoint(width*.5f, height*.5f);

		if(_draggable)
			mouseJoint = new B2FlxMouseJoint();

//		setUserData(this);
	}
	
	/**
	 * It creates an shapeless body.
	 * @param x		The X-coordinate of the point in space.
	 */
	public B2FlxSprite(float x)
	{
		this(x, 0);
	}
	
	/**
	 * It creates an shapeless body.
	 */
	public B2FlxSprite()
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
		_draggable = true; // TODO: set this to false;
	}
	
	/**
	 * Override this in child class to hook for object specific params.
	 */
	public void setDefaults(){}
	
	/**
	 * Override this in child class to create specific shape.
	 */
	public void createShape(){}

	/**
	 * Creates a shapeless body.
	 * @return	This object.
	 */
	public B2FlxSprite create()
	{
		bodyDef.position.x = x / RATIO;
		bodyDef.position.y = y / RATIO;
		position = bodyDef.position;
		body = B2FlxB.world.createBody(bodyDef);
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
			// This could give some problem with certain joints.
			if(angle >= 360)
				body.setTransform(position.x, position.y, 0);
			if(_draggable && mouseJoint != null)
				mouseJoint.update();
		}
		super.update();
	}

	/**
	 * Kills the body and the mouse joint if it is used.
	 */
	@Override
	public void kill()
	{
		if(mouseJoint != null)
			mouseJoint.destroy();
		B2FlxB.world.destroyBody(body);
		super.kill();
	}
	
	/**
	 * Cleans up the memory.
	 */
	@Override
	public void destroy()
	{
		super.destroy();
		if(body != null)
			B2FlxB.world.destroyBody(body);
		body = null;		
		bodyDef = null;
		fixture = null;
		fixtureDef = null;
		disposeShape();
		position = null;
		if(mouseJoint != null)
		{
			mouseJoint.destroy();
			mouseJoint = null;
		}
	}
	
	@Override
	public void revive()
	{
		// TODO: revive an object.
		super.revive();
	}
	
	/**
	 * Dispose the shape. Use this when create() is not called. 
	 * After you use createFixture() for example.
	 * @return	This object. Handy for chaining stuff together.
	 */
	public B2FlxSprite disposeShape()
	{
		if(shape != null)
		{
			shape.dispose();
			shape = null;			
		}
		return this;
	}
	
	// TODO: at an extra argument for disposing shapes?
	/**
	 * Creates a fixture and attach to this body. 
	 * @param sprite	The sprite which contains a shape.
	 * @param density	The density of the fixture.
	 * @return	The fixture that was created.
	 */
	public Fixture createFixture(B2FlxSprite sprite, float density)
	{
		return fixture = body.createFixture(sprite.fixtureDef.shape, density);
	}
	
	/**
	 * Creates a fixture and attach to this body. 
	 * @param sprite	The sprite which contains a shape.
	 * @return	The fixture that was created.
	 */
	public Fixture createFixture(B2FlxSprite sprite)
	{
		return createFixture(sprite, 0);
	}
	
	/**
	 * Creates a fixture and attach to this body. 
	 * @param fixutreDef The fixtureDef which contains a shape.
	 * @return	The fixture that was created.
	 */
	public Fixture createFixture(FixtureDef fixtureDef)
	{
		return fixture = body.createFixture(fixtureDef);
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
	public B2FlxSprite setType(BodyType type)
	{
		bodyDef.type = type;
		return this;
	}
	
	/**
	 * Set the fixture definition.
	 * @param 	fixtureDef
	 * @return 	This object. Handy for chaining stuff together.
	 */
	public B2FlxSprite setFixtureDef(FixtureDef fixtureDef)
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
	public B2FlxSprite setLinearDamping(float linearDamping)
	{
		bodyDef.linearDamping = linearDamping;
		return this;
	}
	
	/**
	 * Set the linear velocity.
	 * @param linearVelocity
	 * @return	This object. Handy for chaining stuff together.
	 */
	public B2FlxSprite setLinearVelocity(Vector2 linearVelocity)
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
	public B2FlxSprite setLinearVelocity(float x, float y)
	{
		bodyDef.linearVelocity.set(x, y);
		return this;
	}
	
	/**
	 * Set the angular damping.
	 * @param angularDamping
	 * @return	This object. Handy for chaining stuff together.
	 */
	public B2FlxSprite setAngularDamping(float angularDamping)
	{
		bodyDef.angularDamping = angularDamping;
		return this;
	}
	
	/**
	 * Set the angular velocity.
	 * @param angularVelocity
	 * @return	This object. Handy for chaining stuff together.
	 */
	public B2FlxSprite setAngularVelocity(float angularVelocity)
	{
		bodyDef.angularVelocity = angularVelocity;
		return this;
	}
	
	/**
	 * Set the bullet.
	 * @param bullet
	 * @return	This object. Handy for chaining stuff together.
	 */
	public B2FlxSprite setBullet(boolean bullet)
	{
		bodyDef.bullet = bullet;
		return this;
	}
	
	public B2FlxSprite setFixedRotation(boolean fixedRotation)
	{
		bodyDef.fixedRotation = fixedRotation;
		return this;
	}
	
	public B2FlxSprite allowSleep(boolean allowSleep)
	{
		bodyDef.allowSleep = allowSleep;
		return this;
	}
	
	public B2FlxSprite setDensity(float density)
	{
		fixtureDef.density = density;
		return this;
	}
	
	public B2FlxSprite setFriction(float friction)
	{
		fixtureDef.friction = friction;
		return this;
	}
	
	public B2FlxSprite setRestitution(float restitution)
	{
		fixtureDef.restitution = restitution;
		return this;
	}
	
	public B2FlxSprite setAngle(float angle)
	{
		bodyDef.angle = angle * B2FlxMath.DEGRAD;
		return this;
	}
	
	public B2FlxSprite setGravityScale(float gravityScale)
	{
		bodyDef.gravityScale = gravityScale;
		return this;
	}
	
	public B2FlxSprite setMaskBits(short maskBits)
	{
		fixtureDef.filter.maskBits = maskBits;
		return this;
	}
	
	public B2FlxSprite setCategoryBits(short categoryBits)
	{
		fixtureDef.filter.categoryBits = categoryBits;
		return this;
	}
	
	public B2FlxSprite setGroupIndex(short groupIndex)
	{
		fixtureDef.filter.groupIndex = groupIndex;
		return this;
	}
	
	public B2FlxSprite setSensor(boolean sensor)
	{
		fixtureDef.isSensor = sensor;
		return this;
	}

	public void setDraggable(boolean draggable)
	{
		_draggable = draggable;
		if(_draggable && mouseJoint == null)
			mouseJoint = new B2FlxMouseJoint();
		else if(!_draggable && mouseJoint != null)
			mouseJoint.destroy();
	}
}
