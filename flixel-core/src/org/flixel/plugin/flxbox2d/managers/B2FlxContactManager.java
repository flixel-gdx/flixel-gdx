package org.flixel.plugin.flxbox2d.managers;

import org.flixel.plugin.flxbox2d.collision.shapes.B2FlxShape;
import org.flixel.plugin.flxbox2d.dynamics.B2FlxContactData;
import org.flixel.plugin.flxbox2d.dynamics.B2FlxContactListener;
import org.flixel.plugin.flxbox2d.events.B2FlxContactEvent;
import org.flixel.plugin.flxbox2d.events.IB2FlxListener;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import flash.events.Event;
import flash.events.IEventListener;

/**
 * A manager that handles the contact events.
 * 
 * @author Ka Wing Chin
 */
public class B2FlxContactManager
{
	public B2FlxContactListener listener;
	public Array<B2FlxContactData> beginData;
	public Array<B2FlxContactData> endData;
	public Array<B2FlxContactData> preSolveData;
	public Array<B2FlxContactData> postSolveData;
	
	public B2FlxContactManager(World world)
	{
		listener = new B2FlxContactListener();
		world.setContactListener(listener);
		
		beginData = new Array<B2FlxContactData>();
		endData = new Array<B2FlxContactData>();
		preSolveData = new Array<B2FlxContactData>();
		postSolveData = new Array<B2FlxContactData>();
		
		listener.addEventListener(B2FlxContactEvent.BEGIN_CONTACT, handleBeginContact);
		listener.addEventListener(B2FlxContactEvent.END_CONTACT, handleEndContact);
		listener.addEventListener(B2FlxContactEvent.PRE_SOLVE, handlePreSolve);
		listener.addEventListener(B2FlxContactEvent.POST_SOLVE, handlePostSolve);
	}
	
	public void destroy()
	{
		listener.destroy();
		listener = null;
		beginData.clear();
		beginData = null;
		endData.clear();
		endData = null;
		preSolveData.clear();
		preSolveData = null;
	}
	
	public void onBeginContact(int categoryA, int categoryB, IB2FlxListener listener)
	{
		beginData.add(new B2FlxContactData(categoryA, categoryB, listener));
	}
	
	public void onBeginContact(B2FlxShape spriteA, B2FlxShape spriteB, IB2FlxListener listener)
	{
		beginData.add(new B2FlxContactData(spriteA, spriteB, listener));
	}
	
	public void onBeginContact(B2FlxShape spriteA, int categoryB, IB2FlxListener listener)
	{
		beginData.add(new B2FlxContactData(spriteA, categoryB, listener));
	}
	
	public void onBeginContact(int categoryA, B2FlxShape spriteB, IB2FlxListener listener)
	{
		beginData.add(new B2FlxContactData(categoryA, spriteB, listener));
	}
	
	public void onEndContact(int categoryA, int categoryB, IB2FlxListener listener)
	{
		endData.add(new B2FlxContactData(categoryA, categoryB, listener));
	}
	
	public void onEndContact(B2FlxShape categoryA, B2FlxShape categoryB, IB2FlxListener listener)
	{
		endData.add(new B2FlxContactData(categoryA, categoryB, listener));
	}
	
	public void onEndContact(B2FlxShape spriteA, int categoryB, IB2FlxListener listener)
	{
		endData.add(new B2FlxContactData(spriteA, categoryB, listener));
	}
	
	public void onEndContact(int categoryA, B2FlxShape spriteB, IB2FlxListener listener)
	{
		endData.add(new B2FlxContactData(categoryA, spriteB, listener));
	}
	
	public void onPreSolve(int categoryA, int categoryB, IB2FlxListener listener)
	{
		preSolveData.add(new B2FlxContactData(categoryA, categoryB, listener));
	}
	
	public void onPreSolve(B2FlxShape categoryA, B2FlxShape categoryB, IB2FlxListener listener)
	{
		preSolveData.add(new B2FlxContactData(categoryA, categoryB, listener));
	}
	
