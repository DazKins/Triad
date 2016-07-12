package com.dazkins.triad.game.gui.object;

import java.util.ArrayList;

import org.omg.Messaging.SyncScopeHelper;

import com.dazkins.triad.game.gui.Gui;
import com.dazkins.triad.game.gui.renderformat.RenderFormatManager;
import com.dazkins.triad.game.gui.renderformat.TextAlign;
import com.dazkins.triad.gfx.OpenGLHelper;
import com.dazkins.triad.gfx.TTF;
import com.dazkins.triad.math.AABB;

public class GuiObjectTextBox extends GuiObjectBox
{
	private String rawMsg;
	private ArrayList<String> msg;
	
	private int maxLineCount;
	
	public GuiObjectTextBox(Gui g) 
	{
		super(g);
	}
	
	public void setMaxLineCount(int m)
	{
		maxLineCount = m;
	}
	
	public void setContent(String c)
	{
		this.rawMsg = c;
	}
	
	private TTF oldFont;
	
	public void render()
	{
		if (RenderFormatManager.TEXT.getFont() != oldFont)
			setupGraphics();
		
		super.render();
		for (int i = 0; i < msg.size(); i++)
		{
			float strLen = RenderFormatManager.TEXT.getFont().getStringLength(msg.get(i));
			
			float move = 0;
			TextAlign a = RenderFormatManager.TEXT.getAlign();
			if (a == TextAlign.CENTRE)
				move = (super.width - strLen) / 2.0f;
			
			TTF.renderStringWithFormating(msg.get(i), x + move + 3, y + height + 3 - (i + 1) * TTF.getLetterHeight(), layer * 0.001f + 0.0001f);
		}
		
		oldFont = RenderFormatManager.TEXT.getFont();
		
//		OpenGLHelper.renderReferencePoint(x, y);
	}
	
	public String getContent()
	{
		return rawMsg;
	}
	
	public void setupGraphics()
	{
		msg = new ArrayList<String>();
		
		String[] ms = rawMsg.split(" ");
		
		String curString = "";
		
		for (int i = 0; i < ms.length; i++)
		{
			if (maxLineCount > 0)
			{
				if (msg.size() >= maxLineCount)
				{
					break;
				}
			}
			
			String oldLine = curString;
			curString += ms[i] + " ";
			float curLineLength = RenderFormatManager.TEXT.getFont().getStringLength(curString);
			
			if (ms[i].equals("\\n"))
			{
				msg.add(oldLine);
				curString = "";
				continue;
			}
			if (curLineLength > width)
			{
				msg.add(oldLine);
				curString = ms[i] + " ";
			}
		}
		
		if (!curString.equals(""))
		{
			if (maxLineCount > 0)
			{
				if (msg.size() < maxLineCount)
					msg.add(curString);
			}
			else
				msg.add(curString);
		}
		
		height = msg.size() * TTF.getLetterHeight();
	}
}