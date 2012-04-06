package org.flixel.examples.mode;

import org.flixel.*;

public class EnemyBullet extends FlxSprite
{
	/*
	[Embed(source="data/bot_bullet.png")] private var ImgBullet:Class;
	[Embed(source="data/jump.mp3")] private var SndHit:Class;
	[Embed(source="data/enemy.mp3")] private var SndShoot:Class;
	*/
	
	public float speed;

	public EnemyBullet()
	{
		super();
		loadGraphic(Asset.ImgBotBullet,true);
		addAnimation("idle",new int[]{0, 1}, 50);
		addAnimation("poof",new int[]{2, 3, 4}, 50, false);
		speed = 120;
	}

	@Override
	public void update()
	{
		if(!alive)
		{
			if(finished)
				exists = false;
		}
		else if(touching > NONE)
			kill();
	}

	@Override
	public void kill()
	{
		if(!alive)
			return;
		velocity.x = 0;
		velocity.y = 0;
		if(onScreen())
			FlxG.play(Asset.SndJump);
		alive = false;
		setSolid(false);
		play("poof");
	}

	public void shoot(FlxPoint Location, float Angle)
	{
		FlxG.play(Asset.SndBotShoot,0.5f);
		super.reset(Location.x-width/2,Location.y-height/2);
		FlxU.rotatePoint(0,(int) speed,0,0,Angle,_point);
		velocity.x = _point.x;
		velocity.y = _point.y;
		setSolid(true);
		play("idle");
	}
}