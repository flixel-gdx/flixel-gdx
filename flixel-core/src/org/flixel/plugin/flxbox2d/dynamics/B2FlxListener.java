package org.flixel.plugin.flxbox2d.dynamics;

import org.flixel.plugin.flxbox2d.collision.shapes.B2FlxShape;

import com.badlogic.gdx.physics.box2d.Contact;

/**
 *
 * @author Ka Wing Chin
 */
public class B2FlxListener
{
	public String type;
	
	public void onContact(B2FlxShape sprite1, B2FlxShape sprite2, Contact contact){};
}

