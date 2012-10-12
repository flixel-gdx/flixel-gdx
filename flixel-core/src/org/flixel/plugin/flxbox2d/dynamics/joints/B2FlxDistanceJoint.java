package org.flixel.plugin.flxbox2d.dynamics.joints;

import org.flixel.plugin.flxbox2d.B2FlxB;
import org.flixel.plugin.flxbox2d.collision.shapes.B2FlxSprite;

import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;

/**
 * A distance joint constrains two points on two bodies to remain at a fixed 
 * distance from each other. You can view this as a massless, rigid rod.
 * 
 * @author Ka Wing Chin
 */
public class B2FlxDistanceJoint extends B2FlxJoint
{
		
	/**
	 * Creates a distance joint.
	 * @param spriteA	The first body.
	 * @param spriteB	The second body.
	 * @param jointDef	The joint definition.
	 */
	public B2FlxDistanceJoint(B2FlxSprite spriteA, B2FlxSprite spriteB, DistanceJointDef jointDef)
	{
		super(spriteA, spriteB, jointDef);
	}
	
	/**
	 * Creates a distance joint.
	 * @param spriteA	The first body.
	 * @param spriteB	The second body.
	 */
	public B2FlxDistanceJoint(B2FlxSprite spriteA, B2FlxSprite spriteB)
	{
		this(spriteA, spriteB, null);
	}
	
	/**
	 * Creates the jointDef.
	 */
	@Override
	protected void setupJointDef()
	{		
		if(jointDef == null)
			jointDef = new DistanceJointDef();
	}
	
	/**
	 * Set the defaults. The anchorA and anchorB get the world center of the body.
	 */
	@Override
	protected void setDefaults()
	{
		anchorA = bodyA.getWorldCenter();
		anchorB = bodyB.getWorldCenter();
	}

	/**
	 * Creates the joint.
	 * @return	This joint. Handy for chaining stuff together.
	 */
	@Override
	public B2FlxDistanceJoint create()
	{
		((DistanceJointDef) jointDef).initialize(bodyA, bodyB, anchorA, anchorB);
		joint = B2FlxB.world.createJoint(jointDef);
		return this;
	}
	
	/**
	 * Set the frequencyHz.
	 * @param frequencyHz
	 * @return This joint. Handy for chaining stuff together.
	 */
	public B2FlxDistanceJoint setFrequencyHz(float frequencyHz)
	{
		((DistanceJointDef) jointDef).frequencyHz = frequencyHz;
		return this;
	}
	
	/**
	 * Set the damping ratio.
	 * @param dampingRatio
	 * @return This joint. Handy for chaining stuff together.
	 */
	public B2FlxDistanceJoint setDampingRatio(float dampingRatio)
	{
		((DistanceJointDef) jointDef).dampingRatio = dampingRatio;
		return this;
	}
}
