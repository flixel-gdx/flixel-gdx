package org.flixel.plugin.flxbox2d.collision.shapes;

import org.flixel.plugin.flxbox2d.B2FlxB;

/**
 * This class doesn't have any shape. It's a shapeless body which is used 
 * to combine multiple shapes together.
 * 
 * @author Ka Wing Chin
 */
public class B2FlxSprite extends B2FlxShape
{
	/**
	 * It creates an shapeless body.
	 * @param x		The X-coordinate of the point in space.
	 * @param y 	The Y-coordinate of the point in space.
	 */
	public B2FlxSprite(float x, float y)
	{
		super(x, y);
	}
	
	/**
	 * It creates an shapeless body.
	 * @param x		The X-coordinate of the point in space.
	 */
	public B2FlxSprite(float x)
	{
		this(x, 0);
	}
	
	/**
	 * It creates an shapeless body.
	 */
	public B2FlxSprite()
	{
		this(0, 0);
	}
	
	@Override
	public void setDefaults()
	{
		// Nothing
	}

	@Override
	public void createShape()
	{
		// Shapeless
	}
	
	/**
	 * Create a body without any fixtures.
	 */
	@Override
	protected void createBody()
	{
		bodyDef.position.x = x / RATIO;
		bodyDef.position.y = y / RATIO;
		position = bodyDef.position;
		body = B2FlxB.world.createBody(bodyDef);
		setUserData(userData);
	}
}

