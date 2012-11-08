package org.flixel.plugin.flxbox2d.event;

import org.flixel.plugin.flxbox2d.collision.shapes.B2FlxShape;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;

/**
 *
 * @author Ka Wing Chin
 */
public interface IB2FlxCollision
{
	public void callback(B2FlxShape sprite1, B2FlxShape sprite2, Vector2[] points, Contact contact);
}