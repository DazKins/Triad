package com.dazkins.triad.audio;

import java.util.HashMap;
import java.util.Map;


public class SoundManager {
	private static Map<String, AudioFile> nameToAudio = new HashMap<String, AudioFile>();
	
	public static void registerSound(String file, String name) {
		AudioFile a = new AudioFile(file);
		nameToAudio.put(name, a);
	}
	
	public static AudioFile getAudio(String name) {
		return nameToAudio.get(name);
	}
}