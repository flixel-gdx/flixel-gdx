package org.flixel;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FlxTileblock extends FlxSprite
{
	
	/**
	 * This is a basic "environment object" class, used to create simple walls and floors.
	 * It can be filled with a random selection of tiles to quickly add detail.
	 * 
	 * @author Ka Wing Chin
	 */
	public FlxTileblock(float X, float Y, int Width, int Height)
	{
		super(X, Y);
		makeGraphic(Width, Height, 0xFF000000, true);
		active = false;
		immovable = true;
	}
	
	//TODO: fix tileblock
	/**
	 * Fills the block with a randomly arranged selection of graphics from the image provided.
	 * 
	 * @param	TileGraphic 	The graphic class that contains the tiles that should fill this block.
	 * @param	TileWidth		The width of a single tile in the graphic.
	 * @param	TileHeight		The height of a single tile in the graphic.
	 * @param	Empties			The number of "empty" tiles to add to the auto-fill algorithm (e.g. 8 tiles + 4 empties = 1/3 of block will be open holes).
	 */
	public FlxTileblock loadTiles(TextureRegion TileGraphic, int TileWidth, int TileHeight, int Empties)
	{
		if(TileGraphic == null)
			return this;
		
		//First create a tile brush
		FlxSprite sprite = new FlxSprite().loadGraphic(TileGraphic,true,TileWidth,TileHeight);
		int spriteWidth = sprite.width;
		int spriteHeight = sprite.height;
		int total = sprite.frames + Empties;
		
		//Then prep the "canvas" as it were (just doublechecking that the size is on tile boundaries)
		boolean regen = false;
		if(width % sprite.width != 0)
		{
			width = (width/spriteWidth+1)*spriteWidth;
			regen = true;
		}
		if(height % sprite.height != 0)
		{
			height = (height/spriteHeight+1)*spriteHeight;
			regen = true;
		}
		if(regen)
			makeGraphic(width,height,0xFFFFFFFF,true);
//		else
//			this.fill(0);
		
		//Stamp random tiles onto the canvas
		int row = 0;
		int column;
		int destinationX;
		int destinationY = 0;
		int widthInTiles = width/spriteWidth;
		int heightInTiles = height/spriteHeight;
		while(row < heightInTiles)
		{
			destinationX = 0;
			column = 0;
			while(column < widthInTiles)
			{
				if(FlxG.random()*total > Empties)
				{
					sprite.randomFrame();
					sprite.drawFrame();
					stamp(sprite,destinationX,destinationY);
				}
				destinationX += spriteWidth;
				column++;
			}
			destinationY += spriteHeight;
			row++;
		}
		
		return this;
	}
	
	/**
	 * Fills the block with a randomly arranged selection of graphics from the image provided.
	 * 
	 * @param	TileGraphic 	The graphic class that contains the tiles that should fill this block.
	 * @param	TileWidth		The width of a single tile in the graphic.
	 * @param	TileHeight		The height of a single tile in the graphic.
	 * @param	Empties			The number of "empty" tiles to add to the auto-fill algorithm (e.g. 8 tiles + 4 empties = 1/3 of block will be open holes).
	 */
	public FlxTileblock loadTiles(TextureRegion TileGraphic, int TileWidth, int TileHeight)
	{
		return loadTiles(TileGraphic, TileWidth, TileHeight, 0);
	}
	
	/**
	 * Fills the block with a randomly arranged selection of graphics from the image provided.
	 * 
	 * @param	TileGraphic 	The graphic class that contains the tiles that should fill this block.
	 * @param	TileWidth		The width of a single tile in the graphic.
	 * @param	TileHeight		The height of a single tile in the graphic.
	 */
	public FlxTileblock loadTiles(TextureRegion TileGraphic, int TileWidth)
	{
		return loadTiles(TileGraphic, TileWidth, 0, 0);
	}
	
	/**
	 * Fills the block with a randomly arranged selection of graphics from the image provided.
	 * 
	 * @param	TileGraphic 	The graphic class that contains the tiles that should fill this block.
	 */
	public FlxTileblock loadTiles(TextureRegion TileGraphic)
	{
		return loadTiles(TileGraphic, 0, 0, 0);
	}
}
