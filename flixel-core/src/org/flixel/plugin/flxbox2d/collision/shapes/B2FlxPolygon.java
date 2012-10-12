package org.flixel.plugin.flxbox2d.collision.shapes;

import org.flixel.FlxG;
import org.flixel.plugin.flxbox2d.B2FlxB;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * A polygon shape which can be a convex or concave. The vertices
 * must be in CCW order for a right-handed coordinates system.
 * 
 * @author Ka Wing Chin
 */
public class B2FlxPolygon extends B2FlxSprite
{
	// Holds the vertices.
	private float[][][] _vertices;

	/**
	 * This creates an polygon.
	 * @param x			The X-coordinate of the point in space.
	 * @param y			The Y-coordinate of the point in space.
	 * @param vertices	The vertices of the polygon.
	 */
	public B2FlxPolygon(float x, float y, float[][][] vertices)
	{
		super(x, y);
		if(vertices != null)
			_vertices = vertices;
		else
			FlxG.log("no vertices has been set, default is used.");
		createShape();
	}
	
	/**
	 * This creates an polygon.
	 * @param x			The X-coordinate of the point in space.
	 * @param y			The Y-coordinate of the point in space.
	 */
	public B2FlxPolygon(float x, float y)
	{
		this(x, y, null);
	}
	
	/**
	 * This creates an polygon.
	 * @param x			The X-coordinate of the point in space.
	 */
	public B2FlxPolygon(float x)
	{
		this(x, 0, null);
	}
	
	/**
	 * This creates an polygon.
	 */
	public B2FlxPolygon()
	{
		this(0, 0, null);
	}
	
	/**
	 * The default polygon is a box. A message will be logged so
	 * you'll know the default has been used.
	 */
	@Override
	public void setDefaults()
	{
		/*
		 * p1----p2
		 * |	 |
		 * |	 |
		 * p4----p3
		 */
		_vertices = new float[][][]{{{-10, -10}, {10, -10}, {10, 10}, {-10, 10}}};
	}
	
	/**
	 * Creates the shape.
	 */
	@Override
	public void createShape()
	{
		shape = new PolygonShape();
		fixtureDef.shape = shape;
	}

	/**
	 * Creates the body.
	 * @return This object. Handy for chaining stuff together.
	 */
	@Override
	public B2FlxPolygon create()
	{	
		bodyDef.position.x = x / RATIO;
		bodyDef.position.y = y / RATIO;
		position = bodyDef.position;
		body = B2FlxB.world.createBody(bodyDef);
		Vector2[] vector;
		int vertexCount = 0;
		int indicesCount = _vertices.length;
		for(int i = 0; i < indicesCount; i++) 
		{
			vertexCount = _vertices[i].length;
			vector = new Vector2[vertexCount];
			for (int j = 0; j < vertexCount; j++) 
			{		
				vector[j] = new Vector2((float)_vertices[i][j][0] / RATIO, (float)_vertices[i][j][1] / RATIO);					
			}
			((PolygonShape)shape).set(vector);			
			body.createFixture(fixtureDef);
		}
		shape.dispose();
		shape = null;
		return this;
	}
	
	/**
	 * Set vertices, call create after this to create the body.
	 * @param vertices
	 * @return	This object. Handy for chaining stuff together.
	 */
	public B2FlxPolygon setVertices(float[][][] vertices)
	{
		_vertices = vertices;
		return this;
	}
}
