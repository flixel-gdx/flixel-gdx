package org.flixel.plugin.flxbox2d.collision.shapes;

import org.flixel.plugin.flxbox2d.B2FlxB;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Transform;

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
		shape.setRadius(_radius/RATIO);
		// The shape radius is gone when the shape got disposed.
		_shapeRadius = shape.getRadius();
		fixtureDef.shape = shape;
	}
	
	/**
	 * Creates the body.
	 * @return	This object. Handy for chaining stuff together.
	 */
	@Override
	protected void createBody()
	{	
		bodyDef.position.x = (x + _radius) / RATIO;
		bodyDef.position.y = (y + _radius) / RATIO;
		position = bodyDef.position;
		body = B2FlxB.world.createBody(bodyDef);
		fixture = body.createFixture(fixtureDef);
		shape.dispose();
		shape = null;
	}
	
	@Override
	protected void drawShape(Fixture fixture, Transform transform, int color)
	{
		CircleShape circle = (CircleShape) fixture.getShape();
		t.set(circle.getPosition());
		transform.mul(t);
		drawSolidCircle(t, circle.getRadius(), axis.set(transform.vals[Transform.COS], transform.vals[Transform.SIN]), color);
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
		((CircleShape)fixtureDef.shape).setPosition(new Vector2(x/RATIO, y/RATIO));
	}

}
