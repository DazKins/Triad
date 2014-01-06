package com.dazkins.triad.util;

import java.util.HashMap;
import java.util.Map;

public class TriadProfiler {
	public static TriadProfiler instance;
	
	static {
		instance = new TriadProfiler();
		instance.setProfiling(true);
	}
	
	private boolean enableProfiling;
	
	private Map<String, Long> timeStamps;
	
	private String cName;
	private long cTime;
	
	public TriadProfiler() {
		timeStamps = new HashMap<String, Long>();
	}
	
	public void setProfiling(boolean b) {
		enableProfiling = b;
	}
	
	public void startStamp(String name) {
		if (enableProfiling) {
			if (cName != null) {
				cName += ".";
				cName += name;
			} else {
				cName = name;
			}
			
			cTime = System.nanoTime();
		}
	}
	
	public void endStamp() {
		if (enableProfiling) {
			cTime = System.nanoTime() - cTime;
			
			timeStamps.put(cName, cTime);
			
//			int dotFromLeft = cName.length() - cName.lastIndexOf('.');
			int dotIndex = cName.lastIndexOf('.');
			if (dotIndex != -1)
				cName = cName.substring(dotIndex + 1);
		}
	}
	
	public void output() {
		System.out.println(cName + ": " + cTime);
	}
}