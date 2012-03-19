package org.flixel;

import org.flixel.event.AFlxObject;
import org.flixel.event.AFlxTile;
import org.flixel.system.FlxTile;
import org.flixel.system.FlxTilemapBuffer;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;

public class FlxTilemap extends FlxObject
{
	
	/**
	 * No auto-tiling.
	 */
	static public final int OFF = 0;
	/**
	 * Good for levels with thin walls that don'tile need interior corner art.
	 */
	static public final int AUTO = 1;
	/**
	 * Better for levels with thick walls that look better with interior corner art.
	 */
	static public final int ALT = 2;

	/**
	 * Set this flag to use one of the 16-tile binary auto-tile algorithms (OFF, AUTO, or ALT).
	 */
	public int auto;
	
	/**
	 * Read-only variable, do NOT recommend changing after the map is loaded!
	 */
	public int widthInTiles;
	/**
	 * Read-only variable, do NOT recommend changing after the map is loaded!
	 */
	public int heightInTiles;
	/**
	 * Read-only variable, do NOT recommend changing after the map is loaded!
	 */
	public int totalTiles;
	
	/**
	 * Rendering helper, minimize new object instantiation on repetitive methods.
	 */
	protected FlxPoint _flashPoint;
	/**
	 * Internal reference to the bitmap data object that stores the original tile graphics.
	 */
	protected TextureRegion _tiles;
	/**
	 * Internal list of buffers, one for each camera, used for drawing the tilemaps.
	 */
	protected Array<FlxTilemapBuffer> _buffers;
	/**
	 * Internal representation of the actual tile data, as a large 1D array of integers.
	 */
	protected IntArray _data;	
	/**
	 * Internal, the width of a single tile.
	 */
	protected int _tileWidth;
	/**
	 * Internal, the height of a single tile.
	 */
	protected int _tileHeight;
	/**
	 * Internal collection of tile objects, one for each type of tile in the map (NOTE one for every single tile in the whole map).
	 */
	protected Array<FlxTile> _tileObjects;
	/**
	 * Internal, used for rendering the debug bounding box display.
	 */
	protected TextureRegion _debugTileNotSolid;
	/**
	 * Internal, used for rendering the debug bounding box display.
	 */
	protected TextureRegion _debugTilePartial;
	/**
	 * Internal, used for rendering the debug bounding box display.
	 */
	protected TextureRegion _debugTileSolid;
	/**
	 * Internal flag for checking to see if we need to refresh
	 * the tilemap display to show or hide the bounding boxes.
	 */
	protected boolean _lastVisualDebug;
	/**
	 * Internal, used to sort of insert blank tiles in front of the tiles in the provided graphic.
	 */
	protected int _startingIndex;

	
	/**
	 * The tilemap constructor just initializes some basic variables.
	 */
	public FlxTilemap()
	{
		super();
		auto = OFF;
		widthInTiles = 0;
		heightInTiles = 0;
		totalTiles = 0;
		_buffers = new Array<FlxTilemapBuffer>();
		_flashPoint = new FlxPoint();
		_data = null;
		_tileWidth = 0;
		_tileHeight = 0;
		_tiles = null;
		_tileObjects = null;
		immovable = true;
		cameras = null;
		_debugTileNotSolid = null;
		_debugTilePartial = null;
		_debugTileSolid = null;
		_lastVisualDebug = FlxG.visualDebug;
		_startingIndex = 0;
	}
	
	
	/**
	 * Clean up memory.
	 */
	@Override
	public void destroy()
	{
		_flashPoint = null;
		_tiles = null;
		int i = 0;
		int l = _tileObjects.size;
		while(i < l)
			_tileObjects.get(i++).destroy();
		_tileObjects = null;
		i = 0;
		l = _buffers.size;
		while(i < l)
			((FlxTilemapBuffer)_buffers.get(i++)).destroy();
		_buffers = null;
		_data = null;
		_debugTileNotSolid.getTexture().dispose();
		_debugTileNotSolid = null;
		_debugTilePartial.getTexture().dispose();
		_debugTilePartial = null;
		_debugTileSolid.getTexture().dispose();
		_debugTileSolid = null;
		super.destroy();
	}
	
	
	/**
	 * Load the tilemap with string data and a tile graphic.
	 * 
	 * @param	MapData			A string of comma and line-return delineated indices indicating what order the tiles should go in.
	 * @param	TileGraphic		All the tiles you want to use, arranged in a strip corresponding to the numbers in MapData.
	 * @param	TileWidth		The width of your tiles (e.g. 8) - defaults to height of the tile graphic if unspecified.
	 * @param	TileHeight		The height of your tiles (e.g. 8) - defaults to width if unspecified.
	 * @param	AutoTile		Whether to load the map using an automatic tile placement algorithm.  Setting this to either AUTO or ALT will override any values you put for StartingIndex, DrawIndex, or CollideIndex.
	 * @param	StartingIndex	Used to sort of insert empty tiles in front of the provided graphic.  Default is 0, usually safest ot leave it at that.  Ignored if AutoTile is set.
	 * @param	DrawIndex		Initializes all tile objects equal to and after this index as visible. Default value is 1.  Ignored if AutoTile is set.
	 * @param	CollideIndex	Initializes all tile objects equal to and after this index as allowCollisions = ANY.  Default value is 1.  Ignored if AutoTile is set.  Can override and customize per-tile-type collision behavior using <code>setTileProperties()</code>.	
	 * 
	 * @return	A pointer this instance of FlxTilemap, for chaining as usual :)
	 */
	public FlxTilemap loadMap(String MapData, TextureRegion TileGraphic, int TileWidth, int TileHeight, int AutoTile, int StartingIndex, int DrawIndex, int CollideIndex)
	{
		auto = AutoTile;
		_startingIndex = StartingIndex;

		//Figure out the map dimensions based on the data string
		String[] columns;		
		String[] rows = MapData.split("\n");
		heightInTiles = rows.length;
		_data = new IntArray();
		int row = 0;
		int column = 0;
		while(row < heightInTiles)
		{
			columns = rows[row++].split(",");
			if(columns.length <= 1)
			{
				heightInTiles = heightInTiles - 1;
				continue;
			}
			if(widthInTiles == 0)
				widthInTiles = columns.length;
			column = 0;
			while(column < widthInTiles)
				_data.add(Integer.parseInt(columns[column++].trim()));
		}
		//Pre-process the map data if it's auto-tiled
		int i;
		totalTiles = widthInTiles*heightInTiles;
		if(auto > OFF)
		{
			_startingIndex = 1;
			DrawIndex = 1;
			CollideIndex = 1;
			i = 0;
			while(i < totalTiles)
				autoTile(i++);
		}
		
		//Figure out the size of the tiles
		_tiles = new Sprite(FlxG.addBitmap(TileGraphic));
		_tileWidth = TileWidth;
		if(_tileWidth == 0)
			_tileWidth = _tiles.getRegionHeight();
		_tileHeight = TileHeight;
		if(_tileHeight == 0)
			_tileHeight = _tileWidth;
		
		//create some tile objects that we'll use for overlap checks (one for each tile)
		i = 0;
		int l = (_tiles.getRegionWidth()/_tileWidth) * (_tiles.getRegionHeight()/_tileHeight);
		if(auto > OFF)
			l++;
		_tileObjects = new Array<FlxTile>(l);
		
		while(i < l)
		{
			_tileObjects.add(new FlxTile(this,i,_tileWidth,_tileHeight,(i >= DrawIndex),(i >= CollideIndex)?allowCollisions:NONE));
			i++;
		}
		
		//create debug tiles for rendering bounding boxes on demand
		if(_debugTileNotSolid != null)
			_debugTileNotSolid.getTexture().dispose();
		_debugTileNotSolid = makeDebugTile(FlxG.BLUE);
		if(_debugTilePartial != null)
			_debugTilePartial.getTexture().dispose();
		_debugTilePartial = makeDebugTile(FlxG.PINK);
		if(_debugTileSolid != null)
			_debugTileSolid.getTexture().dispose();
		_debugTileSolid = makeDebugTile(FlxG.GREEN);
		
		//Then go through and create the actual map
		width = widthInTiles*_tileWidth;
		height = heightInTiles*_tileHeight;
		i = 0;
		
		//TODO: this is not needed anymore
		/*while(i < totalTiles)
			updateTile(i++);*/
		
		return this;
	}
	
