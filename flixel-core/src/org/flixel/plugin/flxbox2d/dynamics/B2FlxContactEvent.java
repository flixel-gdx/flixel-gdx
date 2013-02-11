package org.flixel.plugin.flxbox2d.dynamics;

import org.flixel.plugin.flxbox2d.collision.shapes.B2FlxShape;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

import flash.events.Event;

/**
 * An event type for FlxBox2D.
 *
 * @author Ka Wing Chin
 */
public class B2FlxContactEvent extends Event
{
	public static final String BEGIN = "begin";
	public static final String END = "end";
	public static final String PRESOLVE = "presolve";
	public static final String POSTSOLVE = "postsolve";
	
	/**
	 * The first sprite.
	 */
	public B2FlxShape sprite1;
	/**
	 * The second sprite.
	 */
	public B2FlxShape sprite2;
	/**
	 * The contact data.
	 */
	public Contact contact;
	/**
	 * The old manifold to detect changes.
	 */
	public Manifold oldManifold;
	/**
	 * The contact impulse.
	 */
	public ContactImpulse impulse;

	/**
	 * Constructor
	 * @param type	The type of the event.
	 */
	public B2FlxContactEvent(String type)
	{
		super(type);
	}
	
	/**
	 * Clean up the memory.
	 */
	public void destroy()
	{
		sprite1 = null;
		sprite2 = null;
		contact = null;
		oldManifold = null;
		impulse = null;
	}
}

