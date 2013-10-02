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
			if (cName.length() > 0) {
				cName += ".";
			}
			
			cName += name;
			cTime = System.nanoTime();
		}
	}
	
	public void endStamp() {
		if (enableProfiling) {
			cTime = System.nanoTime() - cTime;
			
			timeStamps.put(cName, cTime);
		}
	}
}