	/**
	 * Load the tilemap with string data and a tile graphic.
	 * 
	 * @param	MapData			A string of comma and line-return delineated indices indicating what order the tiles should go in.
	 * @param	TileGraphic		All the tiles you want to use, arranged in a strip corresponding to the numbers in MapData.
	 * @param	TileWidth		The width of your tiles (e.g. 8) - defaults to height of the tile graphic if unspecified.
	 * @param	TileHeight		The height of your tiles (e.g. 8) - defaults to width if unspecified.
	 * @param	AutoTile		Whether to load the map using an automatic tile placement algorithm.  Setting this to either AUTO or ALT will override any values you put for StartingIndex, DrawIndex, or CollideIndex.
	 * @param	StartingIndex	Used to sort of insert empty tiles in front of the provided graphic.  Default is 0, usually safest ot leave it at that.  Ignored if AutoTile is set.
	 * @param	DrawIndex		Initializes all tile objects equal to and after this index as visible. Default value is 1.  Ignored if AutoTile is set.	
	 * 
	 * @return	A pointer this instance of FlxTilemap, for chaining as usual :)
	 */
	public FlxTilemap loadMap(String MapData, TextureRegion TileGraphic, int TileWidth, int TileHeight, int AutoTile, int StartingIndex, int DrawIndex)
	{
		return loadMap(MapData, TileGraphic, TileWidth, TileHeight, AutoTile, StartingIndex, DrawIndex, 1);
	}
	
	/**
	 * Load the tilemap with string data and a tile graphic.
	 * 
	 * @param	MapData			A string of comma and line-return delineated indices indicating what order the tiles should go in.
	 * @param	TileGraphic		All the tiles you want to use, arranged in a strip corresponding to the numbers in MapData.
	 * @param	TileWidth		The width of your tiles (e.g. 8) - defaults to height of the tile graphic if unspecified.
	 * @param	TileHeight		The height of your tiles (e.g. 8) - defaults to width if unspecified.
	 * @param	AutoTile		Whether to load the map using an automatic tile placement algorithm.  Setting this to either AUTO or ALT will override any values you put for StartingIndex, DrawIndex, or CollideIndex.
	 * @param	StartingIndex	Used to sort of insert empty tiles in front of the provided graphic.  Default is 0, usually safest ot leave it at that.  Ignored if AutoTile is set.	
	 * 
	 * @return	A pointer this instance of FlxTilemap, for chaining as usual :)
	 */
	public FlxTilemap loadMap(String MapData, TextureRegion TileGraphic, int TileWidth, int TileHeight, int AutoTile, int StartingIndex)
	{
		return loadMap(MapData, TileGraphic, TileWidth, TileHeight, AutoTile, StartingIndex, 1, 1);
	}
	
	/**
	 * Load the tilemap with string data and a tile graphic.
	 * 
	 * @param	MapData			A string of comma and line-return delineated indices indicating what order the tiles should go in.
	 * @param	TileGraphic		All the tiles you want to use, arranged in a strip corresponding to the numbers in MapData.
	 * @param	TileWidth		The width of your tiles (e.g. 8) - defaults to height of the tile graphic if unspecified.
	 * @param	TileHeight		The height of your tiles (e.g. 8) - defaults to width if unspecified.
	 * @param	AutoTile		Whether to load the map using an automatic tile placement algorithm.  Setting this to either AUTO or ALT will override any values you put for StartingIndex, DrawIndex, or CollideIndex.
	 * 
	 * @return	A pointer this instance of FlxTilemap, for chaining as usual :)
	 */
	public FlxTilemap loadMap(String MapData, TextureRegion TileGraphic, int TileWidth, int TileHeight, int AutoTile)
	{
		return loadMap(MapData, TileGraphic, TileWidth, TileHeight, AutoTile, 0, 1, 1);
	}
	
	/**
	 * Load the tilemap with string data and a tile graphic.
	 * 
	 * @param	MapData			A string of comma and line-return delineated indices indicating what order the tiles should go in.
	 * @param	TileGraphic		All the tiles you want to use, arranged in a strip corresponding to the numbers in MapData.
	 * @param	TileWidth		The width of your tiles (e.g. 8) - defaults to height of the tile graphic if unspecified.
	 * @param	TileHeight		The height of your tiles (e.g. 8) - defaults to width if unspecified.
	 * @param	AutoTile		Whether to load the map using an automatic tile placement algorithm.  Setting this to either AUTO or ALT will override any values you put for StartingIndex, DrawIndex, or CollideIndex.	
	 * 
	 * @return	A pointer this instance of FlxTilemap, for chaining as usual :)
	 */
	public FlxTilemap loadMap(String MapData, TextureRegion TileGraphic, int TileWidth, int TileHeight)
	{
		return loadMap(MapData, TileGraphic, TileWidth, TileHeight, OFF, 0, 1, 1);
	}
	
	/**
	 * Load the tilemap with string data and a tile graphic.
	 * 
	 * @param	MapData			A string of comma and line-return delineated indices indicating what order the tiles should go in.
	 * @param	TileGraphic		All the tiles you want to use, arranged in a strip corresponding to the numbers in MapData.
	 * @param	TileWidth		The width of your tiles (e.g. 8) - defaults to height of the tile graphic if unspecified.
	 * @param	TileHeight		The height of your tiles (e.g. 8) - defaults to width if unspecified.	
	 * 
	 * @return	A pointer this instance of FlxTilemap, for chaining as usual :)
	 */
	public FlxTilemap loadMap(String MapData, TextureRegion TileGraphic, int TileWidth)
	{
		return loadMap(MapData, TileGraphic, TileWidth, 0, OFF, 0, 1, 1);
	}
	
	/**
	 * Load the tilemap with string data and a tile graphic.
	 * 
	 * @param	MapData			A string of comma and line-return delineated indices indicating what order the tiles should go in.
	 * @param	TileGraphic		All the tiles you want to use, arranged in a strip corresponding to the numbers in MapData.
	 * @param	TileWidth		The width of your tiles (e.g. 8) - defaults to height of the tile graphic if unspecified.
	 * @param	TileHeight		The height of your tiles (e.g. 8) - defaults to width if unspecified.
	 * @param	AutoTile		Whether to load the map using an automatic tile placement algorithm.  Setting this to either AUTO or ALT will override any values you put for StartingIndex, DrawIndex, or CollideIndex.	
	 * 
	 * @return	A pointer this instance of FlxTilemap, for chaining as usual :)
	 */
	public FlxTilemap loadMap(String MapData, TextureRegion TileGraphic)
	{
		return loadMap(MapData, TileGraphic, 0, 0, OFF, 0, 1, 1);
	}

	
	/**
	 * Internal function to clean up the map loading code.
	 * Just generates a wireframe box the size of a tile with the specified color.
	 * @param buffer 
	 */
	protected TextureRegion makeDebugTile(int Color)
	{	
		Pixmap p = new Pixmap(FlxU.ceilPowerOfTwo(_tileWidth), FlxU.ceilPowerOfTwo(_tileHeight),Pixmap.Format.RGBA8888);
		p.setColor(FlxU.colorFromHex(Color));
		p.drawRectangle(0, 0, _tileWidth, _tileHeight);
		TextureRegion sprite = new TextureRegion(new Texture(p));
		sprite.flip(false, true);
		p.dispose();
		return sprite;
	}
	
