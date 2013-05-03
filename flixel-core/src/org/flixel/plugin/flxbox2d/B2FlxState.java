package org.flixel.plugin.flxbox2d;

import org.flixel.FlxG;
import org.flixel.FlxState;
import org.flixel.plugin.flxbox2d.managers.B2FlxContactManager;

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
	/**
	 * The world where the object lives.
	 */
	public World world;
	/**
	 * The amount of time to simulate, this should not vary. 
	 * If you change the framerate be sure to update the timeStep:
	 * 1 / framerate.
	 */
	protected float timeStep;
	/**
	 * Velocity iterations for the velocity constraint solver.
	 */
	protected int velocityIterations = 8; 
	/**
	 * Position iterations for the position constraint solver. 
	 */
	protected int positionIterations = 3;
	/**
	 * Handle the collision.
	 */
	protected B2FlxContactManager contact;
	/**
	 * Preallocated gravity
	 */
	private final Vector2 _gravity = new Vector2(0, 9.8f);
	
	/**
	 * Prepare the Box2D initial setup.
	 * Creates the world with earth gravity and let inactive bodies sleep. 
	 * Override this to setup your own world.
	 */
	@Override
	public void create()
	{			
		// Setup required static variables.
		B2FlxB.init();
		// Fixed time step.
		timeStep = (float) (1f / FlxG.getFramerate());
		// Construct a world object.
		world = B2FlxB.world = new World(_gravity, true);
		// Create the contact manager.
		contact = B2FlxB.contact = new B2FlxContactManager(world);
		
		if(FlxG.debug)
			B2FlxB.initDebugger();
	}
		
	/**
	 * The main loop.
	 */
	@Override
	public void update()
	{
		world.step(timeStep, velocityIterations, positionIterations);
		world.clearForces();
		if(B2FlxB.scheduledForActive.size > 0)
			B2FlxB.safelyActivateBodies();
		if(B2FlxB.scheduledForInActive.size > 0)
			B2FlxB.safelyDeactivateBodies();
		if(B2FlxB.scheduledForRemoval.size > 0)
			B2FlxB.safelyRemoveBodies();
		if(B2FlxB.scheduledForRevival.size > 0)
			B2FlxB.safelyReviveBodies();
		if(B2FlxB.scheduledForMove.size > 0)
			B2FlxB.safelyMoveBodies();
		super.update();
	}
	
	/**
	 * Clean up memory. The world will be disposed.
	 */
	@Override
	public void destroy()
	{		
		super.destroy();		
		B2FlxB.destroy();
		contact = null;
		world = null;
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
		_gravity.set(gravityX, gravityY);
		setGravity(_gravity);
	}
}
