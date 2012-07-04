package org.flixel.examples.tiledmap2;

import org.flixel.FlxG;
import org.flixel.FlxSprite;
import org.flixel.event.AFlxButton;

import com.badlogic.gdx.Input.Keys;

public class Player extends FlxSprite
{
	public AFlxButton jump;
	public AFlxButton left;
	public AFlxButton right;

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
		
		jump = new AFlxButton(){@Override public void onDown(){jump();}};
		left = new AFlxButton()
		{
			@Override	public void onPressed(){moveLeft();}
			@Override	public void onUp(){acceleration.x=0;}
			@Override	public void onOut(){acceleration.x=0;}
		};
		right = new AFlxButton()
		{
			@Override 	public void onPressed(){moveRight();}
			@Override	public void onUp(){acceleration.x=0;}
			@Override	public void onOut(){acceleration.x=0;}
		};
	}
	

	@Override
	public void update()
	{
		//Smooth slidey walking controls
		//acceleration.x = 0;
		if(FlxG.keys.pressed(Keys.LEFT))
			moveLeft();
		if(FlxG.keys.pressed(Keys.RIGHT))
			moveRight();
		if(FlxG.keys.justReleased(Keys.LEFT) || FlxG.keys.justReleased(Keys.RIGHT))
			acceleration.x = 0;
		
		if(isTouching(FLOOR))
		{
			//Jump controls
			if(FlxG.keys.justPressed(Keys.SPACE))
			{
				jump();
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
	
	

	private void moveRight()
	{
		acceleration.x = 0;
		acceleration.x += drag.x;
	}

	private void moveLeft()
	{
		acceleration.x = 0;
		acceleration.x -= drag.x;		
	}
	
	private void jump()
	{
		if(isTouching(FLOOR))
		{
			velocity.y = -acceleration.y*0.51f;
			play("jump");
		}
	}
}
