package org.flixel.plugin.flxbox2d.dynamics.joints;

import org.flixel.FlxCamera;
import org.flixel.FlxG;
import org.flixel.plugin.flxbox2d.B2FlxB;
import org.flixel.plugin.flxbox2d.collision.shapes.B2FlxShape;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.PulleyJoint;
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
	private Vector2 _groundAnchorA;
	// The second ground anchor in world coordinates. This point never moves.
	private Vector2 _groundAnchorB;
	// The pulley ratio, used to simulate a block-and-tackle.
	private float _ratio;

	/**
	 * Creates a pulley joint.
	 * @param spriteA	The first body.
	 * @param spriteB	The second body.
	 * @param jointDef	The joint definition.
	 */
	public B2FlxPulleyJoint(B2FlxShape spriteA, B2FlxShape spriteB, PulleyJointDef jointDef)
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
	public B2FlxPulleyJoint(B2FlxShape spriteA, B2FlxShape spriteB)
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
	public B2FlxPulleyJoint create()
	{
		((PulleyJointDef) jointDef).initialize(bodyA, bodyB, _groundAnchorA, _groundAnchorB, anchorA, anchorB, _ratio);
		joint = B2FlxB.world.createJoint(jointDef);
		return this;
	}
	
	@Override
	public void destroy()
	{
		super.destroy();
		_groundAnchorA = null;
		_groundAnchorB = null;
	}
	
	@Override
	public boolean onScreen(FlxCamera camera)
	{
		if(joint == null)
			return false;
		if(camera == null)
			camera = FlxG.camera;
		
		p1.set(joint.getAnchorA().mul(B2FlxB.RATIO));
		p2.set(joint.getAnchorB().mul(B2FlxB.RATIO));
		
		p1.x -= camera.scroll.x * scrollFactor.x;
		p1.y -= camera.scroll.y * scrollFactor.y;
		p2.x -= camera.scroll.x * scrollFactor.x;
		p2.y -= camera.scroll.y * scrollFactor.y;
		
		x1.set(((PulleyJoint)joint).getGroundAnchorA().mul(B2FlxB.RATIO));
		x2.set(((PulleyJoint)joint).getGroundAnchorB().mul(B2FlxB.RATIO));
		x1.x -= (int)camera.scroll.x * scrollFactor.x;
		x1.y -= (int)camera.scroll.y * scrollFactor.y;
		x2.x -= (int)camera.scroll.x * scrollFactor.x;
		x2.y -= (int)camera.scroll.y * scrollFactor.y;
		
		boolean onScreenX = false;
		boolean onScreenY = false;
		if((p1.x <= p2.x) && (p2.x >= 0 && p2.x <= camera.width) || p2.x >= camera.width && p1.x <= camera.width)
			onScreenX = true;
		else if((p1.x >= p2.x) && (p1.x >= 0 && p1.x <= camera.width) || p1.x >= camera.width && p2.x <= camera.width)
			onScreenX = true;
		if((p1.y <= p2.y) && (p2.y >= 0 && p2.y <= camera.height) || p2.y >= camera.height && p1.y <= camera.height)
			onScreenY = true;
		else if((p1.y >= p2.y) && (p1.y >= 0 && p1.y <= camera.height) || p1.y >= camera.height && p2.y <= camera.height)
			onScreenY = true;
		if((x1.y <= x2.y) && (x2.y >= 0 && x2.y <= camera.height) || x2.y >= camera.height && x1.y <= camera.height)
			onScreenY = true;
		else if((x1.y >= x2.y) && (x1.y >= 0 && x1.y <= camera.height) || x1.y >= camera.height && x2.y <= camera.height)
			onScreenY = true;
		return (onScreenX && onScreenY);
	}
	
	/**
	 * Draw joint.
	 */
	@Override
	protected void drawJoint(FlxCamera camera, float lineThickness, int lineColor, float lineAlpha)
	{
		ShapeRenderer segment = FlxG.flashGfx.getShapeRenderer();
		FlxG.flashGfx.lineStyle(lineThickness, lineColor, lineAlpha);
		segment.line(x1.x, x1.y, p1.x, p1.y);
		segment.line(x2.x, x2.y, p2.x, p2.y);
		segment.line(x1.x, x1.y, x2.x, x2.y);
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
	
	@Override
	public PulleyJoint getJoint(){return (PulleyJoint)joint;}
	@Override
	public B2FlxPulleyJoint setJointDef(JointDef jointDef){super.setJointDef(jointDef);return this;}	
	@Override
	public B2FlxPulleyJoint setBodyA(Body bodyA){super.setBodyA(bodyA);return this;}	
	@Override
	public B2FlxPulleyJoint setBodyB(Body bodyB){super.setBodyB(bodyB);return this;}	
	@Override
	public B2FlxPulleyJoint setAnchorA(Vector2 anchorA){super.setAnchorA(anchorA);return this;}	
	@Override
	public B2FlxPulleyJoint setAnchorB(Vector2 anchorB){super.setAnchorB(anchorB);return this;}	
	@Override
	public B2FlxPulleyJoint setCollideConnected(boolean collideConnected){super.setCollideConnected(collideConnected);return this;}
	@Override
	public B2FlxPulleyJoint setShowLine(boolean showLine){super.setShowLine(showLine);return this;}	
	@Override
	public B2FlxPulleyJoint setLineThickness(float lineThickness){super.setLineThickness(lineThickness);return this;}	
	@Override
	public B2FlxPulleyJoint setLineColor(int lineColor){super.setLineColor(lineColor);return this;}	
	@Override
	public B2FlxPulleyJoint setLineAlpha(float lineAlpha){super.setLineAlpha(lineAlpha);return this;}
}

