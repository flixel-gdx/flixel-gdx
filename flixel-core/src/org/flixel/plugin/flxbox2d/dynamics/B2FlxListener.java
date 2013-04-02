package org.flixel.plugin.flxbox2d.dynamics;

import org.flixel.plugin.flxbox2d.collision.shapes.B2FlxShape;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 *
 * @author Ka Wing Chin
 */
public abstract class B2FlxListener
{
	public String type;
	public void beginContact(B2FlxShape sprite1, B2FlxShape sprite2, Contact contact){};
	public void endContact(B2FlxShape sprite1, B2FlxShape sprite2, Contact contact){};
	public void preSolve(B2FlxShape sprite1, B2FlxShape sprite2, Contact contact, Manifold oldManifold){};
	public void postSolve(B2FlxShape sprite1, B2FlxShape sprite2, Contact contact, ContactImpulse impulse){};
}

