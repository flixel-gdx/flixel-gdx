package org.flixel.plugin.flxbox2d.dynamics.joints;

import org.flixel.FlxBasic;
import org.flixel.FlxCamera;
import org.flixel.FlxG;
import org.flixel.FlxPoint;
import org.flixel.plugin.flxbox2d.B2FlxB;
import org.flixel.plugin.flxbox2d.collision.shapes.B2FlxShape;
import org.flixel.plugin.flxbox2d.system.debug.B2FlxDebug;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.utils.ObjectMap;


/**
 * This is an abstract or parent class for all joints except <code>B2FlxMouseJoint</code>.
 * It contains the required variables for creating joints and drawing lines.
 * You shouldn't add this to the state unless you want to draw a line in your game.
 * <code>showLine</code> is set to true at default. The debug drawing is done with
 * <code>B2FlxDebug</code>. Don't add this to the state if you don't want to draw a line.
 * 
 * @author Ka Wing Chin
 */
public abstract class B2FlxJoint extends FlxBasic
{		
	/**
	 * This stores the attached bodies and the type of the joint.
	 */
	public JointDef jointDef;
	/**
	 * The base joint class. Joints are used to constraint two bodies together.
	 */
	public Joint joint;	
	/**
	 * First body.	
	 */
	public Body bodyA;
	/**
	 * Second body.
	 */
	public Body bodyB;	
	/**
	 * The anchor point of the first body.
	 */
	public Vector2 anchorA;	
	/**
	 * The anchor point of the second body.
	 */
	public Vector2 anchorB;	
	/**
	 * The anchor point of the first body in pixel world. 
	 */
	final static Vector2 p1 = new Vector2();
	/**
	 * The anchor point of the second body in pixel world.
	 */
	final static Vector2 p2 = new Vector2();
	/**
	 * The anchor point of the second body in transform in pixel world.
	 */
	final static Vector2 x1 = new Vector2();
	/**
	 * The anchor point of the second body in transform in pixel world.
	 */
	final static Vector2 x2 = new Vector2();	
	/**
	 * Whether to draw a line or not.
	 */
	public boolean showLine;	
	/**
	 * The thickness of the line. This won't work, because ShapeRenderer doesn't support this.
	 */
	public float lineThickness;
	/**
	 * The color of the line.
	 */
	public int lineColor = 0x00FFFF;
	/**
	 * The opacity of the line.
	 */
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
	 * Survive the joint on state change. Default is false. The joint needs to be static
	 * or keep a reference somewhere or it will be nullified.
	 */
	private boolean survive;
	/**
	 * Holds the user data.
	 */
	private ObjectMap<String, Object> userData;

	
	/**
	 * Depending on the type of joint there is no need of two bodies. You can safely 
	 * pass a JointDef and reuse it. If both bodies are not passed, then an error will 
	 * be prompt.
	 * @param spriteA	The first body.
	 * @param spriteB	The second body.
	 * @param jointDef	The joint definition.
	 */
	public B2FlxJoint(B2FlxShape spriteA, B2FlxShape spriteB, JointDef jointDef)
	{
		super();
		
		if(spriteA != null)
		{
			bodyA = spriteA.body;
			spriteA.joints.add(this);
		}
		if(spriteB != null)
		{
			bodyB = spriteB.body;
			spriteB.joints.add(this);
		}
		if(bodyA == null && bodyB == null)
			throw new Error("your jointDef must have b2Body instances for its a and b properties");
		if(jointDef != null)
			this.jointDef = jointDef;
	
		scrollFactor = new FlxPoint(1.0f,1.0f);
		
		B2FlxB.flxJoints.add(this);
		
		init();
	}
	
	/**
	 * Depending on the type of joint there is no need of two bodies. You can safely 
	 * pass a JointDef and reuse it.
	 * @param spriteA	The first body.
	 * @param spriteB	The second body.
	 */
	public B2FlxJoint(B2FlxShape spriteA, B2FlxShape spriteB)
	{
		this(spriteA, spriteB, null);
	}
	
