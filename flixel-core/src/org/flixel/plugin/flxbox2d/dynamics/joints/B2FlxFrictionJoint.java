package org.flixel.plugin.flxbox2d.dynamics.joints;

import org.flixel.plugin.flxbox2d.B2FlxB;
import org.flixel.plugin.flxbox2d.collision.shapes.B2FlxShape;

import com.badlogic.gdx.physics.box2d.joints.FrictionJointDef;

/**
 * Friction joint. This is used for top-down friction. It provides 2D translational 
 * friction and angular friction.
 *  
 * @author Ka Wing Chin
 */
public class B2FlxFrictionJoint extends B2FlxJoint
{
	/**
	 * Creates a friction joint.
	 * @param spriteA	The first body.
	 * @param spriteB	The second body.
	 * @param jointDef	The joint definition.
	 */
	public B2FlxFrictionJoint(B2FlxShape spriteA, B2FlxShape spriteB, FrictionJointDef jointDef)
	{
		super(spriteA, spriteB, jointDef);
	}
	
	/**
	 * Creates a friction joint.
	 * @param spriteA	The first body.
	 * @param spriteB	The second body.
	 */
	public B2FlxFrictionJoint(B2FlxShape spriteA, B2FlxShape spriteB)
	{
		super(spriteA, spriteB, null);
	}
	
	/**
	 * Creates a friction joint.
	 * @param spriteA	The first body.
	 */
	public B2FlxFrictionJoint(B2FlxShape spriteA)
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
			jointDef = new FrictionJointDef();
	}
	
	/**
	 * Set the default. If the bodies are null, a static body will be created.
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
	 * @return This joint. Handy for chaining stuff together.
	 */
	@Override
	public B2FlxJoint create()
	{
		((FrictionJointDef)jointDef).initialize(bodyA, bodyB, anchorA);
		joint = B2FlxB.world.createJoint(jointDef);
		return this;
	}
	
	/**
	 * Set the max force.
	 * @param maxForce
	 * @return	This joint. Handy for chaining stuff together.
	 */
	public B2FlxFrictionJoint setMaxForce(float maxForce)
	{
		((FrictionJointDef)jointDef).maxForce = maxForce;
		return this;
	}
	
	/**
	 * Set the max torque.
	 * @param maxTorque
	 * @return	This joint. Handy for chaining stuff together.
	 */
	public B2FlxFrictionJoint setMaxTorque(float maxTorque)
	{
		((FrictionJointDef)jointDef).maxTorque = maxTorque;
		return this;
	}
}
