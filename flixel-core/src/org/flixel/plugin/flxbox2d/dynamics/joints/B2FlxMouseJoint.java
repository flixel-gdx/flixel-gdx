package org.flixel.plugin.flxbox2d.dynamics.joints;

import org.flixel.FlxBasic;
import org.flixel.FlxCamera;
import org.flixel.FlxG;
import org.flixel.plugin.flxbox2d.B2FlxB;
import org.flixel.plugin.flxbox2d.system.debug.B2FlxDebug;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * The mouse joint handles the mouse events in box2d world. It can drag bodies if they are draggable.
 * It works with the <code>FlxCamera</code>. In debug mode, on drag it leaves a line behind.
 * 
 * @author Ka Wing Chin
 */
public class B2FlxMouseJoint extends FlxBasic
{	
	/**
	 * The definition of the mouse joint.
	 */
	private MouseJointDef _mouseJointDef;
	/**
	 * The mouse joint.
	 */
	private MouseJoint _mouseJoint;
	/**
	 * The body that got hit by a pointer.
	 */
	private Body _hitBody;
	/**
	 * Just a static body. Used for tracking mouseDown.
	 */
	private Body _groundBody;
	/**
	 * The point where the pointer got hit.
	 */
	private Vector3 _testPoint;
	/**
	 * The target of the current mouse when pressed in box2d world.
	 */
	private Vector2 _mouseTarget;
	/**
	 * The x-position of the mouse in box2d world.
	 */
	private float _mouseWorldX;
	/**
	 * The y-position of the mouse in box2d world.
	 */
	private float _mouseWorldY;
	/**
	 * Flag for destroy when world got locked.
	 */
	private boolean _removeBySchedule;
	/**
	 * The maximum constraint force that can be exerted to move the candidate body. 
	 */
	public static float maxForce = 300f;
		
	// TODO: add multi touch support.
	// TODO: return boolean whether the body got justPressed, pressed and justReleased.
	
	/**
	 * Constructor
	 */
	public B2FlxMouseJoint()
	{
		_mouseJointDef = new MouseJointDef();
		_groundBody = B2FlxB.world.createBody(new BodyDef());
		_testPoint = new Vector3();
		_mouseTarget = new Vector2();
		_removeBySchedule = false;
	}
	
	/**
	 * Main loop for the mouse joint.
	 */
	public void update()
	{
		updateMouseWorld();
		mouseDown();
		mouseDrag();
		mouseUp();
	}
	
	/**
	 * Updates the mouse position in box2d world.
	 */
	private void updateMouseWorld()
	{
		_mouseWorldX = FlxG.mouse.x / B2FlxB.RATIO;
		_mouseWorldY = FlxG.mouse.y / B2FlxB.RATIO;
	}
	
