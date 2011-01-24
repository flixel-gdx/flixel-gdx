package org.flixel;


/**
 * FlxMonitor is a simple class that aggregates and averages data.
 * Flixel uses this to display the framerate and profiling data
 * in the developer console.  It's nice for keeping track of
 * things that might be changing too fast from frame to frame.
 */
public class FlxMonitor
{
	
	/**
	 * Stores the requested size of the monitor array.
	 */
	protected int _size;
	/**
	 * Keeps track of where we are in the array.
	 */
	protected int _itr;
	/**
	 * An array to hold all the data we are averaging.
	 */
	protected float[] _data;

	/**
	 * Creates the monitor array and sets the size.
	 * 
	 * @param	Size	The desired size - more entries means a longer window of averaging.
	 * @param	Default	The default value of the entries in the array (0 by default).
	 */
	public FlxMonitor(int Size, float Default)
	{
		constructor(Size, Default);
	}
	
	public FlxMonitor(int Size)
	{
		constructor(Size, 0);
	}
	
	private void constructor(int Size, float Default)
	{
		_size = Size;
		if(_size <= 0)
			_size = 1;
		_itr = 0;
		_data = new float[Size];
		int i = 0;
		while(i < _size)
			_data[i++] = Default;
	}
	
	/**
	 * Adds an entry to the array of data.
	 * 
	 * @param	Data	The value you want to track and average.
	 */
	public void add(float Data)
	{
		_data[_itr++] = Data;
		if(_itr >= _size)
			_itr = 0;
	}
	
	
	/**
	 * Averages the value of all the numbers in the monitor window.
	 * 
	 * @return	The average value of all the numbers in the monitor window.
	 */
	public float average()
	{
		float sum = 0;
		int i = 0;
		while(i < _size)
			sum += _data[i++];
		return sum/_size;
	}

}
