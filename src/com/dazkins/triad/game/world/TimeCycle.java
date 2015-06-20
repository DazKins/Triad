package com.dazkins.triad.game.world;

import com.dazkins.triad.gfx.Color;
import com.dazkins.triad.util.debugmonitor.DebugMonitor;

public class TimeCycle {
	private static final int DAY = 60 * 60 * 10;
	private static final int HALFDAY = DAY / 2;
	
	private static final int TRANSTIME = 60 * 30;
	
	private int curTime;
	private World world;
	
	private Color dayLight = new Color(150, 150, 150);
	private Color nightLight = new Color(40, 40, 40);
	
	public TimeCycle(World w) {
		this.world = w;
	}
	
	public void tick() {
		int relTime = curTime % DAY;

		if (relTime >= HALFDAY - TRANSTIME / 2 && relTime <= HALFDAY + TRANSTIME / 2)
			world.setAmbientLight(Color.lerp(nightLight, dayLight, ((HALFDAY + TRANSTIME / 2) - relTime) / (float) TRANSTIME));
		else if (relTime >= DAY - TRANSTIME / 2)
			world.setAmbientLight(Color.lerp(nightLight, dayLight, (relTime - (DAY - TRANSTIME / 2)) / (float) TRANSTIME));
		else if (relTime <= TRANSTIME / 2)
			world.setAmbientLight(Color.lerp(nightLight, dayLight, 0.5f + (float) relTime / (float) TRANSTIME));
		else if (relTime < HALFDAY - TRANSTIME / 2)
			world.setAmbientLight(dayLight);
		else if (relTime > HALFDAY + TRANSTIME / 2)
			world.setAmbientLight(nightLight);
				
		DebugMonitor.setVariableValue("Time", getTime() + " (" + getRelTime() + ")" +  " (" + getDayOrNight() + ")");
		
		curTime++;
	}
	
	public int getTime() {
		return curTime;
	}
	
	public int getRelTime() {
		return curTime % DAY;
	}
	
	public String getDayOrNight() {
		int relTime = curTime % DAY;
		if (relTime < HALFDAY)
			return "day";
		else 
			return "night";
	}
}
