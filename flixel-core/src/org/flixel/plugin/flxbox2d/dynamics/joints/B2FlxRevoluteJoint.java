package org.flixel.plugin.flxbox2d.dynamics.joints;

import org.flixel.plugin.flxbox2d.B2FlxB;
import org.flixel.plugin.flxbox2d.collision.shapes.B2FlxSprite;
import org.flixel.plugin.flxbox2d.common.math.B2FlxMath;

import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

/**
 * A revolute joint constrains two bodies to share a common point while they 
 * are free to rotate about the point. The relative rotation about the shared 
 * point is the joint angle. You can limit the relative rotation with a joint 
 * limit that specifies a lower and upper angle. You can use a motor to drive 
 * the relative rotation about the shared point. A maximum motor torque is 
 * provided so that infinite forces are not generated.
 *  
 * @author Ka Wing Chin
 */
public class B2FlxRevoluteJoint extends B2FlxJoint
{
	
	/**
	 * Creates a revolute joint.
	 * @param spriteA	The first body.
	 * @param spriteB	The second body.
	 * @param jointDef	The joint definition.
	 */
	public B2FlxRevoluteJoint(B2FlxSprite spriteA, B2FlxSprite spriteB, RevoluteJointDef jointDef)
	{
		super(spriteA, spriteB, jointDef);
	}
	
	/**
	 * Creates a revolute joint.
	 * @param spriteA	The first body.
	 * @param spriteB	The second body.
	 */
	public B2FlxRevoluteJoint(B2FlxSprite spriteA, B2FlxSprite spriteB)
	{
		super(spriteA, spriteB, null);
	}
	
	/**
	 * Creates a revolute joint.
	 * @param spriteA	The first body.
	 */
	public B2FlxRevoluteJoint(B2FlxSprite spriteA)
	{
		super(spriteA, null, null);
	}

	/**
	 * Creates the jointDef.
	 */
	@Override
	protected void setupJointDef()
	{
		if(jointDef == null)
			jointDef = new RevoluteJointDef();
	}
	
	/**
	 * Set the defaults. If the bodies are null, a static body will be created.
	 */
	@Override
	protected void setDefaults()
	{
		if(bodyA == null)
			bodyA = B2FlxB.getGroundBody(bodyB.getPosition());
		if(bodyB == null)
			bodyB = B2FlxB.getGroundBody(bodyA.getPosition());
		anchorA = bodyA.getWorldCenter();
	}

	/**
	 * Creates the joint.
	 * @return	This joint. Handy for chaining stuff together.
	 */
	@Override
	public B2FlxRevoluteJoint create()
	{
		((RevoluteJointDef)jointDef).initialize(bodyA, bodyB, anchorA);
		joint = B2FlxB.world.createJoint(jointDef);
		return this;
	}
	
	public B2FlxRevoluteJoint setEnableLimit(boolean enableLimit)
	{
		((RevoluteJointDef)jointDef).enableLimit = enableLimit;
		return this;
	}
	
	public B2FlxRevoluteJoint setEnableMotor(boolean enableMotor)
	{
		((RevoluteJointDef)jointDef).enableMotor = enableMotor;
		return this;
	}
	
	public B2FlxRevoluteJoint setMotorSpeed(float motorSpeed)
	{
		((RevoluteJointDef)jointDef).motorSpeed = motorSpeed;
		return this;
	}
	
	public B2FlxRevoluteJoint setMaxMotorTorque(float maxMotorTorque)
	{
		((RevoluteJointDef)jointDef).maxMotorTorque = maxMotorTorque;
		return this;
	}
	
	public B2FlxRevoluteJoint setUpperAngle(float upperAngle)
	{
		((RevoluteJointDef)jointDef).upperAngle = upperAngle * B2FlxMath.DEGRAD;
		return this;
	}
	
	public B2FlxRevoluteJoint setLowerAngle(float lowerAngle)
	{
		((RevoluteJointDef)jointDef).lowerAngle = lowerAngle * B2FlxMath.DEGRAD;
		return this;
	}
}
