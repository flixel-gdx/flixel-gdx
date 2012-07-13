package org.flixel.examples.mode;

import org.flixel.*;
import org.flixel.event.AFlxCamera;
import org.flixel.event.AFlxG;

import com.badlogic.gdx.utils.Array;

public class PlayState extends FlxState
{
	protected String ImgTech = "examples/mode/pack:tech_tiles";
	protected String ImgDirtTop = "examples/mode/pack:dirt_top";
	protected String ImgDirt = "examples/mode/pack:dirt";
	protected String SndMode = "examples/mode/mode.mp3";
	protected String SndCount = "examples/mode/countdown.mp3";
	private String ImgGibs = "examples/mode/pack:gibs";
	private String ImgSpawnerGibs = "examples/mode/pack:spawner_gibs";
	private String ImgMiniFrame = "examples/mode/pack:miniframe";
	
	//major game object storage
	protected FlxGroup _blocks;
	protected FlxGroup _decorations;
	protected FlxGroup _bullets;
	protected Player _player;
	protected FlxGroup _enemies;
	protected FlxGroup _spawners;
	protected FlxGroup _enemyBullets;
	protected FlxEmitter _littleGibs;
	protected FlxEmitter _bigGibs;
	protected FlxGroup _hud;
	protected FlxGroup _gunjam;

	//meta groups, to help speed up collisions
	protected FlxGroup _objects;
	protected FlxGroup _hazards;

	//HUD/User Interface stuff
	protected FlxText _score;
	protected FlxText _score2;
	protected float _scoreTimer;
	protected float _jamTimer;

	//just to prevent weirdness during level transition
	protected boolean _fading;

	@Override
	public void create()
	{
		FlxG.mouse.hide();

		//Here we are creating a pool of 100 little metal bits that can be exploded.
		//We will recycle the crap out of these!
		_littleGibs = new FlxEmitter();
		_littleGibs.setXSpeed(-150,150);
		_littleGibs.setYSpeed(-200,0);
		_littleGibs.setRotation(-720,-720);
		_littleGibs.gravity = 350;
		_littleGibs.bounce = 0.5f;
		_littleGibs.makeParticles(ImgGibs,100,10,true,0.5f);

		//Next we create a smaller pool of larger metal bits for exploding.
		_bigGibs = new FlxEmitter();
		_bigGibs.setXSpeed(-200,200);
		_bigGibs.setYSpeed(-300,0);
		_bigGibs.setRotation(-720,-720);
		_bigGibs.gravity = 350;
		_bigGibs.bounce = 0.35f;
		_bigGibs.makeParticles(ImgSpawnerGibs,50,20,true,0.5f);

		//Then we'll set up the rest of our object groups or pools
		_blocks = new FlxGroup();
		_decorations = new FlxGroup();
		_enemies = new FlxGroup();
		_spawners = new FlxGroup();
		_hud = new FlxGroup();
		_enemyBullets = new FlxGroup();
		_bullets = new FlxGroup();

		//Now that we have references to the bullets and metal bits,
		//we can create the player object.
		_player = new Player(316,300,_bullets,_littleGibs);

		//This refers to a custom function down at the bottom of the file
		//that creates all our level geometry with a total size of 640x480.
		//This in turn calls buildRoom() a bunch of times, which in turn
		//is responsible for adding the spawners and spawn-cameras.
		generateLevel();

		//Add bots and spawners after we add blocks to the state,
		//so that they're drawn on top of the level, and so that
		//the bots are drawn on top of both the blocks + the spawners.
		add(_spawners);
		add(_littleGibs);
		add(_bigGibs);
		add(_blocks);
		add(_decorations);
		add(_enemies);

		//Then we add the player and set up the scrolling camera,
		//which will automatically set the boundaries of the world.
		add(_player);
		FlxG.camera.setBounds(0,0,640,640,true);
		FlxG.camera.follow(_player,FlxCamera.STYLE_PLATFORMER);

		//We add the bullets to the scene here,
		//so they're drawn on top of pretty much everything
		add(_enemyBullets);
		add(_bullets);
		add(_hud);

		//Finally we are going to sort things into a couple of helper groups.
		//We don't add these groups to the state, we just use them for collisions later!
		_hazards = new FlxGroup();
		_hazards.add(_enemyBullets);
		_hazards.add(_spawners);
		_hazards.add(_enemies);
		_objects = new FlxGroup();
		_objects.add(_enemyBullets);
		_objects.add(_bullets);
		_objects.add(_enemies);
		_objects.add(_player);
		_objects.add(_littleGibs);
		_objects.add(_bigGibs);

		//From here on out we are making objects for the HUD,
		//that is, the player score, number of spawners left, etc.
		//First, we'll create a text field for the current score
		_score = new FlxText(FlxG.width/4,0,FlxG.width/2);
		_score.setFormat(null,16,0xd8eba2,"center",0x131c1b);
		_hud.add(_score);
		if(FlxG.scores.size < 2)
		{
			FlxG.scores.add(0);
			FlxG.scores.add(0);
		}

		//Then for the player's highest and last scores
		if(FlxG.score > FlxG.scores.get(0))
			FlxG.scores.set(0, FlxG.score);
		if(FlxG.scores.get(0) != 0)
		{
			_score2 = new FlxText(FlxG.width/2,0,FlxG.width/2);
			_score2.setFormat(null,8,0xd8eba2,"right",_score.getShadow());
			_hud.add(_score2);
			_score2.setText("HIGHEST: "+FlxG.scores.get(0)+"\nLAST: "+FlxG.score);
		}
		FlxG.score = 0;
		_scoreTimer = 0;

		//Then we create the "gun jammed" notification
		_gunjam = new FlxGroup();
		_gunjam.add(new FlxSprite(0,FlxG.height-22).makeGraphic(FlxG.width,24,0xff131c1b));
		_gunjam.add(new FlxText(0,FlxG.height-22,FlxG.width,"GUN IS JAMMED").setFormat(null,16,0xd8eba2,"center"));
		_gunjam.visible = false;
		_hud.add(_gunjam);

		//After we add all the objects to the HUD, we can go through
		//and set any property we want on all the objects we added
		//with this sweet function.  In this case, we want to set
		//the scroll factors to zero, to make sure the HUD doesn't
		//wiggle around while we play.
		_hud.setAll("scrollFactor",new FlxPoint(0,0));
		_hud.setAll("cameras",new Array<FlxCamera>(new FlxCamera[]{FlxG.camera}));

		FlxG.playMusic(SndMode);
		FlxG.flash(0xff131c1b);
		_fading = false;

		//Debugger Watch examples
		FlxG.watch(_player,"x");
		FlxG.watch(_player,"y");
		FlxG.watch(FlxG.class,"score");
	}