	public void onPreSolve(B2FlxShape spriteA, int categoryB, IB2FlxListener listener)
	{
		preSolveData.add(new B2FlxContactData(spriteA, categoryB, listener));
	}
	
	public void onPreSolve(int categoryA, B2FlxShape spriteB, IB2FlxListener listener)
	{
		preSolveData.add(new B2FlxContactData(categoryA, spriteB, listener));
	}
	
	public void onPostSolve(int categoryA, int categoryB, IB2FlxListener listener)
	{
		postSolveData.add(new B2FlxContactData(categoryA, categoryB, listener));
	}
	
	public void onPostSolve(B2FlxShape categoryA, B2FlxShape categoryB, IB2FlxListener listener)
	{
		postSolveData.add(new B2FlxContactData(categoryA, categoryB, listener));
	}
	
	public void onPostSolve(B2FlxShape spriteA, int categoryB, IB2FlxListener listener)
	{
		postSolveData.add(new B2FlxContactData(spriteA, categoryB, listener));
	}
	
	public void onPostSolve(int categoryA, B2FlxShape spriteB, IB2FlxListener listener)
	{
		postSolveData.add(new B2FlxContactData(categoryA, spriteB, listener));
	}
		
	IEventListener handleBeginContact = new IEventListener()
	{
		@Override
		public void onEvent(Event event)
		{
			evaluate(beginData, (B2FlxContactEvent) event);
		}
	};
	
	IEventListener handleEndContact = new IEventListener()
	{
		@Override
		public void onEvent(Event event)
		{
			evaluate(endData, (B2FlxContactEvent) event);
		}
	};
	
	IEventListener handlePreSolve= new IEventListener()
	{
		@Override
		public void onEvent(Event event)
		{
			evaluate(preSolveData, (B2FlxContactEvent) event);
		}
	};
	
	IEventListener handlePostSolve = new IEventListener()
	{
		@Override
		public void onEvent(Event event)
		{
			evaluate(postSolveData, (B2FlxContactEvent) event);
		}
	};
	
	private void evaluate(Array<B2FlxContactData> data, B2FlxContactEvent event)
	{
		for(B2FlxContactData d : data)
		{
			if(d.type == d.GROUP)
			{
				if(d.categoryA == event.sprite1.categoryBits && d.categoryB == event.sprite2.categoryBits)
					d.listener.onContact(event.sprite1, event.sprite2, event.contact, event.oldManifold, event.impulse);
				else if(d.categoryA == event.sprite2.categoryBits && d.categoryB == event.sprite1.categoryBits)
					d.listener.onContact(event.sprite2, event.sprite1, event.contact, event.oldManifold, event.impulse);
			}
			else if(d.type == d.MIX_A)
			{
				if(d.spriteA == event.sprite1 && d.categoryB == event.sprite2.categoryBits)
					d.listener.onContact(d.spriteA, event.sprite2, event.contact, event.oldManifold, event.impulse);
				else if(d.spriteA == event.sprite2 && d.categoryB == event.sprite1.categoryBits)
					d.listener.onContact(d.spriteA, event.sprite1, event.contact, event.oldManifold, event.impulse);
			}
			else if(d.type == d.MIX_B)
			{
				if(d.spriteB == event.sprite1 && d.categoryA == event.sprite2.categoryBits)
					d.listener.onContact(event.sprite2, d.spriteB, event.contact, event.oldManifold, event.impulse);
				else if(d.spriteB == event.sprite2 && d.categoryA == event.sprite1.categoryBits)
					d.listener.onContact(event.sprite1, d.spriteB, event.contact, event.oldManifold, event.impulse);
			}
			else if(d.spriteA == event.sprite1 && d.spriteB == event.sprite2 || d.spriteA == event.sprite2 && d.spriteB == event.sprite2)
			{				
				d.listener.onContact(d.spriteA, d.spriteB, event.contact, event.oldManifold, event.impulse);
			}
		}
	}
}

