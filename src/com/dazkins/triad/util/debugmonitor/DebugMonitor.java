package com.dazkins.triad.util.debugmonitor;

import java.util.ArrayList;
import java.util.List;

import com.dazkins.triad.game.gui.renderformat.RenderFormatManager;
import com.dazkins.triad.gfx.Color;
import com.dazkins.triad.gfx.TTF;
import com.dazkins.triad.gfx.Window;

public class DebugMonitor
{
	private static List<DebugMonitorMessage> messages = new ArrayList<DebugMonitorMessage>();

	private static List<Object> variables = new ArrayList<Object>();

	private static int messageShowTime = 2000;
	private static boolean enabled;
	
	private static Window win;

	public static void addMessage(String s)
	{
		if (enabled)
		{
			messages.add(new DebugMonitorMessage(s));
		}
	}

	public static void setVariableValue(String s, Object o)
	{
		if (enabled)
		{
			if (variables.contains(s))
			{
				int i = variables.indexOf(s) + 1;
				variables.remove(i);
				variables.add(i, o);
			} else
			{
				variables.add(s);
				variables.add(o);
			}
		}
	}
	
	public static void setWindow(Window w)
	{
		win = w;
	}

	public static void enable()
	{
		enabled = true;
	}

	public static void disable()
	{
		enabled = false;
	}

	public static void removeVariable(String s)
	{
		if (enabled)
		{
			variables.remove(variables.indexOf(s) + 1);
			variables.remove(s);
		}
	}

	public static void render()
	{
		if (enabled)
		{
			int yInc = 0;
			for (int i = 0; i < variables.size(); i += 2)
			{
				String msg = variables.get(i).toString() + ": " + variables.get(i + 1).toString();
				float len = RenderFormatManager.TEXT.getFont().getStringLength(msg);
				TTF.renderString(msg, win.getW() - len - 5, yInc * TTF.getLetterHeight(), 1.0f + i * 0.001f);
				yInc++;
			}

			for (int i = 0; i < messages.size(); i++)
			{
				DebugMonitorMessage s = messages.get(i);
				if (System.currentTimeMillis() - s.getStartTime() >= messageShowTime)
				{
					messages.remove(i);
					continue;
				}
				RenderFormatManager.TEXT.reset().setColour(new Color(200, 0, 0)).setA(1.0f);
				float len = RenderFormatManager.TEXT.getFont().getStringLength(s.getMsg());
				TTF.renderString(s.getMsg(), win.getW() - len - 5, yInc * TTF.getLetterHeight(), 1.0f);
				RenderFormatManager.TEXT.reset();
				yInc++;
			}
		}
	}
}