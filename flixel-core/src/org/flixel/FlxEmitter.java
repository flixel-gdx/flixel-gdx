package org.flixel;

import com.badlogic.gdx.utils.reflect.ClassReflection;

/**
 * <code>FlxEmitter</code> is a lightweight particle emitter.
 * It can be used for one-time explosions or for
 * continuous fx like rain and fire.  <code>FlxEmitter</code>
 * is not optimized or anything; all it does is launch
 * <code>FlxParticle</code> objects out at set intervals
 * by setting their positions and velocities accordingly.
 * It is easy to use and relatively efficient,
 * relying on <code>FlxGroup</code>'s RECYCLE POWERS.
 * 
 * @author Ka Wing Chin
 */
public class FlxEmitter extends FlxGroup
{
	/**
	 * The X position of the top left corner of the emitter in world space.
	 */
	public float x;
	/**
	 * The Y position of the top left corner of emitter in world space.
	 */
	public float y;
	/**
	 * The width of the emitter.  Particles can be randomly generated from anywhere within this box.
	 */
	public float width;
	/**
	 * The height of the emitter.  Particles can be randomly generated from anywhere within this box.
	 */
	public float height;
	/**
	 * The minimum possible velocity of a particle.
	 * The default value is (-100,-100).
	 */
	public FlxPoint minParticleSpeed;
	/**
	 * The maximum possible velocity of a particle.
	 * The default value is (100,100).
	 */
	public FlxPoint maxParticleSpeed;
	/**
	 * The X and Y drag component of particles launched from the emitter.
	 */
	public FlxPoint particleDrag;
	/**
	 * The minimum possible angular velocity of a particle.  The default value is -360.
	 * NOTE: rotating particles are more expensive to draw than non-rotating ones!
	 */
	public float minRotation;
	/**
	 * The maximum possible angular velocity of a particle.  The default value is 360.
	 * NOTE: rotating particles are more expensive to draw than non-rotating ones!
	 */
	public float maxRotation;
	/**
	 * Sets the <code>acceleration.y</code> member of each particle to this value on launch.
	 */
	public float gravity;
	/**
	 * Determines whether the emitter is currently emitting particles.
	 * It is totally safe to directly toggle this.
	 */
	public boolean on;
	/**
	 * How often a particle is emitted (if emitter is started with Explode == false).
	 */
	public float frequency;
	/**
	 * How long each particle lives once it is emitted.
	 * Set lifespan to 'zero' for particles to live forever.
	 */
	public float lifespan;
	/**
	 * How much each particle should bounce. 1 = full bounce, 0 = no bounce.
	 */
	public float bounce;
	/**
	 * Set your own particle class type here.
	 * Default is <code>FlxParticle</code>.
	 */
	public Class<? extends FlxParticle> particleClass;
	/**
	 * Internal helper for deciding how many particles to launch.
	 */
	protected int _quantity;
	/**
	 * Internal helper for the style of particle emission (all at once, or one at a time).
	 */
	protected boolean _explode;
	/**
	 * Internal helper for deciding when to launch particles or kill them.
	 */
	protected float _timer;
	/**
	 * Internal counter for figuring out how many particles to launch.
	 */
	protected int _counter;
	/**
	 * Internal point object, handy for reusing for memory mgmt purposes.
	 */
	protected FlxPoint _point;

	/**
	 * Creates a new <code>FlxEmitter</code> object at a specific position.
	 * Does NOT automatically generate or attach particles!
	 * 
	 * @param	X		The X position of the emitter.
	 * @param	Y		The Y position of the emitter.
	 * @param	Size	Optional, specifies a maximum capacity for this emitter.
	 */
	public FlxEmitter(float X, float Y, int Size)
	{
		super(Size);
		x = X;
		y = Y;
		width = 0;
		height = 0;
		minParticleSpeed = new FlxPoint(-100,-100);
		maxParticleSpeed = new FlxPoint(100,100);
		minRotation = -360;
		maxRotation = 360;
		gravity = 0;
		particleClass = null;
		particleDrag = new FlxPoint();
		frequency = 0.1f;
		lifespan = 3;
		bounce = 0;
		_quantity = 0;
		_counter = 0;
		_explode = true;
		on = false;
		_point = new FlxPoint();
	}

