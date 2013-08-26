package org.flixel.plugin.flxbox2d.collision.shapes;

import org.flixel.FlxPoint;
import org.flixel.plugin.flxbox2d.B2FlxB;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * A circle shape.
 * 
 * @author Ka Wing Chin
 */
public class B2FlxCircle extends B2FlxShape
{
	/**
	 * The radius in pixel world which is defined in the constructor.
	 */
	private float _radius;
	/**
	 * The shape radius in box2d world.
	 */
	private float _shapeRadius;
	
	/**
	 * This creates a circle.
	 * @param x			The X-coordinate of the point in space.
	 * @param y 		The Y-coordinate of the point in space.
	 * @param radius	The radius of the circle.
	 */
	public B2FlxCircle(float x, float y, float radius)
	{
		super(x, y);
		if(radius > 0)
			_radius = radius;
		center = new FlxPoint(radius, radius);
		createShape();
	}
	
	/**
	 * This creates a circle with a default radius of 15px.
	 * @param x			The X-coordinate of the point in space.
	 * @param y 		The Y-coordinate of the point in space.
	 */
	public B2FlxCircle(float x, float y)
	{
		this(x, y, 0);
	}
	
	/**
	 * This creates a circle with a default radius of 15px.
	 * @param x			The X-coordinate of the point in space.
	 */
	public B2FlxCircle(float x)
	{
		this(x, 0, 0);
	}
	
	/**
	 * This creates a circle with a default radius of 15px.
	 */
	public B2FlxCircle()
	{
		this(0, 0, 0);
	}
	
	/**
	 * Set the defaults for the radius.
	 */
	@Override
	public void setDefaults()
	{
		_radius = 15f;
		width = height = _radius * 2;
	}
	
	/**
	 * Creates the shape.
	 */
	@Override
	public void createShape()
	{
		shape = new CircleShape();
		shape.setRadius(_radius/B2FlxB.RATIO);
		// The shape radius is gone when the shape got disposed.
		_shapeRadius = shape.getRadius();
		fixtureDef.shape = shape;
	}
	
	/**
	 * Creates the body.
	 */
	@Override
	protected void createBody()
	{	
		bodyDef.position.x = (x + _radius) / B2FlxB.RATIO;
		bodyDef.position.y = (y + _radius) / B2FlxB.RATIO;
		position = bodyDef.position;
		body = B2FlxB.world.createBody(bodyDef);
		fixture = body.createFixture(fixtureDef);
		shape.dispose();
		shape = null;
	}
	
	/**
	 * Creates a Circle body.
	 */
	@Override
	public B2FlxCircle create()
	{
		super.create();
		return this;
	}

	/**
	 * @return Returns the radius of the shape in box2d world.
	 */
	public float getRadius()
	{
		return _shapeRadius;
	}
	
	/**
	 * Set the position of the shape.
	 * @param position
	 */
	public void setShapePosition(Vector2 position)
	{
		((CircleShape)fixtureDef.shape).setPosition(position);
	}
	
	/**
	 * Set the position of the shape.
	 * @param x	The X-position in pixels.
	 * @param y The Y-position in pixels.
	 */
	public void setShapePosition(float x, float y)
	{
		((CircleShape)fixtureDef.shape).setPosition(new Vector2(x/B2FlxB.RATIO, y/B2FlxB.RATIO));
	}

	@Override
	public B2FlxCircle setType(BodyType type){super.setType(type);return this;}	
	@Override
	public B2FlxCircle setFixtureDef(FixtureDef fixtureDef){super.setFixtureDef(fixtureDef);return this;}	
	@Override
	public B2FlxCircle setLinearDamping(float linearDamping){super.setLinearDamping(linearDamping);return this;}
	@Override
	public B2FlxCircle setLinearVelocity(Vector2 linearVelocity){super.setLinearVelocity(linearVelocity);return this;}	
	@Override
	public B2FlxCircle setLinearVelocity(float x, float y){super.setLinearVelocity(x, y);return this;}	
	@Override
	public B2FlxCircle setAngularDamping(float angularDamping){super.setAngularDamping(angularDamping);return this;}	
	@Override
	public B2FlxCircle setAngularVelocity(float angularVelocity){super.setAngularVelocity(angularVelocity);return this;}	
	@Override
	public B2FlxCircle setBullet(boolean bullet){super.setBullet(bullet);return this;}	
	@Override
	public B2FlxCircle setFixedRotation(boolean fixedRotation){super.setFixedRotation(fixedRotation);return this;}	
	@Override
	public B2FlxCircle setAllowSleep(boolean allowSleep){super.setAllowSleep(allowSleep);return this;}	
	@Override
	public B2FlxCircle setActive(boolean active){super.setActive(active);return this;}	
	@Override
	public B2FlxCircle setAwake(boolean awake){super.setAwake(awake);return this;}	
	@Override
	public B2FlxCircle setDensity(float density){super.setDensity(density);return this;}	
	@Override
	public B2FlxCircle setFriction(float friction){super.setFriction(friction);return this;}	
	@Override
	public B2FlxCircle setRestitution(float restitution){super.setRestitution(restitution);return this;}	
	@Override
	public B2FlxCircle setPosition(Vector2 position){super.setPosition(position);return this;}	
	@Override
	public B2FlxCircle setAngle(float angle){super.setAngle(angle);return this;}	
	@Override
	public B2FlxCircle setGravityScale(float gravityScale){super.setGravityScale(gravityScale);return this;}	
	@Override
	public B2FlxCircle setMaskBits(short maskBits){super.setMaskBits(maskBits);return this;}	
	@Override
	public B2FlxCircle setCategoryBits(short categoryBits){super.setCategoryBits(categoryBits);return this;}	
	@Override
	public B2FlxCircle setGroupIndex(short groupIndex){super.setGroupIndex(groupIndex);return this;}	
	@Override
	public B2FlxCircle setSensor(boolean sensor){super.setSensor(sensor);return this;}
	@Override
	public B2FlxCircle setResetAngle(boolean resetAngle){super.setResetAngle(resetAngle);return this;}	
	@Override
	public B2FlxCircle setDraggable(boolean draggable){super.setDraggable(draggable);return this;}
	@Override
	public B2FlxCircle setSurvive(boolean survive){super.setSurvive(survive);return this;}
}
