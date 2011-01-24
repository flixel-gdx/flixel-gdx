package org.flixel;

/**
 * This is the basic "environment object" class, used to create simple walls and floors.
 * It can be filled with a random selection of tiles to quickly add detail.
 */
public class FlxTileblock extends FlxSprite
{
	public FlxTileblock(int X, int Y, int Width, int Height)
	{
		super(X, Y);
		createGraphic(Width, Height, 0, true);
		setFixed(true);
	}

	/**
	 * Fills the block with a randomly arranged selection of graphics from the
	 * image provided.
	 * 
	 * @param TileGraphic The graphic class that contains the tiles that should
	 *        fill this block.
	 * @param Empties The number of "empty" tiles to add to the auto-fill
	 *        algorithm (e.g. 8 tiles + 4 empties = 1/3 of block will be open
	 *        holes).
	 */
	public FlxTileblock loadTiles(Integer TileGraphic, int TileWidth, int TileHeight, int Empties)
	{
		if(TileGraphic == null)
			return this;

		// First create a tile brush
		FlxSprite s = new FlxSprite().loadGraphic(TileGraphic, true, false, TileWidth, TileHeight);
		int sw = s.width;
		int sh = s.height;
		int total = s.frames + Empties;

		// Then prep the "canvas" as it were (just doublechecking that the size is on tile boundaries)
		boolean regen = false;
		if(width % s.width != 0)
		{
			width = (width / sw + 1) * sw;
			regen = true;
		}
		if(height % s.height != 0)
		{
			height = (height / sh + 1) * sh;
			regen = true;
		}
		if(regen)
			createGraphic(width, height, 0, true);
		else
			this.fill(0);

		// Stamp random tiles onto the canvas
		int r = 0;
		int c;
		int ox;
		int oy = 0;
		int widthInTiles = width / sw;
		int heightInTiles = height / sh;
		while(r < heightInTiles)
		{
			ox = 0;
			c = 0;
			while(c < widthInTiles)
			{
				if(FlxU.random() * total > Empties)
				{
					s.randomFrame();
					draw(s, ox, oy);
				}
				ox += sw;
				c++;
			}
			oy += sh;
			r++;
		}

		return this;
	}
	
	public FlxTileblock loadTiles(Integer TileGraphic, int TileWidth, int TileHeight)
	{
		return loadTiles(TileGraphic, TileWidth, TileHeight, 0);
	}
	
	public FlxTileblock loadTiles(Integer TileGraphic, int TileWidth)
	{
		return loadTiles(TileGraphic, TileWidth, 0, 0);
	}
	
	public FlxTileblock loadTiles(Integer TileGraphic)
	{
		return loadTiles(TileGraphic, 0, 0, 0);
	}
	
	
	/**
	 * NOTE: MOST OF THE TIME YOU SHOULD BE USING LOADTILES(), NOT LOADGRAPHIC()!
	 * <code>LoadTiles()</code> has a lot more functionality, can load non-square tiles, etc.
	 * Load an image from an embedded graphic file and use it to auto-fill this block with tiles.
	 * 
	 * @param	Graphic		The image you want to use.
	 * @param	Animated	Ignored.
	 * @param	Reverse		Ignored.
	 * @param	Width		Ignored.
	 * @param	Height		Ignored.
	 * @param	Unique		Ignored.
	 * 
	 * @return	This FlxSprite instance (nice for chaining stuff together, if you're into that).
	 */
	@Override
	public FlxSprite loadGraphic(int Graphic, boolean Animated, boolean Reverse, int Width, int Height, boolean Unique)
	{
		loadTiles(Graphic);
		return this;
	}
	
	@Override
	public FlxSprite loadGraphic(int Graphic, boolean Animated, boolean Reverse, int Width, int Height)
	{
		return loadGraphic(Graphic, Animated, Reverse, Width, Height, false);
	}
	
	@Override
	public FlxSprite loadGraphic(int Graphic, boolean Animated, boolean Reverse, int Width)
	{
		return loadGraphic(Graphic, Animated, Reverse, Width, 0, false);
	}
	
	@Override
	public FlxSprite loadGraphic(int Graphic, boolean Animated, boolean Reverse)
	{
		return loadGraphic(Graphic, Animated, Reverse, 0, 0, false);
	}
	
	@Override
	public FlxSprite loadGraphic(int Graphic, boolean Animated)
	{
		return loadGraphic(Graphic, Animated, false, 0, 0, false);
	}
	
	@Override
	public FlxSprite loadGraphic(int Graphic)
	{
		return loadGraphic(Graphic, false, false, 0, 0, false);
	}
}
