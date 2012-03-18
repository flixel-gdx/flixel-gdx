package org.examples.tiledmap2;

import org.flixel.FlxG;
import org.flixel.FlxSprite;

import com.badlogic.gdx.Input.Keys;

public class Player extends FlxSprite
{

	public Player(float X, float Y)
	{
		super(X, Y);	
		loadGraphic(Asset.ImgPlayer,true);
		maxVelocity.x = 100;			//walking speed
		acceleration.y = 400;			//gravity
		drag.x = maxVelocity.x*4;		//deceleration (sliding to a stop)
		
		//tweak the bounding box for better feel
		width = 8;
		height = 10;
		offset.x = 3;
		offset.y = 3;
		
		addAnimation("idle",new int[]{0},0,false);
		addAnimation("walk",new int[]{1,2,3,0},10,true);
		addAnimation("walk_back",new int[]{3,2,1,0},10,true);
		addAnimation("flail",new int[]{1,2,3,0},18,true);
		addAnimation("jump",new int[]{4},0,false);
	}
	
	@Override
	public void update()
	{
		//Smooth slidey walking controls
		acceleration.x = 0;
		if(FlxG.keys.pressed(Keys.LEFT))
			acceleration.x -= drag.x;
		if(FlxG.keys.pressed(Keys.RIGHT))
			acceleration.x += drag.x;
		
		if(isTouching(FLOOR))
		{
			//Jump controls			
			if(FlxG.keys.justPressed(Keys.SPACE))
			{
				velocity.y = -acceleration.y*0.51f;
				play("jump");
			}//Animations
			else if(velocity.x > 0)
				play("walk");
			else if(velocity.x < 0)
				play("walk_back");
			else
				play("idle");
		}
		else if(velocity.y < 0)
			play("jump");
		else
			play("flail");
		
		//Default object physics update
		super.update();
	}
}
