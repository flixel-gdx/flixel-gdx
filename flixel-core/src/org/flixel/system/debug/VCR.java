package org.flixel.system.debug;

import org.flixel.FlxG;
import org.flixel.FlxU;
import org.flixel.system.gdx.text.GdxTextField;

import com.badlogic.gdx.files.FileHandle;

import flash.events.Event;
import flash.events.MouseEvent;
import flash.text.TextField;
import flash.text.TextFormat;

/**
 * This class contains the record, stop, play, and step 1 frame buttons seen on the top edge of the debugger overlay.
 * 
 * @author Thomas Weston
 */
public class VCR
{
	protected String ImgOpen = "../../data/vcr/open.png";
	protected String ImgRecordOff = "../../data/vcr/record_off.png";
	protected String ImgRecordOn = "../../data/vcr/record_on.png";
	protected String ImgStop = "../../data/vcr/stop.png";
	protected String ImgFlixel = "../../data/vcr/flixel.png";
	protected String ImgRestart = "../../data/vcr/restart.png";
	protected String ImgPause = "../../data/vcr/pause.png";
	protected String ImgPlay = "../../data/vcr/play.png";
	protected String ImgStep = "../../data/vcr/step.png";
	
	//static protected final FILE_TYPES:Array = [new FileFilter("Flixel Game Recording", "*.fgr")];
	static protected final String DEFAULT_FILE_NAME = "replay.fgr";

	/**
	 * Whether the debugger has been paused.
	 */
	public boolean paused;
	/**
	 * Whether a "1 frame step forward" was requested.
	 */
	public boolean stepRequested;

	//protected var _open:Bitmap;
	//protected var _recordOff:Bitmap;
	//protected var _recordOn:Bitmap;
	//protected var _stop:Bitmap;
	//protected var _flixel:Bitmap;
	//protected var _restart:Bitmap;
	//protected var _pause:Bitmap;
	//protected var _play:Bitmap;
	//protected var _step:Bitmap;

	protected boolean _overOpen;
	protected boolean _overRecord;
	protected boolean _overRestart;
	protected boolean _overPause;
	protected boolean _overStep;

	protected boolean _pressingOpen;
	protected boolean _pressingRecord;
	protected boolean _pressingRestart;
	protected boolean _pressingPause;
	protected boolean _pressingStep;

	protected FileHandle _file;

	protected TextField _runtimeDisplay;
	protected int _runtime;

	/**
	 * Creates the "VCR" control panel for debugger pausing, stepping, and recording.
	 */
	public VCR()
	{
		super();

		//int spacing = 7;

		//_open = new ImgOpen();
		//addChild(_open);

		//_recordOff = new ImgRecordOff();
		//_recordOff.x = _open.x + _open.width + spacing;
		//addChild(_recordOff);

		//_recordOn = new ImgRecordOn();
		//_recordOn.x = _recordOff.x;
		//_recordOn.visible = false;
		//addChild(_recordOn);

		//_stop = new ImgStop();
		//_stop.x = _recordOff.x;
		//_stop.visible = false;
		//addChild(_stop);

		//_flixel = new ImgFlixel();
		//_flixel.x = _recordOff.x + _recordOff.width + spacing;
		//addChild(_flixel);

		//_restart = new ImgRestart();
		//_restart.x = _flixel.x + _flixel.width + spacing;
		//addChild(_restart);

		//_pause = new ImgPause();
		//_pause.x = _restart.x + _restart.width + spacing;
		//addChild(_pause);

		//_play = new ImgPlay();
		//_play.x = _pause.x;
		//_play.visible = false;
		//addChild(_play);

		//_step = new ImgStep();
		//_step.x = _pause.x + _pause.width + spacing;
		//addChild(_step);

		_runtimeDisplay = new GdxTextField();
		//_runtimeDisplay.width = width;
		//_runtimeDisplay.x = width;
		_runtimeDisplay.y = -2;
		//_runtimeDisplay.multiline = false;
		//_runtimeDisplay.wordWrap = false;
		//_runtimeDisplay.selectable = false;
		_runtimeDisplay.defaultTextFormat = new TextFormat("Courier",12,0xffffff,false,false,false,null,null,"center");
		//_runtimeDisplay.visible = false;
		//addChild(_runtimeDisplay);
		_runtime = 0;

		stepRequested = false;
		_file = null;

		unpress();
		checkOver();
		updateGUI();

		//addEventListener(Event.ENTER_FRAME,init);
	}

