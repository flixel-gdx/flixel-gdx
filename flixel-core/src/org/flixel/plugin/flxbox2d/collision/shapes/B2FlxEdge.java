package org.flixel.plugin.flxbox2d.collision.shapes;

import org.flixel.FlxG;
import org.flixel.plugin.flxbox2d.B2FlxB;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * A line segment (edge) shape. These can be connected in chains or loops 
 * to other edge shapes. The connectivity information is used to ensure 
 * correct contact normals. 
 * 
 * It won't draw any graphic on the screen. It's not recommended to load a graphic on this shape, because it's simply
 * a line without any bounding box. It doesn't check whether the object is on
 * screen or not. In the B2FlxShape::draw you'll find the if statement that cancels the drawing for this shape.
 * 
 * @author Ka Wing Chin
 */
public class B2FlxEdge extends B2FlxShape
{
	/**
	 * Holds the vertices.
	 */
	private float[][] _vertices;
	/**
	 * Holds the shapes.
	 */
	public EdgeShape[] shapes;

	/**
	 * This creates an edge.
	 * @param x			The X-coordinate of the point in space.
	 * @param y			The Y-coordinate of the point in space.
	 * @param vertices	The vertices of the line segment.
	 */
	public B2FlxEdge(float x, float y, float[][] vertices)
	{
		super(x, y);
		if(vertices != null)
			_vertices = vertices;
		else
			FlxG.log("no vertices has been set, default is used.");
		createShape();
	}
	
	/**
	 * This creates an edge.
	 * @param x			The X-coordinate of the point in space.
	 * @param y			The Y-coordinate of the point in space.
	 */
	public B2FlxEdge(float x, float y)
	{
		this(x, y, null);
	}
	
	/**
	 * This creates an edge.
	 * @param x			The X-coordinate of the point in space.
	 */
	public B2FlxEdge(float x)
	{
		this(x, 0, null);
	}
	
	/**
	 * This creates an edge.
	 */
	public B2FlxEdge()
	{
		this(0, 0, null);
	}
	
	/**
	 * The default line segment. A message will be logged 
	 * so you'll know the default has been used.
	 */
	@Override
	public void setDefaults()
	{
		setType(STATIC);
		/*
		 * 						   p4
		 * 						  /
		 * p1-------p2          /
		 * 			  \       /
		 * 			   \    /
		 * 			    \ /
		 * 				p3
		 */
		_vertices = new float[][]{{10,100}, {100,100}, {150,150}, {250, 50}};
	}
	
	/**
	 * Creates the shape.
	 */
	@Override
	public void createShape()
	{		
		fixtureDef.shape = shape;
		int length = _vertices.length - 1;
		shapes = new EdgeShape[length];
		Vector2 startPoint;
		Vector2 endPoint;
		for (int i = 0; i < length; i++) 
		{
			startPoint = new Vector2(_vertices[i][0]/B2FlxB.RATIO, _vertices[i][1]/B2FlxB.RATIO);
			endPoint= new Vector2(_vertices[i+1][0]/B2FlxB.RATIO, _vertices[i+1][1]/B2FlxB.RATIO);
			shape = new EdgeShape();
			((EdgeShape)shape).set(startPoint, endPoint);
			shapes[i] = ((EdgeShape)shape);
		}
	}
	
	/**
	 * Creates the body.
	 */
	@Override
	public void createBody()
	{		
		bodyDef.position.x = x / B2FlxB.RATIO;
		bodyDef.position.y = y / B2FlxB.RATIO;
		position = bodyDef.position;
		body = B2FlxB.world.createBody(bodyDef);
		EdgeShape s;
		for(int i = 0; i < shapes.length; i++)
		{	
			s = shapes[i];
			fixtureDef.shape = s;
			body.createFixture(fixtureDef);
			s.dispose();
			s = null;
		}
		shape = null;
	}
	
	/**
	 * Creates an Edge body.
	 */
	@Override
	public B2FlxEdge create()
	{
		super.create();
		return this;
	}
	
	@Override
	public void destroy()
	{
		super.destroy();
		_vertices = null;
		shapes = null;
	}
	
	@Override
	public B2FlxEdge setType(BodyType type){super.setType(type);return this;}	
	@Override
	public B2FlxEdge setFixtureDef(FixtureDef fixtureDef){super.setFixtureDef(fixtureDef);return this;}	
	@Override
	public B2FlxEdge setLinearDamping(float linearDamping){super.setLinearDamping(linearDamping);return this;}
	@Override
	public B2FlxEdge setLinearVelocity(Vector2 linearVelocity){super.setLinearVelocity(linearVelocity);return this;}	
	@Override
	public B2FlxEdge setLinearVelocity(float x, float y){super.setLinearVelocity(x, y);return this;}	
	@Override
	public B2FlxEdge setAngularDamping(float angularDamping){super.setAngularDamping(angularDamping);return this;}	
	@Override
	public B2FlxEdge setAngularVelocity(float angularVelocity){super.setAngularVelocity(angularVelocity);return this;}	
	@Override
	public B2FlxEdge setBullet(boolean bullet){super.setBullet(bullet);return this;}	
	@Override
	public B2FlxEdge setFixedRotation(boolean fixedRotation){super.setFixedRotation(fixedRotation);return this;}	
	@Override
	public B2FlxEdge setAllowSleep(boolean allowSleep){super.setAllowSleep(allowSleep);return this;}	
	@Override
	public B2FlxEdge setActive(boolean active){super.setActive(active);return this;}	
	@Override
	public B2FlxEdge setAwake(boolean awake){super.setAwake(awake);return this;}	
	@Override
	public B2FlxEdge setDensity(float density){super.setDensity(density);return this;}	
	@Override
	public B2FlxEdge setFriction(float friction){super.setFriction(friction);return this;}	
	@Override
	public B2FlxEdge setRestitution(float restitution){super.setRestitution(restitution);return this;}	
	@Override
	public B2FlxEdge setPosition(Vector2 position){super.setPosition(position);return this;}	
	@Override
	public B2FlxEdge setAngle(float angle){super.setAngle(angle);return this;}	
	@Override
	public B2FlxEdge setGravityScale(float gravityScale){super.setGravityScale(gravityScale);return this;}	
	@Override
	public B2FlxEdge setMaskBits(short maskBits){super.setMaskBits(maskBits);return this;}	
	@Override
	public B2FlxEdge setCategoryBits(short categoryBits){super.setCategoryBits(categoryBits);return this;}	
	@Override
	public B2FlxEdge setGroupIndex(short groupIndex){super.setGroupIndex(groupIndex);return this;}	
	@Override
	public B2FlxEdge setSensor(boolean sensor){super.setSensor(sensor);return this;}
	@Override
	public B2FlxEdge setResetAngle(boolean resetAngle){super.setResetAngle(resetAngle);return this;}	
	@Override
	public B2FlxEdge setDraggable(boolean draggable){super.setDraggable(draggable);return this;}
	@Override
	public B2FlxEdge setSurvive(boolean survive){super.setSurvive(survive);return this;}
}
