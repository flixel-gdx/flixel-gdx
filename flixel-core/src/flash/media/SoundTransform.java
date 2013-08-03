package flash.media;

/**
 * The <code>SoundTransform</code> class contains properties for volume and panning.
 * 
 * @author Thomas Weston
 */
public class SoundTransform
{
	/**
	 * The left-to-right panning of the sound, ranging from -1 (full pan left) to 1 (full pan right).
	 * A value of 0 represents no panning (balanced center between right and left)
	 */
	public float pan;
	/**
	 * The volume, ranging from 0 (silent) to 1 (full volume).
	 */
	public float volume;
	
	/**
	 * Creates a <code>SoundTransform</code> object.
	 * 
	 * @param vol		The volume, ranging from 0 (silent) to 1 (full volume)
	 * @param panning	The left-to-right panning of the sound, ranging from -1 (full pan left) to 1 (full pan right).
	 * 					A value of 0 represents no panning (balanced center between right and left)
	 */
	public SoundTransform(float vol, float panning)
	{
		volume = vol;
		pan = panning;
	}

	/**
	 * Creates a <code>SoundTransform</code> object.
	 * 
	 * @param vol		The volume, ranging from 0 (silent) to 1 (full volume)
	 */
	public SoundTransform(float vol) 
	{
		this(vol, 0f);
	}
	
	/**
	 * Creates a <code>SoundTransform</code> object.
	 */
	public SoundTransform()
	{
		this(1f, 0f);
	}
}
