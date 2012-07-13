package org.flixel.examples.particles;

import org.flixel.FlxGame;

/**
 * In games, "particles" and "particle emitters" refer to a whole 
 * class of behaviors that are usually used for special effects and 
 * flavor. The "emitter" is the source and manager of the actual 
 * "particle" objects that are spewed out and/or floating around. 
 * FlxParticle is just an extension of FlxSprite, and FlxEmitter 
 * is just an extension of FlxGroup, so a particle system in Flixel 
 * isn't really that different from any normal group of sprites. It just 
 * adds some special behavior for creating and launching 
 * particles, and the particles themselves have some optional, 
 * special behavior to bounce more believably in platformer 
 * situations. FlxEmitter also has built-in variables that let you 
 * specify velocity ranges, rotation speeds, gravity, collision 
 * behaviors, and more. 
 * 
 * @author Zachary Travit
 * @author Ka Wing Chin
 */
public class ParticleDemo extends FlxGame
{
	public ParticleDemo()
	{
		super(400, 300, PlayState.class, 1, 30, 30);
	}
}
