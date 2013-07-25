package flash.media;

import flash.events.IEventDispatcher;

/**
 * A single interface for sound channels.
 * 
 * @author Thomas Weston
 */
public interface SoundChannel extends IEventDispatcher
{
	/**
	 * The <code>SoundTransform</code> object assigned to the sound channel. A <code>SoundTransform</code> object 
	 * includes properties for setting volume, panning, left speaker assignment, and right speaker assignment.
	 *
	 * @param soundTransform	The <code>SoundTransform</code> to set.
	 */
	public void setSoundTransform(SoundTransform soundTransform);
	
	/**
	 * The <code>SoundTransform</code> object assigned to the sound channel. A <code>SoundTransform</code> object
	 * includes properties for setting volume, panning, left speaker assignment, and right speaker assignment.
	 *
	 * @return	The <SoundTransform</code> object assigned to the sound channel.
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
