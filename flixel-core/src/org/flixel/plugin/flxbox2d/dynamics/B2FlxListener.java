package org.flixel.plugin.flxbox2d.dynamics;

import org.flixel.plugin.flxbox2d.collision.shapes.B2FlxShape;
import org.flixel.plugin.flxbox2d.event.IB2FlxContact;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 *
 * @author Ka Wing Chin
 */
public class B2FlxListener implements IB2FlxContact
{
	public String type;
	@Override
	public void beginContact(B2FlxShape sprite1, B2FlxShape sprite2, Contact contact){};
	@Override
	public void endContact(B2FlxShape sprite1, B2FlxShape sprite2, Contact contact){};
	@Override
	public void preSolve(B2FlxShape sprite1, B2FlxShape sprite2, Contact contact, Manifold oldManifold){};
	@Override
	public void postSolve(B2FlxShape sprite1, B2FlxShape sprite2, Contact contact, ContactImpulse impulse){};
}