	/**
	 * Creates a new <code>FlxEmitter</code> object at a specific position.
	 * Does NOT automatically generate or attach particles!
	 * 
	 * @param	X		The X position of the emitter.
	 * @param	Y		The Y position of the emitter.
	 */
	public FlxEmitter(float X, float Y)
	{
		this(X, Y, 0);
	}

	/**
	 * Creates a new <code>FlxEmitter</code> object at a specific position.
	 * Does NOT automatically generate or attach particles!
	 * 
	 * @param	X		The X position of the emitter.
	 */
	public FlxEmitter(float X)
	{
		this(X, 0, 0);
	}

	/**
	 * Creates a new <code>FlxEmitter</code> object at a specific position.
	 * Does NOT automatically generate or attach particles!
	 */
	public FlxEmitter()
	{
		this(0, 0, 0);
	}

	/**
	 * Clean up memory.
	 */
	@Override
	public void destroy()
	{
		minParticleSpeed = null;
		maxParticleSpeed = null;
		particleDrag = null;
		particleClass = null;
		_point = null;
		super.destroy();
	}

	/**
	 * This function generates a new array of particle sprites to attach to the emitter.
	 * 
	 * @param	Graphics		If you opted to not pre-configure an array of FlxParticle objects, you can simply pass in a particle image or sprite sheet.
	 * @param	Quantity		The number of particles to generate when using the "create from image" option.
	 * @param	BakedRotations	How many frames of baked rotation to use (boosts performance).  Set to zero to not use baked rotations.
	 * @param	Multiple		Whether the image in the Graphics param is a single particle or a bunch of particles (if it's a bunch, they need to be square!).
	 * @param	Collide			Whether the particles should be flagged as not 'dead' (non-colliding particles are higher performance).  0 means no collisions, 0-1 controls scale of particle's bounding box.
	 * 
	 * @return	This FlxEmitter instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxEmitter makeParticles(String Graphics, int Quantity, int BakedRotations, boolean Multiple, float Collide)
	{
		setMaxSize(Quantity);

		int totalFrames = 1;
		if(Multiple)
		{
			FlxSprite sprite = new FlxSprite();
			sprite.loadGraphic(Graphics,true);
			totalFrames = sprite.getNumFrames();
			sprite.destroy();
		}

		int randomFrame;
		FlxParticle particle;
		int i = 0;
		while(i < Quantity)
		{
			if(particleClass == null)
				particle = new FlxParticle();
			else
			{
				try
				{
					particle = ClassReflection.newInstance(particleClass);
				}
				catch(Exception e)
				{
					throw new RuntimeException(e);
				}
			}
			if(Multiple)
			{
				randomFrame = (int) (FlxG.random()*totalFrames);
				if(BakedRotations > 0)
					particle.loadRotatedGraphic(Graphics,BakedRotations,randomFrame);
				else
				{
					particle.loadGraphic(Graphics,true);
					particle.setFrame(randomFrame);
				}
			}
			else
			{
				if(BakedRotations > 0)
					particle.loadRotatedGraphic(Graphics,BakedRotations);
				else
					particle.loadGraphic(Graphics);
			}
			if(Collide > 0)
			{
				particle.width *= Collide;
				particle.height *= Collide;
				particle.centerOffsets();
			}
			else
				particle.allowCollisions = FlxObject.NONE;
			particle.exists = false;
			add(particle);
			i++;
		}
		return this;
	}

	/**
	 * This function generates a new array of particle sprites to attach to the emitter.
	 * 
	 * @param	Graphics		If you opted to not pre-configure an array of FlxParticle objects, you can simply pass in a particle image or sprite sheet.
	 * @param	Quantity		The number of particles to generate when using the "create from image" option.
	 * @param	BakedRotations	How many frames of baked rotation to use (boosts performance).  Set to zero to not use baked rotations.
	 * @param	Multiple		Whether the image in the Graphics param is a single particle or a bunch of particles (if it's a bunch, they need to be square!).
	 * 
	 * @return	This FlxEmitter instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxEmitter makeParticles(String Graphics, int Quantity, int BakedRotations, boolean Multiple)
	{
		return makeParticles(Graphics, Quantity, BakedRotations, Multiple, 0.8f);
	}

	/**
	 * This function generates a new array of particle sprites to attach to the emitter.
	 * 
	 * @param	Graphics		If you opted to not pre-configure an array of FlxParticle objects, you can simply pass in a particle image or sprite sheet.
	 * @param	Quantity		The number of particles to generate when using the "create from image" option.
	 * @param	BakedRotations	How many frames of baked rotation to use (boosts performance).  Set to zero to not use baked rotations.
	 * 
	 * @return	This FlxEmitter instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxEmitter makeParticles(String Graphics, int Quantity, int BakedRotations)
	{
		return makeParticles(Graphics, Quantity, BakedRotations, false, 0.8f);
	}

	/**
	 * This function generates a new array of particle sprites to attach to the emitter.
	 * 
	 * @param	Graphics		If you opted to not pre-configure an array of FlxParticle objects, you can simply pass in a particle image or sprite sheet.
	 * @param	Quantity		The number of particles to generate when using the "create from image" option.
	 * 
	 * @return	This FlxEmitter instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxEmitter makeParticles(String Graphics, int Quantity)
	{
		return makeParticles(Graphics, Quantity, 16, false, 0.8f);
	}

	/**
	 * This function generates a new array of particle sprites to attach to the emitter.
	 * 
	 * @param	Graphics		If you opted to not pre-configure an array of FlxParticle objects, you can simply pass in a particle image or sprite sheet.
	 * 
	 * @return	This FlxEmitter instance (nice for chaining stuff together, if you're into that).
	 */
	public FlxEmitter makeParticles(String Graphics)
	{
		return makeParticles(Graphics, 50, 16, false, 0.8f);
	}

