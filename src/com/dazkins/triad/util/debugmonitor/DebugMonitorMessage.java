package com.dazkins.triad.util.debugmonitor;

public class DebugMonitorMessage
{
	private String msg;
	private long startTimer;

	public DebugMonitorMessage(String s)
	{
		msg = s;
		startTimer = System.currentTimeMillis();
	}

	public String getMsg()
	{
		return msg;
	}

	public long getStartTime()
	{
		return startTimer;
	}
}