package com.jde.audio;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 * This Audible class stores a playable sound
 * 
 * @author HarZe (David Serrano)
 */
public abstract class Audible {

	/** Name of the audio file */
	protected String filename;
	/** Resource data from audio file */
	protected Audio audio = null;
	/** Pitch of the sound */
	protected float pitch = 1.0f;
	/** Volume of the sound */
	protected float gain = 1.0f;

	/**
	 * Basic constructor
	 * 
	 * @param filename
	 *            Name of the audio file (must be .ogg or .wav)
	 */
	public Audible(String filename) {
		this.filename = filename;
	}

	public float getGain() {
		return gain;
	}

	public float getPitch() {
		return pitch;
	}

	/**
	 * Loads the audio file into OpenAL, ready to play
	 * 
	 * @throws IOException
	 * @throws LWJGLException
	 */
	public void load() throws IOException, LWJGLException {
		if (filename.endsWith(".ogg"))
			audio = AudioLoader.getAudio("OGG",
					ResourceLoader.getResourceAsStream(filename));

		else if (filename.endsWith(".wav"))
			audio = AudioLoader.getAudio("WAV",
					ResourceLoader.getResourceAsStream(filename));

		else
			throw new LWJGLException("Audio files MUST be .ogg or .wav");
	}

	/**
	 * Plays the sound
	 */
	public abstract void play();

	public void setGain(float gain) {
		this.gain = gain;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	/**
	 * Stops playing the sound
	 */
	public void stop() {
		if (audio != null)
			audio.stop();
	}

}
