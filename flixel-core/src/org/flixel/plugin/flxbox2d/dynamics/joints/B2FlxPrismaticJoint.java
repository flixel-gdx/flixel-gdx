package org.flixel.plugin.flxbox2d.dynamics.joints;

import org.flixel.plugin.flxbox2d.B2FlxB;
import org.flixel.plugin.flxbox2d.collision.shapes.B2FlxShape;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;

/**
 * A prismatic joint. This joint provides one degree of freedom: translation along an 
 * axis fixed in bodyA. Relative rotation is prevented. You can use a joint limit to 
 * restrict the range of motion and a joint motor to drive the motion or to model joint 
 * friction. 
 * 
 * @author Ka Wing Chin
 */
public class B2FlxPrismaticJoint extends B2FlxJoint
{
	/**
	 * The axis.
	 */
	private Vector2 _axis;

	/**
	 * Creates a prismatic joint.
	 * @param spriteA	The first body.
	 * @param spriteB	The second body.
	 * @param jointDef	The joint definition.
	 */
	public B2FlxPrismaticJoint(B2FlxShape spriteA, B2FlxShape spriteB, PrismaticJointDef jointDef)
	{
		super(spriteA, spriteB, jointDef);
	}
	
	/**
	 * Creates a prismatic joint.
	 * @param spriteA	The first body.
	 * @param spriteB	The second body.
	 */
	public B2FlxPrismaticJoint(B2FlxShape spriteA, B2FlxShape spriteB)
	{
		super(spriteA, spriteB, null);
	}
	
	/**
	 * Creates a prismatic joint.
	 * @param spriteA	The first body.
	 */
	public B2FlxPrismaticJoint(B2FlxShape spriteA)
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
			jointDef = new PrismaticJointDef();		
	}
	
	/**
	 * Set the default. The anchorA and anchorB get the world center of the body.
	 */
	@Override
	protected void setDefaults()
	{
		if(bodyA == null)
			bodyA = B2FlxB.getGroundBody(bodyB.getPosition());
		if(bodyB == null)
			bodyB = B2FlxB.getGroundBody(bodyA.getPosition());
		anchorA = bodyA.getWorldCenter();
		_axis = new Vector2(0,1);
	}

	/**
	 * Creates the joint.
	 */
	@Override
	public B2FlxPrismaticJoint create()
	{
		((PrismaticJointDef) jointDef).initialize(bodyA, bodyB, anchorA, _axis);
		joint = B2FlxB.world.createJoint(jointDef);
		return this;
	}
	
	@Override
	public void destroy()
	{
		super.destroy();
		_axis = null;
	}
	
	/**
	 * Set the axis.
	 * @param axis
	 * @return	This joint. Handy for chaining stuff together.
	 */
	public B2FlxPrismaticJoint setAxis(Vector2 axis)
	{
		_axis = axis;
		return this;
	}
	
	public B2FlxPrismaticJoint setEnableLimit(boolean enableLimit)
	{
		((PrismaticJointDef)jointDef).enableLimit = enableLimit;
		return this;
	}
	
	public B2FlxPrismaticJoint setEnableMotor(boolean enableMotor)
	{
		((PrismaticJointDef)jointDef).enableMotor = enableMotor;
		return this;
	}
	
	public B2FlxPrismaticJoint setMotorSpeed(float motorSpeed)
	{
		((PrismaticJointDef)jointDef).motorSpeed = motorSpeed;
		return this;
	}
	
	public B2FlxPrismaticJoint setMaxMotorForce(float maxMotorForce)
	{
		((PrismaticJointDef)jointDef).maxMotorForce = maxMotorForce;
		return this;
	}
	
	public B2FlxPrismaticJoint setUpperTranslation(float upperTranslation)
	{
		((PrismaticJointDef)jointDef).upperTranslation = upperTranslation;
		return this;
	}
	
	public B2FlxPrismaticJoint setLowerTranslation(float lowerTranslation)
	{
		((PrismaticJointDef)jointDef).lowerTranslation = lowerTranslation;
		return this;
	}
	
	@Override
	public PrismaticJoint getJoint(){return (PrismaticJoint)joint;}
	@Override
	public B2FlxPrismaticJoint setJointDef(JointDef jointDef){super.setJointDef(jointDef);return this;}	
	@Override
	public B2FlxPrismaticJoint setBodyA(Body bodyA){super.setBodyA(bodyA);return this;}	
	@Override
	public B2FlxPrismaticJoint setBodyB(Body bodyB){super.setBodyB(bodyB);return this;}	
	@Override
	public B2FlxPrismaticJoint setAnchorA(Vector2 anchorA){super.setAnchorA(anchorA);return this;}	
	@Override
	public B2FlxPrismaticJoint setAnchorB(Vector2 anchorB){super.setAnchorB(anchorB);return this;}	
	@Override
	public B2FlxPrismaticJoint setCollideConnected(boolean collideConnected){super.setCollideConnected(collideConnected);return this;}
	@Override
	public B2FlxPrismaticJoint setShowLine(boolean showLine){super.setShowLine(showLine);return this;}	
	@Override
	public B2FlxPrismaticJoint setLineThickness(float lineThickness){super.setLineThickness(lineThickness);return this;}	
	@Override
	public B2FlxPrismaticJoint setLineColor(int lineColor){super.setLineColor(lineColor);return this;}	
	@Override
	public B2FlxPrismaticJoint setLineAlpha(float lineAlpha){super.setLineAlpha(lineAlpha);return this;}
}
