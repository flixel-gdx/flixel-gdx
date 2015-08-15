package org.flixel.system;

import org.flixel.FlxCamera;
import org.flixel.FlxG;
import org.flixel.FlxPoint;
import org.flixel.FlxU;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * A helper object to keep tilemap drawing performance decent across the new multi-camera system.
 * Pretty much don't even have to think about this class unless you are doing some crazy hacking.
 * 
 * @author Ka Wing Chin
 */
public class FlxTilemapBuffer
{
	/**
	 * The current X position of the buffer.
	 */
	public float x;
	/**
	 * The current Y position of the buffer.
	 */
	public float y;
	/**
	 * The width of the buffer (usually just a few tiles wider than the camera).
	 */
	public float width;
	/**
	 * The height of the buffer (usually just a few tiles taller than the
	 * camera).
	 */
	public float height;
	/**
	 * Whether the buffer needs to be redrawn.
	 */
	public boolean dirty;
	/**
	 * How many rows of tiles fit in this buffer.
	 */
	public int rows;
	/**
	 * How many columns of tiles fit in this buffer.
	 */
	public int columns;

	protected int _cacheId;

	/**
	 * Instantiates a new camera-specific buffer for storing the visual tilemap data.
	 * 
	 * @param	TileWidth		The width of the tiles in this tilemap.
	 * @param	TileHeight		The height of the tiles in this tilemap.
	 * @param	WidthInTiles	How many tiles wide the tilemap is.
	 * @param	HeightInTiles	How many tiles tall the tilemap is.
	 * @param	Camera			Which camera this buffer relates to.
	 */
	public FlxTilemapBuffer(float TileWidth,float TileHeight,int WidthInTiles,int HeightInTiles,FlxCamera Camera)
	{
		if(Camera == null)
			Camera = FlxG.camera;

		columns = (int)(FlxU.ceil(Camera.width/TileWidth)+1);
		if(columns > WidthInTiles)
			columns = WidthInTiles;
		rows = (int)(FlxU.ceil(Camera.height/TileHeight)+1);
		if(rows > HeightInTiles)
			rows = HeightInTiles;

		width = columns*TileWidth;
		height = rows*TileHeight;
		dirty = true;
		// _cache = new SpriteCache(columns * rows, true);
	}

	/**
	 * Instantiates a new camera-specific buffer for storing the visual tilemap data.
	 * 
	 * @param	TileWidth		The width of the tiles in this tilemap.
	 * @param	TileHeight		The height of the tiles in this tilemap.
	 * @param	WidthInTiles	How many tiles wide the tilemap is.
	 * @param	HeightInTiles	How many tiles tall the tilemap is.
	 */
	public FlxTilemapBuffer(float TileWidth,float TileHeight,int WidthInTiles,int HeightInTiles)
	{
		this(TileWidth,TileHeight,WidthInTiles,HeightInTiles,null);
	}

	/**
	 * Clean up memory.
	 */
	public void destroy()
	{
		// _cache.dispose();
	}

	/**
	 * Gets the buffer ready. Must be called before <code>addTile</code>.
	 */
	public void begin()
	{
		// _cache.clear();
		// _cache.beginCache();
	}

	/**
	 * Add a tile to the buffer.
	 * 
	 * @param	Tile	The <code>TextureRegion</code> for this tile.
	 * @param	X		The X position of the tile.
	 * @param	Y		The Y position of the tile.
	 */
	public void addTile(TextureRegion Tile, float X, float Y)
	{
		// _cache.add(Tile, X, Y);
	}

	/**
	 * Ends adding tiles to the buffer. Must be called after <code>begin</code>.
	 */
	public void end()
	{
		// _cacheId = _cache.endCache();
	}

	/**
	 * Just stamps this buffer onto the specified camera at the specified location.
	 * 
	 * @param	Camera	Which camera to draw the buffer onto.
	 * @param	Point	Where to draw the buffer at in camera coordinates.
	 */
	public void draw(FlxCamera Camera,FlxPoint Point)
	{
		/*
		 * FlxG.batch.end();
		 * 
		 * _cache.setProjectionMatrix(Camera.buffer.combined);
		 * 
		 * _cache.begin(); _cache.draw(_cacheId); _cache.end();
		 * 
		 * FlxG.batch.begin();
		 */
	}
}
