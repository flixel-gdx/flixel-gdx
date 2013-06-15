package org.flixel.plugin.flxbox2d.dynamics.joints;

import org.flixel.FlxCamera;
import org.flixel.FlxG;
import org.flixel.plugin.flxbox2d.B2FlxB;
import org.flixel.plugin.flxbox2d.collision.shapes.B2FlxShape;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
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
	public B2FlxDistanceJoint(B2FlxShape spriteA, B2FlxShape spriteB, DistanceJointDef jointDef)
	{
		super(spriteA, spriteB, jointDef);
	}
	
	/**
	 * Creates a distance joint.
	 * @param spriteA	The first body.
	 * @param spriteB	The second body.
	 */
	public B2FlxDistanceJoint(B2FlxShape spriteA, B2FlxShape spriteB)
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
	 * Draw joint.
	 */
	@Override
	protected void drawJoint(FlxCamera camera, float lineThickness, int lineColor, float lineAlpha)
	{
		ShapeRenderer segment = FlxG.flashGfx.getShapeRenderer();
		FlxG.flashGfx.lineStyle(lineThickness, lineColor, lineAlpha);
		segment.line(p1.x, p1.y, p2.x, p2.y);
		
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
	
	@Override
	public DistanceJoint getJoint(){return (DistanceJoint) joint;}	
	@Override
	public B2FlxDistanceJoint setJointDef(JointDef jointDef){super.setJointDef(jointDef);return this;}	
	@Override
	public B2FlxDistanceJoint setBodyA(Body bodyA){super.setBodyA(bodyA);return this;}	
	@Override
	public B2FlxDistanceJoint setBodyB(Body bodyB){super.setBodyB(bodyB);return this;}	
	@Override
	public B2FlxDistanceJoint setAnchorA(Vector2 anchorA){super.setAnchorA(anchorA);return this;}	
	@Override
	public B2FlxDistanceJoint setAnchorB(Vector2 anchorB){super.setAnchorB(anchorB);return this;}	
	@Override
	public B2FlxDistanceJoint setCollideConnected(boolean collideConnected){super.setCollideConnected(collideConnected);return this;}
	@Override
	public B2FlxDistanceJoint setShowLine(boolean showLine){super.setShowLine(showLine);return this;}	
	@Override
	public B2FlxDistanceJoint setLineThickness(float lineThickness){super.setLineThickness(lineThickness);return this;}	
	@Override
	public B2FlxDistanceJoint setLineColor(int lineColor){super.setLineColor(lineColor);return this;}	
	@Override
	public B2FlxDistanceJoint setLineAlpha(float lineAlpha){super.setLineAlpha(lineAlpha);return this;}
	@Override
	public B2FlxDistanceJoint setSurvive(boolean survive){super.setSurvive(survive);return this;}
}