	/**
	 * Main logic loop for tilemap is pretty simple,
	 * just checks to see if visual debug got turned on.
	 * If it did, the tilemap is flagged as dirty so it
	 * will be redrawn with debug info on the next draw call.
	 */
	@Override
	public void update()
	{
		if(_lastVisualDebug != FlxG.visualDebug)
		{
			_lastVisualDebug = FlxG.visualDebug;
			setDirty();
		}
	}
	
	
	/**
	 * Internal function that actually renders the tilemap to the tilemap buffer.  Called by draw().
	 * 
	 * @param	Buffer		The <code>FlxTilemapBuffer</code> you are rendering to.
	 * @param	Camera		The related <code>FlxCamera</code>, mainly for scroll values.
	 */
	protected void drawTilemap(FlxTilemapBuffer Buffer, FlxCamera Camera)
	{		
		//Copy tile images into the tile buffer
		_point.x = (Camera.scroll.x*scrollFactor.x) - x; //modified from getScreenXY()
		_point.y = (Camera.scroll.y*scrollFactor.y) - y;
		int screenXInTiles = (int) ((_point.x + ((_point.x > 0)?0.0000001:-0.0000001))/_tileWidth);
		int screenYInTiles = (int) ((_point.y + ((_point.y > 0)?0.0000001:-0.0000001))/_tileHeight);
		int screenRows = Buffer.rows;
		int screenColumns = Buffer.columns;
		
		//Bound the upper left corner
		if(screenXInTiles < 0)
			screenXInTiles = 0;
		if(screenXInTiles > widthInTiles-screenColumns)
			screenXInTiles = widthInTiles-screenColumns;
		if(screenYInTiles < 0)
			screenYInTiles = 0;
		if(screenYInTiles > heightInTiles-screenRows)
			screenYInTiles = heightInTiles-screenRows;
		
		int rowIndex = screenYInTiles*widthInTiles+screenXInTiles;
		_flashPoint.y = 0;
		int row = 0;
		int column;
		int columnIndex;
		FlxTile tile;
		TextureRegion debugTile;
		_flashPoint.x = x - ((int)(Camera.scroll.x*scrollFactor.x)) + Buffer.x; //copied from getScreenXY()
		_flashPoint.y = y - ((int)(Camera.scroll.y*scrollFactor.y)) + Buffer.y;
		_flashPoint.x += (_flashPoint.x > 0)?0.0000001:-0.0000001;
		_flashPoint.y += (_flashPoint.y > 0)?0.0000001:-0.0000001;
		while(row < screenRows)
		{
			columnIndex = rowIndex;
			column = 0;
			_flashPoint.x = x - ((int)(Camera.scroll.x*scrollFactor.x));
			_flashPoint.x += (_flashPoint.x > 0)?0.0000001:-0.0000001;
			while(column < screenColumns)
			{
				int dataIndex = _data.get(columnIndex);
				if(dataIndex > 0) // All tiles will be come one digit lower, 16 to 15. Except 1 and 0 are ignored. TODO: place this into arrayToCSV?
					dataIndex--;
				
				tile = _tileObjects.get(dataIndex);
				if(tile.visible)
				{
					Buffer.indexX = tile.index;
					Buffer.x = _flashPoint.x;
					Buffer.y = _flashPoint.y;					
					Buffer.draw();
					
					if(FlxG.visualDebug && !ignoreDrawDebug)
					{
						if(tile.allowCollisions <= NONE)
							debugTile = _debugTileNotSolid;
						else if(tile.allowCollisions != ANY)
							debugTile = _debugTilePartial;
						else
							debugTile = _debugTileSolid;
						FlxG.batch.draw(debugTile, Buffer.x, Buffer.y);
					}					
				}
				_flashPoint.x += _tileWidth;
				column++;
				columnIndex++;
			}
			rowIndex += widthInTiles;
			_flashPoint.y += _tileHeight;
			row++;
		}
		Buffer.x = screenXInTiles*_tileWidth;
		Buffer.y = screenYInTiles*_tileHeight;
	}
	
	
	/**
	 * Draws the tilemap buffers to the cameras and handles flickering.
	 */
	@Override
	public void draw()
	{
		if(_flickerTimer != 0)
		{
			_flicker = !_flicker;
			if(_flicker)
				return;
		}
		
		if(cameras == null)
			cameras = FlxG.cameras;
		FlxCamera camera = FlxG.camera;
		FlxTilemapBuffer buffer;
		
		//int l = cameras.size;
				
		try
		{
			_buffers.get(0);
		}
		catch(Exception e)
		{
			_buffers.add(new FlxTilemapBuffer(_tiles, _tileWidth, _tileHeight, widthInTiles, heightInTiles, camera));
		}
			
		buffer = _buffers.get(0);
		if(!buffer.dirty)
		{
			_point.x = x - ((int)(camera.scroll.x*scrollFactor.x)) + buffer.x; //copied from getScreenXY()
			_point.y = y - ((int)(camera.scroll.y*scrollFactor.y)) + buffer.y;
			buffer.dirty = (_point.x > 0) || (_point.y > 0) || (_point.x + buffer.width < camera.width) || (_point.y + buffer.height < camera.height);
		}
		if(buffer.dirty)
		{
			drawTilemap(buffer,camera);
			buffer.dirty = false;
		}
		
		//TODO: normally the position of the tiles will be set here, but is been take care in drawTilemap().
//		buffer.x = _flashPoint.x;
//		buffer.y = _flashPoint.y;
//		buffer.draw();//TODO: needed?
		_VISIBLECOUNT++;		
	}
	
	
	public IntArray getData(boolean Simple)
	{
		if(!Simple)
			return _data;
		
		int i = 0;
		int l = _data.size;
		IntArray data = new IntArray(); 
		while(i < l)
		{			
			data.set(i, ((_tileObjects.get(_data.get(i)).allowCollisions > 0)?1:0));
		}
		return data;
	}
	
	
	/**
	 * Set the dirty flag on all the tilemap buffers.
	 * Basically forces a reset of the drawn tilemaps, even if it wasn'tile necessary.
	 * 
	 * @param	Dirty		Whether to flag the tilemap buffers as dirty or not.
	 */
	public void setDirty(boolean Dirty)
	{
		int i = 0;
		int l = _buffers.size;
		while(i < l)
			_buffers.get(i++).dirty = Dirty;
	}
	
	/**
	 * Set the dirty flag on all the tilemap buffers.
	 * Basically forces a reset of the drawn tilemaps, even if it wasn'tile necessary.
	 */
	public void setDirty()
	{
		setDirty(true);
	}
	
	
	/**
	 * Find a path through the tilemap.  Any tile with any collision flags set is treated as impassable.
	 * If no path is discovered then a null reference is returned.
	 * 
	 * @param	Start		The start point in world coordinates.
	 * @param	End			The end point in world coordinates.
	 * @param	Simplify	Whether to run a basic simplification algorithm over the path data, removing extra points that are on the same line.  Default value is true.
	 * @param	RaySimplify	Whether to run an extra raycasting simplification algorithm over the remaining path data.  This can result in some close corners being cut, and should be used with care if at all (yet).  Default value is false.
	 * 
	 * @return	A <code>FlxPath</code> from the start to the end.  If no path could be found, then a null reference is returned.
	 */
	public FlxPath findPath(FlxPoint Start,FlxPoint End,boolean Simplify,boolean RaySimplify)
	{
		//figure out what tile we are starting and ending on.
		int startIndex = (int)((Start.y-y)/_tileHeight) * widthInTiles + (int)((Start.x-x)/_tileWidth);
		int endIndex = (int)((End.y-y)/_tileHeight) * widthInTiles + (int)((End.x-x)/_tileWidth);

		//check that the start and end are clear.
		if( ((_tileObjects.get(_data.get(startIndex))).allowCollisions > 0) ||
			((_tileObjects.get(_data.get(endIndex))).allowCollisions > 0) )
			return null;
		
		//figure out how far each of the tiles is from the starting tile
		IntArray distances = computePathDistance(startIndex,endIndex);
		if(distances == null)
			return null;

		//then count backward to find the shortest path.
		Array<FlxPoint> points = new Array<FlxPoint>();
		walkPath(distances,endIndex,points);
		
		//reset the start and end points to be exact
		FlxPoint node;
		node = points.get(points.size-1);
		node.x = Start.x;
		node.y = Start.y;
		node = points.get(0);
		node.x = End.x;
		node.y = End.y;

		//some simple path cleanup options
		if(Simplify)
			simplifyPath(points);
		if(RaySimplify)
			raySimplifyPath(points);
		
		//finally load the remaining points into a new path object and return it
		FlxPath path = new FlxPath();
		int i = points.size - 1;
		while(i >= 0)
		{
			node = points.get(i--);
			if(node != null)
				path.addPoint(node,true);
		}
		return path;
	}
	