	/**
	 * Called automatically by the game loop, decides when to launch particles and when to "die".
	 */
	@Override
	public void update()
	{
		if(on)
		{
			if(_explode)
			{
				on = false;
				int i = 0;
				int l = _quantity;
				if((l <= 0) || (l > length))
					l = length;
				while(i < l)
				{
					emitParticle();
					i++;
				}
				_quantity = 0;
			}
			else
			{
				_timer += FlxG.elapsed;
				while((frequency > 0) && (_timer > frequency) && on)
				{
					_timer -= frequency;
					emitParticle();
					if((_quantity > 0) && (++_counter >= _quantity))
					{
						on = false;
						_quantity = 0;
					}
				}
			}
		}
		super.update();
	}

	/**
	 * Call this function to turn off all the particles and the emitter.
	 */
	@Override
	public void kill()
	{
		on = false;
		super.kill();
	}

	/**
	 * Call this function to start emitting particles.
	 * 
	 * @param	Explode		Whether the particles should all burst out at once.
	 * @param	Lifespan	How long each particle lives once emitted. 0 = forever.
	 * @param	Frequency	Ignored if Explode is set to true. Frequency is how often to emit a particle. 0 = never emit, 0.1 = 1 particle every 0.1 seconds, 5 = 1 particle every 5 seconds.
	 * @param	Quantity	How many particles to launch. 0 = "all of the particles".
	 */
	public void start(boolean Explode,float Lifespan,float Frequency,int Quantity)
	{
		revive();
		visible = true;
		on = true;

		_explode = Explode;
		lifespan = Lifespan;
		frequency = Frequency;
		_quantity += Quantity;

		_counter = 0;
		_timer = 0;
	}

	/**
	 * Call this function to start emitting particles.
	 * 
	 * @param	Explode		Whether the particles should all burst out at once.
	 * @param	Lifespan	How long each particle lives once emitted. 0 = forever.
	 * @param	Frequency	Ignored if Explode is set to true. Frequency is how often to emit a particle. 0 = never emit, 0.1 = 1 particle every 0.1 seconds, 5 = 1 particle every 5 seconds.
	 */
	public void start(boolean Explode,float Lifespan,float Frequency)
	{
		start(Explode,Lifespan,Frequency,0);
	}

	/**
	 * Call this function to start emitting particles.
	 * 
	 * @param	Explode		Whether the particles should all burst out at once.
	 * @param	Lifespan	How long each particle lives once emitted. 0 = forever.
	 */
	public void start(boolean Explode,float Lifespan)
	{
		start(Explode,Lifespan,0.1f,0);
	}

	/**
	 * Call this function to start emitting particles.
	 * 
	 * @param	Explode		Whether the particles should all burst out at once.
	 */
	public void start(boolean Explode)
	{
		start(Explode,0,0.1f,0);
	}

