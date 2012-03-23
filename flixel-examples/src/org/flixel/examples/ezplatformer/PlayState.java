package org.flixel.examples.ezplatformer;

import org.flixel.FlxG;
import org.flixel.FlxGroup;
import org.flixel.FlxObject;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.flixel.FlxTilemap;
import org.flixel.event.AFlxG;

public class PlayState extends FlxState
{
	public FlxTilemap level;
	public FlxSprite exit;
	public FlxGroup coins;
	public FlxSprite player;
	public FlxText score;
	public FlxText status;
	
	@Override
	public void create()
	{
		// Background color
		FlxG.setBgColor(0xFFAAAAAA);
		
		//Design your platformer level with 1s and 0s (at 40x30 to fill 320x240 screen)		
		int[] data = {
			1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
			1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
			1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
			1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
			1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
			1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
			1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1,
			1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1,
			1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
			1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1,
			1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1,
			1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
			1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1,
			1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1,
			1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
			1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1,
			1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1,
			1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
			1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1,
			1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1,
			1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1,
			1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
			1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
			1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
			1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
			1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
			1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
			1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 1,
			1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1,
			1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
		
		//Create a new tilemap using our level data
		level = new FlxTilemap();
		level.loadMap(FlxTilemap.arrayToCSV(data, 40), Asset.ImgAuto, 0, 0, FlxTilemap.AUTO);
		add(level);
		
		//Create the level exit, a dark gray box that is hidden at first
		exit = new FlxSprite(35*8+1,25*8);
		exit.makeGraphic(14,16,0xff3f3f3f);
		exit.exists = false;
		add(exit);
		
		//Create coins to collect (see createCoin() function below for more info)
		coins = new FlxGroup();
		//Top left coins
		createCoin(18,4);
		createCoin(12,4);
		createCoin(9,4);
		createCoin(8,11);
		createCoin(1,7);
		createCoin(3,4);
		createCoin(5,2);
		createCoin(15,11);
		createCoin(16,11);
		
		//Bottom left coins
		createCoin(3,16);
		createCoin(4,16);
		createCoin(1,23);
		createCoin(2,23);
		createCoin(3,23);
		createCoin(4,23);
		createCoin(5,23);
		createCoin(12,26);
		createCoin(13,26);
		createCoin(17,20);
		createCoin(18,20);
		
		//Top right coins
		createCoin(21,4);
		createCoin(26,2);
		createCoin(29,2);
		createCoin(31,5);
		createCoin(34,5);
		createCoin(36,8);
		createCoin(33,11);
		createCoin(31,11);
		createCoin(29,11);
		createCoin(27,11);
		createCoin(25,11);
		createCoin(36,14);
		
		//Bottom right coins
		createCoin(38,17);
		createCoin(33,17);
		createCoin(28,19);
		createCoin(25,20);
		createCoin(18,26);
		createCoin(22,26);
		createCoin(26,26);
		createCoin(30,26);

		add(coins);
		
		//Create player (a red box)
		player = new FlxSprite(FlxG.width / 2 - 5);
		player.makeGraphic(10,12,0xffaa1111);
		player.maxVelocity.x = 80;
		player.maxVelocity.y = 200;
		player.acceleration.y = 200;
		player.drag.x = player.maxVelocity.x*4;
		add(player);		
		
		score = new FlxText(2,2,100, "SCORE: ");
		score.setShadow(0xff000000);
		score.setText("SCORE: "+(coins.countDead()*100));
		add(score);
	
		
		status = new FlxText(FlxG.width-160-2,2,160);
		status.setShadow(0xff000000);
		status.setAlignment("right");
		switch(FlxG.score)
		{
			case 0: status.setText("Collect coins."); break;
			case 1: status.setText("Aww, you died!"); break;
		}
		add(status);
	}
	
	//creates a new coin located on the specified tile
	public void createCoin(int X,int Y)
	{
		FlxSprite coin = new FlxSprite(X*8+3,Y*8+2);
		coin.makeGraphic(2,4,0xffffff00);
		coins.add(coin);
	}
	
	@Override
	public void update()
	{
		//Player movement and controls
		player.acceleration.x = 0;
		if(FlxG.keys.LEFT)
			player.acceleration.x = -player.maxVelocity.x*4;
		if(FlxG.keys.RIGHT)
			player.acceleration.x = player.maxVelocity.x*4;
		if(FlxG.keys.justPressed("SPACE") && player.isTouching(FlxObject.FLOOR))
			player.velocity.y = -player.maxVelocity.y/2;
		
		//Updates all the objects appropriately
		super.update();
		
		//Check if player collected a coin or coins this frame
		FlxG.overlap(coins,player,getCoin);
		
		//Check to see if the player touched the exit door this frame
		FlxG.overlap(exit,player,win);

		FlxG.collide(level,player);
		
		//Check for player lose conditions
		if(player.y > FlxG.height)
		{
			FlxG.score = 1; //sets status.text to "Aww, you died!"
			FlxG.resetState();
		}
	}
	
	//Called whenever the player touches a coin
	AFlxG getCoin = new AFlxG()
	{
		@Override
		public void onNotifyCallback(FlxObject Coin, FlxObject Player)
		{						
			Coin.kill();
			score.setText("SCORE: "+(coins.countDead()*100));
			if(coins.countLiving() == 0)
			{
				status.setText("Find the exit.");
				exit.exists = true;
			}
		}
	};
	
	
	//Called whenever the player touches the exit
	AFlxG win = new AFlxG()
	{
		@Override
		public void onNotifyCallback(FlxObject Exit, FlxObject Player)
		{
			status.setText("Yay, you won!");
			score.setText("SCORE: 5000");
			Player.kill();
		}
	};
}
