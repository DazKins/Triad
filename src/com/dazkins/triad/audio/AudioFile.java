package com.dazkins.triad.audio;

import javax.sound.sampled.*;

import com.dazkins.triad.util.TriadLogger;

public class AudioFile
{
	private Clip clip;

	public AudioFile(String path)
	{
		try
		{
			AudioInputStream ais = AudioSystem.getAudioInputStream(AudioFile.class.getResource(path));
			clip = AudioSystem.getClip();
			clip.open(ais);
		} catch (Exception e)
		{
			TriadLogger.log(e.getMessage(), true);
		}
	}

	public void play()
	{
		try
		{
			clip.stop();
			clip.setFramePosition(0);
			clip.start();
			clip.loop(3);
		} catch (Exception e)
		{
			TriadLogger.log(e.getMessage(), true);
		}
	}
}