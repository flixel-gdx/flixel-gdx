package org.flixel;

import org.flixel.event.AFlxSave;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
	
/**
 * A class to help automate and simplify save game functionality.
 * Basically a wrapper for the Flash SharedObject thing, but
 * handles some annoying storage request stuff too.
 * 
 * @author	Thomas Weston
 */
public class FlxSave
{
	static protected int SUCCESS = 0;
	static protected int PENDING = 1;
	static protected int ERROR = 2;
	
	/**
	 * The name of the local shared object.
	 * @default null
	 */
	public String name;
	/**
	 * The local shared object itself.
	 * @default null
	 */
	protected Preferences _sharedObject;
	
	/**
	 * Internal tracker for callback function in case save takes too long.
	 */
	protected AFlxSave _callback;
	/**
	 * Internal tracker for save object close request.
	 */
	protected boolean _closeRequested;
	/**
	 * Internal helper for serialising objects.
	 */
	protected Json _json;
	
	/**
	 * Blanks out the containers.
	 */
	public FlxSave()
	{
		destroy();
	}

	/**
	 * Clean up memory.
	 */
	public void destroy()
	{
		_sharedObject = null;
		name = null;
		_callback = null;
		_closeRequested = false;
		_json = null;
	}
	
	/**
	 * Automatically creates or reconnects to locally saved data.
	 * 
	 * @param	Name	The name of the object (should be the same each time to access old data).
	 * 
	 * @return	Whether or not you successfully connected to the save data.
	 */
	public boolean bind(String Name)
	{
		destroy();
		name = Name;
		
		_sharedObject = Gdx.app.getPreferences(name);
		_json = new Json(JsonWriter.OutputType.minimal);
		
		if (_sharedObject == null)
		{
			FlxG.log("ERROR: There was a problem binding to\nthe shared object data from FlxSave.");
			destroy();
			return false;
		}
		
		return true;
	}
	
	/**
	 * A way to safely call <code>flush()</code> and <code>destroy()</code> on your save file.
	 * Will correctly handle storage size popups and all that good stuff.
	 * If you don't want to save your changes first, just call <code>destroy()</code> instead.
	 *
	 * @param	MinFileSize		If you need X amount of space for your save, specify it here.
	 * @param	OnComplete		This callback will be triggered when the data is written successfully.
	 *
	 * @return	The result of result of the <code>flush()</code> call (see below for more details).
	 */
	public boolean close(int MinFileSize, AFlxSave OnComplete)
	{
		_closeRequested = true;
		return flush(MinFileSize,OnComplete);
	}
	
	/**
	 * A way to safely call <code>flush()</code> and <code>destroy()</code> on your save file.
	 * Will correctly handle storage size popups and all that good stuff.
	 * If you don't want to save your changes first, just call <code>destroy()</code> instead.
	 *
	 * @param	MinFileSize		If you need X amount of space for your save, specify it here.
	 *
	 * @return	The result of result of the <code>flush()</code> call (see below for more details).
	 */
	public boolean close(int MinFileSize)
	{
		return close(MinFileSize, null);
	}
	
	/**
	 * A way to safely call <code>flush()</code> and <code>destroy()</code> on your save file.
	 * Will correctly handle storage size popups and all that good stuff.
	 * If you don't want to save your changes first, just call <code>destroy()</code> instead.
	 *
	 * @return	The result of result of the <code>flush()</code> call (see below for more details).
	 */
	public boolean close()
	{
		return close(0, null);
	}

	/**
	 * Writes the local shared object to disk immediately.  Leaves the object open in memory.
	 *
	 * @param	MinFileSize		If you need X amount of space for your save, specify it here.
	 * @param	OnComplete		This callback will be triggered when the data is written successfully.
	 *
	 * @return	Whether or not the data was written immediately.  False could be an error OR a storage request popup.
	 */
	public boolean flush(int MinFileSize, AFlxSave OnComplete)
	{
		if(!checkBinding())
			return false;
		_callback = OnComplete;
		
		_sharedObject.flush();
		
		return onDone();
	}
	
	/**
	 * Writes the local shared object to disk immediately.  Leaves the object open in memory.
	 *
	 * @param	MinFileSize		If you need X amount of space for your save, specify it here.
	 *
	 * @return	Whether or not the data was written immediately.  False could be an error OR a storage request popup.
	 */
	public boolean flush(int MinFileSize)
	{
		return flush(MinFileSize, null);
	}
	
	/**
	 * Writes the local shared object to disk immediately.  Leaves the object open in memory.
	 *
	 * @return	Whether or not the data was written immediately.  False could be an error OR a storage request popup.
	 */
	public boolean flush()
	{
		return flush(0, null);	
	}
	
	/**
	 * Erases everything stored in the local shared object.
	 * Data is immediately erased and the object is saved that way,
	 * so use with caution!
	 * 
	 * @return	Returns false if the save object is not bound yet.
	 */
	public boolean erase()
	{
		if(!checkBinding())
			return false;
		_sharedObject.clear();
		return true;
	}
		
	/**
	 * Put a value into the shared object.
	 * 
	 * @param Key	A string, used to retrieve the object later.
	 * @param Value	The object to store.
	 */
	public void put(String Key, Object Value)
	{
		_sharedObject.putString(Key, _json.toJson(Value));
	}
	
	/**
	 * Get a value from the shared object. Returns null if
	 * the key doesn't exist.
	 * 
	 * @param Type	The class of the object you are retrieving.
	 * @param Key	The object's key.
	 * 
	 * @return	The object.
	 */
	public <T> T get(Class<T> Type, String Key)
	{
		if (!contains(Key))
			return null;

		return _json.fromJson(Type, _sharedObject.getString(Key));
	}
	
	/**
	 * Check whether or not a key exists in this shared object.
	 * 
	 * @param Key	The key to check.
	 * 
	 * @return	If the key exists.
	 */
	public boolean contains(String Key)
	{
		return _sharedObject.contains(Key);
	}
	
	/**
	 * Event handler for special case storage requests.
	 * Handles logging of errors and calling of callback.
	 *  
	 * @return	Whether the operation was a success or not.
	 */
	protected boolean onDone()
	{
		if(_callback != null)
			_callback.onComplete(true);
		if(_closeRequested)
			destroy();			
		return true;
	}
		
	/**
	 * Handy utility function for checking and warning if the shared object is bound yet or not.
	 * 
	 * @return	Whether the shared object was bound yet.
	 */
	protected boolean checkBinding()
	{
		if(_sharedObject == null)
		{
			FlxG.log("FLIXEL: You must call FlxSave.bind()\nbefore you can read or write data.");
			return false;
		}
		return true;
	}
}