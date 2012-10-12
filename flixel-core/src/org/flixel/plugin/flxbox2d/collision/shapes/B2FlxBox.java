package org.flixel.plugin.flxbox2d.collision.shapes;

import org.flixel.plugin.flxbox2d.B2FlxB;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * A box shape.
 * 
 * @author Ka Wing Chin
 */
public class B2FlxBox extends B2FlxSprite
{
	// Holds the center of an oriented box.
	private Vector2 _center;
	
	/**
	 * If the center is filled, it will be a oriented box, otherwise 
	 * it will be an axis-aligned box.
	 * @param x			The X-coordinate of the point in space.
	 * @param y			The Y-coordinate of the point in space.
	 * @param width		The width of the box.
	 * @param height	The height of the box.
	 * @param center	The center of the box.
	 * @param angle		The rotation of the box (degrees).
	 */
	public B2FlxBox(float x, float y, float width, float height, Vector2 center, float angle)
	{
		super(x, y);
		if(width > 0)
			this.width = width;
		if(height > 0)
			this.height = height;
		_center = center;
		setAngle(angle);
		createShape();
	}
	
	/**
	 * If the center is filled, it will be a oriented box, otherwise 
	 * it will be an axis-aligned box.
	 * @param x			The X-coordinate of the point in space.
	 * @param y			The Y-coordinate of the point in space.
	 * @param width		The width of the box.
	 * @param height	The height of the box.
	 * @param center	The center of the box.
	 */
	public B2FlxBox(float x, float y, float width, float height, Vector2 center)
	{
		this(x, y, width, height, center, 0);
	}
	
	/**
	 * This creates an axis-aligned box.
	 * @param x			The X-coordinate of the point in space.
	 * @param y			The Y-coordinate of the point in space.
	 * @param width		The width of the box.
	 * @param height	The height of the box.
	 */
	public B2FlxBox(float x, float y, float width, float height)
	{
		this(x, y, width, height, null, 0);
	}
	
	/**
	 * This creates an axis-aligned box.
	 * @param x			The X-coordinate of the point in space.
	 * @param y			The Y-coordinate of the point in space.
	 * @param width		The width of the box.
	 */
	public B2FlxBox(float x, float y, float width)
	{
		this(x, y, width, 0, null, 0);
	}
	
	/**
	 * This creates an axis-aligned box.
	 * @param x			The X-coordinate of the point in space.
	 * @param y			The Y-coordinate of the point in space.
	 */
	public B2FlxBox(float x, float y)
	{
		this(x, y, 0, 0, null, 0);
	}
	
	/**
	 * This creates an axis-aligned box.
	 * @param x			The X-coordinate of the point in space.
	 */
	public B2FlxBox(float x)
	{
		this(x, 0, 0, 0, null, 0);
	}
	
	/**
	 * This creates an axis-aligned box.
	 */
	public B2FlxBox()
	{
		this(0, 0, 0, 0, null, 0);
	}
	
	/**
	 * Set the defaults for width and height.
	 */
	@Override
	public void setDefaults()
	{
		width = height = 16;
	}
	
	/**
	 * Creates the shape. If the center is not null, it will be an oriented box.
	 */
	@Override
	public void createShape()
	{
		shape = new PolygonShape();
		fixtureDef.shape = shape;
		if(_center != null)
			((PolygonShape)shape).setAsBox(width * .5f / RATIO, height * .5f / RATIO, _center, bodyDef.angle);
		else
			((PolygonShape)shape).setAsBox(width * .5f / RATIO, height * .5f / RATIO);
	}

	/**
	 * Creates the body.
	 * @return	This object. Handy for chaining stuff together.
	 */
	@Override
	public B2FlxBox create()
	{		
		bodyDef.position.x = (x + width * .5f) / RATIO;
		bodyDef.position.y = (y + height * .5f) / RATIO;
		position = bodyDef.position;
		body = B2FlxB.world.createBody(bodyDef);
		fixture = body.createFixture(fixtureDef);
		shape.dispose();
		shape = null;
		return this;
	}
	
	@Override
	public void destroy()
	{
		super.destroy();
		_center = null;
	}
	
}
