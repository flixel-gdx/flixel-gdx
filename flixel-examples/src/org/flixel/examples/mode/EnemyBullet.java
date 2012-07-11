package org.flixel.examples.mode;

import org.flixel.*;

public class EnemyBullet extends FlxSprite
{
	//private String ImgBullet = "examples/mode/bot_bullet.png";
	private String SndHit = "examples/mode/jump.mp3";
	private String SndShoot = "examples/mode/enemy.mp3";
	
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
			FlxG.play(SndHit);
		alive = false;
		setSolid(false);
		play("poof");
	}

	public void shoot(FlxPoint Location, float Angle)
	{
		FlxG.play(SndShoot,0.5f);
		super.reset(Location.x-width/2,Location.y-height/2);
		FlxU.rotatePoint(0,(int) speed,0,0,Angle,_point);
		velocity.x = _point.x;
		velocity.y = _point.y;
		setSolid(true);
		play("idle");
	}
}