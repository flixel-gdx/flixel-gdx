package flash.media;

/**
 * The SoundTransform class contains properties for volume and panning.
 * 
 * @author Thomas Weston
 */
public class SoundTransform
{
	/**
	 * The left-to-right panning of the sound, ranging from -1 (full pan left) to 1 (full pan right).
	 * A value of 0 represents no panning (balanced center between right and left).
	 */
	public float pan;
	/**
	 * The volume, ranging from 0 (silent) to 1 (full volume).
	 */
	public float volume;
	/**
	 * The pitch multiplier, 1 == default, >1 == faster, <1 == slower, the value has to be between 0.5 and 2.0.
	 */
	public float pitch;

	/**
	 * Creates a SoundTransform object.
	 * 
	 * @param	vol			The volume, ranging from 0 (silent) to 1 (full volume).
	 * @param	panning		The left-to-right panning of the sound, ranging from -1 (full pan left) to 1 (full pan right). A value of 0 represents no panning (balanced center between right and left).
	 * @param	pitching	The pitch multiplier, 1 == default, >1 == faster, <1 == slower, the value has to be between 0.5 and 2.0.
	 */
	public SoundTransform(float vol, float panning, float pitching)
	{
		volume = vol;
		pan = panning;
		pitch = pitching;
	}

	/**
	 * Creates a SoundTransform object.
	 * 
	 * @param	vol			The volume, ranging from 0 (silent) to 1 (full volume).
	 * @param	panning		The left-to-right panning of the sound, ranging from -1 (full pan left) to 1 (full pan right). A value of 0 represents no panning (balanced center between right and left).
	 */
	public SoundTransform(float vol, float panning)
	{
		this(vol, panning, 1f);
	}

	/**
	 * Creates a SoundTransform object.
	 * 
	 * @param	vol			The volume, ranging from 0 (silent) to 1 (full volume).
	 */
	public SoundTransform(float vol)
	{
		this(vol, 0f);
	}

	/**
	 * Creates a SoundTransform object.
	 */
	public SoundTransform()
	{
		this(1f, 0f);
	}
}