	/**
	 * Pathfinding helper function, strips out extra points on the same line.
	 *
	 * @param	Points		An array of <code>FlxPoint</code> nodes.
	 */
	protected void simplifyPath(Array<FlxPoint> Points)
	{
		float deltaPrevious;
		float deltaNext;
		FlxPoint last = Points.get(0);
		FlxPoint node;
		int i = 1;
		int l = Points.size-1;
		while(i < l)
		{
			node = Points.get(i);
			deltaPrevious = (node.x - last.x)/(node.y - last.y);
			deltaNext = (node.x - Points.get(i+1).x)/(node.y - Points.get(i+1).y);
			if((last.x == Points.get(i+1).x) || (last.y == Points.get(i+1).y) || (deltaPrevious == deltaNext))
				Points.set(i, null);
			else
				last = node;
			i++;
		}
	}
	
	/**
	 * Pathfinding helper function, strips out even more points by raycasting from one point to the next and dropping unnecessary points.
	 * 
	 * @param	Points		An array of <code>FlxPoint</code> nodes.
	 */
	protected void raySimplifyPath(Array<FlxPoint> Points)
	{
		FlxPoint source = Points.get(0);
		int lastIndex = -1;
		FlxPoint node;
		int i = 1;
		int l = Points.size;
		while(i < l)
		{
			node = Points.get(i++);
			if(node == null)
				continue;
			if(ray(source,node,_point))	
			{
				if(lastIndex >= 0)
					Points.set(lastIndex, null);
			}
			else
				source = Points.get(lastIndex);
			lastIndex = i-1;
		}
	}
	
