package com.dazkins.triad.audio;

import javax.sound.sampled.*;

public class AudioFile {
	private Clip clip;
	
	public AudioFile(String path) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(AudioFile.class.getResource(path));
			clip = AudioSystem.getClip();
			clip.open(ais);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void play() {
		try {
			clip.stop();
			clip.setFramePosition(0);
			clip.start();
			clip.loop(3);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}