	/**
	 * Clean up memory.
	 */
	public void destroy()
	{
		_file = null;
		
		//removeChild(_open);
		//_open = null;
		//removeChild(_recordOff);
		//_recordOff = null;
		//removeChild(_recordOn);
		//_recordOn = null;
		//removeChild(_stop);
		//_stop = null;
		//removeChild(_flixel);
		//_flixel = null;
		//removeChild(_restart);
		//_restart = null;
		//removeChild(_pause);
		//_pause = null;
		//removeChild(_play);
		//_play = null;
		//removeChild(_step);
		//_step = null;

		//parent.removeEventListener(MouseEvent.MOUSE_MOVE,onMouseMove);
		//parent.removeEventListener(MouseEvent.MOUSE_DOWN,onMouseDown);
		//parent.removeEventListener(MouseEvent.MOUSE_UP,onMouseUp);
	}

	/**
	 * Usually called by FlxGame when a requested recording has begun.
	 * Just updates the VCR GUI so the buttons are in the right state.
	 */
	public void recording()
	{
		//_stop.visible = false;
		//_recordOff.visible = false;
		//_recordOn.visible = true;
	}

	/**
	 * Usually called by FlxGame when a replay has been stopped.
	 * Just updates the VCR GUI so the buttons are in the right state.
	 */
	public void stopped()
	{
		//_stop.visible = false;
		//_recordOn.visible = false;
		//_recordOff.visible = true;
	}

	/**
	 * Usually called by FlxGame when a requested replay has begun.
	 * Just updates the VCR GUI so the buttons are in the right state.
	 */
	public void playing()
	{
		//_recordOff.visible = false;
		//_recordOn.visible = false;
		//_stop.visible = true;
	}

	/**
	 * Just updates the VCR GUI so the runtime displays roughly the right thing.
	 */
	public void updateRuntime(int Time)
	{
		_runtime += Time;
		_runtimeDisplay.setText(FlxU.formatTime(_runtime/1000f,true));
		//if(!_runtimeDisplay.visible)
		//	_runtimeDisplay.visible = true;
	}

	//*** ACTUAL BUTTON BEHAVIORS ***//

	/**
	 * Called when the "open file" button is pressed.
	 * Opens the file dialog and registers event handlers for the file dialog.
	 */
	public void onOpen()
	{
		//_file = Gdx.files.external(path);
		//_file.addEventListener(Event.SELECT, onOpenSelect);
		//_file.addEventListener(Event.CANCEL, onOpenCancel);
		//_file.browse(FILE_TYPES);
	}

	/**
	 * Called when a file is picked from the file dialog.
	 * Attempts to load the file and registers file loading event handlers.
	 * 
	 * @param	E	Flash event.
	 */
	protected void onOpenSelect(Event E)
	{
		//_file.removeEventListener(Event.SELECT, onOpenSelect);
		//_file.removeEventListener(Event.CANCEL, onOpenCancel);

		//_file.addEventListener(Event.COMPLETE, onOpenComplete);
		//_file.addEventListener(IOErrorEvent.IO_ERROR, onOpenError);
		//_file.load();
	}

	/**
	 * Called when a file is opened successfully.
	 * If there's stuff inside, then the contents are loaded into a new replay.
	 * 
	 * @param	E	Flash Event.
	 */
	protected void onOpenComplete(Event E)
	{
		//_file.removeEventListener(Event.COMPLETE, onOpenComplete);
		//_file.removeEventListener(IOErrorEvent.IO_ERROR, onOpenError);

		//Turn the file into a giant string
		String fileContents = null;
		//var data:ByteArray = _file.data;
		//if(data != null)
		//	fileContents = data.readUTFBytes(data.bytesAvailable);
		_file = null;
		if((fileContents == null) || (fileContents.length() <= 0))
		{
			FlxG.log("ERROR: Empty flixel gameplay record.");
			return;
		}

		//FlxG.loadReplay(fileContents);
	}

	/**
	 * Called if the open file dialog is canceled.
	 * 
	 * @param	E	Flash Event.
	 */
	protected void onOpenCancel(Event E)
	{
		//_file.removeEventListener(Event.SELECT, onOpenSelect);
		//_file.removeEventListener(Event.CANCEL, onOpenCancel);
		_file = null;
	}

	/**
	 * Called if there is a file open error.
	 * 
	 * @param	E	Flash Event.
	 */
	protected void onOpenError(Event E)
	{
		//_file.removeEventListener(Event.COMPLETE, onOpenComplete);
		//_file.removeEventListener(IOErrorEvent.IO_ERROR, onOpenError);
		_file = null;
		FlxG.log("ERROR: Unable to open flixel gameplay record.");
	}

	/**
	 * Called when the user presses the white record button.
	 * If Alt is pressed, the current state is reset, and a new recording is requested.
	 * If Alt is NOT pressed, the game is reset, and a new recording is requested.
	 * 
	 * @param	StandardMode	Whether to reset the whole game, or just this <code>FlxState</code>.  StandardMode == false is useful for recording demos or attract modes.
	 */
	public void onRecord(boolean StandardMode)
	{
		//if(_play.visible)
		//	onPlay();
		FlxG.recordReplay(StandardMode);
	}

