package org.flixel.plugin.flxbox2d.dynamics.joints;

import org.flixel.plugin.flxbox2d.B2FlxB;
import org.flixel.plugin.flxbox2d.collision.shapes.B2FlxShape;

import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
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
	public B2FlxWeldJoint(B2FlxShape spriteA, B2FlxShape spriteB, WeldJointDef jointDef)
	{
		super(spriteA, spriteB, jointDef);
	}
	
	/**
	 * Creates a weld joint.
	 * @param spriteA
	 * @param spriteB
	 */
	public B2FlxWeldJoint(B2FlxShape spriteA, B2FlxShape spriteB)
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
	
	@Override
	protected void setDefaults()
	{
		anchorA = bodyA.getWorldCenter();
		super.setDefaults();
	}
	
	/**
	 * Creates the joint.
	 * @return	This joint. Handy for chaining stuff together.
	 */
	@Override
	public B2FlxWeldJoint create()
	{
		((WeldJointDef)jointDef).initialize(bodyA, bodyB, anchorA);
		joint = B2FlxB.world.createJoint(jointDef);
		return this;
	}

	/**
	 * Get the weld joint.
	 * @return
	 */
	public WeldJoint getJoint()
	{
		return (WeldJoint)joint;
	}
}
