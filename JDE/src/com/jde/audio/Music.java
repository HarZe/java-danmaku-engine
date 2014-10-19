package com.jde.audio;

/**
 * This Music class stores a playable sound which will loop, and there can be
 * only one music playing at the same time
 * 
 * @author HarZe (David Serrano)
 */
public class Music extends Audible {

	/**
	 * Basic constructor
	 * 
	 * @param filename
	 *            Name of the audio file (must be .ogg or .wav)
	 */
	public Music(String filename) {
		super(filename);
	}

	@Override
	public void play() {
		if (audio != null)
			audio.playAsMusic(pitch, gain, true);
	}

}
