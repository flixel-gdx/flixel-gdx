package org.flixel.plugin.flxbox2d.dynamics.joints;

import org.flixel.plugin.flxbox2d.B2FlxB;
import org.flixel.plugin.flxbox2d.collision.shapes.B2FlxShape;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;

/**
 * A wheel joint. This joint provides two degrees of freedom: translation 
 * along an axis fixed in bodyA and rotation in the plane. You can use a 
 * joint limit to restrict the range of motion and a joint motor to drive 
 * the rotation or to model rotational friction. This joint is designed 
 * for vehicle suspensions. 
 *
 * @author Ka Wing Chin
 */
public class B2FlxWheelJoint extends B2FlxJoint 
{
	
	/**
	 * The axis.
	 */
	private Vector2 _axis;

	/**
	 * Create a wheel joint.
	 * @param spriteA	The first body.
	 * @param spriteB	The second body.
	 * @param jointDef	The joint definition.
	 */
	public B2FlxWheelJoint(B2FlxShape spriteA, B2FlxShape spriteB, WheelJointDef jointDef)
	{
		super(spriteA, spriteB, jointDef);
	}
	
	/**
	 * Create a wheel joint.
	 * @param spriteA	The first body.
	 * @param spriteB	The second body.
	 */
	public B2FlxWheelJoint(B2FlxShape spriteA, B2FlxShape spriteB)
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
			jointDef = new WheelJointDef();
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
		anchorB = bodyB.getWorldCenter();
		_axis = new Vector2(1, 0);
	}

	/**
	 * Creates the joint.
	 * @return	This joint. Handy for chaining stuff together.
	 */
	@Override
	public B2FlxWheelJoint create()
	{
		((WheelJointDef) jointDef).initialize(bodyA, bodyB, anchorA, _axis);
		joint = B2FlxB.world.createJoint(jointDef);
		return this;
	}
	
	@Override
	public void destroy()
	{		
		super.destroy();
		_axis = null;
	}
	
	public B2FlxWheelJoint setAxis(Vector2 axis)
	{
		_axis = axis;
		return this;
	}
	
	public B2FlxWheelJoint setEnableMotor(boolean enableMotor)
	{
		((WheelJointDef)jointDef).enableMotor = enableMotor;
		return this;
	}
	
	public B2FlxWheelJoint setMotorSpeed(float motorSpeed)
	{
		((WheelJointDef)jointDef).motorSpeed = motorSpeed;
		return this;
	}
	
	public B2FlxWheelJoint setMaxMotorTorque(float maxMotorTorque)
	{
		((WheelJointDef)jointDef).maxMotorTorque = maxMotorTorque;
		return this;
	}
	
	public B2FlxWheelJoint setFrequencyHz(float frequencyHz)
	{
		((WheelJointDef)jointDef).frequencyHz = frequencyHz;
		return this;
	}
	
	public B2FlxWheelJoint setDampingRatio(float dampingRatio)
	{
		((WheelJointDef)jointDef).dampingRatio = dampingRatio;
		return this;
	}
	
	/**
	 * Get the wheel joint.
	 * @return
	 */
	public WheelJoint getJoint()
	{
		return (WheelJoint)joint;
	}
}

