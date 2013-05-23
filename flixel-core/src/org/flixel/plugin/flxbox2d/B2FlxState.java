package org.flixel.plugin.flxbox2d;

import org.flixel.FlxG;
import org.flixel.FlxState;
import org.flixel.FlxU;

/**
 * This is the basic game "state" for Box2D object. The FlxState class doesn't work with 
 * the FlxBox2D plugin. The world is created in this state and it also does the time steps.
 * 
 * @author Ka Wing Chin
 */
public class B2FlxState extends FlxState
{	
	/**
	 * The amount of time to simulate, this should not vary. 
	 * If you change the framerate during runtime, which is not recommended, be sure to 
	 * update the time step.
	 */
	private float FIXED_TIMESTEP;
	/**
	 * Minimum remaining time to avoid box2d unstability caused by very small delta times.
	 * If remaining time to simulate is smaller than this, the rest of time will be added 
	 * to the last step instead of performing one more single step with only the small 
	 * delta time. If you change the framerate during runtime, which is not recommended, 
	 * be sure to update the minimum time step. 
	 */
	private float MINIMUM_TIMESTEP = 1.0f / 600.0f;
	/**
	 * Maximum number of steps per tick to avoid spiral of death.
	 */
	private final int MAXIMUM_NUMBER_OF_STEPS = 25;
	/**
	 * Internal, the frame time.
	 */
	private float _frameTime;
	/**
	 * Internal, the amoutn of steps performed.
	 */
	private int _stepsPerformed;
	/**
	 * Internal, the delta time.
	 */
	private float _deltaTime;
	/**
	 * Velocity iterations for the velocity constraint solver.
	 */
	protected int velocityIterations = 8; 
	/**
	 * Position iterations for the position constraint solver. 
	 */
	protected int positionIterations = 3;	
	
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
		// Apply the correct time step
		FIXED_TIMESTEP = 1f / FlxG.getFramerate();
		// Apply the correct minimum time step.
		MINIMUM_TIMESTEP = 1f / (FlxG.getFramerate() * 10f);
	}
		
	/**
	 * The main loop.
	 */
	@Override
	public void update()
	{
		_frameTime = FlxG.elapsed;
		_stepsPerformed = 0;
		while((_frameTime > 0.0) && (_stepsPerformed < MAXIMUM_NUMBER_OF_STEPS))
		{
			_deltaTime = FlxU.min(_frameTime, FIXED_TIMESTEP);
			_frameTime -= _deltaTime;
			if(_frameTime < MINIMUM_TIMESTEP)
			{
				_deltaTime += _frameTime;
				_frameTime = 0.0f;
			}
			B2FlxB.world.step(_deltaTime, velocityIterations, positionIterations);
			_stepsPerformed++;
		}
		B2FlxB.world.clearForces();
		
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
	}
}
