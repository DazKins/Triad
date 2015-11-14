package com.dazkins.triad.game.gui.object;

import java.util.ArrayList;

import com.dazkins.triad.game.gui.Gui;
import com.dazkins.triad.gfx.Font;
import com.dazkins.triad.math.AABB;

public class GuiObjectTextBox extends GuiObject
{
	private GuiObjectBox box;
	
	private ArrayList<String> msg;
	private float x;
	private float y;
	
	public GuiObjectTextBox(Gui g, String m, float x, float y, float w, float h, int layer)
	{
		super(g, layer);
		
		this.x = x;
		this.y = y;
		
		msg = new ArrayList<String>();
		msg.add(m);
		
		box = new GuiObjectBox(g, x, y, w, h, layer);
		this.layer = layer;
	}
	
	public GuiObjectTextBox(Gui g, String m, float x, float y, float maxWidth, int layer) 
	{
		super(g, layer);
		
		this.x = x;
		this.y = y;
		
		msg = new ArrayList<String>();
		
		String[] ms = m.split(" ");
		
		int cLineLength = 0;
		
		String curString = "";
		
		int longLen = 0;
		
		for (int i = 0; i < ms.length; i++)
		{
			String s = ms[i];
			cLineLength += s.length() + 1;
			if (cLineLength * 16  >= maxWidth - 20)
			{
				msg.add(curString);
				if (curString.length() * 16 > longLen)
					longLen = curString.length() * 16;
				cLineLength = s.length() + 1;
				curString = s + " ";
				continue;
			}
			curString += s + " ";
		}
		
		if (longLen == 0)
			longLen = m.length() * 16;
		
		msg.add(curString);
		
		int h = 20 + (msg.size() - 1) * 16;
		
		box = new GuiObjectBox(g, x, y, longLen + 20, h, layer);
		
		this.layer = layer;
	}
	
	public AABB getBounds()
	{
		return box.getBounds();
	}
	
	public void render()
	{
		box.render();
		for (int i = 0; i < msg.size(); i++)
		{
			Font.drawString(msg.get(i), x + 10, (y + i * 16) - 5, layer * 0.001f + 0.0001f, 1.0f);
		}
	}
}