	/**
	 * Pathfinding helper function, floods a grid with distance information until it finds the end point.
	 * NOTE: Currently this process does NOT use any kind of fancy heuristic!  It's pretty brute.
	 * 
	 * @param	StartIndex	The starting tile's map index.
	 * @param	EndIndex	The ending tile's map index.
	 * 
	 * @return	A Flash <code>Array</code> of <code>FlxPoint</code> nodes.  If the end tile could not be found, then a null <code>Array</code> is returned instead.
	 */
	protected IntArray computePathDistance(int StartIndex,int EndIndex)
	{
		//Create a distance-based representation of the tilemap.
		//All walls are flagged as -2, all open areas as -1.
		int mapSize = widthInTiles*heightInTiles;
		IntArray distances = new IntArray(mapSize);
		int i = 0;
		while(i < mapSize)
		{
			if((_tileObjects.get(_data.get(i))).allowCollisions != NONE)
				distances.set(i, -2);
			else
				distances.set(i, -1);
			i++;
		}
		distances.set(StartIndex,  0);
		int distance = 1;
		IntArray neighbors = new IntArray();
		neighbors.add(StartIndex);
		IntArray current;
		int currentIndex;
		boolean left;
		boolean right;
		boolean up;
		boolean down;
		int currentLength;
		boolean foundEnd = false;
		while(neighbors.size > 0)
		{
			current = neighbors;
			neighbors = new IntArray();
			
			i = 0;
			currentLength = current.size;
			while(i < currentLength)
			{
				currentIndex = current.get(i++);
				if(currentIndex == EndIndex)
				{
					foundEnd = true;
					neighbors.size = 0;
					break;
				}
				
				//basic map bounds
				left = currentIndex%widthInTiles > 0;
				right = currentIndex%widthInTiles < widthInTiles-1;
				up = currentIndex/widthInTiles > 0;
				down = currentIndex/widthInTiles < heightInTiles-1;
				
				int index;
				if(up)
				{
					index = currentIndex - widthInTiles;
					if(distances.get(index) == -1)
					{
						distances.set(index, distance);
						neighbors.add(index);
					}
				}
				if(right)
				{
					index = currentIndex + 1;
					if(distances.get(index) == -1)
					{
						distances.set(index, distance);
						neighbors.add(index);
					}
				}
				if(down)
				{
					index = currentIndex + widthInTiles;
					if(distances.get(index) == -1)
					{
						distances.set(index, distance);
						neighbors.add(index);
					}
				}
				if(left)
				{
					index = currentIndex - 1;
					if(distances.get(index) == -1)
					{
						distances.set(index, distance);
						neighbors.add(index);
					}
				}
				if(up && right)
				{
					index = currentIndex - widthInTiles + 1;
					if((distances.get(index) == -1) && (distances.get(currentIndex-widthInTiles) >= -1) && (distances.get(currentIndex+1) >= -1))
					{
						distances.set(index, distance);
						neighbors.add(index);
					}
				}
				if(right && down)
				{
					index = currentIndex + widthInTiles + 1;
					if((distances.get(index) == -1) && (distances.get(currentIndex+widthInTiles) >= -1) && (distances.get(currentIndex+1) >= -1))
					{
						distances.set(index, distance);
						neighbors.add(index);
					}
				}
				if(left && down)
				{
					index = currentIndex + widthInTiles - 1;
					if((distances.get(index) == -1) && (distances.get(currentIndex+widthInTiles) >= -1) && (distances.get(currentIndex-1) >= -1))
					{
						distances.set(index, distance);
						neighbors.add(index);
					}
				}
				if(up && left)
				{
					index = currentIndex - widthInTiles - 1;
					if((distances.get(index) == -1) && (distances.get(currentIndex-widthInTiles) >= -1) && (distances.get(currentIndex-1) >= -1))
					{
						distances.set(index, distance);
						neighbors.add(index);
					}
				}
			}
			distance++;
		}
		if(!foundEnd)
			distances = null;
		return distances;
	}
	
	
	/**
	 * Pathfinding helper function, recursively walks the grid and finds a shortest path back to the start.
	 * 
	 * @param	Data	A Flash <code>Array</code> of distance information.
	 * @param	Start	The tile we're on in our walk backward.
	 * @param	Points	A Flash <code>Array</code> of <code>FlxPoint</code> nodes composing the path from the start to the end, compiled in reverse order.
	 */
	protected void walkPath(IntArray Data,int Start,Array<FlxPoint>Points)
	{
		Points.add(new FlxPoint((float) (x + (int)(Start%widthInTiles)*_tileWidth + _tileWidth*0.5), (float) (y + (int)(Start/widthInTiles)*_tileHeight + _tileHeight*0.5)));
		if(Data.get(Start) == 0)
			return;
		
		//basic map bounds
		boolean left = Start%widthInTiles > 0;
		boolean right = Start%widthInTiles < widthInTiles-1;
		boolean up = Start/widthInTiles > 0;
		boolean down = Start/widthInTiles < heightInTiles-1;
		
		int current = Data.get(Start);
		int i;
		if(up)
		{
			i = Start - widthInTiles;
			if((Data.get(i) >= 0) && (Data.get(i) < current))
			{
				walkPath(Data,i,Points);
				return;
			}
		}
		if(right)
		{
			i = Start + 1;
			if((Data.get(i) >= 0) && (Data.get(i) < current))
			{
				walkPath(Data,i,Points);
				return;
			}
		}
		if(down)
		{
			i = Start + widthInTiles;
			if((Data.get(i) >= 0) && (Data.get(i) < current))
			{
				walkPath(Data,i,Points);
				return;
			}
		}
		if(left)
		{
			i = Start - 1;
			if((Data.get(i) >= 0) && (Data.get(i) < current))
			{
				walkPath(Data,i,Points);
				return;
			}
		}
		if(up && right)
		{
			i = Start - widthInTiles + 1;
			if((Data.get(i) >= 0) && (Data.get(i) < current))
			{
				walkPath(Data,i,Points);
				return;
			}
		}
		if(right && down)
		{
			i = Start + widthInTiles + 1;
			if((Data.get(i) >= 0) && (Data.get(i) < current))
			{
				walkPath(Data,i,Points);
				return;
			}
		}
		if(left && down)
		{
			i = Start + widthInTiles - 1;
			if((Data.get(i) >= 0) && (Data.get(i) < current))
			{
				walkPath(Data,i,Points);
				return;
			}
		}
		if(up && left)
		{
			i = Start - widthInTiles - 1;
			if((Data.get(i) >= 0) && (Data.get(i) < current))
			{
				walkPath(Data,i,Points);
				return;
			}
		}
	}
	
	
	@Override
	public boolean overlaps(FlxBasic ObjectOrGroup, boolean InScreenSpace, FlxCamera Camera)
	{
		if(ObjectOrGroup instanceof FlxGroup)
		{
			boolean results = false;
			FlxBasic basic;
			int i = 0;
			Array<FlxBasic> members = ((FlxGroup)ObjectOrGroup).members;
			while(i < members.size)
			{
				basic = members.get(i);
				if(basic instanceof FlxObject)
				{
					if(overlapsWithCallback((FlxObject)basic))
						results = true;
				}
				else
				{
					if(overlaps(basic,InScreenSpace,Camera))
						results = true;
				}
			}
			return results;
		}
		else if(ObjectOrGroup instanceof FlxObject)
			return overlapsWithCallback((FlxObject)ObjectOrGroup);
		return false;
	}
	
	
	/**
	 * Checks to see if this <code>FlxObject</code> were located at the given position, would it overlap the <code>FlxObject</code> or <code>FlxGroup</code>?
	 * This is distinct from overlapsPoint(), which just checks that point, rather than taking the object's size into account.
	 * WARNING: Currently tilemaps do NOT support screen space overlap checks!
	 * 
	 * @param	X				The X position you want to check.  Pretends this object (the caller, not the parameter) is located here.
	 * @param	Y				The Y position you want to check.  Pretends this object (the caller, not the parameter) is located here.
	 * @param	ObjectOrGroup	The object or group being tested.
	 * @param	InScreenSpace	Whether to take scroll factors into account when checking for overlap.  Default is false, or "only compare in world space."
	 * @param	Camera			Specify which game camera you want.  If null getScreenXY() will just grab the first global camera.
	 * 
	 * @return	Whether or not the two objects overlap.
	 */
	@Override 
	public boolean overlapsAt(float X,float Y,FlxBasic ObjectOrGroup,boolean InScreenSpace,FlxCamera Camera)
	{
		if(ObjectOrGroup instanceof FlxGroup)
		{
			boolean results = false;
			FlxBasic basic;
			int i = 0;
			Array<FlxBasic> members = ((FlxGroup)ObjectOrGroup).members;
			int length = members.size;
			while(i < length)
			{
				basic = members.get(i++);
				if(basic instanceof FlxObject)
				{
					_point.x = X;
					_point.y = Y;
					if(overlapsWithCallback((FlxObject)basic,null,false,_point))
						results = true;
				}
				else
				{
					if(overlapsAt(X,Y,basic,InScreenSpace,Camera))
						results = true;
				}
			}
			return results;
		}
		else if(ObjectOrGroup instanceof FlxObject)
		{
			_point.x = X;
			_point.y = Y;
			return overlapsWithCallback((FlxObject)ObjectOrGroup,null,false,_point);
		}
		return false;
	}
	
	
	/**
	 * Checks if the Object overlaps any tiles with any collision flags set,
	 * and calls the specified callback function (if there is one).
	 * Also calls the tile's registered callback if the filter matches.
	 * 
	 * @param	Object				The <code>FlxObject</code> you are checking for overlaps against.
	 * @param	Callback			An optional function that takes the form "myCallback(Object1:FlxObject,Object2:FlxObject)", where Object1 is a FlxTile object, and Object2 is the object passed in in the first parameter of this method.
	 * @param	FlipCallbackParams	Used to preserve A-B list ordering from FlxObject.separate() - returns the FlxTile object as the second parameter instead.
	 * @param	Position			Optional, specify a custom position for the tilemap (useful for overlapsAt()-type funcitonality).
	 * 
	 * @return	Whether there were overlaps, or if a callback was specified, whatever the return value of the callback was.
	 */
	public boolean overlapsWithCallback(FlxObject Object, AFlxObject Callback,boolean FlipCallbackParams,FlxPoint Position)
	{		
		boolean results = false;
		
		float X = x;
		float Y = y;
		if(Position != null)
		{
			X = Position.x;
			Y = Position.y;
		}
		
		//Figure out what tiles we need to check against
		int selectionX = (int) FlxU.floor((Object.x - X)/(float)_tileWidth);
		int selectionY = (int) FlxU.floor((Object.y - Y)/(float)_tileHeight);
		int selectionWidth = selectionX + (int)FlxU.ceil(Object.width/(float)_tileWidth) + 1;
		int selectionHeight = selectionY + (int)FlxU.ceil(Object.height/(float)_tileHeight) + 1;

		//Then bound these coordinates by the map edges
		if(selectionX < 0)
			selectionX = 0;
		if(selectionY < 0)
			selectionY = 0;
		if(selectionWidth > widthInTiles)
			selectionWidth = widthInTiles;
		if(selectionHeight > heightInTiles)
			selectionHeight = heightInTiles;

		//Then loop through this selection of tiles and call FlxObject.separate() accordingly
		int rowStart = selectionY*widthInTiles;
		int row = selectionY;
		int column;
		FlxTile tile;
		boolean overlapFound;
		float deltaX = X - last.x;
		float deltaY = Y - last.y;
		while(row < selectionHeight)
		{
			column = selectionX;
			while(column < selectionWidth)
			{
				overlapFound = false;
				int dataIndex = _data.get(rowStart+column);
				if(dataIndex > 0) // All tiles will be come one digit lower, 16 to 15. Except 1 and 0 are ignored. TODO: place this into arrayToCSV?
					dataIndex--;
				tile = _tileObjects.get(dataIndex);
				if(tile.allowCollisions != NONE)
				{
					tile.x = X+column*_tileWidth;
					tile.y = Y+row*_tileHeight;
					tile.last.x = tile.x - deltaX;
					tile.last.y = tile.y - deltaY;
					if(Callback != null)
					{
						if(FlipCallbackParams)
							overlapFound = Callback.onOverlapsWith(Object,tile);
						else
							overlapFound = Callback.onOverlapsWith(tile,Object);
					}
					else
						overlapFound = (Object.x + Object.width > tile.x) && (Object.x < tile.x + tile.width) && (Object.y + Object.height > tile.y) && (Object.y < tile.y + tile.height);
					if(overlapFound)
					{
						if((tile.callback != null) && ((tile.filter == null) || (tile.filter.isInstance(Object))))
						{
							tile.mapIndex = rowStart+column;
							tile.callback.onOverlap(tile,Object);
						}
						results = true;
					}
				}
				else if((tile.callback != null) && ((tile.filter == null) || (tile.filter.isInstance(Object))))
				{
					tile.mapIndex = rowStart+column;
					tile.callback.onOverlap(tile,Object);
				}
				column++;
			}
			rowStart += widthInTiles;
			row++;
		}
	
		return results;
	}	

	/**
	 * Checks if the Object overlaps any tiles with any collision flags set,
	 * and calls the specified callback function (if there is one).
	 * Also calls the tile's registered callback if the filter matches.
	 * 
	 * @param	Object				The <code>FlxObject</code> you are checking for overlaps against.
	 * @param	Callback			An optional function that takes the form "myCallback(Object1:FlxObject,Object2:FlxObject)", where Object1 is a FlxTile object, and Object2 is the object passed in in the first parameter of this method.
	 * @param	FlipCallbackParams	Used to preserve A-B list ordering from FlxObject.separate() - returns the FlxTile object as the second parameter instead.
	 * 
	 * @return	Whether there were overlaps, or if a callback was specified, whatever the return value of the callback was.
	 */
	public boolean overlapsWithCallback(FlxObject Object, AFlxObject Callback, boolean FlipCallbackParams)
	{
		return overlapsWithCallback(Object, Callback, FlipCallbackParams, null);
	}
	
