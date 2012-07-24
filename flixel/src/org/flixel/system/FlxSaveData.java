package org.flixel.system;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

public class FlxSaveData
{
	protected Preferences _sharedObject;
	protected Json _json;
	
	public FlxSaveData(Preferences SharedObject)
	{
		_sharedObject = SharedObject;
		_json = new Json(JsonWriter.OutputType.minimal);
	}
	
	public void put(String Key, Object Value)
	{
		_sharedObject.putString(Key, _json.toJson(Value));
	}
	
	public <T> T get(String Key, Class<T> Type)
	{
		return _json.fromJson(Type, _sharedObject.getString(Key));
	}
}