	/**
	 * Depending on the type of joint there is no need of two bodies. You can safely 
	 * pass a JointDef and reuse it.
	 * @param spriteA	The first body.
	 */
	public B2FlxJoint(B2FlxShape spriteA)
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
		showLine = true;
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
		if(killJoint())
			super.kill();
	}
	
	/**
	 * Kill the joint.
	 * @return	Whether killing the joint was a succes or not.
	 */
	protected boolean killJoint()
	{
		if(!B2FlxB.world.isLocked() && joint != null)
		{
			B2FlxB.world.destroyJoint(joint);
			joint = null;
			return true;
		}
		else if(B2FlxB.world.isLocked())
		{
			B2FlxB.addSafelyRemove(this);
		}
		return false;
	}
	
	/**
	 * It brings the joint back to life and it connects with the bodies 
	 * that were connected before. //TODO: revive joint
	 */
	@Override
	public void revive()
	{
//		Vector2 a = bodyA.getTransform().getPosition();
//		Vector2 b = bodyB.getTransform().getPosition();
//		anchorA.add(a.x/RATIO, a.y/RATIO);
////		anchorA = a;
//		anchorB.add(b.x/RATIO, b.y/RATIO);
//		
//		create();
		super.revive();
	}
	
	/**
	 * Cleans up the memory.
	 */
	@Override
	public void destroy()
	{
		if(joint != null && exists)
		{
			killJoint();
			jointDef = null;
		}
		bodyA = null;
		bodyB = null;
		anchorA = null;
		anchorB = null;
		scrollFactor = null;
	}
	
	/**
	 * Draw line joint. Only if this joint is added to the state and showLine is set to true. 
	 */
	@Override
	public void draw()
	{
		if(joint != null && exists && (showLine || FlxG.visualDebug && !ignoreDrawDebug))
		{
			FlxCamera camera = FlxG.getActiveCamera();	
			if (cameras != null && !cameras.contains(camera, true))
				return;
			if (!onScreen(camera))
				return;			
			if(showLine)
				drawJoint(camera, lineThickness, lineColor, lineAlpha);
		}
	}
	
	/**
	 * Check and see if this object is currently on screen.
	 * @param camera	Specify which game camera you want.  If null getScreenXY() will just grab the first global camera.
	 * @return			Whether the line is on screen or not.
	 */
	public boolean onScreen(FlxCamera camera)
	{
		if(joint == null)
			return false;
		if(camera == null)
			camera = FlxG.camera;
		
		p1.set(joint.getAnchorA().scl(B2FlxB.RATIO));
		p2.set(joint.getAnchorB().scl(B2FlxB.RATIO));
		
		p1.x -= camera.scroll.x * scrollFactor.x;
		p1.y -= camera.scroll.y * scrollFactor.y;
		p2.x -= camera.scroll.x * scrollFactor.x;
		p2.y -= camera.scroll.y * scrollFactor.y;
		
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
		return (onScreenX && onScreenY);
	}
	
	/**
	 * Check and see if this object is currently on screen.
	 * @return		Whether the line is on screen or not.
	 */
	public boolean onScreen()
	{
		return onScreen(null);
	}
	
	/**
	 * Debug-only: draw debug line.
	 */
	@Override
	public void drawDebug()
	{
		if(!onScreen())
			return;
		if(FlxG.visualDebug && !ignoreDrawDebug)
			drawJoint(FlxG.getActiveCamera(), 1, B2FlxDebug.JOINT_COLOR, 1);
	}
	
	/**
	 * Draw joint for Friction, Gear, Prismatic, Revolute, Rope, Weld and Wheel joint.
	 * @param camera
	 * @param lineThickness
	 * @param lineColor
	 * @param lineAlpha
	 */
	protected void drawJoint(FlxCamera camera, float lineThickness, int lineColor, float lineAlpha)
	{
		ShapeRenderer segment = FlxG.flashGfx.getShapeRenderer();
		FlxG.flashGfx.lineStyle(lineThickness, lineColor, lineAlpha);
		x1.set(bodyA.getTransform().getPosition().scl(B2FlxB.RATIO));
		x2.set(bodyB.getTransform().getPosition().scl(B2FlxB.RATIO));
		x1.x -= camera.scroll.x * scrollFactor.x;
		x1.y -= camera.scroll.y * scrollFactor.y;
		x2.x -= camera.scroll.x * scrollFactor.x;
		x2.y -= camera.scroll.y * scrollFactor.y;
		segment.line(x1.x, x1.y, p1.x, p1.y);
		segment.line(p1.x, p1.y, p2.x, p2.y);
		segment.line(x2.x, x2.y, p2.x, p2.y);
	}
	
	/**
	 * Get the joint.
	 * @return
	 */
	public Joint getJoint()
	{
		return joint;
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
	
	/**
	 * Whether the joint needs to be destroyed on state change or not.
	 * When this is set to true, don't forget to set B2FlxB.surviveWorld to true,
	 * otherwise the joint will still get destroyed.
	 * @param survive
	 * @return
	 */	
	public B2FlxJoint setSurvive(boolean survive)
	{
		this.survive = survive;
		userData.put("survive", survive);
		return this;
	}
	
	/**
	 * @return the survive.
	 */
	public boolean getSurvive()
	{
		return survive;
	}
}
