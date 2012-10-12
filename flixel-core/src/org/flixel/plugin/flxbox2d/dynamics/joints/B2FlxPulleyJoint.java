package org.flixel.plugin.flxbox2d.dynamics.joints;

import org.flixel.plugin.flxbox2d.B2FlxB;
import org.flixel.plugin.flxbox2d.collision.shapes.B2FlxSprite;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.PulleyJointDef;

/**
 * The pulley joint is connected to two bodies and two fixed ground points. The pulley 
 * supports a ratio such that: length1 + ratio * length2 <= constant Yes, the force 
 * transmitted is scaled by the ratio. Warning: the pulley joint can get a bit squirrelly 
 * by itself. They often work better when combined with prismatic joints. You should also 
 * cover the the anchor points with static shapes to prevent one side from going to zero 
 * length. 
 *
 * @author Ka Wing Chin
 */
public class B2FlxPulleyJoint extends B2FlxJoint
{
	// The first ground anchor in world coordinates. This point never moves.
	public Vector2 _groundAnchorA;
	// The second ground anchor in world coordinates. This point never moves.
	public Vector2 _groundAnchorB;
	// The pulley ratio, used to simulate a block-and-tackle.
	public float _ratio;

	/**
	 * Creates a pulley joint.
	 * @param spriteA	The first body.
	 * @param spriteB	The second body.
	 * @param jointDef	The joint definition.
	 */
	public B2FlxPulleyJoint(B2FlxSprite spriteA, B2FlxSprite spriteB, PulleyJointDef jointDef)
	{
		super(spriteA, spriteB, jointDef);
		anchorA = bodyA.getWorldCenter();
		anchorB = bodyB.getWorldCenter();
	}

	/**
	 * Creates a pulley joint.
	 * @param spriteA	The first body.
	 * @param spriteB	The second body.
	 */
	public B2FlxPulleyJoint(B2FlxSprite spriteA, B2FlxSprite spriteB)
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
			jointDef = new PulleyJointDef();
	}
	
	/**
	 * Set the defaults.
	 */
	@Override
	protected void setDefaults()
	{
		_groundAnchorA = new Vector2(-1 ,1);
		_groundAnchorB = new Vector2(1 ,1);
		_ratio = 1;
	}

	/**
	 * Creates the joint.
	 * @return	This joint. Handy for chaining stuff together.
	 */
	@Override
	public B2FlxJoint create()
	{
		((PulleyJointDef) jointDef).initialize(bodyA, bodyB, _groundAnchorA, _groundAnchorB, anchorA, anchorB, _ratio);
		joint = B2FlxB.world.createJoint(jointDef);
		return this;
	}
	
	public B2FlxPulleyJoint setGroundAnchorA(Vector2 groundAnchorA)
	{
		_groundAnchorA = groundAnchorA;
		return this;
	}
	
	public B2FlxPulleyJoint setGroundAnchorB(Vector2 groundAnchorB)
	{
		_groundAnchorB = groundAnchorB;
		return this;
	}
	
	public B2FlxPulleyJoint setRatio(float ratio)
	{
		_ratio = ratio;
		return this;
	}

}

