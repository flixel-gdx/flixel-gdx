package org.examples.tiledmap;

import org.flixel.FlxG;
import org.flixel.FlxGroup;
import org.flixel.FlxObject;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.flixel.FlxTilemap;
import org.flixel.event.AFlxG;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class PlayState extends FlxState
{
	private FlxTilemap _level;
	private FlxSprite _player;
	private FlxSprite _exit;
	private FlxGroup _coins;
	private FlxText _score;
	private FlxText _status;
	private static int _finished;
	
	@Override
	public void create()
	{
		// Background color
		FlxG.setBgColor(0xFFFFAAAA);
		
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
		_level = new FlxTilemap();
		_level.loadMap(FlxTilemap.arrayToCSV(data, 40), Asset.tiles, 8, 8, FlxTilemap.AUTO);
		add(_level);
		
		//Create the level exit, a dark gray box that is hidden at first
		_exit = new FlxSprite(35*8+1,25*8);
		_exit.makeGraphic(14,16,0xff3f3f3f);
		_exit.exists = false;
		add(_exit);
		
		//Create coins to collect (see createCoin() function below for more info)
		_coins = new FlxGroup(40);
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

		add(_coins);
		
		//Create player (a red box)
		_player = new FlxSprite(160 - 5);
		_player.makeGraphic(7,8,0xff0000FF);
		_player.setAlpha(0.3f);
		_player.maxVelocity.x = 80;
		_player.maxVelocity.y = 200;
		_player.acceleration.y = 200;
		_player.drag.x = _player.maxVelocity.x*4;
		add(_player);		
		
//		FlxG.camera.follow(_player);
		
		_score = new FlxText(2,2,100, "SCORE: ");
		_score.setText("SCORE: "+(_coins.countDead()*100));
		add(_score);
	
		
		_status = new FlxText(FlxG.width-160-2,2,160, "");
		switch(_finished)
		{
			case 0: _status.setText("Collect coins."); break;
			case 1: _status.setText("Aww, you died!"); break;
		}
		add(_status);
	}
	
	//creates a new coin located on the specified tile
	public void createCoin(int X,int Y)
	{
		FlxSprite coin = new FlxSprite(X*8+3,Y*8+2);
		coin.makeGraphic(2,4,0xffffff00);
		_coins.add(coin);
	}
	
	@Override
	public void update()
	{
		//Player movement and controls
		_player.acceleration.x = 0;
		if(Gdx.input.isKeyPressed(Keys.LEFT))
			_player.acceleration.x = -_player.maxVelocity.x*4;
		if(Gdx.input.isKeyPressed(Keys.RIGHT))
			_player.acceleration.x = _player.maxVelocity.x*4;
		if(Gdx.input.isKeyPressed(Keys.SPACE) && _player.isTouching(FlxObject.FLOOR))
			_player.velocity.y = -_player.maxVelocity.y/2;
		
		//Updates all the objects appropriately
		super.update();
		
		//Check if player collected a coin or coins this frame
		FlxG.overlap(_coins,_player,getCoin);
		
		//Check to see if the player touched the exit door this frame
		FlxG.overlap(_exit,_player,win);

		FlxG.collide(_level, _player);
		
		//Check for player lose conditions
		if(_player.y > FlxG.height)
		{
			_finished = 1; //sets status.text to "Aww, you died!"
			FlxG.resetState();
		}
	}
	
	
	AFlxG getCoin = new AFlxG()
	{
		@Override
		public void onNotifyCallback(FlxObject Coin, FlxObject Player)
		{						
			Coin.kill();
			_score.setText("SCORE: "+(_coins.countDead()*100));
			if(_coins.countLiving() == 0)
			{
				_status.setText("Find the exit.");
				_exit.exists = true;
			}
		}
	};
	
	
	//Called whenever the player touches the exit
	AFlxG win = new AFlxG()
	{
		@Override
		public void onNotifyCallback(FlxObject Exit, FlxObject Player)
		{
			_status.setText("Yay, you won!");
			_score.setText("SCORE: 5000");
			Player.kill();
		}
	};
}
