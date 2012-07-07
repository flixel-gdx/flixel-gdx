package org.flixel.examples.pathfinding;

import org.flixel.*;
import org.flixel.event.AFlxButton;

public class PlayState extends FlxState
{
	/*
     * Tile width
     */
    private static final int TILE_WIDTH = 12;
    /*
     * Tile height
     */
    private static final int TILE_HEIGHT = 12;
                
    /*
     * Unit value for action go
     */
    private static final int ACTION_GO = 1;
    /*
     * Unit value for action idle
     */
    private static final int ACTION_IDLE = 0;
                
    /*
     * Map
     */
    private FlxTilemap _map;
                
    /*
     * Goal sprite
     */
    private FlxSprite _goal;
                
    /*
     * Unit sprite
     */
    private FlxSprite _unit;
    /*
     * Unit action
     */
    private int _action;
                
    /*
     * Button to move unit to Goal
     */
    private FlxButton _btnFindPath;
    /*
     * Button to stop unit
     */
    private FlxButton _btnStopUnit;
    /*
     * Button to reset unit to start point
     */
    private FlxButton _btnResetUnit;
    /*
     * Legend
     */
    private FlxText _legends;
                
    @Override
    public void create()
    {
    	FlxG.setBgColor(0xFF000000);
        FlxG.setFramerate(50);
        FlxG.setFlashFramerate(50);
                        
        //Load _datamap to _map and add to PlayState
        _map = new FlxTilemap();
        _map.loadMap(Asset.DataMap, Asset.ImgTiles, TILE_WIDTH, TILE_HEIGHT, 0, 1);
        add(_map);
                        
        //Set goal coordinate and add goal to PlayState
        _goal = new FlxSprite(_map.width - TILE_WIDTH, _map.height - TILE_HEIGHT).makeGraphic(TILE_WIDTH, TILE_HEIGHT, 0xffffff00);
        add(_goal);
                        
        //Set unit smaller than tile so when following path not collide with map and add unit to PlayState
        _unit  = new FlxSprite(0, 0).makeGraphic(TILE_WIDTH - 2, TILE_HEIGHT - 2, 0xffff0000);
        _action = ACTION_IDLE;
        add(_unit);
                        
        //Add button move to goal to PlayState
        _btnFindPath = new FlxButton(320, 10, "Move To Goal", new AFlxButton(){@Override public void onDown(){moveToGoal();}});
        add(_btnFindPath);

        //Add button stop unit to PlayState
        _btnStopUnit = new FlxButton(320, 30, "Stop Unit", new AFlxButton(){@Override public void onDown(){stopUnit();}});
        add(_btnStopUnit);
                        
        //Add button reset unit to PlayState
        _btnResetUnit = new FlxButton(320, 50, "Reset Unit", new AFlxButton(){@Override public void onDown(){resetUnit();}});
        add(_btnResetUnit);
                        
        //Add label for legend
        _legends = new FlxText(320, 90, 100, "Click in map to\nplace or\nremove tile\n\nLegends:\nRed:Unit\nYellow:Goal\nBlue:Wall\nWhite:Path");
        add(_legends);
        
       // add(_unit);
    }
                
    @Override
    public void draw(FlxCamera Camera)
    {
        super.draw(Camera);
                        
        //To draw path
        if (_unit.path != null)
        {
            _unit.path.drawDebug();
        }
    }
                
    @Override
    public void destroy()
    {
        super.destroy();
        _map = null;
        _goal = null;
        _unit = null;
        _btnFindPath = null;
        _btnStopUnit = null;
        _btnResetUnit = null;
        _legends = null;
    }
                
    @Override
    public void update()
    {
        super.update();

        //Set unit to collide with map
    	FlxG.collide(_unit, _map);

    	//Check mouse pressed and unit action
        if (FlxG.mouse.justPressed() && _action == ACTION_IDLE) 
        {
            //Get data map coordinate
            int mx = FlxG.mouse.screenX / TILE_WIDTH;
            int my = FlxG.mouse.screenY / TILE_HEIGHT;
                                
            //Change tile toogle
            _map.setTile(mx, my, 1 - _map.getTile(mx, my), true);
        }
                        
        //Check if reach goal
        if (_action == ACTION_GO)
        {
            if (_unit.pathSpeed == 0)
            {
            	stopUnit();
            }
        }
    }
                
    private void moveToGoal()
    {
    	if (_action == ACTION_IDLE)
    	{       
    		//Find path to goal from unit to goal
    		FlxPath path = _map.findPath(new FlxPoint(_unit.x + _unit.width / 2, _unit.y + _unit.height / 2), new FlxPoint(_goal.x + _goal.width / 2, _goal.y + _goal.height / 2));
               
    		//Tell unit to follow path
    		_unit.followPath(path);
    		_action = ACTION_GO;
    	}
    }
    
    private void stopUnit()
    {
    	//Stop unit and destroy unit path
    	_action = ACTION_IDLE;
    	_unit.stopFollowingPath(true);
    	_unit.velocity.x = _unit.velocity.y = 0;
    }
    
    private void resetUnit()
    {
    	//Reset _unit position
    	_unit.x = 0;
    	_unit.y = 0;
    	
    	//Stop unit
    	if (_action == ACTION_GO)
    	{
    		stopUnit();
    	}
    }
}