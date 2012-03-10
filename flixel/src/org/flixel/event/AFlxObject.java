package org.flixel.event;

import org.flixel.FlxObject;

public abstract class AFlxObject
{
	public boolean onProcessCallback(FlxObject Object1, FlxObject Object2){return false;}
	
	public boolean onSeparateX(FlxObject Object1, FlxObject Object2){return false;}
	
	public boolean onSeparateY(FlxObject Object1, FlxObject Object2){return false;}

	public boolean overlapsWith(FlxObject object, FlxObject tile){return false;}
}
