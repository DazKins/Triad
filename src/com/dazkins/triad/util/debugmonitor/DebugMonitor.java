package com.dazkins.triad.util.debugmonitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dazkins.triad.gfx.Font;

public class DebugMonitor {
	private static List<DebugMonitorMessage> messages = new ArrayList<DebugMonitorMessage>();
	
	private static List<Object> variables = new ArrayList<Object>();
	
	private static int messageShowTime = 2000;
	private static boolean enabled;
	
	public static void addMessage(String s) {
		if (enabled) {
			messages.add(new DebugMonitorMessage(s));
		}
	}
	
	public static void setVariableValue(String s, Object o) {
		if (enabled) {
			if (variables.contains(s)) {
				int i = variables.indexOf(s) + 1;
				variables.remove(i);
				variables.add(i, o);
			} else {
				variables.add(s);
				variables.add(o);
			}
		}
	}
	
	public static void enable() {
		enabled = true;
	}
	
	public static void disable() {
		enabled = false;
	}
	
	public static void removeVariable(String s) {
		if (enabled) {
			variables.remove(variables.indexOf(s) + 1);
			variables.remove(s);
		}
	}
	
	public static void render() {
		if (enabled) {
			int yInc = 0;
			for (int i = 0; i < variables.size(); i += 2) {
				String msg = variables.get(i).toString() + ": " + variables.get(i + 1).toString();
				Font.drawString(msg, 0, yInc * 16, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f);
				yInc++;
			}
			
			for (int i = 0; i < messages.size(); i++) {
				DebugMonitorMessage s = messages.get(i);
				if (System.currentTimeMillis() - s.getStartTime() >= messageShowTime) {
					messages.remove(i);
					continue;
				}
				Font.drawString(s.getMsg(), 0, yInc * 16, 0.7f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
				yInc++;
			}
		}
	}
}