	@Override
	public void destroy()
	{
		super.destroy();

		_blocks = null;
		_decorations = null;
		_bullets = null;
		_player = null;
		_enemies = null;
		_spawners = null;
		_enemyBullets = null;
		_littleGibs = null;
		_bigGibs = null;
		_hud = null;
		_gunjam = null;

		//meta groups, to help speed up collisions
		_objects = null;
		_hazards = null;

		//HUD/User Interface stuff
		_score = null;
		_score2 = null;
	}

	@Override
	public void update()
	{			
		//save off the current score and update the game state
		int oldScore = FlxG.score;
		super.update();

		//collisions with environment
		FlxG.collide(_blocks,_objects);
		FlxG.overlap(_hazards,_player,new AFlxG(){@Override public void onNotifyCallback(FlxObject Object1, FlxObject Object2) {overlapped(Object1, Object2);}});
		FlxG.overlap(_bullets,_hazards,new AFlxG(){@Override public void onNotifyCallback(FlxObject Object1, FlxObject Object2) {overlapped(Object1, Object2);}});

		//check to see if the player scored any points this frame
		boolean scoreChanged = oldScore != FlxG.score;

		//Jammed message
		if(FlxG.keys.justPressed("C") && _player.getFlickering())
		{
			_jamTimer = 1;
			_gunjam.visible = true;
		}
		if(_jamTimer > 0)
		{
			if(!_player.getFlickering())
				_jamTimer = 0;
			_jamTimer -= FlxG.elapsed;
			if(_jamTimer < 0)
				_gunjam.visible = false;
		}

		if(!_fading)
		{
			//Score + countdown stuffs
			if(scoreChanged)
				_scoreTimer = 2;
			_scoreTimer -= FlxG.elapsed;
			if(_scoreTimer < 0)
			{
				if(FlxG.score > 0)
				{
					if(FlxG.score > 100)
						FlxG.score -= 100;
					else
					{
						FlxG.score = 0;
						_player.kill();
					}
					_scoreTimer = 1;
					scoreChanged = true;

					//Play loud beeps if your score is low
					float volume = 0.35f;
					if(FlxG.score < 600)
						volume = 1.0f;
					FlxG.play(SndCount,volume);
				}
			}

			//Fade out to victory screen stuffs
			if(_spawners.countLiving() <= 0)
			{
				_fading = true;
				FlxG.fade(0xffd8eba2,3,new AFlxCamera(){@Override public void onFadeComplete(){onVictory();}});
			}
		}

		//actually update score text if it changed
		if(scoreChanged)
		{
			if(!_player.alive) FlxG.score = 0;
			_score.setText(String.valueOf(FlxG.score));
		}
	}

	//This is an overlap callback function, triggered by the calls to FlxU.overlap().
	protected void overlapped(FlxObject object1,FlxObject object2)
	{
		if((object1 instanceof EnemyBullet) || (object1 instanceof Bullet))
			object1.kill();
		object2.hurt(1);
	}

