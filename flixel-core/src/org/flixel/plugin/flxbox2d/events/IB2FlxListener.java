package org.flixel.plugin.flxbox2d.events;

import org.flixel.plugin.flxbox2d.collision.shapes.B2FlxShape;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 *
 * @author Ka Wing Chin
 */
public interface IB2FlxListener
{
	public String type = null;
	/**
	 * This event will be called when collision happens.
	 * 
	 * @param sprite1		The first sprite.
	 * @param sprite2		The second sprite.
	 * @param contact		The contact.
	 * @param oldManifold	The old manifold which holds the collision location in world coordinates. Only for PreSolve.
	 * @param impulse		The impulse used to maintain the sustained contact. Only for PostSolve.
	 */
	public void onContact(B2FlxShape sprite1, B2FlxShape sprite2, Contact contact, Manifold oldManifold, ContactImpulse impulse);
}

