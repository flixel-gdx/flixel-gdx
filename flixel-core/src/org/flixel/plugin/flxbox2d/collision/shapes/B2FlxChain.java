package org.flixel.plugin.flxbox2d.collision.shapes;

import org.flixel.FlxG;
import org.flixel.plugin.flxbox2d.B2FlxB;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

/**
 * The chain shape provides an efficient way to connect many edges together 
 * to construct your static game worlds. Chain shapes automatically eliminate 
 * ghost collisions and provide two sided collision.
 * 
 * It won't draw any graphic on the screen. It's not recommended to load a graphic on this shape, because it's simply
 * a line without any bounding box. It doesn't check whether the object is on
 * screen or not. In the B2FlxShape::draw you'll find the if statement that cancels the drawing for this shape.
 * 
 * @author Ka Wing Chin
 */
public class B2FlxChain extends B2FlxShape
{
	/** 
	 * Holds the vertices.
	 */
	private Array<float[]> _vertices;
	/**
	 * Whether if the chain is a loop or not.
	 */
	private boolean _loop;
	/**
	 * Used for establishing connectivity to a vertex that precedes the first vertex.
	 */
	private Vector2 _prevVertex;
	/**
	 * Used for establishing connectivity to a vertex that precedes the last vertex.
	 */
	private Vector2 _nextVertex;
	
	/**
	 * This creates a chain shape.
	 * @param x			The X-coordinate of the point in space.
	 * @param y			The X-coordinate of the point in space.
	 * @param vertices	The vertices of the line segment.
	 * @param loop		Whether if the chain is a loop or not. Default chain.
	 */
	public B2FlxChain(float x, float y, float[][] vertices, boolean loop)
	{
		super(x, y);
		if(vertices != null)
		{
			_vertices = new Array<float[]>();
			int length = vertices.length;
			for(int i = 0; i < length; i++)
			{				
				_vertices.add(vertices[i]);
			}
		}
		else
			FlxG.log("no vertices has been set, default is used.");
		_loop = loop;
		createShape();
	}
	
	/**
	 * This creates a chain shape.
	 * @param x			The X-coordinate of the point in space.
	 * @param y			The X-coordinate of the point in space.
	 * @param vertices	The vertices of the line segment.
	 */
	public B2FlxChain(float x, float y, float[][] vertices)
	{
		this(x, y, vertices, false);
	}
	
	/**
	 * This creates a chain shape.
	 * @param x			The X-coordinate of the point in space.
	 * @param y			The X-coordinate of the point in space.
	 */
	public B2FlxChain(float x, float y)
	{
		this(x, y, null, false);
	}
	
	/**
	 * This creates a chain shape.
	 * @param x			The X-coordinate of the point in space.
	 */
	public B2FlxChain(float x)
	{
		this(x, 0, null, false);
	}
	
	/**
	 * This creates a chain shape.
	 */
	public B2FlxChain()
	{
		this(0, 0, null, false);
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
		 * 						   
		 *
		 * p1-------p2
		 * 			  \
		 * 			   \
		 * 			    \
		 * 				p3----------p4
		 */
		_vertices = new Array<float[]>();
		_vertices.add(new float[]{10,100});
		_vertices.add(new float[]{100,100});
		_vertices.add(new float[]{150,150});
		_vertices.add(new float[]{250,150});
		setPrevVertex(5, 100);
		setNextVertex(255, 150);
	}
	
	/**
	 * Creates the shape.
	 */
	@Override
	public void createShape()
	{
		shape = new ChainShape();
		fixtureDef.shape = shape;
		int length = _vertices.size;
		Vector2[] v = new Vector2[length];
		for(int i = 0; i < length; i++)
		{
			v[i] = new Vector2(_vertices.get(i)[0]/RATIO, _vertices.get(i)[1]/RATIO);
		}
		if(_loop)
			((ChainShape)shape).createLoop(v);
		else
		{
			((ChainShape)shape).createChain(v);
			// Add ghost edges
			if(_prevVertex != null)
				((ChainShape)shape).setPrevVertex(_prevVertex);
			if(_nextVertex != null)
				((ChainShape)shape).setNextVertex(_nextVertex);			
		}
	}
	
	/**
	 * Creates the body.
	 * @return This object. Handy for chaining stuff together.
	 */
	@Override
	protected void createBody()
	{
		bodyDef.position.x = x / RATIO;
		bodyDef.position.y = y / RATIO;
		position = bodyDef.position;
		body = B2FlxB.world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		shape.dispose();
		shape = null;
	}
	
	@Override
	public B2FlxShape create()
	{
		super.create();
		return this;
	}
	
	/**
	 * Establish connectivity to a vertex that precedes the first vertex. 
	 * Don't call this for loops. Needs to be called before create().
	 * @param x		The X-coordinate of the point in space. 
	 * @param y		The Y-coordinate of the point in space.
	 * @return	This object. Handy for chaining stuff together.
	 */
	public B2FlxChain setPrevVertex(float x, float y)
	{
		if(_loop)
		{
			FlxG.log("Can't set ghost edges on loops!");
			return this;
		}
		_prevVertex = new Vector2(x/RATIO, y/RATIO);
		return this;
	}

