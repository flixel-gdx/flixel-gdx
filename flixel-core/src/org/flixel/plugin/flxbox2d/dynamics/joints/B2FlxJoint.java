package org.flixel.plugin.flxbox2d.dynamics.joints;

import org.flixel.FlxBasic;
import org.flixel.FlxCamera;
import org.flixel.FlxG;
import org.flixel.FlxPoint;
import org.flixel.plugin.flxbox2d.B2FlxB;
import org.flixel.plugin.flxbox2d.collision.shapes.B2FlxSprite;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;


/**
 * An abstract class and it must not be initiate.
 * It contains the standard objects which all subclasses also use.
 * 
 * @author Ka Wing Chin
 */
public abstract class B2FlxJoint extends FlxBasic
{
	// The ratio of meters to pixel.
	protected static final float RATIO = B2FlxB.RATIO;
	
	// This stores the attached bodies and the type of the joint.
	public JointDef jointDef;
	// The base joint class. Joints are used to constraint two bodies together.
	public Joint joint;
	
	// First body.	
	public Body bodyA;
	// Second body.
	public Body bodyB;
	
	// The anchor point of the first body.
	public Vector2 anchorA;	
	// The anchor point of the second body.
	public Vector2 anchorB;	
	
	// Whether to draw a line or not.
	public boolean showLine;
	
	// The thickness of the line.
	public float lineThickness;
	// The color of the line.
	public int lineColor;
	// The opacity of the line.
	public float lineAlpha;
	
	/**
	 * A point that can store numbers from 0 to 1 (for X and Y independently)
	 * that governs how much this object is affected by the camera subsystem.
	 * 0 means it never moves, like a HUD element or far background graphic.
	 * 1 means it scrolls along a the same speed as the foreground layer.
	 * scrollFactor is initialized as (1,1) by default.
	 */
	public FlxPoint scrollFactor;
	
	/**
	 * Depending on the type of joint there is no need of two bodies. You can safely 
	 * pass a JointDef and reuse it. If both bodies are not passed, then an error will 
	 * be prompt.
	 * @param spriteA	The first body.
	 * @param spriteB	The second body.
	 * @param jointDef	The joint definition.
	 */
	public B2FlxJoint(B2FlxSprite spriteA, B2FlxSprite spriteB, JointDef jointDef)
	{
		super();
		
		if(spriteA != null)
			bodyA = spriteA.body;
		if(spriteB != null)
			bodyB = spriteB.body;
		if(bodyA == null && bodyB == null)
			throw new Error("your jointDef must have b2Body instances for its a and b properties");
		if(jointDef != null)
			this.jointDef = jointDef;
		scrollFactor = new FlxPoint(1.0f,1.0f);
		init();
	}
	
	/**
	 * Depending on the type of joint there is no need of two bodies. You can safely 
	 * pass a JointDef and reuse it.
	 * @param spriteA	The first body.
	 * @param spriteB	The second body.
	 */
	public B2FlxJoint(B2FlxSprite spriteA, B2FlxSprite spriteB)
	{
		this(spriteA, spriteB, null);
	}
	
	/**
	 * Depending on the type of joint there is no need of two bodies. You can safely 
	 * pass a JointDef and reuse it.
	 * @param spriteA	The first body.
	 */
	public B2FlxJoint(B2FlxSprite spriteA)
	{
		this(spriteA, null, null);
	}
	
	/**
	 * Initialize the jointDef and set the default values.
	 */
	private void init()
	{
		setupJointDef();
		defineDefaults();
		setDefaults();
	}
		
	/**
	 * Define the defaults.
	 */
	protected void defineDefaults()
	{
		lineThickness = 1;
		lineColor = 0xFF0000;
		lineAlpha = .75f;
		showLine = false;
	}
	
	/**
	 * Override this in child class to create specific joint definition.
	 */
	abstract protected void setupJointDef();
	
	/**
	 * Override this in child class to hook for object specific params.
	 */
	protected void setDefaults(){}
	
	/**
	 * Override this in child class to create the specific joint.
	 * @return This joint. Handy for chaining stuff together.
	 */
	abstract public B2FlxJoint create();
		
	/**
	 * This will remove the joint from the field, but the properties will be 
	 * intact so you can revive the joint again if needed.
	 */
	@Override
	public void kill()
	{
		B2FlxB.world.destroyJoint(joint);
		super.kill();
	}
	
	/**
	 * It brings the joint back to life and it connects with the bodies 
	 * were connected before.
	 */
	@Override
	public void revive()
	{
		create();
		super.revive();
	}
	
	/**
	 * Cleans up the memory.
	 */
	@Override
	public void destroy()
	{
		B2FlxB.world.destroyJoint(joint);
		joint = null;
		jointDef = null;
		super.destroy();
	}
	
	/**
	 * Main loop. Draw the line if showLine is set to true.
	 */
	@Override
	public void update()
	{
		if(joint != null && showLine)
			drawLineJoint();
		super.update();
	}

	/**
	 * Draw line joint.
	 */
	protected void drawLineJoint()
	{
		FlxCamera Camera= FlxG.camera;			
		Vector2 p1 = joint.getAnchorA();
		Vector2 p2 = joint.getAnchorB();
		float x1 = p1.x * RATIO - (int)Camera.scroll.x * scrollFactor.x;
		float y1 = p1.y * RATIO - (int)Camera.scroll.y * scrollFactor.y;
		float x2 = p2.x * RATIO - (int)Camera.scroll.x * scrollFactor.x;
		float y2 = p2.y * RATIO - (int)Camera.scroll.y * scrollFactor.y;
		
		// TODO: draw line
		// Draw line
//		var line:Shape = B2FlxB.gfx;
//		line.graphics.lineStyle(lineThickness, lineColor, lineAlpha);
//		line.graphics.moveTo(x1, y1);
//		line.graphics.lineTo(x2, y2);
	}
	
	/**
	 * Set the joint definition.
	 * @param jointDef
	 * @return	This joint. Handy for chaining stuff together.
	 */
	public B2FlxJoint setJointDef(JointDef jointDef)
	{
		this.jointDef = jointDef;
		return this;
	}
	
	public B2FlxJoint setBodyA(Body bodyA)
	{
		this.bodyA = bodyA;
		return this;
	}
	
	public B2FlxJoint setBodyB(Body bodyB)
	{
		this.bodyB = bodyB;
		return this;
	}
	
	public B2FlxJoint setAnchorA(Vector2 anchorA)
	{
		this.anchorA = anchorA;
		return this; 
	}
	
	public B2FlxJoint setAnchorB(Vector2 anchorB)
	{
		this.anchorB = anchorB;
		return this;
	}
	
	public B2FlxJoint setCollideConnected(boolean collideConnected)
	{
		jointDef.collideConnected = collideConnected;
		return this;
	}
	
	public B2FlxJoint setShowLine(boolean showLine)
	{
		this.showLine = showLine;
		return this;
	}
	
	public B2FlxJoint setLineThickness(float lineThickness)
	{
		this.lineThickness = lineThickness;
		return this;
	}
	
	public B2FlxJoint setLineColor(int lineColor)
	{
		this.lineColor = lineColor;
		return this;
	}
	
	public B2FlxJoint setLineAlpha(float lineAlpha)
	{
		this.lineAlpha = lineAlpha;
		return this;
	}
}
