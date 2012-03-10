package org.flixel;


import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * This is a basic "environment object" class, used to create simple walls and floors.
 * It can be filled with a random selection of tiles to quickly add detail.
 * 
 * @author Ka Wing Chin
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
	public FlxTileblock(float X, float Y, int Width, int Height)
	{
		super(X, Y);
		makeGraphic(Width,Height,0,true);		
		active = false;
		immovable = true;
	}
	
	
	/**
	 * Fills the block with a randomly arranged selection of graphics from the image provided.
	 * 
	 * @param	TileGraphic 	The graphic class that contains the tiles that should fill this block.
	 * @param	TileWidth		The width of a single tile in the graphic.
	 * @param	TileHeight		The height of a single tile in the graphic.
	 * @param	RegionWidth		The width of the region.
	 * @param	RegionHeight	The height of the region.
	 * @param	Empties			The number of "empty" tiles to add to the auto-fill algorithm (e.g. 8 tiles + 4 empties = 1/3 of block will be open holes).
	 */
	public FlxTileblock loadTiles(FileHandle TileGraphic, int TileWidth, int TileHeight, int RegionWidth, int RegionHeight, int Empties)
	{
		if(TileGraphic == null)
			return this;

		//First create a tile brush
		FlxSprite sprite = new FlxSprite().loadGraphic(new TextureRegion(new Texture(TileGraphic), RegionWidth, RegionHeight),true,TileWidth,TileHeight);		
		int spriteWidth = sprite.width;
		int spriteHeight = sprite.height;
		int total = sprite.frames + Empties;
					
		//Stamp random tiles onto the canvas
		int row = 0;
		int column = 0;
		int destinationX;
		int destinationY = 0;
		int widthInTiles = width/spriteWidth;
		int heightInTiles = height/spriteHeight;		
		FlxG.log(widthInTiles);
		FlxG.log(heightInTiles);
		Pixmap pix = new Pixmap(TileGraphic);
		Pixmap pixD = new Pixmap(FlxU.ceilPowerOfTwo(width), FlxU.ceilPowerOfTwo(height), Pixmap.Format.RGBA8888);
		while(row < heightInTiles)
		{
			destinationX = 0;
			column = 0;
			while(column < widthInTiles)
			{
				if(FlxG.random()*total > Empties)
				{
					sprite.randomFrame();
					pixD.drawPixmap(pix, destinationX, destinationY, sprite._curIndex*TileWidth, 0, TileWidth, TileHeight);					
				}
				destinationX += spriteWidth;
				column++;
			}
			destinationY += spriteHeight;
			row++;
		}
		
		framePixels = new Sprite(new Texture(pixD));
		framePixels.flip(false, true);		
		
		pix.dispose();
		pixD.dispose();
		
		return this;
	}
	
	/**
	 * Fills the block with a randomly arranged selection of graphics from the image provided.
	 * 
	 * @param	TileGraphic 	The graphic class that contains the tiles that should fill this block.
	 * @param	TileWidth		The width of a single tile in the graphic.
	 * @param	TileHeight		The height of a single tile in the graphic.
	 * @param	RegionWidth		The width of the region.
	 * @param	RegionHeight	The height of the region.
	 */
	public FlxTileblock loadTiles(FileHandle TileGraphic, int TileWidth, int TileHeight, int RegionWidth, int RegionHeight)
	{
		return loadTiles(TileGraphic, TileWidth, TileHeight, RegionWidth, RegionHeight, 0);
	}
}