	/**
	 * Checks if the Object overlaps any tiles with any collision flags set,
	 * and calls the specified callback function (if there is one).
	 * Also calls the tile's registered callback if the filter matches.
	 * 
	 * @param	Object				The <code>FlxObject</code> you are checking for overlaps against.
	 * @param	Callback			An optional function that takes the form "myCallback(Object1:FlxObject,Object2:FlxObject)", where Object1 is a FlxTile object, and Object2 is the object passed in in the first parameter of this method.
	 * 
	 * @return	Whether there were overlaps, or if a callback was specified, whatever the return value of the callback was.
	 */
	public boolean overlapsWithCallback(FlxObject Object, AFlxObject Callback)
	{
		return overlapsWithCallback(Object, Callback, false, null);
	}

	/**
	 * Checks if the Object overlaps any tiles with any collision flags set,
	 * and calls the specified callback function (if there is one).
	 * Also calls the tile's registered callback if the filter matches.
	 * 
	 * @param	Object				The <code>FlxObject</code> you are checking for overlaps against.
	 *  
	 * @return	Whether there were overlaps, or if a callback was specified, whatever the return value of the callback was.
	 */
	public boolean overlapsWithCallback(FlxObject Object)
	{
		return overlapsWithCallback(Object, null, false, null);
	}
	
	
	/**
	 * Checks to see if a point in 2D world space overlaps this <code>FlxObject</code> object.
	 * 
	 * @param	Point			The point in world space you want to check.
	 * @param	InScreenSpace	Whether to take scroll factors into account when checking for overlap.
	 * @param	Camera			Specify which game camera you want.  If null getScreenXY() will just grab the first global camera.
	 * 
	 * @return	Whether or not the point overlaps this object.
	 */
	@Override
	public boolean overlapsPoint(FlxPoint Point, boolean InScreenSpace, FlxCamera Camera)
	{
			if(!InScreenSpace)
				return (_tileObjects.get(_data.get((int)((int)((Point.y-y)/_tileHeight)*widthInTiles + (Point.x-x)/_tileWidth)))).allowCollisions > 0;
		
		if(Camera == null)
			Camera = FlxG.camera;
		Point.x = Point.x - Camera.scroll.x;
		Point.y = Point.y - Camera.scroll.y;
		getScreenXY(_point,Camera);
		return (_tileObjects.get(_data.get((int)((int)((Point.y-_point.y)/_tileHeight)*widthInTiles + (Point.x-_point.x)/_tileWidth)))).allowCollisions > 0;
	}
	
	/**
	 * Checks to see if a point in 2D world space overlaps this <code>FlxObject</code> object.
	 * 
	 * @param	Point			The point in world space you want to check.
	 * @param	InScreenSpace	Whether to take scroll factors into account when checking for overlap.
	 * 
	 * @return	Whether or not the point overlaps this object.
	 */
	public boolean overlapsPoint(FlxPoint Point, boolean InScreenSpace)
	{
		return overlapsPoint(Point, InScreenSpace, null);
	}
	
	/**
	 * Checks to see if a point in 2D world space overlaps this <code>FlxObject</code> object.
	 * 
	 * @param	Point			The point in world space you want to check.
	 *
	 * @return	Whether or not the point overlaps this object.
	 */
	public boolean overlapsPoint(FlxPoint Point)
	{
		return overlapsPoint(Point, false, null);
	}


	/**
	 * Check the value of a particular tile.
	 * 
	 * @param	X		The X coordinate of the tile (in tiles, not pixels).
	 * @param	Y		The Y coordinate of the tile (in tiles, not pixels).
	 * 
	 * @return	A uint containing the value of the tile at this spot in the array.
	 */
	public int getTile(int X, int Y)
	{
		return _data.get(Y * widthInTiles + X);
	}
	
	/**
	 * Get the value of a tile in the tilemap by index.
	 * 
	 * @param	Index	The slot in the data array (Y * widthInTiles + X) where this tile is stored.
	 * 
	 * @return	A uint containing the value of the tile at this spot in the array.
	 */
	public int getTileByIndex(int Index)
	{
		return _data.get(Index);
	}
	
	/**
	 * Returns a new Flash <code>Array</code> full of every map index of the requested tile type.
	 *
	 * @param	Index	The requested tile type.
	 * 
	 * @return	An <code>Array</code> with a list of all map indices of that tile type.
	 */
	public IntArray getTileInstances(int Index)
	{
		IntArray array = null;
		int i = 0;
		int l = widthInTiles * heightInTiles;
		while(i < l)
		{
			if(_data.get(i) == Index)
			{
				if(array == null)
					array = new IntArray();
				array.add(i);
			}
			i++;
		}
		
		return array;
	}
	
	
	/**
	 * Returns a new Flash <code>Array</code> full of every coordinate of the requested tile type.
	 * 
	 * @param	Index		The requested tile type.
	 * @param	Midpoint	Whether to return the coordinates of the tile midpoint, or upper left corner. Default is true, return midpoint.
	 * 
	 * @return	An <code>Array</code> with a list of all the coordinates of that tile type.
	 */
	public Array<FlxPoint> getTileCoords(int Index, boolean Midpoint)
	{
		Array<FlxPoint> array = null;
		
		FlxPoint point;
		int i = 0;
		int l = widthInTiles * heightInTiles;
		while(i < l)
		{
			if(_data.get(i) == Index)
			{
				point = new FlxPoint(x + ((int)(i%widthInTiles))*_tileWidth,y + ((int)(i/widthInTiles))*_tileHeight);
				if(Midpoint)
				{
					point.x += _tileWidth*0.5;
					point.y += _tileHeight*0.5;
				}
				if(array == null)
					array = new Array<FlxPoint>();
				array.add(point);
			}
			i++;
		}
		
		return array;
	}
	
	
	public Array<FlxPoint> getTileCoords(int Index)
	{
		return getTileCoords(Index, false);
	}
	
	/**
	 * Change the data and graphic of a tile in the tilemap.
	 * 
	 * @param	X				The X coordinate of the tile (in tiles, not pixels).
	 * @param	Y				The Y coordinate of the tile (in tiles, not pixels).
	 * @param	Tile			The new integer data you wish to inject.
	 * @param	UpdateGraphics	Whether the graphical representation of this tile should change.
	 * 
	 * @return	Whether or not the tile was actually changed.
	 */ 
	public boolean setTile(int X, int Y, int Tile, boolean UpdateGraphics)
	{
		if((X >= widthInTiles) || (Y >= heightInTiles))
			return false;
		return setTileByIndex(Y * widthInTiles + X,Tile,UpdateGraphics);
	}
	
	public boolean setTile(int X, int Y, int Tile)
	{
		return setTile(X, Y, Tile, true);
	}
	
	/**
	 * Change the data and graphic of a tile in the tilemap.
	 * 
	 * @param	Index			The slot in the data array (Y * widthInTiles + X) where this tile is stored.
	 * @param	Tile			The new integer data you wish to inject.
	 * @param	UpdateGraphics	Whether the graphical representation of this tile should change.
	 * 
	 * @return	Whether or not the tile was actually changed.
	 */
	public boolean setTileByIndex(int Index, int Tile, boolean UpdateGraphics)
	{
		if(Index >= _data.size)
			return false;
		
		boolean ok = true;
		_data.set(Index, Tile);
		
		if(!UpdateGraphics)
			return ok;
		
		setDirty();
		
		if(auto == OFF)
		{
			updateTile(Index);
			return ok;
		}
		
		//If this map is autotiled and it changes, locally update the arrangement
		int i;
		int row = ((int)(Index/widthInTiles)) - 1;
		int rowLength = row + 3;
		int column = Index%widthInTiles - 1;
		int columnHeight = column + 3;
		while(row < rowLength)
		{
			column = columnHeight - 3;
			while(column < columnHeight)
			{
				if((row >= 0) && (row < heightInTiles) && (column >= 0) && (column < widthInTiles))
				{
					i = row*widthInTiles+column;
					autoTile(i);
					updateTile(i);
				}
				column++;
			}
			row++;
		}
		
		return ok;
	}
	
