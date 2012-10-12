package org.flixel.plugin.flxbox2d.dynamics.joints;

import org.flixel.plugin.flxbox2d.B2FlxB;
import org.flixel.plugin.flxbox2d.collision.shapes.B2FlxSprite;

import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

/**
 * A weld joint essentially glues two bodies together. A weld joint may 
 * distort somewhat because the island constraint solver is approximate. 
 * 
 * @author Ka Wing Chin
 */
public class B2FlxWeldJoint extends B2FlxJoint
{
	
	/**
	 * Creates a weld joint.
	 * @param spriteA
	 * @param spriteB
	 * @param jointDef
	 */
	public B2FlxWeldJoint(B2FlxSprite spriteA, B2FlxSprite spriteB, WeldJointDef jointDef)
	{
		super(spriteA, spriteB, jointDef);
	}
	
	/**
	 * Creates a weld joint.
	 * @param spriteA
	 * @param spriteB
	 */
	public B2FlxWeldJoint(B2FlxSprite spriteA, B2FlxSprite spriteB)
	{
		this(spriteA, spriteB, null);
	}
	
	/**
	 * Create the jointDef.
	 */
	@Override
	protected void setupJointDef()
	{
		if(jointDef == null)
			jointDef = new WeldJointDef();
	}
	
	/**
	 * Creates the joint.
	 * @return	This joint. Handy for chaining stuff together.
	 */
	@Override
	public B2FlxJoint create()
	{
		((WeldJointDef)jointDef).initialize(bodyA, bodyB, anchorA);
		joint = B2FlxB.world.createJoint(jointDef);
		return this;
	}

}
