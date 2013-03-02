package org.flixel.plugin.flxbox2d.collision.shapes;

import org.flixel.plugin.flxbox2d.B2FlxB;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * This class doesn't have any shape. It's a shapeless body which is used 
 * to combine multiple shapes together.
 * 
 * @author Ka Wing Chin
 */
public class B2FlxSprite extends B2FlxShape
{
	/**
	 * It creates an shapeless body.
	 * @param x		The X-coordinate of the point in space.
	 * @param y 	The Y-coordinate of the point in space.
	 */
	public B2FlxSprite(float x, float y)
	{
		super(x, y);
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
	
	@Override
	public void setDefaults()
	{
		// Nothing
	}

	@Override
	public void createShape()
	{
		// Shapeless
	}
	
	/**
	 * Create a body without any fixtures.
	 */
	@Override
	protected void createBody()
	{
		bodyDef.position.x = x / RATIO;
		bodyDef.position.y = y / RATIO;
		position = bodyDef.position;
		body = B2FlxB.world.createBody(bodyDef);
		setUserData(userData);
	}
	
	@Override
	public B2FlxSprite create()
	{
		super.create();
		return this;
	}
	
	@Override
	public B2FlxSprite setType(BodyType type){super.setType(type);return this;}	
	@Override
	public B2FlxSprite setFixtureDef(FixtureDef fixtureDef){super.setFixtureDef(fixtureDef);return this;}	
	@Override
	public B2FlxSprite setLinearDamping(float linearDamping){super.setLinearDamping(linearDamping);return this;}
	@Override
	public B2FlxSprite setLinearVelocity(Vector2 linearVelocity){super.setLinearVelocity(linearVelocity);return this;}	
	@Override
	public B2FlxSprite setLinearVelocity(float x, float y){super.setLinearVelocity(x, y);return this;}	
	@Override
	public B2FlxSprite setAngularDamping(float angularDamping){super.setAngularDamping(angularDamping);return this;}	
	@Override
	public B2FlxSprite setAngularVelocity(float angularVelocity){super.setAngularVelocity(angularVelocity);return this;}	
	@Override
	public B2FlxSprite setBullet(boolean bullet){super.setBullet(bullet);return this;}	
	@Override
	public B2FlxSprite setFixedRotation(boolean fixedRotation){super.setFixedRotation(fixedRotation);return this;}	
	@Override
	public B2FlxSprite setAllowSleep(boolean allowSleep){super.setAllowSleep(allowSleep);return this;}	
	@Override
	public B2FlxSprite setActive(boolean active){super.setActive(active);return this;}	
	@Override
	public B2FlxSprite setAwake(boolean awake){super.setAwake(awake);return this;}	
	@Override
	public B2FlxSprite setDensity(float density){super.setDensity(density);return this;}	
	@Override
	public B2FlxSprite setFriction(float friction){super.setFriction(friction);return this;}	
	@Override
	public B2FlxSprite setRestitution(float restitution){super.setRestitution(restitution);return this;}	
	@Override
	public B2FlxSprite setPosition(Vector2 position){super.setPosition(position);return this;}	
	@Override
	public B2FlxSprite setAngle(float angle){super.setAngle(angle);return this;}	
	@Override
	public B2FlxSprite setGravityScale(float gravityScale){super.setGravityScale(gravityScale);return this;}	
	@Override
	public B2FlxSprite setMaskBits(short maskBits){super.setMaskBits(maskBits);return this;}	
	@Override
	public B2FlxSprite setCategoryBits(short categoryBits){super.setCategoryBits(categoryBits);return this;}	
	@Override
	public B2FlxSprite setGroupIndex(short groupIndex){super.setGroupIndex(groupIndex);return this;}	
	@Override
	public B2FlxSprite setSensor(boolean sensor){super.setSensor(sensor);return this;}
	@Override
	public B2FlxSprite setReportBeginContact(boolean reportBeginContact){super.setReportBeginContact(reportBeginContact);return this;}
	@Override
	public B2FlxSprite setReportEndContact(boolean reportEndContact){super.setReportEndContact(reportEndContact);return this;}
	@Override
	public B2FlxSprite setReportPreSolve(boolean reportPreSolve){super.setReportPreSolve(reportPreSolve);return this;}
	@Override
	public B2FlxSprite setReportPostSolve(boolean reportPostSolve){super.setReportPostSolve(reportPostSolve);return this;}
	@Override
	public B2FlxSprite setResetAngle(boolean resetAngle){super.setResetAngle(resetAngle);return this;}	
	@Override
	public B2FlxSprite setDraggable(boolean draggable){super.setDraggable(draggable);return this;}
}