	/**
	 * Change the data and graphic of a tile in the tilemap.
	 * 
	 * @param	Index			The slot in the data array (Y * widthInTiles + X) where this tile is stored.
	 * @param	Tile			The new integer data you wish to inject.
	 * 
	 * @return	Whether or not the tile was actually changed.
	 */
	public boolean setTileByIndex(int Index, int Tile)
	{
		return setTileByIndex(Index, Tile, true);
	}
	
	
	/**
	 * Adjust collision settings and/or bind a callback function to a range of tiles.
	 * This callback function, if present, is triggered by calls to overlap() or overlapsWithCallback().
	 * 
	 * @param	Tile			The tile or tiles you want to adjust.
	 * @param	AllowCollisions	Modify the tile or tiles to only allow collisions from certain directions, use FlxObject constants NONE, ANY, LEFT, RIGHT, etc.  Default is "ANY".
	 * @param	Callback		The function to trigger, e.g. <code>lavaCallback(Tile:FlxTile, Object:FlxObject)</code>.
	 * @param	CallbackFilter	If you only want the callback to go off for certain classes or objects based on a certain class, set that class here.
	 * @param	Range			If you want this callback to work for a bunch of different tiles, input the range here.  Default value is 1.
	 */
	public void setTileProperties(int Tile,int AllowCollisions,AFlxTile Callback,Class<? extends FlxObject> CallbackFilter,int Range)
	{
		if(Range <= 0)
			Range = 1;
		FlxTile tile;
		int i = Tile;
		int l = Tile+Range;
		while(i < l)
		{
			tile = _tileObjects.get(i++);
			tile.allowCollisions = AllowCollisions;
			tile.callback = Callback;
			tile.filter = CallbackFilter;
		}
	}	
	
	/**
	 * Adjust collision settings and/or bind a callback function to a range of tiles.
	 * This callback function, if present, is triggered by calls to overlap() or overlapsWithCallback().
	 * 
	 * @param	Tile			The tile or tiles you want to adjust.
	 * @param	AllowCollisions	Modify the tile or tiles to only allow collisions from certain directions, use FlxObject constants NONE, ANY, LEFT, RIGHT, etc.  Default is "ANY".
	 * @param	Callback		The function to trigger, e.g. <code>lavaCallback(Tile:FlxTile, Object:FlxObject)</code>.
	 * @param	CallbackFilter	If you only want the callback to go off for certain classes or objects based on a certain class, set that class here.
	 */
	public void setTileProperties(int Tile,int AllowCollisions,AFlxTile Callback,Class<? extends FlxObject> CallbackFilter)
	{
		setTileProperties(Tile, AllowCollisions, Callback, CallbackFilter, 1);
	}
	
	/**
	 * Adjust collision settings and/or bind a callback function to a range of tiles.
	 * This callback function, if present, is triggered by calls to overlap() or overlapsWithCallback().
	 * 
	 * @param	Tile			The tile or tiles you want to adjust.
	 * @param	AllowCollisions	Modify the tile or tiles to only allow collisions from certain directions, use FlxObject constants NONE, ANY, LEFT, RIGHT, etc.  Default is "ANY".
	 * @param	Callback		The function to trigger, e.g. <code>lavaCallback(Tile:FlxTile, Object:FlxObject)</code>.
	 */
	public void setTileProperties(int Tile,int AllowCollisions,AFlxTile Callback)
	{
		setTileProperties(Tile, AllowCollisions, Callback, null, 1);
	}
	
	/**
	 * Adjust collision settings and/or bind a callback function to a range of tiles.
	 * This callback function, if present, is triggered by calls to overlap() or overlapsWithCallback().
	 * 
	 * @param	Tile			The tile or tiles you want to adjust.
	 * @param	AllowCollisions	Modify the tile or tiles to only allow collisions from certain directions, use FlxObject constants NONE, ANY, LEFT, RIGHT, etc.  Default is "ANY".
	 */
	public void setTileProperties(int Tile,int AllowCollisions)
	{
		setTileProperties(Tile, AllowCollisions, null, null, 1);
	}
	
	/**
	 * Adjust collision settings and/or bind a callback function to a range of tiles.
	 * This callback function, if present, is triggered by calls to overlap() or overlapsWithCallback().
	 * 
	 * @param	Tile			The tile or tiles you want to adjust.
	 */
	public void setTileProperties(int Tile)
	{
		setTileProperties(Tile, ANY, null, null, 1);
	}
	
	
	/**
	 * Call this function to lock the automatic camera to the map's edges.
	 * 
	 * @param	Camera			Specify which game camera you want.  If null getScreenXY() will just grab the first global camera.
	 * @param	Border			Adjusts the camera follow boundary by whatever number of tiles you specify here.  Handy for blocking off deadends that are offscreen, etc.  Use a negative number to add padding instead of hiding the edges.
	 * @param	UpdateWorld		Whether to update the collision system's world size, default value is true.
	 */
	public void follow(FlxCamera Camera,int Border, boolean UpdateWorld)
	{
		if(Camera == null)
			Camera = FlxG.camera;
		Camera.setBounds(x+Border*_tileWidth,y+Border*_tileHeight,width-Border*_tileWidth*2,height-Border*_tileHeight*2,UpdateWorld);
	}
	
	/**
	 * Call this function to lock the automatic camera to the map's edges.
	 * 
	 * @param	Camera			Specify which game camera you want.  If null getScreenXY() will just grab the first global camera.
	 * @param	Border			Adjusts the camera follow boundary by whatever number of tiles you specify here.  Handy for blocking off deadends that are offscreen, etc.  Use a negative number to add padding instead of hiding the edges.
	 */
	public void follow(FlxCamera Camera,int Border)
	{
		follow(Camera, Border, true);
	}
	
	/**
	 * Call this function to lock the automatic camera to the map's edges.
	 * 
	 * @param	Camera			Specify which game camera you want.  If null getScreenXY() will just grab the first global camera.
	 */
	public void follow(FlxCamera Camera)
	{
		follow(Camera, 0, true);
	}
	
	/**
	 * Call this function to lock the automatic camera to the map's edges.
	 */
	public void follow()
	{
		follow(null, 0, true);
	}
	
	/**
	 * Get the world coordinates and size of the entire tilemap as a <code>FlxRect</code>.
	 * 
	 * @param	Bounds		Optional, pass in a pre-existing <code>FlxRect</code> to prevent instantiation of a new object.
	 * 
	 * @return	A <code>FlxRect</code> containing the world coordinates and size of the entire tilemap.
	 */
	public FlxRect getBounds(FlxRect Bounds)
	{
		if(Bounds == null)
			Bounds = new FlxRect();
		return Bounds.make(x,y,width,height);
	}
	
	/**
	 * Get the world coordinates and size of the entire tilemap as a <code>FlxRect</code>.
	 * 
	 * @return	A <code>FlxRect</code> containing the world coordinates and size of the entire tilemap.
	 */
	public FlxRect getBounds()
	{
		return getBounds(null);
	}
	
	
	/**
	 * Shoots a ray from the start point to the end point.
	 * If/when it passes through a tile, it stores that point and returns false.
	 * 
	 * @param	Start		The world coordinates of the start of the ray.
	 * @param	End			The world coordinates of the end of the ray.
	 * @param	Result		A <code>Point</code> object containing the first wall impact.
	 * @param	Resolution	Defaults to 1, meaning check every tile or so.  Higher means more checks!
	 * @return	Returns true if the ray made it from Start to End without hitting anything.  Returns false and fills Result if a tile was hit.
	 */
	public boolean ray(FlxPoint Start, FlxPoint End, FlxPoint Result, float Resolution)
	{
		float step = _tileWidth;
		if(_tileHeight < _tileWidth)
			step = _tileHeight;
		step /= Resolution;
		float deltaX = End.x - Start.x;
		float deltaY = End.y - Start.y;
		float distance = (float) Math.sqrt(deltaX*deltaX + deltaY*deltaY);
		int steps = (int) Math.ceil(distance/step);
		float stepX = deltaX/steps;
		float stepY = deltaY/steps;
		float curX = Start.x - stepX - x;
		float curY = Start.y - stepY - y;
		int tileX;
		int tileY;
		int i = 0;
		while(i < steps)
		{
			curX += stepX;
			curY += stepY;
			
			if((curX < 0) || (curX > width) || (curY < 0) || (curY > height))
			{
				i++;
				continue;
			}
			
			tileX = (int) (curX/_tileWidth);
			tileY = (int) (curY/_tileHeight);
			if((_tileObjects.get(_data.get(tileY*widthInTiles+tileX))).allowCollisions != NONE)
			{
				//Some basic helper stuff
				tileX *= _tileWidth;
				tileY *= _tileHeight;
				float rx = 0;
				float ry = 0;
				float q;
				float lx = curX-stepX;
				float ly = curY-stepY;
				
				//Figure out if it crosses the X boundary
				q = tileX;
				if(deltaX < 0)
					q += _tileWidth;
				rx = q;
				ry = ly + stepY*((q-lx)/stepX);
				if((ry > tileY) && (ry < tileY + _tileHeight))
				{
					if(Result == null)
						Result = new FlxPoint();
					Result.x = rx;
					Result.y = ry;
					return false;
				}
				
				//Else, figure out if it crosses the Y boundary
				q = tileY;
				if(deltaY < 0)
					q += _tileHeight;
				rx = lx + stepX*((q-ly)/stepY);
				ry = q;
				if((rx > tileX) && (rx < tileX + _tileWidth))
				{
					if(Result == null)
						Result = new FlxPoint();
					Result.x = rx;
					Result.y = ry;
					return false;
				}
				return true;
			}
			i++;
		}
		return true;
	}
	
