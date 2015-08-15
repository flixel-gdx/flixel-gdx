package org.flixel;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.TextureData;

/**
 * This is a basic "environment object" class, used to create simple walls and floors.
 * It can be filled with a random selection of tiles to quickly add detail.
 * 
 * @author Ka Wing Chin
 * @author Thomas Weston
 */
public class FlxTileblock extends FlxSprite
{
	/**
	 * Creates a new <code>FlxBlock</code> object with the specified position and size.
	 * 
	 * @param	X			The X position of the block.
	 * @param	Y			The Y position of the block.
	 * @param	Width		The width of the block.
	 * @param	Height		The height of the block.
	 */
	public FlxTileblock(float X,float Y,int Width,int Height)
	{
		super(X,Y);
		makeGraphic(Width,Height,0,true);
		active = false;
		immovable = true;
		moves = false;
	}

	/**
	 * Fills the block with a randomly arranged selection of graphics from the image provided.
	 * 
	 * @param	TileGraphic		The graphic class that contains the tiles that should fill this block.
	 * @param	TileWidth		The width of a single tile in the graphic.
	 * @param	TileHeight		The height of a single tile in the graphic.
	 * @param	Empties			The number of "empty" tiles to add to the auto-fill algorithm (e.g. 8 tiles + 4 empties = 1/3 of block will be open holes).
	 */
	public FlxTileblock loadTiles(String TileGraphic,int TileWidth,int TileHeight,int Empties)
	{
		if(TileGraphic == null)
			return this;

		//First create a tile brush
		FlxSprite sprite = new FlxSprite().loadGraphic(TileGraphic,true,false,TileWidth,TileHeight);
		int spriteWidth = (int)sprite.width;
		int spriteHeight = (int)sprite.height;
		int total = sprite.getNumFrames() + Empties;

		//Then prep the "canvas" as it were (just doublechecking that the size is on tile boundaries)
		boolean regen = false;
		if(width % sprite.width != 0)
		{
			width = (int)(width/spriteWidth+1)*spriteWidth;
			regen = true;
		}
		if(height % sprite.height != 0)
		{
			height = (int)(height/spriteHeight+1)*spriteHeight;
			regen = true;
		}
		if(regen)
			makeGraphic((int)width,(int)height,0,true);
		else
			this.fill(0);

		TextureData brushTextureData = sprite.framePixels.getTexture().getTextureData();

		if(!brushTextureData.isPrepared())
			brushTextureData.prepare();

		Pixmap brushPixmap = brushTextureData.consumePixmap();

		//Stamp random tiles onto the canvas
		int row = 0;
		int column;
		int destinationX;
		int destinationY = 0;
		int widthInTiles = (int)(width/spriteWidth);
		int heightInTiles = (int)(height/spriteHeight);
		while(row < heightInTiles)
		{
			destinationX = 0;
			column = 0;
			while(column < widthInTiles)
			{
				if(FlxG.random() * total > Empties)
				{
					sprite.randomFrame();
					sprite.drawFrame();
					stamp(brushPixmap,sprite.framePixels.getRegionX(),sprite.framePixels.getRegionY()-sprite.frameHeight,sprite.frameWidth,sprite.frameHeight,destinationX+_pixels.getRegionX(),destinationY+_pixels.getRegionY());
				}
				destinationX += spriteWidth;
				column++;
			}
			destinationY += spriteHeight;
			row++;
		}

		if(brushTextureData.disposePixmap())
			brushPixmap.dispose();

		return this;
	}

	/**
	 * Fills the block with a randomly arranged selection of graphics from the image provided.
	 * 
	 * @param	TileGraphic		The graphic class that contains the tiles that should fill this block.
	 * @param	TileWidth		The width of a single tile in the graphic.
	 * @param	TileHeight		The height of a single tile in the graphic.
	 */
	public FlxTileblock loadTiles(String TileGraphic,int TileWidth,int TileHeight)
	{
		return loadTiles(TileGraphic,TileWidth,TileHeight,0);
	}

	/**
	 * Fills the block with a randomly arranged selection of graphics from the image provided.
	 * 
	 * @param	TileGraphic		The graphic class that contains the tiles that should fill this block.
	 * @param	TileWidth		The width of a single tile in the graphic.
	 */
	public FlxTileblock loadTiles(String TileGraphic,int TileWidth)
	{
		return loadTiles(TileGraphic,TileWidth,0,0);
	}

	/**
	 * Fills the block with a randomly arranged selection of graphics from the image provided.
	 * 
	 * @param	TileGraphic		The graphic class that contains the tiles that should fill this block.
	 */
	public FlxTileblock loadTiles(String TileGraphic)
	{
		return loadTiles(TileGraphic,0,0,0);
	}
}