	/**
	 * Call this function to start emitting particles.
	 */
	public void start()
	{
		start(true,0,0.1f,0);
	}

	/**
	 * This function can be used both internally and externally to emit the next particle.
	 */
	public void emitParticle()
	{
		FlxParticle particle = (FlxParticle) recycle(particleClass);
		particle.lifespan = lifespan;
		particle.elasticity = bounce;
		particle.reset(x - ((int)particle.width>>1) + FlxG.random()*width, y - ((int)particle.height>>1) + FlxG.random()*height);
		particle.visible = true;

		if(minParticleSpeed.x != maxParticleSpeed.x)
			particle.velocity.x = minParticleSpeed.x + FlxG.random()*(maxParticleSpeed.x-minParticleSpeed.x);
		else
			particle.velocity.x = minParticleSpeed.x;
		if(minParticleSpeed.y != maxParticleSpeed.y)
			particle.velocity.y = minParticleSpeed.y + FlxG.random()*(maxParticleSpeed.y-minParticleSpeed.y);
		else
			particle.velocity.y = minParticleSpeed.y;
		particle.acceleration.y = gravity;

		if(minRotation != maxRotation)
			particle.angularVelocity = minRotation + FlxG.random()*(maxRotation-minRotation);
		else
			particle.angularVelocity = minRotation;
		if(particle.angularVelocity != 0)
			particle.angle = FlxG.random()*360-180;

		particle.drag.x = particleDrag.x;
		particle.drag.y = particleDrag.y;
		particle.onEmit();
	}

	/**
	 * A more compact way of setting the width and height of the emitter.
	 * 
	 * @param	Width	The desired width of the emitter (particles are spawned randomly within these dimensions).
	 * @param	Height	The desired height of the emitter.
	 */
	public void setSize(int Width,int Height)
	{
		width = Width;
		height = Height;
	}

	/**
	 * A more compact way of setting the X velocity range of the emitter.
	 * 
	 * @param	Min		The minimum value for this range.
	 * @param	Max		The maximum value for this range.
	 */
	public void setXSpeed(float Min,float Max)
	{
		minParticleSpeed.x = Min;
		maxParticleSpeed.x = Max;
	}

	/**
	 * A more compact way of setting the X velocity range of the emitter.
	 * 
	 * @param	Min		The minimum value for this range.
	 */
	public void setXSpeed(float Min)
	{
		setXSpeed(Min,0);
	}

	/**
	 * A more compact way of setting the X velocity range of the emitter.
	 */
	public void setXSpeed()
	{
		setXSpeed(0,0);
	}

	/**
	 * A more compact way of setting the Y velocity range of the emitter.
	 * 
	 * @param	Min		The minimum value for this range.
	 * @param	Max		The maximum value for this range.
	 */
	public void setYSpeed(float Min,float Max)
	{
		minParticleSpeed.y = Min;
		maxParticleSpeed.y = Max;
	}

	/**
	 * A more compact way of setting the Y velocity range of the emitter.
	 * 
	 * @param	Min		The minimum value for this range.
	 */
	public void setYSpeed(float Min)
	{
		setYSpeed(Min,0);
	}

	/**
	 * A more compact way of setting the Y velocity range of the emitter.
	 */
	public void setYSpeed()
	{
		setYSpeed(0,0);
	}

	/**
	 * A more compact way of setting the angular velocity constraints of the emitter.
	 * 
	 * @param	Min		The minimum value for this range.
	 * @param	Max		The maximum value for this range.
	 */
	public void setRotation(float Min,float Max)
	{
		minRotation = Min;
		maxRotation = Max;
	}

	/**
	 * A more compact way of setting the angular velocity constraints of the emitter.
	 * 
	 * @param	Min		The minimum value for this range.
	 */
	public void setRotation(float Min)
	{
		setRotation(Min,0);
	}

	/**
	 * A more compact way of setting the angular velocity constraints of the emitter.
	 */
	public void setRotation()
	{
		setRotation(0,0);
	}

	/**
	 * Change the emitter's midpoint to match the midpoint of a <code>FlxObject</code>.
	 * 
	 * @param	Object		The <code>FlxObject</code> that you want to sync up with.
	 */
	public void at(FlxObject Object)
	{
		Object.getMidpoint(_point);
		x = _point.x - ((int)width>>1);
		y = _point.y - ((int)height>>1);
	}
}
