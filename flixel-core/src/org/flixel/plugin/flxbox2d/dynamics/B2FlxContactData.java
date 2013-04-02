package org.flixel.plugin.flxbox2d.dynamics;

import org.flixel.plugin.flxbox2d.collision.shapes.B2FlxShape;

/**
 *
 * @author Ka Wing Chin
 */
public class B2FlxContactData
{
	public final int SINGLE = 0;
	public final int GROUP = 1;
	public final int MIX_A = 2;
	public final int MIX_B = 3;
	
	public int type;
	public int categoryA;
	public int categoryB;
	public B2FlxShape spriteA;
	public B2FlxShape spriteB;
	public B2FlxListener listener;
	
	public B2FlxContactData(B2FlxShape spriteA, B2FlxShape spriteB, B2FlxListener listener)
	{
		this.spriteA = spriteA;
		this.spriteB = spriteB;
		this.listener = listener;
		type = SINGLE;
	}

	public B2FlxContactData(int categoryA, int categoryB, B2FlxListener listener)
	{
		this.categoryA = categoryA;
		this.categoryB = categoryB;
		this.listener = listener;
		type = GROUP;
	}
	
	public B2FlxContactData(B2FlxShape spriteA, int categoryB, B2FlxListener listener)
	{
		this.spriteA = spriteA;
		this.categoryB = categoryB;
		this.listener = listener;
		type = MIX_A;
	}
	
	public B2FlxContactData(int categoryA, B2FlxShape spriteB, B2FlxListener listener)
	{
		this.categoryA = categoryA;
		this.spriteB = spriteB;
		this.listener = listener;
		type = MIX_B;
	}
}

