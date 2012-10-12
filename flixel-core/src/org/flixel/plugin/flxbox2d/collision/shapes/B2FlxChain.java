package org.flixel.plugin.flxbox2d.collision.shapes;

import org.flixel.plugin.flxbox2d.B2FlxB;

import com.badlogic.gdx.physics.box2d.ChainShape;

/**
 *
 * @author Ka Wing Chin
 */
public class B2FlxChain extends B2FlxSprite
{
	public B2FlxChain(float X, float Y)
	{
		super(X, Y);
	}
	
	@Override
	public void setDefaults()
	{
		setType(STATIC);
	}
	
	@Override
	public void createShape()
	{
		shape = new ChainShape();
		fixtureDef.shape = shape;
		
	}
	@Override
	public B2FlxSprite create()
	{
		bodyDef.position.x = x / RATIO;
		bodyDef.position.y = y / RATIO;
		position = bodyDef.position;
		body = B2FlxB.world.createBody(bodyDef);
		
		// TODO: create 
		shape.dispose();
		shape = null;
		return this;
	}

	

}