	/**
	 * Shoots a ray from the start point to the end point.
	 * If/when it passes through a tile, it stores that point and returns false.
	 * 
	 * @param	Start		The world coordinates of the start of the ray.
	 * @param	End			The world coordinates of the end of the ray.
	 * @param	Result		A <code>Point</code> object containing the first wall impact.
	 * @return	Returns true if the ray made it from Start to End without hitting anything.  Returns false and fills Result if a tile was hit.
	 */
	public boolean ray(FlxPoint Start, FlxPoint End, FlxPoint Result)
	{
		return ray(Start, End, Result, 1);
	}
	
	/**
	 * Shoots a ray from the start point to the end point.
	 * If/when it passes through a tile, it stores that point and returns false.
	 * 
	 * @param	Start		The world coordinates of the start of the ray.
	 * @param	End			The world coordinates of the end of the ray.
	 * @return	Returns true if the ray made it from Start to End without hitting anything.  Returns false and fills Result if a tile was hit.
	 */
	public boolean ray(FlxPoint Start, FlxPoint End)
	{
		return ray(Start, End, null, 1);
	}
	
	
	/**
	 * Converts a one-dimensional array of tile data to a comma-separated string.
	 * 
	 * @param	Data		An array full of integer tile references.
	 * @param	Width		The number of tiles in each row.
	 * @param	Invert		Recommended only for 1-bit arrays - changes 0s to 1s and vice versa.
	 * 
	 * @return	A comma-separated string containing the level data in a <code>FlxTilemap</code>-friendly format.
	 */
	static public String arrayToCSV(int[] Data, int Width, boolean Invert)
	{
		int row = 0;
		int column;
		StringBuilder csv = new StringBuilder();
		int Height = Data.length / Width;
		int index;
		while(row < Height)
		{
			column = 0;
			while(column < Width)
			{
				index = Data[row*Width+column];
				if(Invert)
				{
					if(index == 0)
						index = 1;
					else if(index == 1)
						index = 0;
				}
				
				if(column == 0)
				{
					if(row == 0)
						csv.append(index);
					else
						csv.append("\n"+index);
				}
				else
					csv.append(","+index);
				column++;
			}
			row++;
		}
		return csv.toString();
	}
	
	/**
	 * Converts a one-dimensional array of tile data to a comma-separated string.
	 * 
	 * @param	Data		An array full of integer tile references.
	 * @param	Width		The number of tiles in each row.
	 * 
	 * @return	A comma-separated string containing the level data in a <code>FlxTilemap</code>-friendly format.
	 */
	static public String arrayToCSV(int[] Data, int Width)
	{
		return arrayToCSV(Data, Width, false);
	}
	
	static public String array2DToCSV(int[][] Data)
	{
		int row = 0;
		int column;
		StringBuilder csv = new StringBuilder();
		int Height = Data.length;
		int Width = Data[0].length;
		int index;
		while(row < Height)
		{
			column = 0;
			while(column < Width)
			{				
				index = Data[row][column];
				
				if(column == 0)
				{
					if(row == 0)
						csv.append(index);
					else
						csv.append("\n"+index);
				}
				else
					csv.append(","+index);
				column++;
			}
			row++;
		}
		return csv.toString();
	}
	
	static public String tilemapToCSV(TiledMap Map, int Layer)
	{
		int row = 0;
		int column;
		StringBuilder csv = new StringBuilder();		
		int Height = Map.layers.get(Layer).getHeight();
		int Width = Map.layers.get(Layer).getWidth();
		int[][] Data = Map.layers.get(Layer).tiles;
		int index;
		while(row < Height)
		{
			column = 0;
			while(column < Width)
			{				
				index = Data[row][column];
				
				if(column == 0)
				{
					if(row == 0)
						csv.append(index);
					else
						csv.append("\n"+index);
				}
				else
					csv.append(","+index);
				column++;
			}
			row++;
		}
		return csv.toString();
	}
	
	/**
	 * An internal function used by the binary auto-tilers.
	 * 
	 * @param	Index		The index of the tile you want to analyze.
	 */
	protected void autoTile(int Index)
	{
		if(_data.get(Index) == 0)
			return;
		
		_data.set(Index,0);
		if((Index-widthInTiles < 0) || (_data.get(Index-widthInTiles) > 0)) 		//UP
			_data.set(Index, _data.get(Index)+1);
		if((Index%widthInTiles >= widthInTiles-1) || (_data.get(Index+1) > 0)) 		//RIGHT
			_data.set(Index, _data.get(Index)+2);
		if((Index+widthInTiles >= totalTiles) || (_data.get(Index+widthInTiles) > 0)) //DOWN
			_data.set(Index, _data.get(Index)+4);
		if((Index%widthInTiles <= 0) || (_data.get(Index-1) > 0)) 					//LEFT
			_data.set(Index, _data.get(Index)+8);
		if((auto == ALT) && (_data.get(Index) == 15))	//The alternate algo checks for interior corners
		{
			if((Index%widthInTiles > 0) && (Index+widthInTiles < totalTiles) && (_data.get(Index+widthInTiles-1) <= 0))
				_data.set(Index, 1);	//BOTTOM LEFT OPEN
			if((Index%widthInTiles > 0) && (Index-widthInTiles >= 0) && (_data.get(Index+widthInTiles-1) <= 0))
				_data.set(Index, 2);	//TOP LEFT OPEN
			if((Index%widthInTiles < widthInTiles-1) && (Index-widthInTiles >= 0) && (_data.get(Index+widthInTiles+1) <= 0))
				_data.set(Index, 4);	//TOP RIGHT OPEN
			if((Index%widthInTiles < widthInTiles-1) && (Index+widthInTiles < totalTiles) && (_data.get(Index+widthInTiles+1) <= 0))
				_data.set(Index, 8); 	//BOTTOM RIGHT OPEN
		}
		_data.set(Index, _data.get(Index)+1);
	}
	
	
	/**
	 * Internal function used in setTileByIndex() and the constructor to update the map.
	 * 
	 * @param	Index		The index of the tile you want to update.
	 */
	protected void updateTile(int Index)
	{			
		/*FlxTile tile = _tileObjects.get(_data.get(Index));
		if((tile == null) || !tile.visible)
		{
//			_rects[Index] = null;
			return;
		}
		int rx = (_data.get(Index)-_startingIndex)*_tileWidth;
		int ry = 0;
		if(rx >= _tiles.getRegionWidth())
		{
			ry = (rx/_tiles.getRegionWidth())*_tileHeight;
			rx %= _tiles.getRegionWidth();
		}*/
		//TODO: update the _data file. Not needed anymore
		//_rects[Index] = (new Rectangle(rx,ry,_tileWidth,_tileHeight));
	}
}
