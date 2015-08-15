package org.flixel.system;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

/**
 * This represents the data object in <code>FlxSave</code>.
 * 
 * @author Thomas Weston
 */
public class FlxSaveData
{
	/**
	 * Internal reference to the SharedObject.
	 */
	protected Preferences _sharedObject;

	/**
	 * Internal, used for serialising Objects.
	 */
	protected Json _json;

	/**
	 * Creates a new <code>FlxSaveData</code>
	 * 
	 * @param	SharedObject	The Preferences to save to.
	 */
	public FlxSaveData(Preferences SharedObject)
	{
		_sharedObject = SharedObject;
		_json = new Json(JsonWriter.OutputType.json);
	}

	/**
	 * Add a value to the data.
	 * 
	 * @param	Key		A <code>String</code>, used to retrieve the object later.
	 * @param	Value	The object to store.
	 */
	public void put(String Key, Object Value)
	{
		_sharedObject.putString(Key, _json.toJson(Value));
	}

	/**
	 * Get a value from the shared object. Returns null if the key doesn't exist.
	 * 
	 * @param	Key		The object's key.
	 * @param	Type	The class of the object you are retrieving.
	 * 
	 * @return	The object.
	 */
	public <T> T get(String Key, Class<T> Type)
	{
		return _json.fromJson(Type, _sharedObject.getString(Key));
	}
}
