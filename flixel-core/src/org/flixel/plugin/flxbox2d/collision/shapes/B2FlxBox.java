package org.flixel.plugin.flxbox2d.collision.shapes;

import org.flixel.FlxPoint;
import org.flixel.plugin.flxbox2d.B2FlxB;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * A box shape.
 * 
 * @author Ka Wing Chin
 */
public class B2FlxBox extends B2FlxShape
{
	/**
	 * Holds the center of an oriented box.
	 */
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
		this.center = new FlxPoint(width*.5f, height*.5f);
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
			((PolygonShape)shape).setAsBox(width * .5f / B2FlxB.RATIO, height * .5f / B2FlxB.RATIO, _center, bodyDef.angle);
		else
			((PolygonShape)shape).setAsBox(width * .5f / B2FlxB.RATIO, height * .5f / B2FlxB.RATIO);
	}

	/**
	 * Creates the body.
	 * @return	This object. Handy for chaining stuff together.
	 */
	@Override
	protected void createBody()
	{
		bodyDef.position.x = (x + width * .5f) / B2FlxB.RATIO;
		bodyDef.position.y = (y + height * .5f) / B2FlxB.RATIO;
		position = bodyDef.position;
		body = B2FlxB.world.createBody(bodyDef);
		fixture = body.createFixture(fixtureDef);
		shape.dispose();
		shape = null;
	}
	
	@Override
	public B2FlxBox create()
	{
		super.create();
		return this;
	}
	
	@Override
	public void revive()
	{
		x = x + width * .5f;
		y = y + height * .5f;
		super.revive();
	}
	
	@Override
	public void destroy()
	{
		super.destroy();
		_center = null;
	}
	
	@Override
	public B2FlxBox setType(BodyType type){super.setType(type);return this;}	
	@Override
	public B2FlxBox setFixtureDef(FixtureDef fixtureDef){super.setFixtureDef(fixtureDef);return this;}	
	@Override
	public B2FlxBox setLinearDamping(float linearDamping){super.setLinearDamping(linearDamping);return this;}
	@Override
	public B2FlxBox setLinearVelocity(Vector2 linearVelocity){super.setLinearVelocity(linearVelocity);return this;}	
	@Override
	public B2FlxBox setLinearVelocity(float x, float y){super.setLinearVelocity(x, y);return this;}	
	@Override
	public B2FlxBox setAngularDamping(float angularDamping){super.setAngularDamping(angularDamping);return this;}	
	@Override
	public B2FlxBox setAngularVelocity(float angularVelocity){super.setAngularVelocity(angularVelocity);return this;}	
	@Override
	public B2FlxBox setBullet(boolean bullet){super.setBullet(bullet);return this;}	
	@Override
	public B2FlxBox setFixedRotation(boolean fixedRotation){super.setFixedRotation(fixedRotation);return this;}	
	@Override
	public B2FlxBox setAllowSleep(boolean allowSleep){super.setAllowSleep(allowSleep);return this;}	
	@Override
	public B2FlxBox setActive(boolean active){super.setActive(active);return this;}	
	@Override
	public B2FlxBox setAwake(boolean awake){super.setAwake(awake);return this;}	
	@Override
	public B2FlxBox setDensity(float density){super.setDensity(density);return this;}	
	@Override
	public B2FlxBox setFriction(float friction){super.setFriction(friction);return this;}	
	@Override
	public B2FlxBox setRestitution(float restitution){super.setRestitution(restitution);return this;}	
	@Override
	public B2FlxBox setPosition(Vector2 position){super.setPosition(position);return this;}	
	@Override
	public B2FlxBox setAngle(float angle){super.setAngle(angle);return this;}	
	@Override
	public B2FlxBox setGravityScale(float gravityScale){super.setGravityScale(gravityScale);return this;}	
	@Override
	public B2FlxBox setMaskBits(short maskBits){super.setMaskBits(maskBits);return this;}	
	@Override
	public B2FlxBox setCategoryBits(short categoryBits){super.setCategoryBits(categoryBits);return this;}	
	@Override
	public B2FlxBox setGroupIndex(short groupIndex){super.setGroupIndex(groupIndex);return this;}	
	@Override
	public B2FlxBox setSensor(boolean sensor){super.setSensor(sensor);return this;}
	@Override
	public B2FlxBox setResetAngle(boolean resetAngle){super.setResetAngle(resetAngle);return this;}	
	@Override
	public B2FlxBox setDraggable(boolean draggable){super.setDraggable(draggable);return this;}
}
