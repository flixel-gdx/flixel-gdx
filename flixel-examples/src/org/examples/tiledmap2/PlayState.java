package org.examples.tiledmap2;

import org.flixel.FlxEmitter;
import org.flixel.FlxG;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.flixel.FlxTilemap;

import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObjectGroup;

public class PlayState extends FlxState
{

	private FlxTilemap _level;


	@Override
	public void create()
	{
		//Background
		FlxG.setBgColor(0xffacbcd7);
		FlxSprite decoration = new FlxSprite(256,159,Asset.ImgBG);
		decoration.moves = false;
		decoration.setSolid(false);
		add(decoration);
		add(new FlxText(32,36,96,"collision"));
		add(new FlxText(32,60,96,"DEMO"));
		
		//Add game objects
//		add(new Elevator(208,80,112));
//		add(new Pusher(96,208,56));
//		add(new Player(32,176));
//		add(new Crate(64,208));
//		add(new Crate(108,176));
//		add(new Crate(140,176));
//		add(new Crate(192,208));
//		add(new Crate(272,48));
		
		for(TiledObjectGroup group : Asset.map.objectGroups)
		{
			for(TiledObject object : group.objects)
			{
				// TODO: Draw sprites where objects occur
//				System.out.println("Object " + object.name + " x,y = " + object.x + "," + object.y + " width,height = "
//						+ object.width + "," + object.height);
				
				String name = object.name;
				if(name != null)
				{
					if(name.equals("crate"))
						add(new Crate(object.x,object.y));
					else if(name.equals("elevator"))
						add(new Elevator(object.x,object.y,object.height));
					else if(name.equals("pusher"))
						add(new Pusher(object.x,object.y,object.width));
					else if(name.equals("player"))
						add(new Player(object.x,object.y));
				}				
			}
		}
		
		
		
		//This is the thing that spews nuts and bolts
		FlxEmitter dispenser = new FlxEmitter(32,32,200);
		dispenser.setSize(8,40);
		dispenser.setXSpeed(100,240);
		dispenser.setYSpeed(-50,50);
		dispenser.gravity = 300;
		dispenser.bounce = 0.3f;
		dispenser.makeParticles(Asset.ImgGibs,100,16,true,0.8f);
		dispenser.start(false,10,0.035f);
		add(dispenser);
		
		
		

		/*int[] data = {
				16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,
				16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,9,12,12,12,12,12,12,2,16,16,16,16,9,12,12,12,12,12,12,2,16,
				16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,14,0,0,0,0,0,0,8,16,16,16,16,14,0,0,0,0,0,0,8,16,
				16,16,16,9,12,12,12,12,12,12,12,12,12,12,12,12,2,16,16,14,0,0,0,0,0,0,4,12,12,12,12,10,0,0,0,0,0,0,8,16,
				16,16,16,14,0,0,0,0,0,0,0,0,0,0,0,0,8,16,16,14,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,16,
				16,16,16,14,0,0,0,0,0,0,0,0,0,0,0,0,4,12,12,10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,16,
				16,16,16,14,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,16,
				16,16,16,14,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,16,
				16,16,16,14,0,0,0,0,0,0,0,0,0,0,0,0,7,15,15,13,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,16,
				16,16,16,14,0,0,0,0,0,0,0,0,0,0,0,0,8,16,16,14,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,16,
				16,16,16,14,0,0,0,0,0,0,0,0,0,0,0,0,8,16,16,5,15,13,0,0,0,0,0,0,0,0,0,0,7,15,15,15,15,15,3,16,
				16,16,16,14,0,0,0,0,0,0,0,0,0,0,0,0,8,16,16,16,16,14,0,0,0,0,0,0,0,0,0,0,8,16,16,16,16,16,16,16,
				16,16,16,5,15,15,15,15,15,15,15,15,15,15,15,15,3,16,16,16,16,14,0,0,0,0,0,0,0,0,0,0,8,16,16,16,16,16,16,16,
				16,16,16,16,16,9,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,10,0,0,0,0,0,0,0,0,0,0,8,16,16,16,16,16,16,16,
				16,16,16,16,16,14,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,16,16,16,16,16,16,16,
				16,9,12,12,12,10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,16,16,16,16,16,16,16,
				16,14,0,0,0,0,0,0,7,15,15,15,15,15,15,15,15,15,15,15,15,13,0,0,0,0,0,0,0,0,0,0,8,16,16,16,16,16,16,16,
				16,14,0,0,0,0,0,0,8,9,12,12,12,12,12,12,12,12,12,12,12,10,0,0,0,0,0,0,0,0,0,0,8,9,12,12,12,12,2,16,
				16,14,0,0,0,0,0,0,8,14,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,14,0,0,0,0,8,16,
				16,14,0,0,0,0,0,0,8,14,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,10,0,0,0,0,8,16,
				16,5,15,13,0,0,7,15,3,14,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,16,
				16,9,12,10,0,0,4,12,12,10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,16,
				16,14,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,13,0,0,0,0,8,16,
				16,14,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,14,0,0,0,0,8,16,
				16,14,0,0,0,0,0,0,0,0,0,0,7,15,15,15,15,15,15,13,0,0,0,0,0,0,0,0,0,0,0,0,8,5,15,15,15,15,3,16,
				16,14,0,0,0,0,0,0,0,0,0,0,4,12,12,12,12,12,12,10,0,0,0,0,0,0,0,0,0,0,0,0,8,16,16,16,16,16,16,16,
				16,5,15,15,15,13,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,16,16,16,16,16,16,16,
				16,16,16,16,16,14,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,16,16,16,16,16,16,16,
				16,16,16,16,16,5,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,15,3,16,16,16,16,16,16,16,
				16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16};*/
		_level = new FlxTilemap();
//		_level.loadMap(FlxTilemap.arrayToCSV(data, 40), Asset.ImgTiles);
//		_level.loadMap(FlxTilemap.array2DToCSV(Asset.map.layers.get(0).tiles), Asset.ImgTiles);
		_level.loadMap(FlxTilemap.tilemapToCSV(Asset.map, 0), Asset.ImgTiles);
		add(_level);
		
		
		
		
	}
	
	@Override
	public void destroy()
	{		
		super.destroy();
		_level = null;
//		_player = null;
	}
	
	
	@Override
	public void update()
	{
		super.update();
		FlxG.collide();
	}
}
