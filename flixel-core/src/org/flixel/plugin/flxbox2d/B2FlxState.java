package org.flixel.plugin.flxbox2d;

import org.flixel.FlxG;
import org.flixel.FlxState;
import org.flixel.plugin.flxbox2d.dynamics.B2FlxCollision;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * This is the basic game "state" for Box2D object. The FlxState class doesn't work with 
 * the FlxBox2D plugin. The world is created in this state and it also does the time steps.
 * 
 * @author Ka Wing Chin
 */
public class B2FlxState extends FlxState
{	
	// The world where the object lives.
	public World world;
	// Velocity iterations for the velocity constraint solver.
	protected int velocityIterations = 10; 
	// Position iterations for the position constraint solver. 
	protected int Iterations = 10;
	
	private B2FlxCollision _contactListener;
	
	
	/**
	 * Creates the world with earth gravity and let inactive bodies sleep. 
	 * Override this to setup your own world.
	 */
	@Override
	public void create()
	{			
		// Setup required static variables
		B2FlxB.init();
		
		// Construct a world object.
		world = B2FlxB.world = new World(new Vector2(0, 9.8f), true);
		world.setContactListener(_contactListener = new B2FlxCollision());
	}
		
	/**
	 * The main loop.
	 */
	@Override
	public void update()
	{
		world.step(FlxG.elapsed, velocityIterations, Iterations);
		world.clearForces();
		super.update();
	}
	
	/**
	 * Clean up memory. The world will be disposed.
	 */
	@Override
	public void destroy()
	{		
		super.destroy();
		_contactListener.destroy();
		_contactListener = null;
		world.dispose();
		world = null;
		B2FlxB.destroy();
	}
	
	/**
	 * Change the global gravity vector.
	 * @param gravity
	 */
	public void setGravity(Vector2 gravity)
	{
		world.setGravity(gravity);
	}
	
	/**
	 * Change the global gravity vector.
	 * @param gravityX
	 * @param gravityY
	 */
	public void setGravity(float gravityX, float gravityY)
	{
		setGravity(new Vector2(gravityX, gravityY));
	}
}
