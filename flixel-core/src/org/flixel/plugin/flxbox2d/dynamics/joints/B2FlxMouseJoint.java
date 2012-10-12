package org.flixel.plugin.flxbox2d.dynamics.joints;

import org.flixel.FlxG;
import org.flixel.plugin.flxbox2d.B2FlxB;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;

public class B2FlxMouseJoint
{
	private MouseJointDef _mouseJointDef;
	private MouseJoint _mouseJoint;
	private Body hitBody;
	protected Body groundBody;
	private Vector3 testPoint;
	private Vector2 _mouseTarget;
	private float _mouseWorldX;
	private float _mouseWorldY;
	// TODO: justRelease, pressed, justPressed
	public B2FlxMouseJoint()
	{
		groundBody = B2FlxB.world.createBody(new BodyDef());
		testPoint = new Vector3();
		_mouseTarget = new Vector2();
	}
	
	
	public void update()
	{
		updateMouseWorld();
		mouseDestroy();
		mouseDown();
		mouseDrag();
	}
	
	public void destroy()
	{
		_mouseJoint = null;
	}

	private void mouseDestroy()
	{
		if(FlxG.mouse.justReleased())
		{
			if(_mouseJoint != null)
			{
				B2FlxB.world.destroyJoint(_mouseJoint);
				_mouseJoint = null;
			}
		}		
	}
	
	
	public boolean mouseDown()
	{
		if(FlxG.mouse.justPressed() && _mouseJoint == null)
		{
			hitBody = null;
			testPoint.set(_mouseWorldX, _mouseWorldY, 0);
			B2FlxB.world.QueryAABB(getBodyCallback, testPoint.x - 0.0001f, testPoint.y - 0.0001f, testPoint.x + 0.0001f, testPoint.y + 0.0001f);
			
			if (hitBody == groundBody)
				return false;
			
			// ignore kinematic bodies, they don't work with the mouse joint
			if (hitBody != null && hitBody.getType() == BodyType.KinematicBody) 
				return false;
			
			// if we hit something we create a new mouse joint
			// and attach it to the hit body.
			if (hitBody != null) 
			{
				_mouseJointDef = new MouseJointDef();
				_mouseJointDef.bodyA = groundBody;
				_mouseJointDef.bodyB = hitBody;
				_mouseJointDef.collideConnected = true;
				_mouseJointDef.target.set(testPoint.x, testPoint.y);
				_mouseJointDef.maxForce = 100.0f * hitBody.getMass();
				
				_mouseJoint = (MouseJoint)B2FlxB.world.createJoint(_mouseJointDef);
				hitBody.setAwake(true);
				return true;
			}
		}
		return false;
	}
	
	/** we instantiate this vector and the callback here so we don't irritate the GC **/
	
	QueryCallback getBodyCallback = new QueryCallback()
	{
		@Override
		public boolean reportFixture(Fixture fixture)
		{
			// if the hit point is inside the fixture of the body
			// we report it
			if(fixture.testPoint(testPoint.x, testPoint.y))
			{
				hitBody = fixture.getBody();
				return false;
			}
			else
				return true;
		}
	};
	
	public boolean mouseDrag()
	{
		if(_mouseJoint != null)
		{	
			_mouseTarget.set(_mouseWorldX, _mouseWorldY);
			_mouseJoint.setTarget(_mouseTarget);
			return true;
		}		
		return false;
	}
	
	private void updateMouseWorld()
	{
		_mouseWorldX = FlxG.mouse.x / B2FlxB.RATIO;
		_mouseWorldY = FlxG.mouse.y / B2FlxB.RATIO;
	}
}