	/**
	 * Check whether a body got pressed.
	 */
	@SuppressWarnings("unchecked")
	private boolean mouseDown()
	{
		if(_mouseJoint == null && FlxG.mouse.justPressed())
		{
			_hitBody = null;
			_testPoint.set(_mouseWorldX, _mouseWorldY, 0);
			B2FlxB.world.QueryAABB(getBodyCallback, _testPoint.x - 0.0001f, _testPoint.y - 0.0001f, _testPoint.x + 0.0001f, _testPoint.y + 0.0001f);
			
			if (_hitBody == _groundBody || _hitBody == null)
				return false;
			
			ObjectMap<String, Object> userData = (ObjectMap<String, Object>) _hitBody.getUserData();
			if(userData == null)
				return false;
			
			// ignore kinematic bodies, they don't work with the mouse joint
			if (_hitBody != null && _hitBody.getType() == BodyType.KinematicBody || !(Boolean)userData.get("draggable")) 
				return false;
			
			// if we hit something we pass the data to the mouse joint.
			// and attach it to the hit body.
			if (_hitBody != null) 
			{
				_mouseJointDef.bodyA = _groundBody;
				_mouseJointDef.bodyB = _hitBody;
				_mouseJointDef.collideConnected = true;
				_mouseJointDef.target.set(_testPoint.x, _testPoint.y);
				_mouseJointDef.maxForce = maxForce * _hitBody.getMass();
				if(_mouseJoint == null)
					_mouseJoint = (MouseJoint)B2FlxB.world.createJoint(_mouseJointDef);
				_hitBody.setAwake(true);
				userData.put("mouseJoint", this);
				_removeBySchedule = false;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * If a body got pressed, the mouse joint will be updated with the mouse coordinates.
	 * @return
	 */
	public boolean mouseDrag()
	{
		if(_mouseJoint != null && FlxG.mouse.pressed())
		{	
			_mouseJoint.setTarget(_mouseTarget.set(_mouseWorldX, _mouseWorldY));
			return true;
		}
		return false;
	}
	
	/**
	 * On release the mouse joint will be destroyed.
	 */
	private void mouseUp()
	{
		if(FlxG.mouse.justReleased())
		{
			kill();
		}
	}
	
	/**
	 * Kills the joints. The mouse joint will be nullified.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void kill()
	{
		if((!B2FlxB.world.isLocked() && !FlxG.mouse.pressed() && _mouseJoint != null) || _removeBySchedule)
		{
			ObjectMap<String, Object> userData = (ObjectMap<String, Object>) _hitBody.getUserData();
			if(userData != null && (Boolean)userData.get("exists"))
			{
				if(_mouseJoint != null)
					B2FlxB.world.destroyJoint(_mouseJoint);
				userData.put("mouseJoint", null);
			}
			_mouseJoint = null;
			_removeBySchedule = false;
		}
		else if(B2FlxB.world.isLocked())
		{
			_removeBySchedule = true;
			B2FlxB.addSafelyRemove(this);
		}
	}
	
	/**
	 * Clean up the memory. This is an internal method. Do not use it!
	 */
	public void destroy()
	{
		_mouseJoint = null;
		_mouseJointDef = null;
		_hitBody = null;
		_groundBody = null;
		_testPoint = null;
		_mouseTarget = null;
	}
	
	/** 
	 * A callback for all fixtures that potentially overlap the provided AABB.
	 * We instantiate this vector and the callback here so we don't irritate the GC. 
	 */	
	QueryCallback getBodyCallback = new QueryCallback()
	{
		@Override
		public boolean reportFixture(Fixture fixture)
		{
			// If the hit point is inside the fixture of the body we report it.
			if(fixture.testPoint(_testPoint.x, _testPoint.y))
			{
				_hitBody = fixture.getBody();
				return false;
			}
			else
				return true;
		}
	};

	@Override
	public void draw()
	{
		if(FlxG.visualDebug && !ignoreDrawDebug)
			drawDebug();
	}
	
	/**
	 * Draws the debug line when the mouse drags.
	 */
	@Override
	public void drawDebug()
	{
		if(_mouseJoint == null || (_mouseJoint.getBodyA() == null || _mouseJoint.getBodyB() == null))
			return;
		
		FlxCamera camera = FlxG.getActiveCamera();
		if(camera == null)
			camera = FlxG.camera;
		
		Vector2 a = _mouseJoint.getAnchorA();
		Vector2 b = _mouseJoint.getAnchorB();
		a.mul(B2FlxB.RATIO);
		b.mul(B2FlxB.RATIO);
		a.x -= camera.scroll.x; 
		a.y -= camera.scroll.y; 
		b.x -= camera.scroll.x; 
		b.y -= camera.scroll.y; 
		ShapeRenderer segment = FlxG.flashGfx.getShapeRenderer();
		FlxG.flashGfx.lineStyle(1, B2FlxDebug.JOINT_COLOR, 1);
		segment.line(a.x, a.y, b.x, b.y);
	}
}