	//A FlxG.fade callback, like in MenuState.
	protected void onVictory()
	{
		FlxG.music.stop();
		FlxG.switchState(new VictoryState());
	}

	//These next two functions look crazy, but all they're doing is generating
	//the level structure and placing the enemy spawners.
	protected void generateLevel()
	{
		int r = 160;
		FlxTileblock b;

		//First, we create the walls, ceiling and floors:
		b = new FlxTileblock(0,0,640,16);
		b.loadTiles(ImgTech);
		_blocks.add(b);

		b = new FlxTileblock(0,16,16,640-16);
		b.loadTiles(ImgTech);
		_blocks.add(b);

		b = new FlxTileblock(640-16,16,16,640-16);
		b.loadTiles(ImgTech);
		_blocks.add(b);

		b = new FlxTileblock(16,640-24,640-32,8);
		b.loadTiles(ImgDirtTop);
		_blocks.add(b);

		b = new FlxTileblock(16,640-16,640-32,16);
		b.loadTiles(ImgDirt);
		_blocks.add(b);

		//Then we split the game world up into a 4x4 grid,
		//and generate some blocks in each area.  Some grid spaces
		//also get a spawner!
		buildRoom(r*0,r*0,true);
		buildRoom(r*1,r*0);
		buildRoom(r*2,r*0);
		buildRoom(r*3,r*0,true);
		buildRoom(r*0,r*1,true);
		buildRoom(r*1,r*1);
		buildRoom(r*2,r*1);
		buildRoom(r*3,r*1,true);
		buildRoom(r*0,r*2);
		buildRoom(r*1,r*2);
		buildRoom(r*2,r*2);
		buildRoom(r*3,r*2);
		buildRoom(r*0,r*3,true);
		buildRoom(r*1,r*3);
		buildRoom(r*2,r*3);
		buildRoom(r*3,r*3,true);
	}

	//Just plops down a spawner and some blocks - haphazard and crappy atm but functional!
	protected void buildRoom(int RX,int RY,boolean Spawners)
	{
		//first place the spawn point (if necessary)
		int rw = 20;
		int sx = 0;
		int sy = 0;
		if(Spawners)
		{
			sx = (int) (2+FlxG.random()*(rw-7));
			sy = (int) (2+FlxG.random()*(rw-7));
		}

		//then place a bunch of blocks
		int numBlocks = (int) (3+FlxG.random()*4);
		if(!Spawners) numBlocks++;
		int maxW = 10;
		int minW = 2;
		int maxH = 8;
		int minH = 1;
		int bx;
		int by;
		int bw;
		int bh;
		boolean check;
		for(int i = 0; i < numBlocks; i++)
		{
			do
			{
				//keep generating different specs if they overlap the spawner
				bw = (int) (minW + FlxG.random()*(maxW-minW));
				bh = (int) (minH + FlxG.random()*(maxH-minH));
				bx = (int) (-1 + FlxG.random()*(rw+1-bw));
				by = (int) (-1 + FlxG.random()*(rw+1-bh));
				if(Spawners)
					check = ((sx>bx+bw) || (sx+3<bx) || (sy>by+bh) || (sy+3<by));
				else
					check = true;
			} while(!check);

			FlxTileblock b;
			b = new FlxTileblock(RX+bx*8,RY+by*8,bw*8,bh*8);
			b.loadTiles(ImgTech);
			_blocks.add(b);

			//If the block has room, add some non-colliding "dirt" graphics for variety
			if((bw >= 4) && (bh >= 5))
			{
				b = new FlxTileblock(RX+bx*8+8,RY+by*8,bw*8-16,8);
				b.loadTiles(ImgDirtTop);
				_decorations.add(b);

				b = new FlxTileblock(RX+bx*8+8,RY+by*8+8,bw*8-16,bh*8-24);
				b.loadTiles(ImgDirt);
				_decorations.add(b);
			}
		}

		if(Spawners)
		{
			//Finally actually add the spawner
			Spawner sp = new Spawner(RX+sx*8,RY+sy*8,_bigGibs,_enemies,_enemyBullets,_littleGibs,_player); 
			_spawners.add(sp);

			//Then create a dedicated camera to watch the spawner
			_hud.add(new FlxSprite(3 + (_spawners.length-1)*16, 3, ImgMiniFrame));
			FlxCamera camera = new FlxCamera(10 + (_spawners.length-1)*32,10,24,24,1);
			camera.follow(sp);
			FlxG.addCamera(camera);
		}
	}
	
	//Just plops down a spawner and some blocks - haphazard and crappy atm but functional!
	protected void buildRoom(int RX,int RY)
	{
		buildRoom(RX, RY, false);
	}
}