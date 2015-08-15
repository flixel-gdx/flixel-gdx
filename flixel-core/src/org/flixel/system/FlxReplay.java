package org.flixel.system;

import org.flixel.FlxG;
import org.flixel.system.input.Input;
import org.flixel.system.replay.FrameRecord;
import org.flixel.system.replay.MouseRecord;

import com.badlogic.gdx.utils.Array;

/**
 * The replay object both records and replays game recordings,
 * as well as handle saving and loading replays to and from files.
 * Gameplay recordings are essentially a list of keyboard and mouse inputs,
 * but since Flixel is fairly deterministic, we can use these to play back
 * recordings of gameplay with a decent amount of fidelity.
 * 
 * @author Thomas Weston
 */
public class FlxReplay
{
	/**
	 * The random number generator seed value for this recording.
	 */
	public float seed;
	/**
	 * The current frame for this recording.
	 */
	public int frame;
	/**
	 * The number of frames in this recording.
	 */
	public int frameCount;
	/**
	 * Whether the replay has finished playing or not.
	 */
	public boolean finished;

	/**
	 * Internal container for all the frames in this replay.
	 */
	protected Array<FrameRecord> _frames;
	/**
	 * Internal tracker for max number of frames we can fit before growing the <code>_frames</code> again.
	 */
	protected int _capacity;
	/**
	 * Internal helper variable for keeping track of where we are in <code>_frames</code> during recording or replay.
	 */
	protected int _marker;

	/**
	 * Instantiate a new replay object.  Doesn't actually do much until you call create() or load().
	 */
	public FlxReplay()
	{
		seed = 0;
		frame = 0;
		frameCount = 0;
		finished = false;
		_frames = null;
		_capacity = 0;
		_marker = 0;
	}

	/**
	 * Clean up memory.
	 */
	public void destroy()
	{
		if(_frames == null)
			return;
		int i = frameCount-1;
		while(i >= 0)
			_frames.get(i--).destroy();
		_frames = null;
		
	}

	/**
	 * Create a new gameplay recording.  Requires the current random number generator seed.
	 * 
	 * @param	Seed	The current seed from the random number generator.
	 */
	public void create(float Seed)
	{
		destroy();
		init();
		seed = Seed;
		rewind();
	}

	/**
	 * Load replay data from a <code>String</code> object.
	 * Strings can come from embedded assets or external
	 * files loaded through the debugger overlay.
	 * 
	 * @param	FileContents	A <code>String</code> object containing a gameplay recording.
	 */
	public void load(String FileContents)
	{
		init();

		String[] lines = FileContents.split("\r?\n|\r");

		seed = Float.parseFloat(lines[0]);

		String line;
		int i = 1;
		int l = lines.length;
		while(i < l)
		{
			line = lines[i++];
			if(line.length() > 3)
			{
				_frames.add(new FrameRecord().load(line));
				frameCount++;
				if(frameCount >= _capacity)
				{
					_capacity *= 2;
					_frames.ensureCapacity(_capacity - _frames.size);
				}
			}
		}

		rewind();
	}

	/**
	 * Common initialization terms used by both <code>create()</code> and <code>load()</code> to set up the replay object.
	 */
	protected void init()
	{
		_capacity = 100;
		_frames = new Array<FrameRecord>(_capacity);
		frameCount = 0;
	}

	/**
	 * Save the current recording data off to a <code>String</code> object.
	 * Basically goes through and calls <code>FrameRecord.save()</code> on each frame in the replay.
	 * 
	 * @return	The gameplay recording in simple ASCII format.
	 */
	public String save()
	{
		if(frameCount <= 0)
			return null;
		String output = seed+"\n";
		int i = 0;
		while(i < frameCount)
			output += _frames.get(i++).save() + "\n";
		return output;
	}

	/**
	 * Get the current input data from the input managers and store it in a new frame record.
	 */
	public void recordFrame()
	{
		Array<Input.KeyData> keysRecord = FlxG.keys.record();
		MouseRecord mouseRecord = FlxG.mouse.record();
		if((keysRecord == null) && (mouseRecord == null))
		{
			frame++;
			return;
		}
		_frames.add(new FrameRecord().create(frame++,keysRecord,mouseRecord));
		frameCount++;
		if(frameCount >= _capacity)
		{
			_capacity *= 2;
			_frames.ensureCapacity(_capacity - _frames.size);
		}
	}

	/**
	 * Get the current frame record data and load it into the input managers.
	 */
	public void playNextFrame()
	{
		FlxG.resetInput();

		if(_marker >= frameCount)
		{
			finished = true;
			return;
		}
		if(_frames.get(_marker).frame != frame++)
			return;

		FrameRecord fr = _frames.get(_marker++);
		if(fr.keys != null)
			FlxG.keys.playback(fr.keys);
		if(fr.mouse != null)
			FlxG.mouse.playback(fr.mouse);
	}

	/**
	 * Reset the replay back to the first frame.
	 */
	public void rewind()
	{
		_marker = 0;
		frame = 0;
		finished = false;
	}
}
