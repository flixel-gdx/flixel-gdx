package org.flixel.data;

import java.util.ArrayList;

import org.flixel.FlxG;
import org.flixel.FlxMonitor;

import flash.display.BitmapData;

/**
 * Contains all the logic for the developer console.
 * This class is automatically created by FlxGame.
 */
public class FlxConsole
{

	public FlxMonitor mtrUpdate;
	public FlxMonitor mtrRender;
	public FlxMonitor mtrTotal;
	
	/**
	 * @private
	 */
	protected final int MAX_CONSOLE_LINES = 256;
	/**
	 * @private
	 */
	protected BitmapData _console;
	/**
	 * @private
	 */
	protected String _text;
	/**
	 * @private
	 */
	protected String _fpsDisplay;
	/**
	 * @private
	 */
	protected String _extraDisplay;
	/**
	 * @private
	 */
	protected int _curFPS;
	/**
	 * @private
	 */
	protected ArrayList<String> _lines;
	/**
	 * @private
	 */
	protected float _Y;
	/**
	 * @private
	 */
	protected float _YT;
	/**
	 * @private
	 */
	protected int _bx;
	/**
	 * @private
	 */
	protected int _by;
	/**
	 * @private
	 */
	protected int _byt;
	
	public boolean visible;
	
	/**
	 * Constructor
	 * 
	 * @param	X		X position of the console
	 * @param	Y		Y position of the console
	 * @param	Zoom	The game's zoom level
	 */
	public FlxConsole(int X, int Y, int Zoom)
	{
		super();
		
		visible = false;
		//x = X*Zoom;		
		_by = Y*Zoom;
		_byt = _by - FlxG.height*Zoom;
//		_YT = _Y = y = _byt;
		_YT = _Y = _byt;
		//BitmapData tmp = new BitmapData(FlxG.width * Zoom, FlxG.height * Zoom, true, 0x7F000000);
		//addChild(tmp);
		
		mtrUpdate = new FlxMonitor(16);
		mtrRender = new FlxMonitor(16);
		mtrTotal = new FlxMonitor(16);
		
		
		
		/*_text = new TextField();
		_text.width = tmp.width;
		_text.height = tmp.height;
		_text.multiline = true;
		_text.wordWrap = true;
		_text.embedFonts = true;
		_text.selectable = false;
		_text.antiAliasType = AntiAliasType.NORMAL;
		_text.gridFitType = GridFitType.PIXEL;
		_text.defaultTextFormat = new TextFormat("system",8,0xffffff);
		addChild(_text);

		_fpsDisplay = new TextField();
		_fpsDisplay.width = 100;
		_fpsDisplay.x = tmp.width-100;
		_fpsDisplay.height = 20;
		_fpsDisplay.multiline = true;
		_fpsDisplay.wordWrap = true;
		_fpsDisplay.embedFonts = true;
		_fpsDisplay.selectable = false;
		_fpsDisplay.antiAliasType = AntiAliasType.NORMAL;
		_fpsDisplay.gridFitType = GridFitType.PIXEL;
		_fpsDisplay.defaultTextFormat = new TextFormat("system",16,0xffffff,true,null,null,null,null,"right");
		addChild(_fpsDisplay);
		
		_extraDisplay = new TextField();
		_extraDisplay.width = 100;
		_extraDisplay.x = tmp.width-100;
		_extraDisplay.height = 64;
		_extraDisplay.y = 20;
		_extraDisplay.alpha = 0.5;
		_extraDisplay.multiline = true;
		_extraDisplay.wordWrap = true;
		_extraDisplay.embedFonts = true;
		_extraDisplay.selectable = false;
		_extraDisplay.antiAliasType = AntiAliasType.NORMAL;
		_extraDisplay.gridFitType = GridFitType.PIXEL;
		_extraDisplay.defaultTextFormat = new TextFormat("system",8,0xffffff,true,null,null,null,null,"right");
		addChild(_extraDisplay);*/
		
		_lines = new ArrayList<String>();
	}
	
	public void update()
	{
		
	}
	public void log(String string)
	{
		
	}

}
