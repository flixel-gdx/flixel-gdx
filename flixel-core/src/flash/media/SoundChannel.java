package flash.media;

import flash.events.IEventDispatcher;

/**
 * The SoundChannel class controls a sound in an application.
 * 
 * @author Thomas Weston
 */
public interface SoundChannel extends IEventDispatcher
{
	/**
	 * The current amplitude (volume) of the left channel, from 0 (silent) to 1 (full amplitude).
	 *
	 * @return	The current amplitude of the left channel.
	 */
	public float getLeftPeak();
	
	/**
	 * The current amplitude (volume) of the right channel, from 0 (silent) to 1 (full amplitude).
	 *
	 * @return	The current amplitude of the right channel.
	 */
	public float getRightPeak();
	
	/**
	 * The SoundTransform object assigned to the sound channel.
	 * A SoundTransform object includes properties for setting volume, panning, left speaker assignment, and right speaker assignment.
	 * 
	 * @param	soundTransform	The SoundTransform to set.
	 */
	public void setSoundTransform(SoundTransform soundTransform);

	/**
	 * The SoundTransform object assigned to the sound channel.
	 * A SoundTransform object includes properties for setting volume, panning, left speaker assignment, and right speaker assignment.
	 * 
	 * @return	The SoundTransform object assigned to the sound channel.
	 */
	public SoundTransform getSoundTransform();

	/**
	 * Stops the sound playing in the channel.
	 */
	public void stop();

	/**
	 * Pauses the sound playing in the channel.
	 */
	public void pause();

	/**
	 * Resumes playing the sound in the channel.
	 */
	public void resume();
}
