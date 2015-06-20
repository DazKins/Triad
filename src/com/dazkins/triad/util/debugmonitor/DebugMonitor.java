package com.dazkins.triad.util.debugmonitor;

import java.util.ArrayList;
import java.util.List;

import com.dazkins.triad.gfx.Font;

public class DebugMonitor {
	private static List<DebugMonitorMessage> messages = new ArrayList<DebugMonitorMessage>();
	private static int fps;
	private static int ups;
	
	private static int messageShowTime = 2000;
	
	public static void addMessage(String s) {
		messages.add(new DebugMonitorMessage(s));
	}
	
	public static void setFPS(int f) {
		fps = f;
	}
	
	public static void setUPS(int u) {
		ups = u;
	}
	
	public static void render() {
		Font.drawString("FPS: " + fps, 0, 0, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f);
		Font.drawString("UPS: " + ups, 0, 16, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f);
		for (int i = 0; i < messages.size(); i++) {
			DebugMonitorMessage s = messages.get(i);
			if (System.currentTimeMillis() - s.getStartTime() >= messageShowTime) {
				messages.remove(i);
				continue;
			}
			Font.drawString(s.getMsg(), 0, (i + 2) * 16, 0.7f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
		}
	}
}