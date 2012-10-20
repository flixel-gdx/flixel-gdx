package org.flixel.plugin.flxbox2d.collision.shapes;

import org.flixel.plugin.flxbox2d.B2FlxB;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.badlogic.gdx.physics.box2d.Transform;

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
	
	/**
	 * Debug-only: because this class can contains any shape. It needs to check whether it's a circle or a polygon.
	 */
	@Override
	protected void drawShape(Fixture fixture, Transform transform, int color)
	{
		if(fixture.getType() == Type.Circle)
		{
			CircleShape circle = (CircleShape) fixture.getShape();
			t.set(circle.getPosition());
			transform.mul(t);
			drawSolidCircle(t, circle.getRadius(), axis.set(transform.vals[Transform.COS], transform.vals[Transform.SIN]), color);
		}
		else if (fixture.getType() == Type.Polygon) 
		{
			PolygonShape chain = (PolygonShape)fixture.getShape();
			int vertexCount = chain.getVertexCount();
			for (int i = 0; i < vertexCount; i++) 
			{
				chain.getVertex(i, B2FlxB.vertices[i]);
				transform.mul(B2FlxB.vertices[i]);
			}
			drawSolidPolygon(B2FlxB.vertices, vertexCount, color);
		}
	}
}