	/**
	 * Establish connectivity to a vertex that precedes the first vertex. 
	 * Don't call this for loops. Needs to be called before create().
	 * @param x		The X-coordinate of the point in space.
	 * @param y		The Y-coordinate of the point in space.
	 * @return	This object. Handy for chaining stuff together.
	 */
	public B2FlxChain setNextVertex(float x, float y)
	{
		if(_loop)
		{
			FlxG.log("Can't set ghost edges on loops!");
			return this;
		}
		_nextVertex = new Vector2(x/RATIO, y/RATIO);
		return this;
	}
	
	/**
	 * Adds a vertex to the line segment.
	 * @param vertex	The vertex in space.
	 * @return	This object. Handy for chaining stuff together.
	 */
	public B2FlxChain addVertex(float x, float y)
	{
		_vertices.add(new float[]{x, y});
		return this;
	}
	
	/**
	 * Adds a vertex to the line segment.
	 * @param vertex	The vertex in space.
	 * @return	This object. Handy for chaining stuff together.
	 */
	public B2FlxChain addVertex(float[] vertex)
	{
		_vertices.add(vertex);
		return this;
	}
	
	/**
	 * Adds vertices to the line segment.
	 * @param vertices	A collection of vertices.
	 * @return	This object. Handy for chaining stuff together.
	 */
	public B2FlxChain addVertices(float[][] vertices)
	{
		int length = vertices.length;
		for(int i = 0; i < length; i++)
		{
			_vertices.add(vertices[i]);
		}
		return this;
	}
	
	@Override
	public void destroy()
	{
		super.destroy();
		_vertices.clear();
		_vertices = null;
		_prevVertex = null;
		_nextVertex = null;
	}
	
	@Override
	public B2FlxChain setType(BodyType type){super.setType(type);return this;}	
	@Override
	public B2FlxChain setFixtureDef(FixtureDef fixtureDef){super.setFixtureDef(fixtureDef);return this;}	
	@Override
	public B2FlxChain setLinearDamping(float linearDamping){super.setLinearDamping(linearDamping);return this;}
	@Override
	public B2FlxChain setLinearVelocity(Vector2 linearVelocity){super.setLinearVelocity(linearVelocity);return this;}	
	@Override
	public B2FlxChain setLinearVelocity(float x, float y){super.setLinearVelocity(x, y);return this;}	
	@Override
	public B2FlxChain setAngularDamping(float angularDamping){super.setAngularDamping(angularDamping);return this;}	
	@Override
	public B2FlxChain setAngularVelocity(float angularVelocity){super.setAngularVelocity(angularVelocity);return this;}	
	@Override
	public B2FlxChain setBullet(boolean bullet){super.setBullet(bullet);return this;}	
	@Override
	public B2FlxChain setFixedRotation(boolean fixedRotation){super.setFixedRotation(fixedRotation);return this;}	
	@Override
	public B2FlxChain setAllowSleep(boolean allowSleep){super.setAllowSleep(allowSleep);return this;}	
	@Override
	public B2FlxChain setActive(boolean active){super.setActive(active);return this;}	
	@Override
	public B2FlxChain setAwake(boolean awake){super.setAwake(awake);return this;}	
	@Override
	public B2FlxChain setDensity(float density){super.setDensity(density);return this;}	
	@Override
	public B2FlxChain setFriction(float friction){super.setFriction(friction);return this;}	
	@Override
	public B2FlxChain setRestitution(float restitution){super.setRestitution(restitution);return this;}	
	@Override
	public B2FlxChain setPosition(Vector2 position){super.setPosition(position);return this;}	
	@Override
	public B2FlxChain setAngle(float angle){super.setAngle(angle);return this;}	
	@Override
	public B2FlxChain setGravityScale(float gravityScale){super.setGravityScale(gravityScale);return this;}	
	@Override
	public B2FlxChain setMaskBits(short maskBits){super.setMaskBits(maskBits);return this;}	
	@Override
	public B2FlxChain setCategoryBits(short categoryBits){super.setCategoryBits(categoryBits);return this;}	
	@Override
	public B2FlxChain setGroupIndex(short groupIndex){super.setGroupIndex(groupIndex);return this;}	
	@Override
	public B2FlxChain setSensor(boolean sensor){super.setSensor(sensor);return this;}
	@Override
	public B2FlxChain setReportBeginContact(boolean reportBeginContact){super.setReportBeginContact(reportBeginContact);return this;}
	@Override
	public B2FlxChain setReportEndContact(boolean reportEndContact){super.setReportEndContact(reportEndContact);return this;}
	@Override
	public B2FlxChain setReportPreSolve(boolean reportPreSolve){super.setReportPreSolve(reportPreSolve);return this;}
	@Override
	public B2FlxChain setReportPostSolve(boolean reportPostSolve){super.setReportPostSolve(reportPostSolve);return this;}
	@Override
	public B2FlxChain setResetAngle(boolean resetAngle){super.setResetAngle(resetAngle);return this;}	
	@Override
	public B2FlxChain setDraggable(boolean draggable){super.setDraggable(draggable);return this;}
}