	/**
	 * Called when the user presses the white record button.
	 * If Alt is pressed, the current state is reset, and a new recording is requested.
	 * If Alt is NOT pressed, the game is reset, and a new recording is requested.
	 */
	public void onRecord()
	{
		onRecord(false);
	}

	/**
	 * Called when the user presses the red record button.
	 * Stops the current recording, opens the save file dialog, and registers event handlers.
	 */
	public void stopRecording()
	{
		String data = FlxG.stopRecording();
		if((data != null) && (data.length() > 0))
		{
			//_file = new FileReference();
			//_file.addEventListener(Event.COMPLETE, onSaveComplete);
			//_file.addEventListener(Event.CANCEL,onSaveCancel);
			//_file.addEventListener(IOErrorEvent.IO_ERROR, onSaveError);
			//_file.save(data, DEFAULT_FILE_NAME);
		}
	}

	/**
	 * Called when the file is saved successfully.
	 * 
	 * @param	E	Flash Event.
	 */
	protected void onSaveComplete(Event E)
	{
		//_file.removeEventListener(Event.COMPLETE, onSaveComplete);
		//_file.removeEventListener(Event.CANCEL,onSaveCancel);
		//_file.removeEventListener(IOErrorEvent.IO_ERROR, onSaveError);
		_file = null;
		FlxG.log("FLIXEL: successfully saved flixel gameplay record.");
	}

	/**
	 * Called when the save file dialog is cancelled.
	 * 
	 * @param	E	Flash Event.
	 */
	protected void onSaveCancel(Event E)
	{
		//_file.removeEventListener(Event.COMPLETE, onSaveComplete);
		//_file.removeEventListener(Event.CANCEL,onSaveCancel);
		//_file.removeEventListener(IOErrorEvent.IO_ERROR, onSaveError);
		_file = null;
	}

	/**
	 * Called if there is an error while saving the gameplay recording.
	 * 
	 * @param	E	Flash Event.
	 */
	protected void onSaveError(Event E)
	{
		//_file.removeEventListener(Event.COMPLETE, onSaveComplete);
		//_file.removeEventListener(Event.CANCEL,onSaveCancel);
		//_file.removeEventListener(IOErrorEvent.IO_ERROR, onSaveError);
		_file = null;
		FlxG.log("ERROR: problem saving flixel gameplay record.");
	}

	/**
	 * Called when the user presses the stop button.
	 * Stops the current replay.
	 */
	public void onStop()
	{
		FlxG.stopReplay();
	}

	/**
	 * Called when the user presses the Rewind-looking button.
	 * If Alt is pressed, the entire game is reset.
	 * If Alt is NOT pressed, only the current state is reset.
	 * The GUI is updated accordingly.
	 * 
	 * @param	StandardMode	Whether to reset the current game (== true), or just the current state.  Just resetting the current state can be very handy for debugging.
	 */
	public void onRestart(boolean StandardMode)
	{
		FlxG.reloadReplay(StandardMode);
		//if(false)
		//{
		//	_recordOff.visible = false;
		//	_recordOn.visible = false;
		//	_stop.visible = true;
		// }
	}

	/**
	 * Called when the user presses the Pause button.
	 * This is different from user-defined pause behavior, or focus lost behavior.
	 * Does NOT pause music playback!!
	 */
	public void onPause()
	{
		paused = true;
		//_pause.visible = false;
		//_play.visible = true;
	}

	/**
	 * Called when the user presses the Play button.
	 * This is different from user-defined unpause behavior, or focus gained behavior.
	 */
	public void onPlay()
	{
		paused = false;
		//_play.visible = false;
		//_pause.visible = true;
	}

	/**
	 * Called when the user presses the fast-forward-looking button.
	 * Requests a 1-frame step forward in the game loop.
	 */
	public void onStep()
	{
		if(!paused)
			onPause();
		stepRequested = true;
	}

	//***EVENT HANDLERS***//

	/**
	 * Just sets up basic mouse listeners, a la FlxWindow.
	 * 
	 * @param	E	Flash event.
	 */
	protected void init(Event E)
	{
		//if(root == null)
		//	return;
		//removeEventListener(Event.ENTER_FRAME,init);

		//parent.addEventListener(MouseEvent.MOUSE_MOVE,onMouseMove);
		//parent.addEventListener(MouseEvent.MOUSE_DOWN,onMouseDown);
		//parent.addEventListener(MouseEvent.MOUSE_UP,onMouseUp);
	}

