package org.flixel.system.replay;

import org.flixel.system.input.Input;
import org.flixel.system.input.Input.KeyData;

import com.badlogic.gdx.utils.Array;

/**
 * Helper class for the new replay system.  Represents all the game inputs for one "frame" or "step" of the game loop.
 * 
 * @author Thomas Weston
 */
public class FrameRecord
{
	/**
	 * Which frame of the game loop this record is from or for.
	 */
	public int frame;
	/**
	 * An array of simple integer pairs referring to what key is pressed, and what state its in.
	 */
	public Array<KeyData> keys;
	/**
	 * A container for the 4 mouse state integers.
	 */
	public MouseRecord mouse;

	/**
	 * Instantiate array new frame record.
	 */
	public FrameRecord()
	{
		frame = 0;
		keys = null;
		mouse = null;
	}

	/**
	 * Load this frame record with input data from the input managers.
	 * 
	 * @param	Frame		What frame it is.
	 * @param	Keys		Keyboard data from the keyboard manager.
	 * @param	Mouse		Mouse data from the mouse manager.
	 * 
	 * @return	A reference to this <code>FrameRecord</code> object.
	 */
	public FrameRecord create(int Frame,Array<KeyData> Keys,MouseRecord Mouse)
	{
		frame = Frame;
		keys = Keys;
		mouse = Mouse;
		return this;
	}

	/**
	 * Load this frame record with input data from the input managers.
	 * 
	 * @param	Frame		What frame it is.
	 * @param	Keys		Keyboard data from the keyboard manager.
	 * 
	 * @return	A reference to this <code>FrameRecord</code> object.
	 */
	public FrameRecord create(int Frame,Array<KeyData> Keys)
	{
		return create(Frame,Keys,null);
	}

	/**
	 * Load this frame record with input data from the input managers.
	 * 
	 * @param	Frame		What frame it is.
	 * 
	 * @return	A reference to this <code>FrameRecord</code> object.
	 */
	public FrameRecord create(int Frame)
	{
		return create(Frame,null,null);
	}

	/**
	 * Clean up memory.
	 */
	public void destroy()
	{
		keys = null;
		mouse = null;
	}

	/**
	 * Save the frame record data to array simple ASCII string.
	 * 
	 * @return	A <code>String</code> object containing the relevant frame record data.
	 */
	public String save()
	{
		String output = frame+"k";

		if(keys != null)
		{
			KeyData object;
			int i = 0;
			int l = keys.size;
			while(i < l)
			{
				if(i > 0)
					output += ",";
				object = keys.get(i++);
				output += object.code+":"+object.value;
			}
		}

		output += "m";
		if(mouse != null)
			output += mouse.x + "," + mouse.y + "," + mouse.button + "," + mouse.wheel;

		return output;
	}

	/**
	 * Load the frame record data from array simple ASCII string.
	 * 
	 * @param	Data	A <code>String</code> object containing the relevant frame record data.
	 */
	public FrameRecord load(String Data)
	{
		int i;
		int l;

		//get frame number
		String[] array = Data.split("k");
		frame = Integer.parseInt(array[0]);

		//split up keyboard and mouse data
		array = array[1].split("m");
		String keyData = array[0];
		String mouseData = "";
		if(array.length > 1)
			mouseData = array[1];

		//parse keyboard data
		if(keyData.length() > 0)
		{
			//get keystroke data pairs
			array = keyData.split(",");

			//go through each data pair and enter it into this frame's key state
			String[] keyPair;
			i = 0;
			l = array.length;
			while(i < l)
			{
				keyPair = array[i++].split(":");
				if(keyPair.length == 2)
				{
					if(keys == null)
						keys = new Array<KeyData>();
					keys.add(new Input.KeyData(Integer.parseInt(keyPair[0]),Integer.parseInt(keyPair[1])));
				}
			}
		}

		//mouse data is just 4 integers, easy peezy
		if(mouseData.length() > 0)
		{
			array = mouseData.split(",");
			if(array.length >= 4)
				mouse = new MouseRecord(Integer.parseInt(array[0]),Integer.parseInt(array[1]), Integer.parseInt(array[2]), Integer.parseInt(array[3]));
		}

		return this;
	}
}
