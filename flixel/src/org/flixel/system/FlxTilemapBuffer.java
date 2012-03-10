package org.flixel.system;

import org.flixel.FlxCamera;
import org.flixel.FlxG;
import org.flixel.FlxU;

import com.badlogic.gdx.graphics.g2d.Sprite;
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
	public int width;
	/**
	 * The height of the buffer (usually just a few tiles taller than the camera).
	 */
	public int height;
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

	private Sprite _pixels;
	
	public int indexX;
	public int indexY;
	
	
	/**
	 * Instantiates a new camera-specific buffer for storing the visual tilemap data.
	 *  
	 * @param TileWidth		The width of the tiles in this tilemap.
	 * @param TileHeight	The height of the tiles in this tilemap.
	 * @param WidthInTiles	How many tiles wide the tilemap is.
	 * @param HeightInTiles	How many tiles tall the tilemap is.
	 * @param Camera		Which camera this buffer relates to.
	 */
	public FlxTilemapBuffer(TextureRegion TileGraphic, int TileWidth, int TileHeight, int WidthInTiles, int HeightInTiles, FlxCamera Camera)
	{
		if(Camera == null)
			Camera = FlxG.camera;
		
		columns = (int) (FlxU.ceil(Camera.width/TileWidth)+1);
		if(columns > WidthInTiles)
			columns = WidthInTiles;
		rows = (int) (FlxU.ceil(Camera.height/TileHeight)+1);
		if(rows > HeightInTiles)
			rows = HeightInTiles;
		_pixels = new Sprite(TileGraphic, 0, 0, TileWidth, TileHeight);
		width = TileWidth;
		height = TileHeight;
		
		dirty = true;
	}
	
	/**
	 * Instantiates a new camera-specific buffer for storing the visual tilemap data.
	 *  
	 * @param TileWidth		The width of the tiles in this tilemap.
	 * @param TileHeight	The height of the tiles in this tilemap.
	 * @param WidthInTiles	How many tiles wide the tilemap is.
	 * @param HeightInTiles	How many tiles tall the tilemap is.
	 */
	public FlxTilemapBuffer(TextureRegion TileGraphic, int TileWidth, int TileHeight, int WidthInTiles, int HeightInTiles)
	{
		this(TileGraphic, TileWidth, TileHeight, WidthInTiles, HeightInTiles, null);
	}
	
	
	public void destroy()
	{
		_pixels = null;
	}

	public Sprite getPixels()
	{
		return _pixels;
	}
	
	
	public void draw()
	{
		_pixels.setRegion(indexX*width,0,width,height); //TODO: there is no support for multiple row spritesheet.
		_pixels.setPosition(x, y);
		_pixels.flip(false, true);
		_pixels.draw(FlxG.batch);
	}
	
}