	/**
	 * If the mouse moves, check to see if any buttons should be highlighted.
	 * 
	 * @param	E	Flash mouse event.
	 */
	protected void onMouseMove(MouseEvent E)
	{
		if(!checkOver())
			unpress();
		updateGUI();
	}

	/**
	 * If the mouse is pressed down, check to see if the user started pressing down a specific button.
	 * 
	 * @param	E	Flash mouse event.
	 */
	protected void onMouseDown(MouseEvent E)
	{
		unpress();
		if(_overOpen)
			_pressingOpen = true;
		if(_overRecord)
			_pressingRecord = true;
		if(_overRestart)
			_pressingRestart = true;
		if(_overPause)
			_pressingPause = true;
		if(_overStep)
			_pressingStep = true;
	}

	/**
	 * If the mouse is released, check to see if it was released over a button that was pressed.
	 * If it was, take the appropriate action based on button state and visibility.
	 * 
	 * @param	E	Flash mouse event.
	 */
	protected void onMouseUp(MouseEvent E)
	{
		if(_overOpen && _pressingOpen)
			onOpen();
		else if(_overRecord && _pressingRecord)
		{
			//if(_stop.visible)
			//	onStop();
			//else if(_recordOn.visible)
			//	stopRecording();
			//else
			//	onRecord(!E.altKey);
		}
		//else if(_overRestart && _pressingRestart)
		//	onRestart(!E.altKey);
		else if(_overPause && _pressingPause)
		{
			//if(_play.visible)
			//	onPlay();
			//else
			//	onPause();
		}
		else if(_overStep && _pressingStep)
			onStep();
		
		unpress();
		checkOver();
		updateGUI();
	}

	//***MISC GUI MGMT STUFF***//

	/**
	 * This function checks to see what button the mouse is currently over.
	 * Has some special behavior based on whether a recording is happening or not.
	 * 
	 * @return	Whether the mouse was over any buttons or not.
	 */
	protected boolean checkOver()
	{
		_overOpen = _overRecord = _overRestart = _overPause = _overStep = false;
		/*
		 * if((mouseX < 0) || (mouseX > width) || (mouseY < 0) || (mouseY > 15))
		 * return false; if((mouseX >= _recordOff.x) && (mouseX <= _recordOff.x
		 * + _recordOff.width)) _overRecord = true; if(!_recordOn.visible &&
		 * !_overRecord) { if((mouseX >= _open.x) && (mouseX <= _open.x +
		 * _open.width)) _overOpen = true; else if((mouseX >= _restart.x) &&
		 * (mouseX <= _restart.x + _restart.width)) _overRestart = true; else
		 * if((mouseX >= _pause.x) && (mouseX <= _pause.x + _pause.width))
		 * _overPause = true; else if((mouseX >= _step.x) && (mouseX <= _step.x
		 * + _step.width)) _overStep = true; }
		 */
		return true;
	}

	/**
	 * Sets all the pressed state variables for the buttons to false.
	 */
	protected void unpress()
	{
		_pressingOpen = false;
		_pressingRecord = false;
		_pressingRestart = false;
		_pressingPause = false;
		_pressingStep = false;
	}

	/**
	 * Figures out what buttons to highlight based on the _overWhatever and _pressingWhatever variables.
	 */
	protected void updateGUI()
	{
		//if(_recordOn.visible)
		//{
		//	_open.alpha = _restart.alpha = _pause.alpha = _step.alpha = 0.35;
		//	_recordOn.alpha = 1.0;
		//	return;
		//}

		//if(_overOpen && (_open.alpha != 1.0))
		//	_open.alpha = 1.0;
		//else if(!_overOpen && (_open.alpha != 0.8))
		//	_open.alpha = 0.8;
		//if(_overRecord && (_recordOff.alpha != 1.0))
		//	_recordOff.alpha = _recordOn.alpha = _stop.alpha = 1.0;
		//else if(!_overRecord && (_recordOff.alpha != 0.8))
		//	_recordOff.alpha = _recordOn.alpha = _stop.alpha = 0.8;

		//if(_overRestart && (_restart.alpha != 1.0))
		//	_restart.alpha = 1.0;
		//else if(!_overRestart && (_restart.alpha != 0.8))
		//	_restart.alpha = 0.8;

		//if(_overPause && (_pause.alpha != 1.0))
		//	_pause.alpha = _play.alpha = 1.0;
		//else if(!_overPause && (_pause.alpha != 0.8))
		//	_pause.alpha = _play.alpha = 0.8;

		//if(_overStep && (_step.alpha != 1.0))
		//	_step.alpha = 1.0;
		//else if(!_overStep && (_step.alpha != 0.8))
		//	_step.alpha = 0.8;
	}
}
